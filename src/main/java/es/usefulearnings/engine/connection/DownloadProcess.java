package es.usefulearnings.engine.connection;

import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.engine.plugin.PluginException;
import es.usefulearnings.entities.Company;

import java.io.IOException;
import java.net.InetAddress;
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

  public DownloadProcess(
    ProcessHandler handler,
    ArrayList<Plugin> plugins,
    List<E> entities
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
    try {
      for (E entity : entities) {
        for (Plugin plugin : plugins) {
          if (stop) {
            onStopped();
            updateMessage("Stopped!");
            break;
          }
          try {
            plugin.addInfo(entity);
          } catch (PluginException e) {
            if (!hasInternetConnection()) {
              throw e;
            }
          }
        }// end foreach plugin
        updateProgress(++workDone, remainingWork);
        updateMessage(workDone + " out of " + remainingWork + "\tCurrent company: " + ((Company) entity).getSymbol());
      }// end foreach entity
      updateMessage("Work Done!");
      onSuccess();// SUCCESS!!!
    } catch (PluginException err) {
      this.hasFailed = true;
      System.err.println("No internet connection");
      this.err = err;
      updateMessage("Connection lost...");
      onError(err);
      err.printStackTrace();
    }
  }

  private boolean hasInternetConnection() {
    try {
      return InetAddress.getByName("8.8.8.8").isReachable(2500) // google.com
        || InetAddress.getByName("finance.yahoo.com").isReachable(2500); // yahoo finance
    } catch (IOException e) {
      return false;
    }
  }

  public void stop() {
    this.stop = true;
    updateMessage("Stopped!");
    onStopped();
  }

  public boolean hasFailed() {
    return hasFailed;
  }

  public Exception getError() {
    return this.err;
  }

}
