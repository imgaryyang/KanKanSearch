package com.kankanews.analyzer.log.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileLogSource implements LogSource {

	private String filePath;
	private Reader input;

	public FileLogSource(String filePath) {
		this.filePath = filePath;
		try {
			input = new BufferedReader(new FileReader(new File(filePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String next() {
		try {
			String line = ((BufferedReader) input).readLine();
			if (line != null)
				return line;
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void closs() {
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
