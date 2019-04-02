package com.amx.jax.customer.api;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.ICustomerProfileService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.customer.manager.CustomerContactVerificationManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.customer.CustomerContactVerificationDto;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CustomerRepository;
import com.mysema.query.types.Template;

@RestController
public class CustProfileController implements ICustomerProfileService {

	private static final Logger LOGGER = LoggerService.getLogger(CustRegController.class);

	@Autowired
	private CustomerContactVerificationManager customerContactVerificationManager;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	private PostManService postManService;

	@Override
	@RequestMapping(value = ApiPath.CONTACT_LINK_CREATE, method = RequestMethod.POST)
	public AmxApiResponse<CustomerContactVerificationDto, Object> createVerificationLink(
			@RequestParam(value = ApiParams.CUSTOMER_ID) BigDecimal customerId,
			@RequestParam(value = ApiParams.CONTACT_TYPE) ContactType contactType) {

		Customer c = customerRepository.findOne(customerId);
		CustomerContactVerification x = customerContactVerificationManager.create(c, contactType);

		if (ContactType.EMAIL.equals(contactType)) {
			Email email = new Email();
			email.addTo(c.getEmail());
			email.setITemplate(TemplatesMX.CONTACT_VERIFICATION_EMAIL);
			email.getModel().put("customer", c);
			email.getModel().put("link", x);
			postManService.sendEmailAsync(email);
		} else if (ContactType.SMS.equals(contactType)) {
			SMS sms = new SMS();
			sms.addTo(c.getMobile());
			sms.setITemplate(TemplatesMX.CONTACT_VERIFICATION_SMS);
			sms.getModel().put("customer", c);
			sms.getModel().put("link", x);
			postManService.sendSMSAsync(sms);
		} else if (ContactType.WHATSAPP.equals(contactType)) {

		}

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

}