package com.amx.jax.pricer;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.time.DurationFormatUtils;

import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil;
import com.amx.utils.JsonUtil;

public final class RuntimeTest {

	public static class A implements Comparable<A>, Serializable {

		private static final long serialVersionUID = 1L;

		public BigDecimal i;
		public int j;

		@Override
		public int compareTo(A o) {
			return this.i.compareTo(o.i);
		}
	}

	public byte[] getRSAEncrypted(String modulusString, String publicExponentString, String toBeEncrypted)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException,
			InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException {

		BigInteger modulus = new BigInteger(modulusString.getBytes());
		BigInteger pubExponent = new BigInteger(publicExponentString.getBytes());

		// Create private and public key specs
		RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modulus, pubExponent);

		// Create a key factory
		KeyFactory factory = KeyFactory.getInstance("RSA");

		// Create the RSA private and public keys
		PublicKey pub = factory.generatePublic(publicSpec);

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pub);

		return cipher.doFinal(toBeEncrypted.getBytes());

	}

	public static void main(String[] args)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException,
			InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException {

		BigDecimal hrs = new BigDecimal(245).divide(new BigDecimal(60), 4, RoundingMode.HALF_EVEN);

		long procTimeInMin = Math.round(hrs.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 60);

		System.out.println(" Hrs ==>" + hrs + " Mins ==>" + procTimeInMin);

		String modulusString = "rqP/kEVeoNKndXPx1wCUbpm8irJoC44DCoMPZm64fPZY+68qARcK/iTRzXmfUS9ZBCSqugt3fAdR4L7nJaWM4DVbJvbWvF7Sp8KbaFlh7oCH2k1FPYAe1nBEjg3Ykj+JLawL1F36hmDY7Y1uzgrGvRYaLWrf46+GS0DeMJTiK78=";
		String publicExponentString = "AQAB";

		BigInteger modulus = new BigInteger(modulusString.getBytes());
		BigInteger pubExponent = new BigInteger(publicExponentString.getBytes());

		// Create private and public key specs
		RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modulus, pubExponent);

		// Create a key factory
		KeyFactory factory = KeyFactory.getInstance("RSA");

		// Create the RSA private and public keys
		PublicKey pub = factory.generatePublic(publicSpec);

		Cipher cipher = Cipher.getInstance("RSA");

		// Cipher cipher = Cipher.getInstance("RSA");

		cipher.init(Cipher.ENCRYPT_MODE, pub);

		byte[] encrptedByte = cipher.doFinal("WS_KWCM_1".getBytes());

		// Cipher cipher2 = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, pub);
		System.out.println("DeCrypted ==>" + new String(cipher.doFinal(encrptedByte), "UTF8"));

		System.out.println(" Public Key Hash ==>" + pub.toString());

		// ==========================

		if (true)
			return;

		System.out.println(" ======== String Test ======= " + "Y".equalsIgnoreCase(null));

		BigDecimal b = new BigDecimal(1235454.453888888);
		BigDecimal dec = b.remainder(new BigDecimal(1)).round(new MathContext(3, RoundingMode.HALF_EVEN));

		BigDecimal rounder = new BigDecimal(0.0503444).setScale(3, RoundingMode.HALF_EVEN);

		System.out.println("Rounder ==> " + rounder);

		System.out.println(" Decimal ==> " + dec);

		BigDecimal diffVal = dec.remainder(rounder).round(new MathContext(3, RoundingMode.HALF_EVEN));

		System.out.println(" Diff Val ==> " + diffVal.negate());

		BigDecimal bumpUpVal = rounder.subtract(diffVal);

		System.out.println(" Bump Up Val ==> " + bumpUpVal);

		System.out.println("Round Down ==>" + dec.subtract(diffVal));

		System.out.println(" Int Val ==>  " + b.longValue());

		System.out.println("New Amt Down ==> " + new BigDecimal(b.longValue()).add(dec.add(diffVal.negate())));

		System.out.println(" double ==> " + dec.add(diffVal.negate()).remainder(rounder).doubleValue());

		System.out.println("New Amt Up ==> " + new BigDecimal(b.longValue()).add(dec.add(bumpUpVal)));

		System.out.println(" double 2 ==> " + dec.add(bumpUpVal).remainder(rounder).doubleValue());

		Map<String, Object> innerJsonMap = new HashMap<String, Object>();

		innerJsonMap.put("1", "value1");
		innerJsonMap.put("2", "value2");
		innerJsonMap.put("3", "value3");
		innerJsonMap.put("4", "value4");
		innerJsonMap.put("5", "value5");
		innerJsonMap.put("6", "value6");
		innerJsonMap.put("7", "value7");

		Map<String, Map<String, Object>> outMap = new HashMap<String, Map<String, Object>>();

		outMap.put("map1", innerJsonMap);
		outMap.put("map2", innerJsonMap);
		outMap.put("map3", innerJsonMap);
		outMap.put("map4", innerJsonMap);
		outMap.put("map5", innerJsonMap);

		System.out.println("\n\n JSON ==> " + JsonUtil.toJson(outMap));

		////////// Duration Start////////

		/*
		 * PrettyTime pt = new PrettyTime(new Locale("hi")); // PrettyTime p = new
		 * PrettyTime(); Date then = Date.from(completionDateForeign.toInstant());
		 * 
		 * Duration du = Duration.ofSeconds(processTimeTotal);
		 */

		// System.out.println(" Final Delivery Pretty Time ==> " +
		// pt.approximateDuration(then).getQuantity()
		// + " Unit ==>" + pt.approximateDuration(then).getUnit() + " Duration ==>" +
		// pt.formatDuration(then));

		Duration du = Duration.ofSeconds(180);

		System.out.println(" duration days ==> " + du.toDays() + " Hrs ==> " + du.toHours() + " Mins ==>"
				+ du.toMinutes() + " Secs ==> " + du.getSeconds());

		System.out.println(" Apache formatted ==>" + DurationFormatUtils.formatDurationWords(180 * 1000, true, true));

		////////// Duration End////////

		List<String> list = new ArrayList<String>();

		list.add("value1");
		list.add("value2");
		list.add("value3");
		list.add("value4");
		list.add("value5");
		list.add("value6");
		list.add("value7");
		list.add("value8");

		String jsonL = JsonUtil.toJson(list);

		System.out.println(" List Json ==> " + jsonL);

		System.out.println(" From Json ==>" + JsonUtil.fromJson(jsonL, List.class));

		String devicePairTokenStr = CryptoUtil.getSHA2Hash("HFOSQUZNXGGNF" + Long.toString(10l));

		System.out.println(" Device Pair Token ==> " + devicePairTokenStr);

		List<A> ts = new LinkedList<A>();

		for (int j = 0; j < 10; j++) {
			A a1 = new A();
			a1.i = new BigDecimal(10 - j);
			a1.j = j;

			ts.add(a1);
		}

		Collections.sort(ts);

		System.out.println(" All Sets ==> " + JsonUtil.toJson(ts));

	}

}
