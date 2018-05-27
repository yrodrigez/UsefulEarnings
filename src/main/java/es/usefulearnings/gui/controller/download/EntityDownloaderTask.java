package es.usefulearnings.gui.controller.download;

import es.usefulearnings.engine.connection.DownloadProcess;
import es.usefulearnings.engine.connection.ProcessHandler;
import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.entities.Entity;
import es.usefulearnings.gui.controller.DownloadController;
import es.usefulearnings.gui.view.AlertHelper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class EntityDownloaderTask extends Task<Void> {
  private DownloadProcess process;
  private ProcessHandler handler;

  EntityDownloaderTask(ArrayList<Plugin> plugins, List<Entity> entities) {
    handler = new ProcessHandler() {
      @Override
      public void updateProgress(int workDone, int remaining) {
        EntityDownloaderTask.this.updateProgress(workDone, remaining);
      }

      @Override
      public void updateMessage(String message) {
        EntityDownloaderTask.this.updateMessage(message);
      }

      @Override
      public void onCancelled() {
        cancel();
      }

      @Override
      public void onError(Throwable err) {
        Platform.runLater(() -> AlertHelper.showExceptionAlert(process.getError()));
        failed();
      }

      @Override
      public void onSuccess() {
        updateMessage("Saving data");
        // downloadCompleted();
        updateMessage("Work done!");
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
    return null;
  }

  void stop(){
    this.process.stop();
  }

  public Node getSkin() {
    VBox innerVBox = new VBox();
    Label label = new Label();
    label.textProperty().bind(this.messageProperty());
    // add the label with the msj property
    innerVBox.getChildren().add(label);

    ProgressBar bar = new ProgressBar();
    bar.setPrefWidth(280);
    bar.progressProperty().bind(this.progressProperty());
    bar.getStyleClass().add("default-progress-bar");

    ProgressIndicator progressIndicator = new ProgressIndicator();
    progressIndicator.setPrefSize(35, 35);
    progressIndicator.getStyleClass().add("default-progress-indicator");
    progressIndicator.progressProperty().bind(this.progressProperty());
    // add a new hBox with progress indicators
    HBox hBox = new HBox(bar, progressIndicator);
    innerVBox.getChildren().add(hBox);

    this.setOnSucceeded(onSuccess -> {
      DownloadController.setProgressBarStatus(bar, progressIndicator, onSuccess);
    });

    this.setOnCancelled(onCancelled -> {
      DownloadController.setProgressBarOnCancelled(bar, progressIndicator, onCancelled);
    });

    this.setOnFailed(onFail -> {
      DownloadController.setProgressBarOnError(bar, progressIndicator, onFail);
    });
    return innerVBox;
  }
}

