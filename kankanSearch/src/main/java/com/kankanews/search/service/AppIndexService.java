package com.kankanews.search.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import com.kankanews.search.config.GlobalConfig;
import com.kankanews.search.db.dao.AppDAO;
import com.kankanews.search.db.model.IncrementNew;
import com.kankanews.search.utils.DBHelper;
import com.kankanews.search.utils.GsonUtil;

public class AppIndexService {
	Logger logger = Logger.getLogger(AppIndexService.class);

	private AppDAO appDAO;
	private CloudSolrClient solrClientApp;

	private static int docIndexNum;
	private static boolean isIndexingWhole = false;

	public boolean addWhole(String version) {
		logger.info("建立索引启动");
		isIndexingWhole = true;
		int curIndexVersion = Integer.parseInt(version);

		Collection<SolrInputDocument> _docs = new ArrayList<SolrInputDocument>();
		ResultSet rs = appDAO.getAllApps();
		if (rs == null) {
			isIndexingWhole = false;
			return false;
		}
		try {
			solrClientApp.connect();
			logger.info("建立连接");
			System.gc();
			docIndexNum = 0;
			while (rs.next()) {
				SolrInputDocument doc = resultSet2SolrDoc(rs, curIndexVersion
						+ "");
				docIndexNum++;
				if (doc != null)
					_docs.add(doc);
				if (_docs.size() >= 10000) {
					solrClientApp.add(_docs);
					Thread.sleep(1000);
					logger.info("提交");
					solrClientApp.commit();
					Thread.sleep(1000);
					_docs.clear();
				}
			}
			if (!_docs.isEmpty()) {
				solrClientApp.add(_docs);
				logger.info("提交");
				solrClientApp.commit();
				_docs.clear();
			}
			_docs = null;
		} catch (Exception e) {
			logger.error("", e);
			try {
				solrClientApp.deleteByQuery("docversion:" + curIndexVersion);
				solrClientApp.commit();
			} catch (Exception err) {
				logger.error("", err);
				return false;
			}
			return false;
		} finally {
			isIndexingWhole = false;
			DBHelper.closeConn(rs);
			System.gc();
			docIndexNum = 0;
		}
		logger.info("建立索引结束");
		return true;
	}

	public boolean optimized() {
		solrClientApp.connect();
		try {
			solrClientApp.optimize();
		} catch (Exception e) {
			logger.error("", e);
			return false;
		} finally {
		}
		return true;
	}

	public boolean addOne(String id) {
		String curIndexVersion = GlobalConfig._INDEX_VERSION_;
		ResultSet rs = appDAO.getOne(id);
		solrClientApp.connect();
		try {
			if (rs != null) {
				while (rs.next()) {
					SolrInputDocument doc = resultSet2SolrDoc(rs,
							curIndexVersion);
					if (doc != null)
						solrClientApp.add(doc);
				}
				solrClientApp.commit();
			} else {
				logger.error(id + "未查询到任何记录");
				return false;
			}
		} catch (Exception e) {
			logger.error("", e);
			return false;
		} finally {
			DBHelper.closeConn(rs);
		}
		logger.info("提交" + id);
		return true;
	}

	public boolean deleteOne(String id) {
		String indexVersion = GlobalConfig._INDEX_VERSION_;
		solrClientApp.connect();
		try {
			UpdateResponse response = solrClientApp.deleteByQuery("id:" + id
					+ " AND docversion:" + indexVersion);
			if ((Integer) response.getResponseHeader().get("status") == 0) {
				solrClientApp.commit();
				logger.info("删除App索引结束id: " + id);
				return true;
			} else
				return false;
		} catch (Exception e) {
			logger.error("", e);
			return false;
		} finally {
		}
	}

	public boolean deleteWhole(String indexVersion) {
		try {
			solrClientApp.connect();
			// String indexVersion = GlobalConfig._INDEX_VERSION_;
			// 删除所有的索引
			solrClientApp.deleteByQuery("docversion:" + indexVersion);
			solrClientApp.commit();
			logger.info("删除索引结束");
		} catch (Exception e) {
			logger.error("", e);
			return false;
		} finally {
		}
		return true;
	}

	public int getDocIndexNum() {
		return docIndexNum;
	}

	public void setDocIndexNum(int docIndexNum) {
		this.docIndexNum = docIndexNum;
	}

	public static boolean isIndexingWhole() {
		return isIndexingWhole;
	}

	public static void setIndexingWhole(boolean isIndexingWhole) {
		AppIndexService.isIndexingWhole = isIndexingWhole;
	}

	private SolrInputDocument resultSet2SolrDoc(ResultSet rs,
			String curIndexVersion) {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", rs.getString("id"));
			doc.addField("classid", rs.getString("classid"));
			doc.addField("checked", rs.getString("checked"));
			doc.addField("editor", rs.getString("editor"));
			doc.addField("headline", rs.getString("headline"));
			doc.addField("keyboard", rs.getString("keyboard"));
			doc.addField("labels", rs.getString("labels"));
			doc.addField("newstime", rs.getString("newstime"));
			doc.addField("o_cmsid", rs.getString("o_cmsid"));
			doc.addField("o_classid", rs.getString("o_classid"));
			doc.addField("title", rs.getString("title"));
			doc.addField("titleGroup", rs.getString("title"));
			doc.addField("titlepic", rs.getString("titlepic"));
			doc.addField("titleurl", rs.getString("titleurl"));
			doc.addField("top", rs.getString("top"));
			doc.addField("type", rs.getString("type"));
			doc.addField("sharepic", rs.getString("sharepic"));
			doc.addField("docversion", curIndexVersion);
			doc.addField("docTable", rs.getString("docTable"));
			doc.addField(
					"_shard",
					"shard"
							+ (Math.abs(rs.getString("title").hashCode()) % 4 + 1));
			return doc;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public AppDAO getAppDAO() {
		return appDAO;
	}

	public void setAppDAO(AppDAO appDAO) {
		this.appDAO = appDAO;
	}

	public CloudSolrClient getSolrClientApp() {
		return solrClientApp;
	}

	public void setSolrClientApp(CloudSolrClient solrClientApp) {
		this.solrClientApp = solrClientApp;
	}

}
