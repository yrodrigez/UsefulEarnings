package es.yahoousefulearnings.engine;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.yahoousefulearnings.entities.Company;
import es.yahoousefulearnings.entities.company.*;
import es.yahoousefulearnings.utils.Json;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchEngine {

  public static Company getCompanyData(String symbol, String... modules) {
    Company company = new Company();
    company.setSymbol(symbol);
    try {
      ObjectMapper mapper = new ObjectMapper();
      String link = YahooLinks.getYahooquoteSummaryLink(symbol, modules);
      JsonNode jsonRoot = mapper.readTree(new URL(link));
      for (String module : modules) {
        switch (module) {
          case "Asset Profile":
            JsonNode assetProfile = Json.removeEmptyClasses(jsonRoot.findValue("assetProfile"));
            try {
              Profile profile = mapper.readValue(assetProfile.traverse(), Profile.class);
              company.setProfile(profile);
            } catch (Exception ne) {
              System.err.println("Something Happened trying to set assetProfile data of " + symbol);
              System.err.println(ne.getMessage());
              // TODO something with this exception!!
            }
            break;
          case "Financial Data":
            JsonNode jsonFinancialData = Json.removeEmptyClasses(jsonRoot.findValue("financialData"));
            try {
              FinancialData financialData = mapper.readValue(jsonFinancialData.traverse(), FinancialData.class);
              company.setFinancialData(financialData);
            } catch (Exception ne) {
              System.err.println("Something Happened trying to set financialData of " + symbol);
              System.err.println(ne.getMessage());
              // TODO something with this exception!!
            }
            break;
          case "Default Key Statistics":
            JsonNode jsonKeyStatistics = Json.removeEmptyClasses(jsonRoot.findValue("defaultKeyStatistics"));
            try {
              DefaultKeyStatistics defaultKeyStatistics = mapper.readValue(jsonKeyStatistics.traverse(), DefaultKeyStatistics.class);
              company.setDefaultKeyStatistics(defaultKeyStatistics);
            } catch (Exception ne) {
              System.err.println("Something Happened trying to set defaultKeyStatistics data of " + symbol);
              System.err.println(ne.getMessage());
              // TODO something with this exception!!
            }
            break;
          case "Calendar Events":
            JsonNode jsonCalendarEvents = Json.removeEmptyClasses(jsonRoot.findValue("calendarEvents"));
            try {
              CalendarEvents calendarEvents = mapper.readValue(jsonCalendarEvents.traverse(), CalendarEvents.class);
              company.setCalendarEvents(calendarEvents);
            } catch (Exception ne) {
              System.err.println("Something Happened trying to set calendarEvents data of " + symbol);
              System.err.println(ne.getMessage());
              // TODO something with this exception!!
            }
            break;
          case "Income Statement History":
            JsonNode jsonIncomeStatementHistory = jsonRoot.findValue("incomeStatementHistory");
            // this is an JSon object that contains a incomeStatementHistory (yeah same name) list inside
            // so we need to go deeper and get that object because is the one who contains the data
            try {
              JsonNode jsonIncomeStatements = Json.removeEmptyClasses(jsonIncomeStatementHistory.findValue("incomeStatementHistory"));
              ArrayList<IncomeStatement> incomeStatements = mapper.readValue(
                jsonIncomeStatements.traverse(),
                new TypeReference<ArrayList<IncomeStatement>>() {
                }
              );
              company.setIncomeStatements(incomeStatements);
            } catch (Exception ne) {
              System.err.println("Something Happened trying to set incomeStatementHistory data of " + symbol);
              System.err.println(ne.getMessage());
              // TODO something with this exception!!
            }
            break;
          case "Cashflow Statement History":
            JsonNode jsonCashFlowStatmentHistory = Json.removeEmptyClasses(jsonRoot.findValue("cashflowStatements"));
            try {
              ArrayList<CashFlowStatement> cashFlowStatements = mapper.readValue(
                jsonCashFlowStatmentHistory.traverse(),
                new TypeReference<ArrayList<CashFlowStatement>>() {
                }
              );
              company.setCashFlowStatements(cashFlowStatements);
            } catch (Exception ne) {
              System.err.println("Something Happened trying to set cashflowStatements data of " + symbol);
              System.err.println(ne.getMessage());
              // TODO something with this exception!!
            }
            break;
          case "Balance Sheet History":
            JsonNode jsonBalanceSheetStatements = Json.removeEmptyClasses(jsonRoot.findValue("balanceSheetStatements"));
            try {
              ArrayList<BalanceSheetStatement> balanceSheetStatements = mapper.readValue(
                jsonBalanceSheetStatements.traverse(),
                new TypeReference<ArrayList<BalanceSheetStatement>>() {
                }
              );
              company.setBalanceSheetStatements(balanceSheetStatements);
            } catch (Exception ne) {
              System.err.println("Something Happened trying to set balanceSheetStatements data of " + symbol);
              System.err.println(ne.getMessage());
              // TODO something with this exception!!
            }
            break;
        }// end switch
      }// end foreach

      return company;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new Company();
  }

}
