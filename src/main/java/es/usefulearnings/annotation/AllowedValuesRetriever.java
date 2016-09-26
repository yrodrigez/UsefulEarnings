package es.usefulearnings.annotation;

import java.util.Collection;

/**
 * @author yago.
 */
public interface AllowedValuesRetriever {
  Collection<String> getAllowedValues();
}
