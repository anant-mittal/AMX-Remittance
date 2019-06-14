package com.amx.jax.payg;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.rest.RestService;
import com.amx.utils.CryptoUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.URLBuilder;

@Component
public class PayGService {

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private RestService restService;

	private static BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	{
		textEncryptor.setPasswordCharArray("ZNEAYuVTsC".toCharArray());
	}

	public String getPaymentUrl(Payment payment, String callback) throws MalformedURLException, URISyntaxException {
		AppContext context = AppContextUtil.getContext();

		URLBuilder builder = new URLBuilder(appConfig.getPaygURL());

		String callbackUrl = callback
				+ "?docNo=" + payment.getDocNo()
				+ "&docFy=" + payment.getDocFinYear()
				+ "&docId=" + payment.getDocId()
				+ "&trckid=" + payment.getMerchantTrackId();
		String callbackd = Base64.getEncoder().encodeToString(callbackUrl.getBytes());

		builder.path("app/payment").queryParam("amount", payment.getNetPayableAmount())
				.queryParam("trckid", payment.getMerchantTrackId()).queryParam("pg", payment.getPgCode())
				.queryParam("docFy", payment.getDocFinYear()).queryParam("docNo", payment.getDocNo())
				.queryParam("docId", payment.getDocId())
				.queryParam("tnt", context.getTenant()).queryParam("callbackd", callbackd)
				.queryParam("prod", payment.getProduct())
				.queryParam(AppConstants.TRACE_ID_XKEY, context.getTraceId());
		return builder.getURL();
	}

	public String getPaymentUrl(PayGParams payment, String callback) throws MalformedURLException, URISyntaxException {
		AppContext context = AppContextUtil.getContext();

		URLBuilder builder = new URLBuilder(appConfig.getPaygURL());

		String callbackUrl = callback
				+ "?docNo=" + payment.getDocNo()
				+ "&docFy=" + payment.getDocFy()
				+ "&docId=" + payment.getDocId()
				+ "&trckid=" + payment.getTrackId();

		if (PayGServiceCode.WT.equals(payment.getServiceCode())) {
			return callbackUrl;
		}

		String callbackd = Base64.getEncoder().encodeToString(callbackUrl.getBytes());

		builder.path("app/payment").queryParam("amount", payment.getAmount())
				.queryParam("trckid", payment.getTrackId()).queryParam("pg", payment.getServiceCode())
				.queryParam("docFy", payment.getDocFy()).queryParam("docNo", payment.getDocNo())
				.queryParam("docId", payment.getDocId())
				.queryParam("tnt", context.getTenant()).queryParam("callbackd", callbackd)
				.queryParam("prod", payment.getProduct())
				.queryParam(AppConstants.TRACE_ID_XKEY, context.getTraceId())
				.queryParam("detail", getEnCryptedDetails(payment.getTrackId(), payment.getAmount(),
						payment.getDocId(), payment.getDocNo(), payment.getDocFy()))
				.queryParam("verify", getVerifyHash(payment.getTrackId(), payment.getAmount(),
						payment.getDocId(), payment.getDocNo(), payment.getDocFy()).getVerification());

		return builder.getURL();
	}

	public String getEnCryptedDetails(String trckid, String amount, String docId, String docNo,
			String docFy) {
		PayGParams payGParams = new PayGParams();
		payGParams.setAmount(amount);
		payGParams.setDocId(docId);
		payGParams.setDocNo(docNo);
		payGParams.setDocFy(docFy);
		return textEncryptor.encrypt(JsonUtil.toJson(payGParams));
	}

	public PayGParams getDeCryptedDetails(String enCryptedDetails) {
		String jsonDetails = textEncryptor.decrypt(enCryptedDetails);
		return JsonUtil.fromJson(jsonDetails, PayGParams.class);
	}

	public PayGParams getVerifyHash(String trckid, String amount, String docId, String docNo,
			String docFy) {
		PayGParams payGParams = new PayGParams();
		payGParams.setAmount(amount);
		payGParams.setDocId(docId);
		payGParams.setDocNo(docNo);
		payGParams.setDocFy(docFy);
		try {
			payGParams.setVerification(CryptoUtil.getMD5Hash((JsonUtil.toJson(payGParams))));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return payGParams;
	}

}
