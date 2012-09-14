package com.meneguello;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;

import com.meneguello.jira.Issue;

interface IssueSet {

	void appendTo(ObservableList<IssueSet> issueSets);
	
	ObservableList<Issue> getIssues();
	
	ReadOnlyListProperty<Issue> issuesProperty();

}