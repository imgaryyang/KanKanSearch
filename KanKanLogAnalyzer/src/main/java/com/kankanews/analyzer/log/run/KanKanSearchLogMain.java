package com.kankanews.analyzer.log.run;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kankanews.analyzer.log.analyzer.Log4jLogAnalyzer;
import com.kankanews.analyzer.log.analyzer.LogAnalyzer;
import com.kankanews.analyzer.log.model.KanKanSearchObj;
import com.kankanews.analyzer.log.model.Log4jModel;
import com.kankanews.analyzer.log.model.LogModel;
import com.kankanews.analyzer.log.source.FileLogSource;
import com.kankanews.analyzer.log.source.LogSource;

public class KanKanSearchLogMain {
	private static List<KanKanSearchObj> logs = new ArrayList<KanKanSearchObj>();
	private static Map<String, Integer> wordMap = new HashMap<String, Integer>();

	public static void main(String[] args) {
		String root = "C://Users/mu/Desktop/新建文件夹/logs";
		File rootDirectory = new File(root);
		rootDirectory.isDirectory();

		for (String path : rootDirectory.list()) {
			LogSource logSource = new FileLogSource(root + "/" + path);
			LogAnalyzer analyzer = new Log4jLogAnalyzer();
			String line;
			while ((line = logSource.next()) != null) {
				LogModel log = analyzer.parseLine(line);
				if (log != null && log instanceof Log4jModel) {
					String message = ((Log4jModel) log).getMessage();
					if (message.charAt(0) == '|'
							&& message.charAt(message.length() - 1) == '|') {
						// String json = message.substring(1, message.length() -
						// 1);
						// KanKanSearchObj obj = GsonUtil.toObject(json,
						// KanKanSearchObj.class);
						int i = message.indexOf("title:") + 7;
						int j = i;
						if (j == 6 || j >= message.length())
							continue;
						while (message.charAt(j) != ')') {
							j++;
						}
						KanKanSearchObj obj = new KanKanSearchObj();
						obj.setTitle((String) message.subSequence(i, j));
						if (obj != null && obj.getTitle() != null
								&& !obj.getTitle().trim().equals("")) {
							obj.setDate(((Log4jModel) log).getDate());
							logs.add(obj);
						}
					}
				}
			}
			logSource.closs();
		}

		for (KanKanSearchObj search : logs) {
//			String[] words = search.getTitle().split(" ");
//			for (String word : words) {
				 String word = search.getTitle();
				Integer num = wordMap.get(word);
				if (num == null) {
					wordMap.put(word, 1);
				} else {
					wordMap.put(word, num + 1);
//				}
			}
		}
		System.out.println(wordMap.size());
		int num1 = 0;
		for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
			if (entry.getValue() == 1)
				num1++;
		}
		Result[] res = new Result[wordMap.size() - num1];
		Result[] res1 = new Result[num1];
		int i = 0;
		int j = 0;
		for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
			if (entry.getValue() > 1) {
				res[i] = new KanKanSearchLogMain().new Result(entry.getKey(),
						entry.getValue());
				i++;
			} else {
				res1[j] = new KanKanSearchLogMain().new Result(entry.getKey(),
						entry.getValue());
				j++;
			}
		}
		// System.out.println(wordMap.get("看看新闻"));
		// System.out.println(wordMap.get("com"));
		quickSort2(res, 0, res.length - 1);
		File file = new File("G://baogao.txt");
		BufferedWriter wirter = null;
		try {
			file.createNewFile();
			wirter = new BufferedWriter(new FileWriter(file));
			for (Result result : res) {
				wirter.write(result.getWord() + " : " + result.getNum());
				wirter.newLine();
				// System.out.println(result.getWord() + " : " +
				// result.getNum());
			}
			for (Result result : res1) {
				wirter.write(result.getWord() + " : " + result.getNum());
				wirter.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				wirter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// System.out.println(res[0].getWord() + " : " + res[0].getNum());
		// System.out.println(res[10691].getWord() + " : " +
		// res[10691].getNum());
	}

	class Result {
		private int num;
		private String word;

		Result(String word, int num) {
			this.word = word;
			this.num = num;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

	}

	/**
	 * for循环的快速排序[只需要这一个方法]
	 * 
	 * @param array
	 * @param start
	 * @param end
	 */
	private static void quickSort(Result[] array, int start, int end) {
		if (start < end) {
			Result key = array[start];// 初始化保存基元
			int i = start, j;// 初始化i,j
			for (j = start + 1; j <= end; j++) {

				if (array[j].getNum() < key.getNum())// 如果此处元素小于基元，则把此元素和i+1处元素交换，并将i加1，如大于或等于基元则继续循环
				{
					Result temp = array[j];
					array[j] = array[i + 1];
					array[i + 1] = temp;
					i++;
				}

			}
			array[start] = array[i];// 交换i处元素和基元
			array[i] = key;
			quickSort(array, start, i - 1);// 递归调用
			quickSort(array, i + 1, end);
		}
	}

	/**
	 * for循环的快速排序[只需要这一个方法]
	 * 
	 * @param array
	 * @param start
	 * @param end
	 */
	private static void quickSort2(Result[] array, int start, int end) {
		if (start < end) {
			Result key = array[start];// 初始化保存基元
			int i = start, j;// 初始化i,j
			for (j = start + 1; j <= end; j++) {

				if (array[j].getNum() > key.getNum())// 如果此处元素小于基元，则把此元素和i+1处元素交换，并将i加1，如大于或等于基元则继续循环
				{
					Result temp = array[j];
					array[j] = array[i + 1];
					array[i + 1] = temp;
					i++;
				}

			}
			array[start] = array[i];// 交换i处元素和基元
			array[i] = key;
			quickSort2(array, start, i - 1);// 递归调用
			quickSort2(array, i + 1, end);
		}
	}
}
