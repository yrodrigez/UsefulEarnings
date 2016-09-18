package es.usefulearnings.engine.plugin;

import java.net.URL;

/**
 * @author Yago on 17/09/2016.
 */
public class PluginException extends  Exception {

  public PluginException(String companySymbol, String pluginName, Exception anyException, URL mUrl) {
    super(
      "Exception in " + pluginName.substring(pluginName.lastIndexOf(".") + 1)
      + " with company " + companySymbol
      + "\n URL: " + mUrl,
      anyException
    );
  }
}
