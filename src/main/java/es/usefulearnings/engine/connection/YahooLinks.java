package es.usefulearnings.engine.connection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains methods that will generate links to the known Yahoo! finance API
 *
 * @author Yago.
 */
public class YahooLinks {

  //Company
  public static final String COMPANY_ASSET_PROFILE              = "assetProfile";
  public static final String COMPANY_FINANCIAL_DATA             = "financialData";
  public static final String COMPANY_DEFAULT_KEY_STATISTICS     = "defaultKeyStatistics";
  public static final String COMPANY_CALENDAR_EVENTS            = "calendarEvents";
  public static final String COMPANY_INCOME_STATEMENT_HISTORY   = "incomeStatementHistory";
  public static final String COMPANY_CASHFLOW_STATEMENT_HISTORY = "cashflowStatementHistory";
  public static final String COMPANY_BALANCE_SHEET_HISTORY      = "balanceSheetHistory";
  //Options
  public static final String OPTION_PRICE          = "price";
  public static final String OPTION_SUMMARY_DETAIL = "summaryDetail";

  private static final String DATE_QUERY = "?date=";

  private static YahooLinks yahooLinks = new YahooLinks();

  public static YahooLinks getInstance() {
    return yahooLinks;
  }
  private YahooLinks() {
    companyQuoteSummaryModules.add(COMPANY_CASHFLOW_STATEMENT_HISTORY);
    companyQuoteSummaryModules.add(COMPANY_ASSET_PROFILE);
    companyQuoteSummaryModules.add(COMPANY_BALANCE_SHEET_HISTORY);
    companyQuoteSummaryModules.add(COMPANY_DEFAULT_KEY_STATISTICS);
    companyQuoteSummaryModules.add(COMPANY_FINANCIAL_DATA);
    companyQuoteSummaryModules.add(COMPANY_INCOME_STATEMENT_HISTORY);
    companyQuoteSummaryModules.add(COMPANY_CALENDAR_EVENTS);

    optionQuoteSummaryModules.add(OPTION_SUMMARY_DETAIL);
    optionQuoteSummaryModules.add(OPTION_PRICE);
  }

  /**
   * companyQuoteSummaryModules
   * Permitted modules to generate a link to get a company summary
   * "https://query2.finance.yahoo.com/v10/finance/quoteSummary/COMPANY_SYMBOL?formatted=true&modules=ANY_QUOTESUMMARYMODULES_SEPPARATED_BY_COMMAS&corsDomain=finance.yahoo.com"
   */
  private List<String> companyQuoteSummaryModules = new LinkedList<>();

  /**
   * companyQuoteSummaryModules
   * Permitted modules to generate a link to get a option summary
   *  "https://query1.finance.yahoo.com/v10/finance/quoteSummary/OPTION_SYMBOL?modules=ANY_QUOTESUMMARYMODULES_SEPPARATED_BY_COMMAS"
   */
  private List<String> optionQuoteSummaryModules = new LinkedList<>();


  /**
   * Generates a link to the Yahoo Finance API to get the specific modules on YahooLinks.companyQuoteSummaryModules Map
   * @param companySymbol company's symbol to search in Yahoo! Finance
   * @param quoteSummaryModules Collection that contains the specific modules to generate the link
   * @return Link to quote summary with the specified modules in companyQuoteSummaryModules
   * @throws IllegalArgumentException if companyQuoteSummaryModules is empty or modules in that collection doesn't match with
   * the modules on YahooLinks.companyQuoteSummaryModules Map
   */
  public URL getYahooQuoteSummaryLink(String companySymbol, String ... quoteSummaryModules) throws IllegalArgumentException, MalformedURLException {
    if(quoteSummaryModules.length == 0) throw new IllegalArgumentException("Quote Summary modules cannot bew empty");
    if(companySymbol.isEmpty() || companySymbol.equals("")) throw new IllegalArgumentException("Symbol can't be empty");
    StringBuilder sb = new StringBuilder("https://query2.finance.yahoo.com/v10/finance/quoteSummary/");
    sb.append(companySymbol);
    sb.append("?formatted=true&modules=");
    for(String module : quoteSummaryModules){
      if(!companyQuoteSummaryModules.contains(module)) {
        throw new IllegalArgumentException("I don't have that module");
      } else {
        sb.append(module);
        sb.append(",");
      }
    }

    return new URL(sb.toString());
  }

  /**
   * Generates a link to the Company's options Chain
   * @param companySymbol company's symbol to search it's options in Yahoo! Finance
   * @return link to the company's options
   * @throws IllegalArgumentException if companySymbol is empty
   */
  public URL getYahooCompanysOptionChainLink(String companySymbol) throws IllegalArgumentException, MalformedURLException {
    if(companySymbol.isEmpty()) throw new IllegalArgumentException("Company's symbol can't be empty");

    return new URL("https://query2.finance.yahoo.com/v7/finance/options/" + companySymbol);
  }

