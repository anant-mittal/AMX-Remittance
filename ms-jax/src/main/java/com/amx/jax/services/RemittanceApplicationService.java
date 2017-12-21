package com.amx.jax.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.repository.RemittanceApplicationRepository;

@Service
public class RemittanceApplicationService {
	
	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;
	
	public void updatePaymentId(List<RemittanceApplication> lstPayIdDetails,String paymnetId, String udf) {
		
		for (RemittanceApplication shoppingCartDataTableBean : lstPayIdDetails) {
			RemittanceApplication remittanceApplication =  remittanceApplicationRepository.findOne(shoppingCartDataTableBean.getRemittanceApplicationId());
			
			System.out.println("paymnetId :"+paymnetId+"\t udf :"+udf);
			if(remittanceApplication != null && udf!=null){
				remittanceApplication.setPaymentId(paymnetId);
			}
			remittanceApplicationRepository.save(remittanceApplication);
		}
		
	}
}
