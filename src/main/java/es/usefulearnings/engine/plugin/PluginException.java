package es.usefulearnings.engine.plugin;

/**
 * @author Yago on 17/09/2016.
 */
public class PluginException extends  Exception {

  public PluginException(String companySymbol, String pluginName, Exception anyException) {
    super(
      "Exception in " + pluginName.substring(pluginName.lastIndexOf(".") + 1)
      + " with company " + companySymbol,
      anyException
    );
  }
}
