package es.usefulearnings.gui.controller;

import es.usefulearnings.gui.Main;
import es.usefulearnings.gui.view.AlertHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Yago on 04/09/2016.
 */
public class VistaNavigator {

  public static final String MAIN     = "fxml/main.fxml";
  public static final String NAVIGATE = "fxml/navigate.fxml";
  static final String FILTER          = "fxml/filter.fxml";
  static final String DOWNLOAD        = "fxml/download.fxml";
  static final String HISTORY         = "fxml/history.fxml";
  static final String OPTIONS         = "fxml/options.fxml";
  static final String NEW_HISTORY     = "fxml/newDownloadPane.fxml";

  private final static VistaNavigator singleton = new VistaNavigator();

  public static VistaNavigator getInstance() {
    return singleton;
  }
  /**
   * private class that will only have the nodes of a loaded vista only if it was previously loaded
   */
  private class Vista {
    private boolean isLoaded;
    private Node node;

    Vista() {
      isLoaded = false;
    }

    Node load(final Node node){
      this.node = node;
      isLoaded = true;
      return this.node;
    }

    boolean isLoaded(){
      return isLoaded;
    }


    Node getNode() {
      return node;
    }
  }

  /**
   * This is the VistaNavigator's cache, it will have all vistas and will know if they are loaded or not
   */
  private Map<String, Vista> vistas = new TreeMap<>();

  private VistaNavigator() {
    vistas.put(MAIN,     new Vista());
    vistas.put(NAVIGATE, new Vista());
    vistas.put(FILTER,   new Vista());
    vistas.put(DOWNLOAD, new Vista());
    vistas.put(HISTORY,  new Vista());
    vistas.put(OPTIONS,  new Vista());
    // vistas.put(NEW_HISTORY,  new Vista());
  }

  /** The main application layout controller. */
  private MainController mainController;

  /**
   * Stores the main controller for later use in navigation tasks.
   *
   * @param mainController the main application layout controller.
   */
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  /**
   * Loads the vista specified by the fxml file into the
   * vistaHolder pane of the main application layout.
   *
   * @param fxml the fxml file to be loaded.
   */
  public void loadVista(final String fxml) {
    try {
      final Vista vista = vistas.get(fxml);
      if(fxml.equals(FILTER)){
        mainController.setVista(vista.load(FXMLLoader.load(Main.class.getResource(fxml))));
      }else {
        mainController.setVista(
          vista.isLoaded() ? vista.getNode() : vista.load(FXMLLoader.load(Main.class.getResource(fxml)))
        );
      }
      } catch (IOException e) {
        AlertHelper.showExceptionAlert(e);
      }
  }
}
