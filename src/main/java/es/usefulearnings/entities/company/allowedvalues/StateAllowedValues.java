package es.usefulearnings.entities.company.allowedvalues;

import es.usefulearnings.annotation.AllowedValuesRetriever;
import es.usefulearnings.engine.Core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yago on 26/09/2016.
 */
public class StateAllowedValues implements AllowedValuesRetriever {
  @Override
  public Collection<String> getAllowedValues() {
    Set<String> values = new HashSet<>();
    Core.getInstance().getAllCompanies().values().forEach(company -> {
      if(company.getProfile().getState() != null && !company.getProfile().getState().equals("")){
        values.add(company.getProfile().getState());
      }
    });

    return values;
  }
}
