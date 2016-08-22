package es.yahoousefulearnings.engine;

import es.yahoousefulearnings.entities.Company;
import es.yahoousefulearnings.entities.Option;

import java.util.ArrayList;

/**
 * @author Yago Rodríguez
 */
public class UsefulEarnings {

  private ArrayList<Company> companies;
  private ArrayList<Option> options;

  public UsefulEarnings() {
    companies = new ArrayList<>();
    options = new ArrayList<>();
  }

  public UsefulEarnings(ArrayList<Company> companies, ArrayList<Option> options) {
    this.companies = companies;
    this.options = options;
  }


  public ArrayList<Company> getCompanies() {
    return companies;
  }

  public void setCompanies(ArrayList<Company> companies) {
    this.companies = companies;
  }

  public ArrayList<Option> getOptions() {
    return options;
  }

  public void setOptions(ArrayList<Option> options) {
    this.options = options;
  }
}
