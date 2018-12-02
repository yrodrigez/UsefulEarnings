package es.usefulearnings.gui.controller;

import javafx.application.Platform;
import javafx.scene.paint.Color;

class OverallProgressBarHandler {
	private DownloadController downloadController;
	private final double max;
	private double current;

	OverallProgressBarHandler(DownloadController downloadController, final int max) {
		this.downloadController = downloadController;
		this.max = max;
		this.current = 0.0;
	}

	synchronized void updateProgress(){
		current++;
		this.updateBar();
	}

	private synchronized void updateBar(){
		Platform.runLater(() -> {
			double progress = (current/max)*100;
			int width =  (int)((downloadController.overallProgressBarContainer.getWidth()/100) * (progress));

			downloadController.overAllProgressbar.setWidth(width);
			if(width == downloadController.overallProgressBarContainer.getWidth())
				downloadController.overAllProgressbar.setFill(Color.GREEN);
		});
	}
}
