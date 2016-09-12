package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.Profile;
import es.usefulearnings.utils.Json;

import java.io.IOException;
import java.net.URL;

/**
 * ${PATH}
 * Created by yago on 9/09/16.
 */
public class ProfilePlugin implements Plugin {

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

  public void setCompanySymbol(String mCompanySymbol) {
    this.mCompanySymbol = mCompanySymbol;
    mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, mModule);
  }

  @Override
  public void addInfo(Company company) {
    try {
      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      JsonNode profileNode = Json.removeEmptyClasses(root.findValue(mModule));

      mProfile = mapper.readValue(profileNode.traverse(), Profile.class);
    } catch (IOException ne) {
      System.err.println("Something Happened trying to set Profile data of " + mCompanySymbol);
      System.err.println(ne.getMessage());
      // TODO something with this exception!!
    }
    company.setProfile(mProfile);
  }
}
