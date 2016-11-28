package es.usefulearnings.gui.view;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.EntityParameterBeanWalker;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;
import es.usefulearnings.entities.company.HistoricalData;
import es.usefulearnings.gui.animation.OverWatchLoader;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
            ScrollPane pane = new ScrollPane(
              new OverWatchLoader(javafx.scene.paint.Color.web("#400090")).getLoader()
            );
            new Thread(() -> {
              try {
                Node n = getViewForObject(innerObject);
                Platform.runLater(()-> pane.setContent(n));
              } catch (IntrospectionException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
              }
            }).start();

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

          case IGNORE:
            break;

          case HISTORICAL_DATA:
            HistoricalData historicalDatum = (HistoricalData) method.invoke(object);
            if (historicalDatum != null) {
              final CategoryAxis xAxis = new CategoryAxis();
              final NumberAxis yAxis = new NumberAxis();
              xAxis.setLabel("Month");

              final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
              lineChart.setCreateSymbols(false);
              lineChart.setAnimated(false);
              lineChart.getStyleClass().add("thick-chart");
              lineChart.setTitle(historicalDatum.getSymbol());

              XYChart.Series adjClose = new XYChart.Series();
              adjClose.setName("Adj Close");

              XYChart.Series open = new XYChart.Series();
              open.setName("Open");

              XYChart.Series high = new XYChart.Series();
              high.setName("High");

              XYChart.Series low = new XYChart.Series();
              low.setName("Low");

              XYChart.Series close = new XYChart.Series();
              close.setName("Close");

              XYChart.Series volume = new XYChart.Series();
              volume.setName("Volume");

              for (int i = 0; i < historicalDatum.getDate().size(); i++) {
                String fmtDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(historicalDatum.getDate().get(i) * 1000));

                adjClose.getData().add(new XYChart.Data<>(
                    fmtDate,
                    historicalDatum.getAdj_close().get(i)
                  )
                );

                low.getData().add(
                  new XYChart.Data<>(
                    fmtDate,
                    historicalDatum.getLow().get(i)
                  )
                );

                high.getData().add(
                  new XYChart.Data<>(
                    fmtDate,
                    historicalDatum.getHigh().get(i)
                  )
                );

                close.getData().add(
                  new XYChart.Data<>(
                    fmtDate,
                    historicalDatum.getClose().get(i)
                  )
                );

                volume.getData().add(
                  new XYChart.Data<>(
                    fmtDate,
                    historicalDatum.getVolume().get(i)
                  )
                );

                open.getData().add(
                  new XYChart.Data<>(
                    fmtDate,
                    historicalDatum.getOpen().get(i)
                  )
                );
              }


              final double SCALE_DELTA = 1.1;
              lineChart.setOnScroll(event -> {
                event.consume();

                if (event.getDeltaY() == 0) {
                  return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

                lineChart.setScaleX(lineChart.getScaleX() * scaleFactor);
                lineChart.setScaleY(lineChart.getScaleY() * scaleFactor);
              });
              Events events = new Events();
              lineChart.setOnMousePressed(event -> {
                if (event.getClickCount() == 2) {
                  lineChart.setScaleX(1.0);
                  lineChart.setScaleY(1.0);
                } else {
                  events.orgSceneX = event.getSceneX();
                  events.orgSceneY = event.getSceneY();

                  events.orgTranslateX = lineChart.getTranslateX();
                  events.orgTranslateY = lineChart.getTranslateY();
                }
              });
              lineChart.setOnMouseDragged(event -> {
                double offsetX = event.getSceneX() - events.orgSceneX;
                double offsetY = event.getSceneY() - events.orgSceneY;
                double newTranslateX = events.orgTranslateX + offsetX;
                double newTranslateY = events.orgTranslateY + offsetY;

                lineChart.setTranslateX(newTranslateX);
                lineChart.setTranslateY(newTranslateY);
              });



              final XYChart.Series [] series = {open, high, low, close, volume, adjClose};
              lineChart.getData().addAll(series);

              Button openButton = new Button("X Open");
              openButton.setOnAction(event -> {
                if(lineChart.getData().contains(open)){
                  lineChart.getData().remove(open);
                  openButton.setText("add Open");
                } else {
                  lineChart.getData().add(open);
                  openButton.setText("X Open");
                }
              });

              Button highButton = new Button("X High");
              highButton.setOnAction(event -> {
                if(lineChart.getData().contains(high)){
                  lineChart.getData().remove(high);
                  highButton.setText("add High");
                } else {
                  lineChart.getData().add(high);
                  highButton.setText("X High");
                }
              });

              Button lowButton = new Button("X Low");
              lowButton.setOnAction(event -> {
                if(lineChart.getData().contains(low)){
                  lineChart.getData().remove(low);
                  lowButton.setText("add Low");
                } else {
                  lineChart.getData().add(low);
                  lowButton.setText("X Low");
                }
              });

              Button closeButton = new Button("X Close");
              closeButton.setOnAction(event -> {
                if(lineChart.getData().contains(close)){
                  lineChart.getData().remove(close);
                  closeButton.setText("add Close");
                } else {
                  lineChart.getData().add(close);
                  closeButton.setText("X Close");
                }
              });

              Button volumeButton = new Button("X Volume");
              volumeButton.setOnAction(event -> {
                if(lineChart.getData().contains(volume)){
                  lineChart.getData().remove(volume);
                  volumeButton.setText("add Volume");
                } else {
                  lineChart.getData().add(volume);
                  volumeButton.setText("X Volume");
                }
              });

              Button adjButton = new Button("X AdjClose");
              adjButton.setOnAction(event -> {
                if(lineChart.getData().contains(adjClose)){
                  lineChart.getData().remove(adjClose);
                  adjButton.setText("add AdjClose");
                } else {
                  lineChart.getData().add(adjClose);
                  adjButton.setText("X AdjClose");
                }
              });

              HBox buttons = new HBox(openButton, highButton, lowButton, closeButton, volumeButton, adjButton);
              buttons.setSpacing(20d);

              VBox vBox = new VBox(lineChart, buttons);

              TitledPane titledPaneForChart = new TitledPane(entityName, vBox);
              accordion.getPanes().add(titledPaneForChart);
            }
            break;

          default:
            throw new IllegalArgumentException("Wrong argument -> " + parameterType.name());
        }
      }
    );
    worker.walk(object.getClass());
    return new VBox(accordion, gridPane);
  }

  private class Events {
    public double orgSceneX, orgSceneY;
    public double orgTranslateX, orgTranslateY;
  }
}
