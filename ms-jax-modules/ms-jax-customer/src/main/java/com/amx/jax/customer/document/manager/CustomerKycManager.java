package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.CustomerDocumentDao;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.dbmodel.customer.DmsDocumentBlobTemparory;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CustomerKycData;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.repository.IDMSAppMappingRepository;
import com.amx.jax.repository.IUserFinancialYearRepo;
import com.amx.jax.repository.customer.DmsDocumentBlobTemparoryRepository;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.Constants;
import com.amx.utils.JsonUtil;
import com.jax.amxlib.exception.jax.GlobaLException;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerKycManager {

	private static final Logger log = LoggerFactory.getLogger(CustomerKycManager.class);

	@Autowired
	DmsDocumentBlobTemparoryRepository dmsDocumentBlobTemparoryRepository;
	@Autowired
	JaxDBService jaxDBService;
	@Autowired
	IUserFinancialYearRepo userFinancialYearRepo;
	@Autowired
	ImageCheckDao imageCheckDao;
	@Autowired
	MetaData metaData;
	@Autowired
	CustomerDocumentDao customerDocumentDao;
	@Autowired
	CustomerIdProofManager idProofManager;
	@Autowired
	IDMSAppMappingRepository idmsAppMappingRepository;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	UserService userService;
	@Autowired
	BizcomponentDao bizcomponentDao;
	@Autowired
	CustomerDao customerDao;

	public void uploadAndCreateKyc(Customer customer, CustomerDocumentUploadReferenceTemp upload) throws ParseException {

		createIdProofForActivateCustomers(customer, upload);
		switch (upload.getScanIndic()) {
		case DB_SCAN:
		default:
			uploadDbScan(customer, upload);
			break;
		}

	}

	private void createIdProofForActivateCustomers(Customer customer, CustomerDocumentUploadReferenceTemp upload) {

		if (!ConstantDocument.Yes.equals(customer.getIsActive())) {
			return;
		}
		CustomerIdProof existingIdProof = customerIdProofManager.getCustomerIdProofByCustomerId(customer.getCustomerId());
		if (existingIdProof == null) {
			throw new GlobaLException("No active id proof record found.");
		}
		if (!customer.getIdentityTypeId().equals(upload.getIdentityTypeId())) {
			log.info("creating id proof for duplication customer id type, current id type {}, uploaded id type {}", customer.getIdentityTypeId(),
					upload.getIdentityTypeId());
			customer.setIdentityInt(upload.getIdentityInt());
			customer.setIdentityTypeId(upload.getIdentityTypeId());
			customerDao.saveCustomer(customer);
		}
		CustomerKycData data = JsonUtil.fromJson(upload.getUploadData(), CustomerKycData.class);
		userService.deActivateCustomerIdProof(customer.getCustomerId());
		userService.deactiveteCustomerIdProofPendingCompliance(customer.getCustomerId());
		CustomerIdProof newIdProof = new CustomerIdProof();
		newIdProof.setIdentityTypeId(upload.getIdentityTypeId());
		newIdProof.setIdentityStatus(ConstantDocument.Yes);
		newIdProof.setCreatedBy(jaxDBService.getCreatedOrUpdatedBy());
		newIdProof.setFsCustomer(customer);
		newIdProof.setLanguageId(metaData.getLanguageId());
		BizComponentData customerType = new BizComponentData();
		customerType.setComponentDataId(
				bizcomponentDao.getComponentId(Constants.CUSTOMERTYPE_INDU, metaData.getLanguageId()).getFsBizComponentData().getComponentDataId());
		newIdProof.setFsBizComponentDataByCustomerTypeId(customerType);
		newIdProof.setIdentityInt(upload.getIdentityInt());
		newIdProof.setCreationDate(new Date());
		newIdProof.setIdentityTypeId(customer.getIdentityTypeId());
		newIdProof.setIdentityExpiryDate(data.getExpiryDate());
		newIdProof.setIdentityFor(ConstantDocument.IDENTITY_FOR_ID_PROOF);
		newIdProof.setScanSystem(Constants.CUST_DB_SCAN);
		customerIdProofManager.saveIdProof(newIdProof);

	}

	private void uploadDbScan(Customer customer, CustomerDocumentUploadReferenceTemp upload) throws ParseException {
		DmsDocumentBlobTemparory dmsDocumentBlobTemparory = new DmsDocumentBlobTemparory();
		dmsDocumentBlobTemparory.setCreatedBy(jaxDBService.getCreatedOrUpdatedBy());
		dmsDocumentBlobTemparory.setCreatedDate(new Date());
		CustomerKycData data = JsonUtil.fromJson(upload.getUploadData(), CustomerKycData.class);
		ImageSubmissionRequest imageSubmissionRequest = new ImageSubmissionRequest();
		imageSubmissionRequest.setIdentityExpiredDate(data.getExpiryDate());
		DmsApplMapping dmsApplMapping = getDmsApplMappingData(customer, imageSubmissionRequest);
		dmsApplMapping.setCustomerId(customer.getCustomerId());
		dmsApplMapping.setIdentityExpiryDate(data.getExpiryDate());
		BigDecimal docBlobId = dmsApplMapping.getDocBlobId();
		BigDecimal docFinYear = dmsApplMapping.getFinancialYear();
		dmsDocumentBlobTemparory.setDocBlobId(docBlobId);
		dmsDocumentBlobTemparory.setDocFinYear(docFinYear);
		dmsDocumentBlobTemparory.setDocumentContent(upload.getDbScanDocumentBlob());
		dmsDocumentBlobTemparory.setCountryCode(dmsApplMapping.getApplicationCountryId());
		dmsDocumentBlobTemparory.setSeqNo(BigDecimal.ONE);
		dmsDocumentBlobTemparoryRepository.save(dmsDocumentBlobTemparory);
		// commenting as data will be copied only after blob records are commited in db
		// so do it at final step
		// dmsDocumentBlobTemparoryRepository.copyBlobDataFromJava(docBlobId,
		// docFinYear);
		idmsAppMappingRepository.save(dmsApplMapping);
	}

	public DmsApplMapping getDmsApplMappingData(Customer model, ImageSubmissionRequest imageSubmissionRequest) throws ParseException {
		DmsApplMapping mappingData = new DmsApplMapping();
		BigDecimal financialYear = getDealYearbyDate();
		BigDecimal applCountryId = metaData.getCountryId();
		BigDecimal docBlobId = imageCheckDao.callTogenerateBlobID(financialYear);
		mappingData.setApplicationCountryId(applCountryId);
		mappingData.setCustomerId(metaData.getCustomerId());
		mappingData.setDocBlobId(docBlobId); // need to change value
		mappingData.setDocFormat("JPG");
		mappingData.setFinancialYear(financialYear);
		if (imageSubmissionRequest != null && imageSubmissionRequest.getIdentityExpiredDate() != null) {
			mappingData.setIdentityExpiryDate(imageSubmissionRequest.getIdentityExpiredDate());
		} else {
			mappingData.setIdentityExpiryDate(model.getIdentityExpiredDate());
		}
		mappingData.setIdentityInt(model.getIdentityInt());
		mappingData.setIdentityIntId(model.getIdentityTypeId());
		mappingData.setCreatedBy(metaData.getEmployeeId().toString());
		mappingData.setCreatedOn(new Date());
		return mappingData;
	}

	public BigDecimal getDealYearbyDate() throws ParseException {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date today = Calendar.getInstance().getTime();
		String date = formatter.format(today);
		UserFinancialYear list = userFinancialYearRepo.findAllByFinancialYearBegin(date, date);
		BigDecimal financialYear = list.getFinancialYear();
		return financialYear;
	}
}
