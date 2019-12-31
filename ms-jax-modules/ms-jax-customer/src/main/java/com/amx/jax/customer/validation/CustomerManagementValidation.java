package com.amx.jax.customer.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.document.manager.CustomerDocMasterManager;
import com.amx.jax.customer.document.manager.CustomerDocumentUploadManager;
import com.amx.jax.customer.manager.OffsiteCustomerRegManager;
import com.amx.jax.customer.service.OffsitCustRegService;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.customer.CustomerDocumentCategory;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.dbmodel.remittance.IDNumberLengthCheckView;
import com.amx.jax.dict.Tenant;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.document.CustomerDocValidationResponseData;
import com.amx.jax.model.request.CustomerEmploymentDetails;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.UpdateCustomerPersonalDetailRequest;
import com.amx.jax.model.request.customer.CustomerDocValidationData;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.repository.remittance.IIdNumberLengthCheckRepository;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.DateUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerManagementValidation {

	@Autowired
	IIdNumberLengthCheckRepository idnumberLengthCheckRepos;
	@Autowired
	CustomerDocumentUploadManager customerDocumentUploadManager;
	@Autowired
	CustomerDocMasterManager customerDocMasterManager;
	@Autowired
	UserService userService;
	@Autowired
	MetaData metaData;
	@Autowired
	OffsiteCustomerRegManager offsiteCustomerRegManager;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	OffsitCustRegService offsitCustRegService;

	public static final int INSURANCE_ELIGILIBITY_AGE = 69;

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
					"Id length setup is missing  in parameter :" + identityInt + " identityTypeId :" + identityTypeId);
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
		String identityInt = data.getIdentityInt();
		BigDecimal identityTypeId = data.getIdentityTypeId();
		Customer customer = null;
		if (metaData.getCustomerId() != null) {
			customer = userService.getCustById(metaData.getCustomerId());
			identityInt = customer.getIdentityInt();
			identityTypeId = customer.getIdentityTypeId();
		}
		List<CustomerDocumentUploadReferenceTemp> customerUploads = customerDocumentUploadManager.getCustomerUploads(identityInt, identityTypeId);
		List<CustomerDocValidationResponseData> missingDocData = new ArrayList<>();
		CustomerDocumentTypeMaster customerDocumentTypeMaster = customerDocMasterManager.getKycDocTypeMaster(identityTypeId);
		String kycDocCategory = customerDocumentTypeMaster.getDocumentCategory();
		boolean isKycDone = true;
		if (customer != null) {
			CustomerIdProof idProof = customerIdProofManager.getCustomerIdProofByCustomerId(customer.getCustomerId());
			if (idProof == null) {
				isKycDone = false;
			}
		}

		// addres proof for bhr
		if (data.isLocalAddressChange() && Tenant.BHR.equals(TenantContextHolder.currentSite())) {
			Optional<CustomerDocumentUploadReferenceTemp> uploadedProof = customerUploads.stream()
					.filter(i -> CustomerDocumentCategory.ADDRESS_PROOF.name().equals(i.getCustomerDocumentTypeMaster().getDocumentCategory()))
					.findFirst();
			if (!uploadedProof.isPresent()) {
				missingDocData.add(new CustomerDocValidationResponseData(CustomerDocumentCategory.ADDRESS_PROOF.name()));
			}
		}
		// create customer request
		if (data.isCreateCustomerRequest() || !isKycDone) {
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

	public void validateCustomerDataForUpdate(UpdateCustomerInfoRequest updateCustomerInfoRequest, BigDecimal customerId) {
		Customer customer = userService.getCustById(customerId);
		boolean updateSignature = false;
		if (updateCustomerInfoRequest.getPersonalDetailInfo() != null
				&& updateCustomerInfoRequest.getPersonalDetailInfo().getCustomerSignature() != null) {
			updateSignature = true;
		}
		if (customer.getSignatureSpecimenClob() == null && !updateSignature) {
			throw new GlobalException(JaxError.SIGNATURE_NOT_AVAILABLE, "Customer Signature Missing");
		}
	}

	public void validateCustomerDataForCreate(CreateCustomerInfoRequest createCustomerInfoRequest) {
		if (createCustomerInfoRequest.getIdentityTypeId() == null) {
			throw new GlobalException("identity type id must be present");
		}
		Customer customer = offsiteCustomerRegManager.getCustomerForRegistration(createCustomerInfoRequest.getIdentityInt(),
				createCustomerInfoRequest.getIdentityTypeId());
		if (customer != null) {
			throw new GlobalException("Customer already created, use update api to update customer info");
		}
		validateInsuranceFlag(createCustomerInfoRequest);
		offsitCustRegService.validateCustomerBlackList(createCustomerInfoRequest.getCustomerPersonalDetail());
		validateEmploymentInfo(createCustomerInfoRequest.getCustomerEmploymentDetails());
	}

	private void validateEmploymentInfo(CustomerEmploymentDetails customerEmploymentDetails) {
		if (customerEmploymentDetails.getEmploymentTypeId() == null) {
			throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Employment type can not be empty");
		}
		// optional fields employer detail for 222 = unemployed type
		if (!(customerEmploymentDetails.getEmploymentTypeId().compareTo(new BigDecimal(222)) == 0)) {
			if (customerEmploymentDetails.getEmployer() == null) {
				throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Employer name can not be empty");
			}
			if (customerEmploymentDetails.getProfessionId() == null) {
				throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Profession can not be empty");
			}
			if (customerEmploymentDetails.getArticleDetailsId() == null) {
				throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Article detail can not be empty");
			}
			if (customerEmploymentDetails.getIncomeRangeId() == null) {
				throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Income range can not be empty");
			}
		}
	}

	private void validateInsuranceFlag(CreateCustomerInfoRequest createCustomerInfoRequest) {
		CustomerPersonalDetail personalDetail = createCustomerInfoRequest.getCustomerPersonalDetail();
		if (personalDetail.getDateOfBirth() != null && ConstantDocument.Yes.equals(personalDetail.getInsurance())) {
			int age = DateUtil.calculateAge(personalDetail.getDateOfBirth());
			if (age > INSURANCE_ELIGILIBITY_AGE) {
				throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE,
						"Customer is not eligible for insurance. Please remove insurance indic.");
			}
		}
	}

	public void validateInsuranceFlag(UpdateCustomerInfoRequest updateCustomerInfoRequest) {
		Customer customer = userService.getCustById(metaData.getCustomerId());
		Date dob = customer.getDateOfBirth();
		if (updateCustomerInfoRequest.getPersonalDetailInfo() != null) {
			UpdateCustomerPersonalDetailRequest personalDetailInfo = updateCustomerInfoRequest.getPersonalDetailInfo();
			if (personalDetailInfo.getDateOfBirth() != null) {
				dob = personalDetailInfo.getDateOfBirth();
			}
			if (dob == null) {
				throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE,
						"In order to update insurance flag, please update date of birth of customer");
			}
			Boolean insuranceIndic = personalDetailInfo.getInsurance();
			if (insuranceIndic != null && insuranceIndic) {
				int age = DateUtil.calculateAge(dob);
				if (age > INSURANCE_ELIGILIBITY_AGE) {
					throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE,
							"Customer is not eligible for insurance. Please remove insurance indic.");
				}
			}
		}
	}
}
