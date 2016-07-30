package es.yahoousefulearnings.entities;


import java.util.Map;

public class Stock {
  private String name;
  private Map<String, String> symbols;

  public Stock(String name, Map<String, String> symbols){
    this.setName(name);
    this.setSymbols(symbols);
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, String> getSymbols() {
    return symbols;
  }

  public void setSymbols(Map<String, String> symbols) {
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
