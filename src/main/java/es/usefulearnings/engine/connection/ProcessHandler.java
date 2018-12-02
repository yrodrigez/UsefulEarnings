package es.usefulearnings.engine.connection;

/**
 * @author yago.
 */
public interface ProcessHandler {
  void updateProgress(final int workDone, final int remaining);
  void updateMessage(final String message);
  void onCancelled();
  void onError(final Throwable err);
  void onSuccess();
}
