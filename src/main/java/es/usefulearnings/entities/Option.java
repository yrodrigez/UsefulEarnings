package es.usefulearnings.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.entities.option.Price;
import es.usefulearnings.entities.option.SummaryDetail;

import java.io.*;

/**
 *
 * @author Yago
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Option implements Serializable, Entity, Savable {

  public static final String EXTENSION = ".opn";
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

  public String getSymbol() {
    return optionSymbol;
  }

  public void setOptionSymbol(String optionSymbol) {
    this.optionSymbol = optionSymbol;
  }

  @Override
  public boolean isEmpty() {
    return !(summaryDetail.isSet() || price.isSet());
  }

  @Override
  public void save(File fileToSave) throws IOException {
    String location = fileToSave.getAbsolutePath()
                      + File.separator
                      + this.optionSymbol
                      + EXTENSION;

    FileOutputStream data = new FileOutputStream(location);
    ObjectOutputStream stream = new ObjectOutputStream(data);
    stream.writeObject(this);
    stream.close();
    data.close();
  }
}
