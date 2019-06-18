package com.amx.jax.customer.document.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.document.manager.CustomerDocMasterManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
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
			uploadCustomerKycRequest.setIdentityInt(customer.getIdentityInt());
			uploadCustomerKycRequest.setIdentityTypeId(customer.getIdentityTypeId());
		}
	}

	public CustomerDocumentUploadReference validateUploadDocumentRequest(UploadCustomerDocumentRequest uploadCustomerDocumentRequest) {
		if (metaData.getCustomerId() == null && uploadCustomerDocumentRequest.getIdentityInt() == null) {
			throw new GlobalException("identity int or customer id is required");
		}
		if (metaData.getCustomerId() != null) {
			Customer customer = userService.getCustById(metaData.getCustomerId());
			uploadCustomerDocumentRequest.setIdentityInt(customer.getIdentityInt());
			uploadCustomerDocumentRequest.setIdentityTypeId(customer.getIdentityTypeId());
		}
		CustomerDocumentTypeMaster docTypeMaster = customerDocMasterManager.getDocTypeMaster(uploadCustomerDocumentRequest.getDocumentCategory(),
				uploadCustomerDocumentRequest.getDocumentType());
		CustomerDocumentUploadReference existing = customerDocumentUploadReferenceRepo.findByCustomerDocumentTypeMasterAndCustomerId(docTypeMaster,
				metaData.getCustomerId());
		if (existing != null && ConstantDocument.Yes.equals(existing.getStatus())) {
			throw new GlobalException("Document already uploaded!");
		}
		return existing;
	}
}
