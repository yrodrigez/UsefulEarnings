package es.usefulearnings.gui.view;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.EntityParameterBeanWorker;
import es.usefulearnings.engine.filter.Filter;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
    Filter filter) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
    GridPane gridPane = new GridPane();
    gridPane.setHgap(20);
    gridPane.setStyle("-fx-background-color: white");

    Iterator it = filter.getEntities().iterator();
    int i = 0;
    while (it.hasNext()) {
      Object entity = it.next();

      if (i == 0) {
        ArrayList<Label> labels = setHeader(entity);
        for (int j = 0; j < labels.size(); j++) {
          gridPane.add(labels.get(j), j, i);
        }
        i++;
      }

      ArrayList<Label> labels = getViewForObject(entity);
      for (int j = 0; j < labels.size(); j++) {
        gridPane.add(labels.get(j), j, i);

        if (labels.size() == j+1) {
          Button detailsButton = new Button("details");
          detailsButton.setStyle(
            "-fx-background-color: #400090;"
            + "-fx-text-fill: white;"
            + "-fx-background-radius: 0%;"
            + "-fx-border-color: transparent;"
          );
          detailsButton.setOnAction(
            event -> {
              CompanyViewHelper.getInstance().showOnWindow((Company) entity);
              event.consume();
            }
          );
          gridPane.add(detailsButton, j + 1, i);
        }
      }
      i++;
    }
    return gridPane;
  }

  @Override
  public FilterView getEntityFilterView() throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
    return null;
  }

  @Override
  public void showOnWindow(Filter filter) {
    Stage dialogStage = new Stage();
    dialogStage.setTitle(filter.toString());
    dialogStage.initModality(Modality.WINDOW_MODAL);
    BorderPane borderPane = new BorderPane(new Label("Loading data..."));
    borderPane.setPrefSize(700, 600);
    Scene scene = new Scene(borderPane);
    new Thread(() -> {
      try {
        Node view = FilterViewHelper.getInstance().getViewForEntity(filter);
        Platform.runLater(()->{
          ScrollPane scrollPane = new ScrollPane(view);
          scrollPane.setStyle("-fx-background-color: #ffffff;");

          borderPane.setCenter(new ScrollPane(view));
          borderPane.getCenter().autosize();
        });
      } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
        AlertHelper.showExceptionAlert(e);
        e.printStackTrace();
      }
    }).start();
    dialogStage.setScene(scene);
    // Show the dialog and wait until the user closes it
    dialogStage.showAndWait();
  }


  @SuppressWarnings("unchecked")
  private ArrayList<Label> getViewForObject(Object object) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    ArrayList<Label> labels = new ArrayList<>();
    EntityParameterBeanWorker worker = new EntityParameterBeanWorker(
      (field, annotation, method, position) -> {
        EntityParameter parameter = (EntityParameter) annotation;
        boolean isMaster = parameter.isMaster();
        ParameterType parameterType = parameter.parameterType();
        if (isMaster) {
          switch (parameterType) {
            case INNER_CLASS:
              labels.addAll(getViewForObject(method.invoke(object)));
              break;

            case INNER_CLASS_COLLECTION:
              ArrayList<Object> innerClassCollection = (ArrayList<Object>) method.invoke(object);
              for (Object anInnerClassCollection : innerClassCollection) {
                Object innerObject = method.invoke(anInnerClassCollection);
                labels.addAll(getViewForObject(innerObject));
              }
              break;

            case RAW_NUMERIC:
              labels.add(new Label((method.invoke(object)).toString()));
              break;

            case URL:
            case IGNORE:
            case RAW_STRING:
              labels.add(new Label((String) method.invoke(object)));
              break;

            case YAHOO_FIELD_DATE:
              YahooField yahooField = (YahooField) method.invoke(object);
              Label dateLabel = YahooFieldNodeRetriever.getInstance().getYahooDateLabel(yahooField);
              if (yahooField != null)
                labels.add(dateLabel);
              break;

            case YAHOO_FIELD_DATE_COLLECTION:
              Collection<YahooField> collection = (Collection<YahooField>) method.invoke(object);
              Label datesLabel = YahooFieldNodeRetriever.getInstance().getYahooDateCollectionLabel(collection);
              labels.add(datesLabel);
              break;

            case YAHOO_FIELD_NUMERIC:
              Label numericLabel =
                YahooFieldNodeRetriever.getInstance().getYahooFieldNumericLabel((YahooField) method.invoke(object));
              labels.add(numericLabel);
              break;

            case YAHOO_LONG_FORMAT_FIELD:
              Label longFormatLabel =
                YahooFieldNodeRetriever.getInstance().getYahooLongFormatLabel((YahooLongFormatField) method.invoke(object));
              labels.add(longFormatLabel);
              break;


            default:
              throw new IllegalArgumentException("FilterViewHelper::getViewForEntity -> no case defined for " + parameterType.name());
          }
        }
      }
    );

    worker.walk(object.getClass());
    return labels;
  }

  private ArrayList<Label> setHeader(Object entity) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    ArrayList<Label> labels = new ArrayList<>();
    EntityParameterBeanWorker worker = new EntityParameterBeanWorker(
      (field, annotation, method, position) -> {
        EntityParameter parameter = (EntityParameter) annotation;
        String header = parameter.name();
        boolean isMaster = parameter.isMaster();
        ParameterType parameterType = parameter.parameterType();
        if (isMaster) {
          switch (parameterType) {
            case INNER_CLASS:
              labels.addAll(setHeader(method.invoke(entity)));
              break;

            case INNER_CLASS_COLLECTION:
              break;

            default:
              labels.add(new Label(header));
          }
        }
      });
    worker.walk(entity.getClass());
    return labels;
  }
}
