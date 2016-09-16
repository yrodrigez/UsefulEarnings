package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.SearchEngine;
import es.usefulearnings.engine.connection.DownloaderTask;
import es.usefulearnings.entities.Company;
import es.usefulearnings.gui.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    stopButton.setDisable(true);
  }

  public void downloadAction(ActionEvent event) {
    // begin a search
    SearchEngine.getInstance().getAllCompaniesData();
    // remove all progress pane nodes
    progressPane.getChildren().remove(0, progressPane.getChildren().size());
    // set the behavior
    VBox scrollable = new VBox();
    scrollable.setStyle("-fx-background-color: white");
    ScrollPane scrollbar = new ScrollPane(scrollable);
    scrollbar.setStyle(scrollable.getStyle());
    progressPane.getChildren().add(scrollbar);
    for (
      DownloaderTask<Company> task :
      SearchEngine.getInstance().getCompaniesDownloaders()
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
        hBox.getChildren().remove(pi);
        hBox.getChildren().add(new ImageView(new Image(Main.class.getResourceAsStream("ok-green.png"))));
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
    SearchEngine.getInstance().getCompaniesDownloaders().forEach(DownloaderTask::stop);
    downloadCompaniesButton.setText("Download");
    downloadCompaniesButton.setDisable(false);
    stopButton.setDisable(true);
    event.consume();
  }
}
