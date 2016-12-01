package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import es.usefulearnings.engine.connection.YahooFinanceAPI;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.CashFlowStatement;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Yago
 */
public class CashFlowStatementsPlugin extends YahooFinanceAPIPlugin {

  @Override
  protected String getValueToSearch() {
    return "cashflowStatements";
  }

  @Override
  protected String getModuleName() {
    return YahooFinanceAPI.COMPANY_CASHFLOW_STATEMENT_HISTORY;
  }

  @Override
  protected void processJsonNode(Company company, JsonNode node) throws IOException {
    ArrayList<CashFlowStatement> cashflowStatements = mapper.readValue(
      node.traverse(),
      new TypeReference<ArrayList<CashFlowStatement>>() {
      }
    );

    //company.setCashFlowStatements(cashflowStatements);
  }
}
