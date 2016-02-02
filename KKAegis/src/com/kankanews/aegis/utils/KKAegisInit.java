package com.kankanews.aegis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.kankanews.aegis.config.GlobalConfig;

public class KKAegisInit {
	private String ENCODING = "UTF-8"; // 字符编码
	private static final String CONFIG_FILE_PATH = "WEB-INF" + File.separator
			+ "resources" + File.separator;

	public static Map _SENSITIVE_WORD_MAP_ = new HashMap();

	private static GlobalConfig globalConfig = GlobalConfig.getInstance();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initKeyWord() {
		try {
			File[] words = new File(globalConfig.getWordFilePath()).listFiles();
			for (File file : words) {
				// 读取敏感词库
				Set<String> keyWordSet = readSensitiveWordFile(file);
				// 将敏感词库加入到HashMap中
				_SENSITIVE_WORD_MAP_.put(file.getName().split("\\.")[0],
						addSensitiveWordToHashMap(keyWordSet));
			}
			// spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
	 * 中 = { isEnd = 0 国 = {<br>
	 * isEnd = 1 人 = {isEnd = 0 民 = {isEnd = 1} } 男 = { isEnd = 0 人 = { isEnd =
	 * 1 } } } } 五 = { isEnd = 0 星 = { isEnd = 0 红 = { isEnd = 0 旗 = { isEnd = 1
	 * } } } }
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map addSensitiveWordToHashMap(Set<String> keyWordSet) {
		Map sensitiveWordMap = new HashMap(keyWordSet.size()); // 初始化敏感词容器，减少扩容操作
		String key = null;
		Map nowMap = null;
		Map<String, String> newWorMap = null;
		// 迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while (iterator.hasNext()) {
			key = iterator.next(); // 关键字
			nowMap = sensitiveWordMap;
			for (int i = 0; i < key.length(); i++) {
				char keyChar = key.charAt(i); // 转换成char型
				Object wordMap = nowMap.get(keyChar); // 获取

				if (wordMap != null) { // 如果存在该key，直接赋值
					nowMap = (Map) wordMap;
				} else { // 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String, String>();
					newWorMap.put("isEnd", "0"); // 不是最后一个
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}

				if (i == key.length() - 1) {
					nowMap.put("isEnd", "1"); // 最后一个
				}
			}
		}
		return sensitiveWordMap;
	}

	/**
	 * 读取敏感词库中的内容，将内容添加到set集合中
	 * 
	 * @return
	 * @version 1.0
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	private Set<String> readSensitiveWordFile(File file) throws Exception {
		Set<String> set = null;

		InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), ENCODING);
		try {
			if (file.isFile() && file.exists()) { // 文件流是否存在
				set = new HashSet<String>();
				BufferedReader bufferedReader = new BufferedReader(read);
				String txt = null;
				while ((txt = bufferedReader.readLine()) != null) { // 读取文件，将文件内容放入到set中
					set.add(txt);
				}
			} else { // 不存在抛出异常信息
				throw new Exception("敏感词库文件不存在");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			read.close(); // 关闭文件流
		}
		return set;
	}
}
