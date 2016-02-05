package test.kankanews.analyzer.log.analyzer;

import org.junit.Test;

import com.kankanews.analyzer.log.analyzer.Log4jLogAnalyzer;

public class Log4jLogAnalyzerTest {

	@Test
	public void test() {
		String log = "2016-01-07 14:03:45,932 - INFO - |{type=1, all=贷款}|";
		Log4jLogAnalyzer analyzer = new Log4jLogAnalyzer();
		analyzer.parseLine(log);
	}

}
