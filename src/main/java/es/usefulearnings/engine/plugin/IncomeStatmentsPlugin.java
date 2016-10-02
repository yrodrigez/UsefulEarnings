package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.usefulearnings.engine.connection.JSONHTTPClient;
import es.usefulearnings.engine.connection.MultiModuleYahooFinanceURLProvider;
import es.usefulearnings.engine.connection.YahooFinanceAPI;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.IncomeStatement;

import java.net.URL;
import java.util.ArrayList;

/**
 * ${PATH}
 * Created by yago on 12/09/16.
 */
public class IncomeStatmentsPlugin implements Plugin<Company> {
  private ArrayList<IncomeStatement> mIncomeStatements;
  private URL mUrl;
  private ObjectMapper mapper;

  private String mCompanySymbol;
  private String mModule = YahooFinanceAPI.COMPANY_INCOME_STATEMENT_HISTORY;

  public IncomeStatmentsPlugin() {
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
      // this is an JSon object that contains a incomeStatementHistory (yeah same name) list inside
      // so we need to go deeper and get that object because is the one who contains the data
      JsonNode root = JSONHTTPClient.getInstance().getJSON(mUrl);
      JsonNode jsonIncomeStatementHistory = root.findValue(mModule);
      JsonNode jsonIncomeStatements = jsonIncomeStatementHistory.findValue(mModule);
      mIncomeStatements = mapper.readValue(
        jsonIncomeStatements.traverse(),
        new TypeReference<ArrayList<IncomeStatement>>() {
        }
      );

      company.setIncomeStatements(mIncomeStatements);
    } catch (Exception anyException) {
      throw new PluginException(company.getSymbol(), this.getClass().getName(), anyException, mUrl);
    }
  }
}
