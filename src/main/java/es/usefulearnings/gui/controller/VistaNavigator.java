package es.usefulearnings.gui.controller;

import es.usefulearnings.gui.Main;
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

  private static VistaNavigator singleton = new VistaNavigator();

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

    Node load(Node node){
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
  public void loadVista(String fxml) {

    try {
      Vista vista = vistas.get(fxml);
      mainController.setVista(
        vista.isLoaded() ? vista.getNode() : vista.load(FXMLLoader.load(Main.class.getResource(fxml)))
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



}
