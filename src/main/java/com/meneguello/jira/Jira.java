package com.meneguello.jira;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dolby.jira.net.soap.jira.JiraSoapService;
import com.dolby.jira.net.soap.jira.JiraSoapServiceServiceLocator;
import com.dolby.jira.net.soap.jira.RemoteComment;
import com.dolby.jira.net.soap.jira.RemoteFilter;
import com.dolby.jira.net.soap.jira.RemoteIssue;
import com.dolby.jira.net.soap.jira.RemoteIssueType;
import com.dolby.jira.net.soap.jira.RemoteUser;
import com.dolby.jira.net.soap.jira.RemoteWorklog;
import com.meneguello.RunLater;

public class Jira implements PreferenceChangeListener {
	
	private static final String PARAM_JIRA_PASSWORD = "jira.password";

	private static final String PARAM_JIRA_USERNAME = "jira.username";

	private static final String PARAM_JIRA_HOSTNAME = "jira.hostname";

	private static final Logger logger = LoggerFactory.getLogger(Jira.class);

	private static final Object instanceLock = new Object();
	
	private static Jira instance;
	
	private final StringProperty token = new SimpleStringProperty();
	
	private final ReadOnlyBooleanWrapper active = new ReadOnlyBooleanWrapper(this, "active");
	
	private final Object issueTypesLock = new Object();
	
	private Map<String, IssueType> issueTypes;
	
	private final Map<String, User> users = new HashMap<>();
	
	private final ReadOnlyBooleanWrapper busy = new ReadOnlyBooleanWrapper(this, "busy");
	
	private final ReadOnlyLongWrapper activeRequests = new ReadOnlyLongWrapper(this, "activeRequests");
	
	public static Jira getInstance() {
		synchronized (instanceLock) {
			if (instance == null) {
				instance = new Jira();
			}
			return instance;
		}
	}

	private Jira() {
		active.bind(token.isNotNull());
		busy.bind(activeRequests.greaterThan(0));
		Preferences.userRoot().addPreferenceChangeListener(this);
	}
	
	private JiraSoapService service() throws RemoteException, ServiceException, JiraException {
		final Preferences preferences = Preferences.userRoot();
		
		final String hostname = preferences.get(PARAM_JIRA_HOSTNAME, null);
		if (hostname == null) {
			throw new JiraException("Host not set");
		}
		
		final JiraSoapServiceServiceLocator serviceLocator = new JiraSoapServiceServiceLocator();
		serviceLocator.setJirasoapserviceV2EndpointAddress(hostname + "/rpc/soap/jirasoapservice-v2");
		final JiraSoapService service = serviceLocator.getJirasoapserviceV2();
		
		if (!isActive()) {
			final String username = preferences.get(PARAM_JIRA_USERNAME, null);
			final String password = preferences.get(PARAM_JIRA_PASSWORD, null);
			token.set(service.login(username, password));
		}
		
		return service;
	}
	
	private String token() {
		return token.get();
	}
	
	private void logout() {
		token.set(null);
	}
	
	public List<Filter> findUserFilters() {
		logger.info("Loading user filters");
		try {
			return _jiraFindUserFilters();
		} catch(JiraException e) {
			logger.info("jira internal failure", e);
			return Collections.emptyList();
		} catch(ServiceException e) {
			logger.warn("remote failure", e);
			return Collections.emptyList();
		} catch(Exception e) {
			logger.error("return failure", e);
			return Collections.emptyList();
		}	
	}

	private List<Filter> _jiraFindUserFilters() throws RemoteException, ServiceException, JiraException {
		final List<Filter> filters = new ArrayList<>();
		final RemoteFilter[] remoteFilters = service().getSavedFilters(token());
		for (RemoteFilter remoteFilter : remoteFilters) {
			filters.add(FilterBuilder.create()
				.id(remoteFilter.getId())
				.name(remoteFilter.getName())
				.project(remoteFilter.getProject())
				.build());
		}
		logger.info("User filters loaded");
		return filters;
	}
	
