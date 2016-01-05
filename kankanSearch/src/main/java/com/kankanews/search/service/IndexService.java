package com.kankanews.search.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.kankanews.search.core.DBHelper;
import com.kankanews.search.core.GsonUtil;
import com.kankanews.search.db.dao.PhotoDAO;
import com.kankanews.search.db.dao.VideoDAO;
import com.kankanews.search.db.model.IncrementNew;
import com.kankanews.search.db.model.Video;

public class IndexService {
	Logger logger = Logger.getLogger(IndexService.class);

	private VideoDAO videoDAO;
	private PhotoDAO photoDAO;
	private CloudSolrClient solrClient;

	@Autowired
	private Properties globalConfig;

	private static int docIndexNum;
	private static boolean isIndexingWhole = false;

	public boolean addWhole() {
		logger.info("建立索引启动");
		isIndexingWhole = true;
		String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
		int curIndexVersion = Integer.parseInt(indexVersion);
		// int curIndexVersion = Integer.parseInt(indexVersion) + 1;
		Collection<SolrInputDocument> _docs = new ArrayList<SolrInputDocument>();
		ResultSet rs = videoDAO.getAllVideo();
		if (rs == null) {
			isIndexingWhole = false;
			return false;
		}
		try {
			solrClient.connect();
			logger.info("建立连接");
			// select id, onclick, title, titlepic, newstime, keywords,
			// createtime, videourl
			docIndexNum = 0;
			while (rs.next()) {
				SolrInputDocument doc = resultSet2SolrDoc(rs, curIndexVersion
						+ "");
				if (doc != null)
					solrClient.add(doc);
				docIndexNum++;
				_docs.add(doc);
				if (_docs.size() >= 30000) {
					solrClient.add(_docs);
					Thread.sleep(1000);
					logger.info("提交");
					solrClient.commit();
					Thread.sleep(1000);
					_docs.clear();
					System.gc();
				}
				// if (docIndexNum >= 300000) {
				// break;
				// }
			}
			if (!_docs.isEmpty()) {
				solrClient.add(_docs);
				logger.info("提交");
				solrClient.commit();
				_docs.clear();
			}
			_docs = null;
			System.gc();
		} catch (Exception e) {
			logger.error("", e);
			try {
				solrClient.deleteByQuery("docversion:" + curIndexVersion);
				solrClient.commit();
			} catch (Exception err) {
				logger.error(err);
				return false;
			}
			return false;
		} finally {
			isIndexingWhole = false;
			DBHelper.closeConn(rs);
		}
		// deleteWhole();
		globalConfig.setProperty("_INDEX_VERSION_", "" + curIndexVersion);
		logger.info("建立索引结束");
		return true;
	}

	public boolean optimized() {
		solrClient.connect();
		try {
			solrClient.optimize();
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	public boolean addOne(IncrementNew incrementNew) {
		String curIndexVersion = globalConfig.getProperty("_INDEX_VERSION_");
		String table = incrementNew.getTable();
		ResultSet rs = null;
		if (table.trim().equals("kk_ecms_kankanvideos")) {
			rs = videoDAO.getOne(incrementNew.getId());
		} else if (table.trim().equals("kk_ecms_photo")) {
			rs = photoDAO.getOne(incrementNew.getId());
		}
		solrClient.connect();
		try {
			if (rs != null) {
				while (rs.next()) {
					SolrInputDocument doc = resultSet2SolrDoc(rs,
							curIndexVersion);
					if (doc != null)
						solrClient.add(doc);
				}
				solrClient.commit();
			} else {
				logger.error(GsonUtil.toString(incrementNew) + "未查询到任何记录");
				return false;
			}
		} catch (Exception e) {
			logger.error(e);
			return false;
		} finally {
			DBHelper.closeConn(rs);
		}
		logger.info("提交" + incrementNew.getId());
		return true;
	}

	public boolean deleteOne(IncrementNew incrementNew) {
		String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
		solrClient.connect();
		try {
			UpdateResponse response = solrClient.deleteByQuery("id:"
					+ incrementNew.getId() + " AND docversion:" + indexVersion);
			if ((Integer) response.getResponseHeader().get("status") == 0) {
				solrClient.commit();
				logger.info("删除索引结束" + incrementNew.getId());
				return true;
			} else
				return false;
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}

	public boolean deleteWhole() {
		try {
			String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
			// int curIndexVersion = Integer.parseInt(indexVersion) + 1;
			// 删除所有的索引
			solrClient.deleteByQuery("docversion:" + indexVersion);
			// solrClient.deleteByQuery("*:*");
			solrClient.commit();
			logger.info("删除索引结束");
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	public CloudSolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(CloudSolrClient solrClient) {
		this.solrClient = solrClient;
	}

	public VideoDAO getVideoDAO() {
		return videoDAO;
	}

	public void setVideoDAO(VideoDAO videoDAO) {
		this.videoDAO = videoDAO;
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
		IndexService.isIndexingWhole = isIndexingWhole;
	}

	public PhotoDAO getPhotoDAO() {
		return photoDAO;
	}

	public void setPhotoDAO(PhotoDAO photoDAO) {
		this.photoDAO = photoDAO;
	}

	private SolrInputDocument resultSet2SolrDoc(ResultSet rs,
			String curIndexVersion) {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", rs.getObject("id"));
			doc.addField("classid", rs.getObject("classid"));
			doc.addField("type", rs.getObject("type"));
			doc.addField("checked", rs.getObject("checked"));
			doc.addField("title", rs.getObject("title"));
			doc.addField("titleGroup", rs.getObject("title"));
			doc.addField("onclick", rs.getObject("onclick"));
			doc.addField("titlepic", rs.getObject("titlepic"));
			doc.addField("newstime", rs.getObject("newstime"));
			doc.addField("keywords", rs.getObject("keywords"));
			doc.addField("videourl", rs.getObject("videourl"));
			doc.addField("titleurl", rs.getObject("titleurl"));
			doc.addField("authorid", rs.getObject("authorid"));
			doc.addField("author", rs.getObject("author"));
			doc.addField("intro", rs.getObject("intro"));
			doc.addField("taskid", rs.getObject("taskid"));
			doc.addField("sourceid", rs.getObject("sourceid"));
			doc.addField("imagegroup", rs.getObject("imagegroup"));
			doc.addField("docversion", curIndexVersion);
			doc.addField("docTable", rs.getObject("docTable"));
			return doc;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

}
