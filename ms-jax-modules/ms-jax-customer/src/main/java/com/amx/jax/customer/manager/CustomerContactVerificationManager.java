package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.db.utils.EntityDtoUtil;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CustomerContactVerificationDto;
import com.amx.jax.repository.CustomerContactVerificationRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.util.AmxDBConstants;
import com.amx.jax.util.AmxDBConstants.Status;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.Random;
import com.amx.utils.TimeUtils;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
public class CustomerContactVerificationManager {

	@Autowired
	CustomerContactVerificationRepository customerContactVerificationRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	MetaData metaData;

	public CustomerContactVerification getCustomerContactVerification(BigDecimal id) {
		return customerContactVerificationRepository.findById(id);
	}

	public CustomerContactVerification create(Customer c, ContactType contactType) {

		CustomerContactVerification link = new CustomerContactVerification();
		link.setCustomerId(c.getCustomerId());
		link.setContactType(contactType);
		link.setVerificationCode(Random.randomAlphaNumeric(8));
		link.setCreatedDate(new Date());
		link.setAppCountryId(metaData.getCountryId());
		link.setIsActive(Status.Y);

		if (ContactType.EMAIL.equals(contactType)) {
			link.setContactValue(c.getEmail());
		} else if (ContactType.SMS.equals(contactType)) {
			link.setContactValue(c.getPrefixCodeMobile() + c.getMobile());
		} else if (ContactType.WHATSAPP.equals(contactType)) {
			link.setContactValue(c.getWhatsappPrefix() + c.getWhatsapp());
		}

		return customerContactVerificationRepository.save(link);
	}

	/**
	 * Validates CustomerContactVerification, throws exception if not valid or
	 * expired
	 * 
	 * @param link
	 * @return
	 */
	public CustomerContactVerification validate(CustomerContactVerification link) {
		if (ArgUtil.isEmpty(link) || AmxDBConstants.Status.D.equals(link.getIsActive())
				|| AmxDBConstants.Status.N.equals(link.getIsActive())) {
			throw new GlobalException(JaxError.ENTITY_INVALID, "Verification link is Invalid : " + link.getIsActive());
		} else if (TimeUtils.isExpired(link.getCreatedDate(), Constants.TimeInterval.DAY)) {
			throw new GlobalException(JaxError.ENTITY_EXPIRED,
					"Verification link is expired, Created on " + link.getCreatedDate());
		}
		return link;
	}

	public CustomerContactVerification verify(Customer c, CustomerContactVerification link, String identity) {

		link = validate(link);

		if (!ArgUtil.isEmpty(identity) || !identity.equals(c.getIdentityInt())) {
			throw new GlobalException(JaxError.INVALID_CIVIL_ID,
					"Invalid civil id, does not match with Verification link");
		}
		if (ContactType.EMAIL.equals(link.getContactType())) {
			if (!link.getContactValue().equals(c.getEmail())) {
				throw new GlobalException(JaxError.ENTITY_INVALID,
						"EmailId in customer records does not match with verification link");
			}
			c.setEmailVerified(Status.Y);

		} else if (ContactType.SMS.equals(link.getContactType())) {
			String mobile = c.getPrefixCodeMobile() + c.getMobile();
			if (!link.getContactValue().equals(mobile)) {
				throw new GlobalException(JaxError.ENTITY_INVALID,
						"MOBILE No in customer records does not match with verification link");
			}
			c.setMobileVerified(Status.Y);
		} else if (ContactType.WHATSAPP.equals(link.getContactType())) {
			String whatsAppNo = c.getWhatsappPrefix() + c.getWhatsapp();
			if (!link.getContactValue().equals(whatsAppNo)) {
				throw new GlobalException(JaxError.ENTITY_INVALID,
						"WhatsApp No in customer records does not match with verification link");
			}
			c.setWhatsAppVerified(Status.Y);
		} else {
			throw new GlobalException(JaxError.ENTITY_INVALID,
					"Verification linkType is Invalid : " + link.getContactType());
		}

		customerRepository.save(c);

		link.setIsActive(Status.D);
		customerContactVerificationRepository.save(link);

		return link;
	}

	public CustomerContactVerification verifyByCode(String identity, BigDecimal linkId, String code) {
		CustomerContactVerification link = getCustomerContactVerification(linkId);

		if (!ArgUtil.isEmpty(code) || !code.equals(link.getVerificationCode())) {
			throw new GlobalException(JaxError.INVALID_OTP,
					"Verification is Invalid, cannot complete.");
		}
		Customer c = customerRepository.findOne(link.getCustomerId());
		verify(c, link, identity);
		return link;
	}

	public CustomerContactVerification verifyByContact(String identity, ContactType type, String contact) {

		Customer c = customerRepository.getCustomerOneByIdentityInt(identity);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		java.util.Date oneDay = new java.util.Date(cal.getTimeInMillis());

		List<CustomerContactVerification> links = customerContactVerificationRepository.getByContact(c.getCustomerId(),
				type,
				contact, oneDay);

		if (ArgUtil.isEmpty(links) || links.size() == 0) {
			throw new GlobalException(JaxError.ENTITY_INVALID, "Verification link is Invalid : Type" + type);
		}

		CustomerContactVerification link = links.get(0);
		verify(c, link, identity);
		return link;
	}

	public CustomerContactVerificationDto convertToDto(CustomerContactVerification entity) {
		CustomerContactVerificationDto dto = new CustomerContactVerificationDto();
		return EntityDtoUtil.entityToDto(entity, dto);
	}

}
