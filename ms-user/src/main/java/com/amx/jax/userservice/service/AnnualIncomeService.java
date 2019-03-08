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
import com.amx.amxlib.meta.model.IncomeDto;
import com.amx.jax.JaxAuthCache;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerEmploymentInfo;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.DocBlobUpload;
import com.amx.jax.dbmodel.IncomeModel;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CountryRepository;
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
import com.amx.jax.scope.TenantContext;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.dao.IncomeDao;
import com.amx.jax.userservice.repository.CustomerRepository;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;

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
	JaxAuthCache jaxAuthCache;

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

	public AmxApiResponse<IncomeDto, Object> getAnnualIncome(BigDecimal customerId) {
		List<IncomeModel> incomeList = incomeDao.getAnnualIncome(customerId);
		Customer cust = custDao.getCustById(customerId);
		String firstName = cust.getFirstName();
		String middleName = cust.getMiddleName();
		String lastName = cust.getLastName();
		String fullName = firstName + middleName + lastName;
		if (incomeList.isEmpty()) {
			throw new GlobalException("Income list is not available");
		}
		return AmxApiResponse.buildList(convertIncomeDto(incomeList, fullName));
	}

	public List<IncomeDto> convertIncomeDto(List<IncomeModel> incomeList, String fullName) {
		List<IncomeDto> output = new ArrayList<>();
		for (IncomeModel incomeModel : incomeList) {
			IncomeDto dto = new IncomeDto();
			dto.setApplicationCountryId(incomeModel.getApplicationCountryId());
			dto.setIncomeRangeFrom(incomeModel.getIncomeRangeFrom());
			dto.setIncomeRangeTo(incomeModel.getIncomeRangeTo());
			dto.setFullName(fullName);
			output.add(dto);
		}
		return output;
	}

	public AmxApiResponse<IncomeDto, Object> saveAnnualIncome(IncomeDto incomeDto) throws ParseException {
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		logger.debug("Object is : ");
		customer.setAnnualIncomeFrom(incomeDto.getIncomeRangeFrom());
		customer.setAnnualIncomeTo(incomeDto.getIncomeRangeTo());
		customer.setAnnualIncomeUpdatedBy(incomeDto.getFullName());
		customer.setAnnualIncomeUpdatedDate(new Date());
		customer.setFsArticleDetails(articleDao.getArticleDetailsByArticleDetailId(incomeDto.getArticleDetailId()));
		CustomerEmploymentInfo customerEmploymentInfo = incomeDao.getCustById(metaData.getCustomerId());
		customerEmploymentInfo.setEmployerName(incomeDto.getCompanyName());
		if (incomeDto.getImage() == null) {

			throw new GlobalException(JaxError.IMAGE_NOT_AVAILABLE, "Image is not available");
		}
		DmsApplMapping mappingData = new DmsApplMapping();
		mappingData = getDmsApplMappingData(customer);
		idmsAppMappingRepository.save(mappingData);
		DocBlobUpload documentDetails = new DocBlobUpload();
		documentDetails = getDocumentUploadDetails(incomeDto.getImage(), mappingData);
		docblobRepository.save(documentDetails);

		custDao.saveCustomer(customer);
		incomeDao.saveCustomerEmploymentInfo(customerEmploymentInfo);
		return AmxApiResponse.build(incomeDto);

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
		mappingData.setCreatedBy(metaData.getEmployeeId().toString());
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

	public Boolean forceUpdateAnnualIncome() {
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		Date date1 = customer.getAnnualIncomeUpdatedDate();
		if (date1 == null) {
			metaData.setAnnualIncomeForceUpdate(Boolean.TRUE);
		}
		Date date2 = new Date();
		long bdmillisec = date2.getTime() - date1.getTime();
		BigDecimal millisec = new BigDecimal(bdmillisec);
		BigDecimal millisecinyear = new BigDecimal("31540000000");
		if (millisec.compareTo(millisecinyear) == 1 || millisec.compareTo(millisecinyear) == 0) {
			metaData.setAnnualIncomeForceUpdate(Boolean.TRUE);
		} else {
			metaData.setAnnualIncomeForceUpdate(Boolean.FALSE);
		}
		return metaData.getAnnualIncomeForceUpdate();
	}

}
