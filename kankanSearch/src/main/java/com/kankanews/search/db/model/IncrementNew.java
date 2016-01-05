package com.kankanews.search.db.model;

import java.io.Serializable;

public class IncrementNew implements Serializable {
	private static final long serialVersionUID = 3968051314611306822L;

	private String id;
	private String classid;
	private String table;
	private String action;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
