package com.amx.jax.customer.document.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constants.DocumentScanIndic;
import com.amx.jax.dal.CustomerDocumentDao;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.DocBlobUpload;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.model.customer.DocumentImageRenderType;
import com.amx.jax.model.customer.UploadCustomerKycRequest;
import com.amx.jax.repository.DOCBLOBRepository;
import com.amx.jax.repository.customer.CustomerDocumentUploadReferenceTempRepo;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.utils.IoUtils;

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
	CustomerDocumentUploadReferenceTempRepo customerDocumentUploadReferenceTempRepo;

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
	public BigDecimal uploadKycDocument(UploadCustomerKycRequest uploadCustomerKycRequest) {
		CustomerDocumentUploadReferenceTemp docUploadRef = new CustomerDocumentUploadReferenceTemp();
		docUploadRef.setScanIndic(DocumentScanIndic.DB_SCAN);
		byte[] documentByteArray = Base64.decodeBase64(uploadCustomerKycRequest.getDocument());
		try {
			docUploadRef.setDbScanDocumentBlob(new SerialBlob(documentByteArray));
		} catch (SQLException e) {
			log.error("error in uploadKycDocument ", e);
		}
		customerDocumentUploadReferenceTempRepo.save(docUploadRef);
		return docUploadRef.getId();
	}
}
