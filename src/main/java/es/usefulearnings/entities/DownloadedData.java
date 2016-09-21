package es.usefulearnings.entities;

import es.usefulearnings.annotation.FieldType;
import es.usefulearnings.annotation.ObservableField;
import es.usefulearnings.engine.Core;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 *
 * @author yago.
 */
public class DownloadedData implements Savable, Serializable {


  // Serializable extension
  public static final String EXTENSION = ".sr";

  @ObservableField(name = "Created", fieldType = FieldType.DATE)
  private long created;

  @ObservableField(name = "Companies found", fieldType = FieldType.FIELD_ARRAY_LIST)
  private Map<String, Company> companiesFound;

  @ObservableField(name = "Options found", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<Option> optionsFound;

  @ObservableField(name = "Option chains found", fieldType = FieldType.FIELD_ARRAY_LIST)
  private ArrayList<OptionChain> optionChainsFound;

  public DownloadedData(long created) {
    this.created = created;
    companiesFound = new TreeMap<>();
    optionsFound = new ArrayList<>();
    optionChainsFound = new ArrayList<>();
  }

  public DownloadedData(
    long created,
     Map<String, Company> companiesFound,
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
  public void addAllFoundOptions(Collection<Option> options){
    this.optionsFound.addAll(options);
  }

  public void addAllFoundCompanies(Company... companies){
    Arrays.asList(companies).forEach(company -> companiesFound.put(company.getSymbol(), company));
  }
  public void addAllFoundCompanies(Collection<Company> companies){
    companies.forEach(company -> companiesFound.put(company.getSymbol(), company));
  }

  public void addAllOptionChains(OptionChain... optionChains){
    this.optionChainsFound.addAll(Arrays.asList(optionChains));
  }

  public Map<String, Company> getCompaniesFound() {
    return this.companiesFound;
  }

  public ArrayList<Option> getOptionsFound(){
    return this.optionsFound;
  }

  public void setCompaniesFound(Map<String, Company> companiesFound) {
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

  public String getDateToString() {
    return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(created * 1000L));
  }

  @Override
  public String toString() {
    return "Downloaded on " + getDateToString();
  }

  @Override
  public void save() throws IOException {
    try {
      if(this.getOptionsFound().isEmpty() && getCompaniesFound().isEmpty() && getOptionChainsFound().isEmpty()){
        this.companiesFound = Core.getInstance().getAllCompanies();
        // TODO implement this for options...
      }
      String location = ResourcesHelper.getInstance().getSearchesPath()
                        + File.separator
                        + this.created
                        + EXTENSION;
      FileOutputStream data = new FileOutputStream(location);
      ObjectOutputStream stream = new ObjectOutputStream(data);
      stream.writeObject(this);
      stream.close();
      data.close();
    } catch (NoStocksFoundException e) {
      e.printStackTrace();
    }
  }
}
