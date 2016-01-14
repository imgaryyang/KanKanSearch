package com.kankanews.search.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kankanews.search.task.TaskManager;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 3733269805178483288L;

	private ApplicationContext app;
	private TaskManager manager;

	@Override
	public void init() throws ServletException {
		System.out.println("在这里初始化了");
		ServletContext servletContext = this.getServletContext();
		app = WebApplicationContextUtils
				.getWebApplicationContext(servletContext); // 获取spring上下文！
		manager = (TaskManager) app.getBean("taskManager");
		manager.start();
	}

	@Override
	public void destroy() {
		manager.destroy();
	}
}
