package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import es.usefulearnings.engine.connection.YahooFinanceAPI;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.CalendarEvents;

import java.io.IOException;

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
    return YahooFinanceAPI.COMPANY_CALENDAR_EVENTS;
  }

  @Override
  protected void processJsonNode(Company company, JsonNode node) throws IOException {
    CalendarEvents calendarEvents = mapper.readValue(node.traverse(), CalendarEvents.class);
    company.setCalendarEvents(calendarEvents);
  }
}
