package com.kankanews.search.client;

import org.apache.solr.client.solrj.impl.CloudSolrClient;

public class SolrClient {
	private CloudSolrClient solrClient;
	private String myCollection;

	public CloudSolrClient getSolrClient() {
		return solrClient;
	}

	public void setSolrClient(CloudSolrClient solrClient) {
		this.solrClient = solrClient;
	}

	public String getMyCollection() {
		return myCollection;
	}

	public void setMyCollection(String myCollection) {
		this.myCollection = myCollection;
	}

}
