package es.yahoousefulearnings.engine;

import com.fasterxml.jackson.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Field  {
  @JsonProperty("raw") private Number raw;
  @JsonProperty("fmt") private String fmt;
  @JsonIgnore private FieldType fieldType;


  @JsonCreator
  public Field(
    @JsonProperty("raw") Number raw,
    @JsonProperty("fmt") final String fmt
  ) {
    this.setFmt(fmt);
    this.setRaw(raw, fmt);
  }

  private boolean isDate(String fmt){
    Pattern datePattern = Pattern.compile(
      "[0-9]{4}-(([1][0-2])|(0[0-9]))-((0[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))"
    );
    Matcher matcher = datePattern.matcher(fmt);
    return matcher.find();
  }

  private boolean isDouble(String fmt) {
    Pattern datePattern = Pattern.compile(
      "[0-9]+\\.?[0-9]*(M|B|%)?$"
    );
    Matcher matcher = datePattern.matcher(fmt);
    return !isDate(fmt) && matcher.find();
  }


  public String getFmt() {
    return fmt;
  }

  public void setFmt(final String fmt) {
    this.fmt = fmt;
  }

  public Number getRaw() {
    switch (this.fieldType){
      case DATE:
        return raw.longValue();
      case DOUBLE:
        return raw.doubleValue();
      case INTEGER:
        return raw.intValue();
      default:
        return raw;
    }
  }

  public void setRaw(final Number raw) {
    this.raw = raw;
  }

  private void setRaw(final Number raw, final String fmt) {
    if(isDate(fmt)) {
      this.setFieldType(FieldType.DATE);
    } else {
      if(isDouble(fmt)){
        this.setFieldType(FieldType.DOUBLE);
      }
    }
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
