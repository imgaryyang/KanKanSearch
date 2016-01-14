package com.kankanews.search.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kankanews.search.task.IncrementIndexTask;
import com.kankanews.search.task.TaskManager;

public class InitListener implements ServletContextListener {

	private ApplicationContext app;
	private TaskManager manager;

	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		app = WebApplicationContextUtils.getWebApplicationContext(event
				.getServletContext()); // 获取spring上下文！
		manager = (TaskManager) app.getBean("taskManager");
		manager.start();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		manager.destroy();
	}

}
