package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.services.AbstractService;

@Service
public class CustomerService extends AbstractService {
	
	@Autowired
	ICustomerRepository customerRepository;
	
	
	public ApiResponse  getCustomer(BigDecimal countryId,String userId){
		List<Customer> customerList =customerRepository.getCustomer(countryId, userId);
		ApiResponse response = getBlackApiResponse();
		if(customerList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(customerList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		
		response.getData().setType("customer");
		return response;
	}
	

	
	public ApiResponse  getCustomerByCustomerId(BigDecimal countryId,BigDecimal companyId,BigDecimal customerId){
		List<Customer> customerList =customerRepository.getCustomerByCustomerId(countryId, companyId, customerId);
		ApiResponse response = getBlackApiResponse();
		if(customerList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(customerList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("customer");
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



	public Customer getCustomerDetails(String loginId) {
		
		return customerRepository.getCustomerDetails(loginId);
	}
	
	/*public void updateLoyaltyPoint(BigDecimal loyaltyPoint,BigDecimal countryId,BigDecimal companyId,BigDecimal customerId) {
		customerRepository.updateLoyaltyPoints(loyaltyPoint, companyId, countryId, customerId);
	}*/
	
}
