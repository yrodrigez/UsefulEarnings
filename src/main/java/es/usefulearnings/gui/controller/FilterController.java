package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.connection.DownloadProcess;
import es.usefulearnings.engine.connection.ProcessHandler;
import es.usefulearnings.engine.connection.YahooFinanceAPI;
import es.usefulearnings.engine.filter.Filter;
import es.usefulearnings.engine.plugin.HistoricalDataPlugin;
import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Entity;
import es.usefulearnings.entities.company.HistoricalData;
import es.usefulearnings.gui.Main;
import es.usefulearnings.gui.animation.OverWatchLoader;
import es.usefulearnings.gui.view.AlertHelper;
import es.usefulearnings.gui.view.CompanyViewHelper;
import es.usefulearnings.gui.view.FilterView;
import es.usefulearnings.gui.view.FilterViewHelper;
import es.usefulearnings.utils.CSVWriter;
import es.usefulearnings.utils.NoStocksFoundException;
import es.usefulearnings.utils.ResourcesHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.Notifications;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Yago on 04/09/2016.
 */
public class FilterController implements Initializable {
  public BorderPane mainPane;
  private ListView<Filter> filterListView;
  public BorderPane rightPane;
  private FilterView companyFilterView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    filterListView = new ListView<>();
    filterListView.prefHeightProperty().bind(mainPane.heightProperty());

    mainPane.setCenter(new OverWatchLoader(Color.web("#400090")).getLoader());
    BorderPane centerPane = new BorderPane();

