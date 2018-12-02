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
    final String splitBy = ",";

    try {
      br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {
        final String[] splitLine = line.split(splitBy);
        if(!splitLine[0].contains("-") && !splitLine[0].contains(".")) { // YAHOO FINANCE DOES NOT HAVE
                                                                               // INFORMATION FOR THESE COMPANIES
          final String companySymbol = splitLine[0].trim().replaceAll("(\")|(&quot;)", "");
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
