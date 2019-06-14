package com.jax.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.amx.utils.MaskUtil;

public class MaskUtilTest {

	@Test
	public void testMask() {
		String mobile = "8123";
		String output = MaskUtil.leftMask(mobile, 4, "*");
		assertEquals("******4923", output);
	}
	
	@Test
	public void testMaskEmail() {
		String email = "pras@almullagroup.com";
		String output = MaskUtil.maskEmail(email, 4, "*");
		assertEquals("******4923", output);
	}
}
