package com.kankanews.search.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.kankanews.search.db.base.BaseDao;
import com.kankanews.search.db.model.Photo;
import com.kankanews.search.utils.DBHelper;

public class PhotoDAO extends BaseDao<Photo, String> {

	@Autowired
	private Properties globalSQL;

	private DBHelper dbHepler;

	public DBHelper getDbHepler() {
		return dbHepler;
	}

	public void setDbHepler(DBHelper dbHepler) {
		this.dbHepler = dbHepler;
	}

	public ResultSet getOne(String id) {
		return dbHepler.executeQuery(globalSQL.getProperty("_PHOTO_SELECT_"),
				new Object[] { id });
	}
}
