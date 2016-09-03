package es.yahoousefulearnings.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));

    stage.setTitle("UsefulEarnings");
    Scene scene = new Scene(root, 1024, 768);
    scene.getStylesheets().add(getClass().getResource("css/main.css").toString());
    stage.setScene(scene);
    stage.setMaximized(Boolean.TRUE);
    stage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
