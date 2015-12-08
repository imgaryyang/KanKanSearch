package com.kankanews.search.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kankanews.search.service.IndexService;

@Controller
@RequestMapping("/index")
public class IndexAction {

	Logger logger = Logger.getLogger(IndexAction.class);
	private IndexService indexService;

	@RequestMapping("/test")
	@ResponseBody
	public String testRichText() {
		System.out.println("test");
		return "test";
	}

	@RequestMapping("/whole")
	@ResponseBody
	public String indexWhole() {
		return indexService.indexWhole() + "";
	}

}
