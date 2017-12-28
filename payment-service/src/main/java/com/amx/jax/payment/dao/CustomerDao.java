package com.amx.jax.payment.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

//import com.amx.amxlib.model.CustomerModel;
//import com.amx.amxlib.model.SecurityQuestionModel;
//import com.amx.jax.dbmodel.Customer;
//import com.amx.jax.dbmodel.CustomerOnlineRegistration;
//import com.amx.jax.dbmodel.LoyaltyPointModel;
//import com.amx.jax.dbmodel.UserVerificationCheckListModel;
//import com.amx.jax.dbmodel.ViewOnlineCustomerCheck;
//import com.amx.jax.meta.MetaData;
//import com.amx.jax.userservice.repository.CustomerRepository;
//import com.amx.jax.userservice.repository.LoyaltyPointRepository;
//import com.amx.jax.userservice.repository.OnlineCustomerRepository;
//import com.amx.jax.userservice.repository.UserVerificationCheckListModelRepository;
//import com.amx.jax.userservice.repository.ViewOnlineCustomerCheckRepository;
//import com.amx.jax.util.CryptoUtil;

@Component
public class CustomerDao {

//	@Autowired
//	private CustomerRepository repo;
//
//	@Autowired
//	private MetaData meta;
//
//	@Transactional
//	public Customer getCustomerByCivilId(String civilId) {
//		Customer cust = null;
//		BigDecimal countryId = meta.getCountryId();
//		List<Customer> customers = repo.getCustomerbyuser(countryId, civilId);
//		if (customers != null && !customers.isEmpty()) {
//			cust = customers.get(0);
//		}
//		return cust;
//	}
//
//	@Transactional
//	public Customer getCustById(BigDecimal id) {
//		return repo.findOne(id);
//	}
//
//	@Transactional
//	public CustomerOnlineRegistration getOnlineCustByCustomerId(BigDecimal customerId) {
//		CustomerOnlineRegistration onlineCust = null;
//		List<CustomerOnlineRegistration> list = onlineCustRepo.getOnlineCustomersById(customerId);
//		if (!CollectionUtils.isEmpty(list) && list.size() >= 1) {
//			onlineCust = list.get(0);
//		}
//		return onlineCust;
//	}



}