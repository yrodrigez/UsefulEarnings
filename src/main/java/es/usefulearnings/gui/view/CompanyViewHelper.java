package es.usefulearnings.gui.view;

import es.usefulearnings.annotation.AllowedValuesRetriever;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.filter.BasicOperator;
import es.usefulearnings.engine.filter.RestrictionValue;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

/**
 * View manager of CompanyViewHelper's data
 *
 * @author Yago Rodríguez
 */
public class CompanyViewHelper implements ViewHelper<Company> {
  private static CompanyViewHelper _instance = new CompanyViewHelper();

  public static CompanyViewHelper getInstance(){
    return _instance;
  }

  private CompanyViewHelper(){
  }

  private <E> Node getContentFor(E eClass) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    GridPane gridPane = new GridPane();
    gridPane.setHgap(20);
    gridPane.setPadding(new Insets(5, 5, 5, 5));
    for (Field field: eClass.getClass().getDeclaredFields()){
      if (field == null) return new VBox();
      if(field.getDeclaredAnnotation(EntityParameter.class) != null){
        String entityName = field.getDeclaredAnnotation(EntityParameter.class).name();
        ParameterType parameterType = field.getAnnotation(EntityParameter.class).parameterType();
        PropertyDescriptor[] descriptors = Introspector.getBeanInfo(eClass.getClass()).getPropertyDescriptors();
        for(int i = 0 ; i < descriptors.length ; i++){
          if(descriptors[i].getName().equals(field.getName())){
            Label entityNameLabel = new Label(entityName+": ");
            switch (parameterType){
              case INNER_CLASS:
                Accordion accordion = new Accordion();
                accordion.getPanes().add(new TitledPane(
                  entityName,
                  new ScrollPane(getContentFor(descriptors[i].getReadMethod().invoke(eClass)))
                ));
                gridPane.add(accordion, 0, i, 10, 1);
                break;

              case YAHOO_FIELD_DATE:
              case YAHOO_FIELD_NUMERIC:
                gridPane.add(entityNameLabel, 0 , i);
                YahooField yahooField = (YahooField)descriptors[i].getReadMethod().invoke(eClass);
                if(yahooField != null)
                  gridPane.add(new Label(yahooField.getFmt()), 1 , i);
                break;

              case YAHOO_LONG_FORMAT_FIELD:
                gridPane.add(entityNameLabel, 0 , i);
                YahooLongFormatField longFormatField = (YahooLongFormatField)descriptors[i].getReadMethod().invoke(eClass);
                if(longFormatField != null)
                  gridPane.add(new Label(longFormatField.getLongFmt()), 1 , i);
                break;

              case YAHOO_FIELD_DATE_COLLECTION:
                gridPane.add(entityNameLabel, 0, i);
                Collection<YahooField> collection = (Collection<YahooField>)descriptors[i].getReadMethod().invoke(eClass);
                if(collection != null) {
                  Iterator<YahooField> it = collection.iterator();
                  Label datesLabel = new Label();
                  while (it.hasNext()) {
                    YahooField date = it.next();
                    if (it.hasNext()) {
                      datesLabel.setText(datesLabel.getText() + date.getFmt() + " - ");
                    } else {
                      datesLabel.setText(datesLabel.getText() + date.getFmt());
                    }
                  }
                  gridPane.add(datesLabel, 1, i);
                }
                break;

              case URL:
                gridPane.add(entityNameLabel, 0, i);
                String url = (String)descriptors[i].getReadMethod().invoke(eClass);
                if(url != null) {
                  Hyperlink hyperlink = new Hyperlink(url);
                  hyperlink.setOnAction(event -> {
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)){
                      try {
                        Desktop.getDesktop().browse(new URI(url));
                      } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                      }
                    } else {
                      Platform.runLater(()-> AlertHelper.showAlert(
                        Alert.AlertType.INFORMATION,
                        "Operation not supported",
                        "Sorry, you can't do this on your actual OS"
                      ));
                    }
                  });
                  gridPane.add(hyperlink, 1, i);
                }
                break;

              case RAW_STRING:
              case RAW_NUMERIC:
                gridPane.add(entityNameLabel, 0, i);
                if(descriptors[i].getReadMethod().invoke(eClass) != null)
                  gridPane.add(new Label(descriptors[i].getReadMethod().invoke(eClass).toString()), 1, i);
                break;

              case IGNORE:
                break;

              default: throw new IllegalArgumentException("Wrong argument -> " + parameterType.name());
            }
            break;
          }
        }
      }
    }
    return gridPane;
  }

  @Override
  public <E> Node getViewFor(Company company) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    Accordion accordion = new Accordion();
    for (Field field : company.getClass().getDeclaredFields()) {
      if (field.getDeclaredAnnotation(EntityParameter.class) != null) {
        String entityName = field.getDeclaredAnnotation(EntityParameter.class).name();
        ParameterType type = field.getDeclaredAnnotation(EntityParameter.class).parameterType();
        for (PropertyDescriptor pd : Introspector.getBeanInfo(Company.class).getPropertyDescriptors()) {
          if (pd.getName().equals(field.getName())) {

            switch (type){
              case INNER_CLASS:
                ScrollPane pane = new ScrollPane(
                  getContentFor(pd.getReadMethod().invoke(company))
                );
                TitledPane titledPane = new TitledPane(entityName, pane);
                accordion.getPanes().add(titledPane);
                break;
              case INNER_CLASS_COLLECTION:
                Accordion collectionAccordion = new Accordion();
                ScrollPane collectionScrollPane = new ScrollPane(collectionAccordion);

                Collection<E> innerClassCollection = (Collection<E>) pd.getReadMethod().invoke(company);
                for(E innerClass: innerClassCollection){
                  TitledPane innerTittledPane = new TitledPane(
                    entityName,
                    new ScrollPane(getContentFor(innerClass))
                  );
                  collectionAccordion.getPanes().add(innerTittledPane);
                }
                accordion.getPanes().add(new TitledPane(entityName, collectionScrollPane));
                break;
            }
            break;
          }
        }
      }
    }
    return accordion;
  }

  @Override
  public Node getFilterView(Map<Field, RestrictionValue> filter) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    Accordion accordion = new Accordion();
    for (Field field : Company.class.getDeclaredFields()) {
      if (field.getDeclaredAnnotation(EntityParameter.class) != null) {
        String entityName = field.getDeclaredAnnotation(EntityParameter.class).name();
        ParameterType type = field.getDeclaredAnnotation(EntityParameter.class).parameterType();
        for (PropertyDescriptor pd : Introspector.getBeanInfo(Company.class).getPropertyDescriptors()) {
          if (pd.getName().equals(field.getName())) {
            if (type.equals(ParameterType.INNER_CLASS)) {

              ScrollPane pane = new ScrollPane(new ProgressIndicator(-1));

              new Thread(() -> {
                GridPane gr = getFilterField(pd.getReadMethod().getReturnType(), filter);

                // when ready
                Platform.runLater(() -> pane.setContent(gr));
              }).start();

              TitledPane titledPane = new TitledPane(entityName, pane);

              accordion.getPanes().add(titledPane);

            } else if (type.equals(ParameterType.INNER_CLASS_COLLECTION)) {
              ScrollPane pane = new ScrollPane(new ProgressIndicator(-1));

              new Thread(() -> {
                GridPane gr = getFilterField(
                  (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0],
                  filter);

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
    return accordion;
  }

  private <E> GridPane getFilterField(Class<E> parameter, Map<Field, RestrictionValue> filter) {
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

                Node input = getInputFor(field, parameterType, getAChoiceBox(), allowedValues, filter);

                gridPane.add(input, 1, i, 2, 1);
                continue;
              }

              if (parameterType.equals(ParameterType.INNER_CLASS)) {
                Class<?> innerClass = descriptor.getReadMethod().getReturnType();

                Accordion newAccordion = new Accordion();

                ScrollPane pane = new ScrollPane(getFilterField(innerClass, filter));
                TitledPane titledPane = new TitledPane(parameterName, pane);

                newAccordion.getPanes().add(titledPane);
                gridPane.add(newAccordion, 0, i, 3, 1);
                continue;
              }

              gridPane.add(parameterNameLabel, 0, i);

              ChoiceBox<String> cb = getAChoiceBox();
              Node input = getInputFor(field, parameterType, cb, allowedValues, filter);

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
    Collection<String> allowedValues,
    Map<Field, RestrictionValue> filter
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
              assert newValue != null;
              if (oldValue != null && (newValue.length() < oldValue.length())) {
                contextMenu.getItems().setAll(items);
              }


              String value = newValue.toUpperCase();
              List<MenuItem> filteredItems = new ArrayList<>();
              strTextField.getContextMenu().getItems().forEach(menuItem -> {
                if (menuItem.getText().toUpperCase().contains(value)) {
                  filteredItems.add(menuItem);
                }
              });

              contextMenu.getItems().setAll(filteredItems);

              strTextField.getContextMenu().show(strTextField, Side.RIGHT, 0, 0);
              filter.put(field, new RestrictionValue<>(newValue, BasicOperator.EQ));
              if(newValue.equals("")){
                filter.remove(field);
              }
            }
          );
        } else {
          strTextField.textProperty().addListener(
            (observable, oldValue, newValue) -> {
              assert newValue != null;
              filter.put(field, new RestrictionValue<>(newValue, BasicOperator.EQ));
              if(newValue.equals("")){
                filter.remove(field);
              }
            });
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
        datePicker.accessibleTextProperty().addListener(
          (observable, oldValue, newValue) -> {
            assert newValue != null;
            if (newValue.equals("")) filter.remove(field);
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
            assert newValue != null;
            if (newValue.matches("\\d*(\\.\\d*)?")) {
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
              if(newValue.equals("")) filter.remove(field);

            } else {
              if (oldValue != null) numTextField.setText(oldValue);
              numTextField.setStyle("-fx-border-color: linear-gradient(#fe372b 25%, #ff4b5c 75%, #fc000f 100%); -fx-border-width: 2;");
              numTextField.setTooltip(new Tooltip("You can write numbers only!"));
              numTextField.getTooltip().show(numTextField.getScene().getWindow());
              if(newValue.equals("")) filter.remove(field);
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
