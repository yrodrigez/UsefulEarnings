package es.usefulearnings.gui.view;

import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.HistoricalData;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Yago on 04/12/2016.
 */
public class ChartHelper implements ViewHelper<Company> {

  private static ChartHelper _instance = new ChartHelper();

  public static ChartHelper getInstance() {
    return _instance;
  }

  private ChartHelper() {
  }

  @Override
  public Node getView(Company company) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
    return getChart(company.getHistoricalData());
  }

  @Override
  public void showOnWindow(Company company) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
    final Stage dialogStage = new Stage();
    dialogStage.setTitle(company.getSymbol());
    dialogStage.initModality(Modality.WINDOW_MODAL);

    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(getView(company));
    borderPane.setPrefSize(1920, 800);
    Scene scene = new Scene(borderPane);

    dialogStage.setScene(scene);
    // Show the dialog and wait until the user closes it
    dialogStage.show();
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
