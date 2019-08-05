/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.Tenant;
import com.amx.jax.payg.PayGParams;
import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil;
import com.amx.utils.Urly;

/**
 * @author Viki Sangani 13-Dec-2017 PaymentConstant.java
 */
public class PaymentConstant {

	private PaymentConstant() {

	}

	public static final String ROOT = "/";
	public static final String PAYMENT_API_ENDPOINT = "/app";
	public static final String KWT = "KWT";
	public static final String BHR = "BHR";
	public static final String OMN = "OMN";

	public static class Params {
		public static final String PAYG_CODE = "paygCode";
		public static final String TENANT = "tenant";
		public static final String CHANNEL = "channel";
		public static final String PRODUCT = "product";
		public static final String UUID = "uuid";
	}

	public static class Path {
		public static final String PAYMENT_CAPTURE_CALLBACK_V1 = "/capture/{paygCode}/{tenant}/{channel}/{uuid}";
		public static final String PAYMENT_CAPTURE_CALLBACK_V1_WILDCARD = "/capture/{paygCode}/{tenant}/{channel}/{uuid}/*";
		public static final String PAYMENT_CAPTURE_CALLBACK_V2 = "/v2/capture/{paygCode}/{tenant}/{channel}/{product}/{uuid}";
		public static final String PAYMENT_CAPTURE_CALLBACK_V2_WILDCARD = "/v2/capture/{paygCode}/{tenant}/{channel}/{product}/{uuid}/*";
	}

	public static String getCalbackUrl(Tenant tenant, PayGServiceCode paygCode, Object channel, Object product,
			String uuid) {
		try {
			if (!ArgUtil.isEmpty(product)) {
				return Urly.getBuilder().path(PAYMENT_API_ENDPOINT).path(Path.PAYMENT_CAPTURE_CALLBACK_V2)
						.pathParam(Params.TENANT, tenant)
						.pathParam(Params.PAYG_CODE, paygCode)
						.pathParam(Params.CHANNEL, channel)
						.pathParam(Params.PRODUCT, product)
						.pathParam(Params.UUID, uuid)
						.getRelativeURL();
			} else {
				return Urly.getBuilder().path(PAYMENT_API_ENDPOINT).path(Path.PAYMENT_CAPTURE_CALLBACK_V1)
						.pathParam(Params.TENANT, tenant)
						.pathParam(Params.PAYG_CODE, paygCode)
						.pathParam(Params.CHANNEL, channel)
						.pathParam(Params.PRODUCT, product)
						.pathParam(Params.UUID, uuid)
						.getRelativeURL();
			}
		} catch (MalformedURLException | URISyntaxException e) {
			return null;
		}
	}

	public static String getCalbackUrl(PayGParams payGParams) {
		return getCalbackUrl(payGParams.getTenant(), payGParams.getServiceCode(),
				payGParams.getChannel(), payGParams.getProduct(), payGParams.getUuid());
	}
	
	public static final String KWT_SECRETE_SALT = "KWT-SALT";
	public static final String OMN_SECRETE_SALT = "KWT-SALT";
	public static final String BHR_SECRETE_SALT = "KWT-SALT";

	public static String getHashedSecrete(String paymentParamStr, String secreteSalt) {

		if (paymentParamStr == null) {
			paymentParamStr = "";
		}

		String hashedSecrete;

		try {
			hashedSecrete = CryptoUtil.getSHA2Hash(paymentParamStr + secreteSalt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			hashedSecrete = null;
		}

		return hashedSecrete;

	}
}
