package es.usefulearnings.engine.connection;

import java.io.UnsupportedEncodingException;
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
      return YahooFinanceAPI.getInstance().getYahooQuoteSummaryLink(
        companySymbol,
        YahooFinanceAPI.COMPANY_SUMMARY_DETAIL,
        YahooFinanceAPI.COMPANY_ASSET_PROFILE,
        YahooFinanceAPI.COMPANY_FINANCIAL_DATA,
        YahooFinanceAPI.COMPANY_DEFAULT_KEY_STATISTICS,
        YahooFinanceAPI.COMPANY_CALENDAR_EVENTS,
        YahooFinanceAPI.COMPANY_INCOME_STATEMENT_HISTORY,
        YahooFinanceAPI.COMPANY_CASHFLOW_STATEMENT_HISTORY,
        YahooFinanceAPI.COMPANY_BALANCE_SHEET_HISTORY
      );
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    } catch (UnsupportedEncodingException e){
      System.err.println(e.getMessage());
      e.printStackTrace();
      return null;
    }
  }
}
