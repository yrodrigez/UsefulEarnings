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
import javafx.scene.layout.HBox;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
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
  // @FXML private TreeView<Node> _treeView;
  private TreeView<Node> mTreeView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    _borderPane.setPrefSize(1024 - 35, 768 - 35);
    mTreeView = new TreeView<>();
    _borderPane.setCenter(new HBox(mTreeView));
    getCompanyDeclaredFields();
  }

  private void getCompanyDeclaredFields() {
    TreeItem<Node> root = new TreeItem<>(new Label("Company"));
    mTreeView.setRoot(root);
    try {
      for (java.lang.reflect.Field field : Company.class.getDeclaredFields()) {
        String entityName = field.getDeclaredAnnotation(EntityParameter.class).name();
        EntityParameterType type = field.getDeclaredAnnotation(EntityParameter.class).entityType();
        for (PropertyDescriptor pd : Introspector.getBeanInfo(Company.class).getPropertyDescriptors()) {
          if (pd.getName().equals(field.getName())) {
            if (!type.equals(EntityParameterType.IGNORE) && !type.equals(EntityParameterType.ARRAY_LIST)) {
              CheckBox entityCB = new CheckBox(entityName);
              entityCB.setId("Company/"+entityName);
              TreeItem<Node> newTree = new TreeItem<>(entityCB);

              Collection<TreeItem<Node>> checkBoxes = getEntityFields(pd.getReadMethod().getReturnType());
              newTree.getChildren().addAll(checkBoxes);

              root.getChildren().add(newTree);
            } else
            if (type.equals(EntityParameterType.ARRAY_LIST)) {
              CheckBox arrayCB = new CheckBox(entityName);
              arrayCB.setId("Company/"+entityName);
              TreeItem<Node> newTree = new TreeItem<>(arrayCB);

              Collection<TreeItem<Node>> moreNodes = getEntityFields(
              (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]
              );
              newTree.getChildren().addAll(moreNodes);

              root.getChildren().add(newTree);
            }
            break;
          }
        }
      }
      mTreeView.getRoot().setExpanded(true);
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }
  }

  private <E> Collection<TreeItem<Node>> getEntityFields(Class<E> entityClass) {
    Collection<TreeItem<Node>> nodes = new ArrayList<>();
    try {
      for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
        String fieldName = field.getDeclaredAnnotation(ObservableField.class).name();
        FieldType fieldType = field.getDeclaredAnnotation(ObservableField.class).fieldType();
        for (PropertyDescriptor pd : Introspector.getBeanInfo(entityClass).getPropertyDescriptors()) {
          if (pd.getName().equals(field.getName())) {

            if (!fieldType.equals(FieldType.FIELD_ARRAY_LIST) && !fieldType.equals(FieldType.INNER_CLASS)) {
              CheckBox checkBox = new CheckBox(fieldName);
              checkBox.setId("Company/" + fieldName);
              nodes.add(new TreeItem<>(checkBox));
            }

            else {

              if (fieldType.equals(FieldType.FIELD_ARRAY_LIST)) {
                CheckBox arrayCB = new CheckBox(fieldName);
                arrayCB.setId("Company/" + fieldName);
                TreeItem<Node> newTree = new TreeItem<>(arrayCB);

                Collection<TreeItem<Node>> moreNodes = getEntityFields(
                  (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]
                );
                newTree.getChildren().addAll(moreNodes);

                nodes.add(newTree);
              }

              if (fieldType.equals(FieldType.INNER_CLASS)) {
                Class<?> innerEntityClass = pd.getReadMethod().getReturnType();

                CheckBox arrayCB = new CheckBox(fieldName);
                arrayCB.setId("Company/" + fieldName);
                TreeItem<Node> newTree = new TreeItem<>(arrayCB);

                newTree.getChildren().addAll(getEntityFields(innerEntityClass));
                nodes.add(newTree);
              }
            }
          }
        }
      }
    } catch (NullPointerException e) {
      //System.err.println(entityClass.getName());
      /*System.err.println(e.getMessage());
      System.err.println(e.getCause() != null ? e.getCause() : "cause is null");*/
      // e.printStackTrace();
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }
    return nodes;
  }

}
