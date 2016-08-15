package es.yahoousefulearnings.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.yahoousefulearnings.entities.option.Price;
import es.yahoousefulearnings.entities.option.SummaryProfile;

/**
 *
 * @author Yago
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Option {

  private String optionSymbol;

  @JsonProperty("price")
  private Price price;
  @JsonProperty("summaryProfile")
  private SummaryProfile summaryProfile;

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
    this.price.set();
  }

  public SummaryProfile getSummaryProfile() {
    return summaryProfile;
  }

  public void setSummaryProfile(SummaryProfile summaryProfile) {
    this.summaryProfile = summaryProfile;
    this.summaryProfile.set();
  }

  public String getOptionSymbol() {
    return optionSymbol;
  }

  public void setOptionSymbol(String optionSymbol) {
    this.optionSymbol = optionSymbol;
  }
}
