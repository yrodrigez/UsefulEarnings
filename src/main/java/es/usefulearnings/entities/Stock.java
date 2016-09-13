package es.usefulearnings.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yago Rodríguez
 */
public class Stock {
  private String name;
  private ArrayList<String> symbols;

  public Stock(
    String name,
    ArrayList<String> symbols) {
    this.setName(name);
    this.setSymbols(symbols);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getSymbols() {
    return symbols;
  }

  public void setSymbols(ArrayList<String> symbols) {
    this.symbols = symbols;
  }


  @Override
  public String toString() {
    return "Stock{" +
      "name='" + name + '\'' +
      ", symbols=" + symbols +
      '}';
  }
}
