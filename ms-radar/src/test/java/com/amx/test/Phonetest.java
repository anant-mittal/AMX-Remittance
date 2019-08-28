package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import com.amx.utils.TimeUtils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class Phonetest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	public static XmlMapper xmlMapper = new XmlMapper();

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {

		System.out.println("====" + TimeUtils.inHourSlot(4, 1));
		String swissNumberStr = "+96551780287";
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, "IN");
			System.out.println("ISD: " + swissNumberProto.getCountryCode());
			System.out.println("NUM: " + swissNumberProto.getNationalNumber());
		} catch (NumberParseException e) {
			System.err.println("NumberParseException was thrown: " + e.toString());
		}
	}

}
