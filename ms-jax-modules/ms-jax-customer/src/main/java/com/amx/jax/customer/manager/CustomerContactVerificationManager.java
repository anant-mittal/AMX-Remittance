
package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.CustomerVerificationType;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.CustomerVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.customer.CustomerContactVerificationDto;
import com.amx.jax.repository.CustomerContactVerificationRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.userservice.repository.CustomerVerificationRepository;
import com.amx.jax.userservice.repository.OnlineCustomerRepository;
import com.amx.jax.userservice.service.CustomerVerificationService;
import com.amx.jax.util.AmxDBConstants;
import com.amx.jax.util.AmxDBConstants.Status;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.EntityDtoUtil;
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
	private CustomerVerificationService customerVerificationService;
	
	@Autowired
	private CustomerVerificationRepository customerVerificationRepository;
	
	@Autowired
	OnlineCustomerRepository onlineCustomerRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public CustomerContactVerification getCustomerContactVerification(BigDecimal id) {
		return customerContactVerificationRepository.findById(id);
	}

	public List<CustomerContactVerification> getValidCustomerContactVerificationsByCustomerId(BigDecimal customerId,
			ContactType contactType, String contact) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		java.util.Date oneDay = new java.util.Date(cal.getTimeInMillis());
		List<CustomerContactVerification> links = customerContactVerificationRepository.getByContact(customerId,
				contactType,
				contact, oneDay);
		return links;
	}

	public CustomerContactVerification getValidCustomerContactVerificationByCustomerId(BigDecimal customerId,
			ContactType contactType, String contact) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		java.util.Date oneDay = new java.util.Date(cal.getTimeInMillis());
		List<CustomerContactVerification> links = getValidCustomerContactVerificationsByCustomerId(customerId,
				contactType, contact);
		if (ArgUtil.isEmpty(links) || links.size() == 0) {
			return null;
		}
		return links.get(0);
	}

	public CustomerContactVerification create(Customer c, ContactType contactType) {

		contactType = contactType.contactType();

		CustomerContactVerification link = new CustomerContactVerification();
		link.setCustomerId(c.getCustomerId());
		link.setContactType(contactType);
		link.setVerificationCode(Random.randomAlphaNumeric(8));
		link.setCreatedDate(new Date());
		link.setAppCountryId(c.getCountryId());
		link.setIsActive(Status.Y);

		if (ContactType.EMAIL.equals(contactType)) {
			if (ArgUtil.isEmpty(c.getEmail())) {
				throw new GlobalException(JaxError.MISSING_CONTACT, "Email is missing for customer");
			}
			link.setContactValue(c.getEmail());
		} else if (ContactType.SMS.equals(contactType)) {
			if (ArgUtil.isEmpty(c.getMobile()) || ArgUtil.isEmpty(c.getPrefixCodeMobile())) {
				throw new GlobalException(JaxError.MISSING_CONTACT, "Mobile is missing for customer");
			}
			link.setContactValue(c.getPrefixCodeMobile() + c.getMobile());
		} else if (ContactType.WHATSAPP.equals(contactType)) {
			if (ArgUtil.isEmpty(c.getWhatsapp()) || ArgUtil.isEmpty(c.getWhatsappPrefix())) {
				throw new GlobalException(JaxError.MISSING_CONTACT, "WhatsApp is missing for customer");
			}
			link.setContactValue(c.getWhatsappPrefix() + c.getWhatsapp());
		}

		List<CustomerContactVerification> oldlinks = getValidCustomerContactVerificationsByCustomerId(c.getCustomerId(),
				contactType,
				link.getContactValue());

		if (!ArgUtil.isEmpty(oldlinks) && oldlinks.size() > 3) {
			throw new GlobalException(JaxError.SEND_OTP_LIMIT_EXCEEDED,
					"Sending Verification Limit(4) has exceeded try again after 24 hours");
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
		if (ArgUtil.isEmpty(link)) {
			throw new GlobalException(JaxError.ENTITY_INVALID, "Verification link is Invalid");
		} else if (AmxDBConstants.Status.D.equals(link.getIsActive())
				|| AmxDBConstants.Status.N.equals(link.getIsActive())) {
			throw new GlobalException(JaxError.ENTITY_INVALID, "Verification link is Invalid : " + link.getIsActive());
		} else if (TimeUtils.isExpired(link.getCreatedDate(), Constants.TimeInterval.DAY)) {
			throw new GlobalException(JaxError.ENTITY_EXPIRED,
					"Verification link is expired, Created on " + link.getCreatedDate());
		}
		return link;
	}

	/**
	 * This method can be used to mark customer contact verifed without
	 * verificationLink Check.
	 * 
	 * @param c
	 * @param type
	 * @param contact
	 */
	public void markCustomerContactVerified(Customer c, ContactType type, String contact) {

		List<Customer> otherCustomers = null;
		CustomerVerification cv = customerVerificationService.getVerification(c.getCustomerId(),
				CustomerVerificationType.EMAIL);
		CustomerOnlineRegistration customerOnlineRegistration = onlineCustomerRepository.getLoginCustomersDeatilsById(c.getIdentityInt());
		if (ContactType.EMAIL.equals(type)) {
			if (!contact.equals(c.getEmail())) {
				throw new GlobalException(JaxError.ENTITY_INVALID,
						"EmailId in customer records does not match with verification link");
			}
			otherCustomers = customerRepository.getCustomersByEmail(c.getEmail());
			for (Customer customer : otherCustomers) {
				customer.setEmailVerified(Status.D);
			}
			c.setEmailVerified(Status.Y);
			cv.setVerificationStatus(ConstantDocument.Yes);
			customerOnlineRegistration.setStatus(ConstantDocument.Yes);
			onlineCustomerRepository.save(customerOnlineRegistration);
			customerVerificationRepository.save(cv);
		} else if (ContactType.SMS.equals(type)) {
			String mobile = c.getPrefixCodeMobile() + c.getMobile();
			if (!contact.equals(mobile)) {
				throw new GlobalException(JaxError.ENTITY_INVALID,
						"MOBILE No in customer records does not match with verification link");
			}
			otherCustomers = customerRepository.getCustomersByMobile(c.getPrefixCodeMobile(), c.getMobile());
			for (Customer customer : otherCustomers) {
				customer.setMobileVerified(Status.D);
			}
			c.setMobileVerified(Status.Y);
		} else if (ContactType.WHATSAPP.equals(type)) {
			String whatsAppNo = c.getWhatsappPrefix() + c.getWhatsapp();
			if (!contact.equals(whatsAppNo)) {
				throw new GlobalException(JaxError.ENTITY_INVALID,
						"WhatsApp No in customer records does not match with verification link");
			}
			otherCustomers = customerRepository.getCustomersByWhatsApp(c.getWhatsappPrefix(), c.getWhatsapp());
			for (Customer customer : otherCustomers) {
				customer.setWhatsAppVerified(Status.D);
			}
			c.setWhatsAppVerified(Status.Y);
		} else {
			throw new GlobalException(JaxError.ENTITY_INVALID,
					"Verification linkType is Invalid : " + type);
		}
		
		if (!ArgUtil.isEmpty(otherCustomers)) {
			customerRepository.save(otherCustomers);
		}

		customerRepository.save(c);
	}

	public CustomerContactVerification verify(Customer c, CustomerContactVerification link, String identity) {

		link = validate(link);

		if (ArgUtil.isEmpty(identity) || !identity.equals(c.getIdentityInt())) {
			throw new GlobalException(JaxError.INVALID_CIVIL_ID,
					"Invalid civil id, does not match with Verification link");
		}
		markCustomerContactVerified(c, link.getContactType(), link.getContactValue());

		link.setIsActive(Status.D);

		List<CustomerContactVerification> oldlinks = getValidCustomerContactVerificationsByCustomerId(c.getCustomerId(),
				link.getContactType(),
				link.getContactValue());
		if (!ArgUtil.isEmpty(oldlinks)) {
			for (CustomerContactVerification customerContactVerification : oldlinks) {
				customerContactVerification.setIsActive(Status.N);
			}
			customerContactVerificationRepository.save(oldlinks);
		}

		customerContactVerificationRepository.save(link);

		return link;
	}

	public CustomerContactVerification verifyByCode(String identity, BigDecimal linkId, String code) {
		CustomerContactVerification link = getCustomerContactVerification(linkId);

		if (ArgUtil.isEmpty(code) || !code.equals(link.getVerificationCode())) {
			throw new GlobalException(JaxError.INVALID_OTP,
					"Verification is Invalid, cannot complete.");
		}
		Customer c = customerRepository.findOne(link.getCustomerId());
		verify(c, link, identity);
		return link;
	}

	/**
	 * Mark the customer contact verfified, only if verification link has been
	 * initiated and is still valid
	 * 
	 * @param identity
	 * @param type
	 * @param contact
	 * @return
	 */
	public CustomerContactVerification verifyByContact(String identity, ContactType type, String contact) {

		Customer c = customerRepository.getCustomerOneByIdentityInt(identity);

		CustomerContactVerification link = getValidCustomerContactVerificationByCustomerId(c.getCustomerId(), type,
				contact);

		if (ArgUtil.isEmpty(link)) {
			throw new GlobalException(JaxError.ENTITY_INVALID, "Verification link is Invalid : Type" + type);
		}

		verify(c, link, identity);
		return link;
	}

	public CustomerContactVerificationDto convertToDto(CustomerContactVerification entity) {
		CustomerContactVerificationDto dto = new CustomerContactVerificationDto();
		return EntityDtoUtil.entityToDto(entity, dto);
	}

}
