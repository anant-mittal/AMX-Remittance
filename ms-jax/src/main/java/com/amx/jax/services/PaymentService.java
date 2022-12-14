package com.amx.jax.services;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.service.DirectPaymentLinkService;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.manager.RemittancePaymentManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.util.JaxContextUtil;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@Component
public class PaymentService {

	private Logger logger = Logger.getLogger(PaymentService.class);

	@Autowired
	MetaData metaData;

	@Autowired
	RemittancePaymentManager remittancePaymentManager;

	@Autowired
	FcSaleService fcSaleService;
	
	@Autowired
	DirectPaymentLinkService directPaymentLinkService;

	public AmxApiResponse<PaymentResponseDto, Object> captrueForRemittance(@RequestBody PaymentResponseDto paymentResponse) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_REMITTANCE);
		JaxContextUtil.setRequestModel(paymentResponse);
		logger.info("save-Remittance Controller :" + paymentResponse.getCustomerId() + "\t country ID :"+ paymentResponse.getApplicationCountryId() + "\t Compa Id:" + paymentResponse.getCompanyId());

		logger.info("paymentResponse captrueForRemittance  :"+JsonUtil.toJson(paymentResponse));
		
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		if (customerId != null) {
			paymentResponse.setCustomerId(customerId);
		} else {
			paymentResponse.setCustomerId(new BigDecimal(paymentResponse.getTrackId()));
		}
		paymentResponse.setApplicationCountryId(applicationCountryId);
		paymentResponse.setCompanyId(companyId);
		logger.info("save-Remittance before payment capture :" + customerId + "\t country ID :" + applicationCountryId+ "\t Compa Id:" + companyId);

		return AmxApiResponse.buildList(remittancePaymentManager.paymentCapture(paymentResponse).getResults());
	}

	public AmxApiResponse<PaymentResponseDto, Object> captrueForFxOrder(
			@RequestBody PaymentResponseDto paymentResponse) {
		logger.info("save-fcsale Controller :" + paymentResponse.getCustomerId() + "\t country ID :"
				+ paymentResponse.getApplicationCountryId() + "\t Compa Id:" + paymentResponse.getCompanyId());
		return fcSaleService.savePaymentId(paymentResponse);
	}

	public AmxApiResponse<PaymentResponseDto, Object> captrueForDirectLink(@RequestBody PaymentResponseDto paymentResponse) {
		logger.info("Direct Link :" + paymentResponse.getCustomerId() + "\t country ID :"
				+ paymentResponse.getApplicationCountryId() + "\t Compa Id:" + paymentResponse.getCompanyId());
		BigDecimal linkId = ArgUtil.parseAsBigDecimal(paymentResponse.getPayId());
		return directPaymentLinkService.saveDirectLinkPayment(paymentResponse, linkId);
	}
	
	
	public AmxApiResponse<PaymentResponseDto, Object> captrueForRemittanceV2(@RequestBody PaymentResponseDto paymentResponse) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_REMITTANCE);
		JaxContextUtil.setRequestModel(paymentResponse);
		logger.info("save-Remittance Controller :" + paymentResponse.getCustomerId() + "\t country ID :"+ paymentResponse.getApplicationCountryId() + "\t Compa Id:" + paymentResponse.getCompanyId());
		
		logger.info("paymentResponse captrueForRemittanceV2 :"+JsonUtil.toJson(paymentResponse));
		
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		if (customerId != null) {
			paymentResponse.setCustomerId(customerId);
		} else {
			paymentResponse.setCustomerId(new BigDecimal(paymentResponse.getTrackId()));
		}
		paymentResponse.setApplicationCountryId(applicationCountryId);
		paymentResponse.setCompanyId(companyId);
		logger.info("save-Remittance before payment capture :" + customerId + "\t country ID :" + applicationCountryId+ "\t Compa Id:" + companyId);

		return AmxApiResponse.buildList(remittancePaymentManager.paymentCaptureV2(paymentResponse).getResults());
	}

	
}
