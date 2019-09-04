package com.amx.jax.customer.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.customer.manager.CustomerForceUpdateManager;
import com.amx.jax.customer.manager.CustomerManagementManager;
import com.amx.jax.model.response.customer.CustomerMgmtMetaInfo;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;

@Service
public class CustomerManagementService {

	@Autowired
	CustomerManagementManager customerManagementManager;
	@Autowired
	CustomerForceUpdateManager customerForceUpdateManager;

	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getCustomerDetail(String identityInt, BigDecimal identityTypeId) {
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
}
