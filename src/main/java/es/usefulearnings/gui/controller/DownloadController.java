package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.SearchEngine;
import es.usefulearnings.engine.connection.DownloaderTask;
import es.usefulearnings.entities.Company;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Yago on 04/09/2016.
 */
public class DownloadController implements Initializable {
  @FXML
  VBox vBox;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    vBox.setStyle("-fx-background-color: white");
    vBox.setPrefSize(1024 - 35, 768 - 35);
    ArrayList<Company> companies = new ArrayList<>();
    Button down = new Button("DOWNLOAD SHIT!");
    Button stopThatShit = new Button("STOP THAT SHIT");

    vBox.getChildren().add(new HBox(down, stopThatShit));
    VBox scrollable = new VBox();

    ScrollPane scrollPane = new ScrollPane(scrollable);
    vBox.getChildren().add(scrollPane);

    down.setOnAction(event -> {
      SearchEngine.getInstance().getAllCompaniesData();
      for (
        DownloaderTask<Company> task :
        SearchEngine.getInstance().getCompaniesDownloaders()
        ) {
        Label label = new Label();
        label.textProperty().bind(task.messageProperty());
        scrollable.getChildren().add(label);

        ProgressBar bar = new ProgressBar();
        bar.setPrefWidth(500);
        bar.progressProperty().bind(task.progressProperty());
        scrollable.getChildren().add(bar);

        down.setDisable(true);
        stopThatShit.setDisable(false);
        event.consume();
      }
    });

    stopThatShit.setDisable(true);
    stopThatShit.setOnAction(event -> {
      SearchEngine.getInstance().getCompaniesDownloaders().forEach(DownloaderTask::stop);
      down.setDisable(false);
      stopThatShit.setDisable(true);
      event.consume();
    });



    List<String> newSymbols = new LinkedList<>();
    companies.forEach(company -> newSymbols.add(company.getSymbol()));

    ListView<String> lv = new ListView<>();

    lv.setItems(FXCollections.observableArrayList(newSymbols));

    scrollable.getChildren().add(lv);
  }
}
