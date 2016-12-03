package es.usefulearnings.entities.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.usefulearnings.annotation.EntityParameter;
import es.usefulearnings.annotation.ParameterType;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionChain extends CompanyData implements Serializable {

  @JsonProperty("expirationDates")
  @EntityParameter(name = "Expiration dates", parameterType = ParameterType.RAW_DATE_COLLECTION)
  private ArrayList<Long> expirationDates;

  @JsonProperty("calls")
  @EntityParameter(name = "Calls", parameterType = ParameterType.INNER_CLASS_COLLECTION)
  private ArrayList<OptionLink> calls;

  @JsonProperty("puts")
  @EntityParameter(name = "Puts", parameterType = ParameterType.INNER_CLASS_COLLECTION)
  private ArrayList<OptionLink> puts;


  public ArrayList<Long> getExpirationDates() {
    return expirationDates;
  }

  public void setExpirationDates(ArrayList<Long> expirationDates) {
    this.expirationDates = expirationDates;
  }

  public ArrayList<OptionLink> getCalls() {
    return calls;
  }

  public void setCalls(ArrayList<OptionLink> calls) {
    this.calls = calls;
  }

  public ArrayList<OptionLink> getPuts() {
    return puts;
  }

  public void setPuts(ArrayList<OptionLink> puts) {
    this.puts = puts;
  }

  @Override
  public String toString() {
    return "OptionChain{" +
      "expirationDates=" + expirationDates +
      ", calls=" + calls +
      ", puts=" + puts +
      '}';
  }


}
