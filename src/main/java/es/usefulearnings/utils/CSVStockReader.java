package es.usefulearnings.utils;

import es.usefulearnings.entities.Stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVStockReader {

  public static Stock createStockFromPath(String path){
    if (!path.substring(path.length() - 3, path.length()).equals("csv"))
      throw new IllegalArgumentException("That's not a csv file");

    String stockName = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf(".csv"));

    ArrayList<String> symbols = new ArrayList<>();

    BufferedReader br = null;
    String line;
    String splitBy = ",";

    try {
      br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {
        String[] splitedLine = line.split(splitBy);

        symbols.add(
          splitedLine[0] // company's symbol
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

    return new Stock(stockName, symbols);
  }


}
