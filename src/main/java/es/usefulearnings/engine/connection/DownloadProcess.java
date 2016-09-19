package es.usefulearnings.engine.connection;

import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.engine.plugin.PluginException;
import es.usefulearnings.entities.Company;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
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
            System.err.println("cause stack: ");
            e.getCause().printStackTrace();
            if (e.getCause().getClass().getName().startsWith("java.net")) {
              System.err.println("java.net exception: "+e.getCause().getClass()+", message: "+e.getCause().getMessage());
              System.exit(1);
              throw e;
            } else {
              System.err.println("other exceptionº: "+e.getCause().getClass()+", message: "+e.getCause().getMessage());
            }
            // e.printStackTrace();
          }
        }// end foreach plugin
        updateProgress(++workDone, remainingWork);
        updateMessage(workDone + " out of " + remainingWork + "\tCurrent company: " + ((Company) entity).getSymbol());
      }// end foreach entity
      if (!stop && !hasFailed) { // Success
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

  private boolean hasInternetConnection() {

    try {
      System.err.println("checking internet...");
      URL url = new URL("http://www.google.com");
      int timeoutMs = 2500;
      URLConnection conn = url.openConnection();
      conn.setConnectTimeout(timeoutMs);
      conn.setReadTimeout(timeoutMs);
      conn.getInputStream();
      System.err.println("yes");

      //return InetAddress.getByName("8.8.8.8").isReachable(2500) // google.com
      //  || InetAddress.getByName("finance.yahoo.com").isReachable(2500); // yahoo finance

      return true;
    } catch (IOException e) {
      System.err.println("no");
      return false;
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

}
