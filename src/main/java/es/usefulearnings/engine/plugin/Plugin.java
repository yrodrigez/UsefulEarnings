package es.usefulearnings.engine.plugin;

/**
 * A component to retrieve specific info from internet
 * Created by yago on 7/09/16.
 */
public interface Plugin<E> {
  void addInfo(E entity) throws Exception;
}