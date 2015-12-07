package com.kankanews.search.service;

import com.kankanews.search.client.SolrClient;
import com.kankanews.search.db.dao.VideosDAO;

public class IndexService {
	private SolrClient solrClient;
	private VideosDAO videosDAO;

	public SolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(SolrClient solrClient) {
		this.solrClient = solrClient;
	}

	public VideosDAO getVideosDAO() {
		return videosDAO;
	}

	public void setVideosDAO(VideosDAO videosDAO) {
		this.videosDAO = videosDAO;
	}

}
