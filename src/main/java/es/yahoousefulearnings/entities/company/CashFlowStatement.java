package es.yahoousefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.yahoousefulearnings.engine.Field;
import es.yahoousefulearnings.engine.LongFormatField;

/**
 * Generated by: "https://query2.finance.yahoo.com/v10/finance/quoteSummary/AAPL?formatted=true&modules=cashflowStatementHistory"
 * key to find in jackson: cashflowStatements
 *
 * CashFlowStatement will have list of objects will be called CashFlowStatements
 * @author Yago Rodríguez
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashFlowStatement {

  @JsonProperty("endDate")
  private Field endDate; // { Field },
  @JsonProperty("netIncome")
  private LongFormatField netIncome; // { LongFormatField },
  @JsonProperty("depreciation")
  private LongFormatField depreciation; // { LongFormatField },
  @JsonProperty("changeToNetincome")
  private LongFormatField changeToNetincome; // { LongFormatField },
  @JsonProperty("changeToAccountReceivables")
  private LongFormatField changeToAccountReceivables; // { LongFormatField },
  @JsonProperty("changeToLiabilities")
  private LongFormatField changeToLiabilities; // { LongFormatField },
  @JsonProperty("changeToInventory")
  private LongFormatField changeToInventory; // { LongFormatField },
  @JsonProperty("changeToOperatingActivities")
  private LongFormatField changeToOperatingActivities; // { LongFormatField },
  @JsonProperty("totalCashFromOperatingActivities")
  private LongFormatField totalCashFromOperatingActivities; // { LongFormatField },
  @JsonProperty("capitalExpenditures")
  private LongFormatField capitalExpenditures; // { LongFormatField },
  @JsonProperty("investments")
  private LongFormatField investments; // { LongFormatField },
  @JsonProperty("otherCashflowsFromInvestingActivities")
  private LongFormatField otherCashflowsFromInvestingActivities; // { LongFormatField },
  @JsonProperty("totalCashflowsFromInvestingActivities")
  private LongFormatField totalCashflowsFromInvestingActivities; // { LongFormatField },
  @JsonProperty("dividendsPaid")
  private LongFormatField dividendsPaid; // { LongFormatField },
  @JsonProperty("salePurchaseOfStock")
  private LongFormatField salePurchaseOfStock; // { LongFormatField },
  @JsonProperty("netBorrowings")
  private LongFormatField netBorrowings; // { LongFormatField },
  @JsonProperty("otherCashflowsFromFinancingActivities")
  private LongFormatField otherCashflowsFromFinancingActivities; // { LongFormatField },
  @JsonProperty("totalCashFromFinancingActivities")
  private LongFormatField totalCashFromFinancingActivities; // { LongFormatField },
  @JsonProperty("changeInCash")
  private LongFormatField changeInCash; // { LongFormatField }
}
