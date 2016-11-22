package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.filter.Filter;
import es.usefulearnings.gui.Main;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.gui.view.CompanyViewHelper;
import es.usefulearnings.gui.view.FilterView;
import es.usefulearnings.gui.view.FilterViewHelper;
import es.usefulearnings.utils.CSVWriter;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.gui.animation.OverWatchLoader;
import es.usefulearnings.utils.ResourcesHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Yago on 04/09/2016.
 */
public class FilterController implements Initializable {
  public BorderPane mainPane;
  private ListView<Filter> filterListView;
  public BorderPane rightPane;
  private FilterView companyFilterView;

  @Override
  public void initialize(URL location, ResourceBundle resources){

    filterListView = new ListView<>();
    filterListView.prefHeightProperty().bind(mainPane.heightProperty());

    mainPane.setCenter(new OverWatchLoader(Color.web("#400090")).getLoader());
    BorderPane centerPane = new BorderPane();

    new Thread(()-> {
      try {
        companyFilterView = CompanyViewHelper.getInstance().getFilterableView();
        Platform.runLater(() -> {
          Node filterView = companyFilterView.getView();
          centerPane.setCenter(filterView);
          mainPane.setCenter(centerPane);
          Button filterButton = new Button("Filter");
          filterButton.getStyleClass().addAll("main-controller-button");
          HBox hBox = new HBox(filterButton);
          hBox.setAlignment(Pos.BASELINE_RIGHT);
          centerPane.setBottom(hBox);
          refreshFilterPane();
          filterButton.setOnAction(getFilterEvent(filterButton));
        });
      } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
        Platform.runLater(() -> AlertHelper.showExceptionAlert(e));
        e.printStackTrace();
      }
    }).start();

  }

  private void refreshFilterPane() {
    if(Core.getInstance().getAppliedFilters().size() > 0) {
      rightPane.setCenter(filterListView);
      List<Filter> appliedFilters = Core.getInstance().getAppliedFilters();
      filterListView.setItems(FXCollections.observableArrayList(appliedFilters));
      filterListView.setCellFactory(param -> {
        ListCell<Filter> filterListCell = new ListCell<>();

        ContextMenu filterContextMenu = new ContextMenu();
        MenuItem export = new MenuItem("Export to Excel", new ImageView(new Image(Main.class.getResourceAsStream("icons/export.png"), 12, 12, false, false)));
        export.setOnAction(event -> {
          try {
            String dateString = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date( filterListCell.getItem().getFilteredDate() * 1000L));

            CSVWriter writer = new CSVWriter(
              ResourcesHelper.getInstance().getExportedDataPath() + File.separator +"exported at " + dateString,
              new ArrayList<>(filterListCell.getItem().getEntities())
            );
            writer.save();
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "File exported succesfully at (" + dateString + ")");
          } catch (InvocationTargetException | NoStocksFoundException | IllegalAccessException | InstantiationException | IntrospectionException | IOException e) {
            e.printStackTrace();
          }
          event.consume();
        });
        MenuItem details = new MenuItem("Details", new ImageView(new Image(Main.class.getResourceAsStream("icons/export.png"), 12, 12, false, false)));
        details.setOnAction(event -> {
          FilterViewHelper.getInstance().showOnWindow(filterListCell.getItem());
        });

        MenuItem historicalPrices = new MenuItem("Get Historical Prices");
        historicalPrices.setOnAction(event -> filterListCell.getItem().createHistoricalPrices());

        filterContextMenu.getItems().addAll(export, details, historicalPrices);
        filterListCell.setContextMenu(filterContextMenu);

        // recover the *damn* text
        filterListCell.itemProperty().addListener((observable, oldValue, newValue) -> {
          if(newValue != null) {
            filterListCell.textProperty().bind(filterListCell.itemProperty().asString());
          }
        });

        return filterListCell;
      });
    }
  }


  private EventHandler<ActionEvent> getFilterEvent(Button filterButton) {
    return event -> {
      new Thread(() -> {
        try {
          Platform.runLater(() -> {
            filterButton.setDisable(true);
            filterButton.setText("Filtering...");
          });

          Core.getInstance().applyFilter(companyFilterView.getFilterParams());

          Platform.runLater(() -> {
            refreshFilterPane();
            filterButton.setDisable(false);
            filterButton.setText("Filter");
          });
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
          Platform.runLater(() -> {
            AlertHelper.showExceptionAlert(e);
            filterButton.setDisable(false);
            filterButton.setText("Filter");
          });
          e.printStackTrace();
        }
      }).start();
      event.consume();
    };
  }

}
