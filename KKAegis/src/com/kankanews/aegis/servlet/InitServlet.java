package com.kankanews.aegis.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.kankanews.aegis.utils.KKAegisInit;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 3733269805178483288L;

	@Override
	public void init() throws ServletException {
		new KKAegisInit().initKeyWord();
	}

	@Override
	public void destroy() {
	}
}
