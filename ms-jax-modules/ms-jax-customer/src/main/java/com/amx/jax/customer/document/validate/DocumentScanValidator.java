package com.amx.jax.customer.document.validate;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.document.manager.CustomerDocMasterManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.document.UploadCustomerDocumentRequest;
import com.amx.jax.model.customer.document.UploadCustomerKycRequest;
import com.amx.jax.repository.customer.CustomerDocumentUploadReferenceRepo;
import com.amx.jax.userservice.service.UserService;

@Component
public class DocumentScanValidator {

	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;
	@Autowired
	CustomerDocumentUploadReferenceRepo customerDocumentUploadReferenceRepo;
	@Autowired
	CustomerDocMasterManager customerDocMasterManager;

	public void validateUploadKycDocumentRequest(UploadCustomerKycRequest uploadCustomerKycRequest) {
		if (metaData.getCustomerId() == null && uploadCustomerKycRequest.getIdentityInt() == null) {
			throw new GlobalException("identity int or customer id is required");
		}
		if (metaData.getCustomerId() != null) {
			Customer customer = userService.getCustById(metaData.getCustomerId());
			if (customer.getIdentityExpiredDate() != null) {
				boolean isIdExpired = Calendar.getInstance().getTime().after(customer.getIdentityExpiredDate());
				if (ConstantDocument.Yes.equals(customer.getIsActive())
						&& customer.getIdentityTypeId().equals(uploadCustomerKycRequest.getIdentityTypeId()) && !isIdExpired) {
					throw new GlobalException("customer already active and uploaded kyc document");
				}
			}
			uploadCustomerKycRequest.setIdentityInt(customer.getIdentityInt());
			uploadCustomerKycRequest.setIdentityTypeId(customer.getIdentityTypeId());
		}
	}

	public void validateUploadDocumentRequest(UploadCustomerDocumentRequest uploadCustomerDocumentRequest) {
		if (metaData.getCustomerId() == null && uploadCustomerDocumentRequest.getIdentityInt() == null) {
			throw new GlobalException("identity int or customer id is required");
		}
		if (metaData.getCustomerId() != null) {
			Customer customer = userService.getCustById(metaData.getCustomerId());
			uploadCustomerDocumentRequest.setIdentityInt(customer.getIdentityInt());
			uploadCustomerDocumentRequest.setIdentityTypeId(customer.getIdentityTypeId());
		}
		validateDocCatAndDocType(uploadCustomerDocumentRequest.getDocumentCategory(), uploadCustomerDocumentRequest.getDocumentType());
	}

	public CustomerDocumentTypeMaster validateDocCatAndDocType(String cat, String type) {
		CustomerDocumentTypeMaster docTypeMaster = customerDocMasterManager.getDocTypeMaster(cat, type);
		if (docTypeMaster == null) {
			throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Invalid doc category or doc type");
		}
		return docTypeMaster;
	}
}
