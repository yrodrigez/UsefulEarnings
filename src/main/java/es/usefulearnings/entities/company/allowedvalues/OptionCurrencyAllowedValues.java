package es.usefulearnings.entities.company.allowedvalues;

import es.usefulearnings.annotation.AllowedValuesRetriever;
import es.usefulearnings.engine.Core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class OptionCurrencyAllowedValues implements AllowedValuesRetriever {
  @Override
  public Collection<String> getAllowedValues() {
    Set<String> values = new HashSet<>();
    Core.getInstance().getAllCompanies().values().forEach(company -> {
      if(company.getOptionChain().getPuts() != null){
        company.getOptionChain().getPuts().forEach(optionLink -> values.add(optionLink.getCurrency()));
      }
      if(company.getOptionChain().getCalls() != null){
        company.getOptionChain().getCalls().forEach(optionLink -> values.add(optionLink.getCurrency()));
      }
    });

    return values;
  }
}
