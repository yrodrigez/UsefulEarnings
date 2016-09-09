package es.yahoousefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.yahoousefulearnings.annotation.Entity;
import es.yahoousefulearnings.entities.Field;
import es.yahoousefulearnings.entities.LongFormatField;

import java.util.ArrayList;

/**
 * Generated by: "https://query2.finance.yahoo.com/v10/finance/quoteSummary/AAPL?formatted=true&modules=calendarEvents"
 * key to find in jackson: earnings
 * *** this class is part of CaledarEvents ***
 *
 * @author Yago Rodríguez
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity(name = "Earnings")
public class Earnings extends CompanyData {
  @JsonProperty("earningsDate")
  private ArrayList<Field> earningsDate;

  @JsonProperty("earningsAverage")
  private Field earningsAverage;
  @JsonProperty("earningsLow")
  private Field earningsLow;
  @JsonProperty("earningsHigh")
  private Field earningsHigh;

  @JsonProperty("revenueAverage")
  private LongFormatField revenueAverage;
  @JsonProperty("revenueLow")
  private LongFormatField revenueLow;
  @JsonProperty("revenueHigh")
  private LongFormatField revenueHigh;

  public ArrayList<Field> getEarningsDate() {
    return earningsDate;
  }

  public void setEarningsDate(ArrayList<Field> earningsDate) {
    this.earningsDate = earningsDate;
  }

  public Field getEarningsAverage() {
    return earningsAverage;
  }

  public void setEarningsAverage(Field earningsAverage) {
    this.earningsAverage = earningsAverage;
  }

  public Field getEarningsLow() {
    return earningsLow;
  }

  public void setEarningsLow(Field earningsLow) {
    this.earningsLow = earningsLow;
  }

  public Field getEarningsHigh() {
    return earningsHigh;
  }

  public void setEarningsHigh(Field earningsHigh) {
    this.earningsHigh = earningsHigh;
  }

  public LongFormatField getRevenueAverage() {
    return revenueAverage;
  }

  public void setRevenueAverage(LongFormatField revenueAverage) {
    this.revenueAverage = revenueAverage;
  }

  public LongFormatField getRevenueLow() {
    return revenueLow;
  }

  public void setRevenueLow(LongFormatField revenueLow) {
    this.revenueLow = revenueLow;
  }

  public LongFormatField getRevenueHigh() {
    return revenueHigh;
  }

  public void setRevenueHigh(LongFormatField revenueHigh) {
    this.revenueHigh = revenueHigh;
  }
}
