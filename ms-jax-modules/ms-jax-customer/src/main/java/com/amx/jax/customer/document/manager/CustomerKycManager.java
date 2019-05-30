package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.dbmodel.customer.DmsDocumentBlobTemparory;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CustomerKycData;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.repository.IUserFinancialYearRepo;
import com.amx.jax.repository.customer.DmsDocumentBlobTemparoryRepository;
import com.amx.jax.services.JaxDBService;
import com.amx.utils.JsonUtil;

@Component
public class CustomerKycManager {

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

	public void uploadAndCreateKyc(Customer customer, CustomerDocumentUploadReferenceTemp upload) {

		switch (upload.getScanIndic()) {
		case DB_SCAN:
			uploadDbScan(customer, upload);
		default:
			break;
		}

	}

	private void uploadDbScan(Customer customer, CustomerDocumentUploadReferenceTemp upload) {
		DmsDocumentBlobTemparory dmsDocumentBlobTemparory = new DmsDocumentBlobTemparory();
		dmsDocumentBlobTemparory.setCreatedBy(jaxDBService.getCreatedOrUpdatedBy());
		dmsDocumentBlobTemparory.setCreatedDate(new Date());
		CustomerKycData data = JsonUtil.fromJson(upload.getUploadData(), CustomerKycData.class);
		ImageSubmissionRequest imageSubmissionRequest = new ImageSubmissionRequest();
		imageSubmissionRequest.setIdentityExpiredDate(data.getExpiryDate());
		//dmsDocumentBlobTemparory.setDocBlobId(docBlobId);
		//dmsDocumentBlobTemparory.setDocFinYear(docFinYear);
		dmsDocumentBlobTemparory.setDocumentContent(upload.getDbScanDocumentBlob());
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

	BigDecimal getDealYearbyDate() throws ParseException {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date today = Calendar.getInstance().getTime();
		String date = formatter.format(today);
		UserFinancialYear list = userFinancialYearRepo.findAllByFinancialYearBegin(date, date);
		BigDecimal financialYear = list.getFinancialYear();
		return financialYear;
	}
}