    new Thread(() -> {
      try {
        companyFilterView = CompanyViewHelper.getInstance().getFilterableView();
        Platform.runLater(() -> {
          Node filterView = companyFilterView.getView();
          centerPane.setCenter(filterView);
          mainPane.setCenter(centerPane);
          Button filterButton = new Button("Filter");
          filterButton.getStyleClass().addAll("main-controller-button");
          HBox hBox = new HBox(filterButton);
          hBox.setAlignment(Pos.BASELINE_RIGHT);
          centerPane.setBottom(hBox);
          refreshFilterPane();
          filterButton.setOnAction(getFilterEvent(filterButton));
        });
      } catch (IntrospectionException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
        Platform.runLater(() -> AlertHelper.showExceptionAlert(e));
        e.printStackTrace();
      }
    }).start();

  }

  @SuppressWarnings("unchecked")
  private void refreshFilterPane() {
    if (Core.getInstance().getAppliedFilters().size() > 0) {
      rightPane.setCenter(filterListView);
      List<Filter> appliedFilters = Core.getInstance().getAppliedFilters();
      filterListView.setItems(FXCollections.observableArrayList(appliedFilters));
      filterListView.setCellFactory(param -> {
        ListCell<Filter> filterListCell = new ListCell<>();

        ContextMenu filterContextMenu = new ContextMenu();
        MenuItem export = new MenuItem("Export to Excel", new ImageView(new Image(Main.class.getResourceAsStream("icons/export.png"), 12, 12, false, false)));
        export.setOnAction(event -> {
          try {
            String dateString = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date(filterListCell.getItem().getFilteredDate() * 1000L));

            CSVWriter writer = new CSVWriter(
              ResourcesHelper.getInstance().getExportedDataPath() + File.separator + "exported at " + dateString,
              new ArrayList<>(filterListCell.getItem().getSelected())
            );
            writer.save();
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "File exported successfully at (" + dateString + ")");
          } catch (InvocationTargetException | NoStocksFoundException | IllegalAccessException | InstantiationException | IntrospectionException | IOException e) {
            e.printStackTrace();
          }
          event.consume();
        });
        MenuItem details = new MenuItem("Details", new ImageView(new Image(Main.class.getResourceAsStream("icons/export.png"), 12, 12, false, false)));
        details.setOnAction(event -> FilterViewHelper.getInstance().showOnWindow(filterListCell.getItem()));

        MenuItem historicalPrices = new MenuItem("Get Historical Prices");
        historicalPrices.setOnAction(event -> showDialogForStartAndEndDates(filterListCell.getItem()));

        filterContextMenu.getItems().addAll(export, details, historicalPrices);
        filterListCell.setContextMenu(filterContextMenu);

        // recover the *damn* text
        filterListCell.itemProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            filterListCell.textProperty().bind(filterListCell.itemProperty().asString());
          }
        });

        return filterListCell;
      });
    }
  }

  private void showDialogForStartAndEndDates(Filter filter) {
    Stage dialogStage = new Stage();
    dialogStage.setTitle(filter.toString());
    dialogStage.initModality(Modality.WINDOW_MODAL);

    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(Main.class.getResource("fxml/start_end_date_picker.fxml"));
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    DatesPickerController controller = fxmlLoader.getController();
    controller.send.setOnAction(event -> {
      event.consume();
      if(controller.validateDates()) {
        dialogStage.close();
        createHistoricalPrices(
          filter,
          controller.startDate.getValue().toEpochDay() * 86400L,
          controller.endDate.getValue().toEpochDay() * 86400L,
          controller.ranges.getValue()
        );
      } else {
        Notifications
          .create()
          .title("Error")
          .text("Start Date must be before End Date!")
        .showError();
      }
    });

    dialogStage.setScene(new Scene(fxmlLoader.getRoot()));
    dialogStage.initStyle(StageStyle.UNDECORATED);
    dialogStage.initOwner(rightPane.getScene().getWindow());
    dialogStage.show();
  }

  @SuppressWarnings("unchecked")
  private void createHistoricalPrices(Filter filter, long startDate, long endDate, YahooFinanceAPI.Range range) {
    Stage dialogStage = new Stage();
    dialogStage.setTitle(filter.toString());
    dialogStage.initModality(Modality.WINDOW_MODAL);

    BorderPane borderPane = new BorderPane();
    borderPane.setPrefSize(989 * .75, 733 * .75);
    borderPane.setStyle("-fx-border-color: rgba(125,125,125,0.93); -fx-border-width: 1;");
    Scene scene = new Scene(borderPane);
    dialogStage.setScene(scene);
    dialogStage.initStyle(StageStyle.UNDECORATED);
    dialogStage.initOwner(rightPane.getScene().getWindow());
    dialogStage.show();

    new Thread(() -> {
      VBox vbox = new VBox(new OverWatchLoader(10.0, Color.web("#400090")).getLoader());
      vbox.setAlignment(Pos.CENTER);
      Platform.runLater(() -> borderPane.setCenter(vbox));
      Label label = new Label("Creating tasks");
      Platform.runLater(() -> vbox.getChildren().add(label));

      final int totalCompanies = filter.getSelected().size();
      final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2 < totalCompanies ?
        Runtime.getRuntime().availableProcessors() * 2 : totalCompanies;

      Collection<HistoricalDataTask> tasks = new ConcurrentLinkedQueue<>();
      int split = totalCompanies / MAX_THREADS;

      String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

      File folderToSave = null;
      try {
        folderToSave = new File(ResourcesHelper.getInstance().getExportedDataPath() +
          File.separator + "HistoricalData " + date);
      } catch (NoStocksFoundException e) {
        e.printStackTrace();
      }
      int i = 0;
      while ( folderToSave != null && folderToSave.exists()){
        if( !new File(folderToSave.getPath() + "(" + ++i + ")").exists()) {
          folderToSave = new File(folderToSave.getPath() + "(" + i + ")");
        }
        System.out.println(folderToSave.getPath());
      }
      if(folderToSave != null) folderToSave.mkdirs();


      for (i = 0; i < MAX_THREADS; i++) {
        ArrayList<Plugin> plugins = new ArrayList<>();
        plugins.add(new HistoricalDataPlugin(startDate, endDate, range));


        int from = i * split;
        int to = from + split;
        if (i == MAX_THREADS - 1) to = totalCompanies;
        tasks.add(
          new HistoricalDataTask(
            plugins,
            new LinkedList<>(filter.getSelected()).subList(from, to),
            tasks,
            dialogStage,
            folderToSave
          )
        );
      }

      Platform.runLater(() -> label.setText("Initializing downloads..."));
      VBox vboxForBars = new VBox();
      ScrollPane scrollPaneForBars = new ScrollPane(vboxForBars);
      scrollPaneForBars.setMaxSize(330, borderPane.getPrefHeight() * 0.75);
      scrollPaneForBars.setStyle("-fx-background-color:transparent;");
      for (HistoricalDataTask task : tasks) {
        new Thread(task).start();
        Platform.runLater(() -> vboxForBars.getChildren().add(task.getSkin()));
      }
      Platform.runLater(() -> {
        label.setText("Downloading....");
        vbox.getChildren().add(scrollPaneForBars);
        vboxForBars.setAlignment(Pos.CENTER);
      });

    }).start();
  }

  private class HistoricalDataTask extends Task<Void> {
    private DownloadProcess process;
    private ProcessHandler handler;
    private Node _skin;

    HistoricalDataTask(ArrayList<Plugin> plugins, List<Entity> entities, Collection<HistoricalDataTask> tasks, Stage dialogStage, File folderToSave) {
      handler = new ProcessHandler() {
        @Override
        public void updateProgress(int workDone, int remaining) {
          HistoricalDataTask.this.updateProgress(workDone, remaining);
        }

        @Override
        public void updateMessage(String message) {
          HistoricalDataTask.this.updateMessage(message);
        }

        @Override
        public void onCancelled() {
          cancel();
        }

        @Override
        public void onError(Throwable err) {
          Platform.runLater(() -> AlertHelper.showExceptionAlert(process.getError()));
          failed();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onSuccess() {
          updateMessage("Exporting to csv");
          entities.forEach(entity ->
          {
            try {
              String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

              String path = folderToSave.getPath() + File.separator +
                ((Company) entity).getSymbol() + date;

              HistoricalDataTask.this.updateMessage("Exporting data from " + ((Company) entity).getSymbol());
              ArrayList<HistoricalData.Historical> historicalData = ((Company) entity).getHistoricalData().getHistoricalDatum();
              if(historicalData != null && !historicalData.isEmpty())
                new CSVWriter(path, historicalData).save();

              HistoricalDataTask.this.updateMessage("Finished the exportation");

            } catch (
                InvocationTargetException
                | IntrospectionException
                | IllegalAccessException
                | InstantiationException
                | IOException
                exception
              ) {
              exception.printStackTrace();
            }
          });
          updateMessage("Work done!");
          boolean canClose = true;
            for (HistoricalDataTask t : tasks) {
              if (!t.equals(HistoricalDataTask.this)) {
                canClose = t.isDone();
              }
            }

            if (canClose) {
              Platform.runLater(() -> {
                Notifications.create()
                  .title("Historical data Completed")
                  .text("Historical data download is completed an successfully exported at your folder")
                  .graphic(new ImageView(new Image(Main.class.getResourceAsStream("icons/ok-notification.png"), 48d, 48d, false, true)))
                  .position(Pos.BOTTOM_RIGHT)
                  .show();
                dialogStage.close();
              });
            }
          succeeded();
        }
      };

      process = new DownloadProcess(handler, plugins, entities);

      ProgressIndicator indicator = new ProgressIndicator();
      indicator.setPrefSize(35, 35);
      indicator.setStyle("-fx-accent: #400090;");
      indicator.progressProperty().bind(this.progressProperty());
      ProgressBar bar = new ProgressBar();
      bar.progressProperty().bind(indicator.progressProperty());
      bar.setPrefWidth(280);
      bar.styleProperty().bind(indicator.styleProperty());
      Label label = new Label();
      label.textProperty().bind(this.messageProperty());
      HBox hBox = new HBox(bar, indicator);
      hBox.setAlignment(Pos.CENTER);
      VBox skin = new VBox(label, hBox);
      skin.setAlignment(Pos.CENTER);
      _skin = skin;
    }

    @Override
    protected Void call() throws Exception {
      process.run();
      if (process.hasFailed()) {
        this.failed();
        super.failed();
        throw process.getError();
      }
      return null;
    }

    void stop() {
      this.process.stop();
    }

    Node getSkin() {
      return _skin;
    }
  }


  private EventHandler<ActionEvent> getFilterEvent(Button filterButton) {
    return event -> {
      new Thread(() -> {
        try {
          Platform.runLater(() -> {
            filterButton.setDisable(true);
            filterButton.setText("Filtering...");
          });

          Core.getInstance().applyFilter(companyFilterView.getFilterParams());

          Platform.runLater(() -> {
            refreshFilterPane();
            filterButton.setDisable(false);
            filterButton.setText("Filter");
          });
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
          Platform.runLater(() -> {
            AlertHelper.showExceptionAlert(e);
            filterButton.setDisable(false);
            filterButton.setText("Filter");
          });
          e.printStackTrace();
        }
      }).start();
      event.consume();
    };
  }

}
