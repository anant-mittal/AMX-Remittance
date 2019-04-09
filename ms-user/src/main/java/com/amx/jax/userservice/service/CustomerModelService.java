package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.CommunicationChannel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.customer.CustomerCommunicationChannel;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.model.response.customer.CustomerModelSignupResponse;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.utils.MaskUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerModelService {

	@Autowired
	UserValidationService userValidationService;
	@Autowired
	UserService userService;
	@Autowired
	CustomerDao custDao;
	@Autowired
	CustomerFlagManager customerFlagManager;
	@Autowired
	MetaData metaData;

	public CustomerModelResponse getCustomerModelResponse(String identityInt) {
		userValidationService.validateIdentityInt(identityInt, ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID);
		List<Customer> customers = userService.getCustomerByIdentityInt(identityInt);
		Customer customer = userValidationService.validateCustomerForDuplicateRecords(customers);
		BigDecimal customerId = customer.getCustomerId();
		PersonInfo personInfo = userService.getPersonInfo(customerId);
		CustomerFlags customerFlags = customerFlagManager.getCustomerFlags(customerId);
		CustomerModelResponse response = new CustomerModelResponse(personInfo, customerFlags);
		response.setCustomerId(customer.getCustomerId());
		return response;
	}

	public CustomerModelResponse getCustomerModelResponse() {
		if (metaData.getCustomerId() == null) {
			throw new GlobalException("Null customer id in header");
		}
		BigDecimal customerId = metaData.getCustomerId();
		PersonInfo personInfo = userService.getPersonInfo(customerId);
		CustomerFlags customerFlags = customerFlagManager.getCustomerFlags(customerId);
		CustomerModelResponse response = new CustomerModelResponse(personInfo, customerFlags);
		response.setCustomerId(customerId);
		return response;
	}

	public CustomerModelSignupResponse getCustomerModelSignupResponse(String identityInt) {
		CustomerModelResponse customerModelResponse = getCustomerModelResponse(identityInt);
		PersonInfo personInfo = customerModelResponse.getPersonInfo();
		CustomerFlags customerFlags = customerModelResponse.getCustomerFlags();
		CustomerModelSignupResponse response = new CustomerModelSignupResponse();
		response.setCustomerFlags(customerModelResponse.getCustomerFlags());
		List<CustomerCommunicationChannel> customerCommunicationChannels = new ArrayList<>();
		if (customerFlags.getEmailVerified()) {
			String maskedEmail = MaskUtil.maskEmail(personInfo.getEmail(), 4, "*");
			customerCommunicationChannels
					.add(new CustomerCommunicationChannel(CommunicationChannel.EMAIL, maskedEmail));
		}
		if (customerFlags.getMobileVerified()) {
			String maskedMobile = MaskUtil.leftMask(personInfo.getMobile(), 4, "*");
			customerCommunicationChannels
					.add(new CustomerCommunicationChannel(CommunicationChannel.MOBILE, maskedMobile));
		}
		if (customerFlags.getWhatsAppVerified()) {
			String maskedMobile = MaskUtil.leftMask(personInfo.getWhatsAppNumber(), 4, "*");
			customerCommunicationChannels
					.add(new CustomerCommunicationChannel(CommunicationChannel.WHATSAPP, maskedMobile));
		}
		response.setCustomerCommunicationChannel(customerCommunicationChannels);
		return response;
	}

}
