package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import es.usefulearnings.engine.connection.YahooFinanceAPI;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.BalanceSheetStatement;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Yago Rodríguez
 */
public class BalanceSheetStatementsPlugin extends YahooFinanceAPIPlugin {

  private ArrayList<BalanceSheetStatement> mBalanceSheetStatements;


  @Override
  protected String getValueToSearch() {
    return "balanceSheetStatements";
  }

  @Override
  protected String getModuleName() {
    return YahooFinanceAPI.COMPANY_BALANCE_SHEET_HISTORY;
  }


  @Override
  protected void processJsonNode(Company company, JsonNode node) throws IOException {
    mBalanceSheetStatements = mapper.readValue(
      node.traverse(),
      new TypeReference<ArrayList<BalanceSheetStatement>>() {
      }
    );
    company.setBalanceSheetStatements(mBalanceSheetStatements);
  }
}
