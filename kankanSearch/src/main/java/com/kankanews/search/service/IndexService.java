package com.kankanews.search.service;

import org.apache.solr.client.solrj.impl.CloudSolrClient;

import com.kankanews.search.db.dao.VideosDAO;

public class IndexService {
	private VideosDAO videosDAO;
	private CloudSolrClient solrClient;

	public boolean indexWhole() {
		return true;
	}

	public CloudSolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(CloudSolrClient solrClient) {
		this.solrClient = solrClient;
	}

	public VideosDAO getVideosDAO() {
		return videosDAO;
	}

	public void setVideosDAO(VideosDAO videosDAO) {
		this.videosDAO = videosDAO;
	}

}
