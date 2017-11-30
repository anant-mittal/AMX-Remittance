package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.ViewOnlineCustomerCheck;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.exception.InvalidCivilIdException;
import com.amx.jax.exception.UserNotFoundException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.service.CustomerIdProofService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.validation.CustomerValidation;
import com.amx.jax.util.validation.PatternValidator;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserValidationService {

	@Autowired
	private PatternValidator patternValidator;

	@Autowired
	private CustomerValidation custValidation;

	@Autowired
	private ContactDetailService contactDetailService;

	@Autowired
	private MetaData meta;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private CustomerIdProofService idproofService;

	@Autowired
	private BlackListDao blistDao;

	protected void validateLoginId(String loginId) {
		boolean userNameValid = patternValidator.validateUserName(loginId);
		if (!userNameValid) {
			throw new GlobalException("Username is not valid", "INVALID_USERNAME");
		}
		CustomerOnlineRegistration existingCust = custDao.getCustomerByLoginId(loginId);
		if (existingCust != null) {
			throw new GlobalException("Username already taken", "INVALID_USERNAME");
		}
	}

	protected Customer validateCustomerForOnlineFlow(String civilId) {
		Customer cust = custDao.getCustomerByCivilId(civilId);
		if (cust == null) {
			throw new UserNotFoundException("Civil id is not registered at branch, civil id no,: " + civilId);
		}
		if (cust.getMobile() == null) {
			throw new InvalidCivilIdException("Mobile number is empty. Contact branch to update the same.");
		}
		if (cust.getEmail() == null) {
			throw new InvalidCivilIdException("Email is empty. Contact branch to update the same.");
		}
		return cust;
	}

	protected void validateCivilId(String civilId) {
		boolean isValid = custValidation.validateCivilId(civilId, meta.getCountry().getCountryCode());
		if (!isValid) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " is not valid.");
		}
	}

	protected void validatePassword(CustomerOnlineRegistration customer, String password) {
		String dbPwd = customer.getPassword();
		String passwordEncrypted = cryptoUtil.encrypt(customer.getUserName(), password);
		if (!dbPwd.equals(passwordEncrypted)) {
			throw new GlobalException("Incorrect/wrong password", "WRONG PASSWORD");
		}

	}

	protected void validateCustIdProofs(BigDecimal custId) {
		idproofService.validateCustomerIdProofs(custId);
	}

	protected void validateCustomerData(CustomerOnlineRegistration onlineCust, Customer customer) {

		if (customer.getCustomerReference() == null) {
			throw new GlobalException("Invalid Customer Reference", "INVALID_CUSTOMER_REFERENCE");
		}
		// validate contact details
		validateCustContact(customer);
		if (!"Y".equals(customer.getIsActive())) {
			throw new GlobalException("Customer is not active", "CUSTOMER_INACTIVE");
		}
		if (customer.getSignatureSpecimenClob() == null) {
			throw new GlobalException("CUSTOMER SIGNATURE NOT AVAILABLE", "CUSTOMER__SIGNATURE_UNAVAILABLE");
		}
		boolean insuranceCheck = ("Y".equals(customer.getMedicalInsuranceInd())
				|| "N".equals(customer.getMedicalInsuranceInd()));
		if (!insuranceCheck) {
			throw new GlobalException("INVALID MEDICAL INSURANCE INDICATOR", "INVALID_INSURANCE_INDICATOR");
		}
		ViewOnlineCustomerCheck onlineCustView = custDao.getOnlineCustomerview(customer.getCustomerId());
		if (onlineCustView != null && onlineCustView.getIdExpirtyDate() == null) {
			throw new GlobalException("ID is expired", "ID_PROOF_EXPIRED");
		}
		validateBlackListedCustomer(customer);

	}

	private void validateCustContact(Customer customer) {

		List<ContactDetail> contactDetails = contactDetailService.getContactDetail(customer.getCompanyId());
		if (CollectionUtils.isEmpty(contactDetails)) {
			throw new GlobalException("No contact details found", "MISSING_CONTACT_DETAILS");
		}
		boolean ishome = false, islocal = false;
		for (ContactDetail contact : contactDetails) {
			if (contact.getContactTypeId().equals(new BigDecimal(49))) {
				islocal = true;
			}
			if (contact.getContactTypeId().equals(new BigDecimal(50))) {
				ishome = true;
			}
		}
		if (!ishome) {
			throw new GlobalException("No home contact details found", "MISSING_HOME_CONTACT_DETAILS");
		}
		if (!islocal) {
			throw new GlobalException("No local details found", "MISSING_LOCAL_CONTACT_DETAILS");
		}
	}

	private void validateBlackListedCustomer(Customer customer) {

		StringBuffer engNamesbuf = new StringBuffer();
		if (StringUtils.isNotBlank(customer.getFirstName())) {
			engNamesbuf.append(customer.getFirstName().trim());
		}
		if (StringUtils.isNotBlank(customer.getMiddleName())) {
			engNamesbuf.append(customer.getMiddleName().trim());
		}
		if (StringUtils.isNotBlank(customer.getLastName())) {
			engNamesbuf.append(customer.getLastName().trim());
		}
		StringBuffer localNamesbuf = new StringBuffer();
		if (StringUtils.isNotBlank(customer.getFirstNameLocal())) {
			localNamesbuf.append(customer.getFirstNameLocal().trim());
		}
		if (StringUtils.isNotBlank(customer.getMiddleNameLocal())) {
			localNamesbuf.append(customer.getMiddleNameLocal().trim());
		}
		if (StringUtils.isNotBlank(customer.getLastNameLocal())) {
			localNamesbuf.append(customer.getLastNameLocal().trim());
		}
		List<BlackListModel> blist = blistDao.getBlackByName(engNamesbuf.toString());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException("Customer name found matching with black list ", "BLACK_LISTED_CUSTOMER");
		}
		blist = blistDao.getBlackByName(localNamesbuf.toString());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException("Customer local name found matching with black list ", "BLACK_LISTED_CUSTOMER");
		}
	}
}
