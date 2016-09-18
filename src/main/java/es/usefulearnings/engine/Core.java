package es.usefulearnings.engine;

import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.engine.plugin.*;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Option;
import es.usefulearnings.entities.OptionChain;
import es.usefulearnings.entities.Stock;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Core {

  public final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;

  private ArrayList<Plugin> companiesPlugins;

  private List<Stock> mStocks;

  private Thread[] threads;

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
      throw new RuntimeException(e);
    }


    threads = new Thread[MAX_THREADS];
  }

  /**
   * TODO: DELETE THIS
   * Sets all Company's data depending on it's modules.
   *
   * @param symbol Company's symbol in the selected stock.
   * @return a new Company with it's modules set.
   * @see YahooLinks for modules.
   * @see Company
   */
  public Company getSingleCompanyData(String symbol) {
    Company company = new Company();
    company.setSymbol(symbol);
    try {
      for (Plugin plugin :
        companiesPlugins) {
        plugin.addInfo(company);
      }

      return company;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return company;
  }

  public Map<String, Company> getAllCompanies() {
    Map<String, Company> companies = new TreeMap<>();

    for (Stock stock : mStocks) {
      companies.putAll(stock.getCompanies());
    }

    return companies;
  }


  public Map<String, Company> getCompaniesFromStock(String stockName) throws IllegalArgumentException {
    for (Stock stock : mStocks) {
      if (stock.getName().equals(stockName)) {
        return stock.getCompanies();
      }
    }

    throw new IllegalArgumentException("Could not find a stock named " + stockName);
  }

  public Thread[] getAvailableThreads() {
    return threads;
  }

  public ArrayList<Plugin> getCompaniesPlugins() {
    return this.companiesPlugins;
  }

  public <E> void setEntityData(E entity) {
    if (entity != null) {
      if (entity instanceof Company) {
        setCompanyData((Company)entity);
      }

      if (entity instanceof Option) {
        // TODO implement method setOptionsChainData()
      }

      if (entity instanceof OptionChain) {
        // TODO implement method setOptionsChainData()
      }
    }
  }

  public void setCompanyData(Company settedCompany){
    mStocks.stream()
      .filter(
        stock -> stock.getName().equals(settedCompany.getStockName())
      ).forEach(
        stock -> stock.getCompanies().replace(settedCompany.getSymbol(), settedCompany)
    );
  }

  public void setCompaniesData(List<Company> companies) {
    companies.forEach(this::setCompanyData);
  }


}
