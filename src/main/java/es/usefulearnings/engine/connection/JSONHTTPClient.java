package es.usefulearnings.engine.connection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * ${PATH}
 * Created by yago on 9/09/16.
 */
public class JSONHTTPClient {

  private Map<String, JsonNode> cache;
  private ObjectMapper mapper;

  private class MyObjectMapper extends ObjectMapper {
    MyObjectMapper(){
      super();
      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
      configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

      configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
      configure(JsonParser.Feature.ALLOW_COMMENTS, true);
      configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
    }
  }

  private JSONHTTPClient() {
    cache = Collections.synchronizedMap(new TreeMap<>());
    mapper = new MyObjectMapper();
  }

  private static JSONHTTPClient mInstance = new JSONHTTPClient();

  public static JSONHTTPClient getInstance() {
    return mInstance;
  }

  public JsonNode getJSON(URL url) throws Exception {
    synchronized (url.toString().intern()) { // for the same URL, only one thread at time
      if (!cache.containsKey(url.toString())) {
        JsonNode jsonObject = getJsonFromJackson(url);
        cache.put(url.toString(), jsonObject);
      }

      return cache.get(url.toString());
    }
  }

  private JsonNode getJsonFromJackson(URL url) throws Exception {
    return mapper.readTree(url);
  }

  public void clearCache() {
    cache.clear();
  }
}
