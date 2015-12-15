package com.kankanews.search.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;
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

	public boolean addWhole() {
		logger.info("建立索引启动");
		String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
		int curIndexVersion = Integer.parseInt(indexVersion) + 1;
		Collection _docs = new ArrayList();
		ResultSet rs = videoDAO.getAllVideo();
		solrClient.connect();
		logger.info("建立连接");
		try {
			// select id, onclick, title, titlepic, newstime, keywords,
			// createtime, videourl
			int i = 0;
			while (rs.next()) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", rs.getObject("id"));
				doc.addField("title", rs.getObject("title"));
				doc.addField("onclick", rs.getObject("onclick"));
				doc.addField("titlepic", rs.getObject("titlepic"));
				doc.addField("newstime", rs.getObject("newstime"));
				doc.addField("keywords", rs.getObject("keywords"));
				doc.addField("createtime", rs.getObject("createtime"));
				doc.addField("videourl", rs.getObject("videourl"));
				doc.addField("docversion", curIndexVersion);
				i++;
				_docs.add(doc);
				if (_docs.size() >= 3000) {
					solrClient.add(_docs);
					logger.info("提交");
					solrClient.commit();
					_docs.clear();
				}
				if (i >= 300000) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			try {
				solrClient.deleteByQuery("docversion:" + curIndexVersion);
				solrClient.commit();
			} catch (Exception err) {
				logger.error(err.getLocalizedMessage());
				return false;
			}
			return false;
		} finally {
			DBHelper.closeAll(rs);
		}
		deleteWhole();
		globalConfig.setProperty("_INDEX_VERSION_", "" + curIndexVersion);
		logger.info("建立索引结束");
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
				doc.addField("createtime", video.getCreateTime());
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
			logger.error(e.getLocalizedMessage());
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
			logger.error(e.getLocalizedMessage());
			return false;
		}
		logger.info("删除索引结束");
		return true;
	}

	public boolean deleteWhole() {
		try {
			String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
			// 删除所有的索引
			solrClient.deleteByQuery("docversion:" + indexVersion);
//			 solrClient.deleteByQuery("*:*");
			solrClient.commit();
			logger.info("删除索引结束");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
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

}
