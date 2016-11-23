package es.usefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HistoricalData extends CompanyData implements Serializable {

  @JsonProperty("Symbol")
  @EntityParameter(name = "Symbol", parameterType = ParameterType.RAW_STRING)
  private String symbol;

  @JsonProperty("Date")
  @EntityParameter(name = "Date", parameterType = ParameterType.RAW_STRING)
  private String date;

  @JsonProperty("Open")
  @EntityParameter(name = "Open", parameterType = ParameterType.RAW_STRING)
  private String open;

  @JsonProperty("High")
  @EntityParameter(name = "High", parameterType = ParameterType.RAW_STRING)
  private String high;

  @JsonProperty("Low")
  @EntityParameter(name = "Low", parameterType = ParameterType.RAW_STRING)
  private String low;

  @JsonProperty("Close")
  @EntityParameter(name = "Close", parameterType = ParameterType.RAW_STRING)
  private String close;

  @JsonProperty("Volume")
  @EntityParameter(name = "Volume", parameterType = ParameterType.RAW_STRING)
  private String volume;

  @JsonProperty("Adj_Close")
  @EntityParameter(name = "Adj Close", parameterType = ParameterType.RAW_STRING)
  private String adj_close;


  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getOpen() {
    return open;
  }

  public void setOpen(String open) {
    this.open = open;
  }

  public String getHigh() {
    return high;
  }

  public void setHigh(String high) {
    this.high = high;
  }

  public String getLow() {
    return low;
  }

  public void setLow(String low) {
    this.low = low;
  }

  public String getClose() {
    return close;
  }

  public void setClose(String close) {
    this.close = close;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getAdj_close() {
    return adj_close;
  }

  public void setAdj_close(String adj_close) {
    this.adj_close = adj_close;
  }

  @Override
  public String toString() {
    return "HistoricalData{" +
        "symbol='" + symbol + '\'' +
        ", date='" + date + '\'' +
        ", open='" + open + '\'' +
        ", high='" + high + '\'' +
        ", low='" + low + '\'' +
        ", close='" + close + '\'' +
        ", volume='" + volume + '\'' +
        ", adj_close='" + adj_close + '\'' +
        '}';
  }
}
