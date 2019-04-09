package com.jax.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.amx.utils.MaskUtil;

public class MaskUtilTest {

	@Test
	public void testMask() {
		String mobile = "8123944923";
		String output = MaskUtil.maskString(mobile, 4, "*");
		assertEquals("******4923", output);
	}
}
