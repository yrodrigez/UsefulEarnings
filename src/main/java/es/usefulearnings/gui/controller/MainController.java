package es.usefulearnings.gui.controller;

import es.usefulearnings.gui.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

  @FXML
  private BorderPane mainPane;
  @FXML
  private VBox verticalMenu;
  @FXML
  private VBox verticalOptions;
  @FXML
  private HBox topMenu;

  private boolean isMaximized;
  /** this class will save boundaries for later use whe windowRestore **/
  private class Boundary{private double x, y, height, width;}
  private Boundary boundary;
  private Button maximizeRestore;
  private ImageView maximizeIcon;
  private ImageView restoreIcon;


  public void initialize(URL location, ResourceBundle resources) {
    isMaximized = false;
    boundary = new Boundary();
    setupTopMenu();

    verticalOptions.setSpacing(10);
    verticalOptions.getChildren().addAll(setupOptions());
    verticalMenu.setSpacing(10);
    verticalMenu.getChildren().addAll(setupSideMenu());
  }

  private void setupTopMenu() {
    setTopPaneGesture();

    ImageView minimizeIcon = new ImageView(new Image(Main.class.getResourceAsStream("icons/window-minimize.png")));
    Button minimize = new Button("", minimizeIcon);
    minimize.getStyleClass().addAll("main-controller-button", "no-radius");
    minimize.setOnAction(event -> {
      ((Stage)mainPane.getScene().getWindow()).setIconified(true);
      event.consume();
    });
    topMenu.getChildren().add(minimize);

    maximizeIcon = new ImageView(new Image(Main.class.getResourceAsStream("icons/window-maximize.png")));
    restoreIcon = new ImageView(new Image(Main.class.getResourceAsStream("icons/window-restore.png")));
    maximizeRestore = new Button("", maximizeIcon);
    maximizeRestore.getStyleClass().addAll("main-controller-button", "no-radius");
    maximizeRestore.setOnAction(event -> {
      if(!isMaximized){
        maximize();
      } else {
        windowRestore();
      }
      event.consume();
    });
    topMenu.getChildren().add(maximizeRestore);

    ImageView closeIcon = new ImageView(new Image(Main.class.getResourceAsStream("icons/window-close.png")));
    Button closeButton = new Button("", closeIcon);
    closeButton.getStyleClass().addAll("main-controller-button", "close-button", "no-radius");
    closeButton.setOnAction(event -> {
      Stage stage = (Stage) closeButton.getScene().getWindow();
      stage.close();
      event.consume();
    });
    topMenu.getChildren().add(closeButton);

    topMenu.setAlignment(Pos.TOP_RIGHT);

  }

  private void setTopPaneGesture() {
    class Delta {
      private double x, y;
    }
    final Delta delta = new Delta();
    topMenu.setOnMousePressed(event -> {
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        if (event.getClickCount() == 1) {
          delta.x = event.getSceneX();
          delta.y = event.getSceneY();
          event.consume();
        }
      }
    });
    topMenu.setOnMouseDragged(event -> {
      if (event.getButton().equals(MouseButton.PRIMARY)) {
        if(isMaximized) windowRestore(); // user is moving window so better windowRestore

        topMenu.getScene().getWindow().setX(event.getScreenX() - delta.x);
        topMenu.getScene().getWindow().setY(event.getScreenY() - delta.y);
        event.consume();
      }
    });

    topMenu.setOnMouseClicked(event -> {
      if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
        if(!isMaximized) {
          maximize();
        } else {
          windowRestore();
        }
      }
    });
  }

  /**
   * saves the current boundaries on it's boundary class
   * and set the width of the window to the full screen view and sets x and x to the upper conner
   */
  private void maximize(){
    this.boundary.height = mainPane.getHeight();
    this.boundary.width = mainPane.getWidth();
    this.boundary.x = mainPane.getScene().getWindow().getX();
    this.boundary.y = mainPane.getScene().getWindow().getY();
    mainPane.getScene().getWindow().setWidth(Screen.getPrimary().getVisualBounds().getWidth());
    mainPane.getScene().getWindow().setHeight(Screen.getPrimary().getVisualBounds().getHeight());
    mainPane.getScene().getWindow().setX(Screen.getPrimary().getVisualBounds().getMinX());
    mainPane.getScene().getWindow().setY(Screen.getPrimary().getVisualBounds().getMinY());

    maximizeRestore.setGraphic(restoreIcon);
    isMaximized = true;
  }

  /**
   * Takes the last boundaries of the window a sets it to the main pane so it seem like is minimized
   */
  private void windowRestore(){
    mainPane.getScene().getWindow().setWidth(this.boundary.width);
    mainPane.getScene().getWindow().setHeight(this.boundary.height);
    mainPane.getScene().getWindow().setX(this.boundary.x);
    mainPane.getScene().getWindow().setY(this.boundary.y);

    maximizeRestore.setGraphic(maximizeIcon);
    isMaximized = false;
  }

  void setVista(Node vista) {
    mainPane.setCenter(vista);
  }

  private ArrayList<Button> setupSideMenu() {
    ArrayList<Button> ret = new ArrayList<>();
    ImageView navigateIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/navigate-white.png")
    ));

    Button navigateButton = new Button("", navigateIcon);
    navigateButton.getStyleClass().add("main-controller-button");
    navigateButton.setTooltip(new Tooltip("Navigate"));
    navigateButton.getStyleClass().add("main-controller-button");
    navigateButton.setOnAction(event -> {
      VistaNavigator.getInstance().loadVista(VistaNavigator.NAVIGATE);
      event.consume();
    });
    ret.add(navigateButton);

    ImageView filterIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/filter-white.png")
    ));
    Button filterButton = new Button("", filterIcon);
    filterButton.getStyleClass().add("main-controller-button");
    filterButton.setTooltip(new Tooltip("Filter"));
    filterButton.setOnAction(event -> {
      VistaNavigator.getInstance().loadVista(VistaNavigator.FILTER);
      event.consume();
    });
    ret.add(filterButton);

    ImageView downloadIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/download-white.png")
    ));
    Button downloadButton = new Button("", downloadIcon);
    downloadButton.getStyleClass().add("main-controller-button");
    downloadButton.setTooltip(new Tooltip("Download"));
    downloadButton.setOnAction(event -> {
      VistaNavigator.getInstance().loadVista(VistaNavigator.DOWNLOAD);
      event.consume();
    });
    ret.add(downloadButton);

    ImageView historyIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/history-white.png")
    ));
    Button historyButton = new Button("", historyIcon);
    historyButton.getStyleClass().add("main-controller-button");
    historyButton.setTooltip(new Tooltip("History"));
    historyButton.setOnAction(event -> {
      VistaNavigator.getInstance().loadVista(VistaNavigator.HISTORY);
      event.consume();
    });
    ret.add(historyButton);

    return ret;
  }

  private ArrayList<Button> setupOptions() {
    ArrayList<Button> ret = new ArrayList<>();
    ImageView optionsIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/gear-white.png")
    ));
    Button optionsButton = new Button("", optionsIcon);
    optionsButton.getStyleClass().add("main-controller-button");
    optionsButton.setTooltip(new Tooltip("Options"));
    optionsButton.setOnAction(event -> {
      VistaNavigator.getInstance().loadVista(VistaNavigator.OPTIONS);
      event.consume();
    });
    ret.add(optionsButton);

    ImageView helpIcon = new ImageView(new Image(
      Main.class.getResourceAsStream("icons/info-white.png")
    ));
    Button aboutButton = new Button("", helpIcon);
    aboutButton.getStyleClass().add("main-controller-button");
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

