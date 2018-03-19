package es.usefulearnings.gui.controller.download;

import es.usefulearnings.entities.DownloadedData;
import es.usefulearnings.gui.animation.OverWatchLoader;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.utils.ResourcesHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class NewDownloadController implements Initializable {
  public ListView<DownloadedData> historyList;
  public BorderPane metadataPane;
  public BorderPane mainPane;
  public SplitPane splitPane;

  private List<DownloadedData> downloadedData;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeHistoryList();
  }

  private void initializeHistoryList() {
    recoverData();
    final ContextMenu contextMenu = new ContextMenu();
    MenuItem refreshList = new MenuItem("Refresh list");
    contextMenu.getItems().add(refreshList);
    refreshList.setOnAction(event -> recoverData());
    historyList.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      if(event.getButton().equals(MouseButton.SECONDARY)){
        contextMenu.show(historyList, event.getScreenX(),event.getScreenY());
        event.consume();
      }
    });
  }

  private void recoverData() {
    new Thread(() -> {
      final OverWatchLoader loader = new OverWatchLoader(Color.web("#400090"));
      try {
        Platform.runLater(() -> {
          splitPane.getItems().remove(historyList);
          splitPane.getItems().add(0, loader.getLoader());
        });

        this.downloadedData = ResourcesHelper.getInstance().getDownloadedData();
        this.downloadedData.sort(DownloadedData::compareTo);
        this.historyList.setItems(FXCollections.observableArrayList(downloadedData));
        this.historyList.getSelectionModel().selectedItemProperty().addListener(
          (observable, oldValue, newValue) -> {
            /*if (oldValue == null) {
              showSummary(newValue);
            } else {
              if (oldValue != newValue) {
                showSummary(newValue);
              }
            }*/
          });

        Platform.runLater(() -> {
          splitPane.getItems().remove(loader.getLoader());
          splitPane.getItems().add(0, historyList);
        });
      } catch (Exception e) {
        e.printStackTrace();
        Platform.runLater(() ->
          AlertHelper.showExceptionAlert(e)
        );
      }
    }).start();
  }

}
