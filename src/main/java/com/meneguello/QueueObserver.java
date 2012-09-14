package com.meneguello;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QueueObserver {
	
	private final StringProperty queue = new SimpleStringProperty(this, "queue");
	
	private final ReadOnlyBooleanWrapper busy = new ReadOnlyBooleanWrapper(this, "busy");
	
	public void setQueue(String queue) {
		busy.bind(Bindings.not(TaskExecutor.getInstance().getQueue(queue).emptyProperty()));
		this.queue.set(queue);
	}
	
	public String getQueue() {
		return queue.get();
	}
	
	public StringProperty queueProperty() {
		return queue;
	}

	public ReadOnlyBooleanProperty busyProperty() {
		return busy.getReadOnlyProperty();
	}
	
}
