package com.kankanews.search.db.dao;

import java.sql.ResultSet;

import com.kankanews.search.core.DBHelper;
import com.kankanews.search.db.base.BaseDao;
import com.kankanews.search.db.model.Video;

public class VideoDAO extends BaseDao<Video, String> {

	private DBHelper dbHepler;

	public DBHelper getDbHepler() {
		return dbHepler;
	}

	public void setDbHepler(DBHelper dbHepler) {
		this.dbHepler = dbHepler;
	}

	public ResultSet getAllVideo() {
		return dbHepler
				.executeQuery("select id, onclick, title, titlepic, newstime, keywords, createtime, videourl from kk_ecms_kankanvideos");
	}
}
