package com.kankanews.search.db.model;

import org.apache.solr.common.SolrDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SearchResult {
	private String id;
	private String classid;
	private String onclick;
	private String title;
	private String titlepic;
	private String titleurl;
	private String newstime;
	private String keywords;
	private String type;
	private String checked;
	private String videourl;
	private String imagegroup;
	private String author;
	private String authorid;
	private String intro;
	@JsonIgnore
	private String docVersion;

	public SearchResult(SolrDocument doc) {
		super();
		this.id = "" + doc.get("id");
		this.classid = (String) doc.get("classid");
		this.onclick = (String) doc.get("onclick");
		this.title = (String) doc.get("title");
		this.checked = (String) doc.get("checked");
		this.titlepic = (String) doc.get("titlepic");
		this.titleurl = (String) doc.get("titleurl");
		this.newstime = (String) doc.get("newstime");
		this.keywords = (String) doc.get("keywords");
		this.type = (String) doc.get("type");
		this.videourl = (String) doc.get("videourl");
		this.imagegroup = (String) doc.get("imagegroup");
		this.author = (String) doc.get("author");
		this.authorid = (String) doc.get("authorid");
		this.intro = (String) doc.get("intro");
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

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
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

	public String getNewstime() {
		return newstime;
	}

	public void setNewstime(String newstime) {
		this.newstime = newstime;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getVideourl() {
		return videourl;
	}

	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

	public String getImagegroup() {
		return imagegroup;
	}

	public void setImagegroup(String imagegroup) {
		this.imagegroup = imagegroup;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getDocVersion() {
		return docVersion;
	}

	public void setDocVersion(String docVersion) {
		this.docVersion = docVersion;
	}

}
