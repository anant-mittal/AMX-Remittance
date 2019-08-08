package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.DocumentScanIndic;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.dbmodel.customer.DbScanRef;
import com.amx.jax.dbmodel.customer.DmsDocumentBlobTemparory;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.customer.CustomerDocumentUploadReferenceRepo;
import com.amx.jax.repository.customer.CustomerDocumentUploadReferenceTempRepo;
import com.amx.jax.repository.customer.DbScanRefRepo;
import com.amx.jax.repository.customer.DmsDocumentBlobTemparoryRepository;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.userservice.service.UserService;
import com.jax.amxlib.exception.jax.GlobaLException;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerDocumentUploadManager {

	private static final Logger log = LoggerFactory.getLogger(CustomerDocumentUploadManager.class);

	@Autowired
	CustomerDocumentUploadReferenceTempRepo customerDocumentUploadReferenceTempRepo;
	@Autowired
	CustomerDocumentUploadReferenceRepo customerDocumentUploadReferenceRepo;
	@Autowired
	JaxDBService jaxDBService;
	@Autowired
	DmsDocumentBlobTemparoryRepository dmsDocumentBlobTemparoryRepository;
	@Autowired
	ImageCheckDao imageCheckDao;
	@Autowired
	CustomerKycManager customerKycManager;
	@Autowired
	MetaData metaData;
	@Autowired
	CustomerDocMasterManager customerDocMasterManager;
	@Autowired
	DbScanRefRepo dbScanRefRepo;
	@Autowired
	UserService userSerivce;

	public void findAndDeleteExistingUploadData(String identityInt, BigDecimal identityType, CustomerDocumentTypeMaster customerDocumentTypeMaster) {

		CustomerDocumentUploadReferenceTemp existing = customerDocumentUploadReferenceTempRepo
				.findByidentityIntAndIdentityTypeIdAndCustomerDocumentTypeMaster(identityInt, identityType, customerDocumentTypeMaster);
		if (existing != null) {
			log.debug("found exising uploaded record {}", log);
			customerDocumentUploadReferenceTempRepo.delete(existing);
		}
	}

	public void deleteTemporaryUploadData(String identityInt, BigDecimal identityType) {

		List<CustomerDocumentUploadReferenceTemp> existing = customerDocumentUploadReferenceTempRepo.findByidentityIntAndIdentityTypeId(identityInt,
				identityType);
		if (existing != null) {
			log.debug("found exising uploaded record {}", log);
			customerDocumentUploadReferenceTempRepo.delete(existing);
		}
	}

	public CustomerDocumentUploadReferenceTemp getUploadData(String identityInt, BigDecimal identityType,
			CustomerDocumentTypeMaster customerDocumentTypeMaster) {

		CustomerDocumentUploadReferenceTemp existing = customerDocumentUploadReferenceTempRepo
				.findByidentityIntAndIdentityTypeIdAndCustomerDocumentTypeMaster(identityInt, identityType, customerDocumentTypeMaster);
		return existing;
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

	public void uploadDocument(Customer customer, CustomerDocumentUploadReferenceTemp upload) throws ParseException {
		switch (upload.getScanIndic()) {
		case DB_SCAN:
			uploadDbDocument(customer, upload);
		default:
			break;
		}

	}

	private void uploadDbDocument(Customer customer, CustomerDocumentUploadReferenceTemp upload) throws ParseException {
		DmsDocumentBlobTemparory dmsDocumentBlobTemparory = new DmsDocumentBlobTemparory();
		dmsDocumentBlobTemparory.setCreatedBy(jaxDBService.getCreatedOrUpdatedBy());
		dmsDocumentBlobTemparory.setCreatedDate(new Date());
		BigDecimal docFinYear = customerKycManager.getDealYearbyDate();
		BigDecimal docBlobId = imageCheckDao.callTogenerateBlobID(docFinYear);
		log.info("blob id generated in uploadDbDocument {}", docBlobId);
		dmsDocumentBlobTemparory.setDocBlobId(docBlobId);
		dmsDocumentBlobTemparory.setDocFinYear(docFinYear);
		dmsDocumentBlobTemparory.setDocumentContent(upload.getDbScanDocumentBlob());
		dmsDocumentBlobTemparory.setCountryCode(metaData.getCountryId());
		dmsDocumentBlobTemparory.setSeqNo(BigDecimal.ONE);
		dmsDocumentBlobTemparoryRepository.save(dmsDocumentBlobTemparory);
		// dmsDocumentBlobTemparoryRepository.copyBlobDataFromJava(docBlobId,
		// docFinYear);
		createDocumentUploadReference(upload, docBlobId, docFinYear);
	}

	private void createDocumentUploadReference(CustomerDocumentUploadReferenceTemp upload, BigDecimal docBlobId, BigDecimal docFinYear) {
		List<CustomerDocumentUploadReference> existingUploads = customerDocumentUploadReferenceRepo
				.findByCustomerDocumentTypeMasterAndCustomerId(upload.getCustomerDocumentTypeMaster(), metaData.getCustomerId());
		existingUploads.forEach(i -> i.setStatus(ConstantDocument.No));
		customerDocumentUploadReferenceRepo.save(existingUploads);
		CustomerDocumentUploadReference docUploadReference = new CustomerDocumentUploadReference();
		docUploadReference.setCustomerDocumentTypeMaster(upload.getCustomerDocumentTypeMaster());
		docUploadReference.setCustomerId(metaData.getCustomerId());
		docUploadReference.setScanIndic(upload.getScanIndic());
		docUploadReference.setStatus(ConstantDocument.Processing);
		docUploadReference.setCreatedAt(new Date());
		docUploadReference = customerDocumentUploadReferenceRepo.save(docUploadReference);
		if (DocumentScanIndic.DB_SCAN.equals(upload.getScanIndic())) {
			DbScanRef dbScanref = new DbScanRef();
			dbScanref.setBlobId(docBlobId);
			dbScanref.setCustomerDocUploadRefId(docUploadReference.getId());
			dbScanref.setDocFormat(upload.getDocFormat());
			dbScanref.setDocFinYear(docFinYear);
			dbScanRefRepo.save(dbScanref);
		}
	}

	public List<CustomerDocumentTypeMaster> fetchCustomerUploadedDocMasterList() {
		BigDecimal customerId = metaData.getCustomerId();
		if (customerId == null) {
			throw new GlobaLException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Missing customer id");
		}
		Customer customer = userSerivce.getCustById(customerId);
		List<CustomerDocumentUploadReferenceTemp> customerUploads = getCustomerUploads(customer.getIdentityInt(), customer.getIdentityTypeId());
		return customerUploads.stream().map(i -> {
			return i.getCustomerDocumentTypeMaster();
		}).collect(Collectors.toList());

	}

	public List<CustomerDocumentUploadReference> fetchCustomerUploadedDocRef(List<CustomerDocumentTypeMaster> customerTempUploads,
			BigDecimal customerId) {
		List<CustomerDocumentUploadReference> uploadRefs = new ArrayList<CustomerDocumentUploadReference>();
		for (CustomerDocumentTypeMaster cdtm : customerTempUploads) {
			List<CustomerDocumentUploadReference> list = customerDocumentUploadReferenceRepo
					.findByCustomerDocumentTypeMasterAndCustomerIdAndStatus(cdtm, customerId, ConstantDocument.Processing);
			if (!CollectionUtils.isEmpty(list)) {
				uploadRefs.addAll(list);
			}
		}
		return uploadRefs;
	}
}
