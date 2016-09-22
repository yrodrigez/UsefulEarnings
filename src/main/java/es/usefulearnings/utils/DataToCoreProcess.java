package es.usefulearnings.utils;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.connection.Process;
import es.usefulearnings.engine.connection.ProcessHandler;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Option;
import javafx.application.Platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;

public class DataToCoreProcess extends Process implements Runnable {

  private Exception exception;
  private File mEntitiesFile;

  public DataToCoreProcess(ProcessHandler handler, File entitiesFile) throws IOException {
    super(handler);
    mEntitiesFile = entitiesFile;
    if((!mEntitiesFile.exists() || mEntitiesFile.listFiles() == null || mEntitiesFile.listFiles().length < 1)){
      throw new IOException(mEntitiesFile.getAbsolutePath() + " does not exists or does not have files inside");
    }
  }

  @Override
  public void run() {
    Collection<Company> companies = new ArrayList<>();
    Collection<Option> options = new ArrayList<>();
    int workDone = 0;
    for (File f: mEntitiesFile.listFiles()) {
      try {
        if (f.getName().endsWith(Company.EXTENSION)) {
          FileInputStream fileIn = new FileInputStream(f.getAbsolutePath());
          ObjectInputStream in = new ObjectInputStream(fileIn);
          Company company = (Company) in.readObject();
          companies.add(company);
          updateMessage("Attaching " + company.getSymbol());
          in.close();
          fileIn.close();
        } else if (f.getName().endsWith(Option.EXTENSION)) {
          FileInputStream fileIn = new FileInputStream(f.getAbsolutePath());
          ObjectInputStream in = new ObjectInputStream(fileIn);
          Option option = (Option) in.readObject();
          options.add(option);
          updateMessage("Attaching " + option.getSymbol());
          in.close();
          fileIn.close();
        }
      }catch (Exception e) {
        e.printStackTrace();
        super.onError(e);
        this.exception = e;
      }
      updateProgress(++workDone, mEntitiesFile.listFiles().length);
    }
    updateMessage("Work Done!");
    EntitiesPackage entitiesPackage = new EntitiesPackage(companies, options);
    Core.getInstance().setFromEntitiesPackage(entitiesPackage);
  }

  public Exception getException() {
    return exception;
  }
}