package es.usefulearnings.engine.connection;

/**
 * @author yago.
 */
public interface DownloadProcessHandler {
  void updateProgress(double workDone, double remaining);
  void updateMessage(String message);
  void onFinish();
  void onError(Throwable err);
}
