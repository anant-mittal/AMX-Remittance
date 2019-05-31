package com.amx.jax.customer.manager;

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

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.customer.service.CustomerService;
import com.amx.jax.customer.service.OffsitCustRegService;
import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.FieldListDao;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.AddressProofModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.DocBlobUpload;
import com.amx.jax.dbmodel.DocumentUploadReference;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.model.response.customer.AddressProofDTO;
import com.amx.jax.repository.CountryMasterRepository;
import com.amx.jax.repository.CustomerEmployeeDetailsRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.DOCBLOBRepository;
import com.amx.jax.repository.EmployeeRespository;
import com.amx.jax.repository.EmploymentTypeRepository;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.repository.IDMSAppMappingRepository;
import com.amx.jax.repository.IDocumentUploadMapRepository;
import com.amx.jax.repository.IUserFinancialYearRepo;
import com.amx.jax.repository.JaxConditionalFieldRuleRepository;
import com.amx.jax.repository.ProfessionRepository;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.service.PrefixService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.AddressProofDao;
import com.amx.jax.userservice.dao.IncomeDao;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
import com.amx.jax.userservice.manager.CustomerRegistrationOtpManager;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.userservice.service.ContactDetailService;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.userservice.validation.CustomerCredentialValidator;
import com.amx.jax.userservice.validation.CustomerPersonalDetailValidator;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.CountryMetaValidation;
@Component
public class OffsiteAddressProofManager {
	
	private static final Logger LOGGER = LoggerService.getLogger(OffsiteAddressProofManager.class);

	@Autowired
	JaxConditionalFieldRuleRepository jaxConditionalFieldRuleRepository;

	@Autowired
	JaxUtil jaxUtil;

	@Autowired
	BizcomponentDao bizcomponentDao;

	@Autowired
	PrefixService prefixService;

	@Autowired
	CustomerRegistrationManager customerRegistrationManager;

	@Autowired
	OtpSettings otpSettings;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	MetaData metaData;

	@Autowired
	ArticleDao articleDao;

	@Autowired
	FieldListDao fieldListDao;

	@Autowired
	AuditService auditService;

	@Autowired
	EmploymentTypeRepository employmentTypeRepo;

	@Autowired
	ProfessionRepository professionRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	CustomerIdProofRepository customerIdProofRepository;

	
	@Autowired
	ContactDetailService contactDetailService;

	@Autowired
	CountryMasterRepository countryMasterRepository;

	@Autowired
	CustomerEmployeeDetailsRepository customerEmployeeDetailsRepository;

	@Autowired
	DOCBLOBRepository docblobRepository;

	@Autowired
	IUserFinancialYearRepo userFinancialYearRepo;

	@Autowired
	ImageCheckDao imageCheckDao;

	@Autowired
	IDMSAppMappingRepository idmsAppMappingRepository;

	@Autowired
	TenantContext<CustomerValidation> tenantContext;

	@Autowired
	CountryMetaValidation countryMetaValidation;

	@Autowired
	UserValidationService userValidationService;

	@Autowired
	BlackListDao blackListDao;

	@Autowired
	CustomerPersonalDetailValidator customerPersonalDetailValidator;

	@Autowired
	CustomerRegistrationOtpManager customerRegistrationOtpManager;

	@Autowired
	CustomerCredentialValidator customerCredentialValidator;

	@Autowired
	CustomerService customerService;

	@Autowired
	IApplicationCountryRepository applicationSetup;

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Autowired
	UserService userService;

	@Autowired
	private CryptoUtil cryptoUtil;
	@Autowired
	OffsiteCustomerRegValidator offsiteCustomerRegValidator;
	@Autowired
	OffsiteCustomerRegManager offsiteCustomerRegManager;
	
	@Autowired
	EmployeeRespository employeeRespository;
	
	@Autowired
	IncomeDao incomeDao;
	
	@Autowired
	AddressProofDao addressProofDao;
	
	@Autowired
	IDocumentUploadMapRepository iDocumentUploadMapRepository;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	
	public AmxApiResponse<AddressProofDTO, Object> getAddressProof(){
		List<AddressProofModel> addressProofModel = addressProofDao.getAddressProof();
		return AmxApiResponse.buildList(convertAddressProofDto(addressProofModel));
	}
	
	public List<AddressProofDTO> convertAddressProofDto(List<AddressProofModel> addressProofModel) {
		List<AddressProofDTO> output = new ArrayList<>();
		for (AddressProofModel addressProofModelIterate : addressProofModel) {
			AddressProofDTO dto = new AddressProofDTO();
			
			dto.setUploadType(AddressProofDTO.UploadType.ADDR);
			dto.setResourceId(addressProofModelIterate.getAddressProofId());
			dto.setResourceName(addressProofModelIterate.getDescription());
			dto.setResourceCode(addressProofModelIterate.getCode());
			output.add(dto);
		}
		return output;
	}
	
