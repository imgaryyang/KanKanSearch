package com.kankanews.search.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kankanews.search.task.IncrementIndexTask;

public class InitListener implements ServletContextListener {

	private ApplicationContext app;
	private Thread incrementIndexTask;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		app = WebApplicationContextUtils.getWebApplicationContext(event
				.getServletContext()); // 获取spring上下文！
		IncrementIndexTask task = (IncrementIndexTask) app
				.getBean("incrementIndexTask");
		if (incrementIndexTask == null)
			incrementIndexTask = new Thread(task);
		incrementIndexTask.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		incrementIndexTask.interrupt();
	}

}
