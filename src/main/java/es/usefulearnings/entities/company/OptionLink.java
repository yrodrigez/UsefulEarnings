package es.usefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;
import es.usefulearnings.entities.company.allowedvalues.ContractSizeAllowedValues;
import es.usefulearnings.entities.company.allowedvalues.OptionCurrencyAllowedValues;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionLink implements Serializable {

  @JsonProperty("contractSymbol")
  @EntityParameter(name = "Contract Symbol", parameterType = ParameterType.RAW_STRING)
  private String symbol;

  @JsonProperty("strike")
  @EntityParameter(name = "Strike", parameterType = ParameterType.RAW_NUMERIC)
  private double strike;

  @JsonProperty("currency")
  @EntityParameter(name = "Currency", parameterType = ParameterType.RAW_STRING, allowedValues = OptionCurrencyAllowedValues.class)
  private String currency;

  @JsonProperty("lastPrice")
  @EntityParameter(name = "Last price", parameterType = ParameterType.RAW_NUMERIC)
  private double lastPrice;

  @JsonProperty("change")
  @EntityParameter(name = "Change", parameterType = ParameterType.RAW_NUMERIC)
  private double change;

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
  @EntityParameter(name = "Contract size", parameterType = ParameterType.RAW_STRING, allowedValues = ContractSizeAllowedValues.class)
  private String contractSize;

  @JsonProperty("expiration")
  @EntityParameter(name = "Expiration date", parameterType = ParameterType.RAW_DATE)
  private long expiration;

  @JsonProperty("lastTradeDate")
  @EntityParameter(name = "Last trade date", parameterType = ParameterType.RAW_DATE)
  private long lastTradeDate;

  @JsonProperty("impliedVolatility")
  @EntityParameter(name = "Implied volatility", parameterType = ParameterType.RAW_NUMERIC)
  private double impliedVolatility;

  public OptionLink() {
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
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

  @Override
  public String toString() {
    return "OptionLink{" +
      "symbol='" + symbol + '\'' +
      ", strike=" + strike +
      ", currency='" + currency + '\'' +
      ", lastPrice=" + lastPrice +
      ", change=" + change +
      ", volume=" + volume +
      ", openInterest=" + openInterest +
      ", bid=" + bid +
      ", ask=" + ask +
      ", contractSize='" + contractSize + '\'' +
      ", expiration=" + expiration +
      ", lastTradeDate=" + lastTradeDate +
      ", impliedVolatility=" + impliedVolatility +
      '}';
  }
}
