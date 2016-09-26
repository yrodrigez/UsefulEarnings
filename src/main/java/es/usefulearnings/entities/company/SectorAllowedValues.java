package es.usefulearnings.entities.company;

import es.usefulearnings.annotation.AllowedValuesRetriever;

import java.util.Arrays;
import java.util.List;

/**
 * @author yago.
 */
public class SectorAllowedValues implements AllowedValuesRetriever {

  @Override
  public List<String> getAllowedValues()  {
    //get Core. getAllCompaines, retrieve a list of allowed values for Sector
    return Arrays.asList("a", "b", "c");
  };
}
