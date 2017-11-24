package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.repository.CustomerRepository;
import com.amx.jax.userservice.repository.OnlineCustomerRepository;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.validation.PatternValidator;

@Component
public class CustomerDao {

	@Autowired
	private CustomerRepository repo;

	@Autowired
	private OnlineCustomerRepository onlineCustRepo;

	@Autowired
	private MetaData meta;

	@Autowired
	private CryptoUtil cryptoUtil;


	@Transactional
	public Customer getCustomerByCivilId(String civilId) {
		Customer cust = null;
		BigDecimal countryId = new BigDecimal(meta.getCountryId());
		List<Customer> customers = repo.getCustomerbyuser(countryId, civilId);
		if (customers != null && !customers.isEmpty()) {
			cust = customers.get(0);
		}
		return cust;
	}

	@Transactional
	public CustomerOnlineRegistration getOnlineCustById(BigDecimal id) {
		return onlineCustRepo.findOne(id);
	}

	@Transactional
	public CustomerOnlineRegistration getOnlineCustByCustomerId(BigDecimal customerId) {
		CustomerOnlineRegistration onlineCust = null;
		List<CustomerOnlineRegistration> list = onlineCustRepo.getOnlineCustomersById(customerId);
		if (!CollectionUtils.isEmpty(list) && list.size() >= 1) {
			onlineCust = list.get(0);
		}
		return onlineCust;
	}

	@Transactional
	public CustomerOnlineRegistration getOnlineCustByUserId(String userName) {
		BigDecimal countryId = new BigDecimal(meta.getCountryId());
		CustomerOnlineRegistration customer = null;
		List<CustomerOnlineRegistration> customers = onlineCustRepo.getOnlineCustomerList(countryId, userName);
		if (!CollectionUtils.isEmpty(customers)) {
			customer = customers.get(0);
		}
		return customer;
	}

	@Transactional
	public CustomerOnlineRegistration saveOrUpdateOnlineCustomer(CustomerOnlineRegistration onlineCust,
			CustomerModel model) {
		String userId = model.getIdentityId();

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

		if (model.getLoginId() != null) {
			onlineCust.setLoginId(model.getLoginId());
		}
		if (model.getPassword() != null) {
			onlineCust.setPassword(cryptoUtil.getHash(userId, model.getPassword()));
		}
		onlineCustRepo.save(onlineCust);
		return onlineCust;
	}

	private void setSecurityQuestions(List<SecurityQuestionModel> secQuestions, CustomerOnlineRegistration onlineCust) {
		String userId = onlineCust.getUserName();

		if (secQuestions.get(0).getAnswer() != null) {
			onlineCust.setSecurityQuestion1(secQuestions.get(0).getQuestionSrNo());
			onlineCust.setSecurityAnswer1(cryptoUtil.getHash(userId, secQuestions.get(0).getAnswer()));
		}

		if (secQuestions.get(1).getAnswer() != null) {
			onlineCust.setSecurityQuestion2(secQuestions.get(1).getQuestionSrNo());
			onlineCust.setSecurityAnswer2(cryptoUtil.getHash(userId, secQuestions.get(1).getAnswer()));
		}

		if (secQuestions.get(2).getAnswer() != null) {
			onlineCust.setSecurityQuestion3(secQuestions.get(2).getQuestionSrNo());
			onlineCust.setSecurityAnswer3(cryptoUtil.getHash(userId, secQuestions.get(2).getAnswer()));
		}
		if (secQuestions.get(3).getAnswer() != null) {
			onlineCust.setSecurityQuestion4(secQuestions.get(3).getQuestionSrNo());
			onlineCust.setSecurityAnswer4(cryptoUtil.getHash(userId, secQuestions.get(3).getAnswer()));
		}
		if (secQuestions.get(4).getAnswer() != null) {
			onlineCust.setSecurityQuestion5(secQuestions.get(4).getQuestionSrNo());
			onlineCust.setSecurityAnswer5(cryptoUtil.getHash(userId, secQuestions.get(4).getAnswer()));
		}
	}

	@Transactional
	public void saveOnlineCustomer(CustomerOnlineRegistration onlineCust) {
		onlineCustRepo.save(onlineCust);
	}

	public CustomerOnlineRegistration getCustomerByLoginId(String loginId) {
		return onlineCustRepo.getOnlineCustomersByLoginId(loginId);
	}

}
