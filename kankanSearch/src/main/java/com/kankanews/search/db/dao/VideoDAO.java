package com.kankanews.search.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

	public ResultSet getAllNews() {
		return dbHepler.executeQuery(globalSQL.getProperty("_WHOLE_INDEX_"));
	}

	public ResultSet getRangeNews(long stTime, long edTime) {
		String sql = globalSQL.getProperty("_WHOLE_INDEX_RANGE_");
		return DBHelper.executeQuery(sql, new Object[] { stTime, edTime,
				stTime, edTime });
	}

	public ResultSet getOne(String id) {
		return dbHepler.executeQuery(globalSQL.getProperty("_VIDEO_SELECT_"),
				new Object[] { id });
	}
}
