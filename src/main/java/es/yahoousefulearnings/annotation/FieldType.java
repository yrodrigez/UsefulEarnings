package es.yahoousefulearnings.annotation;

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
   * to be treated as an Object {@link es.yahoousefulearnings.entities.company.CalendarEvents},
   * {@link es.yahoousefulearnings.entities.company.Earnings}
   */
  CLASS,

  /**
   * Enum type {@link es.yahoousefulearnings.entities.option.OptionType}
   */

  OPTION_TYPE,

  /**
   * Boolean type {@link Boolean}
   */
  BOOLEAN,
  /**
   * For now it will be to identify a Field Collection {@link es.yahoousefulearnings.entities.Field}.
   */
  COLLECTION
}
