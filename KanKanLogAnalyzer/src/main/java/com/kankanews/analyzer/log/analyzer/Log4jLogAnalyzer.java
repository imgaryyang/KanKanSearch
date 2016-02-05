package com.kankanews.analyzer.log.analyzer;

import com.google.gson.Gson;
import com.kankanews.analyzer.log.model.KanKanSearchObj;
import com.kankanews.analyzer.log.model.Log4jModel;
import com.kankanews.analyzer.log.model.LogModel;
import com.kankanews.utils.GsonUtil;

public class Log4jLogAnalyzer implements LogAnalyzer {

	public LogModel parseLine(String oneLineLog) {
		String[] msg = oneLineLog.split(" \\- ");
		if (msg.length == 3) {
			Log4jModel log = new Log4jModel();
			log.setDate(msg[0]);
			log.setLevel(msg[1]);
			log.setMessage(msg[2]);
			return log;
		}
		return null;
	}

}
