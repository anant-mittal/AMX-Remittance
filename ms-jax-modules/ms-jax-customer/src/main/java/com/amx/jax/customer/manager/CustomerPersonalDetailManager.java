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
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.UpdateCustomerPersonalDetailRequest;
import com.amx.jax.model.request.VerifyCustomerContactRequest;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.util.AmxDBConstants.Status;
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
	JaxNotificationService jaxNotificationService;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;

	private static final Logger log = LoggerFactory.getLogger(CustomerPersonalDetailManager.class);

	@Transactional
	public void updateCustomerPersonalDetail(Customer customer, UpdateCustomerPersonalDetailRequest req) {
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
		if (req.getMobile() != null) {
			countryMetaValidation.validateMobileNumber(customer.getCountryId(), req.getMobile());
			countryMetaValidation.validateMobileNumberLength(customer.getCountryId(), req.getMobile());
			customer.setMobile(req.getMobile());
			customer.setMobileVerified(Status.N);
			CustomerContactVerification cv = customerContactVerificationManager.create(customer, ContactType.MOBILE);
			cvs.add(cv);
		}
		if (req.getEmail() != null) {
			tenantContext.get().validateEmailId(req.getEmail());
			customer.setEmail(req.getEmail());
			customer.setEmailVerified(Status.N);
			cvs.add(customerContactVerificationManager.create(customer, ContactType.EMAIL));
		}
		if (req.getWatsAppMobileNo() != null) {
			customer.setWhatsapp(req.getWatsAppMobileNo().toString());
			customer.setWhatsappPrefix(req.getWatsAppTelePrefix());
			customer.setWhatsAppVerified(Status.N);
			cvs.add(customerContactVerificationManager.create(customer, ContactType.WHATSAPP));
		}
		jaxNotificationService.sendCustomerVerificationNotification(cvs, customer);
		customer.setUpdatedBy(jaxDbService.getCreatedOrUpdatedBy());
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
