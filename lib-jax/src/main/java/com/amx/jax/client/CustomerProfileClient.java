package com.amx.jax.client;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.ICustomerProfileService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.ContactType;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.customer.CustomerContactVerificationDto;
import com.amx.jax.rest.RestService;

/**
 * This client is responsible for add/update/edit on Customer Profile
 * 
 * @author lalittanwar
 *
 */
@Component
public class CustomerProfileClient implements ICustomerProfileService {

	private static final Logger LOGGER = LoggerService.getLogger(CustomerProfileClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<CustomerContactVerificationDto, Object> createVerificationLink(BigDecimal customerId,
			ContactType type, String identity) {
		try {
			LOGGER.debug("in createVerificationLink : {} {} ", customerId, type);
			String url = appConfig.getJaxURL() + ApiPath.CONTACT_LINK_CREATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(ApiParams.CUSTOMER_ID, customerId)
					.field(ApiParams.CONTACT_TYPE, type)
					.field(ApiParams.IDENTITY, identity)
					.postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerContactVerificationDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.debug("exception in createVerificationLink : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<CustomerContactVerificationDto, Object> validateVerificationLink(BigDecimal linkId) {
		try {
			LOGGER.debug("in validateVerificationLink : {} ", linkId);
			String url = appConfig.getJaxURL() + ApiPath.CONTACT_LINK_VALIDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(ApiParams.LINK_ID, linkId)
					.postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerContactVerificationDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.debug("exception in validateVerificationLink : ", e);
			return JaxSystemError.evaluate(e);
		}
	};

	@Override
	public AmxApiResponse<CustomerContactVerificationDto, Object> resendLink(String identity, BigDecimal linkId,
			String code) {
		try {
			LOGGER.debug("in resendLink : {} {} ", linkId, code);
			String url = appConfig.getJaxURL() + ApiPath.CONTACT_LINK_RESEND;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(ApiParams.IDENTITY, identity)
					.field(ApiParams.LINK_ID, linkId)
					.field(ApiParams.VERIFICATION_CODE, code)
					.postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerContactVerificationDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.debug("exception in resendLink : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<CustomerContactVerificationDto, Object> verifyLinkByCode(String identity, BigDecimal linkId,
			String code) {
		try {
			LOGGER.debug("in verifyLinkByCode : {} {} ", linkId, code);
			String url = appConfig.getJaxURL() + ApiPath.CONTACT_LINK_VERIFY_BY_CODE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(ApiParams.IDENTITY, identity)
					.field(ApiParams.LINK_ID, linkId)
					.field(ApiParams.VERIFICATION_CODE, code)
					.postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerContactVerificationDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.debug("exception in verifyLinkByCode : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<CustomerContactVerificationDto, Object> verifyLinkByContact(String identity, ContactType type,
			String contact) {
		try {
			LOGGER.debug("in verifyLinkByContact : {} {} {}", identity, type, contact);
			String url = appConfig.getJaxURL() + ApiPath.CONTACT_LINK_VERIFY_BY_CONTACT;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(ApiParams.IDENTITY, identity)
					.field(ApiParams.CONTACT_TYPE, type)
					.field(ApiParams.CONTACT, contact)
					.postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerContactVerificationDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.debug("exception in verifyLinkByContact : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<String, Object> saveLanguage(BigDecimal customerId, BigDecimal languageId) {
		try {
			LOGGER.debug("in save language id : {} {} {}", customerId,languageId);
			String url = appConfig.getJaxURL() + ApiPath.CUSTOMER_ONLINE_APP_LANGUAGE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(ApiParams.CUSTOMER_ID, customerId)
					.field(ApiParams.LANGUAGE_ID, languageId)
					.postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.debug("exception in verifyLinkByContact : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

}
