package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.entities.DownloadedData;
import es.usefulearnings.gui.Main;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.entities.EntitiesPackage;
import es.usefulearnings.gui.animation.OverWatchLoader;
import es.usefulearnings.utils.ResourcesHelper;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

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
          tooltip.setPrefSize(150, 20);
          progressIndicator.setTooltip(tooltip);
          progressLabel.setPrefSize(500, 500);
          borderPane.setCenter(progressLabel);
        });

        this.downloadedData = ResourcesHelper.getInstance().getDownloadedData();
        this.downloadedData.sort(DownloadedData::compareTo);
        this.downloadHistory.setItems(FXCollections.observableArrayList(downloadedData));
        this.downloadHistory.getSelectionModel().selectedItemProperty().addListener(
          (observable, oldValue, newValue) -> {
            if (oldValue == null) {
              showSummary(newValue);
            } else {
              if (oldValue != newValue) {
                showSummary(newValue);
              }
            }
          });

        Platform.runLater(() -> {
          if (downloadHistory.getItems().size() < 1) {
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
    Platform.runLater(() -> {
      // clear all nodes
      downloadedSummary.getChildren().clear();

      // Create the view for DownloadedData
      Label dateLabel = new Label(downloadedData.toString());
      Label companiesFound = new Label("Companies found: " + downloadedData.get_totalSavedCompanies());

      Button delete = new Button("", new ImageView(new Image(Main.class.getResourceAsStream("icons/delete-forever-white.png"))));
      delete.getStyleClass().addAll("history-button");

      Button reloadData = new Button("", new ImageView(new Image(Main.class.getResourceAsStream("icons/import-data-white.png"))));
      reloadData.setMinSize(45, 45);
      reloadData.getStyleClass().addAll("history-button", "no-opacity");

      Node okGraphic = new ImageView(new Image(Main.class.getResourceAsStream("icons/ok.png")));
      long coreLoadedPackage = Core.getInstance().getLoadedPackageId();
      if (coreLoadedPackage == downloadedData.getCreated()) {
        reloadData.setDisable(true);
        reloadData.setGraphic(okGraphic);
      }

      reloadData.setOnAction(event -> {
        ProgressIndicator pi = new ProgressIndicator();
        pi.setPrefSize(35.5, 35.5);
        pi.getStyleClass().addAll("history-data-to-core-process");
        pi.setProgress(-1);

        Tooltip tooltip = new Tooltip("Uploading data to system...");
        reloadData.setTooltip(tooltip);
        reloadData.setDisable(true);
        reloadData.setGraphic(new OverWatchLoader(5.0, Color.WHITE).getLoader());

        delete.setDisable(true);

        new Thread(() -> {
          // restore the files to the core
          Core.getInstance().setDataLoaded(false);// IS LOADING
          if (downloadedData.getEntitiesFile().listFiles() != null) {
            for (File f : downloadedData.getEntitiesFile().listFiles()) {
              try {
                if (f.getName().endsWith(EntitiesPackage.EXTENSION)) {
                  FileInputStream fileIn = new FileInputStream(f);
                  ObjectInputStream in = new ObjectInputStream(fileIn);
                  EntitiesPackage entitiesPackage = (EntitiesPackage) in.readObject();
                  in.close();
                  fileIn.close();

                  Core.getInstance().setFromEntitiesPackage(entitiesPackage);
                }
              } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> AlertHelper.showExceptionAlert(e));
              }
            }
          }
          // restored

          // ALWAYS (when done): restore buttons to re-use
          Platform.runLater(() -> {
            reloadData.setTooltip(null);
            reloadData.setGraphic(okGraphic);

            delete.setDisable(false);
          });
        }).start();

        event.consume();
      }); // end setOnAction


      // Paint it
      HBox buttonsBox = new HBox(reloadData, delete);
      buttonsBox.setSpacing(10);
      downloadedSummary.getChildren().addAll(dateLabel, companiesFound, buttonsBox);
    });
  }


  public void reload(ActionEvent event) {
    notificationsBox.setStyle("");
    notificationsBox.getChildren().clear();

    recoverData();
    event.consume();
  }
}

