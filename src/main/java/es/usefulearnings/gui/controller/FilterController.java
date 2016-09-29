package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.filter.Filter;
import es.usefulearnings.engine.filter.RestrictionValue;
import es.usefulearnings.entities.Company;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.gui.view.CompanyViewHelper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
  private ListView<Filter> filtersPane;
  public ListView<Company> companiesPane;
  public BorderPane rightPane;

  private Map<Field, RestrictionValue> filter;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    filter = new HashMap<>();
    filtersPane = new ListView<>();
    filtersPane.setMaxSize(250, 325);
    filtersPane.setPrefSize(250, 325);
    companiesPane = new ListView<>();
    companiesPane.setMaxSize(250, 325);
    companiesPane.setPrefSize(250, 325);
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


    filterButton.setOnAction(getFilterEvent(filterButton));
  }

  @SuppressWarnings("unchecked")
  private void refreshFilterPane(){
    rightPane.setTop(filtersPane);
    rightPane.setBottom(companiesPane);
    List<Filter> appliedFilters = Core.getInstance().getAppliedFilters();
    filtersPane.setItems(FXCollections.observableArrayList(appliedFilters));
    ChangeListener<Company> companyChangeListener = getCompanySelectedListener();
    ChangeListener<Filter> filterChangeListener = getFilterChangeListener(companyChangeListener);
    filtersPane.getSelectionModel().selectedItemProperty().removeListener(filterChangeListener);
    filtersPane.getSelectionModel().selectedItemProperty().addListener(filterChangeListener);
  }

  private ChangeListener<Filter> getFilterChangeListener(ChangeListener<Company> companyChangeListener) {
    return (observable, oldFilter, newFilter) -> {
      if(newFilter != null && newFilter != oldFilter) {
        companiesPane.getSelectionModel().selectedItemProperty().removeListener(companyChangeListener);
        companiesPane.setItems(FXCollections.observableArrayList(newFilter.getEntities()));
        companiesPane.getSelectionModel().selectedItemProperty().addListener(companyChangeListener);
      }
    };
  }

  private ChangeListener<Company> getCompanySelectedListener(){
    return (observable1, oldCompany, newCompany) -> {
      if(newCompany != null && newCompany != oldCompany) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(newCompany.getSymbol());
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(mainPane.getScene().getWindow());
        Scene scene = null;
        try {
          BorderPane borderPane = new BorderPane();
          borderPane.setCenter(CompanyViewHelper.getInstance().getViewFor(newCompany));
          borderPane.setPrefSize(800, 600);
          scene = new Scene(borderPane);
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
          AlertHelper.showExceptionAlert(e);
          e.printStackTrace();
        }
        dialogStage.setScene(scene);
        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
      }
    };
  }

  private EventHandler<ActionEvent> getFilterEvent(Button filterButton){
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
