package es.yahoousefulearnings.gui.controller;

import es.yahoousefulearnings.engine.SearchEngine;
import es.yahoousefulearnings.engine.YahooLinks;
import es.yahoousefulearnings.entities.Company;
import es.yahoousefulearnings.entities.Stock;
import es.yahoousefulearnings.gui.view.CompanyViewController;
import es.yahoousefulearnings.utils.NoStocksFoundException;
import es.yahoousefulearnings.utils.ResourcesHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Main implements Initializable {

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

  private ObservableList<String> symbols;
  private ChangeListener<String> stockListener;
  private WebEngine webEngine;

  public void initialize(URL location, ResourceBundle resources) {

    ResourcesHelper resourcesHelper = null;
    try {
      resourcesHelper = ResourcesHelper.getInstance();
    } catch (NoStocksFoundException e) {
      //TODO Mostrar ventana avisando de que debe añadir stocks a la carpeta stocksPath
    }
    List<Stock> stocks = resourcesHelper.getAvailableStocks();

    symbols = FXCollections.observableArrayList(stocks.get(0).getSymbols()).sorted();
    companies.setItems(symbols);

    ObservableList<String> stocksNames = FXCollections.observableArrayList();
    stocks.forEach(stock -> stocksNames.add(stock.getName()));
    stocksChoiceBox.setItems(stocksNames);
    stocksChoiceBox.getSelectionModel().select(0);

    stocksChoiceBox.getSelectionModel().selectedItemProperty().addListener(
      (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        symbols = FXCollections.observableArrayList();
        stocks.forEach(stock -> {
          if (stock.getName().equals(newValue)){
            symbols.addAll(stock.getSymbols());
          }
        });
        companies.setItems(symbols);
      }
    );

    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    webTab.setClosable(false);
    WebView webView = new WebView();
    webTab.setContent(webView);
    webEngine = webView.getEngine();
    stockListener = getStockListener();
    textFilter.textProperty().addListener(getSymbolFilterListener());
    companies.getSelectionModel().selectedItemProperty().addListener(stockListener);
  }

  /**
   *
   * @return Listener that handle the press event on the main ListView (companies)
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

      Tab cTab = new Tab(newSymbol);
      cTab.setContent(setCompanyData(newSymbol));
      tabPane.getTabs().add(cTab);
    };
  }

  private Node setCompanyData(String symbol) {
    VBox vBox = new VBox();
    // insertDataHere!!
    Company company = SearchEngine.getCompanyData(
      symbol,
      YahooLinks.COMPANY_ASSET_PROFILE,
      YahooLinks.COMPANY_FINANCIAL_DATA,
      YahooLinks.COMPANY_DEFAULT_KEY_STATISTICS,
      YahooLinks.COMPANY_CALENDAR_EVENTS,
      YahooLinks.COMPANY_INCOME_STATEMENT_HISTORY,
      YahooLinks.COMPANY_CASHFLOW_STATEMENT_HISTORY,
      YahooLinks.COMPANY_BALANCE_SHEET_HISTORY
    );

    vBox.getChildren().addAll(
      new Label("Company's Symbol: " + company.getSymbol()),
      new Separator(Orientation.HORIZONTAL)
    );

    CompanyViewController companyViewManager = new CompanyViewController(company, webEngine);
    vBox.getChildren().addAll(companyViewManager.setView());

    return new ScrollPane(vBox);
  }



  /**
   * Listener to a TextField that filters the main Symbols ListView but first removes the listener from it
   * so it won't crash
   * @return ChangeListener
   */
  private ChangeListener<String> getSymbolFilterListener() {
    return (observable, oldSymbolEntry, newSymbolEntry) -> {
      //remove the listener from the ListView so when it change don't crash
      companies.getSelectionModel().selectedItemProperty().removeListener(stockListener);
      if (oldSymbolEntry != null && (newSymbolEntry.length() < oldSymbolEntry.length())) {
        companies.setItems(symbols);
      }

      String value = newSymbolEntry.toUpperCase();
      ObservableList<String> filteredSymbols = FXCollections.observableArrayList();
      companies.getItems().forEach( symbol -> {
        if (symbol.toUpperCase().contains(value)) {
          filteredSymbols.add(symbol);
        }
      });
      companies.setItems(filteredSymbols);
      // add again the listener this is really important!!!
      companies.getSelectionModel().selectedItemProperty().addListener(stockListener);
    };
  }
}