	public List<Issue> findUserIssues() {
		logger.info("Loading user issues");
		try {
			return _jiraFindUserIssues();
		} catch(JiraException e) {
			logger.info("jira internal failure", e);
			return Collections.emptyList();
		} catch(ServiceException e) {
			logger.warn("remote failure", e);
			return Collections.emptyList();
		} catch(Exception e) {
			logger.error("return failure", e);
			return Collections.emptyList();
		}		
	}

	private List<Issue> _jiraFindUserIssues() throws RemoteException, ServiceException, JiraException {
		final List<Issue> issues = new ArrayList<>();
		final RemoteIssue[] remoteIssues = service().getIssuesFromJqlSearch(token(), "assignee = currentUser()", Integer.MAX_VALUE);
		for (RemoteIssue remoteIssue : remoteIssues) {
			issues.add(parseIssue(remoteIssue));
		}
		logger.info("User issues loaded");
		return issues;
	}
	
	public Issue findIssue(final String key) throws IssueNotFoundException {
		logger.info("Loading issue " + key);
		try {
			return _jiraFindIssue(key);
		} catch(JiraException e) {
			logger.info("jira internal failure", e);
			throw new IssueNotFoundException(key, e);
		} catch(ServiceException e) {
			logger.warn("remote failure", e);
			throw new IssueNotFoundException(key, e);
		} catch(Exception e) {
			logger.error("return failure", e);
			throw new IssueNotFoundException(key, e);
		}	
	}

	private Issue _jiraFindIssue(final String key) throws RemoteException, ServiceException, JiraException {
		final RemoteIssue remoteIssue = service().getIssue(token(), key);
		return parseIssue(remoteIssue);
	}

	public List<Issue> findFilterIssues(final Filter filter) {
		logger.info("Loading issues of filter " + filter.getName());
		try {
			return _jiraFindFilterIssues(filter);
		} catch(JiraException e) {
			logger.info("jira internal failure", e);
			return Collections.emptyList();
		} catch(ServiceException e) {
			logger.warn("remote failure", e);
			return Collections.emptyList();
		} catch(Exception e) {
			logger.error("return failure", e);
			return Collections.emptyList();
		}
	}

	private List<Issue> _jiraFindFilterIssues(final Filter filter) throws RemoteException, ServiceException, JiraException {
		final List<Issue> issues = new ArrayList<>();
		final RemoteIssue[] remoteIssues = service().getIssuesFromFilterWithLimit(token(), filter.getId(), 0, Integer.MAX_VALUE);
		for (RemoteIssue remoteIssue : remoteIssues) {
			issues.add(parseIssue(remoteIssue));
		}
		logger.info("Issues of filter " + filter.getName() + " loaded");
		return issues;
	}

	private Issue parseIssue(RemoteIssue remoteIssue) throws RemoteException, ServiceException, JiraException {
		final Issue issue = IssueBuilder.create()
			.key(remoteIssue.getKey())
			.summary(remoteIssue.getSummary())
			.description(remoteIssue.getDescription())
			.icon(loadIcon(remoteIssue.getType()))
			.build();
		return issue;
	}
	
	private Image loadIcon(String type) throws RemoteException, ServiceException, JiraException {
		loadIssueTypes();
		
		final IssueType issueType = issueTypes.get(type);
		if (issueType.getIcon() == null) {
			try {
				//TODO: try/catch default icon
				issueType.setIcon(new Image(new URL(issueType.getIconURL()).openStream()));
			} catch(MalformedURLException e) {
				logger.error("Malformed URL " + issueType.getIconURL(), e);
			} catch (IOException e) {
				logger.error("Network failure", e);
			}
		}
		return issueType.getIcon();
	}

	private void loadIssueTypes() throws RemoteException, ServiceException, JiraException {
		synchronized (issueTypesLock) {
			if (issueTypes == null) {
				issueTypes = new HashMap<>();
				_jiraLoadIssueTypes();
			}
			
			if (issueTypes.isEmpty()) {
				//TODO: Improve it
				throw new RuntimeException("No issue types available!");
			}
		}
	}

