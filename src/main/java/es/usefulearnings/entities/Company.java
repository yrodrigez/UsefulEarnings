package es.usefulearnings.entities;


import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.company.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Representation of a company in this application
 *
 * @author Yago Rodríguez
 */

public class Company implements Serializable, Entity, Savable {
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

  @EntityParameter(name = "Cashflow statements", parameterType = ParameterType.INNER_CLASS_COLLECTION)
  private ArrayList<CashFlowStatement> cashFlowStatements;
  @EntityParameter(name = "Income statements", parameterType = ParameterType.INNER_CLASS_COLLECTION)
  private ArrayList<IncomeStatement> incomeStatements;
  @EntityParameter(name = "Balance sheet statement" , parameterType = ParameterType.INNER_CLASS_COLLECTION)
  private ArrayList<BalanceSheetStatement> balanceSheetStatements;

  @EntityParameter(name = "Historical Data", parameterType = ParameterType.INNER_CLASS_COLLECTION)
  private ArrayList<HistoricalData> historicalDatas;

  public Company(String symbol, String stockName){
    this.symbol = symbol;
    this.stockName = stockName;

    profile = new Profile();
    summaryDetail = new SummaryDetail();
    calendarEvents = new CalendarEvents();
    financialData = new FinancialData();
    defaultKeyStatistics = new DefaultKeyStatistics();

    cashFlowStatements = new ArrayList<>();
    cashFlowStatements.add(new CashFlowStatement());

    incomeStatements = new ArrayList<>();
    incomeStatements.add(new IncomeStatement());

    balanceSheetStatements = new ArrayList<>();
    balanceSheetStatements.add(new BalanceSheetStatement());

    historicalDatas = new ArrayList<>();
    historicalDatas.add(new HistoricalData());
  }



  public String getSymbol() {
    return symbol;
  }


  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public ArrayList<YahooField> getEarningsDates() {
    return this.calendarEvents.getEarningsDate();
  }

  public CalendarEvents getCalendarEvents() {
    return calendarEvents;
  }

  public void setCalendarEvents(CalendarEvents calendarEvents) {
    this.calendarEvents = calendarEvents;
    this.calendarEvents.set();
  }

  public FinancialData getFinancialData() {
    return financialData;
  }

  public void setFinancialData(FinancialData financialData) {
    this.financialData = financialData;
    this.financialData.set();
  }

  public DefaultKeyStatistics getDefaultKeyStatistics() {
    return defaultKeyStatistics;
  }

  public void setDefaultKeyStatistics(DefaultKeyStatistics defaultKeyStatistics) {
    this.defaultKeyStatistics = defaultKeyStatistics;
    this.defaultKeyStatistics.set();
  }

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
    this.incomeStatements.forEach(incomeStatement -> incomeStatement.set());
  }

  public ArrayList<BalanceSheetStatement> getBalanceSheetStatements() {
    return balanceSheetStatements;
  }

  public void setBalanceSheetStatements(ArrayList<BalanceSheetStatement> balanceSheetStatements) {
    this.balanceSheetStatements = balanceSheetStatements;
    this.balanceSheetStatements.forEach(balanceSheetStatement -> balanceSheetStatement.set());
  }

  public void setSummaryDetail(SummaryDetail summaryDetail) {
    this.summaryDetail = summaryDetail;
    this.summaryDetail.set();
  }

  public SummaryDetail getSummaryDetail(){
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
  public String toString(){
    return this.symbol;
  }

  public ArrayList<HistoricalData> getHistoricalDatas() {
    return historicalDatas;
  }

  public void setHistoricalDatas(ArrayList<HistoricalData> historicalDatas) {
    this.historicalDatas = historicalDatas;
  }

  public String getStockName() {
    return this.stockName;
  }

  @Override
  public boolean isEmpty() {
    return !(profile.isSet() || calendarEvents.isSet() || financialData.isSet() || defaultKeyStatistics.isSet()
      || cashFlowStatements.isEmpty() || incomeStatements.isEmpty());
  }

  @Override
  public void save(File fileToSave) throws IOException {
    String location = fileToSave.getAbsolutePath()
                      + File.separator
                      + this.stockName + this.symbol
                      + EXTENSION;

    FileOutputStream data = new FileOutputStream(location);
    ObjectOutputStream stream = new ObjectOutputStream(data);
    stream.writeObject(this);
    stream.close();
    data.close();
  }
}
