package es.yahoousefulearnings.utils;

import java.io.File;


public class ResourcesHelper {

  private static ResourcesHelper instance;

  private String stocksPath;

  private ResourcesHelper() {
    System.out.println("Creando nuevo");

    stocksPath = System.getProperty("user.home")
      + File.separator + "YahooUsefulEarnings"
      + File.separator + "resources"
      + File.separator + "Stocks";

    createStocksFile();
  }

  public File[] getAvailableStocks(){
    File stocksFile = new File(stocksPath);
    return stocksFile.listFiles();
  }

  private void createStocksFile(){
    System.out.println("creando directorio");
    File stocksFile = new File(stocksPath);
    if(stocksFile.mkdir()) System.out.printf("File succesfully created at "+ stocksFile);
    else System.err.println("Could not create file stocks at " + stocksPath);
  }

  public static ResourcesHelper getInstance() {
    System.out.println("instnciando");
    if (instance == null) instance = new ResourcesHelper();
    return instance;
  }

  public String getStocksPath() {
    return stocksPath;
  }
}
