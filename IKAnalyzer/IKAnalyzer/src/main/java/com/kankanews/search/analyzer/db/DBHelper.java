package com.kankanews.search.analyzer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBHelper {
	private Logger logger = Logger.getLogger(DBHelper.class);

	private String driver; // 数据库驱动
	private String url;// 数据库
	private String user; // 用户名
	private String password; // 密码

	private static DBHelper instance;

	public DBHelper(String driver, String url, String user, String password) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public static DBHelper getInstance(String driver, String url, String user,
			String password) {
		if (instance == null) {
			instance = new DBHelper(driver, url, user, password);
		}
		return instance;
	}

	// 此方法为获取数据库连接
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver); // 加载数据库驱动
			if (null == conn) {
				conn = DriverManager.getConnection(url, user, password);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error("", e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}
		return conn;
	}

	public ResultSet executeQuery(Connection conn, String sql, Object... obj) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);
			}
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			logger.error("", e);
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException err) {
				logger.error("", err);
			}
		}
		return rs;
	}
}
