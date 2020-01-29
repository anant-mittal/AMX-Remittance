package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.CustomerDiscountsView;
import com.amx.jax.pricer.repository.CustomerDiscountRepository;

@Component
public class CustomerDiscountDao {
	
	@Autowired
	CustomerDiscountRepository customerDiscountRepository;
	
	public CustomerDiscountsView fetchCustomerDiscount(BigDecimal customerId, String discountType, BigDecimal groupId) {
		return customerDiscountRepository.getByCustomerIdAndDiscountTypeAndGroupId(customerId, discountType, groupId);
	}
	
	public List<CustomerDiscountsView> fetchCustomerDiscountByCustAndGroupId(BigDecimal customerId, BigDecimal groupId) {
		return customerDiscountRepository.getByCustomerIdAndGroupId(customerId, groupId);
	}

}
