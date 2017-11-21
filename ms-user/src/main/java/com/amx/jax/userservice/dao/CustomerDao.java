package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.CivilIdOtpModel;
import com.amx.jax.model.CustomerModel;
import com.amx.jax.model.SecurityQuestionModel;
import com.amx.jax.userservice.repository.CustomerRepository;
import com.amx.jax.userservice.repository.OnlineCustomerRepository;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.Util;

@Component
@Transactional
public class CustomerDao {

	@Autowired
	private CustomerRepository repo;

	@Autowired
	private OnlineCustomerRepository onlineCustRepo;

	@Autowired
	private MetaData meta;

	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private Util util;

	public Customer getCustomerByCivilId(String civilId) {
		Customer cust = null;
		BigDecimal countryId = new BigDecimal(meta.getCountryId());
		List<Customer> customers = repo.getCustomer(countryId, civilId);
		if (customers != null && !customers.isEmpty()) {
			cust = customers.get(0);
		}
		return cust;
	}

	public CustomerOnlineRegistration getOnlineCustById(BigDecimal id) {
		return onlineCustRepo.findOne(id);
	}

	public CustomerOnlineRegistration getOnlineCustByUserId(String userName) {
		BigDecimal countryId = new BigDecimal(meta.getCountryId());
		CustomerOnlineRegistration customer = null;
		List<CustomerOnlineRegistration> customers = onlineCustRepo.getOnlineCustomerList(countryId, userName);
		if (!CollectionUtils.isEmpty(customers)) {
			customer = customers.get(0);
		}
		return customer;
	}

	public CustomerOnlineRegistration saveOrUpdateOnlineCustomer(CustomerOnlineRegistration onlineCust,
			CustomerModel model) {
		BigDecimal countryId = new BigDecimal(meta.getCountryId());
		String userId = model.getIdentityId();
		repo.getCustomer(countryId, userId);

		List<SecurityQuestionModel> secQuestions = model.getSecurityquestions();
		if (!CollectionUtils.isEmpty(secQuestions)) {
			setSecurityQuestions(secQuestions, onlineCust);
		}
		if (model.getCaption() != null) {
			onlineCust.setCaption(cryptoUtil.encrypt(userId, model.getCaption()));
		}
		if (model.getImageUrl() != null) {
			onlineCust.setImageUrl(model.getImageUrl());
		}
		if (model.getPassword() != null) {
			onlineCust.setPassword(cryptoUtil.getHash(userId, model.getPassword()));
		}
		onlineCustRepo.save(onlineCust);
		return onlineCust;
	}

	private void setSecurityQuestions(List<SecurityQuestionModel> secQuestions, CustomerOnlineRegistration onlineCust) {
		String userId = onlineCust.getUserName();

		onlineCust.setSecurityQuestion1(secQuestions.get(0).getQuestionSrNo());
		onlineCust.setSecurityAnswer1(cryptoUtil.getHash(userId, secQuestions.get(0).getAnswer()));

		onlineCust.setSecurityQuestion1(secQuestions.get(1).getQuestionSrNo());
		onlineCust.setSecurityAnswer1(cryptoUtil.getHash(userId, secQuestions.get(1).getAnswer()));

		onlineCust.setSecurityQuestion1(secQuestions.get(2).getQuestionSrNo());
		onlineCust.setSecurityAnswer1(cryptoUtil.getHash(userId, secQuestions.get(2).getAnswer()));

		onlineCust.setSecurityQuestion1(secQuestions.get(3).getQuestionSrNo());
		onlineCust.setSecurityAnswer1(cryptoUtil.getHash(userId, secQuestions.get(3).getAnswer()));

		onlineCust.setSecurityQuestion1(secQuestions.get(4).getQuestionSrNo());
		onlineCust.setSecurityAnswer1(cryptoUtil.getHash(userId, secQuestions.get(4).getAnswer()));
	}

	public void saveOnlineCustomer(CustomerOnlineRegistration onlineCust) {
		onlineCustRepo.save(onlineCust);
	}

}
