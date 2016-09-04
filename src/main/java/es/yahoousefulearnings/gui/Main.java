package es.yahoousefulearnings.gui;

import es.yahoousefulearnings.gui.controller.MainController;
import es.yahoousefulearnings.gui.controller.VistaNavigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("UsefulEarnings");
    stage.setScene(createScene(loadMainPane()));
    stage.setMaximized(Boolean.TRUE);
    stage.show();
  }

  /**
   * Loads the main fxml layout.
   * Sets up the vista switching VistaNavigator.
   * Loads the first vista into the fxml layout.
   *
   * @return the loaded pane.
   * @throws IOException if the pane could not be loaded.
   */
  private Pane loadMainPane() throws IOException {
    FXMLLoader loader = new FXMLLoader();

    Pane mainPane =  loader.load(
      getClass().getResourceAsStream(
        VistaNavigator.MAIN
      )
    );

    MainController mainController = loader.getController();

    VistaNavigator.setMainController(mainController);
    VistaNavigator.loadVista(VistaNavigator.NAVIGATE);

    return mainPane;
  }

  /**
   * Creates the main application scene.
   *
   * @param mainPane the main application layout.
   *
   * @return the created scene.
   */
  private Scene createScene(Pane mainPane) {
    Scene scene = new Scene(mainPane);

    scene.getStylesheets().setAll(
      getClass().getResource("css/main.css").toExternalForm()
    );

    return scene;
  }


  public static void main(String[] args) {
    launch(args);
  }
}
