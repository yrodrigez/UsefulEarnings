package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.CalendarEvents;

import java.io.IOException;
import java.net.URL;

/**
 * @author Yago
 */
public class CalendarEventsPlugin extends YahooFinanceAPIPlugin {
  @Override
  protected String getValueToSearch() {
    return "calendarEvents";
  }

  @Override
  protected String getModuleName() {
    return YahooLinks.COMPANY_CALENDAR_EVENTS;
  }

  @Override
  protected void processJsonNode(Company company, JsonNode node) throws IOException {
    CalendarEvents calendarEvents = mapper.readValue(node.traverse(), CalendarEvents.class);
    company.setCalendarEvents(calendarEvents);
  }
}
