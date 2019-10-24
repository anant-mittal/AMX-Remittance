package com.amx.stringutiltest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.amx.utils.StringUtils;

public class StringUtilTest {

	@Test
	public void testCapitalize() {
		String s1 = "target";
		String op = StringUtils.capitalize(s1);
		assertEquals("Target", op);

		String s2 = "p";
		String op2 = StringUtils.capitalize(s2);
		assertEquals("P", op2);
	}
}
