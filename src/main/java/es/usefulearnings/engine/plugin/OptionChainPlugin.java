package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.YahooFinanceAPI;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.OptionChain;
import es.usefulearnings.entities.company.OptionLink;

import java.net.URL;
import java.util.ArrayList;


public class OptionChainPlugin implements Plugin<Company> {
  private URL mUrl;
  private ObjectMapper mapper;
  private OptionChain optionChain;


  public OptionChainPlugin() {
    optionChain = new OptionChain();
    mapper = new ObjectMapper();
  }

  @Override
  public void addInfo(Company company) throws PluginException {
    try {

      mUrl = YahooFinanceAPI.getInstance().getOptionChainURL(company.getSymbol());

      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);

      JsonNode expirationDatesNode = root.findValue("expirationDates");
      ArrayList<Long> expirationDates = mapper.readValue(
        expirationDatesNode.traverse(),
        new TypeReference<ArrayList<Long>>() {
        }
      );
      optionChain.setExpirationDates(expirationDates);

      JsonNode callsNode = root.findValue("calls");
      ArrayList<OptionLink> calls = mapper.readValue(
        callsNode.traverse(),
        new TypeReference<ArrayList<OptionLink>>() {
        }
      );
      optionChain.setCalls(calls);

      JsonNode putsNode = root.findValue("puts");
      ArrayList<OptionLink> puts = mapper.readValue(
        putsNode.traverse(),
        new TypeReference<ArrayList<OptionLink>>() {
        }
      );
      optionChain.setPuts(puts);
      company.setOptionChain(optionChain);

    } catch (Exception anyException) {
      throw new PluginException(company.getSymbol(), this.getClass().getName(), anyException, mUrl);
    }
  }
}
