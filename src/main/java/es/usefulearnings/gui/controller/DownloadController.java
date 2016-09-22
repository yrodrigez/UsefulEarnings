package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.connection.DownloadProcess;
import es.usefulearnings.engine.connection.ProcessHandler;
import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.entities.DownloadedData;
import es.usefulearnings.entities.Entity;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Yago on 04/09/2016.
 */
public class DownloadController implements Initializable {

  //FXML
  public VBox progressPane;
  public Button downloadCompaniesButton;
  public Button stopButton;
  public ListView listView;
  public BorderPane mainPane;

  //PRIVATE
  private ArrayList<DownloaderTask> tasks;
  private int downloadButtonLocker;
  private DownloadedData downloadedData;
  private final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;

  private class DownloaderTask extends Task<Void> {
    private DownloadProcess process;
    private ProcessHandler handler;

    DownloaderTask(ArrayList<Plugin> plugins, List<Entity> entities){
      handler = new ProcessHandler() {
        @Override
        public void updateProgress(int workDone, int remaining) {
          DownloaderTask.this.updateProgress(workDone, remaining);
        }

        @Override
        public void updateMessage(String message) {
          DownloaderTask.this.updateMessage(message);
        }

        @Override
        public void onCancelled() {
          cancel();
        }

        @Override
        public void onError(Throwable err) {
          Platform.runLater(()-> AlertHelper.showExceptionAlert(process.getError()));
          failed();
        }

        @Override
        public void onSuccess() {
          succeeded();
        }
      };

      process = new DownloadProcess(handler, plugins, entities);
    }


    @Override
    protected Void call() throws Exception {
      process.run();
      if(process.hasFailed()) {
        this.failed();
        super.failed();
        throw process.getError();
      }
      Core.getInstance().removeEntities(this.process.getEmptyEntities());
      downloadCompleted();
      return null;
    }

    void stop(){
      this.process.stop();
    }
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    stopButton.setDisable(true);
    downloadButtonLocker = 0;
  }

  public void downloadAction(ActionEvent event) {
    // start a download
    this.downloadAllCompaniesData();

    // instance the DownloadedData object
    this.downloadedData = new DownloadedData(new Date().getTime() / 1000L);
    // set a behavior
    progressPane.getChildren().setAll(this.getDownloadBehaviorNode());

    event.consume();
  }

  private Node getDownloadBehaviorNode() {
    VBox innerVBox = new VBox();
    innerVBox.setStyle("-fx-background-color: white");

    ScrollPane scrollPane = new ScrollPane(innerVBox);
    scrollPane.setStyle("-fx-background-color: white");
    Label activeCoresLabel = new Label();
    activeCoresLabel.textProperty().bind(new SimpleLongProperty(this.downloadButtonLocker).asString());
    HBox coresInfo = new HBox();
    coresInfo.getChildren().addAll(new Label("Active downloads: "), activeCoresLabel);
    innerVBox.getChildren().add(coresInfo);
    for (
      DownloaderTask task :
      this.tasks
      ) {
      Label label = new Label();
      label.textProperty().bind(task.messageProperty());
      // add the label with the msj property
      innerVBox.getChildren().add(label);

      ProgressBar bar = new ProgressBar();
      bar.setPrefWidth(280);
      bar.progressProperty().bind(task.progressProperty());
      bar.getStyleClass().add("default-progress-bar");

      ProgressIndicator progressIndicator = new ProgressIndicator();
      progressIndicator.setPrefSize(35, 35);
      progressIndicator.getStyleClass().add("default-progress-indicator");
      progressIndicator.progressProperty().bind(task.progressProperty());
      // add a new hBox with progress indicators
      HBox hBox = new HBox(bar, progressIndicator);
      innerVBox.getChildren().add(hBox);

      task.setOnSucceeded(onSuccess -> {
        bar.setStyle("-fx-accent: #00b900;");
        progressIndicator.setStyle("-fx-accent: #00b900;");
        bar.progressProperty().unbind();
        progressIndicator.progressProperty().unbind();
        onSuccess.consume();
      });

      task.setOnCancelled(onCancelled -> {
        bar.setStyle("-fx-accent: yellow;");
        bar.progressProperty().unbind();
        bar.setProgress(1);
        progressIndicator.setStyle("-fx-accent: yellow;");
        progressIndicator.progressProperty().unbind();
        progressIndicator.setProgress(1);
        onCancelled.consume();
      });

      task.setOnFailed(onFail -> {
        bar.setStyle("-fx-accent: red;");
        progressIndicator.setStyle("-fx-accent: red;");
        bar.progressProperty().unbind();
        progressIndicator.progressProperty().unbind();
        bar.setProgress(1);
        onFail.consume();
      });
    }

    downloadCompaniesButton.setText("Downloading");
    downloadCompaniesButton.setDisable(true);
    stopButton.setDisable(false);

    return scrollPane;
  }

  public void stopAction(ActionEvent event) {
    tasks.forEach(DownloaderTask::stop);
    downloadCompaniesButton.setText("Download");
    downloadCompaniesButton.setDisable(false);
    stopButton.setDisable(true);
    event.consume();
  }

  /**
   * Downloads companies's data from all stocks
   */
  private void downloadAllCompaniesData(){
    tasks = new ArrayList<>();

    List<Entity> allCompanies = new ArrayList<>(Core.getInstance().getAllCompanies().values());

    for(int i = 0; i < MAX_THREADS; i++){
      int from = i * (Core.getInstance().getAllCompanies().values().size() / MAX_THREADS);
      int to = from + (Core.getInstance().getAllCompanies().values().size() / MAX_THREADS);
      if(i == MAX_THREADS - 1) to = Core.getInstance().getAllCompanies().values().size();

      ArrayList<Plugin> plugins = Core.getInstance().getCompaniesPlugins();

      DownloaderTask task = new DownloaderTask(plugins, allCompanies.subList(from, to));
      this.tasks.add(task);

      Thread t = new Thread(task);
      t.setDaemon(true);
      t.setName("UsefulEarnings-DownloadProcess-" + i);
      t.start();

      downloadButtonLocker++;
    }
  }

  private void downloadCompleted(){

    if(--downloadButtonLocker == 0){
      new Thread(() -> {
        try {
          downloadedData.save(new File(ResourcesHelper.getInstance().getSearchesPath()));
        } catch(IOException | NoStocksFoundException e) {
          e.printStackTrace();
          Platform.runLater(() ->
            AlertHelper.showExceptionAlert(e));
        }
      }).start();

      downloadButtonLocker = MAX_THREADS;

      Platform.runLater(() -> {
        downloadCompaniesButton.setDisable(false);
        downloadCompaniesButton.setText("Download");
        stopButton.setDisable(true);
      });
    }
  }
}
