package es.usefulearnings.utils;

import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Option;

import java.util.Collection;

public class EntitiesPackage {
  private Collection<Company> companies;
  private Collection<Option> options;

  public EntitiesPackage(
    Collection<Company> companies,
    Collection<Option> options
  ) {
    this.companies = companies;
    this.options = options;
  }

  public Collection<Company> getCompanies() {return companies;}
  public Collection<Option> getOptions() {return options;}
}