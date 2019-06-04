package com.amx.jax.customer.document.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constants.DocumentScanIndic;
import com.amx.jax.dal.CustomerDocumentDao;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.DocBlobUpload;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.model.customer.CustomerDocUploadType;
import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.model.customer.DocumentImageRenderType;
import com.amx.jax.model.customer.UploadCustomerKycRequest;
import com.amx.jax.repository.DOCBLOBRepository;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.utils.IoUtils;
import com.amx.utils.JsonUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class DatabaseScanManager implements DocumentScanManager {

	private static final Logger log = LoggerFactory.getLogger(DatabaseScanManager.class);

	@Autowired
	ImageCheckDao imageCheckDao;
	@Autowired
	DOCBLOBRepository dOCBLOBRepository;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	CustomerDocumentDao customerDocumentDao;
	@Autowired
	CustomerDocumentUploadManager customerDocumentUploadManager;

	@Override
	public CustomerDocumentInfo fetchKycImageInfo(CustomerIdProof customerIdProof) {
		CustomerDocumentInfo customerDocumentImage = null;
		DmsApplMapping dmsMapping = customerIdProofManager.getDmsMapping(customerIdProof);
		if (dmsMapping != null) {
			try {
				customerDocumentImage = new CustomerDocumentInfo();
				BigDecimal docBlobId = dmsMapping.getDocBlobId();
				BigDecimal docFinYear = dmsMapping.getFinancialYear();
				customerDocumentDao.copyBlobDataIntoJava(docBlobId, docFinYear);
				DocBlobUpload docBlobUpload = dOCBLOBRepository.findByDocBlobIDAndDocFinYear(docBlobId, docFinYear);
				customerDocumentImage.setDocumentRenderType(DocumentImageRenderType.TEXT);
				String kycImage = IoUtils.inputStreamToString(docBlobUpload.getDocContent().getBinaryStream());
				kycImage = Base64.encodeBase64String(kycImage.getBytes());
				customerDocumentImage.setDocumentString(kycImage);
			} catch (IOException | SQLException e) {
				log.error("error in fetch kyc imagage", e);
			}
		}
		return customerDocumentImage;
	}

	@Override
	public List<CustomerDocumentInfo> fetchOtherDocumentInfo(BigDecimal customerId) {
		return null;

	}

	/**
	 * upload kyc document into db
	 * 
	 * @param uploadCustomerKycRequest
	 * @return db identifier as reference of upload
	 */
	@Transactional
	public BigDecimal uploadKycDocument(UploadCustomerKycRequest uploadCustomerKycRequest) {
		customerDocumentUploadManager.findAndDeleteExistingRecord(uploadCustomerKycRequest.getIdentityInt(),
				uploadCustomerKycRequest.getIdentityTypeId(), CustomerDocUploadType.KYC_PROOF);
		CustomerDocumentUploadReferenceTemp docUploadRef = new CustomerDocumentUploadReferenceTemp();
		docUploadRef.setScanIndic(DocumentScanIndic.DB_SCAN);
		docUploadRef.setCustomerDocUploadType(CustomerDocUploadType.KYC_PROOF);
		byte[] documentByteArray = Base64.decodeBase64(uploadCustomerKycRequest.getDocument());
		try {
			docUploadRef.setDbScanDocumentBlob(new SerialBlob(documentByteArray));
			docUploadRef.setIdentityInt(uploadCustomerKycRequest.getIdentityInt());
			docUploadRef.setIdentityTypeId(uploadCustomerKycRequest.getIdentityTypeId());
			docUploadRef.setUploadData(JsonUtil.toJson(uploadCustomerKycRequest.getCustomerKycData()));
		} catch (SQLException e) {
			log.error("error in uploadKycDocument ", e);
		}
		customerDocumentUploadManager.save(docUploadRef);
		return docUploadRef.getId();
	}

	public void copyBlobDataFromJava(BigDecimal blobId, BigDecimal docFinYear) {
		log.info("calling  copyBlobDataFromJava with params blobId {}, docFinYear {}", blobId, docFinYear);
		customerDocumentDao.copyBlobDataFromJava(blobId, docFinYear);
	}

	public void copyBlobDataIntoJava(BigDecimal blobId, BigDecimal docFinYear) {
		customerDocumentDao.copyBlobDataIntoJava(blobId, docFinYear);
	}
}
