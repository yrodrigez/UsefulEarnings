package es.usefulearnings.entities.company.allowedvalues;

import es.usefulearnings.annotation.AllowedValuesRetriever;
import es.usefulearnings.engine.Core;

import java.util.*;

/**
 * @author yago.
 */
public class SectorAllowedValues implements AllowedValuesRetriever {

  @Override
  public Collection<String> getAllowedValues() {
    Set<String> values = new HashSet<>();
    Core.getInstance().getAllCompanies().values().forEach(company -> {
      if(company.getProfile().getSector() != null && !company.getProfile().getSector().equals(""))
        values.add(company.getProfile().getSector());
    });

    return values;
  }
}
