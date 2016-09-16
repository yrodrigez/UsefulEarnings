package es.usefulearnings.engine.connection;

import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.entities.Company;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yago.
 */
public class DownloadProcess<E> extends Process implements Runnable {

  private Exception err;

  private int workDone;
  private int remainingWork;
  private boolean stop;
  private boolean hasFailed;

  private ArrayList<Plugin> plugins;

  private List<E> entities;

  public DownloadProcess (
    ProcessHandler handler,
    ArrayList<Plugin> plugins,
    List<E> entities
  ) {
    super(handler);
    this.plugins  = plugins;
    this.entities = entities;

    stop = hasFailed = false;

    workDone = 0;
    remainingWork = this.entities.size();
  }

  @Override
  public void run() {
    try {
      for(E e: entities) {
        for(Plugin plugin : plugins) {
          if(stop) {
            super.onStopped();
            break;
          }

          updateMessage(workDone + " out of " + remainingWork+ "\tCurrent company: " +((Company) e).getSymbol());
          try {
            plugin.addInfo(e);
          }catch (Exception ex){
            onError(ex);
            throw ex;
          }
        }
        updateProgress(++workDone, remainingWork);
      }
    } catch (Exception err) {
      this.hasFailed = true;
      this.err = err;
    }
  }

  public void stop(){
    this.stop = true;
  }

  public boolean hasFailed(){
    return hasFailed;
  }

  public Exception getError(){
    return this.err;
  }

  public List<E> getResult(){
    return this.entities;
  }
}
