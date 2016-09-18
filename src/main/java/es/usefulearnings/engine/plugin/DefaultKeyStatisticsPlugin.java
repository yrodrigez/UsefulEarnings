package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.DefaultKeyStatistics;
import es.usefulearnings.utils.Json;

import java.net.URL;

/**
 * ${PATH}
 * Created by yago on 12/09/16.
 */
public class DefaultKeyStatisticsPlugin implements Plugin<Company> {

  private DefaultKeyStatistics mDefaultKeyStatistics;
  private URL mUrl;
  private ObjectMapper mapper;

  private String mCompanySymbol;
  private String mModule = YahooLinks.COMPANY_DEFAULT_KEY_STATISTICS;

  public DefaultKeyStatisticsPlugin() {
    mapper = new ObjectMapper();
  }

  public String getCompanySymbol() {
    return mCompanySymbol;
  }


  @Override
  public void addInfo(Company company) throws PluginException {
    try {
      mCompanySymbol = company.getSymbol();
      mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, mModule);

      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      JsonNode calendarEventsNode = Json.removeEmptyClasses(root.findValue(mModule));
      mDefaultKeyStatistics = mapper.readValue(calendarEventsNode.traverse(), DefaultKeyStatistics.class);

      company.setDefaultKeyStatistics(mDefaultKeyStatistics);
    } catch (Exception anyException) {
      throw new PluginException(company.getSymbol(), this.getClass().getName(), anyException);
    }
  }
}
