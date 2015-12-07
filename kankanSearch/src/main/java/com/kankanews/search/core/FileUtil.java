package com.kankanews.search.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {

	public static String readFile(String fileAddress) throws IOException {

		FileInputStream fis = new FileInputStream(fileAddress);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader bread = new BufferedReader(isr);

		StringBuilder sb = new StringBuilder();
		String data = bread.readLine();// 一次读入一行，直到读入null为文件结束
		while (data != null) {
			sb.append(data);
			System.out.println(data);
			data = bread.readLine(); // 接着读下一行
		}
		bread.close();
		isr.close();
		fis.close();
		return sb.toString();
	}

}
