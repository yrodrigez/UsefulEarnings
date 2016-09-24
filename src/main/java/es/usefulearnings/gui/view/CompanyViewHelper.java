package es.usefulearnings.gui.view;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.YahooField;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * View manager of CompanyViewHelper's data
 *
 * @author Yago Rodríguez
 */
public class CompanyViewHelper implements ViewHelper {
  private Company company;
  private WebEngine webEngine;

  public CompanyViewHelper(Company company, WebEngine webEngine) {
    this.setCompany(company);
    this.webEngine = webEngine;
  }


  private <E> Collection<Node> getEntityView(E entity) {
    Collection<Node> nodes = new ArrayList<>();
    /*try {
      for (java.lang.reflect.Field field : entity.getClass().getDeclaredFields()) {
        String fieldName = field.getDeclaredAnnotation(ObservableField.class).name() + ": ";
        FieldType fieldType = field.getDeclaredAnnotation(ObservableField.class).fieldType();
        //System.out.print(fieldName);
        for (PropertyDescriptor pd : Introspector.getBeanInfo(entity.getClass()).getPropertyDescriptors()) {
          if (pd.getName().equals(field.getName())) {
            try {
              if (fieldType.equals(FieldType.DATE)) {
                YahooField propertyValue = (YahooField) pd.getReadMethod().invoke(entity);
                //System.out.println(propertyValue.getFmt());
                nodes.add(new Label(fieldName + propertyValue.getFmt()));
              }

              if (fieldType.equals(FieldType.URL)) {
                String url = (String) pd.getReadMethod().invoke(entity);
                HBox website = new HBox();
                Hyperlink hyperlink = new Hyperlink(url);
                hyperlink.setOnAction(event -> webEngine.load(url));
                website.getChildren().addAll(new Label(fieldName), hyperlink);
                nodes.add(website);
              }

              if (fieldType.equals(FieldType.STRING)) {
                String stringValue = (String) pd.getReadMethod().invoke(entity);
                nodes.add(new Label(fieldName + stringValue));
              }

              if (fieldType.equals(FieldType.NUMERIC)) {
                YahooField fieldValue = (YahooField) pd.getReadMethod().invoke(entity);
                // System.out.println(propertyValue.getRaw());
                nodes.add(new Label(fieldName + fieldValue.getFmt()));
              }

              if (fieldType.equals(FieldType.RAW_NUMERIC)) {
                Number number = (Number) pd.getReadMethod().invoke(entity);
                // System.out.println(number.toString());
                nodes.add(new Label(fieldName + number.toString()));
              }

              if (fieldType.equals(FieldType.FIELD_ARRAY_LIST)) {
                @SuppressWarnings("unchecked") // this is indeed an ArrayList
                ArrayList<YahooField> fields = (ArrayList<YahooField>) pd.getReadMethod().invoke(entity);
                fields.forEach(field1 -> nodes.add(new Label(fieldName + field1.getFmt())));
              }

              if (fieldType.equals(FieldType.INNER_CLASS)) {
                Object newEntity = pd.getReadMethod().invoke(entity);
                // System.out.println("---This is an inner "+ newEntity.getClass().getName() +" class---");
                nodes.addAll(getEntityView(newEntity));
              }

              if (fieldType.equals(FieldType.BOOLEAN)) {
                // System.out.println("This is a boolean name");
                Boolean boolValue = (Boolean) pd.getReadMethod().invoke(entity);
                nodes.add(new Label(fieldName + boolValue.toString()));
              }
            } catch (NullPointerException e) {
              nodes.add(getNullLabel(fieldName));
            }
            break;
          }
        }
      }

    } catch (NullPointerException | IntrospectionException | IllegalAccessException | InvocationTargetException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getCause() != null? e.getCause() : "cause is null" );
    }*/
    return nodes;
  }

  private Label getNullLabel(String fieldName) {
    return new Label(fieldName + " not found...");
  }

  @Override
  public Collection<Node> setView() {
    Collection<Node> view = new ArrayList<>();

    ArrayList<VBox> vBoxes = new ArrayList<>();
    try {

      for (java.lang.reflect.Field field : company.getClass().getDeclaredFields()) {
        String entityName = field.getDeclaredAnnotation(EntityParameter.class).name();
        ParameterType type = field.getDeclaredAnnotation(EntityParameter.class).parameterType();
        VBox entityBox = new VBox();

        if (!type.equals(ParameterType.IGNORE)) {
          entityBox.getChildren().add(new Label("---" + entityName + "---"));
        }

        for (PropertyDescriptor pd : Introspector.getBeanInfo(company.getClass()).getPropertyDescriptors()) {
          if (pd.getName().equals(field.getName())) {

            if (type.equals(ParameterType.INNER_CLASS_COLLECTION)) {
              @SuppressWarnings("unchecked")// this is indeed an ArrayList
              ArrayList<Object> entities = (ArrayList<Object>) pd.getReadMethod().invoke(company);
              entities.forEach(entity -> {
                entityBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
                entityBox.getChildren().addAll(getEntityView(entity));
                });
            }

            if (type.equals(ParameterType.CLASS)) {
              Object entity = pd.getReadMethod().invoke(company);
              entityBox.getChildren().addAll(getEntityView(entity));
            }

            break;
          }
        }
        vBoxes.add(entityBox);
      }
    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }

    Iterator<VBox> vBoxIterator = vBoxes.iterator();
    while (vBoxIterator.hasNext()){
      HBox hbox = new HBox();
      hbox.getChildren().add(vBoxIterator.next());
      hbox.getChildren().add(new Separator(Orientation.VERTICAL));

      if(vBoxIterator.hasNext()){
        hbox.getChildren().add(vBoxIterator.next());
        hbox.getChildren().add(new Separator(Orientation.VERTICAL));
      }

      view.add(hbox);
      view.add(new Separator(Orientation.HORIZONTAL));
    }


    return view;
  }

  public void setCompany(Company company) {
    this.company = company;
  }
}
