package com.amx.jax.customer.document.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.UploadCustomerKycRequest;
import com.amx.jax.userservice.service.UserService;

@Component
public class KycScanValidator {

	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;

	public void validateUploadKycDocumentRequest(UploadCustomerKycRequest uploadCustomerKycRequest) {
		if (metaData.getCustomerId() == null && uploadCustomerKycRequest.getIdentityInt() == null) {
			throw new GlobalException("identity int or customer id is required");
		}
		if (metaData.getCustomerId() != null) {
			Customer customer = userService.getCustById(metaData.getCustomerId());
			uploadCustomerKycRequest.setIdentityInt(customer.getIdentityInt());
			uploadCustomerKycRequest.setIdentityTypeId(customer.getIdentityTypeId());
		}
	}
}
