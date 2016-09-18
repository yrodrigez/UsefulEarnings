package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.Profile;
import es.usefulearnings.utils.Json;

import java.net.URL;

/**
 * @author yago.
 */
public class ProfilePlugin implements Plugin<Company> {

  private Profile mProfile;
  private URL mUrl;
  private ObjectMapper mapper;

  private String mCompanySymbol;
  private String mModule = YahooLinks.COMPANY_ASSET_PROFILE;

  public ProfilePlugin() {
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
      JsonNode profileNode = Json.removeEmptyClasses(root.findValue(mModule));
      mProfile = mapper.readValue(profileNode.traverse(), Profile.class);

      company.setProfile(mProfile);
    } catch (Exception anyException) {
      throw new PluginException(company.getSymbol(), this.getClass().getName(), anyException);
    }
  }

}
