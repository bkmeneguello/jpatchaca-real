package com.meneguello;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskExecutor {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskExecutor.class);

	private static TaskExecutor instance;
	
	private final ExecutorService executorService;
	
	private Map<String, ListProperty<Task<?>>> queues = new HashMap<>();

	public static TaskExecutor getInstance() {
		if (instance == null) {
			instance = new TaskExecutor();
		}
		
		return instance;
	}
	
	private TaskExecutor() {
		executorService = Executors.newCachedThreadPool(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable paramRunnable) {
				Thread thread = new Thread(paramRunnable);
				thread.setDaemon(true);
				return thread;
			}
		});
	}
	
	public <T> Task<T> execute(Task<T> task) {
		logger.info("Executing task " + task);
		executorService.execute(task);
		return task;
	}

	public <T> Task<T> execute(Task<T> task, String queue) {
		logger.info("Executing task " + task);
		addToQueue(task, queue);
		executorService.execute(task);
		return task;
	}

	private <T> void addToQueue(final Task<T> task, final String queue) {
		getQueue(queue).add(task);
		task.stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> paramObservableValue, State oldValue, State newValue) {
				switch (newValue) {
				case SUCCEEDED:
				case FAILED:
				case CANCELLED:
					getQueue(queue).remove(task);
					break;
				default:
					break;
				}
			}
		});
		logger.info("Added to queue " + queue + " task " + task);
	}

	ListProperty<Task<?>> getQueue(String queue) {
		synchronized (queues) {
			ListProperty<Task<?>> list = queues.get(queue);
			if (list == null) {
				final ObservableList<Task<?>> backList = FXCollections.observableArrayList();
				list = new SimpleListProperty<>(backList);
				queues.put(queue, list);
			}
			return list;
		}
	}
	
	public Task<Void> execute(final Runnable runnable) {
		logger.info("Executing runnable " + runnable);
		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				runnable.run();
				return null;
			}
		};
		executorService.execute(task);
		return task;
	}
	
	public <T> Task<T> execute(final Callable<T> callable) {
		logger.info("Executing callable " + callable);
		final Task<T> task = new Task<T>() {
			@Override
			protected T call() throws Exception {
				return callable.call();
			}
		};
		executorService.execute(task);
		return task;
	}
}
