package es.usefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.entities.YahooLongFormatField;
import es.usefulearnings.entities.YahooField;

/**
 * Generated by: "https://query2.finance.yahoo.com/v10/finance/quoteSummary/AAPL?formatted=true&modules=incomeStatementHistory"
 * key to find in jackson: incomeStatementHistory
 * incomeStatementHistory will have list of objects will be called IncomeStatements
 *
 * @author Yago Rodríguez
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
 public class IncomeStatement extends CompanyData {

  @JsonProperty("endDate")
  @EntityParameter(name = "End Date", parameterType = ParameterType.YAHOO_FIELD_DATE)
  private YahooField endDate;

  @JsonProperty("totalRevenue")
  @EntityParameter(name = "Total Revenue", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField totalRevenue;

  @JsonProperty("costOfRevenue")
  @EntityParameter(name = "Cost of Revenue", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooLongFormatField costOfRevenue;

  @JsonProperty("grossProfit")
  @EntityParameter(name = "Gross Profit", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooLongFormatField grossProfit;

  @JsonProperty("researchDevelopment")
  @EntityParameter(name = "Research Development", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField researchDevelopment;

  @JsonProperty("sellingGeneralAdministrative")
  @EntityParameter(name = "Selling General Administrative", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField sellingGeneralAdministrative;

  @JsonProperty("operatingIncome")
  @EntityParameter(name = "Operating Income", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField operatingIncome;

  @JsonProperty("totalOtherIncomeExpenseNet")
  @EntityParameter(name = "Total Other Income Expense Net", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField totalOtherIncomeExpenseNet;

  @JsonProperty("ebit")
  @EntityParameter(name = "e bit", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField ebit;

  @JsonProperty("incomeBeforeTax")
  @EntityParameter(name = "Income before Tax", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField incomeBeforeTax;

  @JsonProperty("incomeTaxExpense")
  @EntityParameter(name = "Income Tax Expense", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField incomeTaxExpense;

  @JsonProperty("netIncome")
  @EntityParameter(name = "Net income", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField netIncome;

  @JsonProperty("netIncomeApplicableToCommonShares")
  @EntityParameter(name = "Net income applicable to common shares", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField netIncomeApplicableToCommonShares;

  @JsonProperty("netIncomeFromContinuingOps")
  @EntityParameter(name = "Net Income From Continuing Ops", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField netIncomeFromContinuingOps;


  public YahooField getEndDate() {
    return endDate;
  }

  public void setEndDate(YahooField endDate) {
    this.endDate = endDate;
  }

  public YahooField getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(YahooField totalRevenue) {
    this.totalRevenue = totalRevenue;
  }

  public YahooLongFormatField getCostOfRevenue() {
    return costOfRevenue;
  }

  public void setCostOfRevenue(YahooLongFormatField costOfRevenue) {
    this.costOfRevenue = costOfRevenue;
  }

  public YahooLongFormatField getGrossProfit() {
    return grossProfit;
  }

  public void setGrossProfit(YahooLongFormatField grossProfit) {
    this.grossProfit = grossProfit;
  }

  public YahooLongFormatField getResearchDevelopment() {
    return researchDevelopment;
  }

  public void setResearchDevelopment(YahooLongFormatField researchDevelopment) {
    this.researchDevelopment = researchDevelopment;
  }

  public YahooLongFormatField getSellingGeneralAdministrative() {
    return sellingGeneralAdministrative;
  }

  public void setSellingGeneralAdministrative(YahooLongFormatField sellingGeneralAdministrative) {
    this.sellingGeneralAdministrative = sellingGeneralAdministrative;
  }

  public YahooLongFormatField getOperatingIncome() {
    return operatingIncome;
  }

  public void setOperatingIncome(YahooLongFormatField operatingIncome) {
    this.operatingIncome = operatingIncome;
  }

  public YahooLongFormatField getTotalOtherIncomeExpenseNet() {
    return totalOtherIncomeExpenseNet;
  }

  public void setTotalOtherIncomeExpenseNet(YahooLongFormatField totalOtherIncomeExpenseNet) {
    this.totalOtherIncomeExpenseNet = totalOtherIncomeExpenseNet;
  }

  public YahooLongFormatField getEbit() {
    return ebit;
  }

  public void setEbit(YahooLongFormatField ebit) {
    this.ebit = ebit;
  }

  public YahooLongFormatField getIncomeBeforeTax() {
    return incomeBeforeTax;
  }

  public void setIncomeBeforeTax(YahooLongFormatField incomeBeforeTax) {
    this.incomeBeforeTax = incomeBeforeTax;
  }

  public YahooLongFormatField getIncomeTaxExpense() {
    return incomeTaxExpense;
  }

  public void setIncomeTaxExpense(YahooLongFormatField incomeTaxExpense) {
    this.incomeTaxExpense = incomeTaxExpense;
  }

  public YahooLongFormatField getNetIncome() {
    return netIncome;
  }

  public void setNetIncome(YahooLongFormatField netIncome) {
    this.netIncome = netIncome;
  }

  public YahooLongFormatField getNetIncomeApplicableToCommonShares() {
    return netIncomeApplicableToCommonShares;
  }

  public void setNetIncomeApplicableToCommonShares(YahooLongFormatField netIncomeApplicableToCommonShares) {
    this.netIncomeApplicableToCommonShares = netIncomeApplicableToCommonShares;
  }

  public YahooLongFormatField getNetIncomeFromContinuingOps() {
    return netIncomeFromContinuingOps;
  }

  public void setNetIncomeFromContinuingOps(YahooLongFormatField netIncomeFromContinuingOps) {
    this.netIncomeFromContinuingOps = netIncomeFromContinuingOps;
  }

  /*
  *** NULL DATA ***

  @JsonProperty("discontinuedOperations") private EntityParameter discontinuedOperations;
  @JsonProperty("extraordinaryItems") private EntityParameter extraordinaryItems;
  @JsonProperty("effectOfAccountingCharges") private EntityParameter effectOfAccountingCharges;
  @JsonProperty("otherItems") private EntityParameter otherItems;
  @JsonProperty("nonRecurring") private EntityParameter nonRecurring;
  @JsonProperty("otherOperatingExpenses") private EntityParameter otherOperatingExpenses;
  @JsonProperty("totalOperatingExpenses") private EntityParameter totalOperatingExpenses;
  @JsonProperty("interestExpense") private EntityParameter interestExpense;
  @JsonProperty("minorityInterest") private EntityParameter minorityInterest;
  */

}
