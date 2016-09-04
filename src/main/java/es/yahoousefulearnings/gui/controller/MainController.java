package es.yahoousefulearnings.gui.controller;

import es.yahoousefulearnings.gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable{

  @FXML
  private BorderPane mainPane;
  @FXML
  private VBox verticalMenu;
  @FXML
  private VBox verticalOptions;

  public void initialize(URL location, ResourceBundle resources){
    verticalOptions.setSpacing(10);
    verticalOptions.getChildren().addAll(setOptionsIcons());
    verticalMenu.setSpacing(10);
    verticalMenu.getChildren().addAll(setMenuIcons());
  }

  public void setVista(Node vista){
    mainPane.setCenter(vista);
  }

  private ArrayList<Button> setMenuIcons() {
    ArrayList<Button> ret = new ArrayList<>();
    ImageView navigateIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/navigate-white.png")
    ));
    Button navigateButton = new Button("", navigateIcon);
    navigateButton.getStyleClass().add("vertical-button");
    navigateButton.setTooltip(new Tooltip("Navigate"));
    navigateButton.setOnAction(event -> {
      VistaNavigator.loadVista(VistaNavigator.NAVIGATE);
      event.consume();
    });
    ret.add(navigateButton);

    ImageView filterIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/filter-white.png")
    ));
    Button filterButton = new Button("", filterIcon);
    filterButton.getStyleClass().add("vertical-button");
    filterButton.setTooltip(new Tooltip("Filter"));
    filterButton.setOnAction(event -> {
      VistaNavigator.loadVista(VistaNavigator.FILTER);
      event.consume();
    });
    ret.add(filterButton);

    ImageView downloadIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/download-white.png")
    ));
    Button downloadButton = new Button("", downloadIcon);
    downloadButton.getStyleClass().add("vertical-button");
    downloadButton.setTooltip(new Tooltip("Download"));
    downloadButton.setOnAction(event -> {
      VistaNavigator.loadVista(VistaNavigator.DOWNLOAD);
      event.consume();
    });
    ret.add(downloadButton);

    ImageView historyIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/history-white.png")
    ));
    Button historyButton = new Button("", historyIcon);
    historyButton.getStyleClass().add("vertical-button");
    historyButton.setTooltip(new Tooltip("History"));
    historyButton.setOnAction(event -> {
      VistaNavigator.loadVista(VistaNavigator.HISTORY);
      event.consume();
    });
    ret.add(historyButton);

    return ret;
  }

  private ArrayList<Button> setOptionsIcons() {
    ArrayList<Button> ret = new ArrayList<>();
    ImageView optionsIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/gear-white.png")
    ));
    Button optionsButton = new Button("", optionsIcon);
    optionsButton.getStyleClass().add("vertical-button");
    optionsButton.setTooltip(new Tooltip("Options"));
    ret.add(optionsButton);

    ImageView helpIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/info-white.png")
    ));
    Button aboutButton = new Button("", helpIcon);
    aboutButton.getStyleClass().add("vertical-button");
    aboutButton.setTooltip(new Tooltip("About"));
    aboutButton.setOnAction(event -> {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("About");
      alert.setHeaderText(null);
      alert.setContentText("UsefulEarnings is is a End of Degree Project developed to Universidad de Vigo");
      alert.showAndWait();
      event.consume();
    });
    ret.add(aboutButton);

    return ret;
  }



}

