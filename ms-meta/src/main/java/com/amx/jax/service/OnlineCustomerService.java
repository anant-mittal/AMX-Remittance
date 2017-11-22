package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.CustomerOnlineRegistration;
import com.amx.jax.repository.IOnlineCustomerRepository;

@Service
public class OnlineCustomerService {
	
	@Autowired
	IOnlineCustomerRepository onlineCustomerRepository;
	
	public List<CustomerOnlineRegistration> getOnlineCustomerList(BigDecimal countryId,String userName){
		return onlineCustomerRepository.getOnlineCustomerList(countryId, userName);
	}

}
