package es.yahoousefulearnings.gui.controller;

import es.yahoousefulearnings.gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Yago on 04/09/2016.
 */
public class FilterController implements Initializable {
  @FXML
  private VBox vBox;
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    vBox.setStyle("-fx-background-color: white");
    vBox.getChildren().addAll(new Label("We are working like crazy"),new ImageView(new Image(Main.class.getResourceAsStream("icons/Y2k.gif"))));
  }
}
