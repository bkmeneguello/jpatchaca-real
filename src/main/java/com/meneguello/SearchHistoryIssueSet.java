package com.meneguello;

import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.meneguello.jira.Issue;

class SearchHistoryIssueSet implements IssueSet {
	
	private final ObservableList<Issue> issueList = FXCollections.observableArrayList();
	
	private final ReadOnlyListWrapper<Issue> issues = new ReadOnlyListWrapper<>(issueList);

	private final ResourceBundle resources;
	
	public SearchHistoryIssueSet(ResourceBundle resources) {
		this.resources = resources;
	}

	@Override
	public void appendTo(final ObservableList<IssueSet> issueSets) {
		if (!issues.isEmpty()) {
			issueSets.add(this);
		}
	}
	
	@Override
	public ObservableList<Issue> getIssues() {
		return FXCollections.unmodifiableObservableList(issues);
	}
	
	@Override
	public ReadOnlyListProperty<Issue> issuesProperty() {
		return issues.getReadOnlyProperty();
	}

	@RunLater
	public void append(Issue issue) {
		issues.add(0, issue);
	}
	
	@Override
	public String toString() {
		return resources.getString("filters.search-history");
	}

}