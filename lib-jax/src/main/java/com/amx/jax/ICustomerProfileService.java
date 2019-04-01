package com.amx.jax;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.customer.CustomerContactVerificationDto;

public interface ICustomerProfileService {

	public static class ApiEndPoints {
		public static final String PREFIX = "/customer-profile";
		public static final String CONTACT_LINK_CREATE = PREFIX + "/contact-link-create";
		public static final String CONTACT_LINK_VALIDATE = PREFIX + "/contact-link-validate";
		public static final String CONTACT_LINK_VERIFY_BY_CODE = PREFIX + "/contact-link-verify-by-code";
		public static final String CONTACT_LINK_VERIFY_BY_CONTACT = PREFIX + "/contact-link-verify-by-contact";
	}

	public static class ApiParams {
		public static final String IDENTITY = "identity";
		public static final String CUSTOMER_ID = "customerId";
		public static final String CONTACT_TYPE = "contactType";
		public static final String VERIFICATION_CODE = "code";
		public static final String LINK_ID = "linkId";
		public static final String CONTACT = "contact";
	}

	@ApiJaxStatus({ JaxError.CUSTOMER_NOT_FOUND })
	AmxApiResponse<CustomerContactVerificationDto, Object> createVerificationLink(BigDecimal customerId,
			ContactType type);

	@ApiJaxStatus({ JaxError.ENTITY_INVALID, JaxError.ENTITY_EXPIRED })
	AmxApiResponse<CustomerContactVerificationDto, Object> validateVerificationLink(BigDecimal linkId);

	@ApiJaxStatus({ JaxError.ENTITY_INVALID, JaxError.ENTITY_EXPIRED })
	AmxApiResponse<CustomerContactVerificationDto, Object> verifyLinkByCode(String identity, BigDecimal linkId,
			BigDecimal code);

	@ApiJaxStatus({ JaxError.ENTITY_INVALID, JaxError.ENTITY_EXPIRED })
	AmxApiResponse<CustomerContactVerificationDto, Object> verifyLinkByContact(String identity, ContactType type,
			String contact);

}
