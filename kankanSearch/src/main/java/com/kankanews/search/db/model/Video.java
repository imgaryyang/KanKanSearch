package com.kankanews.search.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.solr.common.SolrDocument;

@Entity
@javax.persistence.Table(name = "kk_ecms_kankanvideos")
public class Video implements Serializable {
	private static final long serialVersionUID = 5914761149865368622L;

	@Id
	private String id;
	@Column(name = "onclick")
	private String onclick;
	@Column(name = "title")
	private String title;
	@Column(name = "titlepic")
	private String titlePic;
	@Column(name = "newstime")
	private String newsTime;
	@Column(name = "keywords")
	private String keyWords;
	@Column(name = "createtime")
	private String createTime;
	@Column(name = "videourl")
	private String videoUrl;

	@Transient
	private String docVersion;

	public Video(SolrDocument doc) {
		super();
		this.id = "" + doc.get("id");
		this.onclick = (String) doc.get("onclick");
		this.title = (String) doc.get("title");
		this.titlePic = (String) doc.get("titlepic");
		this.newsTime = (String) doc.get("newstime");
		this.keyWords = (String) doc.get("keywords");
		this.createTime = (String) doc.get("createtime");
		this.videoUrl = (String) doc.get("videourl");
		this.docVersion = "" + doc.get("docversion");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getTitlePic() {
		return titlePic;
	}

	public void setTitlePic(String titlePic) {
		this.titlePic = titlePic;
	}

	public String getNewsTime() {
		return newsTime;
	}

	public void setNewsTime(String newsTime) {
		this.newsTime = newsTime;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getDocVersion() {
		return docVersion;
	}

	public void setDocVersion(String docVersion) {
		this.docVersion = docVersion;
	}

}
