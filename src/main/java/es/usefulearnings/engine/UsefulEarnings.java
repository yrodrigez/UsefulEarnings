package es.usefulearnings.engine;

import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Option;

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
