package es.usefulearnings.engine;

import es.usefulearnings.engine.filter.CompanyFilter;
import es.usefulearnings.engine.filter.Filter;
import es.usefulearnings.engine.filter.RestrictionValue;
import es.usefulearnings.engine.plugin.*;
import es.usefulearnings.entities.*;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Core {

  private final ArrayList<Plugin> companiesPlugins;

  private List<Stock> _stocks;

  public static Core getInstance() {
    return instance;
  }

  private final static Core instance = new Core();

  private final List<Filter> _appliedFilters;

  private boolean isDataLoaded;

  private long _loadedPackageId;

  private ThreadPool _threadPool;

  private Core() {
    isDataLoaded = false;
    _loadedPackageId = 0;
    // Add companiesPlugins.
    companiesPlugins = new ArrayList<>();
    companiesPlugins.add(new ProfilePlugin());
    companiesPlugins.add(new CalendarEventsPlugin());
    companiesPlugins.add(new DefaultKeyStatisticsPlugin());
    companiesPlugins.add(new FinancialDataPlugin());
    companiesPlugins.add(new CompanySummaryDetailPlugin());
    companiesPlugins.add(new OptionChainPlugin());
    //companiesPlugins.add(new BalanceSheetStatementsPlugin());
    //companiesPlugins.add(new IncomeStatmentsPlugin());
    //companiesPlugins.add(new CashFlowStatementsPlugin());

    _threadPool = ThreadPool.get_instance();
    setStocksFromFolder();

    _appliedFilters = new LinkedList<>();
  }

  public void runLater(Runnable runnable){
    _threadPool.execute(runnable);
  }

  public void poolShutDown(){
    _threadPool.shutDown();
  }

  public int getMaxPoolSize(){
    return _threadPool.getPoolSize();
  }

  public Company getCompanyFromSymbol(String symbol) throws IllegalArgumentException {
    for (Stock stock : _stocks){
      if (stock.getCompanies().containsKey(symbol))
        return stock.getCompanies().get(symbol);
    }

    throw new IllegalArgumentException("Company " + symbol + " not found.");
  }

  public Map<String, Company> getAllCompanies() {
    Map<String, Company> companies = new TreeMap<>();

    for (Stock stock : _stocks) {
      companies.putAll(stock.getCompanies());
    }

    return companies;
  }

  public Map<String, Company> getCompaniesFromStock(String stockName) throws IllegalArgumentException {
    for (Stock stock : _stocks) {
      if (stock.getName().equals(stockName)) {
        return stock.getCompanies();
      }
    }

    throw new IllegalArgumentException("Can not find a stock named " + stockName);
  }


  public ArrayList<Plugin> getCompanyPlugins() {
    return this.companiesPlugins;
  }

  public <E> void setEntityData(E entity) {
    if (entity != null) {
      if (entity instanceof Company) {
        setCompanyData((Company)entity);
      }
    }
  }


  private void setCompanyData(Company setCompany){
    _stocks.stream()
      .filter(
        stock -> stock.getName().equals(setCompany.getStockName())
      )
      .forEach(
        stock -> stock.getCompanies().replace(setCompany.getSymbol(), setCompany)
      );
  }

  public void removeEntities(Collection<Entity> entitiesToRemove) {
    //TODO: implement remove for options & options chains.
    Collection<Company> companies = new ArrayList<>();
    // Collection<Option> options = new ArrayList<>();

    for (Entity e: entitiesToRemove) {
      if(e instanceof Company) {
        companies.add((Company)e);
      }
    }
    removeCompanies(companies);
  }

  public void removeCompanies(Collection<Company> companiesToRemove) {
    companiesToRemove.forEach(company ->
      getCompaniesFromStock(company.getStockName()).remove(company.getSymbol())
    );
  }

  public long getLoadedPackageId() {
    return _loadedPackageId;
  }

  public void setLoadedPackageId(long loadedPackageId) {
    _loadedPackageId = loadedPackageId;
  }

  public void setFromEntitiesPackage(EntitiesPackage entitiesPackage) {
    List<Stock> newStocks = new LinkedList<>();

    for(Company company : entitiesPackage.getCompanies().values()){
      List<Stock> stocks = newStocks.stream().filter(stock -> stock.getName().equals(company.getStockName())).collect(Collectors.toList());
      if(stocks.isEmpty()){
        Map<String, Company> companies = new TreeMap<>();
        companies.put(company.getSymbol(), company);
        newStocks.add(new Stock(company.getStockName(), companies));
      } else {
        stocks.forEach(s-> s.putCompany(company));
      }
    }

    _stocks = newStocks;
    _loadedPackageId = entitiesPackage.getdateId();
    setDataLoaded(true);
  }

  public void applyFilter(Map<Field, RestrictionValue> parameters) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    CompanyFilter companyFilter = new CompanyFilter(new HashSet<>(getAllCompanies().values()), parameters);
    _appliedFilters.add(companyFilter);
    companyFilter.filter();
  }

  public boolean isDataLoaded() {
    return isDataLoaded;
  }

  public void setDataLoaded(boolean dataLoaded) {
    isDataLoaded = dataLoaded;
  }


  public List<Filter> getAppliedFilters() {
    return _appliedFilters;
  }

  public void setStocks(List<Stock> stocks) {
    this._stocks = stocks;
  }

  public void setStocksFromFolder() {
    try {
      isDataLoaded = false;
      _stocks = ResourcesHelper.getInstance().getAvailableStocks();
    } catch (NoStocksFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Stock> getStocks() {
    return _stocks;
  }
}

