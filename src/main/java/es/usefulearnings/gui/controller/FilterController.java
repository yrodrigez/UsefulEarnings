package es.usefulearnings.gui.controller;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.Company;
import es.usefulearnings.gui.view.AlertHelper;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Yago on 04/09/2016.
 */
public class FilterController implements Initializable {
  public BorderPane mainPane;
  public Accordion accordion;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setTittledPanesForCompanies();
  }

  private void setTittledPanesForCompanies() {
    try {
      for (Field field : Company.class.getDeclaredFields()) {
        if (field.getDeclaredAnnotation(EntityParameter.class) != null) {
          String entityName = field.getDeclaredAnnotation(EntityParameter.class).name();
          ParameterType type = field.getDeclaredAnnotation(EntityParameter.class).parameterType();
          for (PropertyDescriptor pd : Introspector.getBeanInfo(Company.class).getPropertyDescriptors()) {
            if (pd.getName().equals(field.getName())) {
              if (type.equals(ParameterType.INNER_CLASS)) {

                ScrollPane pane = new ScrollPane(getFilterField(pd.getReadMethod().getReturnType()));
                TitledPane titledPane = new TitledPane(entityName, pane);

                accordion.getPanes().add(titledPane);

              } else if (type.equals(ParameterType.INNER_CLASS_COLLECTION)) {
                ScrollPane pane = new ScrollPane(getFilterField(
                  (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]
                ));
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
    gridPane.setPadding(new Insets(5, 15, 5, 15));
    try{
      for(Field field : parameter.getDeclaredFields()) {
       if(field.getDeclaredAnnotation(EntityParameter.class) != null){
         String parameterName = field.getDeclaredAnnotation(EntityParameter.class).name();
         ParameterType parameterType = field.getDeclaredAnnotation(EntityParameter.class).parameterType();

         for(int i = 0; i < Introspector.getBeanInfo(parameter).getPropertyDescriptors().length ; i++){
           PropertyDescriptor descriptor = Introspector.getBeanInfo(parameter).getPropertyDescriptors()[i];
           if(descriptor.getName().equals(field.getName())){

             Label parameterNameLabel = new Label(parameterName);

             if(parameterType.equals(ParameterType.RAW_STRING)){
               gridPane.add(parameterNameLabel, 0, i);
               gridPane.add(new TextField(), 1, i, 2, 1);
             }

             if(parameterType.equals(ParameterType.YAHOO_FIELD_DATE)){
               gridPane.add(parameterNameLabel, 0, i);
               gridPane.add(getAChoiceBox(), 1, i);
               gridPane.add(new DatePicker(), 2, i);
             }

             if(parameterType.equals(ParameterType.YAHOO_FIELD_NUMERIC)){
               gridPane.add(parameterNameLabel, 0, i);
               gridPane.add(getAChoiceBox(), 1, i);
               gridPane.add(new TextField(), 2, i);
             }

             if(parameterType.equals(ParameterType.YAHOO_LONG_FORMAT_FIELD)){
               gridPane.add(parameterNameLabel, 0, i);
               gridPane.add(getAChoiceBox(), 1, i);
               gridPane.add(new TextField(), 2, i);
             }

             if(parameterType.equals(ParameterType.INNER_CLASS)){
               Class<?> innerClass = descriptor.getReadMethod().getReturnType();

               Accordion newAccordion = new Accordion();

               VBox vBox = new VBox();
               vBox.getChildren().addAll(getFilterField(innerClass));
               TitledPane titledPane = new TitledPane(parameterName, vBox);

               newAccordion.getPanes().add(titledPane);
               gridPane.add(newAccordion, 0, i, 3, 1);
             }

           }
         }

       }
      }
    }catch (NullPointerException | IntrospectionException e) {
      e.printStackTrace();
      AlertHelper.showExceptionAlert(e);
    }
    return gridPane;
  }

  private Node getAChoiceBox(){
    String[] operators = {"<", "=", ">"};

    return new ChoiceBox<>(FXCollections.observableArrayList(operators));
  }

}
