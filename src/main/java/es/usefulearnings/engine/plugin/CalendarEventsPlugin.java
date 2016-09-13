package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.CalendarEvents;
import es.usefulearnings.utils.Json;

import java.net.URL;

/**
 * Created by yago on 7/09/16.
 */
public class CalendarEventsPlugin implements Plugin {
  private CalendarEvents mCalendarEvents;
  private URL mUrl;
  private ObjectMapper mapper;

  private String mCompanySymbol;
  private String mModule = YahooLinks.COMPANY_CALENDAR_EVENTS;

  public CalendarEventsPlugin() {
    mapper = new ObjectMapper();
  }

  public String getCompanySymbol() {
    return mCompanySymbol;
  }


  @Override
  public void addInfo(Company company) {
    try {
      mCompanySymbol = company.getSymbol();
      mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, mModule);

      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      JsonNode calendarEventsNode = Json.removeEmptyClasses(root.findValue(mModule));
      mCalendarEvents = mapper.readValue(calendarEventsNode.traverse(), CalendarEvents.class);

      company.setCalendarEvents(mCalendarEvents);

    } catch (Exception ne) {
      System.err.println("Something Happened trying to set CalendarEvents data of " + mCompanySymbol);
      System.err.println(ne.getMessage());
      // TODO something with this exception!!
      //ne.printStackTrace();
    }


  }
}
