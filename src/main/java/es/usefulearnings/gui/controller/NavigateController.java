package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.entities.Stock;
import es.usefulearnings.gui.Main;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.gui.view.CompanyViewHelper;
import es.usefulearnings.gui.animation.OverWatchLoader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * First on the Vistas to be shown
 * it will contan
 * @author Yago on 04/09/2016.
 */
public class NavigateController implements Initializable {
  @FXML
  private ChoiceBox<String> stocksChoiceBox;
  @FXML
  private ListView<String> companies;
  @FXML
  private Tab webTab;
  @FXML
  private TabPane tabPane;
  @FXML
  private TextField textFilter;
  @FXML
  private Button refresh;

  @FXML
  private BorderPane navigatePane;

  private WebEngine webEngine;

  private ObservableList<String> symbols;
  private ChangeListener<String> stockListener;

  public void initialize(URL location, ResourceBundle resources) {
    stocksChoiceBox.getStyleClass().addAll("ue-choice-box");

    ImageView refreshIcon = new ImageView(new javafx.scene.image.Image(Main.class.getResourceAsStream("icons/refresh.png"), 20, 20, true, true));
    refresh.setGraphic(refreshIcon);

    webTab.setClosable(false);
    WebView webView = new WebView();
    webTab.setContent(webView);
    webEngine = webView.getEngine();
    webEngine.load("https://github.com/yrodrigez/UsefulEarnings/blob/master/README.md");

    textFilter.textProperty().addListener(getSymbolsFilter());

    stockListener = getStockListener();
    companies.getSelectionModel().selectedItemProperty().addListener(stockListener);

    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

    getStocks();
  }

  private ChangeListener<String> getSymbolsFilter(){
    return (observable, oldSymbolEntry, newSymbolEntry) -> {
      //remove the listener from the ListView so when it change don't crash
      companies.getSelectionModel().selectedItemProperty().removeListener(stockListener);
      if (oldSymbolEntry != null && (newSymbolEntry.length() < oldSymbolEntry.length())) {
        companies.setItems(symbols);
      }

      String value = newSymbolEntry.toUpperCase();
      ObservableList<String> filteredSymbols = FXCollections.observableArrayList();
      companies.getItems().forEach(symbol -> {
        if (symbol.toUpperCase().contains(value)) {
          filteredSymbols.add(symbol);
        }
      });
      companies.setItems(filteredSymbols);
      // add again the listener this is really important!!!
      companies.getSelectionModel().selectedItemProperty().addListener(stockListener);
    };
  }


  private void getStocks() {

    Core.getInstance().setStocksFromFolder();
    List<Stock> stocks = Core.getInstance().getStocks();

    symbols = FXCollections.observableArrayList(stocks.get(0).getCompanies().keySet()).sorted();

    ObservableList<String> stocksNames = FXCollections.observableArrayList();
    stocks.forEach(stock -> stocksNames.add(stock.getName()));
    stocksChoiceBox.setItems(stocksNames);
    stocksChoiceBox.getSelectionModel().select(0);
    stocksChoiceBox.getSelectionModel().selectedItemProperty().addListener(
        (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
          companies.getSelectionModel().selectedItemProperty().removeListener(stockListener);
          symbols = FXCollections.observableArrayList();
          stocks.forEach(stock -> {
            if (stock.getName().equals(newValue)) {
              symbols.addAll(stock.getCompanies().keySet());
            }
          });
          companies.setItems(symbols);
          companies.getSelectionModel().selectedItemProperty().addListener(stockListener);
        }
    );
    companies.setItems(symbols);
    companies.refresh();
  }

  @FXML
  public void refreshStocks(ActionEvent event) {
    getStocks();
    event.consume();
  }


  private Node getCompanyView(
      String symbol
  ) throws IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException {
    CompanyViewHelper companyViewHelper = CompanyViewHelper.getInstance();
    return companyViewHelper.getViewForEntity(Core.getInstance().getCompanyFromSymbol(symbol));
  }
  /**
   * @return Listener to handle the 'press' event on the main ListView (companies)
   * it will add the content of the
   */
  private ChangeListener<String> getStockListener() {
    return (observable, oldSymbol, newSymbol) -> {

      if (oldSymbol == null) {
        webEngine.load("http://finance.yahoo.com/quote/" + newSymbol);
      } else {
        if (!oldSymbol.equals(newSymbol)) {
          webEngine.load("http://finance.yahoo.com/quote/" + newSymbol);
        }
      }
      if(Core.getInstance().isDataLoaded()) {
        Tab cTab = new Tab(newSymbol);

        cTab.setContent(new OverWatchLoader(Color.web("#400090")).getLoader());

        new Thread(() -> {
          try {
            Node companyData = getCompanyView(newSymbol);
            Platform.runLater(() -> cTab.setContent(companyData));
          } catch (IllegalAccessException | IntrospectionException | InvocationTargetException | InstantiationException e) {
            Platform.runLater(() -> AlertHelper.showExceptionAlert(e));
            e.printStackTrace();
          }
        }).start();
        tabPane.getTabs().add(cTab);
      } //else No data loaded
    };
  }

}
