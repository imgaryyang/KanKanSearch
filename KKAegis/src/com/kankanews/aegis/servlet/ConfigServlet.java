package com.kankanews.aegis.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.kankanews.aegis.config.GlobalConfig;
import com.kankanews.aegis.utils.KKAegisFilter;
import com.kankanews.aegis.utils.KKAegisInit;
import com.kankanews.aegis.utils.KKAegisWarning;

/**
 * Servlet implementation class KKShielder
 */
@WebServlet("/config")
public class ConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ConfigServlet.class);
	private static GlobalConfig globalConfig = GlobalConfig.getInstance();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stubr
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		globalConfig.reload();
		new KKAegisInit().initKeyWord();
		out.println("ok");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
}
