package es.yahoousefulearnings.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.yahoousefulearnings.entities.option.OptionType;

/**
 *
 *  @author Yago Rodríguez
 */
public class CompositeOption {

  @JsonIgnore
  public OptionType type;
  @JsonProperty("contractSymbol")
  private String contractSymbol;
  @JsonProperty("strike")
  private double strike;
  @JsonProperty("currency")
  private String currency;
  @JsonProperty("lastPrice")
  private double lastPrice;
  @JsonProperty("change")
  private double change;
  @JsonProperty("percentChange")
  private double percentChange;
  @JsonProperty("volume")
  private double volume;
  @JsonProperty("openInterest")
  private double openInterest;
  @JsonProperty("bid")
  private double bid;
  @JsonProperty("ask")
  private double ask;
  @JsonProperty("contractSize")
  private String contractSize;
  @JsonProperty("expiration")
  private long expiration;
  @JsonProperty("lastTradeDate")
  private long lastTradeDate;
  @JsonProperty("impliedVolatility")
  private double impliedVolatility;
  @JsonProperty("inTheMoney")
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
    return "CompositeOption{" +
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
