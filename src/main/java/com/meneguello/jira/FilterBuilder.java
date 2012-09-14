package com.meneguello.jira;

import java.util.BitSet;

public class FilterBuilder {

	private static final int ID_ATTR = 0;

	private static final int NAME_ATTR = 1;

	private static final int PROJECT_ATTR = 2;

	private String id;
	
	private String name;
	
	private String project;
	
	private BitSet bitSet = new BitSet();

	public static FilterBuilder create() {
		return new FilterBuilder();
	}
	
	private FilterBuilder() {
		
	}

	public FilterBuilder id(String id) {
		this.id = id;
		bitSet.set(ID_ATTR);
		return this;
	}

	public FilterBuilder name(String name) {
		this.name = name;
		bitSet.set(NAME_ATTR);
		return this;
	}

	public FilterBuilder project(String project) {
		this.project = project;
		bitSet.set(PROJECT_ATTR);
		return this;
	}

	public Filter build() {
		final Filter filter = new Filter();
		if(bitSet.get(ID_ATTR)) {
			filter.setId(id);
		}
		if(bitSet.get(NAME_ATTR)) {
			filter.setName(name);
		}
		if(bitSet.get(PROJECT_ATTR)) {
			filter.setProject(project);
		}
		return filter;
	}

}
