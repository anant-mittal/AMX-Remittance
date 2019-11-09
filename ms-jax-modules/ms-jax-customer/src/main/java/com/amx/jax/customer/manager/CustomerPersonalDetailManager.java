package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.service.JaxCustomerContactVerificationService;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.UpdateCustomerPersonalDetailRequest;
import com.amx.jax.model.request.VerifyCustomerContactRequest;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.validation.CountryMetaValidation;
import com.amx.utils.JsonUtil;

@Component
public class CustomerPersonalDetailManager {

	@Autowired
	CustomerDao customerDao;
	@Autowired
	TenantContext<CustomerValidation> tenantContext;
	@Autowired
	CountryMetaValidation countryMetaValidation;
	@Autowired
	JaxDBService jaxDbService;
	@Autowired
	CustomerContactVerificationManager customerContactVerificationManager;
	@Autowired
	JaxCustomerContactVerificationService jaxCustomerContactVerificationService;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;
	@Autowired
	OffsiteCustomerRegManager offsiteCustomerRegManager;

	private static final Logger log = LoggerFactory.getLogger(CustomerPersonalDetailManager.class);

	@Transactional
	public void updateCustomerPersonalDetail(Customer customer, UpdateCustomerPersonalDetailRequest req) {
		log.debug("in updateCustomerPersonalDetail");
		if (req.getDateOfBirth() != null) {
			customer.setDateOfBirth(req.getDateOfBirth());
		}
		if (req.getInsurance() != null) {
			customer.setMedicalInsuranceInd(req.getInsurance() ? ConstantDocument.Yes : ConstantDocument.No);
		}
		if (req.getCustomerSignature() != null) {
			customer.setSignatureSpecimenClob(req.getCustomerSignature());
		}
		List<CustomerContactVerification> cvs = new ArrayList<>();
		// mobile
		if (req.getTelPrefix() != null) {
			customer.setPrefixCodeMobile(req.getTelPrefix());
		}
		if (req.getMobile() != null) {
			countryMetaValidation.validateMobileNumber(customer.getCountryId(), req.getMobile());
			countryMetaValidation.validateMobileNumberLength(customer.getCountryId(), req.getMobile());
			customer.setMobile(req.getMobile());
			CustomerContactVerification cv = customerContactVerificationManager.create(customer, ContactType.MOBILE);
			cvs.add(cv);
		}
		// email
		if (req.getEmail() != null) {
			customer.setEmail(req.getEmail());
			cvs.add(customerContactVerificationManager.create(customer, ContactType.EMAIL));
		}
		// whatsapp
		if (req.getWatsAppTelePrefix() != null) {
			customer.setWhatsappPrefix(req.getWatsAppTelePrefix());
		}
		if (req.getWatsAppMobileNo() != null) {
			customer.setWhatsapp(req.getWatsAppMobileNo().toString());
			cvs.add(customerContactVerificationManager.create(customer, ContactType.WHATSAPP));
		}
		cvs.forEach(x -> jaxCustomerContactVerificationService.sendVerificationLink(customer, x));
		customer.setUpdatedBy(jaxDbService.getCreatedOrUpdatedBy());
		offsiteCustomerRegManager.setNotificationVerificationFlags(customer, req);
		customerDao.saveCustomer(customer);
	}

	@Transactional
	public void verifyContact(VerifyCustomerContactRequest request) {
		log.debug("in verifyContact with contactType {}", JsonUtil.toJson(request));
		BigDecimal customerId = metaData.getCustomerId();
		Customer customer = userService.getCustById(customerId);
		UpdateCustomerPersonalDetailRequest updateRequest = new UpdateCustomerPersonalDetailRequest();
		updateRequest.setEmail(request.getEmail());
		updateRequest.setMobile(request.getMobile());
		updateCustomerPersonalDetail(customer, updateRequest);
	}

}
