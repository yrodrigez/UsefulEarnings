package es.yahoousefulearnings.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Json {

  /**
   * for some weird reason Jackson does not recognize "empty json classes" something like this: "className":{}
   * so this function will use regular expressions to find those empty classes and remove them to then create a new JsonNode
   *
   * @param jsonNode the node to find the empty classes in it
   * @return the new JsonNode with no empty classes luckily
   */

  public static JsonNode removeEmptyClasses(JsonNode jsonNode) {
    String json = jsonNode.toString();
    json = json.replaceAll("\"[A-z]+\":\\{\\},?", "");
    json = json.replaceAll(",{2,}", "");
    json = json.replaceAll(",\\}", "}");
    ObjectMapper mapper = new ObjectMapper();
    try {
      jsonNode = mapper.readTree(json);
    } catch (IOException ioe){
      System.err.println("I can not remove empty classes");
    }

    return jsonNode;
  }


}
