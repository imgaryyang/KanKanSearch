package com.kankanews.search.core;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	public static Date getTodayDate() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MINUTE, 0); 
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

	public static Date getDateBefore(long beforeTime) {
		Date now = new Date();
		return new Date(now.getTime() - beforeTime);
	}
	
	public static void main(String[] args) {
		long times = 1800;
		Date now = new Date();
		long tttt = now.getTime() - getDateBefore(times).getTime();
		System.out.println(getDateBefore(times));
		System.out.println(tttt);
	}
}
