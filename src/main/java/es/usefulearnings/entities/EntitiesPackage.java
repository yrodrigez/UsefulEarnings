package es.usefulearnings.entities;

import java.io.*;
import java.util.Map;

public class EntitiesPackage implements Serializable, Savable {

  public static String EXTENSION = ".epk";

  private Map<String, Company> _companies;

  public long getdateId() {
    return _dateId;
  }

  private long _dateId;


  public EntitiesPackage(Map<String, Company> companies, long dateId) {
    _companies = companies;
    _dateId = dateId;
  }

  public Map<String, Company> getCompanies() {return _companies;}

  @Override
  public void save(File fileToSave) throws IOException {
    String location = fileToSave.getAbsolutePath()
      + File.separator
      + _companies.size() + "C"
      + EXTENSION;

    FileOutputStream data = new FileOutputStream(location);
    ObjectOutputStream stream = new ObjectOutputStream(data);
    stream.writeObject(this);
    stream.close();
    data.close();
  }
}