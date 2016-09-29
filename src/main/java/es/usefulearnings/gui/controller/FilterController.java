package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.filter.Filter;
import es.usefulearnings.engine.filter.RestrictionValue;
import es.usefulearnings.entities.Company;
import es.usefulearnings.gui.Main;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.gui.view.CompanyViewHelper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Yago on 04/09/2016.
 */
public class FilterController implements Initializable {
  public BorderPane mainPane;
  private ListView<Filter> filterListView;
  private ListView<Company> companyListView;
  public BorderPane rightPane;
  // remove listeners here!!
  private ChangeListener<Company> companyChangeListener;
  private ChangeListener<Filter> filterChangeListener;

  private Map<Field, RestrictionValue> filter;

  private VBox lists;


  @Override
  public void initialize(URL location, ResourceBundle resources){


    filter = new HashMap<>();
    filterListView = new ListView<>();
    filterListView.prefHeightProperty().bind(mainPane.heightProperty().multiply(0.5));
    companyListView = new ListView<>();
    companyListView.prefHeightProperty().bind(mainPane.heightProperty().multiply(0.5));

    lists = new VBox();
    lists.getChildren().addAll(filterListView, companyListView);

    companyChangeListener = getCompanySelectedListener();
    filterChangeListener = getFilterChangeListener(companyChangeListener);
    try {
      mainPane.setCenter(CompanyViewHelper.getInstance().getFilterView(filter));
    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
      Platform.runLater(() -> AlertHelper.showExceptionAlert(e));
      e.printStackTrace();
    }

    Button filterButton = new Button("Filter");

    filterButton.getStyleClass().addAll("main-controller-button");
    HBox hBox = new HBox(filterButton);
    hBox.setAlignment(Pos.BASELINE_RIGHT);
    mainPane.setBottom(hBox);
    refreshFilterPane();
    filterButton.setOnAction(getFilterEvent(filterButton));
  }


  private void refreshFilterPane() {
    if(Core.getInstance().getAppliedFilters().size() > 0) {
      rightPane.setCenter(lists);
      filterListView.getSelectionModel().selectedItemProperty().removeListener(filterChangeListener);
      List<Filter> appliedFilters = Core.getInstance().getAppliedFilters();
      filterListView.setItems(FXCollections.observableArrayList(appliedFilters));
      filterListView.getSelectionModel().selectedItemProperty().addListener(filterChangeListener);

      filterListView.setCellFactory(param -> {
        ListCell<Filter> ret = new ListCell<>();

        ContextMenu filterContextMenu = new ContextMenu();
        MenuItem export = new MenuItem("Export to Excel", new ImageView(new Image(Main.class.getResourceAsStream("icons/export.png"), 12, 12, false, false)));
        export.setOnAction(event -> {
          AlertHelper.showAlert(
            Alert.AlertType.CONFIRMATION,
            "i will export data",
            "i will export data some day hopefully" + " " + ret.getItem().toString()
          );
        });
        filterContextMenu.getItems().addAll(export);
        ret.setContextMenu(filterContextMenu);

        // recover the *damn* text
        ret.itemProperty().addListener((observable, oldValue, newValue) -> {
          assert newValue != null;
          ret.textProperty().bind(ret.itemProperty().asString());
        });

        return ret;
      });
    }
  }

  @SuppressWarnings("unchecked")
  private ChangeListener<Filter> getFilterChangeListener(ChangeListener<Company> companyChangeListener) {
    return (observable, oldFilter, newFilter) -> {
      if (newFilter != null && newFilter != oldFilter) {
        companyListView.getSelectionModel().selectedItemProperty().removeListener(companyChangeListener);
        companyListView.setItems(FXCollections.observableArrayList(newFilter.getEntities()));
        companyListView.getSelectionModel().selectedItemProperty().addListener(companyChangeListener);
      }
    };
  }

  private ChangeListener<Company> getCompanySelectedListener() {
    return (observable1, oldCompany, newCompany) -> {
      if (newCompany != null && newCompany != oldCompany) {
        CompanyViewHelper.getInstance().showEntityOnWindow(mainPane.getScene().getWindow(), newCompany);
      }
    };
  }

  private EventHandler<ActionEvent> getFilterEvent(Button filterButton) {
    return event -> {
      new Thread(() -> {
        try {
          Platform.runLater(() -> {
            filterButton.setDisable(true);
            filterButton.setText("Filtering...");
          });

          Core.getInstance().applyFilter(filter);

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
