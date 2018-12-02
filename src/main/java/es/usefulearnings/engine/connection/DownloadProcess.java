package es.usefulearnings.engine.connection;

import es.usefulearnings.engine.Core;
import es.usefulearnings.engine.plugin.Plugin;
import es.usefulearnings.engine.plugin.PluginException;
import es.usefulearnings.entities.Company;
import es.usefulearnings.entities.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author yago.
 */
public class DownloadProcess extends Process implements Runnable {

	private static final String STOP_MESSAGE = "Stopped!";
	private static final String WORK_DONE_MESSAGE = "Work done!";
	private static final String CONNECTION_LOST_MESSAGE = "Connection lost...";
	private Exception err;

	private int workDone;
	private int remainingWork;

	private boolean stop;
	private boolean hasFailed;

	private ArrayList<Plugin> plugins;

	private Collection<Entity> entities;
	private List<Entity> _emptyEntities;

	public DownloadProcess(
		final ProcessHandler handler,
		final ArrayList<Plugin> plugins,
		final Collection<Entity> entities
	) {
		super(handler);
		this.plugins = plugins;
		this.entities = entities;

		workDone = 0;
		stop = hasFailed = false;
		remainingWork = this.entities.size();
		_emptyEntities = new ArrayList<>();
	}

	@Override
	public void run() {
		try {
			for (final Entity entity : entities) {
				if (stop) {
					this.onStopped();
					break;
				}
				for (final Plugin plugin : plugins) {
					if (stop) {
						this.onStopped();
						break;
					}
					try {
						plugin.addInfo(entity);
					} catch (PluginException e) {
						if (e.getCause().getClass().getName().startsWith("java.net")) {
							System.err.println("java.net exception: " + e.getCause().getClass() + ", message: " + e.getCause().getMessage());
							throw e;
						}
					}
				}
				this.onEntityFinish(entity);
			}// end foreach entity
		} catch (final PluginException err) {
			this.onFail(err);
		}
	}

	private void onFail(final PluginException err){
		this.hasFailed = true;
		System.err.println(CONNECTION_LOST_MESSAGE);
		this.err = err;
		updateMessage(CONNECTION_LOST_MESSAGE);
		onError(err);
		err.printStackTrace();
	}

	public void onSuccess() {
		if (!stop && !hasFailed) { // Success.
			Core.getInstance().removeEntities(_emptyEntities);
			updateMessage(WORK_DONE_MESSAGE);
			super.onSuccess();
		}
	}

	private void onEntityFinish(final Entity actualEntity) {
		if (actualEntity.isEmpty()) {
			_emptyEntities.add(actualEntity);
		}

		actualEntity.flush();
		updateProgress(++workDone, remainingWork);
		updateMessage(workDone + " out of " + remainingWork + "\tCurrent company: " + ((Company) actualEntity).getSymbol());
	}


	private void onStopped() {
		updateMessage(STOP_MESSAGE);
		onCancelled();
	}

	public void stop() {
		this.stop = true;
	}


	public boolean hasFailed() {
		return hasFailed;
	}

	public Exception getError() {
		return this.err;
	}

	public Collection<Entity> getEntities() {
		return entities;
	}

	public List<Entity> get_emptyEntities() {
		return _emptyEntities;
	}
}
