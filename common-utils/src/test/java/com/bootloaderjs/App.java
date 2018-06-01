package com.bootloaderjs;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import com.amx.utils.URLBuilder;
import com.amx.utils.Urly;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println();

		try {
			URLBuilder builder = Urly.parse("http://test.com/testapp/test.do?test_id=1&test_name=SS");

			System.out.println(builder.getURL().toString());

		} catch (MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		}

		// int interval = 30;
		//
		// Long epoch = Math.round(new Date().getTime() / 1000.0);
		// System.out.println(epoch);
		// String elapsed = Long.toString(epoch / interval);
		// String password = String.join("#", elapsed, "");
		// System.out.printf("%s %s %s\n", epoch, elapsed, password);
		// try {
		//
		// MessageDigest md = MessageDigest.getInstance("SHA1");
		// ByteArrayOutputStream pwsalt = new ByteArrayOutputStream();
		// pwsalt.write(password.getBytes("UTF-8"));
		// byte[] unhashedBytes = pwsalt.toByteArray();
		// byte[] digestVonPassword = md.digest(unhashedBytes);
		// System.out.println(bytesToHex(digestVonPassword));
		// } catch (NoSuchAlgorithmException | IOException e) {
		// e.printStackTrace();
		// }

	}
}
