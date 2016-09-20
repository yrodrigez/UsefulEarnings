package es.usefulearnings.utils;

import es.usefulearnings.entities.DownloadedData;
import es.usefulearnings.entities.Stock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Yago Rodríguez
 */
public class ResourcesHelper {

  private static ResourcesHelper mInstance;

  private final String resourcesPath;
  private final String stocksPath;
  private final String serializationPath;
  private final String searchesPath;

  private final File stocksFile;
  private final File serializationFile;
  private final File searchesFile;


  private final List<Stock> stocks;

  private ResourcesHelper() throws NoStocksFoundException {
    resourcesPath = System.getProperty("user.home")
                    + File.separator + "UsefulEarnings"
                    + File.separator + "resources";

    stocksPath = resourcesPath + File.separator + "stocks";
    stocksFile = new File(stocksPath);

    serializationPath = resourcesPath +File.separator + "data";
    serializationFile = new File(serializationPath);

    searchesPath = serializationPath + File.separator + "searches";
    searchesFile = new File(searchesPath);

    createFiles();
    stocks = createAvailableStocksFromFolder();
  }

  public List<Stock> getAvailableStocks(){
    return stocks;
  }

  private  List<Stock> createAvailableStocksFromFolder() throws NoStocksFoundException {
    File stocksFile = new File(stocksPath);
    List<Stock> stocks = new ArrayList<>();

    try {
      if(stocksFile.listFiles().length > 0) {
        for (File csvStock : stocksFile.listFiles()) {
          Stock stock = CSVStockReader.createStockFromPath(csvStock.getPath());
          stocks.add(stock);
        }
      } else {
        throw new NoStocksFoundException();
      }
    } catch (NullPointerException ne) {
      System.err.println("ResourcesHelper: No files available at: " + stocksPath + '\n');
    }

    return stocks;
  }

  public List<DownloadedData> getDownloadedData() throws IOException, ClassNotFoundException {
    List<DownloadedData> toRet = new ArrayList<>();

    if(searchesFile.exists()){
      if(searchesFile.listFiles() != null){
        for(File downloadedData : searchesFile.listFiles()){
          FileInputStream fileIn = new FileInputStream(downloadedData.getAbsolutePath());
          ObjectInputStream in = new ObjectInputStream(fileIn);
          toRet.add((DownloadedData) in.readObject());
          in.close();
          fileIn.close();
        }
      }
    }

    return toRet;
  }

  private void createFiles(){
    // STOCKS
    if(!stocksFile.exists())
      if(stocksFile.mkdirs())
        System.out.printf("File successfully created at "+ stocksFile);
      else System.err.println("Could not create file at " + stocksPath);

    // SERIALIZATION
    if(!serializationFile.exists())
      if(serializationFile.mkdir())
        System.out.printf("File successfully created at "+ serializationPath);
      else System.err.println("Could not create file at " + serializationPath);

    // SEARCHES
    if(!searchesFile.exists())
      if(searchesFile.mkdir())
        System.out.printf("File successfully created at "+ searchesPath);
      else System.err.println("Could not create file at " + searchesFile);

    // ENTITIES
  }

  public static ResourcesHelper getInstance() throws NoStocksFoundException {
    if (mInstance == null) mInstance = new ResourcesHelper();
    return mInstance;
  }

  public String getStocksPath() {
    return stocksPath;
  }

  public String getResourcesPath() {
    return resourcesPath;
  }

  public String getSerializationPath() {
    return serializationPath;
  }

  public File getStocksFile() {
    return stocksFile;
  }

  public File getSerializationFile() {
    return serializationFile;
  }

  public String getSearchesPath() {
    return searchesPath;
  }

}
