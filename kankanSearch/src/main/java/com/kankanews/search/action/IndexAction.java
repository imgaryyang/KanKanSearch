package com.kankanews.search.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kankanews.search.service.IndexService;

@Controller
@RequestMapping("/index")
public class IndexAction {

	Logger logger = Logger.getLogger(IndexAction.class);

	@Autowired
	private IndexService indexService;

	@RequestMapping("/test")
	@ResponseBody
	public String testRichText() {
		System.out.println("test");
		return "test";
	}

	@RequestMapping("/add/whole/{version}")
	@ResponseBody
	public String addWhole(@PathVariable final String version) {
		new Thread(new Runnable() {
			public void run() {
				indexService.addWhole(version);
				indexService.optimized();
			}
		}).start();
		return "begin";
	}

	@RequestMapping("/optimized")
	@ResponseBody
	public String optimized() {
		new Thread(new Runnable() {
			public void run() {
				indexService.optimized();
			}
		}).start();
		return "begin";
	}

	@RequestMapping("/get/curIndexNum")
	@ResponseBody
	public String getCurIndexNum() {
		return indexService.getDocIndexNum() + "";
	}

	@RequestMapping("/delete/whole/{version}")
	@ResponseBody
	public String deleteWhole(@PathVariable final String version) {
		return indexService.deleteWhole(version) + "";
	}

}
