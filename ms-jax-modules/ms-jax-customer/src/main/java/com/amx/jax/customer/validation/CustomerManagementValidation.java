package com.amx.jax.customer.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.customer.document.manager.CustomerDocMasterManager;
import com.amx.jax.customer.document.manager.CustomerDocumentUploadManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.customer.CustomerDocumentCategory;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.dbmodel.remittance.IDNumberLengthCheckView;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.customer.document.CustomerDocValidationResponseData;
import com.amx.jax.model.request.customer.CustomerDocValidationData;
import com.amx.jax.repository.remittance.IIdNumberLengthCheckRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerManagementValidation {

	@Autowired
	IIdNumberLengthCheckRepository idnumberLengthCheckRepos;
	@Autowired
	CustomerDocumentUploadManager customerDocumentUploadManager;
	@Autowired
	CustomerDocMasterManager customerDocMasterManager;

	public void validateIdentityIntLength(String identityInt, BigDecimal identityTypeId) {

		IDNumberLengthCheckView idnumberLengthCheck = idnumberLengthCheckRepos.findByIDTypeId(identityTypeId);
		if (idnumberLengthCheck != null) {
			BigDecimal maxLength = idnumberLengthCheck.getiDLength();
			BigDecimal minimumLength = idnumberLengthCheck.getMinIDLength();
			BigDecimal identityIntLength = identityInt == null ? BigDecimal.ZERO : new BigDecimal(identityInt.length());
			if (identityIntLength.compareTo(minimumLength) < 0 || identityIntLength.compareTo(maxLength) > 0) {
				throw new GlobalException(JaxError.ID_TYPE_LENGTH_NOT_DEFINED,
						"The minimum length should be:" + minimumLength + " and maximum should be :" + maxLength);
			}
		} else {
			throw new GlobalException(JaxError.ID_TYPE_LENGTH_NOT_DEFINED,
					"Id length setup is missing  in paramter :" + identityInt + " identityTypeId :" + identityTypeId);
		}
	}

	public void validateCustomerRecord(Customer customer) {

		if (customer.getSignatureSpecimenClob() == null) {
			throw new GlobalException(JaxError.CUSTOMER_SIGNATURE_UNAVAILABLE, "customer signature unavailable");
		}

		boolean insuranceCheck = ("Y".equals(customer.getMedicalInsuranceInd()) || "N".equals(customer.getMedicalInsuranceInd()));
		if (!insuranceCheck) {
			throw new GlobalException(JaxError.INVALID_INSURANCE_INDICATOR, "invalid insurance indicator");
		}
	}

	/**
	 * Used to validate doc upload data by customer
	 */
	public void validateDocumentsData(CustomerDocValidationData data) {
		List<CustomerDocumentUploadReferenceTemp> customerUploads = customerDocumentUploadManager.getCustomerUploads(data.getIdentityInt(),
				data.getIdentityTypeId());
		List<CustomerDocValidationResponseData> missingDocData = new ArrayList<>();
		CustomerDocumentTypeMaster customerDocumentTypeMaster = customerDocMasterManager.getKycDocTypeMaster(data.getIdentityTypeId());
		String kycDocCategory = customerDocumentTypeMaster.getDocumentCategory();
		// article detail
		if (data.getArticleDetailsId() != null) {
			Optional<CustomerDocumentUploadReferenceTemp> uploadedProof = customerUploads.stream()
					.filter(i -> kycDocCategory.equals(i.getCustomerDocumentTypeMaster().getDocumentCategory())).findFirst();
			if (!uploadedProof.isPresent()) {
				missingDocData.add(new CustomerDocValidationResponseData(kycDocCategory));
			}
		}

		// employer change
		if (data.getEmployer() != null) {
			Optional<CustomerDocumentUploadReferenceTemp> uploadedProof = customerUploads.stream()
					.filter(i -> CustomerDocumentCategory.EMPLOYMENT_PROOF.name().equals(i.getCustomerDocumentTypeMaster().getDocumentCategory()))
					.findFirst();
			if (!uploadedProof.isPresent()) {
				missingDocData.add(new CustomerDocValidationResponseData(CustomerDocumentCategory.EMPLOYMENT_PROOF.name()));
			}
		}

		// income range
		if (data.getIncomeRangeId() != null) {
			Optional<CustomerDocumentUploadReferenceTemp> uploadedProof = customerUploads.stream()
					.filter(i -> CustomerDocumentCategory.INCOME_PROOF.name().equals(i.getCustomerDocumentTypeMaster().getDocumentCategory()))
					.findFirst();
			if (!uploadedProof.isPresent()) {
				missingDocData.add(new CustomerDocValidationResponseData(CustomerDocumentCategory.INCOME_PROOF.name()));
			}
		}
		// create customer request
		if (data.isCreateCustomerRequest()) {
			Optional<CustomerDocumentUploadReferenceTemp> uploadedProof = customerUploads.stream()
					.filter(i -> kycDocCategory.equals(i.getCustomerDocumentTypeMaster().getDocumentCategory())).findFirst();
			if (!uploadedProof.isPresent()) {
				missingDocData.add(new CustomerDocValidationResponseData(kycDocCategory));
			}
		}
		if (!missingDocData.isEmpty()) {
			GlobalException ex = new GlobalException("some documents are missing to upload");
			ex.setMeta(missingDocData);
			throw ex;
		}
	}
}
