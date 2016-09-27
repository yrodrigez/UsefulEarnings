package es.usefulearnings.engine;

import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.engine.plugin.*;
import es.usefulearnings.entities.*;
import es.usefulearnings.utils.EntitiesPackage;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class Core {

  private ArrayList<Plugin> companiesPlugins;

  private List<Stock> mStocks;

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
      for (Plugin<Company> plugin :
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

  public Map<String, Option> getAllOptions(){
    System.err.println("getAllOptions() -> not implemented yet...");
    return new TreeMap<>();
  }


  public Map<String, Company> getCompaniesFromStock(String stockName) throws IllegalArgumentException {
    for (Stock stock : mStocks) {
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

      if (entity instanceof Option) {
        // TODO implement method setOptionsChainData()
      }

      if (entity instanceof OptionChain) {
        // TODO implement method setOptionsChainData()
      }
    }
  }

  private void setCompanyData(Company settedCompany){
    mStocks.stream()
      .filter(
        stock -> stock.getName().equals(settedCompany.getStockName())
      )
      .forEach(
        stock -> stock.getCompanies().replace(settedCompany.getSymbol(), settedCompany)
      );
  }

  public void removeEntities(Collection<Entity> entitiesToRemove) {
    //TODO: implement remove for options & options chains.
    Collection<Company> companies = new ArrayList<>();
    // Collection<Option> options = new ArrayList<>();

    for (Entity e: entitiesToRemove) {
      if(e instanceof Company) {
        companies.add((Company)e);
      }else if (e instanceof Option){
        // options.add((Option)e);
        System.err.println("Remove empty Options not implemented yet");
      }else if(e instanceof OptionChain){
        // optionChains.add((OptionChain)e);
        System.err.println("Remove empty OptionChains not implemented yet");
      }
    }
    removeCompanies(companies);
  }

  public void removeCompanies(Collection<Company> companiesToRemove) {
    companiesToRemove.forEach(company ->
      getCompaniesFromStock(company.getStockName()).remove(company.getSymbol())
    );
  }

  private void removeCompany(Company company) {
    currentCompaniesToFilter.remove(company);
    //getCompaniesFromStock(company.getStockName()).remove(company.getSymbol());
  }


  public void setFromEntitiesPackage(EntitiesPackage entitiesPackage) {
    List<Stock> newStocks = new LinkedList<>();

    for(Company company : entitiesPackage.getCompanies().values()){
      List<Stock> stocks = newStocks.stream().filter(stock -> stock.getName().equals(company.getStockName())).collect(Collectors.toList());
      if(stocks.isEmpty()){
        Map<String, Company> map = new TreeMap<>();
        map.put(company.getSymbol(), company);
        newStocks.add(new Stock(company.getStockName(), map));
      } else {
        stocks.forEach(s-> s.putCompany(company));
      }
    }

    mStocks = newStocks;
  }

  private     Set<Company> currentCompaniesToFilter = null;
  public void applyFilter(Map<Field, RestrictionValue> filter) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    currentCompaniesToFilter = new HashSet<>(getAllCompanies().values());

    for(Company company : getAllCompanies().values()) {
      applyFilter(company, Company.class, company, filter);
    }

    for (Company company: currentCompaniesToFilter) {
      System.err.println("Resulting company: " + company + " exDividend date: " + company.getCalendarEvents().getExDividendDate());
      System.err.println("Current price: " + company.getFinancialData().getCurrentPrice());
    }
  }

  private <E> void applyFilter(
    Company company,
    Class<?> elementType,
    E elementValue,
    Map<Field, RestrictionValue> filter
  ) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    for (Field field : elementType.getDeclaredFields()) {
      if (field.getAnnotation(EntityParameter.class) != null){
        ParameterType parameterType = field.getAnnotation(EntityParameter.class).parameterType();
        for (PropertyDescriptor pd : Introspector.getBeanInfo(elementType).getPropertyDescriptors()) {
          if (pd.getName().equals(field.getName())) {

            switch (parameterType) {
              case INNER_CLASS:
                applyFilter(company, pd.getPropertyType(), (elementValue == null)?null:pd.getReadMethod().invoke(elementValue), filter);
                break;

              case INNER_CLASS_COLLECTION:
                Collection<E> collection = elementValue == null? null: ((Collection<E>) pd.getReadMethod().invoke(elementValue));
                if (collection != null) {
                  for (E innerElement : collection) {
                    applyFilter(company, (Class<?>) ((ParameterizedType) pd.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0], innerElement, filter);
                  }
                } else {
                  applyFilter(company, (Class<?>) ((ParameterizedType) pd.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0], null, filter);
                }
                break;

              case URL:
              case RAW_STRING:
                if(filter.containsKey(field)) {
                  RestrictionValue restrictionValue = filter.get(field);
                  if (elementValue != null) {
                    Object string = pd.getReadMethod().invoke(elementValue);
                    if(! restrictionValue.getValue().equals(string)) {
                      System.out.println("Removing company: " + company.getSymbol() + " because:\n"
                        + restrictionValue.getValue() + " is not " + restrictionValue.getOperator() + " to " + string
                      );
                      removeCompany(company);
                    }
                  } else {
                    removeCompany(company);
                  }
                }
                break;

              case YAHOO_FIELD_NUMERIC:
              case YAHOO_LONG_FORMAT_FIELD:
                if(filter.containsKey(field)) {
                  RestrictionValue restrictionValue = filter.get(field);
                  if (elementValue != null) {
                    if (pd.getReadMethod().invoke(elementValue) != null){
                      double number = ((YahooField) pd.getReadMethod().invoke(elementValue)).getRaw();
                      if (!evaluateNumber(
                        number,
                        restrictionValue.getOperator(),
                        ((double)restrictionValue.getValue())
                      )) {
                        System.out.println("Removing company: " + company.getSymbol() + " because:\n"
                          + (restrictionValue.getValue()) + " is not " + restrictionValue.getOperator() + " to " + number
                        );
                        removeCompany(company);
                      }
                    } else {
                      removeCompany(company);
                    }
                  } else {
                    removeCompany(company);
                  }
                }
                break;

              case RAW_NUMERIC:
                if(filter.containsKey(field)) {
                  if (elementValue != null) {
                    RestrictionValue restrictionValue = filter.get(field);
                    if (pd.getReadMethod().invoke(elementValue)!=null) {
                      double number = ((double) pd.getReadMethod().invoke(elementValue));
                      if (!evaluateNumber(
                        number,
                        restrictionValue.getOperator(),
                        (double) restrictionValue.getValue()
                      )) {
                        System.out.println("Removing company: " + company.getSymbol() + " because:\n"
                          + restrictionValue.getValue() + " is not " + restrictionValue.getOperator() + " to " + number
                        );
                        removeCompany(company);
                      }
                    } else {
                      removeCompany(company);
                    }
                  } else {
                    removeCompany(company);
                  }
                }
                break;


              case YAHOO_FIELD_DATE:
              case YAHOO_FIELD_DATE_COLLECTION:
                if(filter.containsKey(field)){
                  if (elementValue != null) {
                    RestrictionValue restrictionValue = filter.get(field);
                    if(parameterType.equals(ParameterType.YAHOO_FIELD_DATE_COLLECTION)){
                      Collection<YahooField> yahooFieldCollection = (Collection<YahooField>) pd.getReadMethod().invoke(elementValue);
                      if (yahooFieldCollection != null) {
                        boolean remove = true;
                        for (YahooField yahooField : yahooFieldCollection) {
                          remove = remove && !evaluateNumber(yahooField.getRaw(), restrictionValue.getOperator(), new Double((long)restrictionValue.getValue()));
                        }
                        if (remove) {
                          removeCompany(company);
                        }
                      } else {
                        removeCompany(company);
                      }
                    } else if(parameterType.equals(ParameterType.YAHOO_FIELD_DATE)){
                      if((pd.getReadMethod().invoke(elementValue)) != null) {
                        if (!evaluateNumber(((YahooField) pd.getReadMethod().invoke(elementValue)).getRaw(), restrictionValue.getOperator(), new Double((long) restrictionValue.getValue()))) {
                          removeCompany(company);
                        }
                      } else {
                        removeCompany(company);
                      }
                    }
                  } else {
                    removeCompany(company);
                  }
                }
                break;

              case IGNORE:
                break;

              default: throw new IllegalArgumentException("Wrong Argument -> " + parameterType.name());
            }
            break;
          }
        }
      }
    }
  }

  private boolean evaluateNumber(double number, BasicOperator operator , double toEval){
    switch (operator){
      case EQ:
        return number == toEval;
      case LT:
        return number < toEval;
      case GT:
        return number > toEval;

      default: return false;
    }
  }
}

