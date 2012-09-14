package com.meneguello.jira;

import java.util.BitSet;
import java.util.Date;

import javafx.util.Builder;

public class WorklogBuilder implements Builder<Worklog> {
	
	private static final int ID_ATTR = 0;

	private static final int AUTHOR_ATTR = 1;
	
	private static final int CREATED_ATTR = 2;

	private static final int START_DATE_ATTR = 3;

	private static final int TIME_SPENT_ATTR = 4;

	private static final int TIME_SPENT_IN_SECONDS_ATTR = 5;

	private static final int COMMENT_ATTR = 6;

	private static final int UPDATE_AUTHOR_ATTR = 7;

	private static final int UPDATED_ATTR = 8;

	private String id;
	
	private User author;
	
	private Date created;
	
	private Date startDate;
	
	private Interval timeSpent;
	
	private Long timeSpentInSeconds;
	
	private String comment;
	
	private User updateAuthor;
	
	private Date updated;
	
	private BitSet bitSet = new BitSet();

	public static WorklogBuilder create() {
		return new WorklogBuilder();
	}

	public static WorklogBuilder create(Worklog template) {
		final WorklogBuilder builder = new WorklogBuilder();
		if (template.getId() != null) {
			builder.id(template.getId());
		}
		if (template.getAuthor() != null) {
			builder.author(template.getAuthor());
		}
		if (template.getCreated() != null) {
			builder.created(template.getCreated());
		}
		if (template.getStartDate() != null) {
			builder.startDate(template.getStartDate());
		}
		if (template.getTimeSpent() != null) {
			builder.timeSpent(template.getTimeSpent());
		}
		if (template.getTimeSpentInSeconds() != null) {
			builder.timeSpentInSeconds(template.getTimeSpentInSeconds());
		}
		if (template.getComment() != null) {
			builder.comment(template.getComment());
		}
		if (template.getUpdateAuthor() != null) {
			builder.updateAuthor(template.getUpdateAuthor());
		}
		if (template.getUpdated() != null) {
			builder.updated(template.getUpdated());
		}
		return builder;
	}
	
	private WorklogBuilder() {
		
	}

	public WorklogBuilder id(String id) {
		this.id = id;
		bitSet.set(ID_ATTR);
		return this;
	}

	public WorklogBuilder author(User author) {
		this.author = author;
		bitSet.set(AUTHOR_ATTR);
		return this;
	}

	public WorklogBuilder created(Date created) {
		this.created = (Date) created.clone();
		bitSet.set(CREATED_ATTR);
		return this;
	}

	public WorklogBuilder startDate(Date startDate) {
		this.startDate = (Date) startDate.clone();
		bitSet.set(START_DATE_ATTR);
		return this;
	}

	public WorklogBuilder timeSpent(Interval interval) {
		this.timeSpent = interval;
		bitSet.set(TIME_SPENT_ATTR);
		return this;
	}

	public WorklogBuilder timeSpentInSeconds(Long timeSpentInSeconds) {
		this.timeSpentInSeconds = timeSpentInSeconds;
		bitSet.set(TIME_SPENT_IN_SECONDS_ATTR);
		return this;
	}

	public WorklogBuilder comment(String comment) {
		this.comment = comment;
		bitSet.set(COMMENT_ATTR);
		return this;
	}

	public WorklogBuilder updateAuthor(User updateAuthor) {
		this.updateAuthor = updateAuthor;
		bitSet.set(UPDATE_AUTHOR_ATTR);
		return this;
	}

	public WorklogBuilder updated(Date updated) {
		this.updated = (Date) updated.clone();
		bitSet.set(UPDATED_ATTR);
		return this;
	}	

	@Override
	public Worklog build() {
		final Worklog worklog = new Worklog();
		if(bitSet.get(ID_ATTR)) {
			worklog.setId(id);
		}
		if (bitSet.get(AUTHOR_ATTR)) {
			worklog.setAuthor(author);
		}        
		if (bitSet.get(CREATED_ATTR)) {
			worklog.setCreated(created);
		}		                          
		if (bitSet.get(START_DATE_ATTR)) {
			worklog.setStartDate(startDate);
		}		                          
		if (bitSet.get(TIME_SPENT_ATTR)) {
			worklog.setTimeSpent(timeSpent);
		}		                          
		if (bitSet.get(TIME_SPENT_IN_SECONDS_ATTR)) {
			worklog.setTimeSpentInSeconds(timeSpentInSeconds);
		}		                          
		if (bitSet.get(COMMENT_ATTR)) {
			worklog.setComment(comment);
		}		                          
		if (bitSet.get(UPDATE_AUTHOR_ATTR)) {
			worklog.setUpdateAuthor(updateAuthor);
		}		                          
		if (bitSet.get(UPDATED_ATTR)) {
			worklog.setUpdated(updated);
		}		
		return worklog;
	}

}
