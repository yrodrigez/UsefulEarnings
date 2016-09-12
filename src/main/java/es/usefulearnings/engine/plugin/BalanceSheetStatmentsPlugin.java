package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.BalanceSheetStatement;
import es.usefulearnings.utils.Json;

import java.net.URL;
import java.util.ArrayList;

/**
 * ${PATH}
 * Created by yago on 12/09/16.
 */
public class BalanceSheetStatmentsPlugin implements Plugin {
  private ArrayList<BalanceSheetStatement> mBalanceSheetStatements;
  private URL mUrl;
  private ObjectMapper mapper;

  private String mCompanySymbol;
  private String mModule = YahooLinks.COMPANY_BALANCE_SHEET_HISTORY;

  public BalanceSheetStatmentsPlugin(String companySymbol) {
    mCompanySymbol = companySymbol;
    mapper = new ObjectMapper();
    mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, mModule);
  }

  public String getCompanySymbol() {
    return mCompanySymbol;
  }

  public void setCompanySymbol(String mCompanySymbol) {
    this.mCompanySymbol = mCompanySymbol;
  }

  @Override
  public void addInfo(Company company) {

    try {
      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      JsonNode jsonBalanceSheetStatements = Json.removeEmptyClasses(root.findValue(mModule));
      mBalanceSheetStatements = mapper.readValue(
        jsonBalanceSheetStatements.traverse(),
        new TypeReference<ArrayList<BalanceSheetStatement>>() {
        }
      );
      company.setBalanceSheetStatements(mBalanceSheetStatements);
    } catch (Exception ne) {
      System.err.println("Something Happened trying to set balanceSheetStatements data of " + mCompanySymbol);
      System.err.println(ne.getMessage());
      // TODO something with this exception!!
    }

  }
}
