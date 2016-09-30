package es.usefulearnings.entities.company;

import es.usefulearnings.annotation.AllowedValuesRetriever;
import es.usefulearnings.engine.Core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yago.
 */
public class RecommendationKeyAllowedValues implements AllowedValuesRetriever {
  @Override
  public Collection<String> getAllowedValues() {
    Set<String> values = new HashSet<>();
    Core.getInstance().getAllCompanies().values().forEach(company -> {
      if(company.getFinancialData().getRecommendationKey() != null && !company.getFinancialData().getRecommendationKey().equals("")){
        values.add(company.getFinancialData().getRecommendationKey());
      }
    });

    return values;
  }
}
