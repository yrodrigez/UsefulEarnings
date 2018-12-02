package es.usefulearnings.entities;

/**
 * @author yago.
 */
public interface Entity {
  boolean isEmpty();
  void flush();
  void restore();
}
