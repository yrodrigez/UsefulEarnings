package es.usefulearnings.gui.controller;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

class OverallProgressBarHandler {
  private final DownloadController downloadController;
  private final double max;
  private double current;
  private final Label label;

  OverallProgressBarHandler(final DownloadController downloadController, final Label label, final int max) {
    this.downloadController = downloadController;
    this.max = max;
    this.current = 0.0;
    this.label = label;
    this.label.setText("0%");
  }

  synchronized void updateProgress() {
    current++;
    this.updateBar();
  }

  private synchronized void updateBar() {
    Platform.runLater(() -> {
      final double progress = (current / max) * 100;
      final int width = (int) ((downloadController.overallProgressBarContainer.getWidth() / 100) * (progress));
      this.label.setText((int)progress + "%");
      downloadController.overAllProgressbar.setWidth(width);
      if (width == downloadController.overallProgressBarContainer.getWidth())
        downloadController.overAllProgressbar.setFill(Color.GREEN);
    });
  }
}
