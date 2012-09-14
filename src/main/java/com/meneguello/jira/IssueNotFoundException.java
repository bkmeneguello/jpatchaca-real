package com.meneguello.jira;

public class IssueNotFoundException extends Exception {

	private static final long serialVersionUID = -6332107352568875771L;

	public IssueNotFoundException(String key, Exception e) {
		super(key, e);
	}

}
