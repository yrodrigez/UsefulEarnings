package es.yahoousefulearnings.gui.main;

import es.yahoousefulearnings.entities.Stock;
import es.yahoousefulearnings.utils.ResourcesHelper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class Controller implements Initializable {

  @FXML
  private ChoiceBox<String> stocksChoiceBox;
  @FXML
  private ListView<String> companies;
  @FXML
  private BorderPane borderPane;

  private ObservableList<String> symbols;

  public void initialize(URL location, ResourceBundle resources) {

    ResourcesHelper resourcesHelper = ResourcesHelper.getInstance();

    TreeMap<String, Stock> stocksMap = resourcesHelper.getAvailableStocks();

    symbols = FXCollections.observableArrayList(stocksMap.firstEntry().getValue().getSymbols().keySet());
    companies.setItems(symbols);

    ObservableList<String> stocksNames = FXCollections.observableArrayList(stocksMap.keySet());
    stocksChoiceBox.setItems(stocksNames);
    stocksChoiceBox.getSelectionModel().select(0);

    stocksChoiceBox.getSelectionModel().selectedItemProperty().addListener(
        (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
          symbols = FXCollections.observableArrayList(stocksMap.get(newValue).getSymbols().keySet());
          companies.setItems(symbols);
        }
    );

    companies.getSelectionModel().selectedItemProperty().addListener(

      (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        if(oldValue == null) {
          webEngine.load("http://finance.yahoo.com/quote/" + newValue);
        }else {
          if (!oldValue.equals(newValue)){
            webEngine.load("http://finance.yahoo.com/quote/" + newValue);
          }
        }

        borderPane.setCenter(webView);
      }

    );

  }
}
