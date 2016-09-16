package es.usefulearnings.engine.connection;

import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.entities.Company;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  yago
 */
public class DownloaderTask<E> extends Task<List<E>> {

  private int workDone;
  private double remainingWork;
  private boolean stop;

  private ArrayList<Plugin> plugins;
  /**
   * can be an Option, Company or OptionLink
   */
  private List<E> entities;


  public DownloaderTask(ArrayList<Plugin> plugins, List<E> entities) {
    stop = false;
    this.plugins = plugins;
    this.entities = entities;

    this.workDone = 0;
    this.remainingWork = this.entities.size() * 1.0d;
  }

  public void stop() {
    stop = true;
  }

  @Override
  protected List<E> call() throws Exception {
    for(E e: entities) {
      if (stop) {
        cancel();
        break;
      }
      for(Plugin plugin : plugins) {
        if(stop) {
          cancel();
          break;
        }
        updateMessage(workDone + " out of " + remainingWork+ "\tCurrent company: " +((Company) e).getSymbol());


        try {
          plugin.addInfo(e);
        }catch (Exception ex){
          ex.printStackTrace();
          throw ex;
        }
      }
      updateProgress(++workDone, remainingWork);
    }

    updateValue(entities);
    return this.entities;
  }

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
}
