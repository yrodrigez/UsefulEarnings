package es.usefulearnings.utils;

import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class CSVStockReader {

  public static Stock createStockFromPath(String path){
    if (!path.substring(path.length() - 3, path.length()).equals("csv"))
      throw new IllegalArgumentException("That's not a csv file");

    String stockName = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf(".csv"));

    Map<String, Company> companies = new TreeMap<>();

    BufferedReader br = null;
    String line;
    String splitBy = ",";

    try {
      br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {
        String[] splittedLine = line.split(splitBy);
        if(!splittedLine[0].contains("-") && !splittedLine[0].contains(".")) { // YAHOO FINANCE DOES NOT HAVE
                                                                               // INFORMATION FOR THESE COMPANIES
          final String companySymbol = splittedLine[0].replaceAll("\"", "").trim();
          companies.put(companySymbol, new Company(companySymbol, stockName));
        }
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

    return new Stock(stockName, companies);
  }


}
