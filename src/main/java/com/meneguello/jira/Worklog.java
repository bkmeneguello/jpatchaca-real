package com.meneguello.jira;

import java.util.Date;


public class Worklog {

	private String id;

	private User author;
	
	private Date created;
	
	private Date startDate;
	
	private Interval timeSpent;
	
	private Long timeSpentInSeconds;

	private String comment;

	private User updateAuthor;

	private Date updated;

	void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	void setAuthor(User author) {
		this.author = author;
	}
	
	public User getAuthor() {
		return author;
	}

	void setCreated(Date created) {
		this.created = created;
	}
	
	public Date getCreated() {
		return created;
	}

	void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	void setTimeSpent(Interval timeSpent) {
		this.timeSpent = timeSpent;
	}
	
	public Interval getTimeSpent() {
		return timeSpent;
	}

	void setTimeSpentInSeconds(Long timeSpentInSeconds) {
		this.timeSpentInSeconds = timeSpentInSeconds;
	}
	
	public Long getTimeSpentInSeconds() {
		return timeSpentInSeconds;
	}

	void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return comment;
	}

	void setUpdateAuthor(User updateAuthor) {
		this.updateAuthor = updateAuthor;
	}
	
	public User getUpdateAuthor() {
		return updateAuthor;
	}

	void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	public Date getUpdated() {
		return updated;
	}

}
