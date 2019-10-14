package com.amx.jax.customer.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.customer.manager.CustomerForceUpdateManager;
import com.amx.jax.customer.manager.CustomerManagementManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.customer.CustomerMgmtMetaInfo;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.utils.ArgUtil;

@Service
public class CustomerManagementService {

	@Autowired
	CustomerManagementManager customerManagementManager;
	@Autowired
	CustomerForceUpdateManager customerForceUpdateManager;
	@Autowired
	UserValidationService userValidationService;
	@Autowired
	ICustomerRepository customerRepository;

	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getCustomerDetail(String identityInt, BigDecimal identityTypeId) {
		userValidationService.validateIdentityInt(identityInt, identityTypeId);
		OffsiteCustomerDataDTO dto = customerManagementManager.getCustomerDeatils(identityInt, identityTypeId);
		AmxApiResponse<OffsiteCustomerDataDTO, Object> response = AmxApiResponse.build(dto);
		if (dto.getCustomerPersonalDetail() != null && dto.getCustomerPersonalDetail().getCustomerId() != null) {
			BigDecimal customerId = dto.getCustomerPersonalDetail().getCustomerId();
			dto.setCustomerStatusModel(customerManagementManager.getCustomerStatusModel(customerId));
			response.setMeta(getMetaInfoForCustomerMgmt(dto));
		}

		return response;
	}

	private CustomerMgmtMetaInfo getMetaInfoForCustomerMgmt(OffsiteCustomerDataDTO dto) {
		CustomerMgmtMetaInfo metaInfo = new CustomerMgmtMetaInfo();
		boolean isMetaPresent = false;
		if (Boolean.TRUE.equals(dto.getCustomerFlags().getIsDeactivated())) {
			isMetaPresent = true;
			metaInfo.setCustomerForceUpdateModel(customerForceUpdateManager.getBlockedCustomerModel(dto.getCustomerPersonalDetail().getCustomerId()));
		}
		if (isMetaPresent) {
			return metaInfo;
		} else {
			return null;
		}
	}
	public void validateCustomerField(String identityInt, BigDecimal identityType, BigDecimal customerId) {

		if (ArgUtil.isEmpty(identityInt) && ArgUtil.isEmpty(customerId)) {
			throw new GlobalException(JaxError.VALIDATION_NOT_NULL, "Civil ID,Customer ID should not be null");

		}
		if (!ArgUtil.isEmpty(identityInt) && ArgUtil.isEmpty(identityType)) {
			throw new GlobalException(JaxError.VALIDATION_NOT_NULL, "identity Type should not be null");

		}
		if (!ArgUtil.isEmpty(customerId)) {
			Customer customer = customerRepository.findOne(customerId);
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND, "Customer ID not found");

		}

	}
}
