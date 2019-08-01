package com.amx.jax.customer.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.response.customer.CustomerContactDto;
import com.amx.jax.model.response.customer.CustomerDto;
import com.amx.jax.model.response.customer.CustomerIdProofDto;
import com.amx.jax.model.response.customer.CustomerIncomeRangeDto;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.service.CountryService;
import com.amx.jax.services.AbstractService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.manager.OnlineCustomerManager;
import com.amx.jax.userservice.service.UserService;

@Service
public class CustomerService extends AbstractService {

	@Autowired
	ICustomerRepository customerRepository;
	@Autowired
	IContactDetailDao contactDetailRepository;
	@Autowired
	CustomerIdProofDao customerIdProofDao;
	@Autowired
	BizcomponentDao bizcomponentDao;
	@Autowired
	UserService userService;
	@Autowired
	ArticleDao articleDao ;
	@Autowired
	CountryService countryService;
	@Autowired
	MetaData metaData;
	@Autowired
	OnlineCustomerManager onlineCustomerManager;
	@Autowired
	JaxNotificationService jaxNotificationService ; 
	
	static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	public ApiResponse getCustomer(BigDecimal countryId, String userId) {
		List<Customer> customerList = customerRepository.getCustomer(countryId, userId);
		ApiResponse response = getBlackApiResponse();
		if (customerList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		} else {
			response.getData().getValues().addAll(customerList);
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("customer");
		return response;
	}

	public ApiResponse getCustomerByCustomerId(BigDecimal countryId, BigDecimal companyId, BigDecimal customerId) {
		List<Customer> customerList = customerRepository.getCustomerByCustomerId(countryId, companyId, customerId);
		ApiResponse response = getBlackApiResponse();
		if (customerList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		} else {
			response.getData().getValues().addAll(customerList);
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("customer");
		return response;
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public Customer getCustomerDetails(String loginId) {

		return userService.getCustomerDetails(loginId);
	}

	public Customer getCustomerDetailsByCustomerId(BigDecimal customerId){
		
		Customer customerDetails = customerRepository.getCustomerDetailsByCustomerId(customerId);

		return customerDetails;
	}
	public CustomerContactDto getCustomerContactDto(BigDecimal customerId) {
		CustomerContactDto customerContactDto = new CustomerContactDto();
		List<ContactDetail> customerContacts = contactDetailRepository
				.getContactDetailForLocal(new Customer(customerId));
		ContactDetail customerContact = customerContacts.get(0);
		customerContactDto.setBlock(customerContact.getBlock());
		customerContactDto.setBuildingNo(customerContact.getBuildingNo());
		customerContactDto.setCityName(customerContact.getFsCityMaster().getFsCityMasterDescs().get(0).getCityName());
		customerContactDto
				.setCountryName(customerContact.getFsCountryMaster().getFsCountryMasterDescs().get(0).getCountryName());
		customerContactDto
				.setDistrict(customerContact.getFsDistrictMaster().getFsDistrictMasterDescs().get(0).getDistrict());
		customerContactDto.setFlat(customerContact.getFlat());
		customerContactDto
				.setStateName(customerContact.getFsStateMaster().getFsStateMasterDescs().get(0).getStateName());
		customerContactDto.setStreet(customerContact.getStreet());
		return customerContactDto;
	}

	public CustomerDto getCustomerDto(BigDecimal customerId) {
		Customer customer = customerRepository.findOne(customerId);
		CustomerDto customerDto = new CustomerDto();
		if (customer.getNationalityId() != null) {
			customerDto.setNationality(countryService
					.getCountryMasterDesc(customer.getNationalityId(), metaData.getLanguageId()).getNationality());
		}
		try {
			BeanUtils.copyProperties(customerDto, customer);
		} catch (Exception e) {
		}
		customerDto.setTitle(getTitleDescription(customer.getTitle()));
		return customerDto;
	}

	public CustomerIdProofDto getCustomerIdProofDto(BigDecimal customerId, BigDecimal identityTypeId) {
		CustomerIdProofDto customerIdProofDto = new CustomerIdProofDto();
		List<CustomerIdProof> idsProofs = customerIdProofDao.getCustomeridProofForIdType(customerId, identityTypeId);
		try {
			BeanUtils.copyProperties(customerIdProofDto, idsProofs.get(0));
		} catch (Exception e) {

		}
		customerIdProofDto
				.setIdentityType(bizcomponentDao.getBizComponentDataDescByComponmentId(identityTypeId).getDataDesc());
		return customerIdProofDto;
	}

	public CustomerIncomeRangeDto getCustomerIncomeRangeDto(BigDecimal customerId) {
		CustomerIncomeRangeDto dto = new CustomerIncomeRangeDto();
		Customer customer = customerRepository.findOne(customerId);
		dto.setArticleDetailDesc(articleDao.getAricleDetailDesc(customer));
		dto.setArticleeDescription(articleDao.getArticleDesc(customer));
		dto.setMonthlyIncome(articleDao.getMonthlyIncomeRange(customer));
		return dto;
	}
	

	private String getTitleDescription(String titleBizComponentId) {
		String titleDescription = null;
		if (titleBizComponentId != null) {
			try {
				titleDescription = bizcomponentDao.getBizComponentDataDescByComponmentId(titleBizComponentId)
						.getDataDesc();
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid title in fs_customer table value: {}", titleBizComponentId);
			}
		}
		return titleDescription;
	}
	
	public AmxApiResponse<BoolRespModel, Object> saveCustomerSecQuestions(List<SecurityQuestionModel> securityQuestions) {
		onlineCustomerManager.saveCustomerSecQuestions(securityQuestions);
		PersonInfo personInfo = userService.getPersonInfo(metaData.getCustomerId());
		CustomerModel model = new CustomerModel();
		model.setSecurityquestions(securityQuestions);
		jaxNotificationService.sendProfileChangeNotificationEmail(model, personInfo);
		BoolRespModel boolRespModel = new BoolRespModel();
		boolRespModel.setSuccess(Boolean.TRUE);
		return AmxApiResponse.build(boolRespModel);
	}

	public AmxApiResponse<BoolRespModel, Object> resetPasswordFlow(String identityInt, String resetPwd) {
		onlineCustomerManager.resetForgotPassword(identityInt, resetPwd);
		
		BoolRespModel boolRespModel = new BoolRespModel();
		boolRespModel.setSuccess(Boolean.TRUE);
		return AmxApiResponse.build(boolRespModel);
	}

	
}
