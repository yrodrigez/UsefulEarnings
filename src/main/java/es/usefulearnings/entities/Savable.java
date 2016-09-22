package es.usefulearnings.entities;

import java.io.File;
import java.io.IOException;

/**
 * @author yago.
 */
public interface Savable {
  void save(File fileToSave) throws IOException;
}
