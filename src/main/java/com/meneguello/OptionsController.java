package com.meneguello;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class OptionsController implements PreferenceChangeListener {
	
	@FXML
	private TitledPane optionsPane;

	@FXML
	private TextField hostname;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	public void initialize() {
		load();
	}
	
	@FXML
	public void confirm() {
		store();		
		optionsPane.setExpanded(false);
		//MainController.getInstance().loadUserFilters();//TODO: Criar atributo bindable em Properties
	}
	
	@FXML
	public void cancel() {
		load();
	}

	@RunLater
	private void load() {
		final Preferences preferences = Preferences.userRoot();
		hostname.setText(preferences.get("jira.hostname", null));
		username.setText(preferences.get("jira.username", null));
		password.setText(preferences.get("jira.password", null));
	}
	
	@RunLater
	private void store() {
		final Preferences preferences = Preferences.userRoot();
		preferences.put("jira.hostname", hostname.getText());
		preferences.put("jira.username", username.getText());
		preferences.put("jira.password", password.getText());
	}
	
	@Override @RunLater
	public void preferenceChange(PreferenceChangeEvent evt) {
		if (evt.getKey() == "jira.hostname") {
			hostname.setText(evt.getNewValue());
		} else if (evt.getKey() == "jira.username") {
			username.setText(evt.getNewValue());
		} else if (evt.getKey() == "jira.password") {
			password.setText(evt.getNewValue());
		}
	}
	
}
