package com.amx.jax.customer.api;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.customer.ICustomerProfileService;
import com.amx.jax.customer.manager.CustomerContactVerificationManager;
import com.amx.jax.customer.manager.CustomerPreferenceManager;
import com.amx.jax.customer.service.JaxCustomerContactVerificationService;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.exception.ApiHttpExceptions.ApiHttpArgException;
import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.customer.CustomerContactVerificationDto;
import com.amx.jax.repository.CustomerRepository;
import com.amx.utils.ArgUtil;

@RestController
public class CustProfileController implements ICustomerProfileService {

	private static final Logger LOGGER = LoggerService.getLogger(CustRegController.class);

	@Autowired
	private CustomerContactVerificationManager customerContactVerificationManager;

	@Autowired
	private CustomerPreferenceManager customerPreferenceManager;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	JaxCustomerContactVerificationService jaxCustomerContactVerificationService;

	@Override
	@RequestMapping(value = ApiPath.CONTACT_LINK_CREATE, method = RequestMethod.POST)
	public AmxApiResponse<CustomerContactVerificationDto, Object> createVerificationLink(
			@RequestParam(value = ApiParams.CUSTOMER_ID, required = false) BigDecimal customerId,
			@RequestParam(value = ApiParams.CONTACT_TYPE) ContactType contactType,
			@RequestParam(value = ApiParams.IDENTITY, required = false) String identity) {

		Customer c;
		if (!ArgUtil.isEmpty(identity)) {
			// Active customer record fetched
			c = customerRepository.getActiveCustomerDetails(identity);
		} else if (!ArgUtil.isEmpty(customerId)) {
			c = customerRepository.findOne(customerId);
		} else {
			throw new ApiHttpArgException(ApiStatusCodes.PARAM_MISSING, "Either CivilId or Customer Id is required");
		}

		CustomerContactVerification x = customerContactVerificationManager.create(c, contactType);
		jaxCustomerContactVerificationService.sendVerificationLink(c, x);

		return AmxApiResponse.build(customerContactVerificationManager.convertToDto(x));
	}

	@Override
	@RequestMapping(value = ApiPath.CONTACT_LINK_VALIDATE, method = RequestMethod.POST)
	public AmxApiResponse<CustomerContactVerificationDto, Object> validateVerificationLink(
			@RequestParam(value = ApiParams.LINK_ID) BigDecimal id) {
		CustomerContactVerification x = customerContactVerificationManager.getCustomerContactVerification(id);
		x = customerContactVerificationManager.validate(x);
		return AmxApiResponse.build(customerContactVerificationManager.convertToDto(x));
	}

	@Override
	@RequestMapping(value = ApiPath.CONTACT_LINK_VERIFY_BY_CODE, method = RequestMethod.POST)
	public AmxApiResponse<CustomerContactVerificationDto, Object> verifyLinkByCode(
			@RequestParam(value = ApiParams.IDENTITY) String identity,
			@RequestParam(value = ApiParams.LINK_ID) BigDecimal linkId,
			@RequestParam(value = ApiParams.VERIFICATION_CODE) String code) {
		CustomerContactVerification x = customerContactVerificationManager.verifyByCode(identity, linkId, code);
		return AmxApiResponse.build(customerContactVerificationManager.convertToDto(x));
	}

	@Override
	@RequestMapping(value = ApiPath.CONTACT_LINK_VERIFY_BY_CONTACT, method = RequestMethod.POST)
	public AmxApiResponse<CustomerContactVerificationDto, Object> verifyLinkByContact(
			@RequestParam(value = ApiParams.IDENTITY) String identity,
			@RequestParam(value = ApiParams.CONTACT_TYPE) ContactType type,
			@RequestParam(value = ApiParams.CONTACT) String contact) {
		CustomerContactVerification x = customerContactVerificationManager.verifyByContact(identity, type, contact);
		return AmxApiResponse.build(customerContactVerificationManager.convertToDto(x));
	}

	@Override
	@RequestMapping(value = ApiPath.CUSTOMER_ONLINE_APP_LANGUAGE, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> saveLanguage(
			@RequestParam(value = ApiParams.CUSTOMER_ID) BigDecimal customerId,
			@RequestParam(value = ApiParams.LANGUAGE_ID) BigDecimal languageId) {
		String status = customerPreferenceManager.saveLanguage(customerId, languageId);
		return AmxApiResponse.build(status);
	}

	@Override
	@RequestMapping(value = ApiPath.CONTACT_LINK_RESEND, method = RequestMethod.POST)
	public AmxApiResponse<CustomerContactVerificationDto, Object> resendLink(
			@RequestParam(value = ApiParams.IDENTITY) String identity,
			@RequestParam(value = ApiParams.LINK_ID) BigDecimal linkId,
			@RequestParam(value = ApiParams.VERIFICATION_CODE) String code) {

		Customer c;
		if (!ArgUtil.isEmpty(identity)) {
			c = customerRepository.getActiveCustomerDetails(identity);
		} else {
			throw new ApiHttpArgException(ApiStatusCodes.PARAM_MISSING, "CivilId Id is required");
		}
		CustomerContactVerification newLink = customerContactVerificationManager.resend(c, linkId, code);
		jaxCustomerContactVerificationService.sendVerificationLink(c, newLink);
		return AmxApiResponse.build(customerContactVerificationManager.convertToDto(newLink));
	}

}
