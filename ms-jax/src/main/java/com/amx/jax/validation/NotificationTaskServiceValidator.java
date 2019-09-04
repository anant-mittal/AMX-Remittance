package com.amx.jax.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.client.compliance.ComplianceTrnxdDocStatus;
import com.amx.jax.client.task.CustomerDocUploadNotificationTaskData;
import com.amx.jax.dbmodel.compliance.ComplianceBlockedTrnxDocMap;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.compliance.ComplianceTrnxDocMapRepo;
import com.amx.jax.repository.customer.CustomerDocumentTypeMasterRepo;
import com.amx.jax.services.RemittanceTransactionService;

@Component
public class NotificationTaskServiceValidator {

	@Autowired
	ComplianceTrnxDocMapRepo complianceTrnxDocMapRepo;
	@Autowired
	CustomerDocumentTypeMasterRepo customerDocumentTypeMasterRepo;
	@Autowired
	RemittanceTransactionService remittanceTransactionService;

	public void validateNotifyBranchUserForDocumentUpload(CustomerDocUploadNotificationTaskData data) {
		String docCategory = data.getDocumentCategory();
		RemittanceTransaction trnx = remittanceTransactionService.getRemittanceTransactionById(data.getRemittanceTransactionId());
		List<String> docTypes = data.getDocumentTypes();
		for (String docType : docTypes) {
			CustomerDocumentTypeMaster docTypeMaster = customerDocumentTypeMasterRepo.findByDocumentCategoryAndDocumentType(docCategory, docType);
			List<ComplianceBlockedTrnxDocMap> complianceBlockedTrnxDocMap = complianceTrnxDocMapRepo.findByDocTypeMasterAndCustomerIdAndStatus(
					docTypeMaster, trnx.getCustomerId().getCustomerId(), ComplianceTrnxdDocStatus.REQUESTED);
			if (complianceBlockedTrnxDocMap.size() > 0) {
				throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "You have already requested for document of type: " + docType);
			}
		}
	}
}
