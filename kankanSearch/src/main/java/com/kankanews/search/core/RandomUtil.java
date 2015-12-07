package com.kankanews.search.core;

import java.util.Random;

public class RandomUtil {
	private static Random radnom = new Random();

	public static String getRandomString(int length) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buf.append(radnom.nextInt(9));
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
		System.out.println(getRandomString(4));
		}
	}
}
