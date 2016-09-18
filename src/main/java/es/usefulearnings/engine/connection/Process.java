package es.usefulearnings.engine.connection;

/**
 * @author Yago on 16/09/2016.
 */
abstract class Process {
  private ProcessHandler handler;

  Process(ProcessHandler handler) {
    this.handler = handler;
  }

  void updateMessage(String message) {
    handler.updateMessage(message);
  }

  void updateProgress(int workDone, int remaining) {
    handler.updateProgress(workDone, remaining);
  }

  void onStopped() {
    handler.onCancelled();
  }

  void onError(Throwable err) {
    handler.onError(err);
  }

  protected void onSuccess(){
    handler.onSuccess();
  }


}
