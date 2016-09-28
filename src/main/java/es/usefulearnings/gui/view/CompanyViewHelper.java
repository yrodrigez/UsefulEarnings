package es.usefulearnings.gui.view;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * View manager of CompanyViewHelper's data
 *
 * @author Yago Rodríguez
 */
public class CompanyViewHelper implements ViewHelper<Company> {
  private WebEngine webEngine;


  public CompanyViewHelper(WebEngine webEngine) {
    this.webEngine = webEngine;
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
                gridPane.add(accordion, 0, i, 3, 1);
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
                  hyperlink.setOnAction(event -> webEngine.load(url));
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
                Class<?> typeOf = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
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
}
