package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.connection.YahooFinanceAPI;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * @author Yago on 27/11/2016.
 */
public class DatesPickerController implements Initializable {
  public BorderPane datesPicker;
  public DatePicker startDate;
  public DatePicker endDate;
  public ComboBox<YahooFinanceAPI.Range> ranges;
  public Button send;
  private final int DAYS_ON_A_YEAR = 365;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    startDate.setValue(LocalDate.ofEpochDay(LocalDate.now().toEpochDay() - DAYS_ON_A_YEAR));
    endDate.setValue(LocalDate.now());

    ranges.getItems().addAll(
      FXCollections.observableArrayList(YahooFinanceAPI.Range.values())
    );
    ranges.setValue(ranges.getItems().get(0));

  }

  boolean validateDates () {
    return startDate.getValue().isBefore(endDate.getValue()) && endDate.getValue().toEpochDay() - startDate.getValue().toEpochDay()  <= DAYS_ON_A_YEAR;
  }
}
