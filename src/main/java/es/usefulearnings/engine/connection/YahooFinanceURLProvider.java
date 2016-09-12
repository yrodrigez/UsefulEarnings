package es.usefulearnings.engine.connection;

import java.net.URL;

/**
 *
 * Created by yago on 12/09/16.
 */
public interface YahooFinanceURLProvider {
  URL getURLForModule(String companySymbol, String module);
}
