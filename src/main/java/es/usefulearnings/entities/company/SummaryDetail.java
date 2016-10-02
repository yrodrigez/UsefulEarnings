package es.usefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.YahooField;
import es.usefulearnings.entities.YahooLongFormatField;

import java.io.Serializable;

/**
 * @author Yago on 02/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SummaryDetail extends CompanyData implements Serializable {

  @JsonProperty("previousClose")
  @EntityParameter(name = "Prev-Close", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField previousClose; 
  
  @JsonProperty("open")
  @EntityParameter(name = "Open", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField open; 
  
  @JsonProperty("dayLow")
  @EntityParameter(name = "Day low", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField dayLow; 
  
  @JsonProperty("dayHigh")
  @EntityParameter(name = "Day high", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField dayHigh; 

  @JsonProperty("regularMarketPreviousClose")
  @EntityParameter(name = "Reg-Market Prev-close", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField regularMarketPreviousClose;
 
  @JsonProperty("regularMarketOpen")
  @EntityParameter(name = "Reg-Market Open", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField regularMarketOpen;
 
  @JsonProperty("regularMarketDayLow")
  @EntityParameter(name = "Reg-Market day low", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField regularMarketDayLow;
 
  @JsonProperty("regularMarketDayHigh")
  @EntityParameter(name = "Reg-Market day high", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField regularMarketDayHigh;
 
  @JsonProperty("dividendRate")
  @EntityParameter(name = "Dividend rate", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField dividendRate;
 
  @JsonProperty("dividendYield")
  @EntityParameter(name = "Dividend yield", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField dividendYield;
 
  @JsonProperty("exDividendDate")
  @EntityParameter(name = "Ex-dividend date", parameterType = ParameterType.YAHOO_FIELD_DATE)
  private YahooField exDividendDate;
 
  @JsonProperty("payoutRatio")
  @EntityParameter(name = "Pay out ratio", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField payoutRatio;

  @JsonProperty("beta")
  @EntityParameter(name = "Beta", parameterType = ParameterType.YAHOO_FIELD_NUMERIC, isMaster = true)
  private YahooField beta;
 
  @JsonProperty("trailingPE")
  @EntityParameter(name = "Trailing PE", parameterType = ParameterType.YAHOO_FIELD_NUMERIC, isMaster = true)
  private YahooField trailingPE;
 
  @JsonProperty("forwardPE")
  @EntityParameter(name = "Forward PE", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField forwardPE;
 
  @JsonProperty("volume")
  @EntityParameter(name = "Volume", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField volume;
 
  @JsonProperty("regularMarketVolume")
  @EntityParameter(name = "Reg-market volume", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField regularMarketVolume;
 
  @JsonProperty("averageVolume")
  @EntityParameter(name = "Avg volume", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD, isMaster = true)
  private YahooLongFormatField averageVolume;
 
  @JsonProperty("averageVolume10days")
  @EntityParameter(name = "Avg volume 10 days", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField averageVolume10days;
 
  @JsonProperty("averageDailyVolume10Day")
  @EntityParameter(name = "Avg daily volume 10days", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField averageDailyVolume10Day;
 
  @JsonProperty("bid")
  @EntityParameter(name = "Bid", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField bid;
 
  @JsonProperty("ask")
  @EntityParameter(name = "Ask", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField ask;
 
  @JsonProperty("bidSize")
  @EntityParameter(name = "Bid size", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField bidSize;
 
  @JsonProperty("askSize")
  @EntityParameter(name = "Ask size", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD)
  private YahooLongFormatField askSize;
 
  @JsonProperty("marketCap")
  @EntityParameter(name = "Market cap", parameterType = ParameterType.YAHOO_LONG_FORMAT_FIELD, isMaster = true)
  private YahooLongFormatField marketCap;
 
  @JsonProperty("fiftyTwoWeekLow")
  @EntityParameter(name = "52wk low", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField fiftyTwoWeekLow;
 
  @JsonProperty("fiftyTwoWeekHigh")
  @EntityParameter(name = "52wk high", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField fiftyTwoWeekHigh;
 
  @JsonProperty("priceToSalesTrailing12Months")
  @EntityParameter(name = "Price to sales trailing 12 months", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField priceToSalesTrailing12Months;
 
  @JsonProperty("fiftyDayAverage")
  @EntityParameter(name = "50day average", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField fiftyDayAverage;
 
  @JsonProperty("twoHundredDayAverage")
  @EntityParameter(name = "200day average", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField twoHundredDayAverage;
 
  @JsonProperty("trailingAnnualDividendRate")
  @EntityParameter(name = "Trailing annual dividend rate", parameterType = ParameterType.YAHOO_FIELD_NUMERIC)
  private YahooField trailingAnnualDividendRate;
 
  @JsonProperty("trailingAnnualDividendYield")
  @EntityParameter(name = "Trailing annual dividend yield", parameterType = ParameterType.YAHOO_FIELD_NUMERIC, isMaster = true)
  private YahooField trailingAnnualDividendYield;


  public YahooField getPreviousClose() {
    return previousClose;
  }

  public void setPreviousClose(YahooField previousClose) {
    this.previousClose = previousClose;
  }

  public YahooField getOpen() {
    return open;
  }

  public void setOpen(YahooField open) {
    this.open = open;
  }

  public YahooField getDayLow() {
    return dayLow;
  }

  public void setDayLow(YahooField dayLow) {
    this.dayLow = dayLow;
  }

  public YahooField getDayHigh() {
    return dayHigh;
  }

  public void setDayHigh(YahooField dayHigh) {
    this.dayHigh = dayHigh;
  }

  public YahooField getRegularMarketPreviousClose() {
    return regularMarketPreviousClose;
  }

  public void setRegularMarketPreviousClose(YahooField regularMarketPreviousClose) {
    this.regularMarketPreviousClose = regularMarketPreviousClose;
  }

  public YahooField getRegularMarketOpen() {
    return regularMarketOpen;
  }

  public void setRegularMarketOpen(YahooField regularMarketOpen) {
    this.regularMarketOpen = regularMarketOpen;
  }

  public YahooField getRegularMarketDayLow() {
    return regularMarketDayLow;
  }

  public void setRegularMarketDayLow(YahooField regularMarketDayLow) {
    this.regularMarketDayLow = regularMarketDayLow;
  }

  public YahooField getRegularMarketDayHigh() {
    return regularMarketDayHigh;
  }

  public void setRegularMarketDayHigh(YahooField regularMarketDayHigh) {
    this.regularMarketDayHigh = regularMarketDayHigh;
  }

  public YahooField getDividendRate() {
    return dividendRate;
  }

  public void setDividendRate(YahooField dividendRate) {
    this.dividendRate = dividendRate;
  }

  public YahooField getDividendYield() {
    return dividendYield;
  }

  public void setDividendYield(YahooField dividendYield) {
    this.dividendYield = dividendYield;
  }

  public YahooField getExDividendDate() {
    return exDividendDate;
  }

  public void setExDividendDate(YahooField exDividendDate) {
    this.exDividendDate = exDividendDate;
  }

  public YahooField getPayoutRatio() {
    return payoutRatio;
  }

  public void setPayoutRatio(YahooField payoutRatio) {
    this.payoutRatio = payoutRatio;
  }

  public YahooField getBeta() {
    return beta;
  }

  public void setBeta(YahooField beta) {
    this.beta = beta;
  }

  public YahooField getTrailingPE() {
    return trailingPE;
  }

  public void setTrailingPE(YahooField trailingPE) {
    this.trailingPE = trailingPE;
  }

  public YahooField getForwardPE() {
    return forwardPE;
  }

  public void setForwardPE(YahooField forwardPE) {
    this.forwardPE = forwardPE;
  }

  public YahooLongFormatField getVolume() {
    return volume;
  }

  public void setVolume(YahooLongFormatField volume) {
    this.volume = volume;
  }

  public YahooLongFormatField getRegularMarketVolume() {
    return regularMarketVolume;
  }

  public void setRegularMarketVolume(YahooLongFormatField regularMarketVolume) {
    this.regularMarketVolume = regularMarketVolume;
  }

  public YahooLongFormatField getAverageVolume() {
    return averageVolume;
  }

  public void setAverageVolume(YahooLongFormatField averageVolume) {
    this.averageVolume = averageVolume;
  }

  public YahooLongFormatField getAverageVolume10days() {
    return averageVolume10days;
  }

  public void setAverageVolume10days(YahooLongFormatField averageVolume10days) {
    this.averageVolume10days = averageVolume10days;
  }

  public YahooLongFormatField getAverageDailyVolume10Day() {
    return averageDailyVolume10Day;
  }

  public void setAverageDailyVolume10Day(YahooLongFormatField averageDailyVolume10Day) {
    this.averageDailyVolume10Day = averageDailyVolume10Day;
  }

  public YahooField getBid() {
    return bid;
  }

  public void setBid(YahooField bid) {
    this.bid = bid;
  }

  public YahooField getAsk() {
    return ask;
  }

  public void setAsk(YahooField ask) {
    this.ask = ask;
  }

  public YahooLongFormatField getBidSize() {
    return bidSize;
  }

  public void setBidSize(YahooLongFormatField bidSize) {
    this.bidSize = bidSize;
  }

  public YahooLongFormatField getAskSize() {
    return askSize;
  }

  public void setAskSize(YahooLongFormatField askSize) {
    this.askSize = askSize;
  }

  public YahooLongFormatField getMarketCap() {
    return marketCap;
  }

  public void setMarketCap(YahooLongFormatField marketCap) {
    this.marketCap = marketCap;
  }

  public YahooField getFiftyTwoWeekLow() {
    return fiftyTwoWeekLow;
  }

  public void setFiftyTwoWeekLow(YahooField fiftyTwoWeekLow) {
    this.fiftyTwoWeekLow = fiftyTwoWeekLow;
  }

  public YahooField getFiftyTwoWeekHigh() {
    return fiftyTwoWeekHigh;
  }

  public void setFiftyTwoWeekHigh(YahooField fiftyTwoWeekHigh) {
    this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
  }

  public YahooField getPriceToSalesTrailing12Months() {
    return priceToSalesTrailing12Months;
  }

  public void setPriceToSalesTrailing12Months(YahooField priceToSalesTrailing12Months) {
    this.priceToSalesTrailing12Months = priceToSalesTrailing12Months;
  }

  public YahooField getFiftyDayAverage() {
    return fiftyDayAverage;
  }

  public void setFiftyDayAverage(YahooField fiftyDayAverage) {
    this.fiftyDayAverage = fiftyDayAverage;
  }

  public YahooField getTwoHundredDayAverage() {
    return twoHundredDayAverage;
  }

  public void setTwoHundredDayAverage(YahooField twoHundredDayAverage) {
    this.twoHundredDayAverage = twoHundredDayAverage;
  }

  public YahooField getTrailingAnnualDividendRate() {
    return trailingAnnualDividendRate;
  }

  public void setTrailingAnnualDividendRate(YahooField trailingAnnualDividendRate) {
    this.trailingAnnualDividendRate = trailingAnnualDividendRate;
  }

  public YahooField getTrailingAnnualDividendYield() {
    return trailingAnnualDividendYield;
  }

  public void setTrailingAnnualDividendYield(YahooField trailingAnnualDividendYield) {
    this.trailingAnnualDividendYield = trailingAnnualDividendYield;
  }

  @Override
  public String toString() {
    return "SummaryDetail{" +
      "previousClose=" + previousClose +
      ", open=" + open +
      ", dayLow=" + dayLow +
      ", dayHigh=" + dayHigh +
      ", regularMarketPreviousClose=" + regularMarketPreviousClose +
      ", regularMarketOpen=" + regularMarketOpen +
      ", regularMarketDayLow=" + regularMarketDayLow +
      ", regularMarketDayHigh=" + regularMarketDayHigh +
      ", dividendRate=" + dividendRate +
      ", dividendYield=" + dividendYield +
      ", exDividendDate=" + exDividendDate +
      ", payoutRatio=" + payoutRatio +
      ", beta=" + beta +
      ", trailingPE=" + trailingPE +
      ", forwardPE=" + forwardPE +
      ", volume=" + volume +
      ", regularMarketVolume=" + regularMarketVolume +
      ", averageVolume=" + averageVolume +
      ", averageVolume10days=" + averageVolume10days +
      ", averageDailyVolume10Day=" + averageDailyVolume10Day +
      ", bid=" + bid +
      ", ask=" + ask +
      ", bidSize=" + bidSize +
      ", askSize=" + askSize +
      ", marketCap=" + marketCap +
      ", fiftyTwoWeekLow=" + fiftyTwoWeekLow +
      ", fiftyTwoWeekHigh=" + fiftyTwoWeekHigh +
      ", priceToSalesTrailing12Months=" + priceToSalesTrailing12Months +
      ", fiftyDayAverage=" + fiftyDayAverage +
      ", twoHundredDayAverage=" + twoHundredDayAverage +
      ", trailingAnnualDividendRate=" + trailingAnnualDividendRate +
      ", trailingAnnualDividendYield=" + trailingAnnualDividendYield +
      '}';
  }
}
