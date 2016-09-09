package es.yahoousefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.yahoousefulearnings.annotation.Entity;
import es.yahoousefulearnings.annotation.UEField;
import es.yahoousefulearnings.entities.Field;
import es.yahoousefulearnings.entities.LongFormatField;

/**
 * Generated by: "https://query2.finance.yahoo.com/v10/finance/quoteSummary/AAPL?formatted=true&modules=incomeStatementHistory"
 * key to find in jackson: incomeStatementHistory
 * incomeStatementHistory will have list of objects will be called IncomeStatements
 *
 * @author Yago Rodríguez
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity(name = "Income Statement")
public class IncomeStatement extends CompanyData {

  @JsonProperty("endDate")
  @UEField(fieldName = "End Date")
  private Field endDate;
  @JsonProperty("totalRevenue")
  @UEField(fieldName = "Total Revenue")
  private Field totalRevenue;

  @JsonProperty("costOfRevenue")
  @UEField(fieldName = "Cost of Revenue")
  private LongFormatField costOfRevenue;
  @JsonProperty("grossProfit")
  private LongFormatField grossProfit;
  @JsonProperty("researchDevelopment")
  private LongFormatField researchDevelopment;
  @JsonProperty("sellingGeneralAdministrative")
  private LongFormatField sellingGeneralAdministrative;
  @JsonProperty("operatingIncome")
  private LongFormatField operatingIncome;
  @JsonProperty("totalOtherIncomeExpenseNet")
  private LongFormatField totalOtherIncomeExpenseNet;
  @JsonProperty("ebit")
  private LongFormatField ebit;
  @JsonProperty("incomeBeforeTax")
  private LongFormatField incomeBeforeTax;
  @JsonProperty("incomeTaxExpense")
  private LongFormatField incomeTaxExpense;
  @JsonProperty("netIncome")
  private LongFormatField netIncome;
  @JsonProperty("netIncomeApplicableToCommonShares")
  private LongFormatField netIncomeApplicableToCommonShares;
  @JsonProperty("netIncomeFromContinuingOps")
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

  @JsonProperty("discontinuedOperations") private Field discontinuedOperations;
  @JsonProperty("extraordinaryItems") private Field extraordinaryItems;
  @JsonProperty("effectOfAccountingCharges") private Field effectOfAccountingCharges;
  @JsonProperty("otherItems") private Field otherItems;
  @JsonProperty("nonRecurring") private Field nonRecurring;
  @JsonProperty("otherOperatingExpenses") private Field otherOperatingExpenses;
  @JsonProperty("totalOperatingExpenses") private Field totalOperatingExpenses;
  @JsonProperty("interestExpense") private Field interestExpense;
  @JsonProperty("minorityInterest") private Field minorityInterest;
  */

}
