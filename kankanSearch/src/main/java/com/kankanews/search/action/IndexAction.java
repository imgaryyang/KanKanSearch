package com.kankanews.search.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/index")
public class IndexAction {

	Logger logger = Logger.getLogger(IndexAction.class);

	@RequestMapping("/test")
	@ResponseBody
	public String testRichText() {
		System.out.println("test");
		return "test";
	}

}
