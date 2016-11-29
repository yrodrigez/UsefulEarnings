package es.usefulearnings.engine.connection;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * ${PATH}
 * Created by yago on 9/09/16.
 */
public class JSONHTTPClient {

  private Map<String, JsonNode> _cache;
  private ObjectMapper _mapper;

  private JSONHTTPClient() {
    _cache = Collections.synchronizedMap(new TreeMap<>());
    _mapper = new ObjectMapper();
  }

  private static JSONHTTPClient _instance = new JSONHTTPClient();

  public static JSONHTTPClient getInstance() {
    return _instance;
  }

  public JsonNode getJSON(URL url) throws Exception {
    synchronized (url.toString().intern()) { // for the same URL, only one thread at time
      if (!_cache.containsKey(url.toString())) {
        JsonNode jsonObject = getJsonFromJackson(url);
        _cache.put(url.toString(), jsonObject);
        return jsonObject;
      }

      return _cache.get(url.toString());
    }
  }

  private JsonNode getJsonFromJackson(URL url) throws Exception {
    return _mapper.readTree(url);
  }

  public void clearCache() {
    _cache.clear();
  }
}
