package com.meneguello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.commons.lang.StringUtils;

import com.meneguello.jira.Issue;
import com.meneguello.jira.IssueNotFoundException;
import com.meneguello.jira.Jira;

public class FavoriteIssueSet implements IssueSet {
	
	private final ObservableList<Issue> issueList = FXCollections.observableArrayList();
	
	private final ReadOnlyListWrapper<Issue> issues = new ReadOnlyListWrapper<>(issueList);

	private final ResourceBundle resources;
	
	public FavoriteIssueSet(ResourceBundle resources) {
		this.resources = resources;
		
		final String favorites = Preferences.userRoot().get("favorites", "");
		if (StringUtils.isNotEmpty(favorites)) {
			final List<String> favoritesList = Arrays.asList(favorites.split(";"));
			for (String issueKey : favoritesList) {
				load(issueKey);
			}
		}
	}

	private void load(String issueKey) {
		try {
			_append(Jira.getInstance().findIssue(issueKey));
		} catch (IssueNotFoundException e) {
			// TODO:Notify
			e.printStackTrace();
		}
	}

	@RunLater
	private void _append(Issue findIssue) {
		issues.add(findIssue);
	}

	@Override
	public void appendTo(ObservableList<IssueSet> issueSets) {
		if (!issues.isEmpty()) {
			issueSets.add(this);
		}
	}
	
	@Override
	public ObservableList<Issue> getIssues() {
		return FXCollections.unmodifiableObservableList(issues);
	}

	public void append(Issue issue) {
		issues.add(0, issue);
		updateState();
	}

	public void remove(Issue issue) {
		issues.remove(issue);
		updateState();
	}
	
	private void updateState() {
		List<String> issueKeys = new ArrayList<>(issues.size());
		for (Issue issue : issues) {
			issueKeys.add(issue.getKey());
		}
		
		Preferences.userRoot().put("favorites", StringUtils.join(issueKeys, ";"));
	}

	@Override
	public ReadOnlyListProperty<Issue> issuesProperty() {
		return issues.getReadOnlyProperty();
	}
	
	@Override
	public String toString() {
		return resources.getString("filters.favorite-issues");
	}

}
