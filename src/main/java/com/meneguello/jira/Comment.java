package com.meneguello.jira;

import java.util.Calendar;

public class Comment {

	private String id;
	
	private User author;
	
	private Calendar created;
	
	private User updateAuthor;
	
	private Calendar updated;
	
	private String body;

	void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	void setAuthor(User user) {
		this.author = user;
	}
	
	public User getAuthor() {
		return author;
	}

	void setCreated(Calendar created) {
		this.created = created;
	}
	
	public Calendar getCreated() {
		return created;
	}

	void setUpdateAuthor(User updateAuthor) {
		this.updateAuthor = updateAuthor;
	}
	
	public User getUpdateAuthor() {
		return updateAuthor;
	}

	void setUpdated(Calendar updated) {
		this.updated = updated;
	}
	
	public Calendar getUpdated() {
		return updated;
	}

	void setBody(String body) {
		this.body = body;
	}
	
	public String getBody() {
		return body;
	}

}
