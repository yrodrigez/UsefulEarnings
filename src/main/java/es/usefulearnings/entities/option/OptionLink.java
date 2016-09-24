package es.usefulearnings.entities.option;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.annotation.EntityParameter;

/**
 *
 *  This is a part of a Option Chain
 *  @author Yago Rodríguez
 */
public class OptionLink {

  @JsonIgnore
  @EntityParameter(name = "Type", parameterType = ParameterType.OPTION_TYPE)
  public OptionType type;

  @JsonProperty("contractSymbol")
  @EntityParameter(name = "Symbol", parameterType = ParameterType.RAW_STRING)
  private String contractSymbol;

  @JsonProperty("strike")
  @EntityParameter(name = "Strike", parameterType = ParameterType.RAW_NUMERIC)
  private double strike;

  @JsonProperty("currency")
  @EntityParameter(name = "Currency", parameterType = ParameterType.RAW_STRING)
  private String currency;

  @JsonProperty("lastPrice")
  @EntityParameter(name = "Last price", parameterType = ParameterType.RAW_NUMERIC)
  private double lastPrice;

  @JsonProperty("change")
  @EntityParameter(name = "Change", parameterType = ParameterType.RAW_NUMERIC)
  private double change;

  @JsonProperty("percentChange")
  @EntityParameter(name = "% Change", parameterType = ParameterType.RAW_NUMERIC)
  private double percentChange;

  @JsonProperty("volume")
  @EntityParameter(name = "Volume", parameterType = ParameterType.RAW_NUMERIC)
  private double volume;

  @JsonProperty("openInterest")
  @EntityParameter(name = "Open interest", parameterType = ParameterType.RAW_NUMERIC)
  private double openInterest;

  @JsonProperty("bid")
  @EntityParameter(name = "Bid", parameterType = ParameterType.RAW_NUMERIC)
  private double bid;

  @JsonProperty("ask")
  @EntityParameter(name = "Ask", parameterType = ParameterType.RAW_NUMERIC)
  private double ask;

  @JsonProperty("contractSize")
  @EntityParameter(name = "Size", parameterType = ParameterType.RAW_STRING)
  private String contractSize;

  @JsonProperty("expiration")
  @EntityParameter(name = "Expiration", parameterType = ParameterType.RAW_DATE)
  private long expiration;

  @JsonProperty("lastTradeDate")
  @EntityParameter(name = "Last trade", parameterType = ParameterType.RAW_DATE)
  private long lastTradeDate;

  @JsonProperty("impliedVolatility")
  @EntityParameter(name = "Implied volatility", parameterType = ParameterType.RAW_NUMERIC)
  private double impliedVolatility;

  @JsonProperty("inTheMoney")
  @EntityParameter(name = "In the money", parameterType = ParameterType.RAW_BOOLEAN) // this is not important maybe?
  private boolean inTheMoney;

  public OptionType getType() {
    return type;
  }

  public void setType(OptionType type) {
    this.type = type;
  }

  public String getContractSymbol() {
    return contractSymbol;
  }

  public void setContractSymbol(String contractSymbol) {
    this.contractSymbol = contractSymbol;
  }

  public double getStrike() {
    return strike;
  }

  public void setStrike(double strike) {
    this.strike = strike;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public double getLastPrice() {
    return lastPrice;
  }

  public void setLastPrice(double lastPrice) {
    this.lastPrice = lastPrice;
  }

  public double getChange() {
    return change;
  }

  public void setChange(double change) {
    this.change = change;
  }

  public double getPercentChange() {
    return percentChange;
  }

  public void setPercentChange(double percentChange) {
    this.percentChange = percentChange;
  }

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    this.volume = volume;
  }

  public double getOpenInterest() {
    return openInterest;
  }

  public void setOpenInterest(double openInterest) {
    this.openInterest = openInterest;
  }

  public double getBid() {
    return bid;
  }

  public void setBid(double bid) {
    this.bid = bid;
  }

  public double getAsk() {
    return ask;
  }

  public void setAsk(double ask) {
    this.ask = ask;
  }

  public String getContractSize() {
    return contractSize;
  }

  public void setContractSize(String contractSize) {
    this.contractSize = contractSize;
  }

  public long getExpiration() {
    return expiration;
  }

  public void setExpiration(long expiration) {
    this.expiration = expiration;
  }

  public long getLastTradeDate() {
    return lastTradeDate;
  }

  public void setLastTradeDate(long lastTradeDate) {
    this.lastTradeDate = lastTradeDate;
  }

  public double getImpliedVolatility() {
    return impliedVolatility;
  }

  public void setImpliedVolatility(double impliedVolatility) {
    this.impliedVolatility = impliedVolatility;
  }

  public boolean isInTheMoney() {
    return inTheMoney;
  }

  public void setInTheMoney(boolean inTheMoney) {
    this.inTheMoney = inTheMoney;
  }

  @Override
  public String toString() {
    return "OptionLink{" +
      "type=" + type +
      ", contractSymbol='" + contractSymbol + '\'' +
      ", strike=" + strike +
      ", currency='" + currency + '\'' +
      ", lastPrice=" + lastPrice +
      ", change=" + change +
      ", percentChange=" + percentChange +
      ", volume=" + volume +
      ", openInterest=" + openInterest +
      ", bid=" + bid +
      ", ask=" + ask +
      ", contractSize='" + contractSize + '\'' +
      ", expiration=" + expiration +
      ", lastTradeDate=" + lastTradeDate +
      ", impliedVolatility=" + impliedVolatility +
      ", inTheMoney=" + inTheMoney +
      '}';
  }
}
