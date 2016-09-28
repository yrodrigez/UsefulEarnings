package es.usefulearnings.entities;

import java.io.*;
import java.util.Map;

public class EntitiesPackage implements Serializable, Savable {

  public static String EXTENSION = ".epk";

  private Map<String, Company> _companies;
  private Map<String, Option> _options;

  public long getdateId() {
    return _dateId;
  }

  private long _dateId;



  public EntitiesPackage(Map<String, Company> companies, Map<String, Option> options, long dateId) {
    _companies = companies;
    _options = options;
    _dateId = dateId;
  }

  public Map<String, Company> get_companies() {return _companies;}
  public Map<String, Option> getOptions() {return _options;}

  @Override
  public void save(File fileToSave) throws IOException {
    String location = fileToSave.getAbsolutePath()
      + File.separator
      + _companies.size() + "C"
      + "-"
      + _options.size() + "O"
      + EXTENSION;

    FileOutputStream data = new FileOutputStream(location);
    ObjectOutputStream stream = new ObjectOutputStream(data);
    stream.writeObject(this);
    stream.close();
    data.close();
  }
}