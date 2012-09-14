package com.meneguello;

import java.util.List;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.meneguello.jira.Filter;
import com.meneguello.jira.Issue;
import com.meneguello.jira.Jira;

class UserFilterIssueSet implements IssueSet {

	private final Filter filter;
	
	private ObservableList<Issue> issueList = FXCollections.observableArrayList();
	
	private final ReadOnlyListWrapper<Issue> issues = new ReadOnlyListWrapper<>(issueList);
	
	private boolean loaded;

	public UserFilterIssueSet(Filter filter) {
		this.filter = filter;
	}

	@Override
	public void appendTo(ObservableList<IssueSet> issueSets) {
		issueSets.add(this);
	}
	
	@Override
	public ObservableList<Issue> getIssues() {
		if (!loaded) {
			loaded = true;
			load();
		}
		return FXCollections.unmodifiableObservableList(issues);
	}
	
	@Async(queue = "issueList")
	public void load() {
		updateIssueList(Jira.getInstance().findFilterIssues(filter));
	}

	@RunLater
	private void updateIssueList(List<Issue> remoteIssues) {
		issues.addAll(remoteIssues);
	}
	
	@Override
	public ReadOnlyListProperty<Issue> issuesProperty() {
		return issues.getReadOnlyProperty();
	}
	
	@Override
	public String toString() {
		return filter.getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
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
		UserFilterIssueSet other = (UserFilterIssueSet) obj;
		if (filter == null) {
			if (other.filter != null)
				return false;
		} else if (!filter.equals(other.filter))
			return false;
		return true;
	}

}