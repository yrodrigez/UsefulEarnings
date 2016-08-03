package es.yahoousefulearnings.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Field {

  @JsonProperty("raw") private double raw;
  @JsonProperty("fmt") private String fmt;
  @JsonIgnore private FieldType fieldType;

  @JsonCreator
  public Field(
    @JsonProperty("raw") final double  raw,
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

  public double getRaw() {
    return raw;
  }

  public void setRaw(final double raw) {
    this.raw = raw;
  }


  public FieldType getFieldType() {
    return fieldType;
  }

  public void setFieldType(FieldType fieldType) {
    this.fieldType = fieldType;
  }
  @Override
  public String toString() {
    return "\n\t{" +
      "\n\t\traw=" + raw + "," +
      "\n\t\tfmt='" + fmt + '\'' +
      "\n\t}";
  }
}
