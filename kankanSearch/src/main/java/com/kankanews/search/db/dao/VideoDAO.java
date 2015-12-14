package com.kankanews.search.db.dao;

import java.sql.ResultSet;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.kankanews.search.core.DBHelper;
import com.kankanews.search.db.base.BaseDao;
import com.kankanews.search.db.model.Video;

public class VideoDAO extends BaseDao<Video, String> {

	@Autowired
	private Properties globalSQL;

	private DBHelper dbHepler;

	public DBHelper getDbHepler() {
		return dbHepler;
	}

	public void setDbHepler(DBHelper dbHepler) {
		this.dbHepler = dbHepler;
	}

	public ResultSet getAllVideo() {
		return dbHepler.executeQuery(globalSQL.getProperty("_VIDEO_SELECT_"));
	}
}
