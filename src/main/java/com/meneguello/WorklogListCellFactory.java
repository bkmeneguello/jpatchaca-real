package com.meneguello;

import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import com.meneguello.jira.Issue;
import com.meneguello.jira.Worklog;

class WorklogListCellFactory implements Callback<ListView<Worklog>, ListCell<Worklog>> {
	
	private ResourceBundle resources;
	
	private ReadOnlyObjectProperty<Issue> selectedIssue;
	
	public static WorklogListCellFactory create(ResourceBundle resources, ReadOnlyObjectProperty<Issue> selectedIssue) {
		return new WorklogListCellFactory(resources, selectedIssue);
	}
	
	private WorklogListCellFactory(ResourceBundle resources, ReadOnlyObjectProperty<Issue> selectedIssue) {
		this.resources = resources;
		this.selectedIssue = selectedIssue;
	}
	
	@Override
	public ListCell<Worklog> call(ListView<Worklog> listView) {
		return new WorklogListCell(resources, selectedIssue);
	}
	
}