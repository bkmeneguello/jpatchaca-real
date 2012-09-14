package com.meneguello.jira;

import java.util.BitSet;

import javafx.scene.image.Image;
import javafx.util.Builder;

public class IssueBuilder implements Builder<Issue> {
	
	private static final int KEY_ATTR = 0;

	private static final int SUMMARY_ATTR = 1;

	private static final int DESCRIPTION_ATTR = 2;

	private static final int ICON_ATTR = 3;

	private String key;
	
	private String summary;
	
	private Image icon;
	
	private BitSet bitSet = new BitSet();

	private String description;

	public static IssueBuilder create() {
		return new IssueBuilder();
	}
	
	private IssueBuilder() {
		
	}

	public IssueBuilder key(String key) {
		this.key = key;
		bitSet.set(KEY_ATTR);
		return this;
	}

	public IssueBuilder summary(String summary) {
		this.summary = summary;
		bitSet.set(SUMMARY_ATTR);
		return this;
	}

	public IssueBuilder description(String description) {
		this.description = description;
		bitSet.set(DESCRIPTION_ATTR);
		return this;
	}

	public IssueBuilder icon(Image icon) {
		this.icon = icon;
		bitSet.set(ICON_ATTR);
		return this;
	}

	@Override
	public Issue build() {
		final Issue issue = new Issue();
		if(bitSet.get(KEY_ATTR)) {
			issue.setKey(key);
		}
		if(bitSet.get(SUMMARY_ATTR)) {
			issue.setSummary(summary);
		}
		if(bitSet.get(DESCRIPTION_ATTR)) {
			issue.setDescription(description);
		}
		if(bitSet.get(ICON_ATTR)) {
			issue.setIcon(icon);
		}
		return issue;
	}

}
