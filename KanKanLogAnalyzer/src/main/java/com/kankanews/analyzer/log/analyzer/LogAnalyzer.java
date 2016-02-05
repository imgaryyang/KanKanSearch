package com.kankanews.analyzer.log.analyzer;

import com.kankanews.analyzer.log.model.LogModel;

public interface LogAnalyzer {
	public LogModel parseLine(String oneLineLog);
}
