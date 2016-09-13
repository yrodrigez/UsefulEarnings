package es.usefulearnings.engine.connection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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

  private JSONHTTPClient() {
    cache = Collections.synchronizedMap(new TreeMap<>());
    mapper = new ObjectMapper();
  }

  private static JSONHTTPClient mInstance = new JSONHTTPClient();

  public static JSONHTTPClient getInstance() {
    return mInstance;
  }

  public JsonNode getJSON(URL url) throws IOException {
    synchronized (url.toString().intern()) { // for the same URL, only one thread at a time
      if (!cache.containsKey(url.toString())) {
        JsonNode jsonObject = getJsonFromJackson(url);
        cache.put(url.toString(), jsonObject);
      }

      return cache.get(url.toString());
    }
  }

  private JsonNode getJsonFromJackson(URL url) throws IOException {
    return mapper.readTree(url);
  }

  public void clearCache() {
    cache.clear();
  }
}
