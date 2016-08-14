package es.yahoousefulearnings.entities;


import es.yahoousefulearnings.engine.Field;
import es.yahoousefulearnings.entities.company.*;

import java.util.ArrayList;

/**
 * Representation of a company in this application
 * @author Yago Rodríguez
 */

public class Company {

  private String symbol;

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

  public ArrayList<Field> getEarningsDates(){
    return this.calendarEvents.getEarningsDate();
  }

  public CalendarEvents getCalendarEvents() {
    return calendarEvents;
  }

  public void setCalendarEvents(CalendarEvents calendarEvents) {
    this.calendarEvents = calendarEvents;
  }

  public FinancialData getFinancialData() {
    return financialData;
  }

  public void setFinancialData(FinancialData financialData) {
    this.financialData = financialData;
  }

  public DefaultKeyStatistics getDefaultKeyStatistics() {
    return defaultKeyStatistics;
  }

  public void setDefaultKeyStatistics(DefaultKeyStatistics defaultKeyStatistics) {
    this.defaultKeyStatistics = defaultKeyStatistics;
  }

  public ArrayList<CashFlowStatement> getCashFlowStatements() {
    return cashFlowStatements;
  }

  public void setCashFlowStatements(ArrayList<CashFlowStatement> cashFlowStatements) {
    this.cashFlowStatements = cashFlowStatements;
  }

  public ArrayList<IncomeStatement> getIncomeStatements() {
    return incomeStatements;
  }

  public void setIncomeStatements(ArrayList<IncomeStatement> incomeStatements) {
    this.incomeStatements = incomeStatements;
  }

  public ArrayList<BalanceSheetStatement> getBalanceSheetStatements() {
    return balanceSheetStatements;
  }

  public void setBalanceSheetStatements(ArrayList<BalanceSheetStatement> balanceSheetStatements) {
    this.balanceSheetStatements = balanceSheetStatements;
  }
}
