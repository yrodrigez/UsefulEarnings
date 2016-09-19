package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.Profile;

import java.io.IOException;
import java.net.URL;

/**
 * @author yago.
 */
public class ProfilePlugin extends YahooFinanceAPIPlugin {
  @Override
  protected String getValueToSearch() {
    return "assetProfile";
  }

  @Override
  protected String getModuleName() {
    return YahooLinks.COMPANY_ASSET_PROFILE;
  }

  @Override
  protected void processJsonNode(Company company, JsonNode node) throws IOException {
    Profile profile = mapper.readValue(node.traverse(), Profile.class);
    company.setProfile(profile);
  }

}
