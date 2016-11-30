package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.ObjectMapper;
import es.usefulearnings.engine.connection.YahooFinanceAPI;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.HistoricalData;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HistoricalDataPlugin implements Plugin<Company> {

  private URL _Url;
  private ObjectMapper _mapper;
  private long _startDate;
  private long _endDate;
  private YahooFinanceAPI.Range _range;

  public HistoricalDataPlugin(long startTimeStamp, long endTimeStamp, YahooFinanceAPI.Range range) {
    _mapper = new ObjectMapper();

    _startDate = startTimeStamp;
    _endDate = endTimeStamp;
    _range = range;
  }

  @Override
  public void addInfo(Company company) throws PluginException {
    try {
      _Url = YahooFinanceAPI
        .getInstance()
        .getHistoricalDataURL(
          company.getSymbol(),
          _startDate,
          _endDate,
          _range
        );


      JsonNode root = JSONHTTPClient.getInstance().getJSON(_Url);

      JsonNode timestamp = root.findValue("timestamp");
      ArrayList<Long> dates = _mapper.readValue(
        timestamp.traverse(),
        new TypeReference<ArrayList<Long>>() {
        }
      );

      JsonNode opens = root.findValue("open");
      ArrayList<Double> open = _mapper.readValue(
        opens.traverse(),
        new TypeReference<ArrayList<Double>>() {
        }
      );

      JsonNode highs = root.findValue("high");
      ArrayList<Double> high = _mapper.readValue(
        highs.traverse(),
        new TypeReference<ArrayList<Double>>() {
        }
      );

      _mapper = new ObjectMapper();
      JsonNode lows = root.findValue("low");
      ArrayList<Double> low = _mapper.readValue(
        lows.traverse(),
        new TypeReference<ArrayList<Double>>() {
        }
      );

      _mapper = new ObjectMapper();
      JsonNode closes = root.findValue("close");
      ArrayList<Double> close = _mapper.readValue(
        closes.traverse(),
        new TypeReference<ArrayList<Double>>() {
        }
      );

      _mapper = new ObjectMapper();
      JsonNode volumes = root.findValue("volume");
      ArrayList<Double> volume = _mapper.readValue(
        volumes.traverse(),
        new TypeReference<ArrayList<Double>>() {
        }
      );

      _mapper = new ObjectMapper();
      JsonNode unadjclose = root.findValue("unadjclose");
      unadjclose = unadjclose.findValue("unadjclose");
      ArrayList<Double> adjClose = _mapper.readValue(
        unadjclose.traverse(),
        new TypeReference<ArrayList<Double>>() {
        }
      );

      if(dates.isEmpty() && open.isEmpty() && high.isEmpty() && low.isEmpty() && close.isEmpty() && volume.isEmpty() && adjClose.isEmpty())
        throw new PluginException(
          company.getSymbol(),
          this.getClass().getName(),
          new IllegalArgumentException("data is empty for " + company.getSymbol()),
          _Url
        );
      HistoricalData data = new HistoricalData(
        company.getSymbol(),
        dates,
        open,
        high,
        low,
        close,
        volume,
        adjClose
      );
      company.setHistoricalData(data);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (Exception anyException) {
      throw new PluginException(company.getSymbol(), this.getClass().getName(), anyException, _Url);
    }
  }
}
