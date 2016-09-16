package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.connection.DownloadProcess;
import es.usefulearnings.engine.connection.ProcessHandler;
import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.entities.Company;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Yago on 04/09/2016.
 */
public class DownloadController implements Initializable {
  public VBox progressPane;
  public Button downloadCompaniesButton;
  public Button stopButton;
  public ListView listView;
  public BorderPane mainPane;

  private ArrayList<Company> companiesToDownload;
  private ArrayList<DownloaderTask<Company>> companiesTasks;
  private final int MAX_THREADS = Core.getInstance().MAX_THREADS;

  private class DownloaderTask<E> extends Task<Void>{
    DownloadProcess<E> process;
    ProcessHandler handler;

    DownloaderTask(ArrayList<Plugin> plugins, List<E> companies){
      this.handler = new ProcessHandler() {
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
          DownloaderTask.super.cancelled();
          DownloaderTask.this.cancel();
          updateMessage("Cancelled!");
        }

        @Override
        public void onError(Throwable err) throws Throwable {
          DownloaderTask.super.failed();
          updateMessage("Failed!");
          throw err;
        }
      };
      process = new DownloadProcess<>(this.handler, plugins, companies);
    }

    @Override
    protected Void call() throws Exception {
      process.run();
      return null;
    }

    void stop(){
      this.process.stop();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    companiesToDownload = Core.getInstance().getAllCompanies();
    companiesTasks = new ArrayList<>();
    stopButton.setDisable(true);
  }

  public void downloadAction(ActionEvent event) {
    progressPane.getChildren().remove(0, progressPane.getChildren().size());

    for(int i = 0; i < MAX_THREADS; i++){
      int from = i * (companiesToDownload.size() / MAX_THREADS);
      int to = from + (companiesToDownload.size() / MAX_THREADS);
      if(i == MAX_THREADS -1) to = companiesToDownload.size();
      companiesTasks.add(
        new DownloaderTask<>(
          Core.getInstance().getCompaniesPlugins(),
          companiesToDownload.subList(from, to)
        ));
    }

    for(int i = 0; i < MAX_THREADS; i++){
      Core.getInstance().getAvailableThreads()[i] = new Thread(companiesTasks.get(i));
      Core.getInstance().getAvailableThreads()[i].setName("DownloaderTask - " + i);
      Core.getInstance().getAvailableThreads()[i].setDaemon(true);
      Core.getInstance().getAvailableThreads()[i].start();
    }

    // set the behavior
    VBox scrollable = new VBox();
    scrollable.setStyle("-fx-background-color: white");
    ScrollPane scrollbar = new ScrollPane(scrollable);
    scrollbar.setStyle("-fx-background-color: white");
    progressPane.getChildren().add(scrollbar);
    for (
      DownloaderTask<Company> task :
      companiesTasks
      ) {
      Label label = new Label();
      label.textProperty().bind(task.messageProperty());
      scrollable.getChildren().add(label);

      ProgressBar bar = new ProgressBar();
      bar.setPrefWidth(275);
      bar.progressProperty().bind(task.progressProperty());
      bar.getStyleClass().add("default-progress-bar");
      ProgressIndicator pi = new ProgressIndicator();
      pi.setPrefSize(40, 40);
      pi.getStyleClass().add("default-progress-indicator");

      pi.progressProperty().bind(task.progressProperty());

      HBox hBox = new HBox(bar, pi);
      scrollable.getChildren().add(hBox);

      task.setOnSucceeded(onSuccess -> {
        bar.setStyle("-fx-accent: #00b900;");
        pi.setStyle("-fx-accent: #00b900;");
        bar.progressProperty().unbind();
        pi.progressProperty().unbind();
        onSuccess.consume();
      });

      task.setOnCancelled(onCancelled -> {
        bar.setStyle("-fx-accent: yellow;");
        pi.setStyle("-fx-accent: yellow;");
        bar.progressProperty().unbind();
        pi.progressProperty().unbind();
        onCancelled.consume();
      });

      task.setOnFailed(onFail -> {
        bar.setStyle("-fx-accent: red;");
        pi.setStyle("-fx-accent: red;");
        bar.progressProperty().unbind();
        pi.progressProperty().unbind();
        bar.setProgress(-1);
        onFail.consume();
      });

      downloadCompaniesButton.setText("Downloading");
      downloadCompaniesButton.setDisable(true);
      stopButton.setDisable(false);
      event.consume();
    }
  }

  public void stopAction(ActionEvent event) {
    companiesTasks.forEach(DownloaderTask::stop);
    downloadCompaniesButton.setText("Download");
    downloadCompaniesButton.setDisable(false);
    stopButton.setDisable(true);
    event.consume();
  }
}
