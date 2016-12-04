package es.usefulearnings.gui.view;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.EntityParameterBeanWalker;
import es.usefulearnings.engine.connection.YahooFinanceAPI;
import es.usefulearnings.engine.plugin.HistoricalDataPlugin;
import es.usefulearnings.engine.plugin.PluginException;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;
import es.usefulearnings.entities.company.CompanyData;
import es.usefulearnings.entities.company.HistoricalData;
import es.usefulearnings.gui.animation.OverWatchLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

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
    final Stage dialogStage = new Stage();
    dialogStage.setTitle(company.getSymbol());
    dialogStage.initModality(Modality.WINDOW_MODAL);
    Scene scene = null;
    try {
      BorderPane borderPane = new BorderPane();
      borderPane.setCenter(CompanyViewHelper.getInstance().getView(company));
      borderPane.setPrefSize(800, 600);
      scene = new Scene(borderPane);
    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
      AlertHelper.showExceptionAlert(e);
      e.printStackTrace();
    }
    dialogStage.setScene(scene);
    // Show the dialog and wait until the user closes it
    dialogStage.show();
  }

  @Override
  public Node getView(Company object)
    throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
    return getViewForObject(object);
  }

  @Override
  public FilterView getFilterableView() throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
    return new FilterView(Company.class);
  }

  private Node getViewForObject(Object object)
    throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    final GridPane gridPane = new GridPane();
    gridPane.setHgap(20);
    gridPane.setPadding(new Insets(5, 5, 5, 5));
    final Accordion accordion = new Accordion();
    EntityParameterBeanWalker worker = new EntityParameterBeanWalker(
      (field, annotation, method, position) -> {
        EntityParameter parameterDescriptor = ((EntityParameter) annotation);
        ParameterType parameterType = parameterDescriptor.parameterType();
        String entityName = parameterDescriptor.name();
        Label entityNameLabel = new Label(entityName + ": ");
        switch (parameterType) {
          case INNER_CLASS:
            Object innerObject = method.invoke(object);
            if (innerObject != null && innerObject instanceof CompanyData) {
              if (((CompanyData) innerObject).isSet()) {
                Core.getInstance().runLater(() -> {
                  ScrollPane pane = null;
                  try {
                    pane = new ScrollPane(getViewForObject(innerObject));
                  } catch (IntrospectionException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                  }
                  if (pane != null) {
                    final TitledPane titledPane = new TitledPane(entityName, pane);
                    titledPane.setCache(true);
                    titledPane.setCacheHint(CacheHint.SPEED);
                    Platform.runLater(() -> accordion.getPanes().add(titledPane));
                  }
                });
              }
            }
            break;

          case INNER_CLASS_COLLECTION:
            Collection<Object> innerClassCollection = (Collection<Object>) method.invoke(object);
            if (innerClassCollection != null && innerClassCollection.size() > 0) {
              Accordion accordionForCollection = new Accordion();
              ScrollPane scrollPaneForAccordion = new ScrollPane(new Label("Loading..."));
              for (Object objectLink : innerClassCollection) {
                if (objectLink != null) {
                  TitledPane innerTittledPane = new TitledPane();
                  Core.getInstance().runLater(() -> {
                    try {
                      innerTittledPane.setText(entityName);
                      innerTittledPane.setContent(new ScrollPane(getViewForObject(objectLink)));
                    } catch (IntrospectionException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                      e.printStackTrace();
                    }
                    if (innerTittledPane.getContent() != null) {
                      synchronized (accordionForCollection) {
                        accordionForCollection.getPanes().add(innerTittledPane);
                      }
                    }
                  });
                }
              }
              Platform.runLater(() -> scrollPaneForAccordion.setContent(accordionForCollection));
              accordion.getPanes().add(new TitledPane(entityName, scrollPaneForAccordion));
            }
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
            YahooLongFormatField longFormatField = (YahooLongFormatField) method.invoke(object);
            if (longFormatField != null)
              gridPane.add(new Label(longFormatField.getLongFmt()), 1, position);
            break;

          case YAHOO_FIELD_DATE_COLLECTION:
            gridPane.add(entityNameLabel, 0, position);
            Collection<YahooField> collection = (Collection<YahooField>) method.invoke(object);
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

          case RAW_DATE:
            Object rawDate = method.invoke(object);
            gridPane.add(entityNameLabel, 0, position);
            if (rawDate != null) {
              String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date((long) rawDate * 1000L));
              gridPane.add(new Label(date), 1, position);
            }
            break;

          case RAW_DATE_COLLECTION:
          case IGNORE:
            break;


          case HISTORICAL_DATA:
            HistoricalData historicalDatum = (HistoricalData) method.invoke(object);
            VBox vBox = new VBox(new OverWatchLoader());
            vBox.setAlignment(Pos.CENTER);
            if (historicalDatum != null && !historicalDatum.isEmpty()) {
              Node chart = ChartHelper.getInstance().getView(((Company) object));
              vBox.getChildren().clear();
              Button erase = new Button("retry");
              erase.setOnAction(event -> reloadHistoricalView((Company) object, vBox));
              vBox.getChildren().addAll(erase, chart);
            } else {
              reloadHistoricalView((Company) object, vBox);
            }
            TitledPane titledPaneForChart = new TitledPane(entityName, vBox);
            accordion.getPanes().add(titledPaneForChart);
            break;

          default:
            throw new IllegalArgumentException("Wrong argument -> " + parameterType.name());
        }
      }
    );
    worker.walk(object.getClass());
    return new VBox(accordion, gridPane);
  }

  private void reloadHistoricalView(Company company, VBox vBox) {
    Label label = new Label("No historical data was found or loaded...");
    final DatePicker startDate = new DatePicker(LocalDate.ofEpochDay(LocalDate.now().toEpochDay() - 365));
    final DatePicker endDate = new DatePicker(LocalDate.now());
    final ComboBox<YahooFinanceAPI.Range> ranges = new ComboBox<>();
    ranges.setItems(FXCollections.observableArrayList(YahooFinanceAPI.Range.values()));
    ranges.setValue(ranges.getItems().get(0));
    final Button showOnWindow = new Button("Show on window");
    showOnWindow.setOnAction(event -> Core.getInstance().runLater(()->{
      final HistoricalDataPlugin plugin = new HistoricalDataPlugin(
          startDate.getValue().toEpochDay() * 86400L,
          endDate.getValue().toEpochDay() * 86400L,
          ranges.getValue()
        );
      try {
        plugin.addInfo(company);
        Platform.runLater(() -> {
          try {
            Node chart = ChartHelper.getInstance().getView(company);
            vBox.getChildren().clear();
            vBox.getChildren().add(chart);
            ChartHelper.getInstance().showOnWindow(company);
          } catch (IntrospectionException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
          }
        });
      } catch (PluginException e){e.printStackTrace();}
    }));
    final HBox dates = new HBox(startDate, endDate);
    dates.setAlignment(Pos.CENTER);
    final Button reloadChartButton = new Button("Retry");
    reloadChartButton.setOnAction(getReloadHistoricalEventHandler(company, vBox, startDate, endDate, ranges));
    final HBox buttons = new HBox(showOnWindow, reloadChartButton);
    buttons.setAlignment(Pos.CENTER);
    vBox.getChildren().clear();
    vBox.getChildren().addAll(label, dates, ranges, buttons);
  }

  private EventHandler<ActionEvent> getReloadHistoricalEventHandler(
    Company company,
    VBox vBox,
    DatePicker startDate,
    DatePicker endDate,
    ComboBox<YahooFinanceAPI.Range> ranges
  ) {
    return event -> {
      vBox.getChildren().clear();
      vBox.getChildren().add(new OverWatchLoader(javafx.scene.paint.Color.web("#400090")));

      Core.getInstance().runLater(() -> {
        final HistoricalDataPlugin plugin = new HistoricalDataPlugin(
          startDate.getValue().toEpochDay() * 86400L,
          endDate.getValue().toEpochDay() * 86400L,
          ranges.getValue()
        );
        try {
          plugin.addInfo(company);
          Platform.runLater(() -> {
            try {
              Node chart = ChartHelper.getInstance().getView(company);
              vBox.getChildren().clear();
              vBox.getChildren().add(chart);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
              e.printStackTrace();
              vBox.getChildren().clear();
              vBox.getChildren().addAll(new Label("Error loading historical data..."));
            }

          });
        } catch (PluginException e) {
          e.printStackTrace();
        }
      });
    };
  }
}
