package es.usefulearnings.engine;


import es.usefulearnings.engine.connection.DownloaderTask;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.engine.plugin.*;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Stock;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchEngine {

  private final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;

  private ArrayList<Plugin> plugins;

  public static SearchEngine getInstance() {
    return instance;
  }

  private static SearchEngine instance = new SearchEngine();

  private List<Stock> mStocks;

  private ArrayList<DownloaderTask<Company>> companyDownloaders;

  private Thread [] threads;

  private ArrayList<Company> mCompanies;

  private SearchEngine () {
    // Add plugins.
    plugins = new ArrayList<>();
    plugins.add(new ProfilePlugin());
    plugins.add(new BalanceSheetStatementsPlugin());
    plugins.add(new CalendarEventsPlugin());
    plugins.add(new CashFlowStatementsPlugin());
    plugins.add(new DefaultKeyStatisticsPlugin());
    plugins.add(new IncomeStatmentsPlugin());
    plugins.add(new FinancialDataPlugin());

    // get the available mStocks.
    try {
      mStocks = ResourcesHelper.getInstance().getAvailableStocks();
    } catch (NoStocksFoundException e) {
      // i can't do nothing without mStocks!
    }

    // create all the Companies.
    mCompanies =  new ArrayList<>();

    threads = new Thread[MAX_THREADS];
  }

  /**
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
      instance.plugins.forEach(plugin -> {
        plugin.addInfo(company);
      });

      return company;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return company;
  }

  public void getAllCompaniesData() {
    // set all companies to new companies
    mStocks.forEach(stock ->
      stock.getSymbols().forEach(
        symbol -> {
          if(!symbol.trim().contains("-") && !symbol.trim().contains(".")) { // Yahoo doesn't have info for this.
            Company company = new Company();
            company.setSymbol(symbol);
            mCompanies.add(company);
          } else {
            System.out.println("********************" + symbol + "*************************************");
            // System.exit(0);
          }
        })
    );
    // download its data
    companyDownloaders = new ArrayList<>();

    for(int i = 0; i < MAX_THREADS ; i++){

      int from = i * (mCompanies.size() / MAX_THREADS);
      int to = from + (mCompanies.size() / MAX_THREADS);
      if(i == MAX_THREADS - 1) to = mCompanies.size();

      companyDownloaders.add(
        new DownloaderTask<>(
          plugins,
          (to < mCompanies.size()) ? mCompanies.subList(from, to) : mCompanies.subList(from, mCompanies.size())
        )
      );

      threads[i] = new Thread(companyDownloaders.get(i));
      threads[i].setDaemon(true);
      threads[i].start();
    }
  }


  public ArrayList<DownloaderTask<Company>> getCompaniesDownloaders() {
    return this.companyDownloaders;
  }

  public Thread[] getThreads() {
    return threads;
  }


}
