package com.kankanews.search.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kankanews.search.config.GlobalConfig;

@Controller
@RequestMapping("/config")
public class ConfigAction {

	Logger logger = Logger.getLogger(ConfigAction.class);

	@RequestMapping("/indexVersion/{version}")
	@ResponseBody
	public String indexVersion(@PathVariable String version) {
		GlobalConfig._INDEX_VERSION_ = version;
		return GlobalConfig._INDEX_VERSION_;
	}

	@RequestMapping("/isIncrement/{is}")
	@ResponseBody
	public String isIncrement(@PathVariable Boolean is) {
		GlobalConfig._IS_INCREMENT_INDEX_ = is;
		return GlobalConfig._IS_INCREMENT_INDEX_ + "";
	}

	@RequestMapping("/getConfig")
	@ResponseBody
	public String getConfig() {
		return GlobalConfig._IS_INCREMENT_INDEX_ + ""
				+ GlobalConfig._INDEX_VERSION_;
	}
}