	private void _jiraLoadIssueTypes() throws RemoteException, ServiceException, JiraException {
		final RemoteIssueType[] remoteIssueTypes = service().getIssueTypes(token());
		for (RemoteIssueType remoteIssueType : remoteIssueTypes) {
			final IssueType issueType = parseIssueType(remoteIssueType);
			issueTypes.put(remoteIssueType.getId(), issueType);
		}
		
		final RemoteIssueType[] remoteSubIssueTypes = service().getSubTaskIssueTypes(token());
		for (RemoteIssueType remoteSubIssueType : remoteSubIssueTypes) {
			final IssueType issueType = parseIssueType(remoteSubIssueType);
			issueTypes.put(remoteSubIssueType.getId(), issueType);
		}
	}

	private IssueType parseIssueType(RemoteIssueType remoteIssueType) {
		return IssueTypeBuilder.create()
				.id(remoteIssueType.getId())
				.iconURL(remoteIssueType.getIcon())
				.description(remoteIssueType.getDescription())
				.build();
	}
	
	public List<Comment> findIssueComments(Issue issue) {
		logger.info("Loading comments of issue " + issue.getKey());
		try {
			return _jiraFindIssueComments(issue);
		} catch(JiraException e) {
			logger.info("jira internal failure", e);
			return Collections.emptyList();
		} catch(ServiceException e) {
			logger.warn("remote failure", e);
			return Collections.emptyList();
		} catch(Exception e) {
			logger.error("return failure", e);
			return Collections.emptyList();
		}
	}

	private List<Comment> _jiraFindIssueComments(Issue issue) throws RemoteException, ServiceException, JiraException {
		final List<Comment> comments = new ArrayList<>();
		final RemoteComment[] remoteComments = service().getComments(token(), issue.getKey());
		for (RemoteComment remoteComment : remoteComments) {
			comments.add(parseComment(remoteComment));
		}
		logger.info("Comments of issue " + issue.getKey() + " loaded");
		return comments;
	}

	private Comment parseComment(RemoteComment remoteComment) throws RemoteException, ServiceException, JiraException {
		//TODO: Builder
		Comment comment = new Comment();
		comment.setId(remoteComment.getId());
		comment.setAuthor(loadUser(remoteComment.getAuthor()));
		comment.setCreated(remoteComment.getCreated());
		comment.setUpdateAuthor(loadUser(remoteComment.getUpdateAuthor()));
		comment.setUpdated(remoteComment.getUpdated());
		comment.setBody(remoteComment.getBody());
		return comment;
	}

	private User loadUser(String login) throws RemoteException, ServiceException, JiraException {
		synchronized (users) {
			final User user = users.get(login);
			if (user == null) {
				return _jiraLoadUser(login);
			}
			return user;
		}
	}
	
	private User _jiraLoadUser(String login) throws RemoteException, ServiceException, JiraException {
		final RemoteUser remoteUser = service().getUser(token(), login);
		final User user = parseUser(remoteUser);
		users.put(login, user);
		return user;
	}

	private User parseUser(RemoteUser remoteUser) {
		//TODO: Builder
		final User user = new User();
		user.setFullname(remoteUser.getFullname());
		user.setName(remoteUser.getName());
		return user;
	}

	public List<Worklog> findIssueWorklogs(Issue issue) {
		logger.info("Loading worklogs of issue " + issue.getKey());
		try {
			return _jiraFindIssueWorklogs(issue);
		} catch(JiraException e) {
			logger.info("jira internal failure", e);
			return Collections.emptyList();
		} catch(ServiceException e) {
			logger.warn("remote failure", e);
			return Collections.emptyList();
		} catch(Exception e) {
			logger.error("return failure", e);
			return Collections.emptyList();
		}
	}

	private List<Worklog> _jiraFindIssueWorklogs(Issue issue) throws RemoteException, ServiceException, JiraException {
		final List<Worklog> worklogs = new ArrayList<>();
		final RemoteWorklog[] remoteWorklogs = service().getWorklogs(token(), issue.getKey());
		for (RemoteWorklog remoteWorklog : remoteWorklogs) {
			worklogs.add(parseWorklog(remoteWorklog));
		}
		logger.info("Worklogs of issue " + issue.getKey() + " loaded");
		return worklogs;
	}

