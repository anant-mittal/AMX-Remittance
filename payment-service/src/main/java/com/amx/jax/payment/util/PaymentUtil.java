/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.util;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Viki Sangani
 * 15-Dec-2017
 * PaymentUtil.java
 */
public class PaymentUtil {
	
	public static String getMapKeyValueAsString(Map<String, String[]> map) {
		StringBuffer sbuf = new StringBuffer();
		for (java.util.Map.Entry<String, String[]> entry : map.entrySet()) {
			String[] v = entry.getValue();
			String k = entry.getKey();
			sbuf.append(" "+k).append(" = ").append(v.length > 0 ? v[0].toString() : "null");
		}
		return sbuf.toString();
	}
	
	public static String getMapKeyValue(Map<String, Object> map) {
		StringBuffer sbuf = new StringBuffer();
		for (java.util.Map.Entry<String, Object> entry : map.entrySet()) {
			
			String k = entry.getKey();
			
			Object obj = entry.getValue();
			
			if(obj instanceof String){
				String s = (String) obj;
				sbuf.append(" "+k).append(" = ").append(s.length() > 0 ? s : "null");
			} else if (obj instanceof BigDecimal) {
				BigDecimal bd = (BigDecimal) obj;
				String s = bd.toString();
				sbuf.append(" "+k).append(" = ").append(s.length() > 0 ? s : "null");
			}
		}
		return sbuf.toString();
	}

}
