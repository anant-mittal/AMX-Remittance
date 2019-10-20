package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import com.amx.utils.StringUtils.StringMatcher;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Phonetest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	public static final Pattern FUN_AS_ALIAS = Pattern.compile("^(sum|any|count|ucount) (.+) (AS|as|As|aS) (.+)$");
	public static final Pattern ROW_AS_ALIAS = Pattern.compile("^(.+) (AS|as|As|aS) (.+)$");

	public static XmlMapper xmlMapper = new XmlMapper();

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {
		StringMatcher sm = new StringMatcher("ucount custId AS uCount");
		if (sm.isMatch(FUN_AS_ALIAS)) {
			System.out.println(String.format("2 %s(%s) as %s", sm.group(1), sm.group(2), sm.group(4)));
		} else if (sm.isMatch(ROW_AS_ALIAS)) {
			System.out.println(String.format("1 %s as %s", sm.group(1), sm.group(3)));
		}

	}

}
