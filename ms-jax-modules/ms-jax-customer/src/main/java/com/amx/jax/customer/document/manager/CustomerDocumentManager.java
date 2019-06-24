package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.document.validate.DocumentScanValidator;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.IdentityTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.model.customer.CustomerKycData;
import com.amx.jax.model.customer.document.UploadCustomerDocumentRequest;
import com.amx.jax.model.customer.document.UploadCustomerDocumentResponse;
import com.amx.jax.model.customer.document.UploadCustomerKycRequest;
import com.amx.jax.model.customer.document.UploadCustomerKycResponse;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.JsonUtil;
import com.jax.amxlib.exception.jax.GlobaLException;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerDocumentManager {

	@Autowired
	DatabaseScanManager databaseImageScanManager;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	DocumentScanValidator kycScanValidator;
	@Autowired
	UserService userService;
	@Autowired
	CustomerDocumentUploadManager customerDocumentUploadManager;
	@Autowired
	CustomerKycManager customerKycManager;
	@Autowired
	CustomerDocMasterManager customerDocMasterManager;
	@Autowired
	CustomerDao customerDao;
	@Autowired
	MetaData metaData;

	private static final Logger log = LoggerFactory.getLogger(CustomerDocumentManager.class);

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
		if (customerIdProof != null && customerIdProof.getScanSystem() != null) {
			switch (customerIdProof.getScanSystem()) {
			case "D":
				customerDocumentImage = databaseImageScanManager.fetchKycImageInfo(customerIdProof);
			default:
			}
			if (customerDocumentImage != null) {
				addDataFromCustomerIdProof(customerDocumentImage, customerIdProof);
			}
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
		if (CollectionUtils.isEmpty(uploads)) {
			throw new GlobaLException("Customer documents not uploaded");
		}
		for (CustomerDocumentUploadReferenceTemp upload : uploads) {
			switch (upload.getCustomerDocumentTypeMaster().getDocumentCategory()) {
			case "KYC_PROOF":
				customerKycManager.uploadAndCreateKyc(customer, upload);
				break;

			default:
				customerDocumentUploadManager.uploadDocument(customer, upload);
				break;
			}
		}
		if (!ConstantDocument.Yes.equals(customer.getIsActive())) {
			checkAndActivateCustomer(uploads, customerId);
		}
	}

	public void moveCustomerDBDocuments(BigDecimal customerId) {
		Customer customer = userService.getCustById(customerId);
		moveCustomerDBKycDocuments(customer);
	}

	public void moveCustomerDBKycDocuments(Customer customer) {
		DmsApplMapping dmsMapping = customerIdProofManager.getDmsMapping(customer);
		if (dmsMapping != null) {
			databaseImageScanManager.copyBlobDataFromJava(dmsMapping.getDocBlobId(), dmsMapping.getFinancialYear());
		}
	}

	public UploadCustomerDocumentResponse uploadDocument(UploadCustomerDocumentRequest uploadCustomerDocumentRequest) {
		kycScanValidator.validateUploadDocumentRequest(uploadCustomerDocumentRequest);
		CustomerDocumentTypeMaster customerDocumentTypeMaster = customerDocMasterManager
				.getDocTypeMaster(uploadCustomerDocumentRequest.getDocumentCategory(), uploadCustomerDocumentRequest.getDocumentType());
		customerDocumentUploadManager.findAndDeleteExistingUploadData(uploadCustomerDocumentRequest.getIdentityInt(),
				uploadCustomerDocumentRequest.getIdentityTypeId(), customerDocumentTypeMaster);
		BigDecimal blobId = databaseImageScanManager.uploadCustomerDocument(uploadCustomerDocumentRequest);
		if(metaData.getCustomerId() != null) {
			checkAndRemoveBlockedDocuments(metaData.getCustomerId(), customerDocumentTypeMaster);
		}
		return new UploadCustomerDocumentResponse(blobId);
	}

	public void checkAndActivateCustomer(List<CustomerDocumentUploadReferenceTemp> uploads, BigDecimal customerId) {
		Customer customer = userService.getCustById(customerId);
		CustomerDocumentTypeMaster kycDocTypeMaster = customerDocMasterManager.getKycDocTypeMaster(customer.getIdentityTypeId());
		Optional<CustomerDocumentUploadReferenceTemp> kycUpload = uploads.stream()
				.filter(i -> kycDocTypeMaster.getDocumentCategory().equals(i.getCustomerDocumentTypeMaster().getDocumentCategory())).findFirst();
		if (!kycUpload.isPresent()) {
			return;
		}
		log.info("activating customer");
		// TODO look for comlinace blocked customer and dont acitvate
		List<CustomerIdProof> idProofs = customerIdProofManager.fetchCustomerIdProofsForCustomerActivation(customerId);
		if (idProofs.size() > 1) {
			throw new GlobaLException("duplicate customer id proof records. Deactivate one of id proof with status 'C' or 'Y' ");
		}
		if (idProofs.size() == 0) {
			throw new GlobaLException("kyc not added");
		}

		String data = kycUpload.get().getUploadData();
		CustomerKycData customerKycData = JsonUtil.fromJson(data, CustomerKycData.class);

		CustomerIdProof idProof = idProofs.get(0);
		idProof.setIdentityStatus(ConstantDocument.Compliance);
		idProof.setScanSystem(kycUpload.get().getScanIndic().getIndicValue());
		customerIdProofManager.saveIdProof(idProof);
		customerIdProofManager.activateCustomerPendingCompliance(customer, customerKycData.getExpiryDate());
	}
	
	@Transactional
	public void checkAndRemoveBlockedDocuments(BigDecimal customerId, CustomerDocumentTypeMaster docTypeMaster) {
		Customer customer = userService.getCustById(customerId);
		List<CustomerDocumentTypeMaster> complianceBlocked = customer.getComplianceBlockedDocuments();
		if (complianceBlocked.contains(docTypeMaster)) {
			complianceBlocked.remove(docTypeMaster);
		}
		customerDao.saveCustomer(customer);
	}
}
