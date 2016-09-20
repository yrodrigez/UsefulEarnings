package es.usefulearnings.gui.controller;

import es.usefulearnings.entities.DownloadedData;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.utils.ResourcesHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

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

  private List<DownloadedData> downloadedData;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    downloadedData = new LinkedList<>();
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
          progressLabel.setTooltip(tooltip);
          progressLabel.setPrefSize(500, 500);
          borderPane.setCenter(progressLabel);
        });

        downloadedData = ResourcesHelper.getInstance().getDownloadedData();
        downloadHistory.setItems(FXCollections.observableArrayList(downloadedData));

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
          borderPane.setCenter(downloadHistory);
        });
      } catch (Exception e) {
        e.printStackTrace();
        Platform.runLater(() -> {
          borderPane.setTop(new Label("!!!!ERROR!!!!"));
          e.printStackTrace();
          AlertHelper.showExceptionAlert(e);
        });
      }
    }).start();
  }
}

