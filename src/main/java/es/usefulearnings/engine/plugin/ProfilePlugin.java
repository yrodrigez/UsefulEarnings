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
 * ${PATH}
 * Created by yago on 9/09/16.
 */
public class ProfilePlugin<E> implements Plugin<E> {

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
  public void addInfo(E entity) {
    try {
      if(entity.getClass().equals(Company.class)){
        mCompanySymbol = ((Company)entity).getSymbol();
      } else {
        throw new IllegalArgumentException("This is not a company");
      }
      mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, mModule);

      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      JsonNode profileNode = Json.removeEmptyClasses(root.findValue(mModule));
      mProfile = mapper.readValue(profileNode.traverse(), Profile.class);

      ((Company)entity).setProfile(mProfile);
    } catch (Exception ne) {
      System.err.println("Something Happened trying to set Profile data of " + mCompanySymbol);
      System.err.println(ne.getMessage());
      // ne.printStackTrace();
      // TODO something with this exception!!
    }
  }
}
