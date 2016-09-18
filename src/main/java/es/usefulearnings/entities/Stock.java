package es.usefulearnings.entities;

import java.util.Map;

/**
 * @author Yago Rodríguez
 */
public class Stock {
  private String name;
  private Map<String, Company> companies;

  public Stock(
    String name,
    Map<String, Company> companies
  ) {
    this.setName(name);
    this.setCompanies(companies);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, Company> getCompanies() {
    return companies;
  }

  public void setCompanies(Map<String, Company> companies) {
    this.companies = companies;
  }


  @Override
  public String toString() {
    return "Stock{" +
      "name='" + name + '\'' +
      ", symbols=" + companies +
      '}';
  }
}
