package com.kankanews.search.core.httpsqs;


public class Httpsqs4j {
	
	protected static String prefix;
	
	protected static String charset="UTF-8";
	
	protected static boolean configured = false;
	protected static String password="kevin";
	protected static String queuename="q1";
	static
	{
	
		String prefix = "http://192.168.3.159:1218/";
		Httpsqs4j.prefix = prefix + "?";
		if(password!="")
			Httpsqs4j.prefix=prefix+"?auth="+password+"&";
			Httpsqs4j.charset = charset;
			Httpsqs4j.configured = true;
			Httpsqs4j.password=password;
			Httpsqs4j.queuename=queuename;
		
	}
	
	/**
	 * 创建新的客户端对象
	 * 
	 * @return
	 */
	public static HttpsqsClient createNewClient() {
		return new HttpsqsClient();
	}

}
