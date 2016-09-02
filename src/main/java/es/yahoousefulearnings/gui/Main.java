package es.yahoousefulearnings.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.tools.JavaFileManager;

public class
Main extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("view/Main.fxml"));
    stage.setTitle("UsefulEarnings");
    stage.setScene(new Scene(root, 1024, 768));
    stage.setMaximized(Boolean.TRUE);
    stage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
