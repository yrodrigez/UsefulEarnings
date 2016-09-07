package es.yahoousefulearnings.engine;

import javafx.concurrent.Task;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * Created by yago on 5/09/16.
 */
public class Downloader <E> extends Task<E> {
  /**
   * can be an Option, Company or CompositeOption
   */
  private E entity;
  private double workDone;

  public Downloader (E entity, String ... symbols) {
    workDone = 0d;
    this.entity = entity;

  }

  @Override
  protected E call() throws Exception {

    return this.entity;
  }
}
