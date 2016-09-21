package es.usefulearnings.gui.controller;

import es.usefulearnings.entities.DownloadedData;
import es.usefulearnings.gui.Main;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.utils.ResourcesHelper;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Yago on 04/09/2016.
 */
public class HistoryController implements Initializable {
  public ListView<DownloadedData> downloadHistory;
  public BorderPane borderPane;
  public VBox downloadedSummary;
  public HBox notificationsBox;
  public Button reloadBtn;

  private List<DownloadedData> downloadedData;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    downloadedData = new LinkedList<>();
    ImageView imageView = new ImageView(new Image(Main.class.getResourceAsStream("icons/refresh.png")));
    reloadBtn.setGraphic(imageView);
    reloadBtn.setTooltip(new Tooltip("Reload data from disk"));
    notificationsBox.setPrefWidth(1024 - reloadBtn.getWidth());
    downloadedSummary.setPrefWidth(1024 * 0.3);
    recoverData();
  }

  private void recoverData() {
    new Thread(() -> {
      try {
        Platform.runLater(() -> {
          ProgressIndicator progressIndicator = new ProgressIndicator(-1);
          progressIndicator.getStyleClass().add("default-progress-indicator");
          Label progressLabel = new Label();
          progressLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
          progressLabel.setGraphic(progressIndicator);
          Tooltip tooltip = new Tooltip("Recovering data please wait...");
          tooltip.setPrefSize(150,300);
          progressIndicator.setTooltip(tooltip);
          progressLabel.setPrefSize(500, 500);
          borderPane.setCenter(progressLabel);
        });

        this.downloadedData = ResourcesHelper.getInstance().getDownloadedData();
        this.downloadHistory.setItems(FXCollections.observableArrayList(downloadedData));
        this.downloadHistory.getSelectionModel().selectedItemProperty().addListener(
          (observable, oldValue, newValue) -> {
            if(oldValue == null){
              showSummary(newValue);
            } else {
              if(oldValue != newValue){
                showSummary(newValue);
              }
            }
        });

        //Context Menu
        ContextMenu contextMenu = new ContextMenu();

        //Menu items
        MenuItem item = new MenuItem("Use this data");
        item.setOnAction(event -> {
          DownloadedData data = downloadHistory.getSelectionModel().getSelectedItem();
          // TODO: LOAD HERE DOWNLOAD RESULTS

          event.consume();
        });
        contextMenu.getItems().add(item);
        downloadHistory.setContextMenu(contextMenu);



        Platform.runLater(() -> {
          if(downloadHistory.getItems().size() < 1) {
            Label errorLabel = new Label("No downloads found.");
            errorLabel.setTextFill(Color.web("#ffffff"));
            errorLabel.setFont(new Font(20));
            notificationsBox.getChildren().clear();
            notificationsBox.getChildren().add(errorLabel);
            notificationsBox.setStyle("-fx-background-color: #bbac44;");
          } else {
            Label successLabel = new Label("Success");
            successLabel.setTextFill(Color.WHITE);
            successLabel.setFont(new Font(20));
            notificationsBox.getChildren().clear();
            notificationsBox.setStyle("-fx-background-color: #00ffff;");
            notificationsBox.getChildren().add(successLabel);
          }

          // Transition
          FadeTransition fadeTransition =
            new FadeTransition(Duration.millis(10000), notificationsBox);
          fadeTransition.setFromValue(1.0f);
          fadeTransition.setToValue(0.0f);
          fadeTransition.setCycleCount(1);
          fadeTransition.play();
          borderPane.setCenter(downloadHistory);
        });
      } catch (Exception e) {
        e.printStackTrace();
        Platform.runLater(() -> {
          Label errorLabel = new Label("!!!!ERROR!!!!");
          errorLabel.setTextFill(Color.web("#ffffff"));
          errorLabel.setFont(new Font(20));
          notificationsBox.getChildren().clear();
          notificationsBox.setStyle("-fx-background-color: #bb393e;");
          notificationsBox.getChildren().add(errorLabel);
          borderPane.setCenter(downloadHistory);
          e.printStackTrace();
          AlertHelper.showExceptionAlert(e);
        });
      }
    }).start();
  }

  private void showSummary(DownloadedData downloadedData) {
    downloadedSummary.getChildren().clear();
    Label dateLabel = new Label(downloadedData.toString());
    Label companiesFound = new Label("Companies found: " + downloadedData.getCompaniesFound().size());
    Label optionsFound = new Label("Options found: " + downloadedData.getOptionsFound().size());
    Label optionChains = new Label("Option chains found: " + downloadedData.getOptionsFound().size());
    downloadedSummary.getChildren().addAll(dateLabel, companiesFound, optionsFound, optionChains);
  }

  public void reload(ActionEvent event) {
    notificationsBox.setStyle("");
    notificationsBox.getChildren().clear();

    recoverData();
    event.consume();
  }
}

