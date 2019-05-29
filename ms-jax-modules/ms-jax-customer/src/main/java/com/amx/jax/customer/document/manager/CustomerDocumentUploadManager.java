package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.model.customer.CustomerDocUploadType;
import com.amx.jax.model.customer.UploadCustomerKycRequest;
import com.amx.jax.repository.customer.CustomerDocumentUploadReferenceTempRepo;

@Component
public class CustomerDocumentUploadManager {

	private static final Logger log = LoggerFactory.getLogger(CustomerDocumentUploadManager.class);

	@Autowired
	CustomerDocumentUploadReferenceTempRepo customerDocumentUploadReferenceTempRepo;

	public void findAndDeleteExistingRecord(String identityInt, BigDecimal identityType,
			CustomerDocUploadType uploadType) {

		CustomerDocumentUploadReferenceTemp existing = customerDocumentUploadReferenceTempRepo
				.findByidentityIntAndIdentityTypeIdAndCustomerDocUploadType(identityInt, identityType, uploadType);
		if (existing != null) {
			log.debug("found exising uploaded record {}", log);
			customerDocumentUploadReferenceTempRepo.delete(existing);
		}
	}

	public void save(CustomerDocumentUploadReferenceTemp docUploadRef) {
		customerDocumentUploadReferenceTempRepo.save(docUploadRef);
	}

}
