package com.kankanews.search.core.test;

import com.kankanews.search.core.httpsqs.Httpsqs4j;
import com.kankanews.search.core.httpsqs.HttpsqsClient;
import com.kankanews.search.core.httpsqs.HttpsqsException;

public class HttpsqsTest {

	public static void main(String[] args) {
		// put();
		get();
	}

	public static void put() {
		HttpsqsClient client = Httpsqs4j.createNewClient();
		client.setQueueName("q");
		client.putString("my name is a jun kevin1");
		client.putString("my name is a jun kevin2");
		client.putString("my name is a jun kevin3");
	}

	public static String get() {
		HttpsqsClient client = Httpsqs4j.createNewClient();
		try {
			String s = client.getString("q");
			System.out.println(s);
		} catch (HttpsqsException e) {
			e.printStackTrace();
		}
		return null;
	}
}
