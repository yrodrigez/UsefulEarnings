package es.usefulearnings.entities;

import es.usefulearnings.annotation.FieldType;
import es.usefulearnings.annotation.ObservableField;
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
public class DownloadedData implements Savable, Serializable {

  // Serializable extension
  public static final String EXTENSION = ".sr";

  private File mEntitiesFile;

  @ObservableField(name = "Created", fieldType = FieldType.DATE)
  private long created;

  private long totalSavedCompanies;
  private long totalSavedOptions;
  private long totalSavedOptionChains;

  public DownloadedData(long created) {
    this.created = created;
    this.totalSavedCompanies = 0;
    this.totalSavedOptionChains = 0;
    this.totalSavedOptions = 0;
  }


  public String getDateToString() {
    return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(created * 1000L));
  }

  @Override
  public String toString() {
    return "Downloaded on " + getDateToString();
  }


  @Override
  public void save(File fileToSave) throws IOException {
    String location = fileToSave.getAbsolutePath()
      + File.separator
      + this.created
      + EXTENSION;

    Map<String, Company> companiesToSave = Core.getInstance().getAllCompanies();
    Map<String, Option> optionsToSave = Core.getInstance().getAllOptions();


    this.totalSavedCompanies = companiesToSave.size();
    this.totalSavedOptions = optionsToSave.size();

    mEntitiesFile = new File(
      fileToSave.getAbsolutePath()
        + File.separator
        + this.created
    );

    if (!mEntitiesFile.exists()) {
      if(!mEntitiesFile.mkdirs())
        throw new IOException(mEntitiesFile.getAbsolutePath() + "can't be created!");
    }

    for (Company c: companiesToSave.values()){
      c.save(mEntitiesFile);
    }

    for (Option o : optionsToSave.values()){
      o.save(mEntitiesFile);
    }

    FileOutputStream data = new FileOutputStream(location);
    ObjectOutputStream stream = new ObjectOutputStream(data);
    stream.writeObject(this);
    stream.close();
    data.close();
  }

  public long getTotalSavedCompanies() {
    return totalSavedCompanies;
  }

  public long getTotalSavedOptions() {
    return totalSavedOptions;
  }

  public long getTotalSavedOptionChains() {
    return totalSavedOptionChains;
  }

  public File getEntitiesFile() {
    return mEntitiesFile;
  }
}
