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
 *
 * Created by yago on 12/09/16.
 */
public class BalanceSheetStatementsPlugin<E> implements Plugin<E> {
  private ArrayList<BalanceSheetStatement> mBalanceSheetStatements;
  private URL mUrl;
  private ObjectMapper mapper;

  private String mCompanySymbol;
  private String mModule = YahooLinks.COMPANY_BALANCE_SHEET_HISTORY;

  public BalanceSheetStatementsPlugin() {
    mapper = new ObjectMapper();
  }

  public String getCompanySymbol() {
    return mCompanySymbol;
  }


  @Override
  public void addInfo(E entity) {
    try {
      if(!entity.getClass().equals(Company.class)) throw new IllegalArgumentException("This is not a company");

      mCompanySymbol = ((Company)entity).getSymbol();
      mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, mModule);

      mUrl = MultiModuleYahooFinanceURLProvider.getInstance().getURLForModule(mCompanySymbol, mModule);
      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      JsonNode balanceSheetStatementsNode = Json.removeEmptyClasses(root.findValue("balanceSheetStatements"));
      mBalanceSheetStatements = mapper.readValue(
        balanceSheetStatementsNode.traverse(),
        new TypeReference<ArrayList<BalanceSheetStatement>>() {
        }
      );

      ((Company)entity).setBalanceSheetStatements(mBalanceSheetStatements);
    } catch (Exception ne) {
      System.err.println("Something Happened trying to set BalanceSheetStatements data of " + mCompanySymbol);
      System.err.println("URL: " + mUrl);
      System.err.println("Yahoo URL: " + "http://finance.yahoo.com/quote/" + mCompanySymbol);
      System.err.println(ne.getMessage());
    }
  }
}
