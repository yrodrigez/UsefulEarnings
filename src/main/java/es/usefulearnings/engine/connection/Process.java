package es.usefulearnings.engine.connection;

/**
 * @author Yago on 16/09/2016.
 */
public abstract class Process {
  private ProcessHandler handler;

  protected Process(ProcessHandler handler) {
    this.handler = handler;
  }

  protected void updateMessage(String message) {
    handler.updateMessage(message);
  }

  protected void updateProgress(int workDone, int remaining) {
    handler.updateProgress(workDone, remaining);
  }

  protected void onCancelled() {
    handler.onCancelled();
  }

  protected void onError(Throwable err) {
    handler.onError(err);
  }

  protected void onSuccess(){
    handler.onSuccess();
  }

}
