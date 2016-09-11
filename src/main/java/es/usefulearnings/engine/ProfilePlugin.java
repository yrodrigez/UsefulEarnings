package es.usefulearnings.engine;

import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.Profile;

/**
 * ${PATH}
 * Created by yago on 9/09/16.
 */
public class ProfilePlugin implements Plugin {

  private Profile mProfile;
  private String mModule;

  public ProfilePlugin(){
    mModule =  YahooLinks.COMPANY_ASSET_PROFILE;
  }

  @Override
  public void addInfo(Company company) {
    company.setProfile(mProfile);
  }
}
