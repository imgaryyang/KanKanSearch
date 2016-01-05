package com.kankanews.search.task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.kankanews.search.core.GsonUtil;
import com.kankanews.search.core.httpsqs.HttpsqsClient;
import com.kankanews.search.db.model.IncrementNew;
import com.kankanews.search.service.IndexService;

public class IncrementIndexTask implements Runnable {
	Logger logger = Logger.getLogger(IncrementIndexTask.class);

	private HttpsqsClient httpsqsClient;
	private IndexService indexService;

	@Override
	public void run() {
		if(true)
		return;
		logger.info("增量索引服务开启");
		while (true) {
			if (IndexService.isIndexingWhole()) {
				try {
					Thread.currentThread().sleep(60000);
					continue;
				} catch (InterruptedException e) {
					break;
				}
			}
			String str = httpsqsClient.getString();
			if (str == null) {
				try {
					Thread.currentThread().sleep(60000);
				} catch (InterruptedException e) {
					break;
				}
			} else {
				IncrementNew incrementNew = GsonUtil.toObject(str,
						IncrementNew.class);
				if (incrementNew.getTable() == null
						|| incrementNew.getId() == null
						|| incrementNew.getClassid() == null
						|| incrementNew.getAction() == null)
					continue;
				else if (incrementNew.getTable().trim().equals("kk_ecms_photo")) {
					dualIncrementIndex(incrementNew);
				} else if (incrementNew.getTable().trim()
						.equals("kk_ecms_kankanvideos")) {
					dualIncrementIndex(incrementNew);
				} else {
					continue;
				}
			}
		}
	}

	private void dualIncrementIndex(IncrementNew incrementNew) {
		boolean flag = false;
		if (incrementNew.getAction().trim().equals("insert")) {
			flag = indexService.addOne(incrementNew);
			if (!flag)
				httpsqsClient.putString(GsonUtil.toString(incrementNew));
		} else if (incrementNew.getAction().trim().equals("delete")) {
			flag = indexService.deleteOne(incrementNew);
			if (!flag)
				httpsqsClient.putString(GsonUtil.toString(incrementNew));
		} else if (incrementNew.getAction().trim().equals("update")) {
			flag = indexService.deleteOne(incrementNew);
			if (!flag) {
				httpsqsClient.putString(GsonUtil.toString(incrementNew));
				return;
			}
			flag = indexService.addOne(incrementNew);
			if (!flag)
				httpsqsClient.putString(GsonUtil.toString(incrementNew));
		}
	}

	public HttpsqsClient getHttpsqsClient() {
		return httpsqsClient;
	}

	public void setHttpsqsClient(HttpsqsClient httpsqsClient) {
		this.httpsqsClient = httpsqsClient;
	}

	public IndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

}
