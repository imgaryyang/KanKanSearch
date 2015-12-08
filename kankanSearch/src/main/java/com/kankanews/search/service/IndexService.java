package com.kankanews.search.service;

import org.apache.solr.client.solrj.impl.CloudSolrClient;

import com.kankanews.search.db.dao.VideoDAO;

public class IndexService {
	private VideoDAO videoDAO;
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

	public VideoDAO getVideoDAO() {
		return videoDAO;
	}

	public void setVideoDAO(VideoDAO videoDAO) {
		this.videoDAO = videoDAO;
	}

}
