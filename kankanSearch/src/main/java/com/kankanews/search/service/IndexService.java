package com.kankanews.search.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.kankanews.search.core.DBHelper;
import com.kankanews.search.db.dao.VideoDAO;

public class IndexService {
	Logger logger = Logger.getLogger(IndexService.class);

	private VideoDAO videoDAO;
	private CloudSolrClient solrClient;

	public boolean addWhole() {
		logger.info("建立索引启动");
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
				doc.addField("title_ikusersmart", rs.getObject("title"));
				// doc.addField("title_ik", rs.getObject("title"));
				// doc.addField("title", rs.getObject("title"));
				logger.info("添加Doc");
				// solrClient.add(doc);
				// solrClient.commit();
				// logger.info("添加完毕");
				// i++;
				_docs.add(doc);
				if (_docs.size() >= 3000) {
					solrClient.add(_docs);
					logger.info("提交");
					solrClient.commit();
					_docs.clear();
					i = 0;
				}
			}
		} catch (SQLException e) {
			logger.error(e.getLocalizedMessage());
			return false;
		} catch (SolrServerException e) {
			logger.error(e.getLocalizedMessage());
			return false;
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
			return false;
		} finally {
			DBHelper.closeAll(rs);
		}
		logger.info("建立索引结束");
		return true;
	}

	public boolean deleteWhole() {
		try {
			// 删除所有的索引
			solrClient.deleteByQuery("*:*");
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
