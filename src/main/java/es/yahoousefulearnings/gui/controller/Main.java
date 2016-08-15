package es.yahoousefulearnings.gui.controller;

import es.yahoousefulearnings.engine.SearchEngine;
import es.yahoousefulearnings.entities.Company;
import es.yahoousefulearnings.entities.Stock;
import es.yahoousefulearnings.entities.company.Profile;
import es.yahoousefulearnings.utils.ResourcesHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class Main implements Initializable {

  @FXML
  private ChoiceBox<String> stocksChoiceBox;
  @FXML
  private ListView<String> companies;
  @FXML
  private Tab webTab;
  @FXML
  private Tab dataTab;
  @FXML
  private TabPane tabPane;

  private ObservableList<String> symbols;

  public void initialize(URL location, ResourceBundle resources) {

    ResourcesHelper resourcesHelper = ResourcesHelper.getInstance();

    TreeMap<String, Stock> stocksMap = resourcesHelper.getAvailableStocks();

    symbols = FXCollections.observableArrayList(stocksMap.firstEntry().getValue().getSymbols().keySet());
    companies.setItems(symbols.sorted());

    ObservableList<String> stocksNames = FXCollections.observableArrayList(stocksMap.keySet());
    stocksChoiceBox.setItems(stocksNames);
    stocksChoiceBox.getSelectionModel().select(0);

    stocksChoiceBox.getSelectionModel().selectedItemProperty().addListener(
      (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        symbols = FXCollections.observableArrayList(stocksMap.get(newValue).getSymbols().keySet());
        companies.setItems(symbols.sorted());
      }
    );

    companies.getSelectionModel().selectedItemProperty().addListener(getStockListener());

  }

  private ChangeListener<String> getStockListener() {
    return (observable, oldSymbol, newSymbol) -> {
      WebView webView = new WebView();
      WebEngine webEngine = webView.getEngine();

      if (oldSymbol == null) {
        webEngine.load("http://finance.yahoo.com/quote/" + newSymbol);
      } else {
        if (!oldSymbol.equals(newSymbol)) {
          webEngine.load("http://finance.yahoo.com/quote/" + newSymbol);
        }
      }

      dataTab.setContent(setCompanyData(newSymbol));
      webTab.setContent(webView);
    };
  }

  private Node setCompanyData(String symbol) {
    ScrollPane scrollPane = new ScrollPane();
    VBox vBox = new VBox();
    // insertDataHere!!
    Company company = SearchEngine.getCompanyData(
      symbol,
      "Asset Profile",
      "Financial Data",
      "Default Key Statistics",
      "Calendar Events",
      "Income Statement History",
      "Cashflow Statement History",
      "Balance Sheet History"
    );
    vBox.getChildren().add(new Label("Company Symbol: "+ company.getSymbol()));
    ArrayList<Node> assetProfile = getCompanyAssetProfileNodes(company.getProfile());
    vBox.getChildren().addAll(assetProfile);
    scrollPane.setContent(vBox);
    return scrollPane;
  }

  private ArrayList<Node> getCompanyAssetProfileNodes(Profile profile) {
    ArrayList<Node> nodes = new ArrayList<>();
    if(profile.isSet()) {
      nodes.add(new Label("Address: " + profile.getAddress()));
      nodes.add(new Label("City: " + profile.getCity()));
      nodes.add(new Label("State: " + profile.getState()));
      nodes.add(new Label("ZipCode: " + profile.getZip()));
      nodes.add(new Label("Country: " + profile.getCountry()));
      nodes.add(new Label("Phone: " + profile.getPhone()));
      HBox website = new HBox();
      Hyperlink hyperlink = new Hyperlink(profile.getWebsite());
      hyperlink.setOnAction(event -> {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(profile.getWebsite());
        webTab.setContent(webView);
        tabPane.getSelectionModel().select(0);
      });
      website.getChildren().addAll(new Label("Website: "), hyperlink);
      nodes.add(website);
      nodes.add(new Label("Industry: " + profile.getIndustry()));
      nodes.add(new Label("Sector: " + profile.getSector()));
      nodes.add(new Label("Full-Time Employees: " + profile.getFullTimeEmployees()));
    } else {
      nodes.add(new Label("No profile found..." ));
    }

    return nodes;
  }

}

