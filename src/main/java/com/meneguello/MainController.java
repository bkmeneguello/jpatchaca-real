package com.meneguello;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meneguello.jira.Comment;
import com.meneguello.jira.Filter;
import com.meneguello.jira.Issue;
import com.meneguello.jira.IssueNotFoundException;
import com.meneguello.jira.Jira;
import com.meneguello.jira.Worklog;
import com.meneguello.jira.WorklogBuilder;

public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	private static MainController instance;
	
	@FXML
	private ResourceBundle resources;
	
	private FavoriteIssueSet favoriteIssues;

	private SearchHistoryIssueSet searchHistory;

	@FXML
	private ListView<Issue> issues;
	
	@FXML
	private ComboBox<IssueSet> filters;
	
	@FXML
	private ProgressIndicator status;
	
	@FXML
	private QueueObserver filtersQueueObserver;
	
	@FXML
	private QueueObserver issueListQueueObserver;
	
	@FXML
	private QueueObserver issueDetailsQueueObserver;
	
	@FXML
	private TextField issueKey;
	
	@FXML
	private ToggleButton favorite;
	
	@FXML
	private Label issueSummary;
	
	@FXML
	private Label issueDescription;
	
	@FXML
	private ListView<Comment> comments;
	
	@FXML
	private ObservableList<Comment> commentList;
	
	@FXML
	private ListView<Worklog> worklogs;
	
	@FXML
	private ObservableList<Worklog> worklogList;
	
	@FXML
	private Button addWorklog;
	
	public static MainController getInstance() {
		if (instance == null) {
			instance = new MainController();
		}
		return instance;
	}
	
	private MainController() {
		
	}
	
	public void initialize() {
		logger.info("Initializing");
		
		favoriteIssues = new FavoriteIssueSet(resources);
		searchHistory = new SearchHistoryIssueSet(resources);
		
		filters.disableProperty().bind(filtersQueueObserver.busyProperty());
		filters.valueProperty().addListener(new ChangeListener<IssueSet>() {
			@Override
			public void changed(ObservableValue<? extends IssueSet> paramObservableValue, IssueSet oldValue, IssueSet newValue) {
				if (newValue == null) { //TODO: Refactor
					issues.setItems(null);
				} else {
					final Issue selectedItem = issues.getSelectionModel().getSelectedItem();
					issues.setItems(newValue.getIssues());
					if (issues.getItems().contains(selectedItem)) {
						issues.getSelectionModel().select(selectedItem);
					} else {
						issues.getSelectionModel().select(0);
					}
				}
			}
		});
		
		issues.disableProperty().bind( //FIXME: or(or(A, B), C) ???
				Bindings.or(
						filtersQueueObserver.busyProperty(),
						Bindings.or(
								issueListQueueObserver.busyProperty(),
								issueDetailsQueueObserver.busyProperty()
						)
				)
		);
		issues.setCellFactory(IssueListCellFactory.create());
		//issues.itemsProperty().bind((ObservableValue<? extends ObservableList<Issue>>) Bindings.select(filters.valueProperty(), "issues"));
		
		issues.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Issue>() {
			@Override
			public void changed(ObservableValue<? extends Issue> paramObservableValue, Issue oldValue, Issue newValue) {
				if (newValue != null) {
					favorite.setSelected(favoriteIssues.getIssues().contains(newValue));
					loadComments(newValue);
					loadWorklogs(newValue);
				}
			}
		});
		
		issueSummary.textProperty().bind(Bindings.selectString(issues.getSelectionModel().selectedItemProperty(), "summary"));
		issueDescription.textProperty().bind(Bindings.selectString(issues.getSelectionModel().selectedItemProperty(), "description"));
		
		comments.setCellFactory(CommentListCellFactory.create());
		
		worklogs.setCellFactory(WorklogListCellFactory.create(resources, issues.getSelectionModel().selectedItemProperty()));
		addWorklog.disableProperty().bind(Bindings.or(issues.getSelectionModel().selectedItemProperty().isNull(), worklogs.editingIndexProperty().greaterThanOrEqualTo(0)));
		
		status.visibleProperty().bind(Jira.getInstance().busyProperty());
		
		loadIssueSets();
		
		logger.info("Initialization complete");
	}
	
	@FXML @RunLater
	public void toggleFavorite() { //TODO:Refactor
		final Issue issue = issues.getSelectionModel().getSelectedItem();
		if (favorite.isSelected()) {
			if (!favoriteIssues.getIssues().contains(issue)) {
				favoriteIssues.append(issue);
			}
		} else {
			if (favoriteIssues.getIssues().contains(issue)) {
				favoriteIssues.remove(issue);
			}
		}
		
		loadIssueSets();
	}
	
	@FXML @Async(queue = "filters")
	public void loadIssueSets() {
		updateFilters(Jira.getInstance().findUserFilters());
	}
	
	@RunLater
	private void updateFilters(final List<Filter> userFilters) {
		final IssueSet selectedItem = filters.getSelectionModel().getSelectedItem();
		
		filters.getItems().clear();
		
		favoriteIssues.appendTo(filters.getItems());
		searchHistory.appendTo(filters.getItems());
		for (Filter userFilter : userFilters) {
			new UserFilterIssueSet(userFilter).appendTo(filters.getItems());
		}
		
		filters.layout();
		
		if (filters.getItems().contains(selectedItem)) { //TODO:Refactor
			filters.getSelectionModel().select(selectedItem);
		} else {
			logger.info("Current filter not found");
			filters.getSelectionModel().select(0);
		}
	}

	@FXML @Async
	public void searchIssue() {
		try {
			updateFoundIssue(Jira.getInstance().findIssue(issueKey.getText()));
		} catch (IssueNotFoundException e) {
			logger.warn("Issue not found", e); //TODO
		}
	}
	
	@RunLater
	private void updateFoundIssue(final Issue remoteIssue) {
		searchHistory.append(remoteIssue);
		filters.setValue(searchHistory);
		issues.getSelectionModel().select(0);
		
		loadIssueSets();
	}

	@Async(queue = "issueDetails")
	private void loadComments(Issue issue) {
		updateComments(Jira.getInstance().findIssueComments(issue));
	}

	@RunLater
	private void updateComments(final List<Comment> newCommentList) {
		logger.debug("Updating Comment list");
		
		commentList.clear();
		commentList.addAll(newCommentList);
		
		logger.debug("Comment list updated");
	}
	
	@Async(queue = "issueDetails")
	private void loadWorklogs(Issue issue) {
		updateWorklogs(Jira.getInstance().findIssueWorklogs(issue));
	}

	@RunLater
	private void updateWorklogs(final List<Worklog> newWorklogList) {
		logger.debug("Updating Worklog list");
		
		worklogList.clear();
		worklogList.addAll(newWorklogList);
		
		logger.debug("Worklog list updated");
	}
	
	@FXML
	public void addWorklog() {
		final Worklog worklog = WorklogBuilder.create()
				.startDate(DateUtils.truncate(new Date(), Calendar.HOUR_OF_DAY))
				.build();
		worklogList.add(0, worklog);
		worklogs.layout();//FIXME?
		worklogs.edit(0);
	}
	
}