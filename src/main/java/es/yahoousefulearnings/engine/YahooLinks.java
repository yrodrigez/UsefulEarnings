package es.yahoousefulearnings.engine;

import es.yahoousefulearnings.entities.Company;

import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase static para la construcción de links a Yahoo Finance
 * @author Yago.
 */
public class YahooLinks {

  public static final String dateQuery = "&date=";
  /**
   * quoteSummaryModules
   * Permitted modules to generate a link to get a company summary
   * @see "https://query2.finance.yahoo.com/v10/finance/quoteSummary/COMPANY_SYMBOL?formatted=true&modules=ANY_QUOTESUMMARYMODULES_SEPPARATED_BY_COMMAS&corsDomain=finance.yahoo.com"
   */
  public static Map<String, String> quoteSummaryModules = new HashMap<>();
  static {
    quoteSummaryModules.put("Asset Profile", "assetProfile"); // solo para info específica...
    quoteSummaryModules.put("Financial Data", "financialData"); // *** IMPORTANTE ***
    quoteSummaryModules.put("Default Key Statistics", "defaultKeyStatistics"); // *** IMPORTANTE ***
    quoteSummaryModules.put("Calendar Events", "calendarEvents"); // *** IMPORTANTE ***
    quoteSummaryModules.put("Income Statement History", "incomeStatementHistory"); // *** IMPORTANTE ***
    quoteSummaryModules.put("Cashflow Statement History", "cashflowStatementHistory");// *** IMPORTANTE ***
    quoteSummaryModules.put("Balance Sheet History", "balanceSheetHistory");// *** IMPORTANTE ***

    /* ***REVISAR DESPUES***
    quoteSummaryModules.put("Upgrade Downgrade History", "upgradeDowngradeHistory");// datos para los gráficos del sumario...
    quoteSummaryModules.put("Earnings", "earnings");// datos para los gráficos del sumario...
    quoteSummaryModules.put("Summary Profile", "summaryProfile"); // como assetsProfile pero menos extenso....

    quoteSummaryModules.put("Income Statement History Quarterly", "incomeStatementHistoryQuarterly"); //igual al anterior pero qutrimestral
    quoteSummaryModules.put("Cashflow Statement History Quarterly", "cashflowStatementHistoryQuarterly"); //igual al anterior pero qutrimestral
    quoteSummaryModules.put("Balance Sheet History Quarterly", "balanceSheetHistoryQuarterly"); //igual al anterior pero qutrimestral

    // modules=earningsHistory,earningsTrend,industryTrend
    quoteSummaryModules.put("Earnings History", "earningsHistory"); // sin definir
    quoteSummaryModules.put("Earnings Trend", "earningsTrend"); // sin definir
    quoteSummaryModules.put("Industry Trend", "industryTrend"); // sin definir

    quoteSummaryModules.put("Institution Ownership", "institutionOwnership"); // *** ¿Basura? ***
    quoteSummaryModules.put("Major Direct Holders", "majorDirectHolders"); // *** ¿BASURA? ***
    quoteSummaryModules.put("Major Holders Breakdown", "majorHoldersBreakdown");// *** ¿BASURA? ***
    quoteSummaryModules.put("Insider Transactions", "insiderTransactions");// *** ¿BASURA? ***
    quoteSummaryModules.put("Insider Holders", "insiderHolders");// *** ¿BASURA? ***
    quoteSummaryModules.put("Net Share Purchase Activity", "netSharePurchaseActivity");// *** ¿BASURA? ***

    quoteSummaryModules.put("Sec Filings", "secFilings"); // *** BASURA ***
    quoteSummaryModules.put("Recommendation Trend", "recommendationTrend");// datos para los gráficos del sumario... *** BASURA de verdad ***
    */
  }

  /**
   * Generates a link to the Yahoo Finance API to get the specific modules on YahooLinks.quoteSummaryModules Map
   * @param companySymbol company's symbol to search in Yahoo! Finance
   * @param quoteSummaryModules Collection that contains the specific modules to generate the link
   * @return Link to quote summary with the specified modules in quoteSummaryModules
   * @throws IllegalArgumentException if quoteSummaryModules is empty or modules in that collection doesn't match with
   * the modules on YahooLinks.quoteSummaryModules Map
   */
  public static String getYahooquoteSummaryLink(String companySymbol, Collection<String> quoteSummaryModules) throws IllegalArgumentException {
    if(quoteSummaryModules.isEmpty()) throw new IllegalArgumentException("Quote Summary modules cannot bew empty");
    StringBuilder sb = new StringBuilder("https://query2.finance.yahoo.com/v10/finance/quoteSummary/");
    sb.append(companySymbol);
    sb.append("?/formatted=true&modules=");
    for(String module : quoteSummaryModules){
      if(!YahooLinks.quoteSummaryModules.containsKey(module)) {
        throw new IllegalArgumentException("I don't have that module");
      } else {
        sb.append(module);
        sb.append(",");
      }
    }
    return sb.toString();
  }

  /**
   * Generates a link to the Company's options
   * @param companySymbol company's symbol to search it's options in Yahoo! Finance
   * @return link to the company's options
   */
  public static String getYahooOptionsLink(String companySymbol){
    return "https://query2.finance.yahoo.com/v7/finance/options/" + companySymbol + "/?formatted=true";
  }

  /**
   * Generates a link to the Company's options with an specific date
   * @param companySymbol company's symbol to search it's options in Yahoo! Finance
   * @param date link to the company's options
   * @return link to the company's options with the specified date
   */
  public static String getYahooOptionsLink(String companySymbol, long date){
    return getYahooOptionsLink(companySymbol) + dateQuery + date ;
  }

}
