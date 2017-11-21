package com.amx.jax.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxcore.model.response.ApiResponse;
import com.amx.jax.dbmodel.OracleCustomer;
import com.amx.jax.repository.OracleCustomerRespository;

@Service
public class OracleCustomerService extends AbstractService {

	@Autowired
	private OracleCustomerRespository oracleCustRepo;
	
	public ApiResponse getOracleCust(BigDecimal custid) {
		OracleCustomer cust =  oracleCustRepo.findOne(custid);
		ApiResponse resp = getBlackApiResponse();
		resp.getData().getValues().add(cust);
		return resp;
		
	}

	@Override
	public String getModelType() {
		return "oracle_ciust";
	}

	@Override
	public Class<?> getModelClass() {
		return OracleCustomer.class;
	}

	public ApiResponse saveCust(OracleCustomer userModel) {
		OracleCustomer cust =  oracleCustRepo.save(userModel);
		ApiResponse resp = getBlackApiResponse();
		resp.getData().getValues().add(cust);
		return resp;
	}
}
