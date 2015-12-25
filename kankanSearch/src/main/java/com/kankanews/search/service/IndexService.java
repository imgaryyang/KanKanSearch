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
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.kankanews.search.core.DBHelper;
import com.kankanews.search.db.dao.VideoDAO;
import com.kankanews.search.db.model.Video;

public class IndexService {
	Logger logger = Logger.getLogger(IndexService.class);

	private VideoDAO videoDAO;
	private CloudSolrClient solrClient;

	@Autowired
	private Properties globalConfig;

	private static int docIndexNum;

	public boolean addWhole() {
		logger.info("建立索引启动");
		String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
		int curIndexVersion = Integer.parseInt(indexVersion);
		// int curIndexVersion = Integer.parseInt(indexVersion) + 1;
		Collection _docs = new ArrayList();
		ResultSet rs = videoDAO.getAllVideo();
		solrClient.connect();
		logger.info("建立连接");
		try {
			// select id, onclick, title, titlepic, newstime, keywords,
			// createtime, videourl
			docIndexNum = 0;
			while (rs.next()) {
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
				doc.addField("imagegroup", rs.getObject("imagegroup"));
				doc.addField("docversion", curIndexVersion);
				docIndexNum++;
				_docs.add(doc);
				if (_docs.size() >= 30000) {
					solrClient.add(_docs);
					Thread.sleep(1000);
					logger.info("提交");
					solrClient.commit();
					Thread.sleep(1000);
					_docs.clear();
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

	public boolean addOne(String id) {
		String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
		Video video = videoDAO.get(id);
		solrClient.connect();
		try {
			if (video != null) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", video.getId());
				doc.addField("title", video.getTitle());
				doc.addField("onclick", video.getOnclick());
				doc.addField("titlepic", video.getTitlePic());
				doc.addField("newstime", video.getNewsTime());
				doc.addField("keywords", video.getKeyWords());
				doc.addField("videourl", video.getVideoUrl());
				doc.addField("docversion", indexVersion);
				solrClient.add(doc);
				logger.info("提交");
				solrClient.commit();
			} else {
				logger.error("id:" + id + "未查询到任何记录");
				return false;
			}
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		logger.info("新增索引结束");
		return true;
	}

	public boolean deleteOne(String id) {
		String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
		solrClient.connect();
		try {
			solrClient.deleteByQuery("id:" + id + " AND docversion:"
					+ indexVersion);
			solrClient.commit();
			logger.info("删除索引结束");
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		logger.info("删除索引结束");
		return true;
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

}
