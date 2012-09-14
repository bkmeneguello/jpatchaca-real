package com.meneguello.jira;

import javafx.scene.image.Image;

public class IssueType {

	private String id;
	
	private String iconURL;
	
	private String description;
	
	private Image icon;
	
	public IssueType() {
		
	}

	void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
	public String getIconURL() {
		return iconURL;
	}

	void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	void setIcon(Image icon) {
		this.icon = icon;
	}
	
	public Image getIcon() {
		return icon;
	}

}
