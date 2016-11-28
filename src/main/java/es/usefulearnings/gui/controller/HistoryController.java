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
import javafx.geometry.Pos;
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
import org.controlsfx.control.Notifications;

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
          borderPane.setCenter(new OverWatchLoader(Color.web("#400090")));
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

        Platform.runLater(() -> borderPane.setCenter(downloadHistory));
      } catch (Exception e) {
        e.printStackTrace();
        Platform.runLater(() ->
          AlertHelper.showExceptionAlert(e)
        );
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
        Tooltip tooltip = new Tooltip("Uploading data to system...");
        reloadData.setTooltip(tooltip);
        reloadData.setDisable(true);
        reloadData.setGraphic(new OverWatchLoader(6d, Color.WHITE).getLoader());

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
                  Platform.runLater(() -> {
                      Notifications.create()
                        .title("Upload Completed")
                        .text("Successfully uploaded " + entitiesPackage.getCompanies().size() + " companies to the program! \nHappy filtering")
                        .graphic(new ImageView(new Image(Main.class.getResourceAsStream("icons/ok-notification.png"), 48d, 48d, false, true)))
                        .position(Pos.BOTTOM_RIGHT)
                        .show();
                    }
                  );

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

