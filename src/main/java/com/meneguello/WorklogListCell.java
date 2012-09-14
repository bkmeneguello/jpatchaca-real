package com.meneguello;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListCell;

import com.meneguello.jira.Issue;
import com.meneguello.jira.Worklog;

class WorklogListCell extends ListCell<Worklog> {
	
	private ResourceBundle resources;
	
	private ReadOnlyObjectProperty<Issue> selectedIssue;
	
	public WorklogListCell(ResourceBundle resources, ReadOnlyObjectProperty<Issue> selectedIssue) {
		this.resources = resources;
		this.selectedIssue = selectedIssue;
	}

	@Override
	public void updateItem(Worklog worklog, boolean empty) {
		super.updateItem(worklog, empty);
		if (worklog != null) {
			setGraphic(createViewingGraphic());
		}
	}

	private Node createViewingGraphic() {
		try {
			final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/viewWorklog.fxml"));
			fxmlLoader.setResources(resources);
			final Node graphic = (Node) fxmlLoader.load();
			final ViewWorklogController controller = fxmlLoader.getController();
			controller.setWorklog(getItem());
			return graphic;
		} catch(IOException e) {
			//TODO:Log and inform
			e.printStackTrace();
			return LabelBuilder.create().build();
		}
	}
	
	@Override
	public void startEdit() {
		super.startEdit();
		setGraphic(createEditingGraphic());
	}

	private Node createEditingGraphic() {
		try {
			final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/editWorklog.fxml"));
			fxmlLoader.setResources(resources);
			final Node graphic = (Node) fxmlLoader.load();
			final EditWorklogController controller = fxmlLoader.getController();
			controller.setListCell(this);
			controller.setSelectedIssue(selectedIssue);
			controller.setWorklog(getItem());
		
			return graphic;
		} catch(IOException e) {
			//TODO:Log and inform
			e.printStackTrace();
			return LabelBuilder.create().build();
		}
	}
	
	@Override
	public void commitEdit(Worklog worklog) {
		super.commitEdit(worklog);
		setGraphic(createViewingGraphic());
	}
	
	@Override
	public void cancelEdit() {
		super.cancelEdit();
		if (getItem().getId() == null) {
			getListView().getItems().remove(getItem());
		}
		setGraphic(createViewingGraphic());
	}
	
}