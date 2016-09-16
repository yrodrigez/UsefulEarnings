package es.usefulearnings.engine;


import es.usefulearnings.engine.connection.DownloadProcess;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.engine.plugin.*;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Stock;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;

import java.util.ArrayList;
import java.util.List;

public class Core {

  public final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;

  private ArrayList<Plugin> companiesPlugins;

  private List<Stock> mStocks;

  private Thread [] threads;

  private ArrayList<DownloadProcess<Company>> companyProcesses;
  /**
   * Companies from all loaded stocks
   */
  private ArrayList<Company> allCompanies;

  public static Core getInstance() {
    return instance;
  }

  private static Core instance = new Core();
  private Core() {
    // Add companiesPlugins.
    companiesPlugins = new ArrayList<>();
    companiesPlugins.add(new ProfilePlugin());
    companiesPlugins.add(new BalanceSheetStatementsPlugin());
    companiesPlugins.add(new CalendarEventsPlugin());
    companiesPlugins.add(new CashFlowStatementsPlugin());
    companiesPlugins.add(new DefaultKeyStatisticsPlugin());
    companiesPlugins.add(new IncomeStatmentsPlugin());
    companiesPlugins.add(new FinancialDataPlugin());

    // get the available mStocks.
    try {
      mStocks = ResourcesHelper.getInstance().getAvailableStocks();
    } catch (NoStocksFoundException e) {
      // i can't do nothing without mStocks!
    }

    // create all the Companies.
    allCompanies =  new ArrayList<>();
      mStocks.forEach(stock ->
      stock.getSymbols().forEach(
        symbol -> {
          if(!symbol.trim().contains("-") && !symbol.trim().contains(".")) { // Yahoo doesn't have info for this.
            Company company = new Company();
            company.setSymbol(symbol);
            allCompanies.add(company);
          }
        })
    );

    threads = new Thread[MAX_THREADS];
  }

  /**
   * TODO: DELETE THIS
   * Sets all Company's data depending on it's modules.
   * @see YahooLinks for modules.
   * @see Company
   * @param symbol Company's symbol in the selected stock.
   * @return a new Company with it's modules set.
   */
  public Company getSingleCompanyData(String symbol) {
    Company company = new Company();
    company.setSymbol(symbol);
    try {
      for ( Plugin plugin:
        companiesPlugins) {
        plugin.addInfo(company);
      }

      return company;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return company;
  }

  public ArrayList<Company> getAllCompanies(){
    return this.allCompanies;
  }



  public ArrayList<Company> getCompaniesFromStock(String stockName) throws IllegalArgumentException {
    for(Stock stock :
      mStocks){
      if(stock.getName().equals(stockName)){
        ArrayList<Company> newCompanies = new ArrayList<>();
        stock.getSymbols().forEach(symbol -> newCompanies.add(new Company(symbol)));
        return newCompanies;
      }
    }
    throw new IllegalArgumentException("could not find a stock named " + stockName);
  }

  public Thread[] getAvailableThreads() {
    return threads;
  }

  public ArrayList<Plugin> getCompaniesPlugins() {
    return this.companiesPlugins;
  }

  public ArrayList<DownloadProcess<Company>> getCompanyProcesses() {
    return this.companyProcesses;
  }
}
