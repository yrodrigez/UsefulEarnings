package es.usefulearnings.entities;

import es.usefulearnings.annotation.FieldType;
import es.usefulearnings.annotation.ObservableField;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *
 *
 * @author yago.
 */
public class SearchResult implements Savable, Serializable {
  @ObservableField(name = "Created", fieldType = FieldType.DATE)
  private long created;

  @ObservableField(name = "Companies found", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<Company> companiesFound;

  @ObservableField(name = "Options found", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<Option> optionsFound;

  @ObservableField(name = "Option chains found", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<OptionChain> optionChainsFound;

  public SearchResult(long created) {
    this.created = created;
    companiesFound = new ArrayList<>();
    optionsFound = new ArrayList<>();
    optionChainsFound = new ArrayList<>();
  }

  public SearchResult (
    long created,
    ArrayList<Company> companiesFound,
    ArrayList<Option> optionsFound,
    ArrayList<OptionChain> optionChainsFound
  ) {
    this.created = created;
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

  @Override
  public String toString() {
    return "Downloaded on " + new SimpleDateFormat("yyyy/MM/dd").format(new Date(created * 1000L));
  }

  @Override
  public void save() {
    final String searchResultExtension = ".sr";
    try {
      String location = ResourcesHelper.getInstance().getSearchesPath()
                        + File.separator
                        + searchResultExtension;
      try {
        FileOutputStream data = new FileOutputStream(location);
        ObjectOutputStream stream = new ObjectOutputStream(data);
        stream.writeObject(this);
        stream.close();
        data.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (NoStocksFoundException e) {
      // TODO: Do something with this exception....
    }
  }
}
