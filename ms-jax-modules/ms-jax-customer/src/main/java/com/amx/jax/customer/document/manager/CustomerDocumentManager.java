package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.document.validate.KycScanValidator;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.IdentityTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.model.customer.UploadCustomerKycRequest;
import com.amx.jax.model.customer.UploadCustomerKycResponse;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.jax.userservice.service.UserService;
import com.jax.amxlib.exception.jax.GlobaLException;

@Component
public class CustomerDocumentManager {

	@Autowired
	DatabaseScanManager databaseImageScanManager;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	KycScanValidator kycScanValidator;
	@Autowired
	UserService userService;
	@Autowired
	CustomerDocumentUploadManager customerDocumentUploadManager;
	@Autowired
	CustomerKycManager customerKycManager;

	public List<CustomerDocumentInfo> getCustomerUploadDocuments(BigDecimal customerId) {

		CustomerDocumentInfo kycImage = fetchKycCustomerImage(customerId);
		List<CustomerDocumentInfo> customerOtherDocumentImages = fetchCustomerOtherDocuments(customerId);
		List<CustomerDocumentInfo> allDocuments = new ArrayList<>();
		if (kycImage != null) {
			allDocuments.add(kycImage);
		}
		if (CollectionUtils.isNotEmpty(customerOtherDocumentImages)) {
			allDocuments.addAll(customerOtherDocumentImages);
		}
		return allDocuments;
	}

	private List<CustomerDocumentInfo> fetchCustomerOtherDocuments(BigDecimal customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	private CustomerDocumentInfo fetchKycCustomerImage(BigDecimal customerId) {
		CustomerIdProof customerIdProof = customerIdProofManager.getCustomerIdProofByCustomerId(customerId);
		CustomerDocumentInfo customerDocumentImage = null;
		if (customerIdProof != null) {
			switch (customerIdProof.getScanSystem()) {
			case "D":
				customerDocumentImage = databaseImageScanManager.fetchKycImageInfo(customerIdProof);
			default:
			}
			addDataFromCustomerIdProof(customerDocumentImage, customerIdProof);
		}
		return customerDocumentImage;
	}

	private void addDataFromCustomerIdProof(CustomerDocumentInfo customerDocumentImage, CustomerIdProof customerIdProof) {
		IdentityTypeMaster identityMaster = customerIdProofManager.getIdentityTypeMaster(customerIdProof.getIdentityTypeId(), ConstantDocument.Yes);
		customerDocumentImage.setDocumentType(identityMaster.getIdentityType());
		customerDocumentImage.setUploadedDate(customerIdProof.getCreationDate());
	}

	public UploadCustomerKycResponse uploadKycDocument(UploadCustomerKycRequest uploadCustomerKycRequest) {
		kycScanValidator.validateUploadKycDocumentRequest(uploadCustomerKycRequest);
		BigDecimal uploadReference = databaseImageScanManager.uploadKycDocument(uploadCustomerKycRequest);
		UploadCustomerKycResponse uploadCustomerKycResponse = new UploadCustomerKycResponse();
		uploadCustomerKycResponse.setUploadReference(uploadReference);
		return uploadCustomerKycResponse;
	}

	public void addCustomerDocument(BigDecimal customerId) throws ParseException {
		Customer customer = userService.getCustById(customerId);
		List<CustomerDocumentUploadReferenceTemp> uploads = customerDocumentUploadManager.getCustomerUploads(customer.getIdentityInt(),
				customer.getIdentityTypeId());
		if(CollectionUtils.isEmpty(uploads)) {
			throw new GlobaLException("Customer documents not uploaded");
		}
		for (CustomerDocumentUploadReferenceTemp upload : uploads) {
			switch (upload.getCustomerDocUploadType()) {
			case KYC_PROOF:
				customerKycManager.uploadAndCreateKyc(customer, upload);
				break;

			default:
				break;
			}
		}
	}
}
