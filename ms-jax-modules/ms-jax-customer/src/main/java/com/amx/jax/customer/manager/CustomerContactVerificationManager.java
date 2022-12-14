
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
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.CustomerVerificationType;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.CustomerVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.AuditActorInfo;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.CActivityEvent.Step;
import com.amx.jax.logger.events.CActivityEvent.Type;
import com.amx.jax.logger.events.Verify;
import com.amx.jax.model.customer.CustomerContactVerificationDto;
import com.amx.jax.repository.CustomerContactVerificationRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.userservice.repository.CustomerVerificationRepository;
import com.amx.jax.userservice.repository.OnlineCustomerRepository;
import com.amx.jax.userservice.service.CustomerVerificationService;
import com.amx.jax.util.AmxDBConstants.Status;
import com.amx.utils.ArgUtil;
import com.amx.utils.CollectionUtil;
import com.amx.utils.Constants;
import com.amx.utils.EntityDtoUtil;
import com.amx.utils.Random;

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

	@Autowired
	AuditService auditService;

	@Autowired
	JaxTenantProperties jaxTenantProperties;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public CustomerContactVerification getCustomerContactVerification(BigDecimal id) {
		return customerContactVerificationRepository.findById(id);
	}

	public List<CustomerContactVerification> getValidCustomerContactVerificationsByCustomerId(BigDecimal customerId,
			ContactType contactType, String contact, int validHours) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -1 * validHours);
		java.util.Date oneDay = new java.util.Date(cal.getTimeInMillis());
		List<CustomerContactVerification> links = null;

		if (ArgUtil.is(contact)) {
			links = customerContactVerificationRepository.getByContact(customerId,
					contactType, contact, oneDay);
		} else {
			links = customerContactVerificationRepository.getByContact(customerId,
					contactType, oneDay);
		}
		return links;
	}

	public List<CustomerContactVerification> getValidCustomerContactVerificationsByCustomerId(BigDecimal customerId,
			ContactType contactType, String contact) {
		if (ContactType.WHATSAPP.equals(contactType)) {
			return this.getValidCustomerContactVerificationsByCustomerId(customerId, contactType, contact,
					jaxTenantProperties.getVerificationValidHours()
							* CustomerContactVerification.EXPIRY_WHATS_APP_FACTOR);
		} else {
			return this.getValidCustomerContactVerificationsByCustomerId(customerId, contactType, contact,
					jaxTenantProperties.getVerificationValidHours());
		}
	}

	public CustomerContactVerification getValidCustomerContactVerificationByCustomerId(BigDecimal customerId,
			ContactType contactType, String contact) {
		List<CustomerContactVerification> links = getValidCustomerContactVerificationsByCustomerId(customerId,
				contactType, contact);
		if (ArgUtil.isEmpty(links) || links.size() == 0) {
			return null;
		}
		return links.get(0);
	}

	public BigDecimal getConfirmedCountsByCustomer(Customer c, ContactType contactType) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1 * 90);
		java.util.Date expiryPeriod = new java.util.Date(cal.getTimeInMillis());
		List<Object[]> x = customerContactVerificationRepository.getCountsByCustomer(c.getCustomerId(),
				contactType,
				expiryPeriod);
		if (ArgUtil.is(x)) {
			for (Object[] p : x) {
				if (Status.N.equals(p[1])) {
					return ArgUtil.parseAsBigDecimal(p[0]);
				}
			}
		}
		return BigDecimal.ZERO;
	}

	public CustomerContactVerification create(Customer c, ContactType contactType) {

		contactType = contactType.contactType();

		// Audit Info
		CActivityEvent audit = new CActivityEvent(Type.CONTACT_VERF).step(Step.INIT);
		audit.setCustomerId(c.getCustomerId());
		audit.setCustomer(c.getIdentityInt());
		audit.setContactType(contactType);
		audit.setVerify(new Verify().contactType(contactType));

		CustomerContactVerification link = new CustomerContactVerification();
		link.setCustomerId(c.getCustomerId());
		link.setContactType(contactType);
		link.setVerificationCode(Random.randomAlphaNumeric(8));
		link.setAppCountryId(c.getCountryId());
		link.setIsActive(Status.Y);

		link.setCreatedDate(new Date());
		link.setSendDate(new Date());

		AuditActorInfo actor = auditService.getActor(AuditActorInfo.class);
		if (!ArgUtil.isEmpty(actor)) {
			link.setCreatedById(actor.getActorIdAsBigDecimal());
			link.setCreatedByType(actor.getActorType());
			// link.setSendById(actor.getActorIdAsBigDecimal());
			// link.setSendByType(actor.getActorType());
		}
		CustomerContactVerification link2;
		try {

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

			List<CustomerContactVerification> oldlinks = getValidCustomerContactVerificationsByCustomerId(
					c.getCustomerId(), contactType, link.getContactValue(),
					jaxTenantProperties.getVerificationValidHours());

			if (ArgUtil.is(oldlinks)) {
				if (oldlinks.size() > jaxTenantProperties.getVerificationAttemptLimit()) {
					throw new GlobalException(JaxError.SEND_OTP_LIMIT_EXCEEDED,
							"Please verify your " + contactType.getLabel()
									+ " to continue. Resend again after " +
									jaxTenantProperties.getVerificationValidHours()
									+ " hours");
				}
				for (CustomerContactVerification oldLink : oldlinks) {

					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MINUTE, -1 * jaxTenantProperties.getVerificationResendAfterMinutes());
					java.util.Date lastAttemptLimit = new java.util.Date(cal.getTimeInMillis());
					if (lastAttemptLimit.before(oldLink.getCreatedDate())
							&& !oldLink.hasExpired(Constants.TimeInterval.MIN,
									jaxTenantProperties.getVerificationResendAfterMinutes())) {
						throw new GlobalException(JaxError.SEND_OTP_LIMIT_EXCEEDED,
								"Please verify your " + contactType.getLabel()
										+ " to continue. Resend again after " +
										jaxTenantProperties.getVerificationResendAfterMinutes()
										+ " minutes");
					}
				}
			}

			link2 = customerContactVerificationRepository.save(link);

		} catch (GlobalException e) {
			auditService.log(audit.result(Result.FAIL).message(e.getError()));
			throw e;
		}

		// Audit Info
		audit.getVerify().id(link2.getId()).count(getConfirmedCountsByCustomer(c, contactType));
		audit.setTargetId(link2.getId());
		auditService.log(audit.result(Result.DONE));

		return link2;
	}

	public CustomerContactVerification resend(Customer c, BigDecimal linkId, String code) {
		CustomerContactVerification oldLink = this.getCustomerContactVerification(linkId);

		if (ArgUtil.isEmpty(oldLink)) {
			throw new GlobalException(JaxError.ENTITY_INVALID, "Cannot ReSend from Invalid Verification link");
		}

		if (ArgUtil.isEmpty(c)) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND, "Customer Does no exists for given civil id");
		}

		// Audit Info
		CActivityEvent audit = new CActivityEvent(Type.CONTACT_VERF).step(Step.RESEND);
		audit.setCustomerId(c.getCustomerId());
		audit.setCustomer(c.getIdentityInt());
		audit.setContactType(oldLink.getContactType());
		audit.setVerify(new Verify().contactType(oldLink.getContactType()).id(oldLink.getId()));

		CustomerContactVerification x;

		try {
			if (!oldLink.getCustomerId().equals(c.getCustomerId())) {
				throw new GlobalException(JaxError.INVALID_CIVIL_ID, "Civil id does not belong to the link");
			}

			if (oldLink.hasValidStatus() && !oldLink.hasExpired(Constants.TimeInterval.HRS,
					jaxTenantProperties.getVerificationValidHours())) {
				throw new GlobalException(JaxError.SEND_OTP_LIMIT_EXCEEDED,
						"Link is Valid for a day and cannot resend it again");
			}

			oldLink.setVerificationCode(Random.randomAlphaNumeric(8));
			oldLink.setSendDate(new Date());
			oldLink = customerContactVerificationRepository.save(oldLink);

		} catch (GlobalException e) {
			auditService.log(audit.result(Result.FAIL).message(e.getError()));
			throw e;
		}

		// Audit Info
		audit.getVerify().count(getConfirmedCountsByCustomer(c, oldLink.getContactType()));
		audit.setTargetId(oldLink.getId());
		auditService.log(audit.result(Result.DONE));

		return oldLink;
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
		} else if (!link.hasValidStatus()) {
			throw new GlobalException(JaxError.ENTITY_INVALID, "Verification link is Invalid : " + link.getIsActive());
		} else if (link.hasExpired(Constants.TimeInterval.HRS,
				jaxTenantProperties.getVerificationValidHours())) {
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
	private void markCustomerContactVerified(Customer c, ContactType type, String contact) {

		List<Customer> otherCustomers = null;
		/**
		 * Old Customer Links from Branch Verified
		 */
		CustomerVerification cv = customerVerificationService.getVerification(c.getCustomerId(),
				CustomerVerificationType.EMAIL);

		CustomerOnlineRegistration customerOnlineRegistration = onlineCustomerRepository
				.findByCustomerId(c.getCustomerId());

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
			if (cv != null) {
				cv.setVerificationStatus(ConstantDocument.Yes);
			}
			if (customerOnlineRegistration != null) {
				customerOnlineRegistration.setStatus(ConstantDocument.Yes);
			}

		} else if (ContactType.SMS.equals(type) || ContactType.MOBILE.equals(type)) {
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
			throw new GlobalException(JaxError.ENTITY_INVALID, "Verification linkType is Invalid : " + type);
		}

		if (!ArgUtil.isEmpty(otherCustomers)) {
			customerRepository.save(otherCustomers);
		}

		customerRepository.save(c);

		if (cv != null) {
			customerVerificationRepository.save(cv);
		}

		if (customerOnlineRegistration != null) {
			onlineCustomerRepository.save(customerOnlineRegistration);
		}

	}

	public CustomerContactVerification verify(Customer c, CustomerContactVerification link, String identity) {

		// Audit Info
		CActivityEvent audit = new CActivityEvent(Type.CONTACT_VERF).step(Step.COMPLETE);
		audit.setCustomerId(c.getCustomerId());
		audit.setCustomer(c.getIdentityInt());
		audit.setContactType(link.getContactType());
		audit.setVerify(new Verify().contactType(link.getContactType()).id(link.getId()));

		try {

			link = validate(link);

			if (ArgUtil.isEmpty(identity) || !identity.equals(c.getIdentityInt())) {
				throw new GlobalException(JaxError.INVALID_CIVIL_ID,
						"Invalid civil id, does not match with Verification link");
			}
			markCustomerContactVerified(c, link.getContactType(), link.getContactValue());

			List<CustomerContactVerification> oldlinks = getValidCustomerContactVerificationsByCustomerId(
					c.getCustomerId(), link.getContactType(), null);
			if (!ArgUtil.isEmpty(oldlinks)) {
				for (CustomerContactVerification customerContactVerification : oldlinks) {
					customerContactVerification.setIsActive(Status.D);
				}
				customerContactVerificationRepository.save(oldlinks);
			}

			link.setIsActive(Status.N);
			link.setVerifiedDate(new Date());
			customerContactVerificationRepository.save(link);

		} catch (GlobalException e) {
			auditService.log(audit.result(Result.FAIL).message(e.getError()));
			throw e;
		}

		// Audit Info
		audit.getVerify().count(getConfirmedCountsByCustomer(c, link.getContactType()));
		audit.setTargetId(link.getId());
		auditService.log(audit.result(Result.DONE));

		return link;
	}

	public CustomerContactVerification verifyByCode(String identity, BigDecimal linkId, String code) {
		CustomerContactVerification link = getCustomerContactVerification(linkId);

		if (ArgUtil.isEmpty(code) || !code.equals(link.getVerificationCode())) {
			throw new GlobalException(JaxError.INVALID_OTP, "Verification is Invalid, cannot complete L:" + linkId);
		}
		Customer c = customerRepository.findOne(link.getCustomerId());

		if (ArgUtil.isEmpty(c)) {
			throw new GlobalException(JaxError.INVALID_CIVIL_ID,
					"Invalid civil id, does not exists in our system L:" + linkId);
		}

		if (c.hasVerified(link.getContactType())) {
			throw new GlobalException(JaxError.ALREADY_VERIFIED_CONTACT,
					link.getContactType() + " contact is already Verified L:" + linkId);
		}

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

		Customer c = CollectionUtil.getOne(customerRepository.findActiveCustomers(identity));

		if (ArgUtil.isEmpty(c)) {
			throw new GlobalException(JaxError.INVALID_CIVIL_ID,
					"Invalid civil id, does not exists in our system I:" + identity);
		}

		if (c.hasVerified(type)) {
			throw new GlobalException(JaxError.ALREADY_VERIFIED_CONTACT,
					type + " contact is already Verified C:" + c.getCustomerId());
		}

		CustomerContactVerification link = getValidCustomerContactVerificationByCustomerId(c.getCustomerId(), type,
				contact);

		if (ArgUtil.isEmpty(link) && ContactType.WHATSAPP.equals(type)) {
			link = create(c, type);
		}

		if (ArgUtil.isEmpty(link)) {
			throw new GlobalException(JaxError.ENTITY_INVALID,
					String.format("Verification link is Invalid :C:%s T:%s", c.getCustomerId(), type));
		}

		verify(c, link, identity);
		return link;
	}

	public CustomerContactVerificationDto convertToDto(CustomerContactVerification entity) {
		CustomerContactVerificationDto dto = new CustomerContactVerificationDto();
		return EntityDtoUtil.entityToDto(entity, dto);
	}

	public CustomerContactVerification checkAndCreateVerification(Customer customer, ContactType contactType) {
		CustomerContactVerification verification = null;
		switch (contactType) {
		case EMAIL:
			if (!Status.Y.equals(customer.getEmailVerified())) {
				verification = create(customer, contactType);
			}
			break;
		case SMS:
			if (!Status.Y.equals(customer.getMobileVerified())) {
				verification = create(customer, contactType);
			}
			break;
		default:
			break;
		}
		return verification;
	}
}
