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
public class YahooFinanceAPI {

  //Company
  public static final String COMPANY_ASSET_PROFILE              = "assetProfile";
  public static final String COMPANY_FINANCIAL_DATA             = "financialData";
  public static final String COMPANY_DEFAULT_KEY_STATISTICS     = "defaultKeyStatistics";
  public static final String COMPANY_CALENDAR_EVENTS            = "calendarEvents";
  public static final String COMPANY_INCOME_STATEMENT_HISTORY   = "incomeStatementHistory";
  public static final String COMPANY_CASHFLOW_STATEMENT_HISTORY = "cashflowStatementHistory";
  public static final String COMPANY_BALANCE_SHEET_HISTORY      = "balanceSheetHistory";
  public static final String COMPANY_SUMMARY_DETAIL             = "summaryDetail";

  //Options
  public static final String OPTION_PRICE          = "price";
  public static final String OPTION_SUMMARY_DETAIL = "summaryDetail";

  private static final String DATE_QUERY = "?date=";

  private static YahooFinanceAPI yahooLinks = new YahooFinanceAPI();

  public static YahooFinanceAPI getInstance() {
    return yahooLinks;
  }
  private YahooFinanceAPI() {
    companyQuoteSummaryModules.add(COMPANY_CASHFLOW_STATEMENT_HISTORY);
    companyQuoteSummaryModules.add(COMPANY_ASSET_PROFILE);
    companyQuoteSummaryModules.add(COMPANY_BALANCE_SHEET_HISTORY);
    companyQuoteSummaryModules.add(COMPANY_DEFAULT_KEY_STATISTICS);
    companyQuoteSummaryModules.add(COMPANY_FINANCIAL_DATA);
    companyQuoteSummaryModules.add(COMPANY_INCOME_STATEMENT_HISTORY);
    companyQuoteSummaryModules.add(COMPANY_CALENDAR_EVENTS);
    companyQuoteSummaryModules.add(COMPANY_SUMMARY_DETAIL);

    optionQuoteSummaryModules.add(OPTION_SUMMARY_DETAIL);
    optionQuoteSummaryModules.add(OPTION_PRICE);
  }

  public URL getHistoricalDataURL(String symbol, String startDate , String endDate) throws MalformedURLException {
    String yql = "select * from yahoo.finance.historicaldata where symbol = \""+ symbol +
        "\" and startDate = \""+ startDate + "\" and endDate = \""+ endDate +"\"";

    yql = yql.replaceAll("\\s", "%20");

    return new URL(
        "https://query.yahooapis.com/v1/public/yql?q=" + yql + "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
    );
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
   * Generates a link to the Yahoo Finance API to get the specific modules on YahooFinanceAPI.companyQuoteSummaryModules Map
   * @param companySymbol company's symbol to search in Yahoo! Finance
   * @param quoteSummaryModules Collection that contains the specific modules to generate the link
   * @return Link to quote summary with the specified modules in companyQuoteSummaryModules
   * @throws IllegalArgumentException if companyQuoteSummaryModules is empty or modules in that collection doesn't match with
   * the modules on YahooFinanceAPI.companyQuoteSummaryModules Map
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