/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.amx.amxlib.meta.model.PaymentResponseDto;

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
	
	public static PaymentResponseDto  generatePaymentResponseDTO(HashMap<String, String> params){
		PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
		
		//paymentResponseDto.setApplicationCountryId(params.get(""));
		paymentResponseDto.setAuth_appNo(params.get("auth_appNo"));
		paymentResponseDto.setTransactionId(params.get("tranId"));
		paymentResponseDto.setResultCode(params.get("result"));
		paymentResponseDto.setPostDate(params.get("postDate"));
		//paymentResponseDto.setCustomerId(params.get(""));
		paymentResponseDto.setTrackId(params.get("trackId"));
		paymentResponseDto.setReferenceId(params.get("referenceId"));
		paymentResponseDto.setUdf1(params.get("udf1"));
		paymentResponseDto.setUdf2(params.get("udf2"));
		paymentResponseDto.setUdf3(params.get("udf3"));
		paymentResponseDto.setUdf4(params.get("udf4"));
		paymentResponseDto.setUdf5(params.get("udf5"));
		
		return paymentResponseDto;
	}
	
	public static HashMap<String, String> generateParameterMapForPaymentCapture(Map<String, String[]> parameters) {

		HashMap<String, String> mapPaymentResponeDetails = new HashMap<String, String>();
		mapPaymentResponeDetails.put("paymentId", parameters.get("paymentid")[0]);
		mapPaymentResponeDetails.put("result", parameters.get("result")[0]);
		mapPaymentResponeDetails.put("auth_appNo", parameters.get("auth")[0]);
		mapPaymentResponeDetails.put("referenceId", parameters.get("ref")[0]);
		mapPaymentResponeDetails.put("postDate", parameters.get("postdate")[0]);
		mapPaymentResponeDetails.put("trackId", parameters.get("trackid")[0]);
		mapPaymentResponeDetails.put("tranId", parameters.get("tranid")[0]);
		mapPaymentResponeDetails.put("responsecode", parameters.get("responsecode")[0]);
		mapPaymentResponeDetails.put("udf1", parameters.get("udf1")[0]);
		mapPaymentResponeDetails.put("udf2", parameters.get("udf2")[0]);
		mapPaymentResponeDetails.put("udf3", parameters.get("udf3")[0]);
		mapPaymentResponeDetails.put("udf4", parameters.get("udf4")[0]);
		mapPaymentResponeDetails.put("udf5", parameters.get("udf5")[0]);
		return mapPaymentResponeDetails;
	}

}
