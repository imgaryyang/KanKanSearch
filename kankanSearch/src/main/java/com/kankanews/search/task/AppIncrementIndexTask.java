package com.kankanews.search.task;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.kankanews.search.config.GlobalConfig;
import com.kankanews.search.db.model.IncrementNew;
import com.kankanews.search.service.AppIndexService;
import com.kankanews.search.service.IndexService;
import com.kankanews.search.utils.GsonUtil;
import com.kankanews.search.utils.httpsqs.HttpsqsClient;

public class AppIncrementIndexTask implements Runnable {
	Logger logger = Logger.getLogger(AppIncrementIndexTask.class);

	private HttpsqsClient httpsqsClientApp;
	private AppIndexService appIndexService;

	public void run() {
		while (true) {
			if (!GlobalConfig._IS_INCREMENT_INDEX_
					|| IndexService.isIndexingWhole()) {
				try {
					Thread.currentThread().sleep(60000);
					continue;
				} catch (InterruptedException e) {
					break;
				}
			}
			String str = httpsqsClientApp.getString();
			if (str == null) {
				try {
					Thread.currentThread().sleep(60000);
				} catch (InterruptedException e) {
					break;
				}
			} else {
				Map<String, String> map = GsonUtil.toMap(str);
				if (map.get("ids") == null || map.get("action") == null)
					continue;
				else {
					dualIncrementIndex(map);
				}
			}
		}
	}

	private void dualIncrementIndex(Map<String, String> map) {
		try {
			boolean flag = false;
			if (map.get("action").trim().equals("insert")) {
				flag = appIndexService.addOne(map.get("id"));
				if (!flag) {
					httpsqsClientApp.putString(GsonUtil.toString(map));
					Thread.currentThread().sleep(60000);
				}
			} else if (map.get("action").trim().equals("delete")) {
				flag = appIndexService.deleteOne(map.get("ids"));
				if (!flag) {
					httpsqsClientApp.putString(GsonUtil.toString(map));
					Thread.currentThread().sleep(60000);
				}
			} else if (map.get("action").trim().equals("update")) {
				flag = appIndexService.deleteOne(map.get("ids"));
				if (!flag) {
					httpsqsClientApp.putString(GsonUtil.toString(map));
					Thread.currentThread().sleep(60000);
					return;
				}
				flag = appIndexService.addOne(map.get("id"));
				if (!flag) {
					httpsqsClientApp.putString(GsonUtil.toString(map));
					Thread.currentThread().sleep(60000);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HttpsqsClient getHttpsqsClientApp() {
		return httpsqsClientApp;
	}

	public void setHttpsqsClientApp(HttpsqsClient httpsqsClientApp) {
		this.httpsqsClientApp = httpsqsClientApp;
	}

	public AppIndexService getAppIndexService() {
		return appIndexService;
	}

	public void setAppIndexService(AppIndexService appIndexService) {
		this.appIndexService = appIndexService;
	}

}
