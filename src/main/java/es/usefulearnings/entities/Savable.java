package es.usefulearnings.entities;

import java.io.IOException;

/**
 * @author yago.
 */
public interface Savable {
  void save() throws IOException;
}
