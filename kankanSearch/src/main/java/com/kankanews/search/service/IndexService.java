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
				/**
				 * <field name="id" type="int" indexed="true" stored="true"
				 * required="true" multiValued="false" />
				 * 
				 * <!--<field name="id" type="int" indexed="true" stored="true"
				 * required="true" multiValued="false"/> -->
				 * 
				 * <field name="title_mmseg_complex" type="text_mmseg_complex"
				 * indexed="true" stored="true" /> <field
				 * name="title_mmseg_maxword" type="text_mmseg_maxword"
				 * indexed="true" stored="true" /> <field
				 * name="title_ikusersmart" type="text_ikanalyse_useSmart"
				 * indexed="true" stored="true" /> <field name="title_ik"
				 * type="text_ikanalyse" indexed="true" stored="true" /> <field
				 * name="title" type="text_general" indexed="true" stored="true"
				 * />
				 */
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", rs.getObject("id"));
				// doc.addField("title_mmseg_complex", rs.getObject("title"));
				// doc.addField("title_mmseg_maxword", rs.getObject("title"));
				// doc.addField("title_iksmart", rs.getObject("title"));
				// doc.addField("title_ik", rs.getObject("title"));
				doc.addField("title", rs.getObject("title"));
				doc.addField("onclick", rs.getObject("onclick"));
				doc.addField("titlepic", rs.getObject("titlepic"));
				doc.addField("newstime", rs.getObject("newstime"));
				doc.addField("keywords", rs.getObject("keywords"));
				doc.addField("createtime", rs.getObject("createtime"));
				doc.addField("videourl", rs.getObject("videourl"));
				doc.addField("docversion", curIndexVersion);
				// doc.addField("title_ik", rs.getObject("title"));
				// doc.addField("title", rs.getObject("title"));
				logger.info("添加Doc");
				// solrClient.add(doc);
				// solrClient.commit();
				// logger.info("添加完毕");
				i++;
				_docs.add(doc);
				if (_docs.size() >= 3000) {
					solrClient.add(_docs);
					logger.info("提交");
					solrClient.commit();
					_docs.clear();
					// i = 0;
				}
				if (i >= 30000) {
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

	public boolean deleteWhole() {
		try {
			String indexVersion = globalConfig.getProperty("_INDEX_VERSION_");
			// 删除所有的索引
			solrClient.deleteByQuery("docversion:" + indexVersion);
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
