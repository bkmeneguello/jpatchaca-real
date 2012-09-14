package com.meneguello.jira;

public class Filter {

	private String id;

	private String name;
	
	private String project;

	Filter() {
		
	}
	
	void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	void setProject(String project) {
		this.project = project;
	}
	
	public String getProject() {
		return project;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Filter other = (Filter) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
