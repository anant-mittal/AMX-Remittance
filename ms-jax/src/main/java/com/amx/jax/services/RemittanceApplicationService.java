package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.RemittanceApplicationRepository;

@Service
public class RemittanceApplicationService {
	
	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;
	
	/**
	 *  Update the payment id
	 * @param lstPayIdDetails
	 * @param paymnetId
	 * @param udf
	 */
	public void updatePaymentId(List<RemittanceApplication> lstPayIdDetails,String paymnetId, String udf) {
		try {
			for (RemittanceApplication shoppingCartDataTableBean : lstPayIdDetails) {
				RemittanceApplication remittanceApplication =  remittanceApplicationRepository.findOne(shoppingCartDataTableBean.getRemittanceApplicationId());
				System.out.println("Update paymnetId :"+paymnetId+"\t udf :"+udf);
				if(remittanceApplication != null && udf!=null){
					remittanceApplication.setPaymentId(paymnetId);
					remittanceApplication.setApplicaitonStatus("S");
				}
				remittanceApplicationRepository.save(remittanceApplication);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new GlobalException("updatePaymentId faliled for custoemr:"+paymnetId+"\t udf3 :"+udf);
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
			
			//remittanceApplicationRepository.deActivateApplication(customerId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("deActivateApplication faliled for custoemr:"+customerId);
		}
	}
}
