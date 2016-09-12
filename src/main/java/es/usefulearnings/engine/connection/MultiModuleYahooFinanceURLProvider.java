package es.usefulearnings.engine.connection;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * ${PATH}
 * Created by yago on 12/09/16.
 */
public class MultiModuleYahooFinanceURLProvider implements YahooFinanceURLProvider {

  private static MultiModuleYahooFinanceURLProvider mInstance = new MultiModuleYahooFinanceURLProvider();

  public static MultiModuleYahooFinanceURLProvider getInstance() {
    return mInstance;
  }

  @Override
  public URL getURLForModule(String companySymbol, String module) {
    try {
      return YahooLinks.getInstance().getYahooQuoteSummaryLink(
        companySymbol,
        YahooLinks.COMPANY_ASSET_PROFILE,
        YahooLinks.COMPANY_FINANCIAL_DATA,
        YahooLinks.COMPANY_DEFAULT_KEY_STATISTICS,
        YahooLinks.COMPANY_CALENDAR_EVENTS,
        YahooLinks.COMPANY_INCOME_STATEMENT_HISTORY,
        YahooLinks.COMPANY_CASHFLOW_STATEMENT_HISTORY,
        YahooLinks.COMPANY_BALANCE_SHEET_HISTORY
      );
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
