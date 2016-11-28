package es.usefulearnings.gui.view;

import es.usefulearnings.annotation.AllowedValuesRetriever;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.EntityParameterBeanWalker;
import es.usefulearnings.engine.filter.BasicOperator;
import es.usefulearnings.engine.filter.RestrictionValue;
import es.usefulearnings.gui.animation.OverWatchLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author yago.
 */
public class FilterView {

  private Map<Field, RestrictionValue> _filterParams;
  private Node _view;

  public FilterView(
    Class<?> anyClass
  ) throws IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException {
    _filterParams = new HashMap<>();
    _view = getFilterView(anyClass, _filterParams);
  }
  public Node getView(){
    return _view;
  }

  public Map<Field, RestrictionValue> getFilterParams() {
    return _filterParams;
  }

  private Node getFilterView(
    Class<?> anyClass,
    Map<Field, RestrictionValue> filterParams
  ) throws IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException {
    Accordion accordion = new Accordion();
    GridPane gridPane = new GridPane();
    gridPane.setHgap(20);
    gridPane.setPadding(new Insets(5, 5, 5, 5));
    EntityParameterBeanWalker worker = new EntityParameterBeanWalker(
      (field, annotation, method, position)->{
        EntityParameter parameter = (EntityParameter) annotation;
        String parameterName = parameter.name();
        ParameterType parameterType = parameter.parameterType();
        Class<? extends AllowedValuesRetriever> allowedValuesRetrieverClass = field.getDeclaredAnnotation(EntityParameter.class).allowedValues();
        Collection<String> allowedValues = allowedValuesRetrieverClass.newInstance().getAllowedValues();
        Label parameterNameLabel = new Label(parameterName+ ": ");
        switch (parameterType){
          case INNER_CLASS:
            OverWatchLoader loader = new OverWatchLoader(Color.web("#400090"));
            ScrollPane scrollPane = new ScrollPane(loader.getLoader());
            new Thread(()->{
              try {
                Node view = getFilterView(method.getReturnType(), filterParams);
                Platform.runLater(()->scrollPane.setContent(view));
              } catch (
                IllegalAccessException
                  | IntrospectionException
                  | InstantiationException
                  | InvocationTargetException
                  e
                ) {
                e.printStackTrace();
              }
            }).start();
            TitledPane innerTittledPane = new TitledPane(
              parameterName,
              scrollPane
            );
            accordion.getPanes().add(innerTittledPane);
            break;

          case INNER_CLASS_COLLECTION:
            Class<?> parameterizedClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            TitledPane parameterizedTittledPane = new TitledPane(
              parameterName,
              new ScrollPane(getFilterView(parameterizedClass, filterParams))
            );
            accordion.getPanes().add(parameterizedTittledPane);
            break;

          case URL:
          case RAW_STRING:
            TextField strTextField = new TextField();
            ContextMenu contextMenu = new ContextMenu();
            strTextField.setContextMenu(contextMenu);
            if (allowedValues != null) {
              setAllowedValuesForContextMenu(field, allowedValues, filterParams, strTextField, contextMenu);
            } else {
              strTextField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                  if(newValue != null && !newValue.isEmpty()) {
                    filterParams.put(field, new RestrictionValue<>(newValue, BasicOperator.EQ));
                  } else {
                    System.err.println("Removing from " + field.getName());
                    filterParams.remove(field);
                  }
                });
            }
            gridPane.add(parameterNameLabel, 0, position);
            gridPane.add(strTextField, 1, position, 2, 1);
            break;

          case YAHOO_FIELD_DATE_COLLECTION:
          case YAHOO_FIELD_DATE:
            DatePicker datePicker = new DatePicker();
            ChoiceBox<String> dateChoiceBox = getAChoiceBox();
            dateChoiceBox.setOnAction(event -> {
              if (datePicker.getValue() != null && !datePicker.getValue().toString().isEmpty()) {
                putDate(field, dateChoiceBox, filterParams, datePicker);
              } else {
                filterParams.remove(field);
              }
            });

            datePicker.setOnAction(event -> {
              if(datePicker.getValue() != null && !datePicker.getValue().toString().isEmpty()) {
                putDate(field, dateChoiceBox, filterParams, datePicker);
              } else {
                filterParams.remove(field);
              }
            });
            gridPane.add(parameterNameLabel, 0, position);
            gridPane.add(dateChoiceBox, 1, position);
            gridPane.add(datePicker, 2, position);
            break;

          case RAW_NUMERIC:
          case YAHOO_LONG_FORMAT_FIELD:
          case YAHOO_FIELD_NUMERIC:
            TextField numTextField = new TextField();
            ChoiceBox<String> numericChoiceBox = getAChoiceBox();
            numericChoiceBox.setOnAction(event -> {
              if (!numTextField.getText().equals("")) {
                putNumber(field, numericChoiceBox, filterParams, numTextField);
              } else {
                filterParams.remove(field);
              }
            });
            numTextField.textProperty().addListener(
              (observable, oldValue, newValue) -> {
                assert newValue != null;
                if (newValue.matches("\\d*(\\d+\\.\\d*)?")) {
                  numTextField.setStyle("");
                  if(numTextField.getTooltip() != null){
                    numTextField.getTooltip().hide();
                    numTextField.setTooltip(null);
                  }
                  if (!newValue.equals("")){
                    putNumber(field, numericChoiceBox, filterParams, numTextField);
                  } else {
                    filterParams.remove(field);
                  }
                } else {
                  if (oldValue != null) numTextField.setText(oldValue);
                  numTextField.setStyle("-fx-border-color: red; -fx-border-width: 1;");
                  numTextField.setTooltip(new Tooltip("You can write numbers only!"));
                  numTextField.getTooltip().show(numTextField.getScene().getWindow());
                  if (newValue.equals("")){
                    filterParams.remove(field);
                  }
                }
              });
            gridPane.add(parameterNameLabel, 0, position);
            gridPane.add(numericChoiceBox, 1, position);
            gridPane.add(numTextField, 2, position);
            break;

          case CLASS:
          case HISTORICAL_DATA:
          case IGNORE:
          case OPTION_TYPE:
          case RAW_BOOLEAN:
          case YAHOO_FIELD_COLLECTION:
          case RAW_DATE:
          case OPTION_LINK_COLLECTION:
          case NUMBER_COLLECTION:
          case RAW_DATE_COLLECTION:
            break;

          default:
            throw new IllegalArgumentException("I have no input for " + parameterType.name() + " class " + field.getName());
        }
      }
    );
    worker.walk(anyClass);
    return new VBox(accordion, gridPane);
  }


  private void setAllowedValuesForContextMenu(
    Field field,
    Collection<String> allowedValues,
    Map<Field, RestrictionValue> filter,
    TextField textField,
    ContextMenu contextMenu
  ) {
    List<MenuItem> items = new ArrayList<>();
    allowedValues.forEach(value -> {
      MenuItem item = new MenuItem(value);
      item.setOnAction(event -> {
        textField.setText(item.getText());
        event.consume();
      });
      items.add(item);
      items.sort((o1, o2) -> o1.getText().compareTo(o2.getText()));
      contextMenu.getItems().setAll(items);
    });

    textField.textProperty().addListener(
      (observable, oldValue, newValue) -> {
        if (oldValue != null && (newValue.length() < oldValue.length())) {
          contextMenu.getItems().setAll(items);
        }

        String value = newValue.toUpperCase();
        List<MenuItem> filteredItems = new ArrayList<>();
        textField.getContextMenu().getItems().forEach(menuItem -> {
          if (menuItem.getText().toUpperCase().contains(value)) {
            filteredItems.add(menuItem);
          }
        });

        contextMenu.getItems().setAll(filteredItems);

        textField.getContextMenu().show(textField, Side.RIGHT, 0, 0);

        if(!newValue.isEmpty() && allowedValues.contains(newValue)) {
          filter.put(field, new RestrictionValue<>(newValue, BasicOperator.EQ));
        } else {
          System.err.println("Removing from " + field.getName());
          filter.remove(field);
        }
      }
    );

    textField.setOnMouseClicked(event -> {
      if(event.getButton().equals(MouseButton.PRIMARY)){
        if(event.getClickCount() == 2){
          textField.getContextMenu().show(textField, Side.RIGHT, 0, 0);
        }
        if(event.getClickCount() == 1){
          textField.getContextMenu().hide();
        }
      } else if(event.getButton().equals(MouseButton.SECONDARY)){
        textField.getContextMenu().show(textField, Side.RIGHT, 0, 0);
      }
      event.consume();
    });

    textField.setTooltip(new Tooltip("Double or right click for allowed values"));
  }

  private void putNumber(
    Field field,
    ChoiceBox<String> choiceBox,
    Map<Field, RestrictionValue> filter,
    TextField textField
  ) {
    switch (choiceBox.getValue()) {
      case "<":
        filter.put(field, new RestrictionValue<>(Double.parseDouble(textField.getText()), BasicOperator.LT));
        break;
      case ">":
        filter.put(field, new RestrictionValue<>(Double.parseDouble(textField.getText()), BasicOperator.GT));
        break;
      case "=":
        filter.put(field, new RestrictionValue<>(Double.parseDouble(textField.getText()), BasicOperator.EQ));
        break;
      default:
        throw new IllegalArgumentException("\"" + choiceBox.getValue() + "\" not defined for this method");
    }
  }

  private void putDate(
    Field field,
    ChoiceBox<String> choiceBox,
    Map<Field, RestrictionValue> filter,
    DatePicker datePicker
  ) {
    switch (choiceBox.getValue()) {
      case "<":
        filter.put(field, new RestrictionValue<>(datePicker.getValue().toEpochDay() * 86400L, BasicOperator.LT));
        break;
      case ">":
        filter.put(field, new RestrictionValue<>(datePicker.getValue().toEpochDay() * 86400L, BasicOperator.GT));
        break;
      case "=":
        filter.put(field, new RestrictionValue<>(datePicker.getValue().toEpochDay() * 86400, BasicOperator.EQ));
        break;
      default:
        throw new IllegalArgumentException("\"" + choiceBox.getValue() + "\" not defined for this method");
    }
  }

  private ChoiceBox<String> getAChoiceBox() {
    String[] operators = {"<", "=", ">"};
    ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(operators));
    choiceBox.setValue(operators[1]);
    return choiceBox;
  }
}
