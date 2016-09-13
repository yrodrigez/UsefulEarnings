package es.usefulearnings.entities;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yago Rodríguez
 */
public class Stock implements Serializable {
  private String name;
  private List<String> symbols;

  public Stock(
    String name,
    List<String> symbols) {
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

  public void setSymbols(List<String> symbols) {
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
