package es.usefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.annotation.FieldType;
import es.usefulearnings.annotation.ObservableField;
import es.usefulearnings.entities.Field;
import es.usefulearnings.entities.LongFormatField;

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
  @ObservableField(name = "End Date", fieldType = FieldType.DATE)
  private Field endDate;

  @JsonProperty("totalRevenue")
  @ObservableField(name = "Total Revenue", fieldType = FieldType.NUMERIC)
  private Field totalRevenue;

  @JsonProperty("costOfRevenue")
  @ObservableField(name = "Cost of Revenue", fieldType = FieldType.NUMERIC)
  private LongFormatField costOfRevenue;

  @JsonProperty("grossProfit")
  @ObservableField(name = "Gross Profit", fieldType = FieldType.NUMERIC)
  private LongFormatField grossProfit;

  @JsonProperty("researchDevelopment")
  @ObservableField(name = "Research Development", fieldType = FieldType.NUMERIC)
  private LongFormatField researchDevelopment;

  @JsonProperty("sellingGeneralAdministrative")
  @ObservableField(name = "Selling General Administrative", fieldType = FieldType.NUMERIC)
  private LongFormatField sellingGeneralAdministrative;

  @JsonProperty("operatingIncome")
  @ObservableField(name = "Operating Income", fieldType = FieldType.NUMERIC)
  private LongFormatField operatingIncome;

  @JsonProperty("totalOtherIncomeExpenseNet")
  @ObservableField(name = "Total Other Income Expense Net", fieldType = FieldType.NUMERIC)
  private LongFormatField totalOtherIncomeExpenseNet;

  @JsonProperty("ebit")
  @ObservableField(name = "e bit", fieldType = FieldType.NUMERIC)
  private LongFormatField ebit;

  @JsonProperty("incomeBeforeTax")
  @ObservableField(name = "Income before Tax", fieldType = FieldType.NUMERIC)
  private LongFormatField incomeBeforeTax;

  @JsonProperty("incomeTaxExpense")
  @ObservableField(name = "Income Tax Expense", fieldType = FieldType.NUMERIC)
  private LongFormatField incomeTaxExpense;

  @JsonProperty("netIncome")
  @ObservableField(name = "Net income", fieldType = FieldType.NUMERIC)
  private LongFormatField netIncome;

  @JsonProperty("netIncomeApplicableToCommonShares")
  @ObservableField(name = "Net income applicable to common shares")
  private LongFormatField netIncomeApplicableToCommonShares;

  @JsonProperty("netIncomeFromContinuingOps")
  @ObservableField(name = "Net Income From Continuing Ops", fieldType = FieldType.NUMERIC)
  private LongFormatField netIncomeFromContinuingOps;


  public Field getEndDate() {
    return endDate;
  }

  public void setEndDate(Field endDate) {
    this.endDate = endDate;
  }

  public Field getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(Field totalRevenue) {
    this.totalRevenue = totalRevenue;
  }

  public LongFormatField getCostOfRevenue() {
    return costOfRevenue;
  }

  public void setCostOfRevenue(LongFormatField costOfRevenue) {
    this.costOfRevenue = costOfRevenue;
  }

  public LongFormatField getGrossProfit() {
    return grossProfit;
  }

  public void setGrossProfit(LongFormatField grossProfit) {
    this.grossProfit = grossProfit;
  }

  public LongFormatField getResearchDevelopment() {
    return researchDevelopment;
  }

  public void setResearchDevelopment(LongFormatField researchDevelopment) {
    this.researchDevelopment = researchDevelopment;
  }

  public LongFormatField getSellingGeneralAdministrative() {
    return sellingGeneralAdministrative;
  }

  public void setSellingGeneralAdministrative(LongFormatField sellingGeneralAdministrative) {
    this.sellingGeneralAdministrative = sellingGeneralAdministrative;
  }

  public LongFormatField getOperatingIncome() {
    return operatingIncome;
  }

  public void setOperatingIncome(LongFormatField operatingIncome) {
    this.operatingIncome = operatingIncome;
  }

  public LongFormatField getTotalOtherIncomeExpenseNet() {
    return totalOtherIncomeExpenseNet;
  }

  public void setTotalOtherIncomeExpenseNet(LongFormatField totalOtherIncomeExpenseNet) {
    this.totalOtherIncomeExpenseNet = totalOtherIncomeExpenseNet;
  }

  public LongFormatField getEbit() {
    return ebit;
  }

  public void setEbit(LongFormatField ebit) {
    this.ebit = ebit;
  }

  public LongFormatField getIncomeBeforeTax() {
    return incomeBeforeTax;
  }

  public void setIncomeBeforeTax(LongFormatField incomeBeforeTax) {
    this.incomeBeforeTax = incomeBeforeTax;
  }

  public LongFormatField getIncomeTaxExpense() {
    return incomeTaxExpense;
  }

  public void setIncomeTaxExpense(LongFormatField incomeTaxExpense) {
    this.incomeTaxExpense = incomeTaxExpense;
  }

  public LongFormatField getNetIncome() {
    return netIncome;
  }

  public void setNetIncome(LongFormatField netIncome) {
    this.netIncome = netIncome;
  }

  public LongFormatField getNetIncomeApplicableToCommonShares() {
    return netIncomeApplicableToCommonShares;
  }

  public void setNetIncomeApplicableToCommonShares(LongFormatField netIncomeApplicableToCommonShares) {
    this.netIncomeApplicableToCommonShares = netIncomeApplicableToCommonShares;
  }

  public LongFormatField getNetIncomeFromContinuingOps() {
    return netIncomeFromContinuingOps;
  }

  public void setNetIncomeFromContinuingOps(LongFormatField netIncomeFromContinuingOps) {
    this.netIncomeFromContinuingOps = netIncomeFromContinuingOps;
  }

  /*
  *** NULL DATA ***

  @JsonProperty("discontinuedOperations") private ObservableField discontinuedOperations;
  @JsonProperty("extraordinaryItems") private ObservableField extraordinaryItems;
  @JsonProperty("effectOfAccountingCharges") private ObservableField effectOfAccountingCharges;
  @JsonProperty("otherItems") private ObservableField otherItems;
  @JsonProperty("nonRecurring") private ObservableField nonRecurring;
  @JsonProperty("otherOperatingExpenses") private ObservableField otherOperatingExpenses;
  @JsonProperty("totalOperatingExpenses") private ObservableField totalOperatingExpenses;
  @JsonProperty("interestExpense") private ObservableField interestExpense;
  @JsonProperty("minorityInterest") private ObservableField minorityInterest;
  */

}
