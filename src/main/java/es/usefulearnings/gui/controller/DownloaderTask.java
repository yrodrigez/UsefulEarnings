package es.usefulearnings.gui.controller;

import es.usefulearnings.engine.connection.DownloadProcess;
import es.usefulearnings.engine.connection.ProcessHandler;
import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.entities.Entity;
import es.usefulearnings.gui.view.AlertHelper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class DownloaderTask extends Task<Void> {
	private DownloadProcess process;
	private final String name;

	private final DownloadController controller;
	DownloaderTask(
		final DownloadController downloadController,
		final ArrayList<Plugin> plugins,
		final List<Entity> entities,
		final OverallProgressBarHandler progressBarHandler,
		final String name
	) {
		this.controller = downloadController;
		this.name = name;
		final ProcessHandler handler = new ProcessHandler() {
			@Override
			public void updateProgress(int workDone, int remaining) {
				DownloaderTask.this.updateProgress(workDone, remaining);
				progressBarHandler.updateProgress();
			}

			@Override
			public void updateMessage(final String message) {
				DownloaderTask.this.updateMessage(message);
				Platform.runLater(() -> controller.debuggerBox.getChildren().add(0, new Text(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " | " + message)));
			}

			@Override
			public void onCancelled() {
				downloadController.downloadCompleted();
				cancel();
			}

			@Override
			public void onError(Throwable err) {
				Platform.runLater(() -> AlertHelper.showExceptionAlert(process.getError()));
				downloadController.downloadCompleted();
				failed();
			}

			@Override
			public void onSuccess() {
				updateMessage("Saving data");
				downloadController.downloadCompleted();
				updateMessage("Work done!");
				succeeded();
			}
		};

		process = new DownloadProcess(handler, plugins, entities);
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
		controller.downloadCompleted();
		this.process.stop();
	}

	public String getName() {
		return this.name;
	}
}
