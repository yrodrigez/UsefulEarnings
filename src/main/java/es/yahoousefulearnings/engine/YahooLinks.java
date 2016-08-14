package es.yahoousefulearnings.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains methods that will generate links to the known Yahoo! finance API
 * @author Yago.
 */
public class YahooLinks {

  public static final String dateQuery = "&date=";
  /**
   * companyQuoteSummaryModules
   * Permitted modules to generate a link to get a company summary
   * @see "https://query2.finance.yahoo.com/v10/finance/quoteSummary/COMPANY_SYMBOL?formatted=true&modules=ANY_QUOTESUMMARYMODULES_SEPPARATED_BY_COMMAS&corsDomain=finance.yahoo.com"
   */
  public static Map<String, String> companyQuoteSummaryModules = new HashMap<>();
  static {
    companyQuoteSummaryModules.put("Asset Profile", "assetProfile"); // solo para info específica...
    companyQuoteSummaryModules.put("Financial Data", "financialData"); // *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Default Key Statistics", "defaultKeyStatistics"); // *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Calendar Events", "calendarEvents"); // *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Income Statement History", "incomeStatementHistory"); // *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Cashflow Statement History", "cashflowStatementHistory");// *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Balance Sheet History", "balanceSheetHistory");// *** IMPORTANTE ***

    /* ***REVISAR DESPUES***
    companyQuoteSummaryModules.put("Upgrade Downgrade History", "upgradeDowngradeHistory");// datos para los gráficos del sumario...
    companyQuoteSummaryModules.put("Earnings", "earnings");// datos para los gráficos del sumario...
    companyQuoteSummaryModules.put("Summary Profile", "summaryProfile"); // como assetsProfile pero menos extenso....

    companyQuoteSummaryModules.put("Income Statement History Quarterly", "incomeStatementHistoryQuarterly"); //igual al anterior pero qutrimestral
    companyQuoteSummaryModules.put("Cashflow Statement History Quarterly", "cashflowStatementHistoryQuarterly"); //igual al anterior pero qutrimestral
    companyQuoteSummaryModules.put("Balance Sheet History Quarterly", "balanceSheetHistoryQuarterly"); //igual al anterior pero qutrimestral

    // modules=earningsHistory,earningsTrend,industryTrend
    companyQuoteSummaryModules.put("Earnings History", "earningsHistory"); // sin definir
    companyQuoteSummaryModules.put("Earnings Trend", "earningsTrend"); // sin definir
    companyQuoteSummaryModules.put("Industry Trend", "industryTrend"); // sin definir

    companyQuoteSummaryModules.put("Institution Ownership", "institutionOwnership"); // *** ¿Basura? ***
    companyQuoteSummaryModules.put("Major Direct Holders", "majorDirectHolders"); // *** ¿BASURA? ***
    companyQuoteSummaryModules.put("Major Holders Breakdown", "majorHoldersBreakdown");// *** ¿BASURA? ***
    companyQuoteSummaryModules.put("Insider Transactions", "insiderTransactions");// *** ¿BASURA? ***
    companyQuoteSummaryModules.put("Insider Holders", "insiderHolders");// *** ¿BASURA? ***
    companyQuoteSummaryModules.put("Net Share Purchase Activity", "netSharePurchaseActivity");// *** ¿BASURA? ***

    companyQuoteSummaryModules.put("Sec Filings", "secFilings"); // *** BASURA ***
    companyQuoteSummaryModules.put("Recommendation Trend", "recommendationTrend");// datos para los gráficos del sumario... *** BASURA de verdad ***
    */
  }
  /**
   * companyQuoteSummaryModules
   * Permitted modules to generate a link to get a option summary
   * @see "https://query1.finance.yahoo.com/v10/finance/quoteSummary/OPTION_SYMBOL?modules=ANY_QUOTESUMMARYMODULES_SEPPARATED_BY_COMMAS"
   */
  public static Map<String, String> optionQuoteSummaryModules = new HashMap<>();
  static {
    optionQuoteSummaryModules.put("Asset Profile", "assetProfile");
    optionQuoteSummaryModules.put("Financial Data", "financialData");
  }

  /**
   * Generates a link to the Yahoo Finance API to get the specific modules on YahooLinks.companyQuoteSummaryModules Map
   * @param companySymbol company's symbol to search in Yahoo! Finance
   * @param quoteSummaryModules Collection that contains the specific modules to generate the link
   * @return Link to quote summary with the specified modules in companyQuoteSummaryModules
   * @throws IllegalArgumentException if companyQuoteSummaryModules is empty or modules in that collection doesn't match with
   * the modules on YahooLinks.companyQuoteSummaryModules Map
   */
  public static String getYahooquoteSummaryLink(String companySymbol, Collection<String> quoteSummaryModules) throws IllegalArgumentException {
    if(quoteSummaryModules.isEmpty()) throw new IllegalArgumentException("Quote Summary modules cannot bew empty");
    if(companySymbol.isEmpty()) throw new IllegalArgumentException("Symbol can't be empty");
    StringBuilder sb = new StringBuilder("https://query2.finance.yahoo.com/v10/finance/quoteSummary/");
    sb.append(companySymbol);
    sb.append("?/formatted=true&modules=");
    for(String module : quoteSummaryModules){
      if(!YahooLinks.companyQuoteSummaryModules.containsKey(module)) {
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
   * @throws IllegalArgumentException if companySymbol is empty
   */
  public static String getYahooOptionsLink(String companySymbol) throws IllegalArgumentException {
    if(companySymbol.isEmpty()) throw new IllegalArgumentException("Company symbol can't be empty");
    return "https://query2.finance.yahoo.com/v7/finance/options/" + companySymbol + "/?formatted=true";
  }

  /**
   * Generates a link to the Company's options with an specific date
   * @param companySymbol company's symbol to search it's options in Yahoo! Finance
   * @param date link to the company's options
   * @throws IllegalArgumentException if company symbol is empty or date is not a valid unix timestamp
   * @return link to the company's options with the specified date
   */
  public static String getYahooOptionsLink(String companySymbol, long date) throws IllegalArgumentException {
    if (date > Integer.MAX_VALUE) throw new IllegalArgumentException("That's not a valid raw date");
    return getYahooOptionsLink(companySymbol) + dateQuery + date ;
  }

}
