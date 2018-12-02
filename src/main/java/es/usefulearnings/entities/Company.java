package es.usefulearnings.entities;


import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.engine.Observable;
import es.usefulearnings.engine.Observer;
import es.usefulearnings.entities.company.*;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Representation of a company in this application
 *
 * @author Yago Rodríguez
 */

public class Company implements Serializable, Entity, Savable, Observable<Company> {
  public static final String EXTENSION = ".cpy";

  @EntityParameter(name = "Symbol", parameterType = ParameterType.IGNORE, isMaster = true)
  private String symbol;

  @EntityParameter(name = "Market", parameterType = ParameterType.IGNORE, isMaster = true)
  private String stockName;

  @EntityParameter(name = "Summary detail", parameterType = ParameterType.INNER_CLASS, isMaster = true)
  private SummaryDetail summaryDetail;
  @EntityParameter(name = "Profile", parameterType = ParameterType.INNER_CLASS, isMaster = true)
  private Profile profile;
  @EntityParameter(name = "Calendar events", parameterType = ParameterType.INNER_CLASS, isMaster = true)
  private CalendarEvents calendarEvents;
  @EntityParameter(name = "Financial data", parameterType = ParameterType.INNER_CLASS, isMaster = true)
  private FinancialData financialData;
  @EntityParameter(name = "Default key statistics", parameterType = ParameterType.INNER_CLASS, isMaster = true)
  private DefaultKeyStatistics defaultKeyStatistics;

  private final List<Observer<Company>> observers;
/*
  @EntityParameter(name = "Cashflow statements", parameterType = ParameterType.INNER_CLASS_COLLECTION)
  private ArrayList<CashFlowStatement> cashFlowStatements;
  @EntityParameter(name = "Income statements", parameterType = ParameterType.INNER_CLASS_COLLECTION)
  private ArrayList<IncomeStatement> incomeStatements;
  @EntityParameter(name = "Balance sheet statement", parameterType = ParameterType.INNER_CLASS_COLLECTION)
  private ArrayList<BalanceSheetStatement> balanceSheetStatements;
*/
  @EntityParameter(name = "Historical Data", parameterType = ParameterType.HISTORICAL_DATA)
  private HistoricalData historicalData;

  @EntityParameter(name = "Option Chain", parameterType = ParameterType.INNER_CLASS)
  private OptionChain optionChain;

  public Company(final String symbol, final String stockName, final Observer<Company> ... observers) {
    this.symbol = symbol;
    this.stockName = stockName;

    profile = new Profile();
    summaryDetail = new SummaryDetail();
    calendarEvents = new CalendarEvents();
    financialData = new FinancialData();
    defaultKeyStatistics = new DefaultKeyStatistics();
    optionChain = new OptionChain();
    historicalData = new HistoricalData();

    this.observers = new LinkedList<>(Arrays.asList(observers));

    /*
    cashFlowStatements = new ArrayList<>();
    cashFlowStatements.add(new CashFlowStatement());

    incomeStatements = new ArrayList<>();
    incomeStatements.add(new IncomeStatement());

    balanceSheetStatements = new ArrayList<>();
    balanceSheetStatements.add(new BalanceSheetStatement());
    */
  }

  public String getSymbol() {
    return symbol;
  }


  public void setSymbol(final String symbol) {
    this.symbol = symbol;
  }


  public CalendarEvents getCalendarEvents() {
    return calendarEvents;
  }

  public void setCalendarEvents(final CalendarEvents calendarEvents) {
    this.calendarEvents = calendarEvents;
    this.calendarEvents.set();
  }

  public FinancialData getFinancialData() {
    return financialData;
  }

  public void setFinancialData(final FinancialData financialData) {
    this.financialData = financialData;
    this.financialData.set();
  }

  public DefaultKeyStatistics getDefaultKeyStatistics() {
    return defaultKeyStatistics;
  }

