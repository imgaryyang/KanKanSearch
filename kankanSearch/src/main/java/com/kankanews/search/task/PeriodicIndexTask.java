package com.kankanews.search.task;

import java.util.Date;

import org.apache.log4j.Logger;

import com.kankanews.search.config.GlobalConfig;
import com.kankanews.search.service.AppIndexService;
import com.kankanews.search.service.IndexService;

public class PeriodicIndexTask {
	Logger logger = Logger.getLogger(PeriodicIndexTask.class);

	private IndexService indexService;
	private AppIndexService appIndexService;
	private long reindexTimeRange;

	public void periodicReindexStart() {
		if (GlobalConfig._IS_INCREMENT_INDEX_) {
			if (GlobalConfig._IS_PERIODIC_INDEX_) {
				logger.info("定时重建索引任务开启");
				long now = new Date().getTime() / 1000;
				boolean flag = indexService
						.reindex(now - reindexTimeRange, now);
				logger.info("任务是否成功:" + flag);
			}
			indexService.optimized();
			appIndexService.optimized();
		}
	}

	public long getReindexTimeRange() {
		return reindexTimeRange;
	}

	public void setReindexTimeRange(long reindexTimeRange) {
		this.reindexTimeRange = reindexTimeRange;
	}

	public IndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public AppIndexService getAppIndexService() {
		return appIndexService;
	}

	public void setAppIndexService(AppIndexService appIndexService) {
		this.appIndexService = appIndexService;
	}

}
