package es.usefulearnings.engine.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import es.usefulearnings.engine.connection.YahooFinanceAPI;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.SummaryDetail;

import java.io.IOException;

/**
 * @author Yago on 02/10/2016.
 */
public class CompanySummaryDetailPlugin extends YahooFinanceAPIPlugin {
  @Override
  protected String getValueToSearch() {
    return YahooFinanceAPI.COMPANY_SUMMARY_DETAIL;
  }

  @Override
  protected String getModuleName() {
    return YahooFinanceAPI.COMPANY_SUMMARY_DETAIL;
  }

  @Override
  protected void processJsonNode(Company company, JsonNode node) throws IOException {
    SummaryDetail summaryDetail = mapper.readValue(node.traverse(), SummaryDetail.class);
    company.setSummaryDetail(summaryDetail);
  }
}