  public void setDefaultKeyStatistics(final DefaultKeyStatistics defaultKeyStatistics) {
    this.defaultKeyStatistics = defaultKeyStatistics;
    this.defaultKeyStatistics.set();
  }
/*
  public ArrayList<CashFlowStatement> getCashFlowStatements() {
    return cashFlowStatements;
  }

  public void setCashFlowStatements(ArrayList<CashFlowStatement> cashFlowStatements) {
    this.cashFlowStatements = cashFlowStatements;
    this.calendarEvents.set();
  }

  public ArrayList<IncomeStatement> getIncomeStatements() {
    return incomeStatements;
  }

  public void setIncomeStatements(ArrayList<IncomeStatement> incomeStatements) {
    this.incomeStatements = incomeStatements;
    this.incomeStatements.forEach(CompanyData::set);
  }

  public ArrayList<BalanceSheetStatement> getBalanceSheetStatements() {
    return balanceSheetStatements;
  }

  public void setBalanceSheetStatements(ArrayList<BalanceSheetStatement> balanceSheetStatements) {
    this.balanceSheetStatements = balanceSheetStatements;
    this.balanceSheetStatements.forEach(CompanyData::set);
  }
*/
  public void setSummaryDetail(final SummaryDetail summaryDetail) {
    if(summaryDetail != null)
      this.summaryDetail.set();
    this.summaryDetail = summaryDetail;
  }

  public SummaryDetail getSummaryDetail() {
    return summaryDetail;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
    this.profile.set();
  }

  @Override
  public String toString() {
    return this.symbol;
  }

  public HistoricalData getHistoricalData() {
    return historicalData;
  }

  public OptionChain getOptionChain() {

    return optionChain;
  }

  public void setOptionChain(OptionChain optionChain) {
    if (optionChain.getCalls().size() > 0) {
      optionChain.getCalls().forEach(optionLink -> {
        if (!optionLink.getSymbol().substring(0, this.symbol.replace("^", "").length()).equals(this.symbol.replace("^", "")))
          throw new IllegalArgumentException("Contract " + optionLink.getSymbol() + " does not belong to " + this.symbol);
      });

    }

    if (optionChain.getPuts().size() > 0) {
      optionChain.getPuts().forEach(optionLink -> {
        if (!optionLink.getSymbol().substring(0, this.symbol.replace("^", "").length()).equals(this.symbol.replace("^", "")))
          throw new IllegalArgumentException("Contract " + optionLink.getSymbol() + " does not belong to " + this.symbol);
      });
    }

    this.optionChain = optionChain;
    if (this.optionChain.getCalls().size() > 0 || this.optionChain.getPuts().size() > 0) {
      this.optionChain.set();
    }
  }

  public void setHistoricalData(HistoricalData historicalData) {
    this.historicalData = historicalData;
    if (this.historicalData.getHistoricalDatum().size() > 0)
      this.historicalData.set();
  }

  public String getStockName() {
    return this.stockName;
  }

  @Override
  public boolean isEmpty() {
    return !(
      profile.isSet()
      || calendarEvents.isSet()
      || financialData.isSet()
      || defaultKeyStatistics.isSet()
      || optionChain.isSet()
    );
  }

  @Override
  public void flush() {

  }

  @Override
  public void restore() {

  }

  @Override
  public void save(final File fileToSave) throws IOException {
    final String location = fileToSave.getAbsolutePath()
      + File.separator
      + this.stockName + this.symbol
      + EXTENSION;

    final FileOutputStream data = new FileOutputStream(location);
    ObjectOutputStream stream = new ObjectOutputStream(data);
    stream.writeObject(this);
    stream.close();
    data.close();
  }


  @Override
  public void onChange() {
    this.observers.forEach(o -> o.onChange(this));
  }

  @Override
  public void addListeners(final Observer<Company>... observer) {
    this.observers.addAll(Arrays.asList(observer));
  }
}
