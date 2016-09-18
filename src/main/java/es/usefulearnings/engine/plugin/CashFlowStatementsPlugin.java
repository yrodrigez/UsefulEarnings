package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooLinks;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.CashFlowStatement;
import es.usefulearnings.utils.Json;

import java.net.URL;
import java.util.ArrayList;

/**
 * @author Yago
 */
public class CashFlowStatementsPlugin implements Plugin<Company> {
  private ArrayList<CashFlowStatement> mCashflowStatemnts;
  private URL mUrl;
  private ObjectMapper mapper;

  private String mCompanySymbol;
  private String mModule = YahooLinks.COMPANY_CASHFLOW_STATEMENT_HISTORY;

  public CashFlowStatementsPlugin() {
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
      JsonNode jsonBalanceSheetStatements = Json.removeEmptyClasses(root.findValue("cashflowStatements"));
      mCashflowStatemnts = mapper.readValue(
        jsonBalanceSheetStatements.traverse(),
        new TypeReference<ArrayList<CashFlowStatement>>() {
        }
      );

      company.setCashFlowStatements(mCashflowStatemnts);
    } catch (Exception anyException) {
      throw new PluginException(company.getSymbol(), this.getClass().getName(), anyException);
    }
  }
}
