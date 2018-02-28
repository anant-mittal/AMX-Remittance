package com.amx.jax.amxcore;

import java.util.regex.Pattern;

import com.amx.jax.scope.Tenant;

public class App { // Noncompliant

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("====" + Tenant.fromString("app-kwt").toString());
		System.out.println("====" + Tenant.fromString("app-kwt2").toString());
		System.out.println("====" + Tenant.fromString("app-kwt3").toString());
		System.out.println("====" + Tenant.fromString("app-kwt-q").toString());
		System.out.println("====" + Tenant.fromString("APP-kwt3").toString());

		System.out.println("====" + Tenant.fromString("APp-kWt3").toString());

		System.out.println("====" + Tenant.fromString("APp-OMN").toString());

	}
}
