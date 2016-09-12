package es.usefulearnings.gui.controller;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.EntityParameterType;
import es.usefulearnings.annotation.FieldType;
import es.usefulearnings.annotation.ObservableField;
import es.usefulearnings.entities.Company;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * @author Yago on 12/09/2016.
 */
public class OptionsController implements Initializable {

  @FXML
  private BorderPane _borderPane;
  @FXML
  private TreeView<Node> _treeView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    getCompanyDeclaredFields();
  }

  private void getCompanyDeclaredFields() {
    _borderPane.setPrefSize(1024 - 35, 768 - 35);
    TreeItem<Node> root = new TreeItem<>(new Label("Company"));
    _treeView.setRoot(root);
    try {
      for (java.lang.reflect.Field field : Company.class.getDeclaredFields()) {
        String entityName = field.getDeclaredAnnotation(EntityParameter.class).name();
        EntityParameterType type = field.getDeclaredAnnotation(EntityParameter.class).entityType();

        for (PropertyDescriptor pd : Introspector.getBeanInfo(Company.class).getPropertyDescriptors()) {
          if (pd.getName().equals(field.getName())) {
            if (!type.equals(EntityParameterType.IGNORE)) {
              CheckBox entityCB = new CheckBox(entityName);
              entityCB.setId("Company/"+entityName);
              TreeItem<Node> newTree = new TreeItem<>(entityCB);

              Collection<TreeItem<Node>> checkBoxes = getEntityFields(pd.getReadMethod().invoke(new Company()));
              newTree.getChildren().addAll(checkBoxes);

              root.getChildren().add(newTree);
            }
            break;
          }
        }
      }

    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private <E> Collection<TreeItem<Node>> getEntityFields(E entity) {
    Collection<TreeItem<Node>> nodes = new ArrayList<>();
    try {
      for (java.lang.reflect.Field field : entity.getClass().getDeclaredFields()) {
        String fieldName = field.getDeclaredAnnotation(ObservableField.class).name();
        FieldType fieldType = field.getDeclaredAnnotation(ObservableField.class).fieldType();
        if(!fieldType.equals(FieldType.FIELD_ARRAY_LIST)) { // it's not an ArrayList
          CheckBox checkBox = new CheckBox(fieldName);
          checkBox.setId("Company/" + fieldName);
          nodes.add(new TreeItem<>(checkBox));
        } else {
          // TODO create the checkboxes for this...
        }
      }

    } catch (NullPointerException e) {
      System.err.println(e.getMessage());
      System.err.println(e.getCause() != null ? e.getCause() : "cause is null");
    }
    return nodes;
  }

}
