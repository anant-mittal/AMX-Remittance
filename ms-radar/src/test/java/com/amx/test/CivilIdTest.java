package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import com.amx.utils.StringUtils.StringMatcher;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class CivilIdTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");
	private static final Pattern FIND_CIVIL_ID = Pattern.compile("Civil ID No[\\n ](\\d{12})\n");
	private static final Pattern FIND_NAME = Pattern.compile("Name ([a-zA-Z ]+)\n");
	private static final Pattern FIND_NATIONALITY = Pattern.compile("Nationality ([a-zA-Z]{1,3})\n");
	private static final Pattern FIND_DATE = Pattern.compile("\n(\\d{2}/\\d{2}/\\d{4})");
	public static final String FIND_DATE_FORMAT_STRING = "dd/MM/yyyy";
	public static final SimpleDateFormat FIND_DATE_FORMAT = new SimpleDateFormat(FIND_DATE_FORMAT_STRING);

	public static XmlMapper xmlMapper = new XmlMapper();

	public static final String STR1 = "STATE OF KUWAIT CIVIL ID CARD\nCivil ID No 273022300825\nName MUBARAK SAAD SAEED\nALRASHEEDI\nNationality SAU\nSex\nBirth Date\nExpiry Date\nG\n23/02/1973\n09/06/2020";

	public static final String STR2 = "STATE OF KUWAIT CIVIL ID CARD\nCivil ID No 293040704063\nName GEETHU NAGAMANI NAGAMANI\nNationality ND\nSex\nBirth Date\nExpiry Date\nsi\nF\n07/04/1993\n20/05/2019";

	public static final String STR3 = "STATE OF KUWAIT CIVIL ID CARD\nCivil ID No\n293040704063\nlall ulali jis\nName GEETHU NAGAMANI NAGAMANI\nu\nNationality ND\nSex\nBirth Date\nsi\nF\n07/04/1993\n20/05/2019\npypiry Date";

	
	public static void main2(String[] args) throws URISyntaxException, IOException {
		getDate("23-02-1973");
	}
	public static void main(String[] args) throws URISyntaxException, IOException {
		// CivilIdValidationService service = new CivilIdValidationService();

		// service.validateCaptcha("285061506787");

		StringMatcher matcher = new StringMatcher(STR1);

		// System.out.println(STR1);

		if (matcher.isMatch(FIND_CIVIL_ID)) {
			System.out.println("CIVIL = " + matcher.group(1));
		}

		if (matcher.isMatch(FIND_NAME)) {
			System.out.println("Name = " + matcher.group(1));
		}

		if (matcher.isMatch(FIND_DATE)) {

			Date now = new Date();
			Date dob = getDate(matcher.group(1));
			Date expiry = null;

			if (dob.after(now)) {
				expiry = dob;
				dob = null;
			}

			if (matcher.find()) {
				Date date2 = getDate(matcher.group(1));
				if (expiry == null) {
					expiry = date2;
				} else if (date2 != null && date2.after(expiry)) {
					expiry = date2;
				} else if (dob == null && date2.before(now)) {
					dob = date2;
				}
			}

			System.out.println("DOB = " + FIND_DATE_FORMAT.format(dob) + "    " + "23/02/1973");
			System.out.println("EXP = " + FIND_DATE_FORMAT.format(expiry) + "    " + "09/06/2020");
			System.out.println("======");
		}

		// service.validate2("280030801246");
		// service.validate2("278122103469");
		// service.validate2("931126916");
	}

	public static Date getDate(String date) {
		try {

			Date x = FIND_DATE_FORMAT.parse(date);
			System.out.println("Input String  =  " + date);
			System.out.println(x.getDate() + " " + x.getMonth() + " " + x.getYear());
			System.out.println("GMT String  =  " + x.toGMTString());
			System.out.println("Local String  =  " + x.toLocaleString());
			System.out.println("Fetched  =  " + date + " ===> " + FIND_DATE_FORMAT.format(x));

			return x;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
