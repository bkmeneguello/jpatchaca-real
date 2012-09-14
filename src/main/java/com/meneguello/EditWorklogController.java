package com.meneguello;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.converter.DateTimeStringConverter;

import com.meneguello.jira.IntervalStringConverter;
import com.meneguello.jira.Issue;
import com.meneguello.jira.Jira;
import com.meneguello.jira.Worklog;
import com.meneguello.jira.WorklogAditionException;
import com.meneguello.jira.WorklogBuilder;
import com.meneguello.jira.WorklogUpdateException;

public class EditWorklogController {
	
	@FXML
	private Label author;
	
	@FXML
	private TextField startDate;

	@FXML
	private TextField timeSpent;

	@FXML
	private TextArea comment;
	
	private final DateTimeStringConverter dateTimeStringConverter = new DateTimeStringConverter();
	
	private final IntervalStringConverter intervalStringConverter = new IntervalStringConverter();
	
	private Label errorLabel;

	private Worklog worklog;

	private WorklogListCell worklogListCell;

	private ReadOnlyObjectProperty<Issue> selectedIssue;

	public void setListCell(WorklogListCell worklogListCell) {
		this.worklogListCell = worklogListCell;		
	}

	public void setSelectedIssue(ReadOnlyObjectProperty<Issue> selectedIssue) {
		this.selectedIssue = selectedIssue;
	}
	
	@RunLater
	public void setWorklog(Worklog worklog) {
		this.worklog = worklog;
		author.setText(worklog.getAuthor() != null ? worklog.getAuthor().getFullname() : null);
		startDate.setText(dateTimeStringConverter.toString(worklog.getStartDate()));
		//TooltipBuilder.create().text(dateTimeStringConverter.toString(new Date(0))).build()
		timeSpent.setText(intervalStringConverter.toString(worklog.getTimeSpent()));
		comment.setText(worklog.getComment());
	}
	
	@FXML
	public void confirm() {
		try {
			final Worklog worklog = WorklogBuilder.create(this.worklog)
					.startDate(dateTimeStringConverter.fromString(startDate.getText()))
					.timeSpent(intervalStringConverter.fromString(timeSpent.getText()))
					.comment(comment.getText())
					.build();
			
			if (this.worklog.getId() == null) {
				try {
					worklogListCell.commitEdit(Jira.getInstance().addWorklog(selectedIssue.get(), worklog));
				} catch(WorklogAditionException e) {
					setError(e.getLocalizedMessage());
				}
			} else {
				try {
					Jira.getInstance().updateWorklog(worklog);
					worklogListCell.commitEdit(worklog);
				} catch(WorklogUpdateException e) {
					setError(e.getLocalizedMessage());
				}
			}
		} catch(RuntimeException e) {
			setError(e.getLocalizedMessage());
		}
	}
	
	private void setError(String text) {
		/*if (errorLabel == null) {
			vbox.getChildren().add(0, errorLabel = LabelBuilder.create()
					.textFill(Color.RED)
					.build());
		}
		errorLabel.setText(text);*/
	}
	
	@FXML
	public void cancel() {
		worklogListCell.cancelEdit();
	}

}
