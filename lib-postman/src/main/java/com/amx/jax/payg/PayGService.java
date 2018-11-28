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

		String callbackUrl = callback + "?docNo=" + payment.getDocNo() + "&docFy=" + payment.getDocFinYear();
		String callbackd = Base64.getEncoder().encodeToString(callbackUrl.getBytes());

		builder.setPath("app/payment").addParameter("amount", payment.getNetPayableAmount())
				.addParameter("trckid", payment.getMerchantTrackId()).addParameter("pg", payment.getPgCode())
				.addParameter("docFy", payment.getDocFinYear()).addParameter("docNo", payment.getDocNo())
				.addParameter("tnt", context.getTenant()).addParameter("callbackd", callbackd)
				.addParameter("prod", payment.getProduct())
				.addParameter(AppConstants.TRACE_ID_XKEY, context.getTraceId());
		return builder.getURL();
	}

}
