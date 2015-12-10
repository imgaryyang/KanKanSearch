package com.kankanews.search.factory;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZooKeeperFactory {
	private String zkHost;

	private int maxRetries;

	private int baseSleepTimeMS;

	private String nameSpace;

	public CuratorFramework instanceClient() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMS,
				maxRetries);
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(zkHost).retryPolicy(retryPolicy)
				.namespace(nameSpace).build();
		client.start();
		return client;
	}

	public String getZkHost() {
		return zkHost;
	}

	public void setZkHost(String zkHost) {
		this.zkHost = zkHost;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public int getBaseSleepTimeMS() {
		return baseSleepTimeMS;
	}

	public void setBaseSleepTimeMS(int baseSleepTimeMS) {
		this.baseSleepTimeMS = baseSleepTimeMS;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

}
