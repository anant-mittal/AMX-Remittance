package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceProcedureDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IShoppingCartDetailsDao;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.service.FinancialService;
import com.amx.jax.services.AbstractService;
import com.amx.jax.services.RemittanceApplicationService;
import com.amx.jax.util.Util;


@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittancePaymentManager extends AbstractService{
	private Logger logger = Logger.getLogger(RemittancePaymentManager.class);
	
	@Autowired
	IShoppingCartDetailsDao shoppingCartApplDao;
	
	@Autowired
	FinancialService finanacialService;
	
	@Autowired
	RemittanceApplicationRepository applicationDao;
	
	@Autowired
	RemittanceApplicationService remittanceApplicationService;
	
	@Autowired
	RemittanceProcedureDao remittanceDao;
	
	
	
	
	public ApiResponse paymentCapture(PaymentResponseDto paymentResponse) {
		ApiResponse response = null;
		logger.info("paymment capture :"+paymentResponse.toString());
		List<ShoppingCartDetails>  shoppingCartList = new ArrayList<>();
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		List<RemittanceApplication> lstPayIdDetails =null;
		BigDecimal collectionFinanceYear = null;
		BigDecimal collectionDocumentNumber = null;
		BigDecimal collectionDocumentCode = null;
		String errorMsg = null;
		Map<String,Object> remitanceMap  = null;
		
		
		
		try {
			 response = getBlackApiResponse();
			if(!StringUtils.isBlank(paymentResponse.getPaymentId()) && !StringUtils.isBlank(paymentResponse.getResultCode()) 
			&& (paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.CAPTURED)|| paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.APPROVED))) 
			{
				
				lstPayIdDetails = applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(paymentResponse.getUdf3(),new Customer(paymentResponse.getCustomerId()));
				
				logger.info("Appl :"+lstPayIdDetails.get(0).getRemittanceApplicationId());
				remittanceApplicationService.updatePaymentDetails(lstPayIdDetails, paymentResponse);
				/** Calling stored procedure  insertRemittanceOnline **/
				remitanceMap = remittanceApplicationService.saveRemittance(paymentResponse);
				errorMsg = (String)remitanceMap.get("P_ERROR_MESG");
				
				if(remitanceMap!=null && !remitanceMap.isEmpty() && StringUtils.isBlank(errorMsg)){
					collectionFinanceYear = (BigDecimal)remitanceMap.get("P_COLLECT_FINYR");
					collectionDocumentNumber = (BigDecimal)remitanceMap.get("P_COLLECTION_NO");
					collectionDocumentCode = (BigDecimal)remitanceMap.get("P_COLLECTION_DOCUMENT_CODE");
					errorMsg = (String)remitanceMap.get("P_ERROR_MESG");

					logger.info("EX_INSERT_REMITTANCE_ONLINE collectionFinanceYear : " + collectionFinanceYear);
					logger.info("collectionDocumentNumber : " + collectionDocumentNumber);
					logger.info("collectionDocumentCode : " + collectionDocumentCode);
					logger.info("errorMsg : " + errorMsg);
				
				/** Calling stored procedure  to move remittance to old emos **/
				if(Util.isNullZeroBigDecimalCheck(collectionDocumentNumber)) {
					paymentResponse.setCollectionDocumentCode(collectionDocumentCode);
					paymentResponse.setCollectionDocumentNumber(collectionDocumentNumber);
					paymentResponse.setCollectionFinanceYear(collectionFinanceYear);
					remitanceMap = remittanceApplicationService.saveRemittancetoOldEmos(paymentResponse);
					errorMsg = (String)remitanceMap.get("P_ERROR_MESG");
					paymentResponse.setErrorText(errorMsg);
					logger.info("EX_INSERT_EMOS_TRANSFER_LIVE :"+errorMsg);
					
					/** For Receipt Print **/
					
					response.getData().getValues().add(paymentResponse);
					response.setResponseStatus(ResponseStatus.OK);
				    response.getData().setType("pg_remit_response");
				}
					
				}else {
					
				
					lstPayIdDetails =applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(paymentResponse.getPaymentId(),new Customer(paymentResponse.getCustomerId()));
					if(!lstPayIdDetails.isEmpty()) {
						paymentResponse.setErrorText(errorMsg);
						remittanceApplicationService.updatePayTokenNull(lstPayIdDetails, paymentResponse);
					}
					throw new GlobalException("Remittance error :"+errorMsg,JaxError.PG_ERROR);
					
				}

				
			}else{
				logger.info("PaymentResponseDto "+paymentResponse.getPaymentId()+"\t Result :"+paymentResponse.getResultCode());
				
				
				lstPayIdDetails =applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(paymentResponse.getUdf3(),new Customer(paymentResponse.getCustomerId()));
				if(!lstPayIdDetails.isEmpty()) {
					remittanceApplicationService.updatePayTokenNull(lstPayIdDetails, paymentResponse);
				}
				throw new GlobalException("Remittance error :"+errorMsg,JaxError.PG_ERROR);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
			lstPayIdDetails =applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(paymentResponse.getUdf3(),new Customer(paymentResponse.getCustomerId()));
			if(!lstPayIdDetails.isEmpty()) {
				remittanceApplicationService.updatePayTokenNull(lstPayIdDetails, paymentResponse);
			}
			
			throw new GlobalException("Remittance error :"+errorMsg,JaxError.PG_ERROR);
		}
		
		return response;
	}




	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
