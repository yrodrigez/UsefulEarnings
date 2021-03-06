package es.usefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HistoricalData extends CompanyData implements Serializable {

  public class Historical implements Serializable {
    @EntityParameter(name = "Symbol", parameterType = ParameterType.RAW_STRING)
    private String symbol;

    @EntityParameter(name = "Dates", parameterType = ParameterType.UNIX_TIME_STAMP)
    private long date;

    @EntityParameter(name = "Open", parameterType = ParameterType.RAW_DOUBLE)
    private double open;

    @EntityParameter(name = "High", parameterType = ParameterType.RAW_DOUBLE)
    private double high;

    @EntityParameter(name = "Low", parameterType = ParameterType.RAW_DOUBLE)
    private double low;

    @EntityParameter(name = "Close", parameterType = ParameterType.RAW_DOUBLE)
    private double close;

    @EntityParameter(name = "Volume", parameterType = ParameterType.RAW_LONG)
    private long volume;

    @EntityParameter(name = "Adj Close", parameterType = ParameterType.RAW_DOUBLE)
    private double adj_close;

    @EntityParameter(name = "Average(Open, high, low, close)", parameterType = ParameterType.RAW_DOUBLE)
    private double average;

    Historical(
      final String symbol,
      final long date,
      final double open,
      final double high,
      final double low,
      final double close,
      final long volume,
      final double adj_close
    ) {
      this.symbol = symbol;
      this.date = date;
      this.open = open;
      this.high = high;
      this.low = low;
      this.close = close;
      this.volume = volume;
      this.adj_close = adj_close;

      this.average = (open+high+low+close) / 4;
    }


    @Override
    public String toString() {
      return "Historical{" +
        "symbol='" + symbol + '\'' +
        ", date='" + date + '\'' +
        ", open=" + open +
        ", high=" + high +
        ", low=" + low +
        ", close=" + close +
        ", volume=" + volume +
        ", adj_close=" + adj_close +
        '}';
    }

    public String getSymbol() {
      return symbol;
    }

    public long getDate() {
      return date;
    }

    public Double getOpen() {
      return open;
    }

    public Double getHigh() {
      return high;
    }

    public Double getLow() {
      return low;
    }

    public Double getClose() {
      return close;
    }

    public Long getVolume() {
      return volume;
    }

    public Double getAdj_close() {
      return adj_close;
    }

    public double getAverage() {
      return average;
    }
  }

  private ArrayList<Historical> historicalDatum;

  @JsonProperty("Symbol")
  @EntityParameter(name = "Symbol", parameterType = ParameterType.RAW_STRING)
  private String symbol;

  @JsonProperty("timestamp")
  @EntityParameter(name = "Dates", parameterType = ParameterType.RAW_DATE_COLLECTION)
  private ArrayList<Long> date;

  @JsonProperty("open")
  @EntityParameter(name = "Open", parameterType = ParameterType.NUMBER_COLLECTION)
  private ArrayList<Double> open;

  @JsonProperty("high")
  @EntityParameter(name = "High", parameterType = ParameterType.NUMBER_COLLECTION)
  private ArrayList<Double> high;

  @JsonProperty("low")
  @EntityParameter(name = "Low", parameterType = ParameterType.NUMBER_COLLECTION)
  private ArrayList<Double> low;

  @JsonProperty("close")
  @EntityParameter(name = "Close", parameterType = ParameterType.NUMBER_COLLECTION)
  private ArrayList<Double> close;

  @JsonProperty("volume")
  @EntityParameter(name = "Volume", parameterType = ParameterType.NUMBER_COLLECTION)
  private ArrayList<Long> volume;

  @JsonProperty("unadjclose")
  @EntityParameter(name = "Adj Close", parameterType = ParameterType.NUMBER_COLLECTION)
  private ArrayList<Double> adj_close;

  public HistoricalData(){
    symbol = "";
    date = new ArrayList<>();
    open = new ArrayList<>();
    high = new ArrayList<>();
    low = new ArrayList<>();
    close = new ArrayList<>();
    volume = new ArrayList<>();
    adj_close = new ArrayList<>();
  }

  public HistoricalData(
    String symbol,
    ArrayList<Long> date,
    ArrayList<Double> open,
    ArrayList<Double> high,
    ArrayList<Double> low,
    ArrayList<Double> close,
    ArrayList<Long> volume,
    ArrayList<Double> adj_close
  ) {
    this.symbol = symbol;
    this.date = date;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
    this.adj_close = adj_close;

    historicalDatum = new ArrayList<>();

    for(int i = 0; i< date.size() ; i++){
      historicalDatum.add(new Historical(
        symbol,
        date.get(i),
        open.get(i),
        high.get(i),
        low.get(i),
        close.get(i),
        volume.get(i),
        adj_close.get(i)
      ));
    }
  }

  public String getSymbol() {
    return symbol;
  }


  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public ArrayList<Long> getDate() {
    return date;
  }

  public void setDate(ArrayList<Long> date) {
    this.date = date;
  }

  public ArrayList<Double> getOpen() {
    return open;
  }

  public void setOpen(ArrayList<Double> open) {
    this.open = open;
  }

  public ArrayList<Double> getHigh() {
    return high;
  }

  public void setHigh(ArrayList<Double> high) {
    this.high = high;
  }

  public ArrayList<Double> getLow() {
    return low;
  }

  public void setLow(ArrayList<Double> low) {
    this.low = low;
  }

  public ArrayList<Double> getClose() {
    return close;
  }

  public void setClose(ArrayList<Double> close) {
    this.close = close;
  }

  public ArrayList<Long> getVolume() {
    return volume;
  }

  public void setVolume(ArrayList<Long> volume) {
    this.volume = volume;
  }

  public ArrayList<Double> getAdj_close() {
    return adj_close;
  }

  public void setAdj_close(ArrayList<Double> adj_close) {
    this.adj_close = adj_close;
  }

  public ArrayList<Historical> getHistoricalDatum() {
    return historicalDatum;
  }

  @Override
  public String toString() {
    return "HistoricalData{" +
        "symbol='" + symbol + '\'' +
        ", date='" + date + '\n' +
        ", open='" + open + '\n' +
        ", high='" + high + '\n' +
        ", low='" + low + '\n' +
        ", close='" + close + '\n' +
        ", volume='" + volume + '\n' +
        ", adj_close='" + adj_close + '\'' +
        '}';
  }

  public boolean isEmpty(){
    return historicalDatum == null || historicalDatum.isEmpty();
  }
}
