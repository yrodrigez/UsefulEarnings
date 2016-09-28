package es.usefulearnings.gui.controller;

import es.usefulearnings.annotation.AllowedValuesRetriever;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.filter.BasicOperator;
import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.filter.RestrictionValue;
import es.usefulearnings.entities.Company;
import es.usefulearnings.gui.view.AlertHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.*;

/**
 * @author Yago on 04/09/2016.
 */
public class FilterController implements Initializable {
  public BorderPane mainPane;
  public Accordion accordion;

  private Map<Field, RestrictionValue> filter;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setTittledPanesForCompanies();
    Button filterButton = new Button("Filter");

    filterButton.getStyleClass().addAll("main-controller-button");
    HBox hBox = new HBox(filterButton);
    hBox.setAlignment(Pos.BASELINE_RIGHT);
    mainPane.setBottom(hBox);
    filter = new HashMap<>();

    filterButton.setOnAction(event -> {
      new Thread(()->{
        try {
          Core.getInstance().applyFilter(filter);
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
          Platform.runLater(()-> AlertHelper.showExceptionAlert(e));
          e.printStackTrace();
        }

      }).start();
      event.consume();
    });
  }

  protected void setTittledPanesForCompanies() {
    try {
      for (Field field : Company.class.getDeclaredFields()) {
        if (field.getDeclaredAnnotation(EntityParameter.class) != null) {
          String entityName = field.getDeclaredAnnotation(EntityParameter.class).name();
          ParameterType type = field.getDeclaredAnnotation(EntityParameter.class).parameterType();
          for (PropertyDescriptor pd : Introspector.getBeanInfo(Company.class).getPropertyDescriptors()) {
            if (pd.getName().equals(field.getName())) {
              if (type.equals(ParameterType.INNER_CLASS)) {

                ScrollPane pane = new ScrollPane(new ProgressIndicator(-1));

                new Thread(() -> {
                  GridPane gr = getFilterField(pd.getReadMethod().getReturnType());

                  // when ready
                  Platform.runLater(() -> pane.setContent(gr));
                }).start();

                TitledPane titledPane = new TitledPane(entityName, pane);

                accordion.getPanes().add(titledPane);

              } else if (type.equals(ParameterType.INNER_CLASS_COLLECTION)) {
                ScrollPane pane = new ScrollPane(new ProgressIndicator(-1));

                new Thread(() -> {
                  GridPane gr = getFilterField(
                    (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]
                  );

                  // when ready
                  Platform.runLater(() -> pane.setContent(gr));
                }).start();

                TitledPane titledPane = new TitledPane(entityName, pane);
                accordion.getPanes().add(titledPane);
              }
              break;
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      AlertHelper.showExceptionAlert(e);
    }
  }


  private <E> GridPane getFilterField(Class<E> parameter) {
    GridPane gridPane = new GridPane();
    gridPane.setHgap(20);
    gridPane.setPadding(new Insets(5, 5, 5, 5));
    try {
      for (Field field : parameter.getDeclaredFields()) {
        if (field.getDeclaredAnnotation(EntityParameter.class) != null) {
          String parameterName = field.getDeclaredAnnotation(EntityParameter.class).name();
          ParameterType parameterType = field.getDeclaredAnnotation(EntityParameter.class).parameterType();
          Class<? extends AllowedValuesRetriever> allowedValuesRetrieverClass = field.getDeclaredAnnotation(EntityParameter.class).allowedValues();
          Collection<String> allowedValues = allowedValuesRetrieverClass.newInstance().getAllowedValues();
          for (int i = 0; i < Introspector.getBeanInfo(parameter).getPropertyDescriptors().length; i++) {
            PropertyDescriptor descriptor = Introspector.getBeanInfo(parameter).getPropertyDescriptors()[i];
            if (descriptor.getName().equals(field.getName())) {

              Label parameterNameLabel = new Label(parameterName);

              if (parameterType.equals(ParameterType.RAW_STRING) || parameterType.equals(ParameterType.URL)) {
                gridPane.add(parameterNameLabel, 0, i);

                Node input = getInputFor(field, parameterType, getAChoiceBox(), allowedValues);

                gridPane.add(input, 1, i, 2, 1);
                continue;
              }

              if (parameterType.equals(ParameterType.INNER_CLASS)) {
                Class<?> innerClass = descriptor.getReadMethod().getReturnType();

                Accordion newAccordion = new Accordion();

                ScrollPane pane = new ScrollPane(getFilterField(innerClass));
                TitledPane titledPane = new TitledPane(parameterName, pane);

                newAccordion.getPanes().add(titledPane);
                gridPane.add(newAccordion, 0, i, 3, 1);
                continue;
              }

              gridPane.add(parameterNameLabel, 0, i);

              ChoiceBox<String> cb = getAChoiceBox();
              Node input = getInputFor(field, parameterType, cb, allowedValues);

              gridPane.add(cb, 1, i);
              gridPane.add(input, 2, i);
            }
          }

        }
      }
    } catch (NullPointerException | IntrospectionException e) {
      e.printStackTrace();
      AlertHelper.showExceptionAlert(e);
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return gridPane;
  }

  private Node getInputFor(
    Field field,
    ParameterType parameterType,
    ChoiceBox<String> choiceBox,
    Collection<String> allowedValues
  ) {
    switch (parameterType) {
      case URL:
      case RAW_STRING:
        TextField strTextField = new TextField();
        ContextMenu contextMenu = new ContextMenu();
        strTextField.setContextMenu(contextMenu);
        if (allowedValues != null) {
          List<MenuItem> items = new ArrayList<>();
          allowedValues.forEach(value -> {
            MenuItem item = new MenuItem(value);
            item.setOnAction(event -> {
              strTextField.setText(item.getText());
              event.consume();
            });
            items.add(item);
            items.sort((o1, o2) -> o1.getText().compareTo(o2.getText()));
            contextMenu.getItems().setAll(items);
          });

          strTextField.textProperty().addListener(
            (observable, oldValue, newValue) -> {
              if (oldValue != null && (newValue.length() < oldValue.length())) {
                contextMenu.getItems().setAll(items);
              }

              if (newValue != null) {
                String value = newValue.toUpperCase();
                List<MenuItem> filteredItems = new ArrayList<>();
                strTextField.getContextMenu().getItems().forEach(menuItem -> {
                  if (menuItem.getText().toUpperCase().contains(value)) {
                    filteredItems.add(menuItem);
                  }
                });

                contextMenu.getItems().setAll(filteredItems);
              }
              strTextField.getContextMenu().show(strTextField, Side.RIGHT, 0, 0);
              System.out.println("Field: " + field + ", value: "+ newValue);
              filter.put(field, new RestrictionValue<>(newValue, BasicOperator.EQ));
            }
          );
        } else {
          strTextField.textProperty().addListener(
            (observable, oldValue, newValue) -> filter.put(field, new RestrictionValue<>(newValue, BasicOperator.EQ))
          );
        }
        return strTextField;

      case YAHOO_FIELD_DATE_COLLECTION:
      case YAHOO_FIELD_DATE:
        DatePicker datePicker = new DatePicker();
        choiceBox.setOnAction(event -> {
          if(datePicker.getValue() != null) {
            switch (choiceBox.getValue()) {
              case "<":
                filter.put(field, new RestrictionValue<>(datePicker.getValue().toEpochDay() * 86400L, BasicOperator.LT));
                break;
              case ">":
                filter.put(field, new RestrictionValue<>(datePicker.getValue().toEpochDay() * 86400L, BasicOperator.GT));
                break;
              case "=":
                filter.put(field, new RestrictionValue<>(datePicker.getValue().toEpochDay() * 86400L, BasicOperator.EQ));
                break;
            }
          }
        });
        datePicker.setOnAction(event -> {
          switch (choiceBox.getValue()) {
            case "<":
              filter.put(field, new RestrictionValue<>(datePicker.getValue().toEpochDay() * 86400L, BasicOperator.LT));
              break;
            case ">":
              filter.put(field, new RestrictionValue<>(datePicker.getValue().toEpochDay() * 86400L, BasicOperator.GT));
              break;
            default:
              filter.put(field, new RestrictionValue<>(datePicker.getValue().toEpochDay() * 86400, BasicOperator.EQ));
              break;
          }
        });
        return datePicker;

      case RAW_NUMERIC:
      case YAHOO_LONG_FORMAT_FIELD:
      case YAHOO_FIELD_NUMERIC:
        TextField numTextField = new TextField();
        choiceBox.setOnAction(event -> {
          if(!numTextField.getText().equals("")) {
            switch (choiceBox.getValue()) {
              case "<":
                filter.put(field, new RestrictionValue<>(Double.parseDouble(numTextField.getText()), BasicOperator.LT));
                break;
              case ">":
                filter.put(field, new RestrictionValue<>(Double.parseDouble(numTextField.getText()), BasicOperator.GT));
                break;
              case "=":
                filter.put(field, new RestrictionValue<>(Double.parseDouble(numTextField.getText()), BasicOperator.EQ));
                break;
            }
          }
        });
        numTextField.textProperty().addListener(
          (observable, oldValue, newValue) -> {
          if (newValue.matches("\\d+(\\.\\d*)?")) {
            numTextField.setStyle("");
            if (numTextField.getTooltip() != null) numTextField.getTooltip().hide();
            numTextField.setTooltip(null);
            switch (choiceBox.getValue()) {
              case "<":
                filter.put(field, new RestrictionValue<>(Double.parseDouble(numTextField.getText()), BasicOperator.LT));
                break;
              case ">":
                filter.put(field, new RestrictionValue<>(Double.parseDouble(numTextField.getText()), BasicOperator.GT));
                break;
              case "=":
                filter.put(field, new RestrictionValue<>(Double.parseDouble(numTextField.getText()), BasicOperator.EQ));
                break;
            }
          } else {
            if (oldValue != null) numTextField.setText(oldValue);
            numTextField.setStyle("-fx-border-color: linear-gradient(#fe372b 25%, #ff4b5c 75%, #fc000f 100%); -fx-border-width: 2;");
            numTextField.setTooltip(new Tooltip("You can write numbers only!"));
            numTextField.getTooltip().show(numTextField.getScene().getWindow());
          }
        });
        return numTextField;


      default:
        throw new IllegalArgumentException("getInputFor() -> I have no input for " + parameterType.name() + " class " + field.getName());
    }
  }

  private ChoiceBox<String> getAChoiceBox() {
    String[] operators = {"<", "=", ">"};
    ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(operators));
    choiceBox.setValue(operators[1]);
    return choiceBox;
  }

}
