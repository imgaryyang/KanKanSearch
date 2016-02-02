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
import com.kankanews.aegis.utils.KKAegisWarning;

/**
 * Servlet implementation class KKShielder
 */
@WebServlet("/KKShielder")
public class KKShielder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Gson gson = new Gson();
	private static Logger logger = Logger.getLogger(KKShielder.class);
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
		String json = "{\"size\":-1}";
		String words = request.getParameter("words");
		if (words == null || words.trim().equals("")) {
			out.println(json);
			return;
		}
		String wordname = request.getParameter("lexicon");
		if (wordname != null && !wordname.trim().equals("")) {
			wordname = globalConfig.get(wordname);
		}
		if (wordname == null || wordname.trim().equals("")) {
			wordname = globalConfig.get("global");
		}
		KKAegisFilter aegisFilter = new KKAegisFilter();
		KKAegisWarning aegisWarning = new KKAegisWarning();

		try {
			Set<String> set = aegisFilter.getSensitiveWord(words, 1, wordname);
			aegisWarning.setSize(set.size());
			aegisWarning.setSensitive(set);
			json = gson.toJson(aegisWarning);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			out.println(json);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
}
