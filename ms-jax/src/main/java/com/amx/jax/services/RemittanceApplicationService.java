package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.RemittanceProcedureDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.repository.RemittanceApplicationRepository;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceApplicationService {
	

	
	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;
	
	@Autowired
	ApplicationProcedureDao applRemitDao;
	
	@Autowired
	RemittanceProcedureDao remitDao;
	
	Logger logger = LoggerFactory.getLogger(RemittanceApplicationService.class);
	
	
	/**
	 *  Update the payment id
	 * @param lstPayIdDetails
	 * @param paymnetId
	 * @param udf
	 */
	public void updatePaymentDetails(List<RemittanceApplication> lstPayIdDetails,PaymentResponseDto paymentResponse) {
		try {
			for (RemittanceApplication shoppingCartDataTableBean : lstPayIdDetails) {
				RemittanceApplication remittanceApplication =  remittanceApplicationRepository.findOne(shoppingCartDataTableBean.getRemittanceApplicationId());
				if(remittanceApplication != null && !StringUtils.isBlank(paymentResponse.getPaymentId())){
					remittanceApplication.setPaymentId(paymentResponse.getPaymentId());
					remittanceApplication.setResultCode(paymentResponse.getResultCode());
					remittanceApplication.setPgReferenceId(paymentResponse.getReferenceId());
					remittanceApplication.setPgTransactionId(paymentResponse.getTransactionId());
					remittanceApplication.setPgAuthCode(paymentResponse.getAuth_appNo());
					remittanceApplication.setPgErrorText(paymentResponse.getErrorText());
					remittanceApplication.setPgReceiptDate(paymentResponse.getPostDate());
					remittanceApplication.setApplicaitonStatus("S");
				}
				remittanceApplicationRepository.save(remittanceApplication);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("updatePaymentId faliled for custoemr:"+paymentResponse.getPaymentId()+"\t Status :"+paymentResponse.getResultCode());
		}
		
	}
	
/*public void updatePayTokenNull(List<ShoppingCartDetails> lstShoppingCartAppl,PaymentResponseDto paymentResponse) {
	
	for(ShoppingCartDetails shopAppl : lstShoppingCartAppl) {
		RemittanceApplication appl = remittanceApplicationRepository.findOne(shopAppl.getApplicationId());
		if(appl!=null) {
			appl.setResultCode(paymentResponse.getResultCode());
			appl.setPaymentId(paymentResponse.getPaymentId());
			appl.setPayToken(null);
			appl.setApplicaitonStatus(null);
			appl.setIsactive("D");
			remittanceApplicationRepository.save(appl);
		}
	}
	
}*/




public void updatePayTokenNull(List<RemittanceApplication> lstPayIdDetails,PaymentResponseDto paymentResponse) {
	
	for(RemittanceApplication shopAppl : lstPayIdDetails) {
		RemittanceApplication appl = remittanceApplicationRepository.findOne(shopAppl.getRemittanceApplicationId());
		if(appl!=null) {
			appl.setResultCode(paymentResponse.getResultCode());
			appl.setPaymentId(paymentResponse.getPaymentId());
			appl.setPgReferenceId(paymentResponse.getReferenceId());
			appl.setPgTransactionId(paymentResponse.getTransactionId());
			appl.setResultCode(paymentResponse.getResultCode());
			appl.setPayToken(null);
			appl.setApplicaitonStatus(null);
			appl.setIsactive("D");
			appl.setErrorMessage(paymentResponse.getErrorText());
			remittanceApplicationRepository.save(appl);
		}
	}
	
}
	
	
	public void deActivateApplication(BigDecimal customerId) {
		try {
			
			List<RemittanceApplication> listOfApplication = remittanceApplicationRepository.deActivateNotUsedApplication(new Customer(customerId));
			if(!listOfApplication.isEmpty()) {
				for(RemittanceApplication application : listOfApplication) {
					RemittanceApplication remittanceApplication =  remittanceApplicationRepository.findOne(application.getRemittanceApplicationId());
					remittanceApplication.setIsactive("D");
					remittanceApplication.setApplicaitonStatus(null);
					remittanceApplicationRepository.save(remittanceApplication);
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("deActivateApplication faliled for custoemr:"+customerId);
		}
	}
	
	/**
	 * EX_INSERT_REMITTANCE_ONLINE 
	 * 
	 * @param paymentResponse
	 * @return : To save remittance 
	 */
	public Map<String, Object> saveRemittance(PaymentResponseDto paymentResponse) {
		String result = null;
		logger.info("Payment ID :" + paymentResponse.getPaymentId());
		Map<String, Object> resultMap =null;
		try {
			HashMap<String, Object> inputValues = new HashMap<>();
			inputValues.put("P_APPL_CNTY_ID", paymentResponse.getApplicationCountryId());
			inputValues.put("P_COMPANY_ID", paymentResponse.getCompanyId());
			inputValues.put("P_CUSTOMER_ID", paymentResponse.getCustomerId());
			inputValues.put("P_USER_NAME", paymentResponse.getUserName());
			inputValues.put("P_PAYMENT_ID", paymentResponse.getPaymentId());
			inputValues.put("P_AUTHCOD", paymentResponse.getAuth_appNo());
			inputValues.put("P_TRANID", paymentResponse.getTransactionId());
			inputValues.put("P_REFID", paymentResponse.getReferenceId());
			inputValues.put("P_RESULT", paymentResponse.getResultCode());
			//resultMap = applRemitDao.insertRemittanceOnlineProcedure(inputValues);
			resultMap = remitDao.insertRemittanceForOnline(inputValues);
		} catch (Exception e) {
			logger.error("error occured in save remittance", e);
		}
		return resultMap;
	}
	
	
	

	
	
	
	/**
	 * BigDecimal applicationCountryId = (BigDecimal) inputValues.get("P_APPL_CNTY_ID");
		BigDecimal companyId = (BigDecimal) inputValues.get("P_COMPANY_ID");
		BigDecimal documentId = (BigDecimal) inputValues.get("P_DOCUMENT_ID");
		BigDecimal financialYr = (BigDecimal) inputValues.get("P_DOC_FINYR");
		BigDecimal documentNo = (BigDecimal) inputValues.get("P_DOCUMENT_NO");
	 * @param paymentResponse
	 * @return
	 */
	
	public Map<String, Object> saveRemittancetoOldEmos(PaymentResponseDto paymentResponse) {
		String result = null;
		Map<String, Object> resultMap=null;
		try {
			HashMap<String, Object> inputValues = new HashMap<>();
			inputValues.put("P_APPL_CNTY_ID", paymentResponse.getApplicationCountryId());
			inputValues.put("P_COMPANY_ID", paymentResponse.getCompanyId());
			inputValues.put("P_CUSTOMER_ID", paymentResponse.getCustomerId());
			inputValues.put("P_DOCUMENT_ID", paymentResponse.getCollectionDocumentCode());
			inputValues.put("P_DOC_FINYR", paymentResponse.getCollectionFinanceYear());
			inputValues.put("P_DOCUMENT_NO", paymentResponse.getCollectionDocumentNumber());
			//resultMap = applRemitDao.insertEMOSLIVETransfer(inputValues);
			resultMap = remitDao.insertEMOSLIVETransfer(inputValues);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
}
