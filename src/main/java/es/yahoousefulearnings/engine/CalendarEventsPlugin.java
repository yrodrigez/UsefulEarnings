package es.yahoousefulearnings.engine;

import es.yahoousefulearnings.entities.Company;
import es.yahoousefulearnings.entities.company.CalendarEvents;

/**
 *
 * Created by yago on 7/09/16.
 */
public class CalendarEventsPlugin implements Plugin {

  private CalendarEvents calendarEvents;
  private String mModule = YahooLinks.COMPANY_CALENDAR_EVENTS;


  @Override
  public void addInfo(Company company) {

    company.setCalendarEvents(calendarEvents);
  }
}
