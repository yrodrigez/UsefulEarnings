package es.yahoousefulearnings.engine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LongFormatField extends Field{

  @JsonProperty("longFmt") private String longFmt;

  @JsonCreator
  public LongFormatField(
    @JsonProperty("raw") final double  raw,
    @JsonProperty("fmt") final String fmt,
    @JsonProperty("longFmt") final String longFmt
  ) {
    super(raw, fmt);
    this.setLongFmt(longFmt);
  }

  public String getLongFmt() {
    return longFmt;
  }

  public void setLongFmt(String longFmt) {
    this.longFmt = longFmt;
  }

  @Override
  public String toString() {
    return "\n\t{" +
      "\n\t\traw=" + super.getRaw() + ',' +
      "\n\t\tfmt='" + super.getFmt() + "'," +
      "\n\t\tlongFmt='" + longFmt + '\'' +
      "\n\t}";
  }
}
