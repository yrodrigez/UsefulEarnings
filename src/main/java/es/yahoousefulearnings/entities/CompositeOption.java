package es.yahoousefulearnings.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.yahoousefulearnings.engine.Field;
import es.yahoousefulearnings.engine.LongFormatField;
/**
 *  @author Yago Rodríguez
 */
public class CompositeOption {

  @JsonIgnore
  private OptionType type;

  @JsonIgnore
  private Company company;

  @JsonProperty("contractSymbol")
  private String contractSymbol;
  @JsonProperty("contractSize")
  private String contractSize;
  @JsonProperty("currency")
  private String currency;

  @JsonProperty("inTheMoney")
  private boolean inTheMoney;
  @JsonProperty("percentChange")
  private Field percentChange;
  @JsonProperty("strike")
  private Field strike;
  @JsonProperty("change")
  private Field change;
  @JsonProperty("impliedVolatility")
  private Field impliedVolatility;
  @JsonProperty("ask")
  private Field ask;
  @JsonProperty("bid")
  private Field bid;
  @JsonProperty("lastPrice")
  private Field lastPrice;

  @JsonProperty("volume")
  private LongFormatField volume;
  @JsonProperty("lastTradeDate")
  private LongFormatField lastTradeDate;
  @JsonProperty("expiration")
  private LongFormatField expiration;
  @JsonProperty("openInterest")
  private LongFormatField openInterest;


  public String getContractSymbol() {
    return contractSymbol;
  }

  public void setContractSymbol(String contractSymbol) {
    this.contractSymbol = contractSymbol;
  }

  public String getContractSize() {
    return contractSize;
  }

  public void setContractSize(String contractSize) {
    this.contractSize = contractSize;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public boolean isInTheMoney() {
    return inTheMoney;
  }

  public void setInTheMoney(boolean inTheMoney) {
    this.inTheMoney = inTheMoney;
  }

  public Field getPercentChange() {
    return percentChange;
  }

  public void setPercentChange(Field percentChange) {
    this.percentChange = percentChange;
  }

  public Field getStrike() {
    return strike;
  }

  public void setStrike(Field strike) {
    this.strike = strike;
  }

  public Field getChange() {
    return change;
  }

  public void setChange(Field change) {
    this.change = change;
  }

  public Field getImpliedVolatility() {
    return impliedVolatility;
  }

  public void setImpliedVolatility(Field impliedVolatility) {
    this.impliedVolatility = impliedVolatility;
  }

  public Field getAsk() {
    return ask;
  }

  public void setAsk(Field ask) {
    this.ask = ask;
  }

  public Field getBid() {
    return bid;
  }

  public void setBid(Field bid) {
    this.bid = bid;
  }

  public Field getLastPrice() {
    return lastPrice;
  }

  public void setLastPrice(Field lastPrice) {
    this.lastPrice = lastPrice;
  }

  public LongFormatField getVolume() {
    return volume;
  }

  public void setVolume(LongFormatField volume) {
    this.volume = volume;
  }

  public LongFormatField getLastTradeDate() {
    return lastTradeDate;
  }

  public void setLastTradeDate(LongFormatField lastTradeDate) {
    this.lastTradeDate = lastTradeDate;
  }

  public LongFormatField getExpiration() {
    return expiration;
  }

  public void setExpiration(LongFormatField expiration) {
    this.expiration = expiration;
  }

  public LongFormatField getOpenInterest() {
    return openInterest;
  }

  public void setOpenInterest(LongFormatField openInterest) {
    this.openInterest = openInterest;
  }

  public Company getCompany() { return company; }

  public void setCompany(Company company) { this.company = company; }

  public OptionType getType() {
    return type;
  }

  public void setType(OptionType type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CompositeOption compositeOption = (CompositeOption) o;

    return contractSymbol.equals(compositeOption.contractSymbol);
  }

  @Override
  public String toString() {
    return "\"CompositeOption\":\n{" +
      "\n\ttype: " + type.name() +
      ",\n\tcontractSymbol: '" + contractSymbol + '\'' +
      ",\n\tcontractSize: '" + contractSize + '\'' +
      ",\n\tcurrency: '" + currency + '\'' +
      ",\n\tinTheMoney: " + inTheMoney +
      ",\n\tpercentChange: " + percentChange.toString() +
      ",\n\tstrike:" + strike.toString() +
      ",\n\tchange:" + change.toString() +
      ",\n\timpliedVolatility:" + impliedVolatility.toString() +
      ",\n\task:" + ask.toString() +
      ",\n\tbid:" + bid.toString() +
      ",\n\tlastPrice:" + lastPrice.toString() +
      ",\n\tvolume:" + volume.toString() +
      ",\n\tlastTradeDate:" + lastTradeDate.toString() +
      ",\n\texpiration:" + expiration.toString() +
      ",\n\topenInterest:" + openInterest.toString() +
      "\n}";
  }


}
