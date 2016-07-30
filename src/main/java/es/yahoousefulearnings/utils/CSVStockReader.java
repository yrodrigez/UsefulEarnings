package es.yahoousefulearnings.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CSVStockReader {


  public static Map<String, String> getStock(String path){
    if (!path.substring(path.length()-3, path.length()).equals("csv"))
      throw new IllegalArgumentException("That's not a csv file");

    Map<String, String> stock = new HashMap<>();

    if( path.contains("/") ) path = getRealPath(path, "/");
    if( path.contains("\\") ) path = getRealPath(path, "\"\\\"");

    BufferedReader br = null;
    String line;
    String splitBy = ",";

    try {

      br = new BufferedReader(new FileReader(path));
      while ((line = br.readLine()) != null) {

        String[] action = line.split(splitBy);

        stock.put(action[0], action[1]);
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

    return stock;
  }



  private static String getRealPath(String path, String token){

    StringBuilder realPath = new StringBuilder();
    String [] splitedPath = path.split(token);

    for (int i = 0 ; i < splitedPath.length ; i++) {
      realPath.append(splitedPath[i]);

      if( i < splitedPath.length-1 ) {
        realPath.append(File.separator);
      }
    }

    return realPath.toString();
  }


}
