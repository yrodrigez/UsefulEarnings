package es.usefulearnings.engine.connection;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;


/**
 * @author yago.
 */
public class ObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {
  public ObjectMapper(){
    super();
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

    configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
    configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
  }
}
