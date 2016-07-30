package es.yahoousefulearnings.gui.main;

import es.yahoousefulearnings.entities.Stock;
import es.yahoousefulearnings.utils.ResourcesHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import es.yahoousefulearnings.utils.CSVStockReader;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable{

  @FXML
  private ChoiceBox availableStocks;
  @FXML
  private ListView<String> companies;

  public void initialize(URL location, ResourceBundle resources) {

    List<Stock> stocks = new LinkedList<>();

    ResourcesHelper resourcesHelper = ResourcesHelper.getInstance();

    for (int i= 0 ; i < resourcesHelper.getAvailableStocks().length ; i++){
      stocks.add(new Stock(
        resourcesHelper.getAvailableStocks()[i].getName().substring(0, resourcesHelper.getAvailableStocks()[i].getName().length()-4),
        CSVStockReader.getStock(resourcesHelper.getAvailableStocks()[i].getPath())
      ));
    }



    ObservableList<String> symbols = FXCollections.observableArrayList(stocks.get(0).getSymbols().keySet());

    ObservableList<String> stocksNames = FXCollections.observableArrayList();

    for (Stock s : stocks){
      stocksNames.add(s.getName());
    }

    this.availableStocks.setItems(stocksNames);
    this.companies.setItems(symbols);
  }
}
