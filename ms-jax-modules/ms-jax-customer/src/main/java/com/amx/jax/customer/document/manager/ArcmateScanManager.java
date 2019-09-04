package com.amx.jax.customer.document.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.customer.ArcmateScanMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
import com.amx.jax.dbmodel.customer.ScanIdTypeMaster;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.model.customer.DocumentImageRenderType;
import com.amx.jax.repository.customer.ArcmateScanMasterRepository;
import com.amx.jax.repository.customer.ScanIdTypeMasterRepository;
import com.amx.jax.service.CountryBranchService;
import com.amx.jax.userservice.manager.CustomerIdProofManager;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class ArcmateScanManager implements DocumentScanManager {

	private static final Logger log = LoggerFactory.getLogger(ArcmateScanManager.class);

	@Autowired
	ArcmateScanMasterRepository arcmateScanMasterRepository;
	@Autowired
	ScanIdTypeMasterRepository scanIdTypeMasterRepository;
	@Autowired
	CountryBranchService countryBranchService;
	@Autowired
	MetaData metaData;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	CustomerDocMasterManager customerDocumentManager;

	@Override
	public CustomerDocumentInfo fetchKycImageInfo(CustomerIdProof customerIdProof) {
		CustomerDocumentInfo customerDocumentImage = new CustomerDocumentInfo();
		CountryBranch countryBranch = countryBranchService.getCountryBranchByCountryBranchId(metaData.getCountryBranchId());
		StringBuffer urlBuffer = new StringBuffer();
		List<ArcmateScanMaster> arcmateList = arcmateScanMasterRepository.findByModeOfOperationAndScanTypeAndIsActive(ConstantDocument.VIEW,
				ConstantDocument.BOTH_VIEW, ConstantDocument.Yes);
		List<ScanIdTypeMaster> scanIdList = scanIdTypeMasterRepository.findByidTypeIdAndScanIndAndIsActive(customerIdProof.getIdentityTypeId(),
				countryBranch.getScanInd(), ConstantDocument.Yes);
		
		customerDocumentImage.setUploadedDate(customerIdProof.getCreationDate());
		CustomerDocumentTypeMaster kycDocTypeMaster = customerDocumentManager.getKycDocTypeMaster(customerIdProof.getIdentityTypeId());
		customerDocumentImage.setDocumentCategory(kycDocTypeMaster.getDocumentCategory());
		customerDocumentImage.setDocumentType(kycDocTypeMaster.getDocumentType());
		customerDocumentImage.setDocumentRenderType(DocumentImageRenderType.URL);
		if (arcmateList.size() != 0 && scanIdList.size() != 0) {
			ArcmateScanMaster arcmateValue = arcmateList.get(0);
			ScanIdTypeMaster scanIdValue = scanIdList.get(0);
			String rootContext = "http://";
			urlBuffer.append(rootContext).append(arcmateValue.getIpAddress().trim()).append("/").append(arcmateValue.getContextPath().trim());
			if (arcmateValue.getUrlParamField1() != null) {
				urlBuffer.append(arcmateValue.getUrlParamField1().trim());
			}
			if (arcmateValue.getUrlParamField2() != null) {
				urlBuffer.append(arcmateValue.getFieldSeprator().trim()).append(arcmateValue.getUrlParamField2().trim());
			}
			if (arcmateValue.getUrlParamField3() != null && scanIdValue.getDocumentId() != null) {
				urlBuffer.append(arcmateValue.getFieldSeprator().trim()).append(arcmateValue.getUrlParamField3().trim())
						.append(arcmateValue.getFieldAssigner().trim()).append(scanIdValue.getDocumentId());
			}

			if (arcmateValue.getUrlParamField4() != null && scanIdValue.getFolderId() != null) {
				urlBuffer.append(arcmateValue.getFieldSeprator().trim()).append(arcmateValue.getUrlParamField4().trim())
						.append(arcmateValue.getFieldAssigner().trim()).append(scanIdValue.getFolderId());
			}

			if (arcmateValue.getUrlParamField5() != null && scanIdValue.getUserName() != null) {
				urlBuffer.append(arcmateValue.getFieldSeprator().trim()).append(arcmateValue.getUrlParamField5().trim())
						.append(arcmateValue.getFieldAssigner().trim()).append(scanIdValue.getUserName());
			}
			if (arcmateValue.getUrlParamField6() != null && scanIdValue.getPassword() != null) {
				urlBuffer.append(arcmateValue.getFieldSeprator().trim()).append(arcmateValue.getUrlParamField6().trim())
						.append(arcmateValue.getFieldAssigner().trim()).append(scanIdValue.getPassword());
			}

			if (arcmateValue.getUrlParamField7() != null && scanIdValue.getFileCategoryId() != null) {
				urlBuffer.append(arcmateValue.getFieldSeprator().trim()).append(arcmateValue.getUrlParamField7().trim())
						.append(arcmateValue.getFieldAssigner().trim()).append(scanIdValue.getFileCategoryId());
			}

			if (arcmateValue.getUrlParamField8() != null) {
				urlBuffer.append(arcmateValue.getFieldSeprator().trim()).append(arcmateValue.getUrlParamField8().trim())
						.append(arcmateValue.getFieldAssigner().trim()).append(customerIdProof.getIdentityInt());
			}

			if (arcmateValue.getUrlParamField9() != null) {
				urlBuffer.append(arcmateValue.getFieldSeprator().trim()).append(arcmateValue.getUrlParamField9().trim())
						.append(arcmateValue.getFieldAssigner().trim()).append(scanIdValue.getIdTypeValue());
			}
			customerDocumentImage.setDocumentUrl(urlBuffer.toString());
			log.debug("SCANNED VIEW URL :  " + urlBuffer.toString());

		}
		return customerDocumentImage;
	}

	@Override
	public CustomerDocumentInfo getDocumentInfo(CustomerDocumentUploadReference upload) {
		// TODO Auto-generated method stub
		return null;
	}

}
