package com.amx.jax.branch;

import java.util.Calendar;

import com.amx.jax.dbmodel.CustomerContactVerification;

public class App { // Noncompliant
	/**
	 * This is just a test method
	 * 
	 * @param args
	 */

	public static void main(String[] args) {

		Calendar cal = Calendar.getInstance();
		System.out.println(new java.util.Date(cal.getTimeInMillis()));
		cal.add(Calendar.HOUR_OF_DAY, -1 * 48);
		java.util.Date oneDay = new java.util.Date(cal.getTimeInMillis());
		System.out.println(oneDay);
	}

}
