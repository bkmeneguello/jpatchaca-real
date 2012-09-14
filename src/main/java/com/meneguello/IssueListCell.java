package com.meneguello;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

import com.meneguello.jira.Issue;

class IssueListCell extends ListCell<Issue> {
	
	@Override
	public void updateItem(Issue issue, boolean empty) {
		super.updateItem(issue, empty);
		if (issue != null) {
			setText("[" + issue.getKey() + "] " + issue.getSummary());
			
			setGraphic(new ImageView(issue.getIcon()));
			
//			final Tooltip tooltip = new Tooltip();
//			tooltip.setText(issue.getDescription());
//			tooltip.setGraphic(new ImageView(issue.getIcon()));
//			setTooltip(tooltip);
		}
	}
}