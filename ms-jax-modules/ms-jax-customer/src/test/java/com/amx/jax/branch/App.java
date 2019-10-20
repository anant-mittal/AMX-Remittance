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
		cal.add(Calendar.DATE, -1 * CustomerContactVerification.EXPIRY_DAY_WHATS_APP);
		java.util.Date oneDay = new java.util.Date(cal.getTimeInMillis());
		System.out.println(oneDay);
	}
	

}
