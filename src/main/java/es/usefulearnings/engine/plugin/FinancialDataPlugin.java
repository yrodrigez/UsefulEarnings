package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.CalendarEvents;
import es.usefulearnings.entities.company.FinancialData;
import es.usefulearnings.utils.Json;

import java.io.IOException;
import java.net.URL;

/**
 * ${PATH}
 * Created by yago on 12/09/16.
 */
public class FinancialDataPlugin implements Plugin {
  private FinancialData mFinancialData;
  private URL mUrl;
  private ObjectMapper mapper;

  private String mCompanySymbol;
  private String mModule = YahooLinks.COMPANY_FINANCIAL_DATA;

  public FinancialDataPlugin(String companySymbol) {
    mCompanySymbol = companySymbol;
    mapper = new ObjectMapper();
    mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, mModule);
  }

  public String getCompanySymbol() {
    return mCompanySymbol;
  }

  public void setCompanySymbol(String mCompanySymbol) {
    this.mCompanySymbol = mCompanySymbol;
  }

  @Override
  public void addInfo(Company company) {
    try {
      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      JsonNode calendarEventsNode = Json.removeEmptyClasses(root.findValue(mModule));

      mFinancialData = mapper.readValue(calendarEventsNode.traverse(), FinancialData.class);
    } catch (IOException ne) {
      System.err.println("Something Happened trying to set Profile data of " + mCompanySymbol);
      System.err.println(ne.getMessage());
      // TODO something with this exception!!
    }

    company.setFinancialData(mFinancialData);
  }
}
