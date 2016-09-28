package es.usefulearnings.gui.controller;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.Company;
import es.usefulearnings.gui.view.AlertHelper;
import javafx.application.Platform;
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
import java.lang.reflect.Field;
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
  private TreeView<Node> companyOptions;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    _borderPane.setPrefSize(1024 - 35, 768 - 35);
    companyOptions = new TreeView<>();
    _borderPane.setCenter(new HBox(companyOptions));
    getCompanyDeclaredFields();
  }

  private void getCompanyDeclaredFields() {
    TreeItem<Node> root = new TreeItem<>(new Label("Company"));
    companyOptions.setRoot(root);
    try {
      for (Field field : Company.class.getDeclaredFields()) {
        if (field.getDeclaredAnnotation(EntityParameter.class)!= null) {
          String entityName = field.getDeclaredAnnotation(EntityParameter.class).name();
          ParameterType type = field.getDeclaredAnnotation(EntityParameter.class).parameterType();
          for (PropertyDescriptor pd : Introspector.getBeanInfo(Company.class).getPropertyDescriptors()) {
            if (pd.getName().equals(field.getName())) {
              if (type.equals(ParameterType.INNER_CLASS)) {
                CheckBox entityCB = new CheckBox(entityName);
                entityCB.setId(field.getName());
                TreeItem<Node> newTree = new TreeItem<>(entityCB);

                Collection<TreeItem<Node>> checkBoxes = getEntityFields(pd.getReadMethod().getReturnType());
                newTree.getChildren().addAll(checkBoxes);

                root.getChildren().add(newTree);
              } else if (type.equals(ParameterType.INNER_CLASS_COLLECTION)) {
                CheckBox arrayCB = new CheckBox(entityName);
                arrayCB.setId(field.getName());
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
      }
      companyOptions.getRoot().setExpanded(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * InnerFields for the InnerClasses
   * @param entityClass
   * @param <E>
   * @return a collection with all checkboxes for the entityClassParameter
   */
  private <E> Collection<TreeItem<Node>> getEntityFields(Class<E> entityClass) {
    Collection<TreeItem<Node>> nodes = new ArrayList<>();
    try {
      for (Field field : entityClass.getDeclaredFields()) {
        if (field.getDeclaredAnnotation(EntityParameter.class) != null) {
          String fieldName = field.getDeclaredAnnotation(EntityParameter.class).name();
          ParameterType parameterType = field.getDeclaredAnnotation(EntityParameter.class).parameterType();
          for (PropertyDescriptor pd : Introspector.getBeanInfo(entityClass).getPropertyDescriptors()) {
            if (pd.getName().equals(field.getName())) {

              if (!parameterType.equals(ParameterType.YAHOO_FIELD_COLLECTION) && !parameterType.equals(ParameterType.INNER_CLASS)) {
                CheckBox checkBox = new CheckBox(fieldName);
                checkBox.setId("Company." + fieldName.toLowerCase().replaceAll(" ", ""));
                nodes.add(new TreeItem<>(checkBox));
              } else {

                if (parameterType.equals(ParameterType.YAHOO_FIELD_COLLECTION)) {
                  CheckBox arrayCB = new CheckBox(fieldName);
                  arrayCB.setId("Company." + fieldName.toLowerCase().replaceAll(" ", ""));
                  TreeItem<Node> newTree = new TreeItem<>(arrayCB);

                  Collection<TreeItem<Node>> moreNodes = getEntityFields(
                    (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]
                  );
                  newTree.getChildren().addAll(moreNodes);

                  nodes.add(newTree);
                }

                if (parameterType.equals(ParameterType.INNER_CLASS)) {
                  Class<?> innerEntityClass = pd.getReadMethod().getReturnType();

                  CheckBox innerClassCB = new CheckBox(fieldName);
                  innerClassCB.setId("Company." + fieldName.toLowerCase().replaceAll(" ", ""));
                  TreeItem<Node> newTree = new TreeItem<>(innerClassCB);

                  newTree.getChildren().addAll(getEntityFields(innerEntityClass));
                  nodes.add(newTree);
                }
              }
            }
          }
        }
      }
    } catch (NullPointerException | IntrospectionException e) {
      Platform.runLater(()-> {
        AlertHelper.showExceptionAlert(e);
        e.printStackTrace();
      });
    }
    return nodes;
  }

}
