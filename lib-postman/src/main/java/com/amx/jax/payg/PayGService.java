package com.amx.jax.payg;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.utils.URLBuilder;

@Component
public class PayGService {

	@Autowired
	private AppConfig appConfig;

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
		String callbackd = Base64.getEncoder().encodeToString(callbackUrl.getBytes());

		builder.path("app/payment").queryParam("amount", payment.getAmount())
				.queryParam("trckid", payment.getTrackId()).queryParam("pg", payment.getServiceCode())
				.queryParam("docFy", payment.getDocFy()).queryParam("docNo", payment.getDocNo())
				.queryParam("docId", payment.getDocId())
				.queryParam("tnt", context.getTenant()).queryParam("callbackd", callbackd)
				.queryParam("prod", payment.getProduct())
				.queryParam(AppConstants.TRACE_ID_XKEY, context.getTraceId());
		return builder.getURL();
	}

}
