package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.remittance.PaymentLinkModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.IPaymentLinkDetailsRepository;

@Component
public class DirectPaymentLinkDao {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IPaymentLinkDetailsRepository paymentLinkDetailsRepository;

	public void savePaymentLinkDetails(List<PaymentLinkModel> paymentLinkData) {
		paymentLinkDetailsRepository.save(paymentLinkData);
	}

	public List<PaymentLinkModel> deactivatePreviousLink(BigDecimal customerId) {
		return paymentLinkDetailsRepository.deactivatePrevLink(customerId);
	}

	public PaymentLinkModel fetchPaymentLinkId(BigDecimal customerId, String hashVerifyCode) {
		return paymentLinkDetailsRepository.fetchPayLinkIdForCustomer(customerId, hashVerifyCode);
	}

	public PaymentLinkModel validatePaymentLinkByCode(BigDecimal linkId, String verificationCode) {
		return paymentLinkDetailsRepository.fetchPaymentByLinkIdandCode(linkId, verificationCode);
	}

	public void updatePaygDetailsInPayLink(PaymentResponseDto paymentResponse, BigDecimal linkId) {
		try {
			if(paymentResponse!= null && paymentResponse.getUdf3()!=null){
				PaymentLinkModel pgLinkModel =paymentLinkDetailsRepository.findOne(linkId);
				pgLinkModel.setResultCode(paymentResponse.getResultCode());
				pgLinkModel.setPgAuthCode(paymentResponse.getAuth_appNo());
				pgLinkModel.setPgErrorText(paymentResponse.getErrorText());
				pgLinkModel.setPaymentId(paymentResponse.getPaymentId());
				pgLinkModel.setPgTransactionId(paymentResponse.getTransactionId());
				pgLinkModel.setPgReceiptDate(paymentResponse.getPostDate());
				pgLinkModel.setPgReferenceId(paymentResponse.getReferenceId());
				pgLinkModel.setIsActive("P");
				if(paymentResponse.getErrorCategory() != null)
					pgLinkModel.setErrorCategory(paymentResponse.getErrorCategory());
				pgLinkModel.setModifiedDate(new Date());
				paymentLinkDetailsRepository.save(pgLinkModel);
			}else{
				logger.error("Update after PG details Payment Id :"+paymentResponse.getPaymentId()+"\t Udf 3--Pg trnx seq Id :"+paymentResponse.getUdf3()+"Result code :"+paymentResponse.getResultCode());
				throw new GlobalException(JaxError.PAYMENT_UPDATION_FAILED,"PG updatio failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("catch Update after PG details Payment Id :"+paymentResponse.getPaymentId()+"\t Udf 3--Pg trnx seq Id :"+paymentResponse.getUdf3()+"Result code :"+paymentResponse.getResultCode());
			throw new GlobalException(JaxError.PAYMENT_UPDATION_FAILED,"PG updatio failed");
		}
	}

	public void savePaymentLinkApplication(PaymentLinkModel paymentApplication) {
		paymentLinkDetailsRepository.save(paymentApplication);
	}
}
