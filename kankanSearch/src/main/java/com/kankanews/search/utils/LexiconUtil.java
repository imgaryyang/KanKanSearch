package com.kankanews.search.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class LexiconUtil {
	public static void main(String[] args) {
		// insertDB();
		getAllLexicon();
	}

	public static void getAllLexicon() {
		Connection con = null;
		BufferedWriter writer = null;
		try {
			con = DBHelper.getConnection();
			File file = new File("G://Lexicon.txt");
			if (!file.exists())
				file.createNewFile();
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			Statement st = con.createStatement();
			ResultSet result = st
					.executeQuery("select word from kk_lexicon where length(word) > 3 order by pinyin");
			int i = 0;
			String newLine = "\r\n";
			while (result.next()) {
				System.out.println(++i + " " + result.getString(1));
				writer.write(result.getString(1));
				writer.write(newLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertDB() {
		Connection con = null;
		try {
			con = DBHelper.getConnection();
			con.setAutoCommit(true);
			Statement st = con.createStatement();
			File[] files = null;
			File file = new File("G://词库");
			if (file.isDirectory())
				files = file.listFiles();
			else
				files = new File[] { file };
			for (File lexicon : files) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(lexicon)));
				while (true) {
					String word = reader.readLine();
					if (word == null || word.trim().equals(""))
						break;
					if (word.length() > 6)
						continue;
					// String[] arr = str.split("	");
					String pinyin = PinYinUtil.converterToFirstSpell(word);

					System.out.println(word + " : " + pinyin.split(",")[0]);
					st.execute("insert into kk_lexicon_copy1 values(null,'"
							+ word + "','" + pinyin.split(",")[0] + "')");
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
