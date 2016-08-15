package es.yahoousefulearnings.utils;

import es.yahoousefulearnings.entities.Stock;

import java.io.File;
import java.util.TreeMap;


public class ResourcesHelper {

  private static ResourcesHelper instance;

  private final String stocksPath;

  private final TreeMap<String, Stock> stockMap;

  private ResourcesHelper() {
    stocksPath = System.getProperty("user.home")
      + File.separator + "YahooUsefulEarnings"
      + File.separator + "resources"
      + File.separator + "Stocks";

    createStocksFile();
    stockMap = createAvailableStocks();
  }

  public TreeMap<String, Stock> getAvailableStocks(){
    return stockMap;
  }

  private TreeMap<String, Stock> createAvailableStocks(){
    File stocksFile = new File(stocksPath);
    TreeMap<String, Stock> stocks = new TreeMap<>();
    try {
      for (File csvStock : stocksFile.listFiles()) {
        Stock stock = CSVStockReader.createStockFromPath(csvStock.getPath());
        stocks.put(stock.getName(), stock);
      }
    } catch (NullPointerException ne) {
      System.err.println("ResourcesHelper: No files available at: " + stocksPath + '\n');
    }

    return stocks;
  }

  private void createStocksFile(){
    File stocksFile = new File(stocksPath);
    if(!stocksFile.exists())
      if(stocksFile.mkdirs())
        System.out.printf("File succesfully created at "+ stocksFile);
      else System.err.println("Could not create file stocks at " + stocksPath);
  }

  public static ResourcesHelper getInstance() {
    if (instance == null) instance = new ResourcesHelper();
    return instance;
  }

  public String getStocksPath() {
    return stocksPath;
  }
}
