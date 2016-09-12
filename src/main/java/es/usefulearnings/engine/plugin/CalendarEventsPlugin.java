package es.usefulearnings.engine.plugin;

import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.CalendarEvents;

/**
 *
 * Created by yago on 7/09/16.
 */
public class CalendarEventsPlugin implements Plugin {

  private CalendarEvents calendarEvents;
  private String mModule = YahooLinks.COMPANY_CALENDAR_EVENTS;

  @Override
  public void addInfo(Company company) {
    //hacer peticion HTTP para obtener el JSON y parsearlo para
    //obtener la informacion de calendar events
    //la peticion http debe hacerse a través de un cliente HTTP que
    //sea un objeto que mantenga una cache para no repetir peticiones
    //en esa cache (map<URL,Object>) mantendrá el contenido ya descargado
    //de las URLs que se han pedido con anterioridad.

    /* class JSONHTTPClient  {

      //implementar singleton

        private Map<URL, Object> cache = ...

        public Object getJSON(URL url) {
          if (!cache.containsKey(url)) {
            Object jsonObject = getJsonFromJackson(url); //metodo privado que hace eso
            cache.put(url, jsonObject);
          }
          return cache.get(url);
        }

        public void clearCache() {
          cache.clear();
        }
     */

    //la llamada seria
    // JSONHTTPClient.getInstance().getJSON(yahoolink);
    company.setCalendarEvents(calendarEvents);
  }
}