	private Worklog parseWorklog(RemoteWorklog remoteWorklog) throws RemoteException, ServiceException, JiraException {
		final IntervalStringConverter intervalStringConverter = new IntervalStringConverter();
		return WorklogBuilder.create()
			.id(remoteWorklog.getId())
			.author(loadUser(remoteWorklog.getAuthor()))
			.created(remoteWorklog.getCreated().getTime())
			.startDate(remoteWorklog.getStartDate().getTime())
			.timeSpent(intervalStringConverter.fromString(remoteWorklog.getTimeSpent()))
			.timeSpentInSeconds(Long.valueOf(remoteWorklog.getTimeSpentInSeconds()))
			.comment(remoteWorklog.getComment())
			.updateAuthor(loadUser(remoteWorklog.getUpdateAuthor()))
			.updated(remoteWorklog.getUpdated().getTime())
			.build();
	}

	public Worklog addWorklog(Issue issue, Worklog worklog) throws WorklogAditionException {
		logger.info("Adding worklog to issue " + issue.getKey());
		try {
			return _jiraAddWorklog(issue, worklog);
		} catch(JiraException e) {
			logger.info("jira internal failure", e);
			throw new WorklogAditionException(e);
		} catch(ServiceException e) {
			logger.warn("remote failure", e);
			throw new WorklogAditionException(e);
		} catch(Exception e) {
			logger.error("return failure", e);
			throw new WorklogAditionException(e);
		}
	}

	private Worklog _jiraAddWorklog(Issue issue, Worklog worklog) throws RemoteException, ServiceException, JiraException {
		final IntervalStringConverter intervalStringConverter = new IntervalStringConverter();
		final RemoteWorklog remoteWorklog = new RemoteWorklog();
		remoteWorklog.setStartDate(DateUtils.toCalendar(worklog.getStartDate()));
		remoteWorklog.setTimeSpent(intervalStringConverter.toString(worklog.getTimeSpent()));
		remoteWorklog.setComment(worklog.getComment());
		return parseWorklog(service().addWorklogAndAutoAdjustRemainingEstimate(token(), issue.getKey(), remoteWorklog));
	}

	public void updateWorklog(Worklog worklog) throws WorklogUpdateException {
		logger.info("Updating worklog");
		try {
			_jiraUpdateWorklog(worklog);
		} catch(JiraException e) {
			logger.info("jira internal failure", e);
			throw new WorklogUpdateException(e);
		} catch(ServiceException e) {
			logger.warn("remote failure", e);
			throw new WorklogUpdateException(e);
		} catch(Exception e) {
			logger.error("return failure", e);
			throw new WorklogUpdateException(e);
		}
	}

	private void _jiraUpdateWorklog(Worklog worklog) throws RemoteException, ServiceException, JiraException {
		final IntervalStringConverter intervalStringConverter = new IntervalStringConverter();
		final RemoteWorklog remoteWorklog = new RemoteWorklog();
		remoteWorklog.setId(worklog.getId());
		remoteWorklog.setStartDate(DateUtils.toCalendar(worklog.getStartDate()));
		remoteWorklog.setTimeSpent(intervalStringConverter.toString(worklog.getTimeSpent()));
		remoteWorklog.setComment(worklog.getComment());
		service().updateWorklogAndAutoAdjustRemainingEstimate(token(), remoteWorklog);
	}
	
	public boolean isBusy() {
		return busy.get();
	}
	
	public ReadOnlyBooleanProperty busyProperty() {
		return busy.getReadOnlyProperty();
	}

	public long getActiveRequests() {
		return activeRequests.get();
	}
	
	public ReadOnlyLongProperty activeRequestsProperty() {
		return activeRequests.getReadOnlyProperty();
	}
	
	public boolean isActive() {
		return active.get();
	}
	
	public ReadOnlyBooleanProperty activeProperty() {
		return active.getReadOnlyProperty();
	}
	
	@RunLater
	void beginRequest() {
		synchronized (activeRequests) {
			activeRequests.set(activeRequests.get() + 1);
		}
	}

	@RunLater
	void finishRequest() {
		synchronized (activeRequests) {
			activeRequests.set(activeRequests.get() - 1);
		}
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent evt) {
		if (evt.getKey() == PARAM_JIRA_HOSTNAME || 
				evt.getKey() == PARAM_JIRA_USERNAME || 
				evt.getKey() == PARAM_JIRA_PASSWORD) {
			logout();
		}
	}

}
