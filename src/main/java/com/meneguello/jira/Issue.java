package com.meneguello.jira;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;


public class Issue {
	
	private StringProperty key = new SimpleStringProperty();
	
	private StringProperty summary = new SimpleStringProperty();
	
	private StringProperty description = new SimpleStringProperty();

	private ObjectProperty<Image> icon = new SimpleObjectProperty<>();
	
	Issue() {
		
	}
	
	void setKey(String key) {
		this.key.set(key);
	}

	public String getKey() {
		return key.get();
	}
	
	public StringProperty keyProperty() {
		return key;
	}
	
	void setSummary(String summary) {
		this.summary.set(summary);
	}
	
	public String getSummary() {
		return summary.get();
	}
	
	public StringProperty summaryProperty() {
		return summary;
	}

	void setDescription(String description) {
		this.description.set(description);
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public StringProperty descriptionProperty() {
		return description;
	}

	void setIcon(Image icon) {
		this.icon.set(icon);
	}
	
	public Image getIcon() {
		return icon.get();
	}
	
	public ObjectProperty<Image> iconProperty() {
		return icon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issue other = (Issue) obj;
		if (key.get() == null) {
			if (other.key.get() != null)
				return false;
		} else if (!key.get().equals(other.key.get()))
			return false;
		return true;
	}
	
}
