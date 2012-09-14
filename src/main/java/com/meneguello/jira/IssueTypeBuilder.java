package com.meneguello.jira;

import java.util.BitSet;

import javafx.scene.image.Image;
import javafx.util.Builder;

public class IssueTypeBuilder implements Builder<IssueType> {
	
	private static final int ID_ATTR = 0;

	private static final int ICON_URL_ATTR = 1;
	
	private static final int DESCRIPTION_ATTR = 2;

	private static final int ICON_ATTR = 3;

	private String id;
	
	private String iconURL;
	
	private String description;
	
	private Image icon;
	
	private BitSet bitSet = new BitSet();

	public static IssueTypeBuilder create() {
		return new IssueTypeBuilder();
	}
	
	private IssueTypeBuilder() {
		
	}

	public IssueTypeBuilder id(String id) {
		this.id = id;
		bitSet.set(ID_ATTR);
		return this;
	}

	public IssueTypeBuilder iconURL(String iconURL) {
		this.iconURL = iconURL;
		bitSet.set(ICON_URL_ATTR);
		return this;
	}

	public IssueTypeBuilder description(String description) {
		this.description = description;
		bitSet.set(DESCRIPTION_ATTR);
		return this;
	}

	public IssueTypeBuilder icon(Image icon) {
		this.icon = icon;
		bitSet.set(ICON_ATTR);
		return this;
	}

	@Override
	public IssueType build() {
		final IssueType issueType = new IssueType();
		if(bitSet.get(ID_ATTR)) {
			issueType.setId(id);
		}
		if(bitSet.get(ICON_URL_ATTR)) {
			issueType.setIconURL(iconURL);
		}
		if(bitSet.get(DESCRIPTION_ATTR)) {
			issueType.setDescription(description);
		}
		if(bitSet.get(ICON_ATTR)) {
			issueType.setIcon(icon);
		}
		return issueType;
	}

}
