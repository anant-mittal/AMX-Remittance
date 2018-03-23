package com.amx.jax.amxcore;

import com.amx.utils.UniqueID;

public class App { // Noncompliant

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Long lng = UniqueID.generate();
		System.out.println("====" + lng);
		System.out.println("====" + Long.toString(lng,36));
	}
}
