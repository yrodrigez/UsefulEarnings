package es.usefulearnings.engine.plugin;

import es.usefulearnings.entities.Company;

/**
 *
 * Created by yago on 7/09/16.
 */
public interface Plugin {
  void addInfo(Company company);
  void setCompanySymbol(String companySymbol);
}
