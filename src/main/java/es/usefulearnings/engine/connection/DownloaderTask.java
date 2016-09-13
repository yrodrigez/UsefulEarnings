package es.usefulearnings.engine.connection;

import javafx.concurrent.Task;

/**
 *
 * Created by yago on 5/09/16.
 */
public class DownloaderTask<E> extends Task<E> {
  /**
   * can be an Option, Company or OptionLink
   */
  private E entity;
  private double workDone;

  public DownloaderTask() {
    workDone = 0d;
  }

  @Override
  protected E call() throws Exception {

    return this.entity;
  }
}
