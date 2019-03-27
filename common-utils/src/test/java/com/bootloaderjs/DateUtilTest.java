package com.bootloaderjs;

import java.util.Date;

import com.amx.utils.DateUtil;

public class DateUtilTest {

	public static void main(String args[]) {
		String dateTime = DateUtil.formatDateTime(new Date());
		System.out.println(dateTime);
	}
}
