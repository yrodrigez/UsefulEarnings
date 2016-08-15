package es.yahoousefulearnings.entities;


import es.yahoousefulearnings.engine.Field;
import es.yahoousefulearnings.entities.company.*;

import java.util.ArrayList;

/**
 * Representation of a company in this application
 *
 * @author Yago Rodríguez
 */

public class Company {

  private String symbol;

  private Profile profile;
  private CalendarEvents calendarEvents;
  private FinancialData financialData;
  private DefaultKeyStatistics defaultKeyStatistics;

  private ArrayList<CashFlowStatement> cashFlowStatements;
  private ArrayList<IncomeStatement> incomeStatements;
  private ArrayList<BalanceSheetStatement> balanceSheetStatements;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public ArrayList<Field> getEarningsDates() {
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

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
    this.profile.set();
  }
}
