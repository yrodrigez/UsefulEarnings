package es.usefulearnings.entities;

import es.usefulearnings.engine.Core;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 *
 *
 * @author yago.
 */
public class DownloadedData implements Savable, Serializable, Comparable {

  // Serializable extension
  public static final String EXTENSION = ".metadata";

  private File _entitiesFile; //1474537062.cdata (List<Company> serializado)

  public long getCreated() {
    return _created;
  }

  private long _created;

  private long _totalSavedCompanies;
  private long _totalSavedOptions;
  private long _totalSavedOptionChains;

  public DownloadedData(long created) {
    _created = created;
    _totalSavedCompanies = 0;
    _totalSavedOptionChains = 0;
    _totalSavedOptions = 0;
  }


  public String getDateToString() {
    return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(_created * 1000L));
  }

  @Override
  public String toString() {
    return "Downloaded on " + getDateToString();
  }


  @Override
  public void save(File fileToSave) throws IOException {
    String location = fileToSave.getAbsolutePath()
      + File.separator
      + _created
      + EXTENSION;

    Map<String, Company> companiesToSave = Core.getInstance().getAllCompanies();
    Map<String, Option> optionsToSave = Core.getInstance().getAllOptions();


    _totalSavedCompanies = companiesToSave.size();
    _totalSavedOptions = optionsToSave.size();

    _entitiesFile = new File(
      fileToSave.getAbsolutePath()
        + File.separator
        + _created
    );

    if (!_entitiesFile.exists()) {
      if(!_entitiesFile.mkdirs())
        throw new IOException(_entitiesFile.getAbsolutePath() + "can't be _created!");
    }

    EntitiesPackage entitiesPackage = new EntitiesPackage(
      companiesToSave,
      optionsToSave,
      _created
    );
    entitiesPackage.save(_entitiesFile);


    FileOutputStream data = new FileOutputStream(location);
    ObjectOutputStream stream = new ObjectOutputStream(data);
    stream.writeObject(this);
    stream.close();
    data.close();
  }

  public long get_totalSavedCompanies() {
    return _totalSavedCompanies;
  }

  public long get_totalSavedOptions() {
    return _totalSavedOptions;
  }

  public long get_totalSavedOptionChains() {
    return _totalSavedOptionChains;
  }

  public File getEntitiesFile() {
    return _entitiesFile;
  }

  @Override
  public int compareTo(Object o) {
    if(((DownloadedData)o)._created == this._created) return 0;
    if(((DownloadedData)o)._created < this._created) return -1;
    if(((DownloadedData)o)._created > this._created) return 1;
    System.err.println("This shouldn't appear");
    return -1;
  }
}
