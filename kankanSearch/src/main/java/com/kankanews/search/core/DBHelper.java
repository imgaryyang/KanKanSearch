package com.kankanews.search.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public final class DBHelper {
	private static Logger logger = Logger.getLogger(DBHelper.class);

	private static String driver; // 数据库驱动
	private static String url;// 数据库
	private static String user; // 用户名
	private static String password; // 密码

	// 此方法为获取数据库连接
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver); // 加载数据库驱动
			if (null == conn) {
				conn = DriverManager.getConnection(url, user, password);
			}
		} catch (ClassNotFoundException e) {
			logger.error(e.getLocalizedMessage());
		} catch (SQLException e) {
			logger.error(e.getLocalizedMessage());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return conn;
	}

	/**
	 * 
	 * 增删改【Add、Del、Update】
	 * 
	 *
	 * 
	 * @param sql
	 * 
	 * @return int
	 */
	public static int executeNonQuery(String sql) {
		int result = 0;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
			closeAll(null, stmt, conn);
		} finally {
			closeAll(null, stmt, conn);
		}
		return result;
	}

	/**
	 * 
	 * 增删改【Add、Delete、Update】
	 * 
	 *
	 * 
	 * @param sql
	 * 
	 * @param obj
	 * 
	 * @return int
	 */

	public static int executeNonQuery(String sql, Object... obj) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);
			}
			result = pstmt.executeUpdate();
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
			closeAll(null, pstmt, conn);
		} finally {
			closeAll(null, pstmt, conn);
		}
		return result;
	}

	/**
	 * 
	 * 查【Query】
	 * 
	 *
	 * 
	 * @param sql
	 * 
	 * @return ResultSet
	 */

	public static ResultSet executeQuery(String sql) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
			closeAll(rs, stmt, conn);
		}
		return rs;
	}

	/**
	 * 
	 * 查【Query】
	 * 
	 *
	 * 
	 * @param sql
	 * 
	 * @param obj
	 * 
	 * @return ResultSet
	 */

	public static ResultSet executeQuery(String sql, Object... obj) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);
			}
			rs = pstmt.executeQuery();
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
			closeAll(rs, pstmt, conn);
		}
		return rs;
	}

	/**
	 * 
	 * 判断记录是否存在
	 * 
	 *
	 * 
	 * @param sql
	 * 
	 * @return Boolean
	 */

	public static Boolean isExist(String sql) {
		ResultSet rs = null;
		try {
			rs = executeQuery(sql);
			rs.last();
			int count = rs.getRow();
			if (count > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
			close(rs);
			return false;
		} finally {
			close(rs);
		}
	}

	/**
	 * 
	 * 判断记录是否存在
	 * 
	 *
	 * 
	 * @param sql
	 * 
	 * @return Boolean
	 */

	public static Boolean isExist(String sql, Object... obj) {
		ResultSet rs = null;
		try {
			rs = executeQuery(sql, obj);
			rs.last();
			int count = rs.getRow();
			if (count > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
			close(rs);
			return false;
		} finally {
			close(rs);
		}
	}

	/**
	 * 
	 * 获取查询记录的总行数
	 * 
	 *
	 * 
	 * @param sql
	 * 
	 * @return int
	 */

	public static int getCount(String sql) {
		int result = 0;
		ResultSet rs = null;
		try {
			rs = executeQuery(sql);
			rs.last();
			result = rs.getRow();
		} catch (SQLException err) {
			close(rs);
			logger.error(err.getLocalizedMessage());
		} finally {
			close(rs);
		}
		return result;
	}

	/**
	 * 
	 * 获取查询记录的总行数
	 * 
	 *
	 * 
	 * @param sql
	 * 
	 * @param obj
	 * 
	 * @return int
	 */

	public static int getCount(String sql, Object... obj) {
		int result = 0;
		ResultSet rs = null;
		try {
			rs = executeQuery(sql, obj);
			rs.last();
			result = rs.getRow();
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
		} finally {
			close(rs);
		}
		return result;
	}

	/**
	 * 
	 * 释放【ResultSet】资源
	 * 
	 *
	 * 
	 * @param rs
	 */

	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * 释放【Statement】资源
	 * 
	 *
	 * 
	 * @param st
	 */

	public static void close(Statement st) {
		try {
			if (st != null) {
				st.close();
			}
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * 释放【Connection】资源
	 * 
	 *
	 * 
	 * @param conn
	 */

	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException err) {
			logger.error(err.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * 释放所有数据资源
	 * 
	 *
	 * 
	 * @param rs
	 * 
	 * @param st
	 * 
	 * @param conn
	 */
	public static void closeAll(ResultSet rs) {
		try {
//			if (rs != null) {
//				rs.close();
//			}
			if (rs.getStatement() != null) {
				rs.getStatement().close();
			}
			if (rs.getStatement().getConnection() != null) {
				rs.getStatement().getConnection().close();
			}
		} catch (SQLException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * 释放所有数据资源
	 * 
	 *
	 * 
	 * @param rs
	 * 
	 * @param st
	 * 
	 * @param conn
	 */
	public static void closeAll(ResultSet rs, Statement st, Connection conn) {
		close(rs);
		close(st);
		close(conn);
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}