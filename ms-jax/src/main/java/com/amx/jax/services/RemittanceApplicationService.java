package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.jax.dal.ApplicationProcedureDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.RemittanceApplicationRepository;

@Service
public class RemittanceApplicationService {
	

	
	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;
	
	@Autowired
	ApplicationProcedureDao applRemitDao;
	
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
				System.out.println("Update paymnetId :"+paymentResponse.toString());
				if(remittanceApplication != null && !StringUtils.isBlank(paymentResponse.getPaymentId())){
					remittanceApplication.setPaymentId(paymentResponse.getPaymentId());
					remittanceApplication.setResultCode(paymentResponse.getResultCode());
					remittanceApplication.setApplicaitonStatus("S");
				}
				remittanceApplicationRepository.save(remittanceApplication);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("updatePaymentId faliled for custoemr:"+paymentResponse.getPaymentId()+"\t Status :"+paymentResponse.getResultCode());
		}
		
	}
	
public void updatePayTokenNull(List<ShoppingCartDetails> lstShoppingCartAppl,PaymentResponseDto paymentResponse) {
	
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
		Map<String, Object> resultMap = applRemitDao.insertRemittanceOnline(inputValues);
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
		HashMap<String, Object> inputValues = new HashMap<>();
		inputValues.put("P_APPL_CNTY_ID", paymentResponse.getApplicationCountryId());
		inputValues.put("P_COMPANY_ID", paymentResponse.getCompanyId());
		inputValues.put("P_CUSTOMER_ID", paymentResponse.getCustomerId());
		inputValues.put("P_DOCUMENT_ID", paymentResponse.getCollectionDocumentCode());
		inputValues.put("P_DOC_FINYR", paymentResponse.getCollectionFinanceYear());
		inputValues.put("P_DOCUMENT_NO", paymentResponse.getCollectionDocumentNumber());
		Map<String, Object> resultMap = applRemitDao.insertEMOSLIVETransfer(inputValues);
		return resultMap;
	}
	
	
}
