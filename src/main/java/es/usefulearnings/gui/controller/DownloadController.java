package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.entities.DownloadedData;
import es.usefulearnings.entities.Entity;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

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
  public VBox debuggerBox;
  public BorderPane mainPane;
  public ScrollPane debuggerScrollPane;
  public CheckBox showDebug;
  public Rectangle overAllProgressbar;
  public VBox overallProgressBarContainer;
  public Label overAllProgressText;

  //PRIVATE
  private ArrayList<DownloaderTask> tasks;
  private DownloadedData downloadedData;
  private final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;
  private Label activeDownloadsLabel;
  private long active_downloads = 0;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    stopButton.setDisable(true);
    showDebug.selectedProperty().addListener((observable, oldValue, newValue) -> {

      if (newValue) {
        debuggerScrollPane.setContent(debuggerBox);
      } else {
        debuggerScrollPane.setContent(null);
      }
    });
  }

  public void downloadAction(final ActionEvent event) {
    // instance the DownloadedData object
    this.downloadedData = new DownloadedData(new Date().getTime() / 1000L);

    // start a download
    this.downloadAllCompaniesData();

    // set a behavior
    progressPane.getChildren().setAll(getDownloadBehaviorNode());

    event.consume();
  }

  private HBox downloaderTaskView(final DownloaderTask task) {
    final ProgressBar bar = new ProgressBar();
    bar.setPrefWidth(280);
    bar.progressProperty().bind(task.progressProperty());
    bar.getStyleClass().add("default-progress-bar");

    final ProgressIndicator progressIndicator = new ProgressIndicator();
    progressIndicator.setPrefSize(35, 35);
    progressIndicator.getStyleClass().add("default-progress-indicator");
    progressIndicator.progressProperty().bind(task.progressProperty());
    // add a new hBox with progress indicators

    task.setOnSucceeded(onSuccess -> setProgressBarStatus(bar, progressIndicator, onSuccess));
    task.setOnCancelled(onCancelled -> setProgressBarOnCancelled(bar, progressIndicator, onCancelled));
    task.setOnFailed(onFail -> setProgressBarOnError(bar, progressIndicator, onFail));

    return new HBox(bar, progressIndicator);
  }

  private Node getDownloadBehaviorNode() {
    final VBox innerVBox = new VBox();
    innerVBox.setStyle("-fx-background-color: white");

    final ScrollPane scrollPane = new ScrollPane(innerVBox);
    scrollPane.setStyle("-fx-background-color: white");
    activeDownloadsLabel = new Label("Active downloads: " + this.active_downloads);

    final HBox coresInfo = new HBox();
    coresInfo.getChildren().addAll(activeDownloadsLabel);
    innerVBox.getChildren().add(coresInfo);

    this.tasks.forEach(task -> {
      final Label label = new Label();
      label.textProperty().bind(task.messageProperty());
      innerVBox.getChildren().add(label);

      innerVBox.getChildren().add(downloaderTaskView(task));
    });

    disableDownloads();
    return scrollPane;
  }

  private void disableDownloads() {
    downloadCompaniesButton.setText("Downloading");
    downloadCompaniesButton.setDisable(true);
    stopButton.setDisable(false);
  }

  public static void setProgressBarOnCancelled(final ProgressBar bar, final ProgressIndicator progressIndicator, final WorkerStateEvent onCancelled) {
    bar.setStyle("-fx-accent: yellow;");
    bar.progressProperty().unbind();
    bar.setProgress(1);
    progressIndicator.setStyle("-fx-accent: yellow;");
    progressIndicator.progressProperty().unbind();
    progressIndicator.setProgress(1);
    onCancelled.consume();
  }

  public static void setProgressBarOnError(final ProgressBar bar, final ProgressIndicator progressIndicator, final WorkerStateEvent onFail) {
    bar.setStyle("-fx-accent: red;");
    progressIndicator.setStyle("-fx-accent: red;");
    bar.progressProperty().unbind();
    progressIndicator.progressProperty().unbind();
    bar.setProgress(1);
    onFail.consume();
  }

  public static void setProgressBarStatus(final ProgressBar bar, final ProgressIndicator progressIndicator, final WorkerStateEvent onSuccess) {
    bar.setStyle("-fx-accent: #00b900;");
    progressIndicator.setStyle("-fx-accent: #00b900;");
    bar.progressProperty().unbind();
    progressIndicator.progressProperty().unbind();
    onSuccess.consume();
  }

  public void stop(final ActionEvent event) {
    tasks.forEach(DownloaderTask::stop);
    downloadCompaniesButton.setText("Download");
    downloadCompaniesButton.setDisable(false);
    stopButton.setDisable(true);
    event.consume();
  }


  /**
   * Downloads companies's data from all stocks
   */
  private void downloadAllCompaniesData() {
    final Core core = Core.getInstance();
    core.setDataLoaded(false);
    tasks = new ArrayList<>();
    JSONHTTPClient.getInstance().clearCache();

    final List<Entity> allCompanies = new ArrayList<>(Core.getInstance().getAllCompanies().values());
    final OverallProgressBarHandler overallProgressBarHandler = new OverallProgressBarHandler(this, overAllProgressText, allCompanies.size());

    for (int i = 0; i < MAX_THREADS; i++) {
      this.active_downloads = MAX_THREADS;
      int from = i * (Core.getInstance().getAllCompanies().values().size() / MAX_THREADS);
      int to = from + (Core.getInstance().getAllCompanies().values().size() / MAX_THREADS);
      if (i == MAX_THREADS - 1) to = Core.getInstance().getAllCompanies().values().size();

      final ArrayList<Plugin> plugins = Core.getInstance().getCompanyPlugins();

      final DownloaderTask task = new DownloaderTask(this, plugins, allCompanies.subList(from, to), overallProgressBarHandler, "UsefulEarnings-DownloadProcess-" + i);
      tasks.add(task);
    }

    this.startDownload(tasks);
  }

  private void startDownload(final ArrayList<DownloaderTask> tasks) {
    Core.getInstance()
      .invokeAll(tasks);
  }

  private void saveOnDisk() {
    try {
      JSONHTTPClient.getInstance().clearCache();
      downloadedData.save(new File(ResourcesHelper.getInstance().getSearchesPath()));
      Core.getInstance().setDataLoaded(true);
    } catch (IOException | NoStocksFoundException e) {
      e.printStackTrace();
      Platform.runLater(() ->
        AlertHelper.showExceptionAlert(e));
    }
  }

  private void enableDownloadButton() {
    Platform.runLater(() -> {
      downloadCompaniesButton.setDisable(false);
      downloadCompaniesButton.setText("Download");
      stopButton.setDisable(true);
    });
  }

  synchronized void downloadCompleted() {
    this.active_downloads -= 1;
    if (this.active_downloads <= 0) {
      this.saveOnDisk();
      this.enableDownloadButton();
    }
    Platform.runLater(() -> activeDownloadsLabel.setText("Active downloads: " + this.active_downloads));
  }
}
