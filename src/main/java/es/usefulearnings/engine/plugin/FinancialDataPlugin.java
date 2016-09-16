package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.FinancialData;
import es.usefulearnings.utils.Json;

import java.net.URL;

/**
 * ${PATH}
 * Created by yago on 12/09/16.
 */
public class FinancialDataPlugin <E>implements Plugin<E> {
  private FinancialData mFinancialData;
  private URL mUrl;
  private ObjectMapper mapper;

  private String mCompanySymbol;
  private String mModule = YahooLinks.COMPANY_FINANCIAL_DATA;

  public FinancialDataPlugin() {
    mapper = new ObjectMapper();

  }

  public String getCompanySymbol() {
    return mCompanySymbol;
  }


  @Override
  public void addInfo(E entity) {
    try {
      if(!entity.getClass().equals(Company.class)) throw new IllegalArgumentException("This is not a company");

      mCompanySymbol = ((Company)entity).getSymbol();
      mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, mModule);

      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      JsonNode calendarEventsNode = Json.removeEmptyClasses(root.findValue(mModule));
      mFinancialData = mapper.readValue(calendarEventsNode.traverse(), FinancialData.class);

      ((Company)entity).setFinancialData(mFinancialData);
    } catch (Exception ne) {
      System.err.println("Something Happened trying to set FinancialData of " + mCompanySymbol);
      System.err.println("URL: " + mUrl);
      System.err.println("Yahoo URL: " + "http://finance.yahoo.com/quote/" + mCompanySymbol);
      System.err.println(ne.getMessage());
    }


  }
}
