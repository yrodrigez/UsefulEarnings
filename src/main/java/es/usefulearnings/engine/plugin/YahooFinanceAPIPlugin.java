package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.ObjectMapper;
import es.usefulearnings.entities.Company;

import java.io.IOException;
import java.net.URL;

/**
 * @author yago.
 */
abstract class YahooFinanceAPIPlugin implements Plugin<Company> {

  protected ObjectMapper mapper ;

  private String mCompanySymbol;

  YahooFinanceAPIPlugin() {
    mapper = new ObjectMapper();
  }

  public String getCompanySymbol() {
    return mCompanySymbol;
  }


  @Override
  public void addInfo(Company company) throws PluginException {
    URL mUrl = null;
    try {
      mCompanySymbol = company.getSymbol();
      mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, getModuleName());

      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      if(root.findValue("error").isNull() && !root.findValue("result").isNull()) {
        JsonNode foundNode = root.findValue(getValueToSearch());
        processJsonNode(company, foundNode);
      } /*else {
        System.err.println(
          "***ACTUALIZACION***\n"+
        "Resultado de "+ mCompanySymbol +" es nulo para: " + getModuleName() + "\n"+ root.toString());
        //System.exit(-1);

      }*/
    } catch (Exception anyException) {
      throw new PluginException(company.getSymbol(), this.getClass().getName(), anyException, mUrl);
    }
  }

  //subclasses should implement
  protected abstract String getValueToSearch();

  protected abstract String getModuleName();

  protected abstract void processJsonNode(Company company, JsonNode node) throws IOException;
}
