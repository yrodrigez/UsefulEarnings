package es.usefulearnings.engine.connection;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.engine.plugin.PluginException;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yago.
 */
public class DownloadProcess extends Process implements Runnable {

  private Exception err;

  private int workDone;
  private int remainingWork;

  private boolean stop;
  private boolean hasFailed;

  private ArrayList<Plugin> plugins;

  private List<Entity> entities;
  private List<Entity> emptyEntities;

  public DownloadProcess(
    ProcessHandler handler,
    ArrayList<Plugin> plugins,
    List<Entity> entities
  ) {
    super(handler);
    this.plugins = plugins;
    this.entities = entities;

    workDone = 0;
    stop = hasFailed = false;
    remainingWork = this.entities.size();
  }

  @Override
  public void run() {
    emptyEntities = new ArrayList<>(entities);
    try {
      for (Entity entity : entities) {
        if (stop) {
          updateMessage("Stopped!");
          onCancelled();
          break;
        }
        for (Plugin plugin : plugins) {
          if (stop) {
            updateMessage("Stopped!");
            onCancelled();
            break;
          }
          try {
            plugin.addInfo(entity);
          } catch (PluginException e) {
            if (e.getCause().getClass().getName().startsWith("java.net")) {
              System.err.println("java.net exception: "+e.getCause().getClass()+", message: "+e.getCause().getMessage());
              throw e;
            }
          }
        }// end foreach plugin
        if(!entity.isEmpty()){
          emptyEntities.remove(entity);
        }
        updateProgress(++workDone, remainingWork);
        updateMessage(workDone + " out of " + remainingWork + "\tCurrent company: " + ((Company) entity).getSymbol());
      }// end foreach entity
      if (!stop && !hasFailed) { // Success.
        Core.getInstance().removeEntities(emptyEntities);
        updateMessage("Work done!");
        onSuccess();
      }
    } catch (PluginException err) {
      this.hasFailed = true;
      System.err.println("No internet connection");
      this.err = err;
      updateMessage("Connection lost...");
      onError(err);
      err.printStackTrace();
    }
  }

  public void stop() {
    this.stop = true;
  }


  public boolean hasFailed() {
    return hasFailed;
  }

  public Exception getError() {
    return this.err;
  }

  public List<Entity> getEntities() {
    return entities;
  }
  public List<Entity> getEmptyEntities() {
    return emptyEntities;
  }

}
