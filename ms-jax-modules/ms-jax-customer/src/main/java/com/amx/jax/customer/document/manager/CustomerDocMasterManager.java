package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.IdentityTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentCategory;
import com.amx.jax.dbmodel.customer.CustomerDocumentType;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.repository.customer.CustomerDocumentTypeMasterRepo;
import com.amx.jax.userservice.manager.CustomerIdProofManager;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerDocMasterManager {

	@Autowired
	CustomerDocumentTypeMasterRepo customerDocumentTypeMasterRepo;
	@Autowired
	CustomerIdProofManager customerIdProofManager;

	public CustomerDocumentTypeMaster getDocTypeMaster(String docCategory, String docType) {
		return customerDocumentTypeMasterRepo.findByDocumentCategoryAndDocumentType(docCategory, docType);
	}

	/**
	 * @param bizComponentId
	 *            - biz component id for that identity type
	 * @return returns document type master object
	 */
	public CustomerDocumentTypeMaster getKycDocTypeMaster(BigDecimal identityTypeId) {
		IdentityTypeMaster identityTypeMaster = customerIdProofManager.getIdentityTypeMaster(identityTypeId, ConstantDocument.Yes);
		String identityType = identityTypeMaster.getIdentityType();
		CustomerDocumentType customerDocType = CustomerDocumentType.getCustomerDocTypeByIdentityType(identityType);
		return customerDocumentTypeMasterRepo.findByDocumentCategoryAndDocumentType(CustomerDocumentCategory.KYC_PROOF.toString(),
				customerDocType.toString());
	}
}
