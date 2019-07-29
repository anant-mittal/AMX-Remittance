package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.sql.SQLException;

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
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.model.customer.DocumentImageRenderType;
import com.amx.jax.model.customer.document.UploadCustomerDocumentRequest;
import com.amx.jax.model.customer.document.UploadCustomerKycRequest;
import com.amx.jax.repository.DOCBLOBRepository;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
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
	@Autowired
	CustomerDocMasterManager customerDocMasterManager;

	@Override
	public CustomerDocumentInfo fetchKycImageInfo(CustomerIdProof customerIdProof) {
		CustomerDocumentInfo customerDocumentImage = new CustomerDocumentInfo();
		DmsApplMapping dmsMapping = customerIdProofManager.getDmsMapping(customerIdProof);
		CustomerDocumentTypeMaster kycDocTypeMaster = customerDocMasterManager.getKycDocTypeMaster(customerIdProof.getIdentityTypeId());
		customerDocumentImage.setDocumentCategory(kycDocTypeMaster.getDocumentCategory());
		customerDocumentImage.setDocumentType(kycDocTypeMaster.getDocumentType());
		if (dmsMapping != null) {
			try {
				BigDecimal docBlobId = dmsMapping.getDocBlobId();
				BigDecimal docFinYear = dmsMapping.getFinancialYear();
				customerDocumentDao.copyBlobDataIntoJava(docBlobId, docFinYear);
				DocBlobUpload docBlobUpload = dOCBLOBRepository.findByDocBlobIDAndDocFinYear(docBlobId, docFinYear);
				if (docBlobUpload == null) {
					return customerDocumentImage;
				}
				customerDocumentImage.setDocumentRenderType(DocumentImageRenderType.TEXT);
				int blobLength = (int) docBlobUpload.getDocContent().length();
				byte[] blobAsBytes = docBlobUpload.getDocContent().getBytes(1, blobLength);
				String kycImage = Base64.encodeBase64String(blobAsBytes);
				customerDocumentImage.setDocumentString(kycImage);
				customerDocumentImage.setDocumentFormat(dmsMapping.getDocFormat());
				customerDocumentImage.setUploadedDate(dmsMapping.getCreatedOn());

			} catch (SQLException e) {
				log.error("error in fetch kyc imagage", e);
			}
		}
		return customerDocumentImage;
	}

	/**
	 * upload kyc document into db
	 * 
	 * @param uploadCustomerKycRequest
	 * @return db identifier as reference of upload
	 */
	@Transactional
	public BigDecimal uploadKycDocument(UploadCustomerKycRequest uploadCustomerKycRequest) {
		CustomerDocumentTypeMaster docTypeMaster = customerDocMasterManager.getKycDocTypeMaster(uploadCustomerKycRequest.getIdentityTypeId());
		customerDocumentUploadManager.findAndDeleteExistingUploadData(uploadCustomerKycRequest.getIdentityInt(),
				uploadCustomerKycRequest.getIdentityTypeId(), docTypeMaster);
		CustomerDocumentUploadReferenceTemp docUploadRef = new CustomerDocumentUploadReferenceTemp();
		docUploadRef.setScanIndic(DocumentScanIndic.DB_SCAN);
		docUploadRef.setCustomerDocumentTypeMaster(docTypeMaster);
		docUploadRef.setDocFormat(uploadCustomerKycRequest.getDocFormat());
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

	public BigDecimal uploadCustomerDocument(UploadCustomerDocumentRequest uploadCustomerDocumentRequest) {

		CustomerDocumentTypeMaster docTypeMaster = customerDocMasterManager.getDocTypeMaster(uploadCustomerDocumentRequest.getDocumentCategory(),
				uploadCustomerDocumentRequest.getDocumentType());
		customerDocumentUploadManager.findAndDeleteExistingUploadData(uploadCustomerDocumentRequest.getIdentityInt(),
				uploadCustomerDocumentRequest.getIdentityTypeId(), docTypeMaster);
		CustomerDocumentUploadReferenceTemp docUploadRef = new CustomerDocumentUploadReferenceTemp();
		docUploadRef.setScanIndic(DocumentScanIndic.DB_SCAN);
		docUploadRef.setCustomerDocumentTypeMaster(docTypeMaster);
		docUploadRef.setDocFormat(uploadCustomerDocumentRequest.getDocFormat());
		byte[] documentByteArray = Base64.decodeBase64(uploadCustomerDocumentRequest.getDocument());
		try {
			docUploadRef.setDbScanDocumentBlob(new SerialBlob(documentByteArray));
			docUploadRef.setIdentityInt(uploadCustomerDocumentRequest.getIdentityInt());
			docUploadRef.setIdentityTypeId(uploadCustomerDocumentRequest.getIdentityTypeId());
			docUploadRef.setUploadData(JsonUtil.toJson(uploadCustomerDocumentRequest.getData()));
		} catch (SQLException e) {
			log.error("error in uploadCustomerDocument ", e);
		}
		customerDocumentUploadManager.save(docUploadRef);
		return docUploadRef.getId();

	}

	@Override
	public CustomerDocumentInfo getDocumentInfo(CustomerDocumentUploadReference upload) {
		CustomerDocumentInfo info = new CustomerDocumentInfo();
		info.setDocumentFormat(upload.getDbScanRef().getDocFormat());
		info.setDocumentRenderType(DocumentImageRenderType.TEXT);
		info.setUploadedDate(upload.getCreatedAt());
		info.setDocumentType(upload.getCustomerDocumentTypeMaster().getDocumentType());
		info.setDocumentCategory(upload.getCustomerDocumentTypeMaster().getDocumentCategory());
		BigDecimal docBlobId = upload.getDbScanRef().getBlobId();
		BigDecimal docFinYear = upload.getDbScanRef().getDocFinYear();
		copyBlobDataIntoJava(docBlobId, docFinYear);
		DocBlobUpload docBlobUpload = dOCBLOBRepository.findByDocBlobIDAndDocFinYear(docBlobId, docFinYear);
		if (docBlobUpload == null) {
			log.error("image not found for parameters docblobid {}, docfinyear {}", docBlobId, docFinYear);
		}
		if (docBlobUpload != null) {
			String docImage;
			try {
				int blobLength = (int) docBlobUpload.getDocContent().length();
				byte[] blobAsBytes = docBlobUpload.getDocContent().getBytes(1, blobLength);
				docImage = Base64.encodeBase64String(blobAsBytes);
				info.setDocumentString(docImage);
			} catch (SQLException e) {
				log.error("error in getDocumentInfo", e);
			}
		}
		return info;
	}
}
