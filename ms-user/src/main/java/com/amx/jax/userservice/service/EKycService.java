package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.EKycModel;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.DocBlobUpload;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.repository.DOCBLOBRepository;
import com.amx.jax.repository.IDMSAppMappingRepository;
import com.amx.jax.repository.IUserFinancialYearRepo;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.CustomerIdProofDao;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class EKycService {
	@Autowired
	MetaData metaData;
	
	@Autowired
	ImageCheckDao imageCheckDao;

	@Autowired
	DOCBLOBRepository docblobRepository;

	@Autowired
	IDMSAppMappingRepository idmsAppMappingRepository;

	@Autowired
	IUserFinancialYearRepo userFinancialYearRepo;

	@Autowired
	private CustomerDao custDao;
	
	@Autowired
	private CustomerIdProofDao customerIdProofDao;

	Logger logger = Logger.getLogger(EKycService.class);
	
	public BoolRespModel eKycsaveDetails(String image, String stexpiryDate) throws ParseException {
		if(org.apache.commons.lang.StringUtils.isEmpty(image)) {
			throw new GlobalException("Kyc document image cannot be null");
		}
		if(org.apache.commons.lang.StringUtils.isEmpty(image)) {
			throw new GlobalException("Expiry date cannot be null");
		}
		
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		//OffsiteCustomerRegManager offsiteCustomerRegManager = new OffsiteCustomerRegManager();
		List<CustomerIdProof> customerIdProofList = customerIdProofDao.getCustomerIdProofs(metaData.getCustomerId());
		CustomerIdProof customerIdProof=customerIdProofList.get(0);
		
		DmsApplMapping mappingData = new DmsApplMapping();
		mappingData = getDmsApplMappingData(customer);
		idmsAppMappingRepository.save(mappingData);
		DocBlobUpload documentDetails = new DocBlobUpload();
		documentDetails = getDocumentUploadDetails(image, mappingData);
		docblobRepository.save(documentDetails);
		
		Date expiryDate=new SimpleDateFormat("dd/MM/yyyy").parse(stexpiryDate);  
		customerIdProof.setIdentityExpiryDate(expiryDate);
		List<CustomerIdProof> custIdProofList = new ArrayList<CustomerIdProof>();
		custIdProofList.add(customerIdProof);
		customerIdProofDao.save(custIdProofList);
		
		BoolRespModel boolRespModel = new BoolRespModel();
		boolRespModel.setSuccess(Boolean.TRUE);
		return boolRespModel;
	}
	
	private DocBlobUpload getDocumentUploadDetails(String image, DmsApplMapping mappingData) {
		DocBlobUpload documentDetails = new DocBlobUpload();
		documentDetails.setCntryCd(mappingData.getApplicationCountryId());
		documentDetails.setDocBlobID(mappingData.getDocBlobId());
		documentDetails.setDocFinYear(mappingData.getFinancialYear());
		documentDetails.setSeqNo(new BigDecimal(1));
		

		try {
			Blob documentContent = new javax.sql.rowset.serial.SerialBlob(decodeImage(image));
			documentDetails.setDocContent(documentContent);
		} catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		documentDetails.setCreatedOn(new Date());
		documentDetails.setCreatedBy(metaData.getCustomerId().toString());
		return documentDetails;
	}

	public static byte[] decodeImage(String imageDataString) {
		return Base64.decodeBase64(imageDataString);
		
	}

	private DmsApplMapping getDmsApplMappingData(Customer model) throws ParseException {
		DmsApplMapping mappingData = new DmsApplMapping();
		BigDecimal financialYear = getDealYearbyDate();
		BigDecimal applCountryId = metaData.getCountryId();
		BigDecimal docBlobId = imageCheckDao.callTogenerateBlobID(financialYear);
		mappingData.setApplicationCountryId(applCountryId);
		mappingData.setCustomerId(metaData.getCustomerId());
		mappingData.setDocBlobId(docBlobId); // need to change value
		mappingData.setDocFormat("JPG");
		mappingData.setFinancialYear(financialYear);
		mappingData.setIdentityExpiryDate(model.getIdentityExpiredDate());
		mappingData.setIdentityInt(model.getIdentityInt());
		mappingData.setIdentityIntId(model.getIdentityTypeId());
		if(metaData.getEmployeeId()==null) {
			mappingData.setCreatedBy("WEB");
			logger.info("created by is set");
		}	
		else {
		mappingData.setCreatedBy(metaData.getEmployeeId().toString());
		}

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
	
	public EKycModel eKycgetDetails() {
		EKycModel eKycModel = new EKycModel();
		return eKycModel;
		
	}

}
