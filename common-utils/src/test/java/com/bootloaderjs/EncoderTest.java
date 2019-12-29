package com.bootloaderjs;

import java.util.regex.Pattern;

import com.amx.utils.CryptoUtil;

public class EncoderTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	public static class TestClass {
		private int num;
		private String str;

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}
	}

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		TestClass p = new TestClass();
		p.setNum(3);
		p.setStr("Test");
		String ecd = new CryptoUtil.Encoder().obzect(p).encrypt().encodeBase64().toString();

		TestClass p2 = new CryptoUtil.Encoder().message(ecd).decodeBase64().decrypt().toObzect(TestClass.class);
		System.out.println(p2.getNum() + " " + p2.getStr());

	}

}
