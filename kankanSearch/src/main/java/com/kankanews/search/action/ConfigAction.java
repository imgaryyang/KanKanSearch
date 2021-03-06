package com.kankanews.search.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kankanews.search.config.GlobalConfig;

@Controller
public class ConfigAction {

	Logger logger = Logger.getLogger(ConfigAction.class);

	@RequestMapping("/config/indexVersion/{version}")
	@ResponseBody
	public String indexVersion(@PathVariable String version) {
		GlobalConfig._INDEX_VERSION_ = version;
		return GlobalConfig._INDEX_VERSION_;
	}

	@RequestMapping("/config/isIncrement/{is}")
	@ResponseBody
	public String isIncrement(@PathVariable Boolean is) {
		GlobalConfig._IS_INCREMENT_INDEX_ = is;
		return GlobalConfig._IS_INCREMENT_INDEX_ + "";
	}

	@RequestMapping("/config/isPeriodicIndex/{is}")
	@ResponseBody
	public String isPeriodicIndex(@PathVariable Boolean is) {
		GlobalConfig._IS_PERIODIC_INDEX_ = is;
		return GlobalConfig._IS_PERIODIC_INDEX_ + "";
	}

	@RequestMapping("/config/getConfig")
	@ResponseBody
	public String getConfig() {
		return "_IS_INCREMENT_INDEX_ : " + GlobalConfig._IS_INCREMENT_INDEX_
				+ "<br/>" + "_INDEX_VERSION_ : " + GlobalConfig._INDEX_VERSION_
				+ "<br/>" + "_IS_PERIODIC_INDEX_ : "
				+ GlobalConfig._IS_PERIODIC_INDEX_;
	}

	@RequestMapping("/config")
	@ResponseBody
	public ModelAndView show() {
		ModelAndView model = new ModelAndView("home");
		model.addObject("_IS_INCREMENT_INDEX_",
				GlobalConfig._IS_INCREMENT_INDEX_);
		return model;
	}
}
