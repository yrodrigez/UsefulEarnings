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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class HistoricalDataPlugin implements Plugin<Company> {

  private ArrayList<HistoricalData> _historicalDatas;
  private URL _Url;
  private ObjectMapper _mapper;
  private Date _startDate;
  private Date _endDate;

  public HistoricalDataPlugin(long startTimeStamp, long endTimeStamp) {
    _mapper = new ObjectMapper();

    _startDate = Date.from(Instant.ofEpochSecond(startTimeStamp));
    _endDate = Date.from(Instant.ofEpochSecond(endTimeStamp));
  }

  @Override
  public void addInfo(Company company) throws PluginException {
    try {
      _Url = YahooFinanceAPI
          .getInstance()
          .getHistoricalDataURL(
              company.getSymbol(),
              new SimpleDateFormat("yyyy-MM-dd").format(_startDate),
              new SimpleDateFormat("yyyy-MM-dd").format(_endDate)
          );


      JsonNode root = JSONHTTPClient.getInstance().getJSON(_Url);
      JsonNode quote = root.findValue("quote");
      _historicalDatas = _mapper.readValue(
          quote.traverse(),
          new TypeReference<ArrayList<HistoricalData>>() {}
      );
      company.setHistoricalDatas(_historicalDatas);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (Exception anyException) {
      throw new PluginException(company.getSymbol(), this.getClass().getName(), anyException, _Url);
    }
  }
}
