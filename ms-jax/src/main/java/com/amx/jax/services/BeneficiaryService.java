package com.amx.jax.services;

/**
 * Author :Rabil
 */
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.BeneficiaryConstant.BeneStatus;
import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.meta.model.RoutingBankMasterDTO;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.amxlib.model.RoutingBankMasterParam;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.BeneficiaryDao;
import com.amx.jax.dbmodel.AgentBranchModel;
import com.amx.jax.dbmodel.AgentMasterModel;
import com.amx.jax.dbmodel.BeneficiaryCountryView;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.dbmodel.RoutingBankMasterView;
import com.amx.jax.dbmodel.ServiceProviderModel;
import com.amx.jax.dbmodel.SwiftMasterView;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.dbmodel.bene.RelationsDescription;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IBeneficaryContactDao;
import com.amx.jax.repository.IBeneficiaryCountryDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.RoutingAgentLocationRepository;
import com.amx.jax.repository.RoutingBankMasterRepository;
import com.amx.jax.service.MetaService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.RelationsRepository;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.util.JaxUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class BeneficiaryService extends AbstractService {

	Logger logger = Logger.getLogger(BeneficiaryService.class);

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	IBeneficiaryCountryDao beneficiaryCountryDao;

	@Autowired
	BeneficiaryCheckService beneCheck;

	@Autowired
	IBeneficiaryRelationshipDao beneRelationShipDao;

	@Autowired
	IBeneficaryContactDao beneficiaryContactDao;

	@Autowired
	CustomerDao custDao;

	@Autowired
	BeneficiaryDao beneDao;

	@Autowired
	ITransactionHistroyDAO tranxHistDao;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	RelationsRepository relationsRepository;
	
	@Autowired
	JaxUtil jaxUtil;
	
	@Autowired
	JaxNotificationService jaxNotificationService;
	
	@Autowired
	private UserValidationService userValidationService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoutingBankMasterRepository routingBankMasterRepository;
	
	@Autowired
	RoutingAgentLocationRepository routingAgentLocationRepository;
	
	@Autowired
	MetaService metaService;
	
	@Autowired
	CountryRepository countryRepository;

	public ApiResponse getBeneficiaryListForOnline(BigDecimal customerId, BigDecimal applicationCountryId,
			BigDecimal beneCountryId) {
		List<BenificiaryListView> beneList = null;
		if (beneCountryId != null && beneCountryId.compareTo(BigDecimal.ZERO) != 0) {
			beneList = beneficiaryOnlineDao.getOnlineBeneListFromViewForCountry(customerId, applicationCountryId,
					beneCountryId);
		} else {
			beneList = beneficiaryOnlineDao.getOnlineBeneListFromView(customerId, applicationCountryId);
		}
		BigDecimal nationalityId = custDao.getCustById(customerId).getNationalityId();
		BenificiaryListViewOnlineComparator comparator = new BenificiaryListViewOnlineComparator(nationalityId);
		Collections.sort(beneList, comparator);
		ApiResponse response = getBlackApiResponse();
		if (beneList.isEmpty()) {
			throw new GlobalException("Beneficiary list is not found",JaxError.BENEFICIARY_LIST_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convertBeneList(beneList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("beneList");
		return response;
	}

	public class BenificiaryListViewOnlineComparator implements Comparator<BenificiaryListView> {

		private BigDecimal countryId;

		public BenificiaryListViewOnlineComparator(BigDecimal nationalityId) {
			this.countryId = nationalityId;
		}

		@Override
		public int compare(BenificiaryListView o1, BenificiaryListView o2) {

			if (o1.getCountryId() != o2.getCountryId() && o1.getCountryId() != null
					&& o1.getCountryId().equals(this.countryId)) {
				return -1;
			}
			return 0;
		}

		public BigDecimal getCountryId() {
			return countryId;
		}

		public void setCountryId(BigDecimal countryId) {
			this.countryId = countryId;
		}

	}

	public ApiResponse getBeneficiaryListForBranch(BigDecimal customerId, BigDecimal applicationCountryId,
			BigDecimal beneCountryId) {

		List<BenificiaryListView> beneList = null;
		if (beneCountryId != null && beneCountryId.compareTo(BigDecimal.ZERO) != 0) {
			beneList = beneficiaryOnlineDao.getBeneListFromViewForCountry(customerId, applicationCountryId,
					beneCountryId);
		} else {
			beneList = beneficiaryOnlineDao.getBeneListFromView(customerId, applicationCountryId);
		}

		ApiResponse response = getBlackApiResponse();
		if (beneList.isEmpty()) {
			throw new GlobalException("Beneficiary list is not found",JaxError.BENEFICIARY_LIST_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convertBeneList(beneList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("beneList");
		return response;
	}

	public ApiResponse getBeneficiaryCountryListForOnline(BigDecimal customerId) {
		List<BeneficiaryCountryView> beneocountryList = beneficiaryCountryDao.getBeneCountryForOnline(customerId);
		List<BigDecimal> supportedServiceGroupList = beneDao.getRoutingBankMasterList(); // add for channeling
																							// 04-05-2018
		ApiResponse response = getBlackApiResponse();
		if (beneocountryList.isEmpty()) {
			throw new GlobalException("Beneficiary country list is not found",
					JaxError.BENEFICIARY_COUNTRY_LIST_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convert(beneocountryList, supportedServiceGroupList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("benecountry");
		return response;
	}

	public ApiResponse getBeneficiaryCountryListForBranch(BigDecimal customerId) {
		List<BeneficiaryCountryView> beneocountryList = beneficiaryCountryDao.getBeneCountryForBranch(customerId);
		List<BigDecimal> supportedServiceGroupList = beneDao.getRoutingBankMasterList(); //add for channeling 04-05-2018
		ApiResponse response = getBlackApiResponse();
		if (beneocountryList.isEmpty()) {
			throw new GlobalException("Beneficiary country list is not found",JaxError.BENEFICIARY_COUNTRY_LIST_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convert(beneocountryList,supportedServiceGroupList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("benecountry");
		return response;
	}

	public ApiResponse disableBeneficiary(BeneficiaryListDTO beneDetails) {
		ApiResponse response = getBlackApiResponse();
		try {
			List<BeneficaryRelationship> beneRelationList = null;

			beneRelationList = beneRelationShipDao.getBeneRelationshipByBeneMasterIdForDisable(
					beneDetails.getBeneficaryMasterSeqId(), beneDetails.getCustomerId());

			if (!beneRelationList.isEmpty()) {
				BeneficaryRelationship beneRelationModel = beneRelationShipDao
						.findOne((beneRelationList.get(0).getBeneficaryRelationshipId()));
				beneRelationModel.setIsActive("D");
				beneRelationModel.setModifiedBy(beneDetails.getCustomerId().toString());
				beneRelationModel.setModifiedDate(new Date());
				beneRelationModel.setRemarks(beneDetails.getRemarks());
				beneRelationShipDao.save(beneRelationModel);
				response.setResponseStatus(ResponseStatus.OK);
			} else {
				throw new GlobalException("No record found",JaxError.NO_RECORD_FOUND);
			}
			return response;
		} catch (Exception e) {
			throw new GlobalException("Error while update");
		}
	}
	
	public ApiResponse updateFavoriteBeneficiary(BeneficiaryListDTO beneDetails) {
		ApiResponse response = getBlackApiResponse();
		try {
			List<BeneficaryRelationship> beneRelationList = null;

			beneRelationList = beneRelationShipDao.getBeneRelationshipByBeneMasterIdForDisable(
					beneDetails.getBeneficaryMasterSeqId(), beneDetails.getCustomerId());

			if (!beneRelationList.isEmpty()) {
				BeneficaryRelationship beneRelationModel = beneRelationShipDao.findOne((beneRelationList.get(0).getBeneficaryRelationshipId()));
				beneRelationModel.setModifiedBy(beneDetails.getCustomerId().toString());
				beneRelationModel.setModifiedDate(new Date());
				if(StringUtils.isBlank(beneRelationModel.getMyFavouriteBene()) || beneRelationModel.getMyFavouriteBene().equalsIgnoreCase("N")){
					beneRelationModel.setMyFavouriteBene("Y");
				}else{
					beneRelationModel.setMyFavouriteBene("N");
				}
				beneRelationShipDao.save(beneRelationModel);
				response.setResponseStatus(ResponseStatus.OK);
			} else {
				throw new GlobalException("No record found",JaxError.NO_RECORD_FOUND);
			}

			return response;
		} catch (Exception e) {
			throw new GlobalException("Error while update");
		}

	}
	
	public ApiResponse beneficiaryUpdate(BeneficiaryListDTO beneDetails) {
		ApiResponse response = getBlackApiResponse();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Error while update Custoemr Id" + beneDetails.getCustomerId()
					+ "\t Bene Ralation :" + beneDetails.getBeneficiaryRelationShipSeqId());
		}

		return response;
	}

	/**
	 * to get default beneficiary.
	 * 
	 * @param beneocountryList
	 * @return Transaction Id: idno And Beneficiary Id :idNo
	 */

	public ApiResponse getDefaultBeneficiary(BigDecimal customerId, BigDecimal applicationCountryId,
			BigDecimal beneRealtionId, BigDecimal transactionId) {
		ApiResponse response = getBlackApiResponse();
		try {
			BenificiaryListView beneList = null;
			BeneficiaryListDTO beneDto = null;
			CustomerRemittanceTransactionView trnxView = null;
			RemittancePageDto remitPageDto = new RemittancePageDto();

			if (beneRealtionId != null && beneRealtionId.compareTo(BigDecimal.ZERO) != 0) {
				beneList = beneficiaryOnlineDao.getBeneficiaryByRelationshipId(customerId, applicationCountryId,
						beneRealtionId);
			} else {
				beneList = beneficiaryOnlineDao.getDefaultBeneficiary(customerId, applicationCountryId);

			}

			if (beneList == null) {
				throw new GlobalException("Not found");
			} else {
				beneDto = beneCheck.beneCheck(convertBeneModelToDto((beneList)));
				System.out.println("beneDto :" + beneDto.getBeneficiaryRelationShipSeqId());
				if (beneDto != null && !JaxUtil.isNullZeroBigDecimalCheck(transactionId)
						&& (JaxUtil.isNullZeroBigDecimalCheck(beneRealtionId)
								|| JaxUtil.isNullZeroBigDecimalCheck(beneDto.getBeneficiaryRelationShipSeqId()))) {
					trnxView = tranxHistDao.getDefaultTrnxHist(customerId, beneDto.getBeneficiaryRelationShipSeqId());
				} else if (beneDto != null && JaxUtil.isNullZeroBigDecimalCheck(transactionId)
						&& JaxUtil.isNullZeroBigDecimalCheck(beneRealtionId)) {
					trnxView = tranxHistDao.getTrnxHistByBeneIdAndTranId(customerId, beneRealtionId, transactionId);
				} else if (beneDto != null && JaxUtil.isNullZeroBigDecimalCheck(transactionId)) {
					trnxView = tranxHistDao.getTrnxHistTranId(customerId, transactionId);
				}

			}

			remitPageDto.setBeneficiaryDto(beneDto);
			if (trnxView != null) {
				remitPageDto.setTrnxHistDto(convertTranHistDto(trnxView));
			}
			response.getData().getValues().add(remitPageDto);
			response.getData().setType(remitPageDto.getModelType());
			response.setResponseStatus(ResponseStatus.OK);
		} catch (Exception e) {
			logger.error("Error occured in getDefaultBeneficiary method", e);
			throw new GlobalException("Default bene not found" + e.getMessage());
		}
		return response;
	}
	
	
	/** My fovorite Bene List **/
	
	public ApiResponse getFavouriteBeneficiaryList(BigDecimal customerId, BigDecimal applicationCountryId) {
		List<BenificiaryListView> beneList = null;
		ApiResponse response = getBlackApiResponse();
		beneList = beneficiaryOnlineDao.getFavouriteBeneListFromViewForCountry(customerId, applicationCountryId);
		if(beneList.isEmpty()){
			beneList = beneficiaryOnlineDao.getOnlineBeneListFromView(customerId, applicationCountryId);
		}
		if (beneList.isEmpty()) {
			throw new GlobalException("My favourite eneficiary list is not found",JaxError.BENEFICIARY_LIST_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convertBeneList(beneList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("beneList");
		return response;
	}
	
	

	private List<BeneCountryDTO> convert(List<BeneficiaryCountryView> beneocountryList, List<BigDecimal> supportedServiceGroupList) {
		List<BeneCountryDTO> list = new ArrayList<BeneCountryDTO>();
		Map<BigDecimal, ServiceGroupMasterDescDto> map = metaService.getServiceGroupDtoMap();	
		for (BeneficiaryCountryView beneCountry : beneocountryList) {
			List<ServiceGroupMasterDescDto> listData = new ArrayList<ServiceGroupMasterDescDto>();					
			listData.add(map.get(BigDecimal.valueOf(2)));
			BeneCountryDTO model = new BeneCountryDTO();
			model.setApplicationCountry(beneCountry.getApplicationCountry());
			model.setBeneCountry(beneCountry.getBeneCountry());
			model.setCountryCode(beneCountry.getCountryCode());
			model.setCountryName(beneCountry.getCountryName());
			model.setCustomerId(beneCountry.getCustomerId());
			model.setIdNo(beneCountry.getIdNo());
			model.setOrsStatus(beneCountry.getOrsStatus());
			model.setSupportedServiceGroup(listData);
			if(supportedServiceGroupList.contains(beneCountry.getCustomerId()))
			{
				listData.add(map.get(BigDecimal.valueOf(1)));
				model.setSupportedServiceGroup(listData);
			}
			list.add(model);
		}
		return list;
	}

	private List<BeneficiaryListDTO> convertBeneList(List<BenificiaryListView> beneList) {
		List<BeneficiaryListDTO> output = new ArrayList<>();
		beneList.forEach(beneModel -> output.add(beneCheck.beneCheck(convertBeneModelToDto(beneModel))));
		return output;
	}

	private BeneficiaryListDTO convertBeneModelToDto(BenificiaryListView beneModel) {
		BeneficiaryListDTO dto = new BeneficiaryListDTO();
		try {
			BeanUtils.copyProperties(dto, beneModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("bene list display", e);
		}
		beneCheck.setCanTransact(dto);
		return dto;
	}

	private TransactionHistroyDTO convertTranHistDto(CustomerRemittanceTransactionView tranView) {
		TransactionHistroyDTO tranDto = new TransactionHistroyDTO();
		try {
			BeanUtils.copyProperties(tranDto, tranView);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("bene list display", e);
		}
		return tranDto;

	}

	public String getBeneficiaryContactNumber(BigDecimal beneMasterId) {
		List<BeneficaryContact> beneContactList = beneficiaryContactDao.getBeneContact(beneMasterId);
		BeneficaryContact beneContact = beneContactList.get(0);
		String contactNumber = null;
		if (beneContact != null) {
			if (beneContact.getTelephoneNumber() != null) {
				contactNumber = beneContact.getTelephoneNumber();
			} else {
				contactNumber = beneContact.getMobileNumber().toString();
			}
		}
		return contactNumber;
	}

	public SwiftMasterView getSwiftMasterBySwiftBic(String swiftBic) {
		return beneDao.getSwiftMasterBySwiftBic(swiftBic);
	}

	public Integer getTodaysTransactionForBene(BigDecimal customerId, BigDecimal benRelationId) {
		logger.info("in getTodaysTransactionForBene, customerId: " + customerId + " benRelationId: " + benRelationId);
		return tranxHistDao.getTodaysTransactionForBeneficiary(customerId, benRelationId);
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

	public void setBeneDataVerificationQuestion(List<QuestModelDTO> result) {

		result.forEach(i -> {
			if (i.getQuestNumber().equals(new BigDecimal(2))) {
				List<BenificiaryListView> list = beneficiaryOnlineDao
						.getOnlineBeneListFromView(metaData.getCustomerId(), metaData.getCountryId());
				int index = ThreadLocalRandom.current().nextInt(0, list.size());
				BenificiaryListView randomBene = list.get(index);
				logger.info("Random bene-id recieved:" + randomBene.getIdNo());
				logger.info("Random bene name:" + randomBene.getFirstName());
				i.getQuestAnswerModelDTO().setAnswerKey(randomBene.getIdNo().toString());
				String question = i.getDescription();
				Map<String, String> valuesMap = new HashMap<String, String>();
				valuesMap.put("name", randomBene.getFirstName());
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				question = sub.replace(question);
				i.setDescription(question);
			}
		});
	}

	/**
	 * @return list of bene for customer and country in meta data
	 */
	public List<BenificiaryListView> getBeneList() {
		List<BenificiaryListView> list = beneficiaryOnlineDao.getOnlineBeneListFromView(metaData.getCustomerId(),
				metaData.getCountryId());
		return list;
	}
	
	public BenificiaryListView getBeneByIdNo(BigDecimal idNo) {
		return beneficiaryOnlineDao.findOne(idNo);
	}
	
	public BenificiaryListView getLastTransactionBene() {
		List<BenificiaryListView> list = beneficiaryOnlineDao.getLastTransactionBene(metaData.getCustomerId(), metaData.getCountryId(), new PageRequest(0, 1));
		return list.get(0);
	}
	
	public BenificiaryListView getBeneBybeneficiaryRelationShipSeqId(BigDecimal beneficiaryRelationShipSeqId) {
		return beneficiaryOnlineDao.findBybeneficiaryRelationShipSeqId(beneficiaryRelationShipSeqId);
	}
	
	/**
	 * @return ApiResponse containing beneficiary relations
	 * */
	public ApiResponse getBeneRelations() {
		List<RelationsDescription> allRelationsDesc = relationsRepository.findBylangId(metaData.getLanguageId());
		List<BeneRelationsDescriptionDto> allRelationsDescDto = new ArrayList<>();
		allRelationsDesc.forEach(i -> {
			BeneRelationsDescriptionDto dto = new BeneRelationsDescriptionDto();
			jaxUtil.convert(i, dto);
			allRelationsDescDto.add(dto);
		});
		ApiResponse apiResponse = getBlackApiResponse();
		apiResponse.getData().getValues().addAll(allRelationsDescDto);
		apiResponse.getData().setType("bene-relation-desc");
		return apiResponse;
	}
	
	/**
	 * sends otp to channel provided
	 * @param channels
	 * @return apiresponse
	 * 
	 */
	public ApiResponse sendOtp(List<CommunicationChannel> channels) {
		
		Customer customer = null;
		String civilId=null;
		BigDecimal customerId = null;
		
		if (metaData.getCustomerId() != null) {
			customer = custDao.getCustById(metaData.getCustomerId());
			civilId = customer.getIdentityInt();
			customerId = customer.getCustomerId();
		}else {
			//customer is not logged-in
			throw new GlobalException("Customer not logged-in", JaxError.CUSTOMER_NOT_FOUND);
		}
		
		logger.info("customerId for sending OTPs is --> " + customerId);
		CivilIdOtpModel model = new CivilIdOtpModel();
		CustomerOnlineRegistration onlineCustReg = custDao.getOnlineCustByCustomerId(customerId);
		
		userValidationService.validateCustomerForOnlineFlow(civilId);
		
		if (onlineCustReg != null) {
			logger.info("validating customer lock count.");
			userValidationService.validateCustomerLockCount(onlineCustReg);
			model.setIsActiveCustomer(ConstantDocument.Yes.equals(onlineCustReg.getStatus()) ? true : false);
		} else {
			logger.info("onlineCustReg is null");
		}
		
		try {
			userValidationService.validateTokenDate(onlineCustReg);
		} catch (GlobalException e) {
			// reset sent token count
			onlineCustReg.setTokenSentCount(BigDecimal.ZERO);
		}
		userValidationService.validateTokenSentCount(onlineCustReg);
		userService.generateToken(civilId, model, channels);
		onlineCustReg.setEmailToken(model.getHashedeOtp());
		onlineCustReg.setSmsToken(model.getHashedmOtp());
		onlineCustReg.setTokenDate(new Date());
		BigDecimal tokenSentCount = (onlineCustReg.getTokenSentCount() == null) ? BigDecimal.ZERO
				: onlineCustReg.getTokenSentCount().add(new BigDecimal(1));
		onlineCustReg.setTokenSentCount(tokenSentCount);
		custDao.saveOnlineCustomer(onlineCustReg);

		model.setFirstName(customer.getFirstName());
		model.setLastName(customer.getLastName());
		model.setCustomerId(customer.getCustomerId());
		model.setMiddleName(customer.getMiddleName());
		model.setEmail(customer.getEmail());
		model.setMobile(customer.getMobile());
		
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.getData().setType(model.getModelType());
		response.setResponseStatus(ResponseStatus.OK);

		PersonInfo personinfo = new PersonInfo();
		try {
			BeanUtils.copyProperties(personinfo, customer);
		} catch (Exception e) {
		}

		jaxNotificationService.sendOtpSms(personinfo, model);

		if (channels != null && channels.contains(CommunicationChannel.EMAIL)) {
			jaxNotificationService.sendOtpEmail(personinfo, model);
		}
		return response;
	}
	
	public ApiResponse updateStatus(BeneficiaryListDTO beneDetails,BeneStatus status,String mOtp,String eOtp) {
		
		userService.validateOtp(null, mOtp, eOtp);
		
		ApiResponse response = getBlackApiResponse();
		try {
			List<BeneficaryRelationship> beneRelationList = null;

			if (status!=null && status.equals(BeneStatus.DISABLE)) {
				beneRelationList = beneRelationShipDao.getBeneRelationshipByBeneMasterIdForDisable(
						beneDetails.getBeneficaryMasterSeqId(), beneDetails.getCustomerId());
			}else {
				beneRelationList = beneRelationShipDao.getBeneRelationshipByBeneMasterIdForEnable(
						beneDetails.getBeneficaryMasterSeqId(), beneDetails.getCustomerId());
			}
			
			if (!beneRelationList.isEmpty()) {
				BeneficaryRelationship beneRelationModel = beneRelationShipDao
						.findOne((beneRelationList.get(0).getBeneficaryRelationshipId()));
				
				if (status!=null && status.equals(BeneStatus.DISABLE)) {
					beneRelationModel.setIsActive("D");
				}else {
					beneRelationModel.setIsActive("Y");
				}
				
				beneRelationModel.setModifiedBy(beneDetails.getCustomerId().toString());
				beneRelationModel.setModifiedDate(new Date());
				beneRelationModel.setRemarks(beneDetails.getRemarks());
				beneRelationShipDao.save(beneRelationModel);
				response.setResponseStatus(ResponseStatus.OK);
				response.getData().getValues().add(new BooleanResponse(Boolean.TRUE));
				response.getData().setType("boolean_response");
			} else {
				throw new GlobalException("No record found",JaxError.NO_RECORD_FOUND);
			}
			return response;
		} catch (GlobalException ge) {
            throw ge;
        }catch (Exception e) {
			throw new GlobalException("Error while update");
		}
	}
	
	
	public ApiResponse getServiceProviderList(RoutingBankMasterParam.RoutingBankMasterServiceImpl param) {
		
		logger.info("getServiceProviderList called with Parameters : "+param.toString());		
		List<ServiceProviderModel> serviceProviderList = routingBankMasterRepository.getServiceProvider(param.getApplicationCountryId(), 
																										param.getRoutingCountryId(), 
																										param.getServiceGroupId());
		
		ApiResponse response = getBlackApiResponse();
		if (serviceProviderList.isEmpty()) {
			throw new GlobalException("Service provider list is not found.",JaxError.SERVICE_PROVIDER_LIST_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convertSeriviceList(serviceProviderList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("routingBankMaster");
		return response;
	}
	
	private List<RoutingBankMasterDTO> convertSeriviceList(List<ServiceProviderModel> serviceProviderList) {
		
		List<RoutingBankMasterDTO> list = new ArrayList<RoutingBankMasterDTO>();
		
		for (ServiceProviderModel routingMasterRecord : serviceProviderList) {
			RoutingBankMasterDTO routingMasterDTO = new RoutingBankMasterDTO();
			routingMasterDTO.setApplicationCountryId(routingMasterRecord.getApplicationCountryId());
			routingMasterDTO.setRoutingCountryId(routingMasterRecord.getRoutingCountryId());
			routingMasterDTO.setServiceGroupId(routingMasterRecord.getServiceGroupId());
			routingMasterDTO.setServiceBankId(routingMasterRecord.getRoutingBankId());
			routingMasterDTO.setServiceBankName(routingMasterRecord.getRoutingBankName());
			routingMasterDTO.setServiceBankCode(routingMasterRecord.getRoutingBankCode());
			list.add(routingMasterDTO);
		}
		return list;
	}
	
	public ApiResponse getAgentMasterList(RoutingBankMasterParam.RoutingBankMasterServiceImpl param) {
		logger.info("getAgentMasterList called with Parameters : "+param.toString());
		List<AgentMasterModel> agentMasterList = routingBankMasterRepository.getAgentMaster(param.getApplicationCountryId(), 
																							param.getRoutingCountryId(), 
																							param.getServiceGroupId(), 
																							param.getRoutingBankId(), 
																							param.getCurrencyId());
		ApiResponse response = getBlackApiResponse();
		if (agentMasterList.isEmpty()) {
			throw new GlobalException("Agent Master List is not found.",JaxError.AGENT_BANK_LIST_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convertAgentList(agentMasterList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("routingBankMaster");
		return response;
	}
	
	private List<RoutingBankMasterDTO> convertAgentList(List<AgentMasterModel> agentMasterList) {
		
		List<RoutingBankMasterDTO> list = new ArrayList<RoutingBankMasterDTO>();
		for (AgentMasterModel routingMasterRecord : agentMasterList) {
			RoutingBankMasterDTO routingMasterDTO = new RoutingBankMasterDTO();
			routingMasterDTO.setApplicationCountryId(routingMasterRecord.getApplicationCountryId());
			routingMasterDTO.setRoutingCountryId(routingMasterRecord.getRoutingCountryId());
			routingMasterDTO.setServiceGroupId(routingMasterRecord.getServiceGroupId());
			routingMasterDTO.setServiceBankId(routingMasterRecord.getRoutingBankId());
			routingMasterDTO.setAgentBankId(routingMasterRecord.getAgentBankId());
			routingMasterDTO.setAgentBankCode(routingMasterRecord.getAgentBankCode());
			routingMasterDTO.setAgentBankName(routingMasterRecord.getAgentBankName());
			list.add(routingMasterDTO);
		}
		return list;
	}
	
	public ApiResponse getAgentLocationList(RoutingBankMasterParam.RoutingBankMasterServiceImpl param) {
		
		logger.info("getAgentLocationList called with Parameters : "+param.toString());
		List<AgentBranchModel> agentBranchList = routingAgentLocationRepository.getAgentBranch(param.getApplicationCountryId(),  
																							   param.getRoutingCountryId(), 
																							   param.getServiceGroupId(), 
																							   param.getRoutingBankId(), 
																							   param.getCurrencyId(),
																							   param.getAgentBankId());
		ApiResponse response = getBlackApiResponse();
		if (agentBranchList.isEmpty()) {
			throw new GlobalException("Agent Branch List is not found.",JaxError.AGENT_BRANCH_LIST_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convertBranchList(agentBranchList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("routingBankMaster");
		return response;
	}
	
	private List<RoutingBankMasterDTO> convertBranchList(List<AgentBranchModel> agentBranchList) {
		
		List<RoutingBankMasterDTO> list = new ArrayList<RoutingBankMasterDTO>();
		
		for (AgentBranchModel branchRecord : agentBranchList) {
			RoutingBankMasterDTO routingMasterDTO = new RoutingBankMasterDTO();
			routingMasterDTO.setApplicationCountryId(branchRecord.getApplicationCountryId());
			routingMasterDTO.setRoutingCountryId(branchRecord.getRoutingCountryId());
			routingMasterDTO.setServiceGroupId(branchRecord.getServiceGroupId());
			routingMasterDTO.setServiceBankId(branchRecord.getRoutingBankId());
			routingMasterDTO.setAgentBankId(branchRecord.getAgentBankId());
			routingMasterDTO.setBankBranchId(branchRecord.getBankBranchId());
			routingMasterDTO.setRoutingBranchId(branchRecord.getRoutingBranchId());
			routingMasterDTO.setBranchFullName(branchRecord.getBranchFullName());
			list.add(routingMasterDTO);
		}
		return list;
	}
	
	public List<BeneficaryRelationship> getBeneRelationShip(BigDecimal beneMasterId, BigDecimal beneAccountId) {
		List<BeneficaryRelationship> beneRelationShips = beneRelationShipDao
				.findByBeneficaryMasterIdAndBeneficaryAccountIdAndCustomerId(beneMasterId, beneAccountId,
						metaData.getCustomerId());

		return beneRelationShips;
	}
	
	public List<BeneficaryRelationship> getBeneRelationShipByRelationsId(BigDecimal beneMasterId,
			BigDecimal beneAccountId, BigDecimal relationsId) {
		List<BeneficaryRelationship> beneRelationShips = beneRelationShipDao
				.findByBeneficaryMasterIdAndBeneficaryAccountIdAndCustomerIdAndRelationsId(beneMasterId, beneAccountId,
						metaData.getCustomerId(), relationsId);

		return beneRelationShips;
	}
	
	// Added by chetan 03-05-2018 for country with channeling
	public ApiResponse getBeneficiaryCountryListWithChannelingForOnline(BigDecimal customerId) {

		List<CountryMasterView> countryList = countryRepository.findByLanguageId(metaData.getLanguageId());
		List<BigDecimal> supportedServiceGroupList = beneDao.getRoutingBankMasterList(); // add for channeling
																							// 03-05-2018
		ApiResponse response = getBlackApiResponse();
		if (countryList.isEmpty()) {
			throw new GlobalException("Country list is not abaliable");
		} else {
			response.getData().getValues().addAll(convertData(countryList, supportedServiceGroupList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("country");
		return response;
	}
	
	// Added by chetan 03-05-2018 for country with channeling
	private List<CountryMasterDTO> convertData(List<CountryMasterView> countryList,
			List<BigDecimal> supportedServiceGroupList) {
		List<CountryMasterDTO> list = new ArrayList<CountryMasterDTO>();
		Map<BigDecimal, ServiceGroupMasterDescDto> map = metaService.getServiceGroupDtoMap();
		for (CountryMasterView beneCountry : countryList) {
			List<ServiceGroupMasterDescDto> listData = new ArrayList<ServiceGroupMasterDescDto>();
			listData.add(map.get(BigDecimal.valueOf(2)));
			CountryMasterDTO model = new CountryMasterDTO();
			jaxUtil.convert(beneCountry, model);
			//disable cash
			/*if (supportedServiceGroupList.contains(model.getCountryId())) {
				listData.add(map.get(BigDecimal.valueOf(1)));
			}*/
			model.setSupportedServiceGroup(listData);
			list.add(model);
		}
		return list;
	}
}
