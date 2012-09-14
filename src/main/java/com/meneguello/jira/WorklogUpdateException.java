package com.meneguello.jira;

public class WorklogUpdateException extends Exception {

	private static final long serialVersionUID = 5849246187878471778L;

	public WorklogUpdateException(Exception e) {
		super(e);
	}

}
