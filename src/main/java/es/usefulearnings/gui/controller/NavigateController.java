package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.entities.Stock;
import es.usefulearnings.gui.Main;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.gui.view.CompanyViewHelper;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.beans.IntrospectionException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
  private ResourcesHelper resourcesHelper;

  public void initialize(URL location, ResourceBundle resources) {
    ImageView refreshIcon = new ImageView(new javafx.scene.image.Image(Main.class.getResourceAsStream("icons/refresh.png"), 20, 20, true, true));
    refresh.setGraphic(refreshIcon);

    webTab.setClosable(false);
    WebView webView = new WebView();
    webTab.setContent(webView);
    webEngine = webView.getEngine();
    webEngine.load("https://github.com/yrodrigez/UsefulEarnings/blob/master/README.md");

    textFilter.textProperty().addListener(
      (observable, oldSymbolEntry, newSymbolEntry) -> {
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
      });

    stockListener = getStockListener();
    companies.getSelectionModel().selectedItemProperty().addListener(stockListener);

    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

    getStocks();

  }


  private void getStocks() {
    try {
      resourcesHelper = ResourcesHelper.getInstance();
      List<Stock> stocks = resourcesHelper.getAvailableStocks();
      symbols = FXCollections.observableArrayList(stocks.get(0).getCompanies().keySet()).sorted();
      companies.setItems(symbols);

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
      stocksChoiceBox.getStyleClass().addAll("ue-choice-box");
    } catch (NoStocksFoundException e) {
      stocksChoiceBox.getItems().add("No Stocks found");
      stocksChoiceBox.getSelectionModel().select(0);
      // ALERT SHOW ALERT!
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("UsefulEarnings | No stocks found at your folder!");
      alert.setHeaderText("No stocks found at your stocks folder");
      alert.setContentText("Choose your option.");
      ButtonType openFolderButton = new ButtonType("Open stock Folder");
      ButtonType buttonTypeCancel = new ButtonType("I don't care!", ButtonBar.ButtonData.CANCEL_CLOSE);
      alert.getButtonTypes().setAll(openFolderButton, buttonTypeCancel);
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == openFolderButton) {
        if (Desktop.isDesktopSupported()) {
          try {
            File resourcesFile = new File(resourcesHelper.getResourcesPath());
            Desktop.getDesktop().open(resourcesFile.getAbsoluteFile());
          } catch (Exception e1) {
            AlertHelper.showExceptionAlert(e1);
          }
        }
      }
    }


  }

  @FXML
  public void refreshStocks(ActionEvent event) {
    getStocks();
    event.consume();
  }


  private Node getCompanyView(String symbol) throws IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException {
    CompanyViewHelper companyViewHelper = CompanyViewHelper.getInstance();
    return companyViewHelper.getViewFor(Core.getInstance().getCompanyFromSymbol(symbol));
  }
  /**
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
      if(Core.getInstance().isDataLoaded()) {
        Tab cTab = new Tab(newSymbol);
        ProgressIndicator progressIndicator = new ProgressIndicator(-1);
        progressIndicator.getStyleClass().addAll("default-progress-indicator");
        cTab.setContent(progressIndicator);

        new Thread(() -> {
          try {
            Node companyData = getCompanyView(newSymbol);
            Platform.runLater(() -> cTab.setContent(companyData));
          } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            Platform.runLater(() -> AlertHelper.showExceptionAlert(e));
            e.printStackTrace();
          } catch (InstantiationException e) {
            e.printStackTrace();
          }
        }).start();
        tabPane.getTabs().add(cTab);
      } // No data loaded
    };
  }

}
