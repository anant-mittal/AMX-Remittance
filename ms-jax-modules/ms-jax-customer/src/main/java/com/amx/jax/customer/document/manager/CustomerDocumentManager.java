package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.client.compliance.ComplianceTrnxdDocStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.DocumentScanIndic;
import com.amx.jax.customer.document.validate.DocumentScanValidator;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.IdentityTypeMaster;
import com.amx.jax.dbmodel.compliance.ComplianceBlockedCustomerDocMap;
import com.amx.jax.dbmodel.compliance.ComplianceBlockedTrnxDocMap;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.dbmodel.customer.DbScanRef;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.model.customer.CustomerKycData;
import com.amx.jax.model.customer.document.UploadCustomerDocumentRequest;
import com.amx.jax.model.customer.document.UploadCustomerDocumentResponse;
import com.amx.jax.model.customer.document.UploadCustomerKycRequest;
import com.amx.jax.model.customer.document.UploadCustomerKycResponse;
import com.amx.jax.repository.compliance.ComplianceBlockedCustomerDocMapRepo;
import com.amx.jax.repository.customer.CustomerDocumentUploadReferenceRepo;
import com.amx.jax.repository.customer.DbScanRefRepo;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.JsonUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerDocumentManager {

	@Autowired
	DatabaseScanManager databaseImageScanManager;
	@Autowired
	ArcmateScanManager arcmateScanManager;
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
	@Autowired
	CustomerDocumentUploadReferenceRepo customerDocumentUploadReferenceRepo;
	@Autowired
	DbScanRefRepo dbScanRefRepo;
	@Autowired
	ComplianceBlockedCustomerDocMapRepo complianceBlockedCustomerDocMapRepo;

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
		List<CustomerDocumentUploadReference> uploads = customerDocumentUploadReferenceRepo.findByCustomerIdAndStatusIn(customerId,
				Arrays.asList(ConstantDocument.Yes, ConstantDocument.No));
		return uploads.stream().map(i -> {
			CustomerDocumentInfo docInfo = new CustomerDocumentInfo();
			if (i.getScanIndic().equals(DocumentScanIndic.DB_SCAN)) {
				docInfo = databaseImageScanManager.getDocumentInfo(i);
			}
			return docInfo;
		}).collect(Collectors.toList());
	}

	private CustomerDocumentInfo fetchKycCustomerImage(BigDecimal customerId) {
		Customer customer = userService.getCustById(customerId);
		CustomerIdProof customerIdProof = customerIdProofManager.getCustomerIdProofByCustomerId(customerId);
		if (customerIdProof == null) {
			List<CustomerIdProof> idProofPendingCompliance = customerIdProofManager.getCustomerIdProofPendingCompliance(customer.getCustomerId(),
					customer.getIdentityTypeId());
			if (idProofPendingCompliance.size() > 0) {
				customerIdProof = idProofPendingCompliance.get(0);
			}
		}
		CustomerDocumentInfo customerDocumentImage = null;
		if (customerIdProof != null && customerIdProof.getScanSystem() != null) {
			switch (customerIdProof.getScanSystem()) {
			case "D":
				customerDocumentImage = databaseImageScanManager.fetchKycImageInfo(customerIdProof);
				break;
			case "A":
				customerDocumentImage = arcmateScanManager.fetchKycImageInfo(customerIdProof);
				break;
			default:
			}
		}
		if(customerIdProof != null && customerIdProof.getScanSystem() == null) {
			customerDocumentImage = databaseImageScanManager.fetchKycImageInfo(customerIdProof);
		}
		if (customerDocumentImage != null) {
			addDataFromCustomerIdProof(customerDocumentImage, customerIdProof);
		}
		return customerDocumentImage;
	}

	private void addDataFromCustomerIdProof(CustomerDocumentInfo customerDocumentImage, CustomerIdProof customerIdProof) {
		IdentityTypeMaster identityMaster = customerIdProofManager.getIdentityTypeMaster(customerIdProof.getIdentityTypeId(), ConstantDocument.Yes);
		customerDocumentImage.setDocumentType(identityMaster.getIdentityType());
		customerDocumentImage.setUploadedDate(customerIdProof.getCreationDate());
		customerDocumentImage.setExpiryDate(customerIdProof.getIdentityExpiryDate());
	}

	public UploadCustomerKycResponse uploadKycDocument(UploadCustomerKycRequest uploadCustomerKycRequest) {
		kycScanValidator.validateUploadKycDocumentRequest(uploadCustomerKycRequest);
		BigDecimal uploadReference = databaseImageScanManager.uploadKycDocument(uploadCustomerKycRequest);
		UploadCustomerKycResponse uploadCustomerKycResponse = new UploadCustomerKycResponse();
		uploadCustomerKycResponse.setUploadReference(uploadReference);
		return uploadCustomerKycResponse;
	}

	public void addCustomerDocument(BigDecimal customerId) throws ParseException {
		log.debug("in addCustomerDocument");
		Customer customer = userService.getCustById(customerId);
		List<CustomerDocumentUploadReferenceTemp> uploads = customerDocumentUploadManager.getCustomerUploads(customer.getIdentityInt(),
				customer.getIdentityTypeId());
		if (CollectionUtils.isEmpty(uploads)) {
			log.info("no customer docs to update");
			return;
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
		customerDocumentUploadManager.deleteTemporaryUploadData(customer.getIdentityInt(), customer.getIdentityTypeId());
	}

	public void moveCustomerDBDocuments(BigDecimal customerId) {
		List<CustomerDocumentUploadReference> customerUploadDocuments = getInProcessCustomerUploads(customerId);
		if (CollectionUtils.isEmpty(customerUploadDocuments)) {
			return;
		}
		customerUploadDocuments.forEach(i -> {
			DbScanRef dbScan = dbScanRefRepo.findOne(i.getId());
			BigDecimal blobId = dbScan.getBlobId();
			BigDecimal docFinYear = dbScan.getDocFinYear();
			databaseImageScanManager.copyBlobDataFromJava(blobId, docFinYear);
			i.setStatus(ConstantDocument.Yes);
		});
		saveCustomerDocumentUploadsRefs(customerUploadDocuments);
	}

	public void moveCustomerDBKycDocuments(Customer customer, CustomerDocumentUploadReferenceTemp kycUploadReferenceTemp) {
		log.info("moving customer db kyc docs");
		CustomerKycData data = JsonUtil.fromJson(kycUploadReferenceTemp.getUploadData(), CustomerKycData.class);
		DmsApplMapping dmsMapping = customerIdProofManager.getDmsMapping(customer, data.getExpiryDate());
		if (dmsMapping == null) {
			StringBuffer sbuff = new StringBuffer();
			sbuff.append("dmsappl map record not found params: identity int: ").append(customer.getIdentityInt());
			sbuff.append(" id type: ").append(customer.getIdentityTypeId());
			sbuff.append(" customer id: ").append(customer.getCustomerId());
			sbuff.append("id expiery date: ").append(customer.getIdentityExpiredDate());
			throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, sbuff.toString());
		}
		databaseImageScanManager.copyBlobDataFromJava(dmsMapping.getDocBlobId(), dmsMapping.getFinancialYear());
	}

	public UploadCustomerDocumentResponse uploadDocument(UploadCustomerDocumentRequest uploadCustomerDocumentRequest) {
		kycScanValidator.validateUploadDocumentRequest(uploadCustomerDocumentRequest);
		CustomerDocumentTypeMaster customerDocumentTypeMaster = customerDocMasterManager
				.getDocTypeMaster(uploadCustomerDocumentRequest.getDocumentCategory(), uploadCustomerDocumentRequest.getDocumentType());
		customerDocumentUploadManager.findAndDeleteExistingUploadData(uploadCustomerDocumentRequest.getIdentityInt(),
				uploadCustomerDocumentRequest.getIdentityTypeId(), customerDocumentTypeMaster);
		BigDecimal uploadTempRefId = databaseImageScanManager.uploadCustomerDocument(uploadCustomerDocumentRequest);
		if (metaData.getCustomerId() != null) {
			checkAndRemoveBlockedDocuments(metaData.getCustomerId(), customerDocumentTypeMaster);
		}
		return new UploadCustomerDocumentResponse(uploadTempRefId);
	}

	public void checkAndActivateCustomer(List<CustomerDocumentUploadReferenceTemp> uploads, BigDecimal customerId) {
		log.debug("in checkAndActivateCustomer");
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
			throw new GlobalException("duplicate customer id proof records. Deactivate one of id proof with status 'C' or 'Y' ");
		}
		if (idProofs.size() == 0) {
			throw new GlobalException("kyc not added");
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
		List<ComplianceBlockedCustomerDocMap> complianceBlockedDocs = customer.getComplianceBlockedDocuments();
		complianceBlockedDocs.stream().forEach(i -> {
			if (i.getStatus().equals(ComplianceTrnxdDocStatus.REQUESTED) && i.getDocTypeMaster().equals(docTypeMaster)) {
				i.setStatus(ComplianceTrnxdDocStatus.UPLOADED);
			}
		});

		complianceBlockedCustomerDocMapRepo.save(complianceBlockedDocs);
	}

	public List<CustomerDocumentUploadReference> getCustomerUploads(BigDecimal customerId) {
		List<CustomerDocumentUploadReference> uploads = customerDocumentUploadReferenceRepo.findByCustomerId(customerId);
		return uploads;
	}

	public List<CustomerDocumentUploadReference> getInProcessCustomerUploads(BigDecimal customerId) {
		List<CustomerDocumentUploadReference> uploads = customerDocumentUploadReferenceRepo.findByCustomerIdAndStatusIn(customerId,
				Arrays.asList(ConstantDocument.Processing));
		return uploads;
	}

	public void saveCustomerDocumentUploadsRefs(List<CustomerDocumentUploadReference> uploads) {
		customerDocumentUploadReferenceRepo.save(uploads);
	}

	public CustomerDocumentInfo convertToCustomerDocumentInfo(ComplianceBlockedTrnxDocMap complianceBlockedTrnxDocMap) {
		CustomerDocumentInfo docInfo = new CustomerDocumentInfo();
		CustomerDocumentUploadReference i = complianceBlockedTrnxDocMap.getCustomerDocumentUploadReference();
		String docType = complianceBlockedTrnxDocMap.getDocTypeMaster().getDocumentType();
		if (i != null && i.getScanIndic().equals(DocumentScanIndic.DB_SCAN)) {
			docInfo = databaseImageScanManager.getDocumentInfo(i);
			docInfo.setUploadRefId(i.getId());
		}
		docInfo.setDocumentCategory(complianceBlockedTrnxDocMap.getDocTypeMaster().getDocumentCategory());
		docInfo.setDocumentType(docType);
		if (customerDocMasterManager.getDocumentTypeDesc(docType) != null) {
			docInfo.setDocumentTypeDesc(customerDocMasterManager.getDocumentTypeDesc(docType));
		}
		return docInfo;
	}

}
