package es.yahoousefulearnings.utils;

import es.yahoousefulearnings.entities.Stock;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CSVStockReader {

  public static Stock createStockFromPath(String path){
    if (!path.substring(path.length() - 3, path.length()).equals("csv"))
      throw new IllegalArgumentException("That's not a csv file");

    String stockName = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf(".csv"));

    Map<String, String> stockMap = new HashMap<>();

    BufferedReader br = null;
    String line;
    String splitBy = ",";

    try {

      br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {
        String[] splittedLine = line.split(splitBy);

        stockMap.put(
          splittedLine[0], // company symbol
          splittedLine[1] // Company name
        );
      }

    } catch (IOException e) {
      e.printStackTrace();
    }  finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return new Stock(stockName, stockMap);
  }


}
