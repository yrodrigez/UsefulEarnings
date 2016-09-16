package es.usefulearnings.entities;

import es.usefulearnings.annotation.FieldType;
import es.usefulearnings.annotation.ObservableField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 *
 * @author yago.
 */
public class SearchResult implements Serializable {
  @ObservableField(name = "Companies found", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<Company> companiesFound;

  @ObservableField(name = "Options found", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<Option> optionsFound;

  @ObservableField(name = "Option chains found", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<OptionChain> optionChainsFound;

  public SearchResult() {
    companiesFound = new ArrayList<>();
    optionsFound = new ArrayList<>();
    optionChainsFound = new ArrayList<>();
  }

  public SearchResult (
    ArrayList<Company> companiesFound,
    ArrayList<Option> optionsFound,
    ArrayList<OptionChain> optionChainsFound
  ) {
    this.companiesFound = companiesFound;
    this.optionsFound = optionsFound;
    this.optionChainsFound = optionChainsFound;
  }

  public void addAllFoundOptions(Option... options){
    this.optionsFound.addAll(Arrays.asList(options));
  }

  public void addAllFoundCompanies(Company... companies){
    this.companiesFound.addAll(Arrays.asList(companies));
  }

  public void addAllOptionChains(OptionChain... optionChains){
    this.optionChainsFound.addAll(Arrays.asList(optionChains));
  }

  public ArrayList<Company> getCompaniesFound() {
    return this.companiesFound;
  }

  public ArrayList<Option> getOptionsFound(){
    return this.optionsFound;
  }

  public void setCompaniesFound(ArrayList<Company> companiesFound) {
    this.companiesFound = companiesFound;
  }

  public void setOptionsFound(ArrayList<Option> optionsFound) {
    this.optionsFound = optionsFound;
  }

  public ArrayList<OptionChain> getOptionChainsFound() {
    return optionChainsFound;
  }

  public void setOptionChainsFound(ArrayList<OptionChain> optionChainsFound) {
    this.optionChainsFound = optionChainsFound;
  }
}
