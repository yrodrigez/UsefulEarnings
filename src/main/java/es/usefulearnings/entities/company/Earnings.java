package es.usefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.YahooLongFormatField;
import es.usefulearnings.entities.YahooField;

import java.io.Serializable;
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
public class Earnings extends CompanyData implements Serializable {

  @JsonProperty("earningsDate")
  @EntityParameter(name = "Earnings date", parameterType = ParameterType.YAHOO_FIELD_DATE_COLLECTION, isMaster = true)
  private ArrayList<YahooField> earningsDate;

  @JsonProperty("earningsAverage")
  @EntityParameter(name = "Earnings average", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField earningsAverage;

  @JsonProperty("earningsLow")
  @EntityParameter(name = "Earnings low", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField earningsLow;

  @JsonProperty("earningsHigh")
  @EntityParameter(name = "Earnings high", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField earningsHigh;

  @JsonProperty("revenueAverage")
  @EntityParameter(name = "Revenue average", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooLongFormatField revenueAverage;

  @JsonProperty("revenueLow")
  @EntityParameter(name = "Revenue low", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooLongFormatField revenueLow;

  @JsonProperty("revenueHigh")
  @EntityParameter(name = "Revenue high", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooLongFormatField revenueHigh;

  public ArrayList<YahooField> getEarningsDate() {
    return earningsDate;
  }

  public void setEarningsDate(ArrayList<YahooField> earningsDate) {
    this.earningsDate = earningsDate;
  }

  public YahooField getEarningsAverage() {
    return earningsAverage;
  }

  public void setEarningsAverage(YahooField earningsAverage) {
    this.earningsAverage = earningsAverage;
  }

  public YahooField getEarningsLow() {
    return earningsLow;
  }

  public void setEarningsLow(YahooField earningsLow) {
    this.earningsLow = earningsLow;
  }

  public YahooField getEarningsHigh() {
    return earningsHigh;
  }

  public void setEarningsHigh(YahooField earningsHigh) {
    this.earningsHigh = earningsHigh;
  }

  public YahooLongFormatField getRevenueAverage() {
    return revenueAverage;
  }

  public void setRevenueAverage(YahooLongFormatField revenueAverage) {
    this.revenueAverage = revenueAverage;
  }

  public YahooLongFormatField getRevenueLow() {
    return revenueLow;
  }

  public void setRevenueLow(YahooLongFormatField revenueLow) {
    this.revenueLow = revenueLow;
  }

  public YahooLongFormatField getRevenueHigh() {
    return revenueHigh;
  }

  public void setRevenueHigh(YahooLongFormatField revenueHigh) {
    this.revenueHigh = revenueHigh;
  }
}
