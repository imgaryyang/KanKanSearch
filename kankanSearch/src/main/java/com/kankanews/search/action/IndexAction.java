package com.kankanews.search.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kankanews.search.service.AppIndexService;
import com.kankanews.search.service.IndexService;

@Controller
@RequestMapping("/index")
public class IndexAction {

	Logger logger = Logger.getLogger(IndexAction.class);

	@Autowired
	private IndexService indexService;

	@Autowired
	private AppIndexService appIndexService;

	@RequestMapping("/test")
	@ResponseBody
	public String testRichText() {
		System.out.println("test");
		return "test";
	}

	@RequestMapping("/add/whole/{version}/type/{type}")
	@ResponseBody
	public String addWhole(@PathVariable final String version,
			@PathVariable final String type) {
		if ("app".equals(type)) {
			new Thread(new Runnable() {
				public void run() {
					appIndexService.addWhole(version);
					appIndexService.optimized();
				}
			}).start();
		} else {
			new Thread(new Runnable() {
				public void run() {
					indexService.addWhole(version);
					indexService.optimized();
				}
			}).start();
		}
		return "begin";
	}

	@RequestMapping("/optimized")
	@ResponseBody
	public String optimized() {
		new Thread(new Runnable() {
			public void run() {
				indexService.optimized();
				appIndexService.optimized();
			}
		}).start();
		return "begin";
	}

	@RequestMapping("/get/curIndexNum")
	@ResponseBody
	public String getCurIndexNum() {
		return "index : " + indexService.getDocIndexNum() + " <br/>apps : "
				+ appIndexService.getDocIndexNum();
	}

	@RequestMapping("/delete/whole/{version}/type/{type}")
	@ResponseBody
	public String deleteWhole(@PathVariable final String version,
			@PathVariable final String type) {
		if ("app".equals(type)) {
			return appIndexService.deleteWhole(version) + "";
		} else {
			return indexService.deleteWhole(version) + "";
		}
	}

}
