package es.usefulearnings.engine.connection;

/**
 * @author yago.
 */
public interface ProcessHandler {
  void updateProgress(int workDone, int remaining);
  void updateMessage(String message);
  void onCancelled();
  void onError(Throwable err) throws Throwable;
}
