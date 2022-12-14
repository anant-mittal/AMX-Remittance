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
import java.util.Map;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.AnnualIncomeRangeDTO;
import com.amx.amxlib.meta.model.IncomeDto;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerEmploymentInfo;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.DocBlobUpload;
import com.amx.jax.dbmodel.IncomeModel;
import com.amx.jax.dbmodel.IncomeRangeMaster;
import com.amx.jax.dbmodel.LanguageType;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.annualtransaction.AnnualTransactionFactorModel;
import com.amx.jax.logger.AuditService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.DOCBLOBRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IDMSAppMappingRepository;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.IUserFinancialYearRepo;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.repository.annualtransaction.AnnualTransactionFactorRepository;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.dao.IncomeDao;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.utils.Constants;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class AnnualIncomeService {

	Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private CustomerDao custDao;

	@Autowired
	IViewCityDao cityDao;

	@Autowired
	IViewStateDao stateDao;

	@Autowired
	CountryRepository countryDao;

	@Autowired
	ICustomerRepository customerDao;

	@Autowired
	IContactDetailDao contactDao;

	@Autowired
	IViewDistrictDAO districtDao;

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	ITransactionHistroyDAO tranxHistDao;

	@Autowired
	MetaData metaData;

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Autowired
	CustomerVerificationService customerVerificationService;

	@Autowired
	TenantContext<CustomerValidation> tenantContext;

	@Autowired
	private CustomerRepository repo;

	@Autowired
	AuditService auditService;
	@Autowired
	CustomerIdProofDao customerIdProofDao;

	@Autowired
	IncomeDao incomeDao;

	@Autowired
	ArticleDao articleDao;

	@Autowired
	ImageCheckDao imageCheckDao;

	@Autowired
	DOCBLOBRepository docblobRepository;

	@Autowired
	IDMSAppMappingRepository idmsAppMappingRepository;

	@Autowired
	IUserFinancialYearRepo userFinancialYearRepo;
	
	@Autowired
	AnnualTransactionFactorRepository annualTransactionFactorRepository;

	public AmxApiResponse<AnnualIncomeRangeDTO, Object> getAnnualIncome(BigDecimal customerId) {
		List<IncomeModel> incomeList = incomeDao.getAnnualIncome(customerId);
		
		
		if (incomeList.isEmpty()) {
			throw new GlobalException("Income list is not available");
		}
		return AmxApiResponse.buildList(convertIncomeDto(incomeList));
	}

	public List<AnnualIncomeRangeDTO> convertIncomeDto(List<IncomeModel> incomeList) {
		List<AnnualIncomeRangeDTO> output = new ArrayList<>();
		for (IncomeModel incomeModel : incomeList) {
			AnnualIncomeRangeDTO dto = new AnnualIncomeRangeDTO();
			dto.setIncomeRangeFrom(incomeModel.getIncomeRangeFrom());
			dto.setIncomeRangeTo(incomeModel.getIncomeRangeTo());
			dto.setResourceId(incomeModel.getIncomeRangeMasterId());
			output.add(dto);
		}
		return output;
	}

	public AmxApiResponse<IncomeDto, Object> saveAnnualIncome(IncomeDto incomeDto) throws ParseException {
		logger.info("Income Dto value is "+incomeDto.getArticleDetailId());
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		logger.info("fetch customer by customer id "+customer);
		

		

		customer.setAnnualIncomeUpdatedBy(metaData.getCustomerId().toString());
		logger.info("set annual income updated by "+customer);

		customer.setAnnualIncomeUpdatedDate(new Date());
		logger.info("set annual income updated date "+customer);

		if (incomeDto.getArticleDetailId() == null) {
			throw new GlobalException("Article detail id can not be null");
		}
		logger.info("ArtDetaId is "+incomeDto.getArticleDetailId());
		List<Map<String, Object>> designationList = articleDao
				.getArticleDescriptionByArticleDetailId(incomeDto.getArticleDetailId(), metaData.getLanguageId(), metaData.getCustomerId());
		logger.info("set designation list  "+designationList);
		
		if (designationList.isEmpty()) {
			throw new GlobalException("Invalid designation id entered");
		}

		customer.setFsArticleDetails(articleDao.getArticleDetailsByArticleDetailId(incomeDto.getArticleDetailId()));
		logger.info("set designation id : ");
		
		logger.info("Constant is "+Constants.ANNUALINCOME_VERIFIED_LIMIT);
		
		CustomerEmploymentInfo customerEmploymentInfo = incomeDao.getCustById(metaData.getCustomerId());
		
		List<CustomerEmploymentInfo> custEmploymentInfo = incomeDao.getAllCustById(metaData.getCustomerId());
		
		if(custEmploymentInfo.isEmpty()) {
			CustomerEmploymentInfo custEmplInfo = new CustomerEmploymentInfo();
			custEmplInfo.setFsBizComponentDataByEmploymentTypeId(new BizComponentData(new BigDecimal(187)));
			custEmplInfo.setFsDistrictMaster(new DistrictMaster(new BigDecimal(4165)));
			custEmplInfo.setFsStateMaster(new StateMaster(new BigDecimal(584)));
			custEmploymentInfo.add(custEmplInfo);
		}
		else {
			CustomerEmploymentInfo custEmplInfo = custEmploymentInfo.get(0);
			if(custEmplInfo.getFsBizComponentDataByEmploymentTypeId()==null) {
				custEmplInfo.setFsBizComponentDataByEmploymentTypeId(new BizComponentData(new BigDecimal(187)));
			}
			if(custEmplInfo.getFsDistrictMaster()==null) {
				custEmplInfo.setFsDistrictMaster(new DistrictMaster(new BigDecimal(4165)));
			}
			if(custEmplInfo.getFsStateMaster()==null) {
				custEmplInfo.setFsStateMaster(new StateMaster(new BigDecimal(584)));
			}
		}
		
		logger.info("set customerEmpInfo : " +customerEmploymentInfo);
		if (customerEmploymentInfo == null) {
			customerEmploymentInfo = createCustomerEmploymentInfo(incomeDto,custEmploymentInfo.get(0));
			
		}
		else {
			if(customerEmploymentInfo.getFsBizComponentDataByEmploymentTypeId()==null) {
				customerEmploymentInfo.setFsBizComponentDataByEmploymentTypeId(new BizComponentData(new BigDecimal(187)));
			}
			if(customerEmploymentInfo.getFsDistrictMaster()==null) {
				customerEmploymentInfo.setFsDistrictMaster(new DistrictMaster(new BigDecimal(4165)));
			}
			if(customerEmploymentInfo.getFsStateMaster()==null) {
				customerEmploymentInfo.setFsStateMaster(new StateMaster(new BigDecimal(584)));
			}
			customerEmploymentInfo.setUpdatedBy(metaData.getCustomerId().toString());
			customerEmploymentInfo.setLastUpdated(new Date());
		}
		if (incomeDto.getCompanyName() == null || incomeDto.getCompanyName() == "") {
			throw new GlobalException("Company name cannot be null or empty");
		}
		customerEmploymentInfo.setEmployerName(incomeDto.getCompanyName());
		logger.info("employee name is set:" + customerEmploymentInfo.getEmployerName());

		if (!StringUtils.isEmpty(incomeDto.getImage())&& !StringUtils.isEmpty(incomeDto.getFileName())) {
			logger.info("image is set 1");
			DmsApplMapping mappingData = new DmsApplMapping();

			mappingData = getDmsApplMappingData(customer);
			
			idmsAppMappingRepository.save(mappingData);
			
			DocBlobUpload documentDetails = new DocBlobUpload();
			if (incomeDto.getImage().length() > Constants.IMAGE_SIZE) {
				throw new GlobalException("Image size should be less than 1MB");
			}

			documentDetails = getDocumentUploadDetails(incomeDto.getImage(), mappingData);
			
			docblobRepository.save(documentDetails);
			
			customerEmploymentInfo.setDocBlobId(mappingData.getDocBlobId());
			

			if (getFileExtension(incomeDto.getFileName()) == "") {
				throw new GlobalException("Filename cannot be entered without extension");
			}

			if (getFileExtension(incomeDto.getFileName()) == "") {
				throw new GlobalException("Filename cannot be entered without extension");
			}
			String fileExtension = getFileExtension(incomeDto.getFileName());
			if (!(fileExtension.equalsIgnoreCase("pdf") || fileExtension.equalsIgnoreCase("jpg")
					|| fileExtension.equalsIgnoreCase("png"))) {
				throw new GlobalException("Filename extension can only be pdf,jpg and png");
			}

			customerEmploymentInfo.setFileName(incomeDto.getFileName());
			logger.info("file is set 1");
		} else if (StringUtils.isEmpty(incomeDto.getImage()) && StringUtils.isEmpty(incomeDto.getFileName())) {
			logger.info("income and filename is set");
			customerEmploymentInfo.setDocBlobId(null);
			logger.info("blob id is set");
			customerEmploymentInfo.setFileName(null);
			logger.info("filename is set");
		} else if (!StringUtils.isEmpty(incomeDto.getFileName()) && StringUtils.isEmpty(incomeDto.getImage())) {
			logger.info("Editing card details after initial upload");
		}
		
		else {
			throw new GlobalException("If you have uploaded image also enter filename");
		}

		logger.info("details are set");
		custDao.saveCustomer(customer);
		
		
		incomeDao.saveCustomerEmploymentInfo(customerEmploymentInfo);
		
		return AmxApiResponse.build(incomeDto);

	}

	private CustomerEmploymentInfo createCustomerEmploymentInfo(IncomeDto incomeDto, CustomerEmploymentInfo customerEmploymentInfo) {
		// TODO Auto-generated method stub
		CustomerEmploymentInfo custEmploymentInfo = new CustomerEmploymentInfo();
		custEmploymentInfo.setEmployerName(incomeDto.getCompanyName());
		custEmploymentInfo.setCreatedBy(metaData.getCustomerId().toString());
		custEmploymentInfo.setCreationDate(new Date());
		custEmploymentInfo.setFsCountryMaster(new CountryMaster(metaData.getCountryId()));
		custEmploymentInfo.setFsCompanyMaster(new CompanyMaster(metaData.getCompanyId()));
		custEmploymentInfo.setFsLanguageType(new LanguageType(metaData.getLanguageId()));
		custEmploymentInfo.setIsActive("Y");
		custEmploymentInfo.setFsCustomer(new Customer(metaData.getCustomerId()));
		custEmploymentInfo.setCreatedBy(metaData.getCustomerId().toString());
		custEmploymentInfo.setCreationDate(new Date());
		custEmploymentInfo.setFsBizComponentDataByEmploymentTypeId(customerEmploymentInfo.getFsBizComponentDataByEmploymentTypeId());
		custEmploymentInfo.setFsStateMaster(customerEmploymentInfo.getFsStateMaster());
		custEmploymentInfo.setFsDistrictMaster(customerEmploymentInfo.getFsDistrictMaster());
		
		return custEmploymentInfo;
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
	
	public static String getFileExtension(String fileName) {
        
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

	
	
	public AmxApiResponse<IncomeDto, Object> getAnnualIncomeDetails() {
		IncomeDto incomeDto = new IncomeDto();
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		CustomerEmploymentInfo customerEmploymentInfo = incomeDao.getCustById(metaData.getCustomerId());

		if (customer.getAnnualIncomeFrom()== null || customer.getAnnualIncomeTo()== null) {
			IncomeRangeMaster articleDetails = customer.getFsIncomeRangeMaster();
			incomeDto.setIncomeRangeFrom(articleDetails.getIncomeRangeFrom());
			incomeDto.setIncomeRangeTo(articleDetails.getIncomeRangeTo());
			incomeDto.setArticleDetailId(customer.getFsArticleDetails().getArticleDetailId());
			incomeDto.setCompanyName(customerEmploymentInfo.getEmployerName());
			incomeDto.setFileName(customerEmploymentInfo.getFileName());
		} else {

			if (customer.getAnnualIncomeFrom() == null || customer.getAnnualIncomeTo() == null) {
				throw new GlobalException("Please set Annual Income range");
			}
			if (customer.getFsArticleDetails().getArticleDetailId() == null) {
				throw new GlobalException("Please set your designation");
			}

			
			incomeDto.setIncomeRangeFrom(customer.getAnnualIncomeFrom());
			incomeDto.setIncomeRangeTo(customer.getAnnualIncomeTo());

			incomeDto.setArticleDetailId(customer.getFsArticleDetails().getArticleDetailId());
			if(customerEmploymentInfo!=null && customerEmploymentInfo.getEmployerName()!=null && customerEmploymentInfo.getFileName()!=null) {
				incomeDto.setCompanyName(customerEmploymentInfo.getEmployerName());
				incomeDto.setFileName(customerEmploymentInfo.getFileName());
			}
			
		}
		logger.info("Article detailed id returned is "+incomeDto.getArticleDetailId());
		return AmxApiResponse.build(incomeDto);

	}
	
	public List<AnnualIncomeRangeDTO> getAnnualTransactionLimitRange() {
		List<IncomeModel> incomeList = incomeDao.getAnnualTransactionLimitRange();
		
		
		if (incomeList.isEmpty()) {
			throw new GlobalException("Annual transaction list is not available");
		}
		List<AnnualIncomeRangeDTO> transactionLimitRange = convertIncomeDto(incomeList);
		return transactionLimitRange;
	}
	public BoolRespModel saveAnnualTransactionLimit(IncomeDto incomeDto) {
		BoolRespModel boolRespModel = new BoolRespModel();
		if(incomeDto.getIncomeRangeFrom()==null||incomeDto.getIncomeRangeTo()==null) {
			throw new GlobalException("Income range should not be null");
		}
		
		List<IncomeModel> incomeModel = incomeDao.getAnnualIncomeRangeId(incomeDto.getIncomeRangeFrom(),
				incomeDto.getIncomeRangeTo());
		
		int c = 0;
		for (int i = 0; i < incomeModel.size(); i++) {
			if (!incomeModel.get(i).getRangeType().equalsIgnoreCase(ConstantDocument.ANNUAL_TRANSACTION_LIMIT)) {
				c++;

			}
		}
		if (c == incomeModel.size()) {
			throw new GlobalException("Invalid income range entered.Please enter a valid income range");
		}
		
		
		
		Iterable<AnnualTransactionFactorModel> annualTransactionFactorModelList=annualTransactionFactorRepository.findAll();
		AnnualTransactionFactorModel annualTransactionFactorModel = annualTransactionFactorModelList.iterator().next();
		BigDecimal annualTransactionModelFactor = annualTransactionFactorModel.getFactor();
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		customer.setAnnualTransactionLimitFrom(incomeDto.getIncomeRangeFrom());
		customer.setAnnualTransactionLimitTo(incomeDto.getIncomeRangeTo());
		customer.setAnnualTransactionUpdatedDate(new Date());
		customer.setAnnualIncomeFrom(incomeDto.getIncomeRangeFrom().multiply(annualTransactionModelFactor));
		customer.setAnnualIncomeTo(incomeDto.getIncomeRangeTo().multiply(annualTransactionModelFactor));
		
		
		
		repo.save(customer);
		
		boolRespModel.setSuccess(Boolean.TRUE);
		return boolRespModel;
	}
	
	public AnnualIncomeRangeDTO getAnnualTransactionLimit() {
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		AnnualIncomeRangeDTO annualIncomeRangeDTO = new AnnualIncomeRangeDTO();
		annualIncomeRangeDTO.setIncomeRangeFrom(customer.getAnnualTransactionLimitFrom());
		annualIncomeRangeDTO.setIncomeRangeTo(customer.getAnnualTransactionLimitTo());
		return annualIncomeRangeDTO;
	}
	
}
