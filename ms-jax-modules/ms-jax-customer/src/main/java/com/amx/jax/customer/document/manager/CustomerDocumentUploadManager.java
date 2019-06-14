package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.repository.customer.CustomerDocumentUploadReferenceTempRepo;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerDocumentUploadManager {

	private static final Logger log = LoggerFactory.getLogger(CustomerDocumentUploadManager.class);

	@Autowired
	CustomerDocumentUploadReferenceTempRepo customerDocumentUploadReferenceTempRepo;

	public void findAndDeleteExistingRecord(String identityInt, BigDecimal identityType, CustomerDocumentTypeMaster customerDocumentTypeMaster) {

		CustomerDocumentUploadReferenceTemp existing = customerDocumentUploadReferenceTempRepo
				.findByidentityIntAndIdentityTypeIdAndCustomerDocumentTypeMaster(identityInt, identityType, customerDocumentTypeMaster);
		if (existing != null) {
			log.debug("found exising uploaded record {}", log);
			customerDocumentUploadReferenceTempRepo.delete(existing);
		}
	}

	public List<CustomerDocumentUploadReferenceTemp> getCustomerUploads(String identityInt, BigDecimal identityType) {

		List<CustomerDocumentUploadReferenceTemp> uploads = customerDocumentUploadReferenceTempRepo.findByidentityIntAndIdentityTypeId(identityInt,
				identityType);
		return uploads;
	}

	public void deleteCustomerUploads(String identityInt, BigDecimal identityType) {
		List<CustomerDocumentUploadReferenceTemp> uploads = getCustomerUploads(identityInt, identityType);
		if (CollectionUtils.isNotEmpty(uploads)) {
			customerDocumentUploadReferenceTempRepo.delete(uploads);
		}
	}

	public void save(CustomerDocumentUploadReferenceTemp docUploadRef) {
		customerDocumentUploadReferenceTempRepo.save(docUploadRef);
	}

}