	public BoolRespModel saveDocumentUploadReference(ImageSubmissionRequest model) throws ParseException {

		BoolRespModel boolRespModel = new BoolRespModel();
		if (model != null) {
			if (metaData.getCustomerId() == null) {

				throw new GlobalException(JaxError.NULL_CUSTOMER_ID, "Customer Id is not available");
			}
			Customer customer = customerRepository.findOne(metaData.getCustomerId());
			if (customer == null) {

				throw new GlobalException(JaxError.INVALID_CUSTOMER, "Customer is Invalid");
			}
			if (model.getImage() == null) {

				throw new GlobalException(JaxError.IMAGE_NOT_AVAILABLE, "Image is not available");
			}
			if (model.getIdentityExpiredDate() != null) {
				customerIdProofManager.createIdProofForExpiredCivilId(model, customer);
			}

			if (model.getDocumentUploadReferenceId() == null) {
				throw new GlobalException("Document upload reference id not available");
			}

			if (model.getUploadType() == null) {
				throw new GlobalException("Document upload type cannot be null");
			}

			DocumentUploadReference documentUploadReferenceDetails = iDocumentUploadMapRepository
					.getCustById(metaData.getCustomerId());
			if (documentUploadReferenceDetails != null) {
				for (String image : model.getImage()) {
					DmsApplMapping mappingData = new DmsApplMapping();
					mappingData = getDmsApplMappingData(customer, model);
					mappingData.setUploadType(model.getUploadType());
					idmsAppMappingRepository.save(mappingData);
					DocBlobUpload documentDetails = new DocBlobUpload();
					documentDetails = getDocumentUploadDetails(image, mappingData);
					docblobRepository.save(documentDetails);

					LOGGER.debug("document details are " + documentDetails.toString());
					documentUploadReferenceDetails.setUploadType(model.getUploadType());
					documentUploadReferenceDetails.setDocBlobId(mappingData.getDocBlobId());
					documentUploadReferenceDetails.setUploadDoctypeId(model.getDocumentUploadReferenceId());

					documentUploadReferenceDetails.setModifiedBy(metaData.getCustomerId().toString());
					documentUploadReferenceDetails.setModifiedDate(new Date());
					LOGGER.info("Document details for old record are "+documentUploadReferenceDetails.toString());
					iDocumentUploadMapRepository.save(documentUploadReferenceDetails);
					boolRespModel.setSuccess(Boolean.TRUE);
				}
			} else {
				for (String image : model.getImage()) {
					DmsApplMapping mappingData = new DmsApplMapping();
					mappingData = getDmsApplMappingData(customer, model);
					mappingData.setUploadType(model.getUploadType());
					idmsAppMappingRepository.save(mappingData);

					DocBlobUpload documentDetails = new DocBlobUpload();
					documentDetails = getDocumentUploadDetails(image, mappingData);
					docblobRepository.save(documentDetails);

					LOGGER.debug("document details are " + documentDetails.toString());

					DocumentUploadReference documentUploadReference = new DocumentUploadReference();
					documentUploadReference = getDocumentUploadReference(mappingData, model);
					LOGGER.info("Document details for new record are "+documentUploadReference.toString());

					if (documentUploadReference != null) {
						iDocumentUploadMapRepository.save(documentUploadReference);

					}
					boolRespModel.setSuccess(Boolean.TRUE);
				}
			}

		} else {

			throw new GlobalException(JaxError.IMAGE_NOT_AVAILABLE, "Image data is not available");
		}

		return boolRespModel;

	}
	
	
	private DocBlobUpload getDocumentUploadDetails(String image, DmsApplMapping mappingData) {
		DocBlobUpload documentDetails = new DocBlobUpload();
		documentDetails.setCntryCd(mappingData.getApplicationCountryId());
		documentDetails.setDocBlobID(mappingData.getDocBlobId());
		documentDetails.setDocFinYear(mappingData.getFinancialYear());
		documentDetails.setSeqNo(new BigDecimal(1));
		// documentDetails.setDocContent(image.getBytes());

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
		// return null;
	}

	private DmsApplMapping getDmsApplMappingData(Customer model, ImageSubmissionRequest imageSubmissionRequest) throws ParseException {
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
	
	private DocumentUploadReference getDocumentUploadReference(DmsApplMapping dmsApplMapping, ImageSubmissionRequest imageSubmissionRequest) {
		if (imageSubmissionRequest.getDocumentUploadReferenceId() != null) {
			DocumentUploadReference documentUploadReference = new DocumentUploadReference();
			documentUploadReference.setApplicationCountryId(metaData.getCountryId());
			documentUploadReference.setCustomerId(metaData.getCustomerId());
			documentUploadReference.setUploadType(imageSubmissionRequest.getUploadType());
			documentUploadReference.setDocBlobId(dmsApplMapping.getDocBlobId());
			documentUploadReference.setUploadDoctypeId(imageSubmissionRequest.getDocumentUploadReferenceId());
			documentUploadReference.setIsActive("Y");
			documentUploadReference.setCreatedBy(metaData.getCustomerId().toString());
			documentUploadReference.setCreatedDate(new Date());
			documentUploadReference.setModifiedBy(metaData.getCustomerId().toString());
			documentUploadReference.setModifiedDate(new Date());
			return documentUploadReference;
			
		}
		return null;
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
