package es.yahoousefulearnings.gui.view;

import es.yahoousefulearnings.engine.Field;
import es.yahoousefulearnings.entities.Company;
import es.yahoousefulearnings.entities.company.CalendarEvents;
import es.yahoousefulearnings.entities.company.Profile;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;

import java.util.ArrayList;
import java.util.Collection;

/**
 * View manager of CompanyViewController's data
 * @author Yago Rodríguez
 */
public class CompanyViewController implements ViewManager {
  private Company company;
  private WebEngine webEngine;

  public CompanyViewController(Company company, WebEngine webEngine) {
    this.setCompany(company);
    this.webEngine = webEngine;
  }

  /**
   * Generates the company's profile view
   * @param profile entities.company.Profile
   * @return Collection<Node>
   */
  private Collection<Node> getProfileNodes(Profile profile) {
    Collection<Node> nodes = new ArrayList<>();

    if (profile.isSet()) {
      nodes.add(new Label("Address: " + profile.getAddress()));
      nodes.add(new Label("City: " + profile.getCity()));
      nodes.add(new Label("State: " + profile.getState()));
      nodes.add(new Label("ZipCode: " + profile.getZip()));
      nodes.add(new Label("Country: " + profile.getCountry()));
      nodes.add(new Label("Phone: " + profile.getPhone()));
      HBox website = new HBox();
      Hyperlink hyperlink = new Hyperlink(profile.getWebsite());
      hyperlink.setOnAction( event -> webEngine.load(profile.getWebsite()) );
      website.getChildren().addAll(new Label("Website: "), hyperlink);
      nodes.add(website);
      nodes.add(new Label("Industry: " + profile.getIndustry()));
      nodes.add(new Label("Sector: " + profile.getSector()));
      nodes.add(new Label("Full-Time Employees: " + profile.getFullTimeEmployees()));
    } else {
      nodes.add(new Label("No profile found..."));
    }

    return nodes;
  }

  private Collection<Node> getCalendarEventsNodes(CalendarEvents calendarEvents) {
    Collection<Node> nodes = new ArrayList<>();
    if(calendarEvents.isSet()){
      if(calendarEvents.getEarningsDate().size() > 1) {
        nodes.add(new Label("Earnings date: " + calendarEvents.getEarningsDate().get(0).getFmt() + " - " + calendarEvents.getEarningsDate().get(1).getFmt()));
      } else {
        if(calendarEvents.getEarningsDate().size() > 0) {
          nodes.add(new Label("Earnings date: " + calendarEvents.getEarningsDate().get(0).getFmt()));
        } else {
          nodes.add(new Label("No earnings date found..."));
        }
      }
      nodes.add(getFieldLabel("Earnings average: ", calendarEvents.getEarningsAverage()));
      try {
        nodes.add(
          new Label(
              "Earnings: " +
              calendarEvents.getEarningsLow().getFmt() +
              " - " +
              calendarEvents.getEarningsHigh().getFmt()
          )
        );
      } catch (NullPointerException ex) {
        nodes.add(new Label("Earnings: not found..."));
      }

      nodes.add(getFieldLabel("Revenue average: ", calendarEvents.getRevenueAverage()));
      try {
        nodes.add(
          new Label(
            "Revenue: " +
              calendarEvents.getRevenueLow().getFmt() +
              " - " +
              calendarEvents.getRevenueHigh().getFmt()
          )
        );
      } catch (NullPointerException ex) {
        nodes.add(new Label("Revenue: not found..."));
      }

      nodes.add(getFieldLabel("Ex Dividend Date: ", calendarEvents.getExDividendDate()));
      nodes.add(getFieldLabel("Dividend Date: ", calendarEvents.getDividendDate()));

    } else {
      nodes.add(new Label("No calendar events found..."));
    }

    return nodes;
  }

  private Label getFieldLabel(String msj, Field field){
    Label label = new Label();
    try {
      label.setText(msj + field.getFmt());
    } catch (NullPointerException ex){
      label.setText(msj + " not found...");
    }

    return label;
  }

  @Override
  public Collection<Node> setView() {
    Collection<Node> view = new ArrayList<>();


    VBox profile = new VBox();
    profile.getChildren().addAll(getProfileNodes(company.getProfile()));

    VBox calendarEvents = new VBox();
    calendarEvents.getChildren().addAll(getCalendarEventsNodes(this.company.getCalendarEvents()));

    HBox hBox = new HBox(
      profile,
      new Separator(Orientation.VERTICAL),
      calendarEvents,
      new Separator(Orientation.VERTICAL)
    );
    view.add(hBox);
    view.add(new Separator(Orientation.HORIZONTAL));

    return view;
  }

  public void setCompany(Company company) {
    this.company = company;
  }
}
