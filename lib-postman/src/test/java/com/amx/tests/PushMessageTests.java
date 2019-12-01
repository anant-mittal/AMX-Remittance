package com.amx.tests;

import java.math.BigDecimal;
import java.text.ParseException;

import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.model.Contact;
import com.amx.jax.postman.model.PushMessage;

public class PushMessageTests { // Noncompliant

	static BigDecimal country = new BigDecimal(30);
	static BigDecimal customer = new BigDecimal(30333);
	static Tenant tnt = Tenant.KWT;
	static String FORMAT = "%10s : %-10s : %10s";

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		test1();
		test2();
		everyOne();
		everyOne(Language.AR);
		customer();
	}

	private static void print(String type, Object expected, Object actual) {
		System.out.println(String.format(FORMAT, type, actual, expected));
	}

	private static void print(String testname, Contact c) {
		System.out.println("Test : " + testname);
		print("country", country, c.getCountry());
		print("tnt", tnt, c.getTenant());
		print("lang", Language.EN, c.getLang());
		print("cusomter", customer, c.getUserid());
	}

	private static void test1() {
		PushMessage msg = new PushMessage();
		msg.addToCountry(tnt, country);
		Contact c = PushMessage.toContact(msg.getTo().get(0));
		print("test1", c);
	}

	private static void test2() {
		PushMessage msg = new PushMessage();
		msg.addToCountry(country);
		Contact c = PushMessage.toContact(msg.getTo().get(0));
		print("test2", c);
	}

	private static void everyOne() {
		PushMessage msg = new PushMessage();
		msg.setLang(Language.HI);
		msg.addToEveryone();
		Contact c = PushMessage.toContact(msg.getTo().get(0));
		print("everyOne", c);
	}

	private static void everyOne(Language lang) {
		PushMessage msg = new PushMessage();
		msg.setLang(lang);
		msg.addToTenant(tnt, lang);
		Contact c = PushMessage.toContact(msg.getTo().get(0));
		print("everyOne", c);
	}

	private static void customer() {
		PushMessage msg = new PushMessage();
		msg.setLang(Language.HI);
		msg.addToUser(customer);
		Contact c = PushMessage.toContact(msg.getTo().get(0));
		print("customer", c);
	}
}
