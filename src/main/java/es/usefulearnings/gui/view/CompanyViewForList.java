package es.usefulearnings.gui.view;

import es.usefulearnings.engine.Core;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.company.Profile;
import es.usefulearnings.entities.company.SummaryDetail;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Yago on 10/12/2016.
 */
public class CompanyViewForList extends HBox {
  private Company _company;

  public CompanyViewForList(Company company) throws IllegalArgumentException {
    _company = company;

    getChildren().clear();
    reloadView();
  }

  private void reloadView() throws IllegalArgumentException {
    try {
      _company = Core.getInstance().getCompanyFromSymbol(_company.getSymbol());
    } catch (IllegalArgumentException ex) {
      System.err.println(ex.getMessage());
      throw ex;
    }
    if (_company.getSummaryDetail() != null && _company.getSummaryDetail().isSet()) {
      SummaryDetail summaryDetail = _company.getSummaryDetail();
      Profile profile = _company.getProfile();

      Label symbol = new Label(_company.getSymbol());

      Label beta = new Label(
        (summaryDetail.getBeta().getFmt() != null ? summaryDetail.getBeta().getFmt() : "?")
      );
      Label avgVolume = new Label(
        "AvgVol: " +
          (summaryDetail.getAverageVolume().getFmt()  != null ? summaryDetail.getAverageVolume().getFmt() : "?")
      );
/*
      Label marketCap = new Label(
          (_company.get.getMarketCap().getFmt()  != null ? summaryDetail.getMarketCap().getFmt() : "?")
      );
*/
      beta.setStyle(
        "-fx-font: 10px Tahoma;" +
        "-fx-fill: #726e74"
      );
      avgVolume.setStyle(beta.getStyle());
      //marketCap.setStyle(avgVolume.getStyle());

      HBox data = new HBox(beta, avgVolume/*, marketCap*/);
      data.setSpacing(7d);
      getChildren().clear();
      getChildren().add(new VBox(symbol, data));
    } else {
      // System.out.println(_company.getSummaryDetail().toString());
      getChildren().clear();
      getChildren().add(new Label(_company.getSymbol()));
    }
  }

  public Company getCompany() {
    return _company;
  }

}
