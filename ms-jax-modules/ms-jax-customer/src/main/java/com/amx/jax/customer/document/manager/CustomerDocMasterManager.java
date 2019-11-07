package com.amx.jax.customer.document.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.IdentityTypeMaster;
import com.amx.jax.dbmodel.JaxField;
import com.amx.jax.dbmodel.customer.CustomerDocumentCategory;
import com.amx.jax.dbmodel.customer.CustomerDocumentType;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeDesc;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.model.customer.document.CustomerDocCatTypeDto;
import com.amx.jax.model.customer.document.CustomerDocumentCategoryDto;
import com.amx.jax.model.customer.document.CustomerDocumentTypeDto;
import com.amx.jax.repository.customer.CustomerDocumentTypeDescRepo;
import com.amx.jax.repository.customer.CustomerDocumentTypeMasterRepo;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.jax.util.JaxUtil;
import com.amx.libjax.model.jaxfield.JaxFieldDto;
import com.amx.libjax.model.jaxfield.ValidationRegexDto;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerDocMasterManager {

	@Autowired
	CustomerDocumentTypeMasterRepo customerDocumentTypeMasterRepo;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	JaxUtil jaxUtil;
	@Autowired
	CustomerDocumentTypeDescRepo customerDocumentTypeDescRepo;

	public CustomerDocumentTypeMaster getDocTypeMaster(String docCategory, String docType) {
		return customerDocumentTypeMasterRepo.findByDocumentCategoryAndDocumentType(docCategory, docType);
	}

	/**
	 * @param bizComponentId
	 *            - biz component id for that identity type
	 * @return returns document type master object
	 */
	public CustomerDocumentTypeMaster getKycDocTypeMaster(BigDecimal identityTypeId) {
		IdentityTypeMaster identityTypeMaster = customerIdProofManager.getIdentityTypeMaster(identityTypeId, ConstantDocument.Yes);
		String identityType = identityTypeMaster.getIdentityType();
		CustomerDocumentType customerDocType = CustomerDocumentType.getCustomerDocTypeByIdentityType(identityType);
		return customerDocumentTypeMasterRepo.findByDocumentCategoryAndDocumentType(CustomerDocumentCategory.KYC_PROOF.toString(),
				customerDocType.toString());
	}

	public List<CustomerDocumentCategoryDto> getDocumentCategory() {
		return customerDocumentTypeMasterRepo.getDistinctDocumentCategory().stream().filter(i -> {
			if (i.equals("KYC_PROOF")) {
				return false;
			}
			return true;
		}).map(i -> {
			return new CustomerDocumentCategoryDto(i);
		}).collect(Collectors.toList());
	}

	public List<CustomerDocumentTypeDto> getDocumentType(String documentCategory) {
		return customerDocumentTypeMasterRepo.findByDocumentCategory(documentCategory).stream().map(i -> {
			CustomerDocumentTypeDesc docTypeDesc = customerDocumentTypeDescRepo.findByDocumentType(i.getDocumentType());
			CustomerDocumentTypeDto dto = new CustomerDocumentTypeDto(i.getDocumentType());
			if (docTypeDesc != null) {
				dto.setDocumentTypeDesc(docTypeDesc.getDescription());
			}
			return dto;
		}).collect(Collectors.toList());
	}

	public List<JaxFieldDto> getDocumentFields(String documentCategory, String documentType) {
		CustomerDocumentTypeMaster docTypeMaster = getDocTypeMaster(documentCategory, documentType);
		List<JaxFieldDto> jaxFields = new ArrayList<>();
		jaxFields = docTypeMaster.getJaxFields().stream().map(i -> {
			JaxFieldDto jaxField = convert(i);
			jaxField.setDtoPath("data." + i.getName());
			return jaxField;
		}).collect(Collectors.toList());
		return jaxFields;
	}

	private JaxFieldDto convert(JaxField field) {
		JaxFieldDto dto = new JaxFieldDto();
		jaxUtil.convert(field, dto);
		dto.setRequired(ConstantDocument.Yes.equals(field.getRequired()) ? true : false);
		List<ValidationRegexDto> validationdtos = new ArrayList<>();
		if (field.getValidationRegex() != null) {
			field.getValidationRegex().forEach(validation -> {
				ValidationRegexDto regexdto = new ValidationRegexDto();
				jaxUtil.convert(validation, regexdto);
				validationdtos.add(regexdto);
			});
		}
		dto.setValidationRegex(validationdtos);
		return dto;
	}

	public boolean hasKycDocTypeMaster(List<CustomerDocumentUploadReferenceTemp> docTypeMasters, BigDecimal identityTypeId) {
		CustomerDocumentTypeMaster kycDocTypeMaster = getKycDocTypeMaster(identityTypeId);
		return docTypeMasters.stream().anyMatch(i -> i.getCustomerDocumentTypeMaster().getId().equals(kycDocTypeMaster.getId()));
	}

	public Optional<CustomerDocumentUploadReferenceTemp> getKycDocUpload(List<CustomerDocumentUploadReferenceTemp> customerTempUploads,
			BigDecimal identityTypeId) {
		CustomerDocumentTypeMaster kycDocTypeMaster = getKycDocTypeMaster(identityTypeId);
		Optional<CustomerDocumentUploadReferenceTemp> kycUpload = customerTempUploads.stream()
				.filter(i -> i.getCustomerDocumentTypeMaster().getId().equals(kycDocTypeMaster.getId())).findFirst();
		return kycUpload;
	}

	public String getDocumentTypeDesc(String docType) {
		CustomerDocumentTypeDesc docTypeDesc = customerDocumentTypeDescRepo.findByDocumentType(docType);
		String desc = null;
		if (docTypeDesc != null) {
			desc = docTypeDesc.getDescription();
		}
		return desc;
	}

	public List<CustomerDocCatTypeDto> listDocCatType() {
		List<CustomerDocCatTypeDto> list = new ArrayList<>();
		List<CustomerDocumentCategoryDto> docCategories = getDocumentCategory();

		for (CustomerDocumentCategoryDto documentCategory : docCategories) {
			List<CustomerDocumentTypeDto> documentTypes = getDocumentType(documentCategory.getDocumentCategory());
			list.add(new CustomerDocCatTypeDto(documentCategory, documentTypes));
		}
		return list;
	}
}
