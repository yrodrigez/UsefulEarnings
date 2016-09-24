package es.usefulearnings.annotation;

import es.usefulearnings.entities.YahooLongFormatField;
import es.usefulearnings.entities.YahooField;

/**
 * @author Yago on 11/09/2016.
 */
public enum ParameterType {
  CLASS,
  /**
   * Parameter to be ignored at UI
   */
  IGNORE,
  /**
   * {@link YahooLongFormatField}
   */
  YAHOO_LONG_FORMAT_FIELD,

  /**
   * Such as a Collection<T>
   */
  INNER_CLASS_COLLECTION,
  /**
   *
   * String parameter {@link String}.
   */
  RAW_STRING,

  /**
   * To be treated as a Long type {@link Long} to use the Unix TimeStamp
   */
  YAHOO_FIELD_DATE,

  /**
   * To be treated as a Java Double type {@link Double}.
   */
  RAW_NUMERIC,

  /**
   * to be treated as an Object
   * {@link es.usefulearnings.entities.company.CalendarEvents},
   * {@link es.usefulearnings.entities.company.Earnings}
   */
  INNER_CLASS,

  /**
   * Enum Option Type type {@link es.usefulearnings.entities.option.OptionType}
   */
  OPTION_TYPE,

  /**
   * Boolean type {@link Boolean}
   */
  RAW_BOOLEAN,

  /**
   * Represents a Yahoo_Field to be treated as numeric
   * {@link YahooField}
   */
  YAHOO_FIELD_NUMERIC,

  /**
   * Represents a YahooField Collection {@link YahooField}.
   */
  YAHOO_FIELD_COLLECTION,

  /**
   * Java Long parameter, to be converted to a date such as this:
   *
   * **************************** Example *********************************
   * new SimpleDateFormat("yyyy/MM/dd").format(new Date(RAW_DATE * 1000L));
   * **********************************************************************
   */
  RAW_DATE,
  OPTION_LINK_COLLECTION, /**
   * YahooField to be converted to an URL.
   */
  URL
}
