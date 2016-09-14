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
  private double remainingWork;
  private boolean stop;

  private ArrayList<Plugin> plugins;
  private List<E> entities;
  private E currentEntitiy;

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

  public DownloaderTask(ArrayList<Plugin> plugins, List<E> entities) {
    stop = true;
    this.plugins = plugins;
    this.entities = entities;

    this.workDone = 0;
    this.remainingWork = this.entities.size() * 1.0d;
  }

  public void stop() {
    stop = false;
  }

  @Override
  protected List<E> call() throws Exception {
    for(E e: entities) {
      if (!stop) {
        cancel();
        break;
      }
      // this.currentEntitiy = e;
      for(Plugin plugin : plugins) {
        if(!stop) {
          cancel();
          break;
        }
        updateMessage(workDone + " out of " + remainingWork+ "\tCurrent company: " +((Company) e).getSymbol());

        //System.out.println("Downloading data from " +((Company) e).getSymbol());
        //TODO CHANGE PLUGIN INTERFACE!!!
        try {
          plugin.addInfo(((Company) e));
        }catch (Exception ex){
          ex.printStackTrace();
        }
      }
      updateProgress(++workDone, remainingWork);
    }

    updateValue(entities);
    return this.entities;
  }
}
