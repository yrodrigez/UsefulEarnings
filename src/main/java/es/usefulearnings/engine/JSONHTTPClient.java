package es.usefulearnings.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * ${PATH}
 * Created by yago on 9/09/16.
 */
class JSONHTTPClient {

  private Map<URL, JsonNode> cache;
  private ObjectMapper mapper;

  private JSONHTTPClient() {
    cache = new TreeMap<>();
    mapper = new ObjectMapper();
  }

  private static JSONHTTPClient mInstance = new JSONHTTPClient();

  public static JSONHTTPClient getInstance() {
    return mInstance;
  }

  public JsonNode getJSON(URL url) throws IOException{
    if (!cache.containsKey(url)) {
      JsonNode jsonObject = getJsonFromJackson(url);
      cache.put(url, jsonObject);
    }
    return cache.get(url);
  }

  private JsonNode getJsonFromJackson(URL url) throws IOException {
    return mapper.readTree(url);
  }

  public void clearCache() {
    cache.clear();
  }
}
