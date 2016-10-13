package es.usefulearnings.gui.view;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.EntityParameterBeanWorker;
import es.usefulearnings.engine.filter.Filter;
import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Yago on 01/10/2016.
 */
public class FilterViewHelper implements ViewHelper<Filter> {

  private static FilterViewHelper _instance = new FilterViewHelper();

  private FilterViewHelper() {
  }

  public static FilterViewHelper getInstance() {
    return _instance;
  }

  @Override
  public Node getViewForEntity(
    Filter filter
  ) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {

    TableView<Map<String, String>> tableView = new TableView<>();
    tableView.setEditable(false);
    tableView.setStyle("-fx-background-color: white");

    ArrayList<Map<String, String>> valuesArray = new ArrayList<>();
    for(Object object : filter.getEntities()) {
      Map<String, String> values = getValuesAsStringsForObject(object);
      valuesArray.add(values);
    }

    for(Object o : filter.getEntities()){
      List<String> headers = getHeaders(o);
      for (String s :headers){
        TableColumn<Map<String, String>, String> tableColumn = new TableColumn<>(s);
        tableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(s)));

        tableView.getColumns().add(tableColumn);
      }
      break;
    }

    tableView.getItems().addAll(valuesArray);
    return tableView;
  }

  @Override
  public void showOnWindow(Filter filter) {
    Stage dialogStage = new Stage();
    dialogStage.setTitle(filter.toString());
    dialogStage.initModality(Modality.WINDOW_MODAL);
    BorderPane borderPane = new BorderPane(new Label("Loading data..."));
    borderPane.setPrefSize(1400, 768);
    Scene scene = new Scene(borderPane);
    new Thread(() -> {
      try {
        Node view = FilterViewHelper.getInstance().getViewForEntity(filter);
        Platform.runLater(() -> {
          ScrollPane scrollPane = new ScrollPane(view);
          scrollPane.setStyle("-fx-background-color: #ffffff;");
          scrollPane.setFitToHeight(true);
          scrollPane.setFitToWidth(true);
          borderPane.setCenter(scrollPane);
          borderPane.prefHeightProperty().bind(scene.heightProperty());
          borderPane.prefWidthProperty().bind(scene.widthProperty());
        });
      } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
        AlertHelper.showExceptionAlert(e);
        e.printStackTrace();
      }
    }).start();
    dialogStage.setScene(scene);
    dialogStage.showAndWait();
  }

  @SuppressWarnings("unchecked")
  private Map<String, String> getValuesAsStringsForObject(
    Object object
  ) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    Map<String, String> values = new HashMap<>();
    EntityParameterBeanWorker worker = new EntityParameterBeanWorker(
      (field, annotation, method, position) -> {
        EntityParameter parameter = (EntityParameter) annotation;
        String header = parameter.name();
        boolean isMaster = parameter.isMaster();
        ParameterType parameterType = parameter.parameterType();
        if (isMaster) {
          switch (parameterType) {
            case INNER_CLASS:
              Object innerClass;
              if ((innerClass = method.invoke(object)) != null)
                values.putAll(getValuesAsStringsForObject(innerClass));
              else values.put(header, "");
              break;

            case INNER_CLASS_COLLECTION:
              ArrayList<Object> innerClassCollection;
              if ((innerClassCollection = (ArrayList<Object>) method.invoke(object)) != null) {
                for (Object anInnerClassCollection : innerClassCollection) {
                  Object innerObject = method.invoke(anInnerClassCollection);
                  values.putAll(getValuesAsStringsForObject(innerObject));
                }
              } else values.put(header, "");
              break;

            case RAW_NUMERIC:
              Object rawNum;
              if ((rawNum = method.invoke(object)) != null) {
                values.put(header, rawNum.toString());
              }  else values.put(header, "");
              break;

            case URL:
            case IGNORE:
            case RAW_STRING:
              String string;
              if ((string = (String) method.invoke(object)) != null)
                values.put(header, string);
              else values.put(header, "");
              break;

            case YAHOO_FIELD_DATE:
              YahooField yahooField;
              if ((yahooField = (YahooField) method.invoke(object)) != null) {
                String dateLabel = yahooField.getFmt();
                values.put(header, dateLabel);
              }  else values.put(header, "");
              break;

            case YAHOO_FIELD_DATE_COLLECTION:
              Collection<YahooField> collection;
              if ((collection = (Collection<YahooField>) method.invoke(object)) != null) {
                Label datesLabel = YahooFieldNodeRetriever.getInstance().getYahooDateCollectionLabel(collection);
                values.put(header, datesLabel.getText());
              }  else values.put(header, "");
              break;

            case YAHOO_FIELD_NUMERIC:
              YahooField numericField;
              if ((numericField = (YahooField) method.invoke(object)) != null) {
                values.put(header, numericField.getFmt());
              }  else values.put(header, "");
              break;

            case YAHOO_LONG_FORMAT_FIELD:
              YahooLongFormatField longFormatField;
              if ((longFormatField = (YahooLongFormatField) method.invoke(object)) != null) {
                values.put(header, longFormatField.getLongFmt());
              }  else values.put(header, "");
              break;


            default:
              throw new IllegalArgumentException(
                "FilterViewHelper::getViewForEntity -> no case defined for " + parameterType.name()
              );
          }
        }
      }
    );

    worker.walk(object.getClass());
    return values;
  }

  private ArrayList<String> getHeaders(
    Object entity
  ) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    ArrayList<String> labels = new ArrayList<>();
    EntityParameterBeanWorker worker = new EntityParameterBeanWorker(
      (field, annotation, method, position) -> {
        EntityParameter parameter = (EntityParameter) annotation;
        String header = parameter.name();
        boolean isMaster = parameter.isMaster();
        ParameterType parameterType = parameter.parameterType();
        if (isMaster) {
          switch (parameterType) {
            case INNER_CLASS:
              Object innerClass;
              if((innerClass = method.invoke(entity)) != null)
                labels.addAll(getHeaders(innerClass));
              break;

            case INNER_CLASS_COLLECTION:
              break;

            default:
              labels.add(header);
          }
        }
      });
    worker.walk(entity.getClass());
    return labels;
  }
}
