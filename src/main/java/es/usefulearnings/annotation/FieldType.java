package es.usefulearnings.annotation;

/**
 * @author Yago on 10/09/2016.
 */
public enum FieldType {
  /**
   * default. String type {@link String}.
   */
  STRING,

  /**
   * To be treated as a Long type {@link Long} to use the Unix TimeStamp
   */
  DATE,

  /**
   * To be treated as a Double type {@link Double}.
   */
  NUMERIC,

  /**
   * to be treated as an Object {@link es.usefulearnings.entities.company.CalendarEvents},
   * {@link es.usefulearnings.entities.company.Earnings}
   */
  INNER_CLASS,

  /**
   * Enum type {@link es.usefulearnings.entities.option.OptionType}
   */

  OPTION_TYPE,

  /**
   * Boolean type {@link Boolean}
   */
  BOOLEAN,

  /**
   * Represents a raw numeric name (not wrapped in a class)
   */
  RAW_NUMERIC,

  /**
   * Only to identify a Field Collection {@link es.usefulearnings.entities.Field}.
   */
  FIELD_ARRAY_LIST,

  /**
   * Field to be converted to an URL.
   */
  URL
}
