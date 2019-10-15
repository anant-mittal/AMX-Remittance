package com.amx.jax.customer.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.services.JaxDBService;
import com.amx.utils.JsonUtil;

@Component
public class CustomerBranchAuditManager {

	private static final String INCOME_RANGE = "income-range";
	private static final String EMPLOYER = "employer";
	private static final String CUSTOMER_SIGNATURE = "customer-signature";
	private static final String EMAIL = "email";
	private static final String MOBILE = "mobile";
	private static final String WHATSAPP = "whatsapp";
	private static final String DOB = "dob";
	private static final String LOCAL_ADDRESS = "local-address";
	private static final String PEPS_IND = "pes-ind";

	@Autowired
	AuditService auditService;
	@Autowired
	JaxDBService jaxDBService;

	public void logAuditUpdateCustomer(UpdateCustomerInfoRequest request) {
		String actor = jaxDBService.getCreatedOrUpdatedBy();
		if (request.getIncomeRangeId() != null) {
			CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(INCOME_RANGE);
			auditEvent.setActorId(actor);
			auditEvent.to(request.getIncomeRangeId());
			auditService.log(auditEvent);
		}

		if (request.getEmployer() != null) {
			CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(EMPLOYER);
			auditEvent.setActorId(actor);
			auditEvent.to(request.getEmployer());
			auditService.log(auditEvent);
		}

		if (request.getPersonalDetailInfo() != null) {
			if (request.getPersonalDetailInfo().getCustomerSignature() != null) {
				CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(CUSTOMER_SIGNATURE);
				auditEvent.setActorId(actor);
				auditEvent.to(request.getPersonalDetailInfo().getCustomerSignature());
				auditService.log(auditEvent);
			}

			if (request.getPersonalDetailInfo().getEmail() != null) {
				CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(EMAIL);
				auditEvent.setActorId(actor);
				auditEvent.to(request.getPersonalDetailInfo().getEmail());
				auditService.log(auditEvent);
			}

			if (request.getPersonalDetailInfo().getMobile() != null) {
				CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(MOBILE);
				auditEvent.setActorId(actor);
				auditEvent.to(request.getPersonalDetailInfo().getMobile());
				auditService.log(auditEvent);
			}

			if (request.getPersonalDetailInfo().getWatsAppMobileNo() != null) {
				CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(WHATSAPP);
				auditEvent.setActorId(actor);
				auditEvent.to(request.getPersonalDetailInfo().getWatsAppMobileNo());
				auditService.log(auditEvent);
			}

			if (request.getPersonalDetailInfo().getDateOfBirth() != null) {
				CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(DOB);
				auditEvent.setActorId(actor);
				auditEvent.to(request.getPersonalDetailInfo().getDateOfBirth());
				auditService.log(auditEvent);
			}

			if (request.getPersonalDetailInfo().getPepsIndicator() != null) {
				CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(PEPS_IND);
				auditEvent.setActorId(actor);
				auditEvent.to(request.getPersonalDetailInfo().getPepsIndicator());
				auditService.log(auditEvent);
			}
		}

		if (request.getLocalAddressDetail() != null) {
			CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(LOCAL_ADDRESS);
			auditEvent.setActorId(actor);
			auditEvent.to(JsonUtil.toStringMap(request.getLocalAddressDetail()));
			auditService.log(auditEvent);
		}
	}
}
