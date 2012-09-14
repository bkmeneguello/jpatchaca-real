package com.meneguello;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import com.meneguello.jira.Issue;

class IssueListCellFactory implements Callback<ListView<Issue>, ListCell<Issue>> {
	
	public static IssueListCellFactory create() {
		return new IssueListCellFactory();
	}
	
	private IssueListCellFactory() {
		
	}
	
	@Override
	public ListCell<Issue> call(ListView<Issue> listView) {
		return new IssueListCell();
	}

}