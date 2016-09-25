package es.usefulearnings.utils;

import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Option;
import es.usefulearnings.entities.Savable;

import java.io.*;
import java.util.Map;

public class EntitiesPackage implements Serializable, Savable {

  public static String EXTENSION = ".epk";

  private Map<String, Company> companies;
  private Map<String, Option> options;



  public EntitiesPackage(Map<String, Company> companies, Map<String, Option> options) {
    this.companies = companies;
    this.options = options;
  }

  public Map<String, Company> getCompanies() {return companies;}
  public Map<String, Option> getOptions() {return options;}

  @Override
  public void save(File fileToSave) throws IOException {
    String location = fileToSave.getAbsolutePath()
      + File.separator
      + companies.size() + "C"
      + "-"
      + options.size() + "O"
      + EXTENSION;

    FileOutputStream data = new FileOutputStream(location);
    ObjectOutputStream stream = new ObjectOutputStream(data);
    stream.writeObject(this);
    stream.close();
    data.close();
  }
}