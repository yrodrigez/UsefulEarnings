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
import javafx.scene.web.WebView;
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
    throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    GridPane gridPane = new GridPane();
    gridPane.setHgap(20);
    gridPane.setPadding(new Insets(5, 5, 5, 5));
    Accordion accordion = new Accordion();
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
                    TitledPane titledPane = new TitledPane(entityName, pane);
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
              Accordion collectionAccordion = new Accordion();
              ScrollPane collectionScrollPane = new ScrollPane(new Label("Loading..."));
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
                    if (innerTittledPane != null && innerTittledPane.getContent() != null) {
                      collectionAccordion.getPanes().add(innerTittledPane);
                    }
                  });
                }
              }
              Platform.runLater(() -> collectionScrollPane.setContent(collectionAccordion));
              accordion.getPanes().add(new TitledPane(entityName, collectionScrollPane));
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
              Node chart = getChart(((Company) object).getHistoricalData());
              vBox.getChildren().clear();
              Button erase = new Button("retry");
              erase.setOnAction(event -> setReloadHistoricalMiniView((Company) object, vBox));
              vBox.getChildren().addAll(erase, chart);
            } else {
              setReloadHistoricalMiniView((Company) object, vBox);
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

  private void setReloadHistoricalMiniView(Company company, VBox vBox) {
    Label label = new Label("No historical data was found or loaded...");
    Button reloadHistoricalData = new Button("Retry");
    DatePicker startDate = new DatePicker(LocalDate.ofEpochDay(LocalDate.now().toEpochDay() - 365));
    DatePicker endDate = new DatePicker(LocalDate.now());
    ComboBox<YahooFinanceAPI.Range> ranges = new ComboBox<>();
    ranges.setItems(FXCollections.observableArrayList(YahooFinanceAPI.Range.values()));
    ranges.setValue(ranges.getItems().get(0));
    reloadHistoricalData.setOnAction(getReloadHistoricalEventHandler(company, vBox, startDate, endDate, ranges));
    vBox.getChildren().clear();
    HBox dates = new HBox(startDate, endDate);
    dates.setAlignment(Pos.CENTER);
    vBox.getChildren().addAll(label, dates, ranges, reloadHistoricalData);
  }

  private String getWebCandleChart(HistoricalData historicalData) {
    // ['Mon', 20, 28, 38, 45]
    StringBuilder data = new StringBuilder();
    historicalData.getHistoricalDatum().forEach(historical -> {
      data.append('[');
      data.append('\'');
      data.append(historical.getDate());
      data.append('\'');
      data.append(',');
      data.append(historical.getHigh());
      data.append(',');
      data.append(historical.getOpen());
      data.append(',');
      data.append(historical.getClose());
      data.append(',');
      data.append(historical.getLow());
      data.append("],");
    });

    int size = historicalData.getDate().size() > 200 ? 8192 : 1900;
    System.out.println(size);
    return "<html><head><script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script><script type=\"text/javascript\">google.charts.load('current', {'packages':['corechart']});google.charts.setOnLoadCallback(drawChart);function drawChart() {var data = google.visualization.arrayToDataTable(["
      + data.substring(0, data.length() - 1) +
      "], true);var options = {legend:'none', candlestick: {fallingColor: { strokeWidth: 0, fill: '#ff0000' },risingColor: { strokeWidth: 0, fill: '#00ff00' }}};var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));chart.draw(data, options);}</script></head><body><div id=\"chart_div\" style=\"width: " + size + "px; height: 700px;\"></div></body></html>";
  }

  private EventHandler<ActionEvent> getReloadHistoricalEventHandler(
    Company company,
    VBox vBox,
    DatePicker startDate,
    DatePicker endDate,
    ComboBox<YahooFinanceAPI.Range> ranges
  ) {
    return event -> {
      new Thread(() -> {
        HistoricalDataPlugin plugin = new HistoricalDataPlugin(
          startDate.getValue().toEpochDay() * 86400L,
          endDate.getValue().toEpochDay() * 86400L,
          ranges.getValue()
        );
        try {
          plugin.addInfo(company);
          Platform.runLater(() -> {
            Node chart = getChart(company.getHistoricalData());
            vBox.getChildren().clear();
            vBox.getChildren().add(chart);
          });
        } catch (PluginException e) {
          e.printStackTrace();
        }
      }).start();
      vBox.getChildren().clear();
      vBox.getChildren().add(new OverWatchLoader(javafx.scene.paint.Color.web("#400090")));
    };
  }

  private Node getChart(HistoricalData historicalDatum) {
    WebView webView = new WebView();
    Platform.runLater(() -> {
      final Axis axis = new Axis();
      final double SCALE_DELTA = 1.1;
      webView.setOnScroll(event -> {
        event.consume();
        if (event.getDeltaY() == 0) {
          return;
        }

        double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

        webView.setZoom(webView.getZoom() * scaleFactor);
        webView.setZoom(webView.getZoom() * scaleFactor);
      });

      webView.setOnMousePressed(event -> {
        if (event.getClickCount() == 2) {
          webView.setZoom(0);
          webView.setZoom(0);
        } else {
          axis.orgSceneX = event.getSceneX();
          axis.orgSceneY = event.getSceneY();

          axis.orgTranslateX = webView.getTranslateX();
          axis.orgTranslateY = webView.getTranslateY();
        }
      });


      webView.getEngine().loadContent(getWebCandleChart(historicalDatum));
    });
    return webView;
  }

  private class Axis {
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
  }
}
