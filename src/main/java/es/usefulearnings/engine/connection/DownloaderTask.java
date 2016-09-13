package es.usefulearnings.engine.connection;

import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.entities.Company;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by yago on 5/09/16.
 */
public class DownloaderTask<E> extends Task<List<E>> {
  /**
   * can be an Option, Company or OptionLink
   */
  private int workDone;
  private int remainingWork;
  private boolean stop;

  private int jump;
  private ArrayList<Plugin> plugins;
  private List<E> entities;

  @Override
  protected void succeeded() {
    super.succeeded();
    updateMessage("Done!");
  }

  @Override
  protected void cancelled() {
    super.cancelled();
    updateMessage("Cancelled!");
  }

  @Override
  protected void failed() {
    super.failed();
    updateMessage("Failed!");
  }

  public DownloaderTask(int jump, ArrayList<Plugin> plugins, List<E> entities) {
    stop = true;
    this.jump = jump;
    this.plugins = plugins;
    this.entities = entities;

    this.workDone = 0;
    this.remainingWork = entities.size();
  }

  public void stop() {
    stop = false;
  }

  @Override
  protected List<E> call() throws Exception {
    for(E e: entities) {
      if (!stop) break;
      for(Plugin plugin : plugins) {
        if(!stop) break;
        updateMessage("Downloading data from " +((Company) e).getSymbol());

        //System.out.println("Downloading data from " +((Company) e).getSymbol());
        //TODO CHANGE PLUGIN INTERFACE!!!
        try {
          plugin.addInfo(((Company) e));
        }catch (Exception ex){
          ex.printStackTrace();
        }

        updateProgress(workDone++, remainingWork);
      }
    }

    updateValue(entities);
    return this.entities;
  }
}
