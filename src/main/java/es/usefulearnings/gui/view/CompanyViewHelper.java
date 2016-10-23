package es.usefulearnings.gui.view;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.EntityParameterBeanWorker;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * View manager of CompanyViewHelper's data
 *
 * @author Yago Rodríguez
 */
public class CompanyViewHelper implements ViewHelper<Company>, FilterableView {
  private static CompanyViewHelper _instance = new CompanyViewHelper();

  public static CompanyViewHelper getInstance() {
    return _instance;
  }

  private CompanyViewHelper() {
  }

  @Override
  public void showOnWindow(Company company) {
    Stage dialogStage = new Stage();
    dialogStage.setTitle(company.getSymbol());
    dialogStage.initModality(Modality.WINDOW_MODAL);
    Scene scene = null;
    try {
      BorderPane borderPane = new BorderPane();
      borderPane.setCenter(CompanyViewHelper.getInstance().getViewForEntity(company));
      borderPane.setPrefSize(800, 600);
      scene = new Scene(borderPane);
    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
      AlertHelper.showExceptionAlert(e);
      e.printStackTrace();
    }
    dialogStage.setScene(scene);
    // Show the dialog and wait until the user closes it
    dialogStage.showAndWait();
  }

  @Override
  public Node getViewForEntity(Company object)
    throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
    return getViewForObject(object);
  }

  @Override
  public FilterView getFilterableView() throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
    return new FilterView(Company.class);
  }

  private Node getViewForObject(Object object)
    throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException
  {
    GridPane gridPane = new GridPane();
    gridPane.setHgap(20);
    gridPane.setPadding(new Insets(5, 5, 5, 5));
    Accordion accordion = new Accordion();
    EntityParameterBeanWorker worker = new EntityParameterBeanWorker(
      (field, annotation, method, position) -> {
        EntityParameter parameterDescriptor = ((EntityParameter)annotation);
        ParameterType parameterType = parameterDescriptor.parameterType();
        String entityName = parameterDescriptor.name();
        Label entityNameLabel = new Label(entityName + ": ");
        switch (parameterType){
          case INNER_CLASS:
            Object innerObject = method.invoke(object);
            ScrollPane pane = new ScrollPane(
              getViewForObject(innerObject)
            );
            TitledPane titledPane = new TitledPane(entityName, pane);
            accordion.getPanes().add(titledPane);
            break;

          case INNER_CLASS_COLLECTION:
            Accordion collectionAccordion = new Accordion();
            ScrollPane collectionScrollPane = new ScrollPane(collectionAccordion);

            Collection<Object> innerClassCollection = (Collection<Object>) method.invoke(object);
            for (Object objectLink : innerClassCollection) {
              TitledPane innerTittledPane = new TitledPane(
                entityName,
                new ScrollPane(getViewForObject(objectLink))
              );
              collectionAccordion.getPanes().add(innerTittledPane);
            }
            accordion.getPanes().add(new TitledPane(entityName, collectionScrollPane));
            break;

          case YAHOO_FIELD_DATE:
          case YAHOO_FIELD_NUMERIC:
            gridPane.add(entityNameLabel, 0, position);
            YahooField yahooField = (YahooField) method.invoke(object);
            Label dateLabel = YahooFieldNodeRetriever.getInstance().getYahooDateLabel(yahooField);
            if (yahooField != null)
              gridPane.add(dateLabel, 1, position);
            break;

          case YAHOO_LONG_FORMAT_FIELD:
            gridPane.add(entityNameLabel, 0, position);
            YahooLongFormatField longFormatField = (YahooLongFormatField)method.invoke(object);
            if (longFormatField != null)
              gridPane.add(new Label(longFormatField.getLongFmt()), 1, position);
            break;

          case YAHOO_FIELD_DATE_COLLECTION:
            gridPane.add(entityNameLabel, 0, position);
            Collection<YahooField> collection = (Collection<YahooField>)method.invoke(object);
            Label datesLabel = YahooFieldNodeRetriever.getInstance().getYahooDateCollectionLabel(collection);
            gridPane.add(datesLabel, 1, position);
            break;

          case URL:
            gridPane.add(entityNameLabel, 0, position);
            String url = (String) method.invoke(object);
            if (url != null) {
              Hyperlink hyperlink = new Hyperlink(url);
              hyperlink.setOnAction(event -> {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                  try {
                    Desktop.getDesktop().browse(new URI(url));
                  } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                  }
                } else {
                  Platform.runLater(() -> AlertHelper.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Operation not supported",
                    "Sorry, you can't do this on your actual OS"
                  ));
                }
              });
              gridPane.add(hyperlink, 1, position);
            }
            break;

          case RAW_STRING:
          case RAW_NUMERIC:
            gridPane.add(entityNameLabel, 0, position);
            if (method.invoke(object) != null)
              gridPane.add(new Label(method.invoke(object).toString()), 1, position);
            break;

          case IGNORE:
            break;

          default:
            throw new IllegalArgumentException("Wrong argument -> " + parameterType.name());
        }
      }
    );
    worker.walk(object.getClass());
    return new VBox(accordion, gridPane);
  }
}
