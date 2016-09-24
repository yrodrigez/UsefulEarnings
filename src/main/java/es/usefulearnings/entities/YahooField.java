package es.usefulearnings.entities;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

/**
 * @author Yago Rodríguez
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class YahooField implements Serializable {
  @JsonProperty("raw") private double raw;
  @JsonProperty("fmt") private String fmt;

  @JsonCreator
  public YahooField(
    @JsonProperty("raw") double raw,
    @JsonProperty("fmt") final String fmt
  ) {
    this.setFmt(fmt);
    this.setRaw(raw);
  }


  public String getFmt() {
    return fmt;
  }

  public void setFmt(final String fmt) {
    this.fmt = fmt;
  }

  public void setRaw(final double raw) {
    this.raw = raw;
  }

  public double getRaw() {
    return raw;
  }

  @Override
  public String toString() {
    return "\n\t{" +
      "\n\t\traw=" + raw + "," +
      "\n\t\tfmt='" + fmt + '\'' +
      "\n\t}";
  }

}
