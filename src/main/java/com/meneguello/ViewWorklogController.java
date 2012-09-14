package com.meneguello;

import org.apache.commons.lang.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.converter.DateTimeStringConverter;

import com.meneguello.jira.IntervalStringConverter;
import com.meneguello.jira.Worklog;

public class ViewWorklogController {
	
	@FXML
	private VBox parent;
	
	@FXML
	private Label author;

	@FXML
	private Label startDate;

	@FXML
	private Label timeSpent;

	@FXML
	private Label comment;
	
	private final DateTimeStringConverter dateTimeStringConverter = new DateTimeStringConverter();
	
	private final IntervalStringConverter intervalStringConverter = new IntervalStringConverter();

	@RunLater
	public void setWorklog(Worklog worklog) {
		author.setText(worklog.getAuthor() != null ? worklog.getAuthor().getFullname() : null);
		startDate.setText(dateTimeStringConverter.toString(worklog.getStartDate()));
		timeSpent.setText(intervalStringConverter.toString(worklog.getTimeSpent()));
		if (StringUtils.isNotEmpty(worklog.getComment())) {
			comment.setText(worklog.getComment());
		} else {
			parent.getChildren().remove(comment);
			parent.layout();
		}
	}

}