  /**
   * Generates a link to the Company Options Chain with an specific date
   * @param companySymbol company's symbol to search it's options in Yahoo! Finance
   * @param date link to the company's options
   * @throws IllegalArgumentException if company symbol is empty or date is not a valid unix timestamp
   * @return link to the company's options with the specified date
   */
  public URL getYahooCompanysOptionChainLink(String companySymbol, long date) throws IllegalArgumentException, MalformedURLException {
    if (date > Integer.MAX_VALUE) throw new IllegalArgumentException("That's not a valid raw date");

    return new URL(getYahooCompanysOptionChainLink(companySymbol) + DATE_QUERY + date);
  }

  /**
   * Generates a link to the quoteSummary of an company's option
   * @param optionSymbol key, it can be generated by it's company's symbol expiration date, it's type and strike
   * @param modules must be specified on quoteSummaryOptions.
   * @return Link to the option specified modules.
   * @throws IllegalArgumentException if modules or optionSymbol are null
   */
  public URL getYahooOptionQuoteSummaryLink(String optionSymbol, String ... modules) throws IllegalArgumentException, MalformedURLException {
    if(optionSymbol == null || optionSymbol.isEmpty()) throw new IllegalArgumentException("Option Symbol can't be empty");
    if(modules == null || modules.length < 1) throw new IllegalArgumentException("Modules are empty");

    StringBuilder sb = new StringBuilder("https://query1.finance.yahoo.com/v10/finance/quoteSummary/");
    sb.append(optionSymbol);
    sb.append("?modules=");
    for(String module : modules){
      sb.append(module);
      sb.append(',');
    }

    return new URL(sb.toString());
  }

}
/*
    companyQuoteSummaryModules.put("Asset Profile", "assetProfile"); // solo para info específica...
    companyQuoteSummaryModules.put("Financial Data", "financialData"); // *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Default Key Statistics", "defaultKeyStatistics"); // *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Calendar Events", "calendarEvents"); // *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Income Statement History", "incomeStatementHistory"); // *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Cashflow Statement History", "cashflowStatementHistory");// *** IMPORTANTE ***
    companyQuoteSummaryModules.put("Balance Sheet History", "balanceSheetHistory");// *** IMPORTANTE ***

     ***REVISAR DESPUES***
    companyQuoteSummaryModules.put("Upgrade Downgrade History", "upgradeDowngradeHistory");// datos para los gráficos del sumario...
    companyQuoteSummaryModules.put("Earnings", "earnings");// datos para los gráficos del sumario...
    companyQuoteSummaryModules.put("Summary Profile", "summaryProfile"); // como assetsProfile pero menos extenso....

    companyQuoteSummaryModules.put("Income Statement History Quarterly", "incomeStatementHistoryQuarterly"); //igual al anterior pero qutrimestral
    companyQuoteSummaryModules.put("Cashflow Statement History Quarterly", "cashflowStatementHistoryQuarterly"); //igual al anterior pero qutrimestral
    companyQuoteSummaryModules.put("Balance Sheet History Quarterly", "balanceSheetHistoryQuarterly"); //igual al anterior pero qutrimestral

    // modules=earningsHistory.json,earningsTrend.json,industryTrend
    companyQuoteSummaryModules.put("Earnings History", "earningsHistory.json"); // sin definir
    companyQuoteSummaryModules.put("Earnings Trend", "earningsTrend.json"); // sin definir
    companyQuoteSummaryModules.put("Industry Trend", "industryTrend"); // sin definir

    companyQuoteSummaryModules.put("Institution Ownership", "institutionOwnership"); // *** ¿Basura? ***
    companyQuoteSummaryModules.put("Major Direct Holders", "majorDirectHolders"); // *** ¿BASURA? ***
    companyQuoteSummaryModules.put("Major Holders Breakdown", "majorHoldersBreakdown");// *** ¿BASURA? ***
    companyQuoteSummaryModules.put("Insider Transactions", "insiderTransactions");// *** ¿BASURA? ***
    companyQuoteSummaryModules.put("Insider Holders", "insiderHolders.json");// *** ¿BASURA? ***
    companyQuoteSummaryModules.put("Net Share Purchase Activity", "netSharePurchaseActivity");// *** ¿BASURA? ***

    companyQuoteSummaryModules.put("Sec Filings", "secFilings"); // *** BASURA ***
    companyQuoteSummaryModules.put("Recommendation Trend", "recommendationTrend");// datos para los gráficos del sumario... *** BASURA de verdad ***
*/