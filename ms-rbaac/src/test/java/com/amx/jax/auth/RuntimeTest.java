package com.amx.jax.auth;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.amx.utils.CryptoUtil;
import com.amx.utils.DateUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.RoundUtil;

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

	public static void main(String[] args) throws NoSuchAlgorithmException {

		System.out.println(" Date Now  ==>  "
				+ DateUtil.formatDate(DateUtil.getCurrentDateWithTime("Indian/Mauritius"), "us", "24_hr"));

		/*
		 * ******** TimeZones ********** Asia/Kolkata Asia/Kuwait Asia/Karachi
		 * America/New_York Asia/Singapore Australia/Sydney America/Los_Angeles
		 */
		ZoneId zoneId = ZoneId.of("America/Los_Angeles");

		ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), zoneId);

		System.out.println(" Hour ==>   " + zdt.getHour());

		System.out.println(" Minutes ==>   " + zdt.getMinute());

		System.out.println(" Hour Minutes ==>   " + (zdt.getHour() * 100 + zdt.getMinute()));

		Date date1 = DateUtil.getCurrentDateAtTime(5, 90, 0, 0);

		Date date2 = DateUtil.getCurrentDateAtTime(23, 30, 0, 0);

		long diffInMillies = date2.getTime() - date1.getTime();

		long diffHr = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		long diffMin = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

		System.out.println(" Hours difference ==>  " + diffHr);
		System.out.println(" Minutes difference ==>  " + (diffMin - (diffHr * 60)));

		System.out.println(
				"Date String ==>  " + zdt.toString() + " Formatted Date String ==>  " + DateUtil.formatDateTime(zdt));

		System.out.println(
				"Date String ==>  " + date2.toString() + " Formatted Date String ==>  " + DateUtil.formatDate(date2));

		BigDecimal bd = new BigDecimal("0.00000000093933");

		// BigDecimal bd = new BigDecimal("9.3933000000E-10");

		System.out.println(
				"\n\n Big Decimal ==>" + bd.toPlainString() + " : " + RoundUtil.round(bd.doubleValue(), 20) + "\n\n");

		zdt = DateUtil.getNextZonedDay(zdt);
		
		System.out.println(" Zone Date 1 ==> " + zdt);

		System.out.println(" Zone Date + 0.25Hrs ==> " + zdt.plusMinutes((long) (0.25 * 60) % 60));
		
		System.out.println(" Zone Date + 1.25Hrs ==> " + zdt.plusMinutes((long) (1.25 * 60) % 60));
		
		System.out.println(" Zone Date + 1.30Hrs ==> " + zdt.plusMinutes((long) (1.30 * 60) % 60));
		
		System.out.println(" Zone Date + 1.50Hrs ==> " + zdt.plusMinutes((long) (1.50 * 60) % 60));
		
		
		System.out.println("\n\n HourMin ==>" + DateUtil.getHrMinIntVal(23, 100));
		
		Date dt = new Date(1552055789000l);
		
		System.out.println("\n\n Date d ==>" + dt);
		
		
		// ZoneId zoneId = ZoneId.of("Indian/Mauritius");

		// System.out.println(" Zone Id ==>" + zoneId.getId() +" : " +
		// zoneId.getAvailableZoneIds());

		System.out.println(" Hour ==>" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

		System.out.println(" Hour ==>" + Calendar.getInstance().get(Calendar.MINUTE));

		NavigableMap<Integer, String> map = new TreeMap<Integer, String>();

		map.put(12, "a");
		map.put(11, "b");
		map.put(10, "c");
		map.put(9, "d");
		map.put(8, "e");
		map.put(7, "a");
		map.put(6, "a");
		map.put(5, "a");
		map.put(4, "a");
		map.put(3, "a");
		map.put(2, "a");
		map.put(1, "a");

		Map<Integer, String> subMap = map.subMap(3, true, 10, true);

		System.out.println(" Sub Map ==> " + JsonUtil.toJson(subMap));

		if (true)
			return;

		System.out.println(" ======== String Test ======= " + "Y".equalsIgnoreCase(null));

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

		System.out.println(" JSON ==> " + JsonUtil.toJson(outMap));

		Map<String, Map<String, Object>> revJsonMap = (Map<String, Map<String, Object>>) JsonUtil
				.fromJson(JsonUtil.toJson(outMap), Map.class);

		System.out.println(" Rev MAP ==> " + JsonUtil.toJson(revJsonMap));

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
