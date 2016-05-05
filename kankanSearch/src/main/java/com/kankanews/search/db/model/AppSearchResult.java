package com.kankanews.search.db.model;

import org.apache.solr.common.SolrDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AppSearchResult {
	private String id;
	private String classid;
	private String checked;
	private String editor;
	private String headline;
	private String keyboard;
	private String labels;
	private String newstime;
	private String o_cmsid;
	private String o_classid;
	private String title;
	private String titlepic;
	private String titleurl;
	private String type;
	private String top;
	private String sharepic;
	@JsonIgnore
	private String docVersion;

	public AppSearchResult(SolrDocument doc) {
		super();
		this.id = "" + doc.get("id");
		this.classid = (String) doc.get("classid");
		this.checked = (String) doc.get("checked");
		this.editor = (String) doc.get("editor");
		this.headline = (String) doc.get("headline");
		this.keyboard = (String) doc.get("keyboard");
		this.labels = (String) doc.get("labels");
		this.newstime = "" + doc.get("newstime");
		this.o_cmsid = (String) doc.get("o_cmsid");
		this.o_classid = (String) doc.get("o_classid");
		this.title = (String) doc.get("title");
		this.titlepic = (String) doc.get("titlepic");
		this.titleurl = (String) doc.get("titleurl");
		this.type = (String) doc.get("type");
		this.top = (String) doc.get("top");
		this.sharepic = (String) doc.get("sharepic");
		this.docVersion = "" + doc.get("docversion");
	}

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

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(String keyboard) {
		this.keyboard = keyboard;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getNewstime() {
		return newstime;
	}

	public void setNewstime(String newstime) {
		this.newstime = newstime;
	}

	public String getO_cmsid() {
		return o_cmsid;
	}

	public void setO_cmsid(String o_cmsid) {
		this.o_cmsid = o_cmsid;
	}

	public String getO_classid() {
		return o_classid;
	}

	public void setO_classid(String o_classid) {
		this.o_classid = o_classid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitlepic() {
		return titlepic;
	}

	public void setTitlepic(String titlepic) {
		this.titlepic = titlepic;
	}

	public String getTitleurl() {
		return titleurl;
	}

	public void setTitleurl(String titleurl) {
		this.titleurl = titleurl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getSharepic() {
		return sharepic;
	}

	public void setSharepic(String sharepic) {
		this.sharepic = sharepic;
	}

	public String getDocVersion() {
		return docVersion;
	}

	public void setDocVersion(String docVersion) {
		this.docVersion = docVersion;
	}

}
