package com.kankanews.aegis.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class GlobalConfig {
	private static Logger logger = Logger.getLogger(GlobalConfig.class);

	private static final String WORD_FILE_PATH = "WEB-INF" + File.separator
			+ "resources" + File.separator + "lexicon" + File.separator;
	private static final String CONFIG_FILE_PATH = "WEB-INF" + File.separator
			+ "resources" + File.separator + "configs" + File.separator;

	private String wordFilePath;

	private final Properties properties = new Properties();

	public static GlobalConfig getInstance() {
		return GlobalConfig.GlobalConfigHolder.instance;
	}

	private static class GlobalConfigHolder {
		private static GlobalConfig instance = new GlobalConfig();
	}

	private GlobalConfig() {
		String rootUrl = GlobalConfig.class.getResource("/").getPath()
				.replace("%20", " ");
		wordFilePath = rootUrl.substring(0, rootUrl.indexOf("WEB-INF"))
				+ WORD_FILE_PATH;

		loadConfig();
	}

	private void loadConfig() {
		InputStream in = null;
		try {
			String rootUrl = GlobalConfig.class.getResource("/").getPath()
					.replace("%20", " ");
			String realPath = rootUrl.substring(0, rootUrl.indexOf("WEB-INF"))
					+ CONFIG_FILE_PATH;
			in = new FileInputStream(realPath + "GlobalConfig.properties");
			properties.load(in);
		} catch (FileNotFoundException e1) {
			logger.error("", e1);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	public void reload() {
		loadConfig();
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

	public String getWordFilePath() {
		return wordFilePath;
	}

}
