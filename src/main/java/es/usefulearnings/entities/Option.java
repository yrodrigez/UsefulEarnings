package es.usefulearnings.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.entities.option.Price;
import es.usefulearnings.entities.option.SummaryDetail;

import java.io.Serializable;

/**
 *
 * @author Yago
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Option implements Serializable {

  private String optionSymbol;

  @JsonProperty("price")
  private Price price;
  @JsonProperty("summaryDetail")
  private SummaryDetail summaryDetail;

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
    this.price.set();
  }

  public SummaryDetail getSummaryDetail() {
    return summaryDetail;
  }

  public void setSummaryDetail(SummaryDetail summaryDetail) {
    this.summaryDetail = summaryDetail;
    this.summaryDetail.set();
  }

  public String getOptionSymbol() {
    return optionSymbol;
  }

  public void setOptionSymbol(String optionSymbol) {
    this.optionSymbol = optionSymbol;
  }
}
