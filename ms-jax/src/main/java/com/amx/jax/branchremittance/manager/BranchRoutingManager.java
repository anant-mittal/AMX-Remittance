package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.BankBranchView;
import com.amx.jax.dbmodel.BanksView;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.remittance.ViewDeliveryMode;
import com.amx.jax.dbmodel.remittance.ViewRemittanceMode;
import com.amx.jax.dbmodel.remittance.ViewServiceDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.RemittanceApplicationManager;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.response.remittance.DeliveryModeDto;
import com.amx.jax.model.response.remittance.RemittanceModeDto;
import com.amx.jax.model.response.remittance.RoutingBankDto;
import com.amx.jax.model.response.remittance.RoutingBranchDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.RoutingServiceDto;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IAccountTypeFromViewDao;
import com.amx.jax.repository.IBankBranchView;
import com.amx.jax.repository.IBankMasterFromViewDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.repository.remittance.BeneficiaryAccountExceptRepository;
import com.amx.jax.repository.remittance.BranchDayTransactionRepository;
import com.amx.jax.repository.remittance.IServiceViewRepository;
import com.amx.jax.repository.remittance.IViewDeliveryMode;
import com.amx.jax.repository.remittance.IViewRemittanceMode;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryCheckService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.RoutingService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.FxOrderValidation;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRoutingManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4602595256039337910L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MetaData metaData;

	@Autowired
	BranchDayTransactionRepository branchTrnxRepository;

	@Autowired
	ICollectionDetailRepository collecDetailRepository;

	@Autowired
	ICurrencyDao currencyDao;

	@Autowired
	ITransactionHistroyDAO transactionHistroyDao;

	@Autowired
	EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	FxOrderValidation validateHeaderInfo;

	@Autowired
	ICustomerRepository customerDao;

	@Autowired
	BeneficiaryAccountExceptRepository beneAccountExceptionRepo;

	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;

	@Autowired
	IAccountTypeFromViewDao accountTypeRepository;

	@Autowired
	BeneficiaryCheckService beneCheckService;

	@Autowired
	BeneficiaryService beneService;

	@Autowired
	ApplicationProcedureDao applProcedureDao;

	@Autowired
	RoutingService routingService;

	@Autowired
	RemittanceTransactionManager remitTrnxManager;

	@Autowired
	BankService bankService;

	@Autowired
	RoutingProcedureDao routingPro;

	@Autowired
	CustomerIdProofRepository customerIdProof;

	@Autowired
	BizcomponentDao bizCompDao;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	CustomerIdProofRepository idProofDao;

	@Resource
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	RemittanceApplicationManager remitApplTrnxManager;

	/*
	 * @Autowired IViewRoutingDetailsRepository routingDetailsRepository;
	 */

	@Autowired
	IServiceViewRepository serviceViewRepo;

	@Autowired
	CountryRepository countryRepository;

	@Autowired
	IBankMasterFromViewDao bankMaster;

	@Autowired
	IBankBranchView bankBranchViewRepo;

	@Autowired
	IViewDeliveryMode deliveryModeRepository;

	@Autowired
	IViewRemittanceMode remittanceModeRepository;
	
	
	@Autowired
	BranchRemittanceManager branceRemittanceManager;
	

	RoutingResponseDto routingResponseDto = new RoutingResponseDto();

	public RoutingResponseDto getRoutingSetupDeatils(BigDecimal beneRelaId) {
		Map<String, Object> outPut = new HashMap<>();

		try {
			BigDecimal languageId = metaData.getLanguageId();
			/*checkingStaffIdNumberWithCustomer **/
			branceRemittanceManager.checkingStaffIdNumberWithCustomer();
			/*checking banned bank details **/
			 String warningMsg = branceRemittanceManager.bannedBankCheck(beneRelaId);
			 routingResponseDto.setWarnigMsg(warningMsg);
			
			if (!JaxUtil.isNullZeroBigDecimalCheck(languageId)) {
				languageId = new BigDecimal(1);
			}

			Map<String, Object> inputValues = getBeneMapSet(beneRelaId);
			String serviceGroupCode = inputValues.get("P_SERVICE_GROUP_CODE").toString();
			
			
			

			logger.debug("output :" + inputValues.toString());
			if (serviceGroupCode.equalsIgnoreCase(ConstantDocument.SERVICE_GROUP_CODE_CASH)) {
				outPut = routingService.getRoutingDetails(inputValues);
			} else if (serviceGroupCode.equalsIgnoreCase(ConstantDocument.SERVICE_GROUP_CODE_BANK)) {
				outPut = applProcedureDao.getRoutingBankSetupDetailsForBranch(inputValues);
			}

			logger.debug("output :" + outPut.toString());
			if (outPut != null && outPut.get("P_ERROR_MESSAGE") != null) {
				throw new GlobalException(JaxError.ROUTING_SETUP_ERROR, outPut.get("P_ERROR_MESSAGE").toString());
			} else {

				BigDecimal serviceMasterId = (BigDecimal) outPut.get("P_SERVICE_MASTER_ID");
				BigDecimal routingBankId = (BigDecimal) outPut.get("P_ROUTING_BANK_ID");
				BigDecimal routingBankBranchId = (BigDecimal) outPut.get("P_ROUTING_BANK_BRANCH_ID");
				BigDecimal remittanceId = (BigDecimal) outPut.get("P_REMITTANCE_MODE_ID");
				BigDecimal deliveryId = (BigDecimal) outPut.get("P_DELIVERY_MODE_ID");
				BigDecimal routingcountryId = (BigDecimal) outPut.get("P_ROUTING_COUNTRY_ID");
				inputValues.put("P_SERVICE_MASTER_ID",serviceMasterId);
				
				if (serviceMasterId.compareTo(BigDecimal.ZERO) != 0) {
					List<RoutingServiceDto> listOfService = new ArrayList<>();
					listOfService.add(getServiceDto(serviceMasterId));
					routingResponseDto.setServiceList(listOfService);
					if (routingcountryId.compareTo(BigDecimal.ZERO) > 0) {
						List<CountryMasterView> countryMasterView = countryRepository.findByLanguageIdAndCountryId(languageId, routingcountryId);
						List<ResourceDTO> routCount = new ArrayList<ResourceDTO>();
						ResourceDTO rout = new ResourceDTO();
						rout.setResourceId(countryMasterView.get(0).getCountryId());
						rout.setResourceName(countryMasterView.get(0).getCountryName());
						rout.setResourceCode(countryMasterView.get(0).getCountryCode());
						routCount.add(rout);
						routingResponseDto.setRoutingCountrydto(routCount);
						inputValues.put("P_ROUTING_COUNTRY_ID",countryMasterView.get(0).getCountryId());
						if (routingBankId.compareTo(BigDecimal.ZERO) > 0) {
							inputValues.put("P_ROUTING_BANK_ID",routingBankId);
							List<RoutingBankDto> lisOfRoutingBank = new ArrayList<>();
							lisOfRoutingBank.add(getRoutingBankDto(routingBankId));
							routingResponseDto.setRoutingBankDto(lisOfRoutingBank);
							if (remittanceId.compareTo(BigDecimal.ZERO) > 0) {
								List<RemittanceModeDto> remitModeDtoLst = new ArrayList<>();
								remitModeDtoLst.add(getRemittanceModeDto(remittanceId, languageId));
								routingResponseDto.setRemittanceModeList(remitModeDtoLst);
								inputValues.put("P_REMITTANCE_MODE_ID",remittanceId);
								if (deliveryId.compareTo(BigDecimal.ZERO) > 0) {
									List<DeliveryModeDto> delvModeListDto = new ArrayList<>();
									delvModeListDto.add(getDeliveryModeDto(deliveryId, languageId));
									routingResponseDto.setDeliveryModeList(delvModeListDto);
									if (routingBankBranchId.compareTo(BigDecimal.ZERO) > 0) {
										List<RoutingBranchDto> listOfCountryBranch = new ArrayList<>();
										listOfCountryBranch.add(getRoutingBranchDto(routingBankBranchId));
										routingResponseDto.setRoutingBankBranchDto(listOfCountryBranch);

									} else {
										getRoutingBankBranchList(inputValues);
									}

								} else {
									getDeliveryModeList(inputValues);
								}

							} else {
								getRemittanceModeList(inputValues);
							}

						} else {
							getRoutingCountryBankList(inputValues);
						}

					} else {
						getRoutingCountryList(inputValues);
					}
				} else {
					getServiceListList(inputValues);
				}
			}
		} catch (GlobalException e) {
			logger.error("routing  procedure", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		}
		return routingResponseDto;
	}


	public void getServiceListList(Map<String, Object> inputValues) {
		List<RoutingServiceDto> listOfService = new ArrayList<>();
		List<Map<String, Object>> listofService = routingPro.getServiceList(inputValues);

		if (listofService != null && !listofService.isEmpty()) {
			listOfService = convertRoutingDetails(listofService);
			routingResponseDto.setServiceList(listOfService);
			inputValues.put("P_SERVICE_MASTER_ID", listOfService.get(0).getServiceMasterId());
			getRoutingCountryList(inputValues);
		} else {
			throw new GlobalException(JaxError.SERVICE_NOT_FOUND,
					"Service is not avaliable :" + inputValues.toString());
		}

	}

	public void getRoutingCountryList(Map<String, Object> inputValues) {

		List<ResourceDTO> listOfRouCountry = new ArrayList<>();
		List<Map<String, Object>> listofService = routingPro.getRoutingCountryId(inputValues);

		if (listofService != null && !listofService.isEmpty()) {
			listOfRouCountry = convertRoutingCountry(listofService);
			routingResponseDto.setRoutingCountrydto(listOfRouCountry);
			inputValues.put("P_ROUTING_COUNTRY_ID", listOfRouCountry.get(0).getResourceId());
			getRoutingCountryBankList(inputValues);
		} else {
			throw new GlobalException(JaxError.ROUTING_COUNTRY_NOT_FOUND,
					"Routing country is not avaliable :" + inputValues.toString());
		}
	}

	public void getRoutingCountryBankList(Map<String, Object> inputValues) {
		List<RoutingBankDto> listOfCountry = new ArrayList<>();
		List<Map<String, Object>> listofService = routingPro.getRoutingCountryBank(inputValues);

		if (listofService != null && !listofService.isEmpty()) {
			listOfCountry = convertRoutingBank(listofService);
			routingResponseDto.setRoutingBankDto(listOfCountry);
			inputValues.put("P_ROUTING_BANK_ID", listOfCountry.get(0).getRoutingBankId());
			getRemittanceModeList(inputValues);
		} else {
			throw new GlobalException(JaxError.ROUTING_BANK_COUNTRY_NOT_FOUND,
					"Routing country bank is not avaliable :" + inputValues.toString());
		}

	}

	public void getRemittanceModeList(Map<String, Object> inputValues) {
		List<RemittanceModeDto> listOfCountry = new ArrayList<>();
		List<Map<String, Object>> listOfRemittance = routingPro.getRemittanceModeList(inputValues);
		if (listOfRemittance != null && !listOfRemittance.isEmpty()) {
			listOfCountry = convertRemittanceMode(listOfRemittance);
			routingResponseDto.setRemittanceModeList(listOfCountry);
			inputValues.put("P_REMITTANCE_MODE_ID", listOfCountry.get(0).getRemittanceModeId());
			getDeliveryModeList(inputValues);
		} else {
			throw new GlobalException(JaxError.REMITTANCE_MODE_NOT_FOUND,
					"Remittance mode not found " + inputValues.toString());

		}

	}

	public void getDeliveryModeList(Map<String, Object> inputValues) {
		List<DeliveryModeDto> listOfDelv = new ArrayList<>();
		List<Map<String, Object>> listofService = routingPro.getDeliveryModeList(inputValues);
		if (listofService != null && !listofService.isEmpty()) {
			listOfDelv = convertDeliveryMode(listofService);
			routingResponseDto.setDeliveryModeList(listOfDelv);
			inputValues.put("P_DELIVERY_MODE_ID", listOfDelv.get(0).getDeliveryModeId());
			getRoutingBankBranchList(inputValues);
		} else {
			throw new GlobalException(JaxError.DELIVERY_MODE_NOT_FOUND,"Delivery mode not found " + inputValues.toString());
		}

	}

	public void getRoutingBankBranchList(Map<String, Object> inputValues) {
		List<RoutingBranchDto> listOfCountryBranch = new ArrayList<>();
		List<Map<String, Object>> listofService = routingPro.getRoutingBranchList(inputValues);
		if (listofService != null && !listofService.isEmpty()) {
			listOfCountryBranch = convertRoutingBranch(listofService);
			routingResponseDto.setRoutingBankBranchDto(listOfCountryBranch);
		} else {
			throw new GlobalException(JaxError.ROUTING_BANK_BRANCH_NOT_FOUND,
					"Routing branch not found :" + inputValues.toString());

		}
	}

	private List<RoutingServiceDto> convertRoutingDetails(List<Map<String, Object>> listofService) {
		List<RoutingServiceDto> dtoList = new ArrayList<>();
		listofService.forEach(serviceDto -> dtoList.add(convertToService(serviceDto)));
		return dtoList;

	}

	private RoutingServiceDto convertToService(Map<String, Object> i) {
		RoutingServiceDto serDto = new RoutingServiceDto();
		serDto.setServiceMasterId(
				i.get("SERVICE_MASTER_ID") != null ? (BigDecimal) i.get("SERVICE_MASTER_ID") : BigDecimal.ZERO);
		serDto.setServiceCode(i.get("SERVICE_CODE") != null ? i.get("SERVICE_CODE").toString() : null);
		serDto.setServiceDescription(
				i.get("SERVICE_DESCRIPTION") != null ? i.get("SERVICE_DESCRIPTION").toString() : null);
		return serDto;
	}

	public List<ResourceDTO> convertRoutingCountry(List<Map<String, Object>> listofService) {
		List<ResourceDTO> dtoList = new ArrayList<ResourceDTO>();
		listofService.forEach(serviceDto -> dtoList.add(convertToRoutingCountry(serviceDto)));
		return dtoList;

	}

	private ResourceDTO convertToRoutingCountry(Map<String, Object> i) {
		ResourceDTO serDto = new ResourceDTO();
		serDto.setResourceId(i.get("ROUTING_COUNTRY_ID") != null ? (BigDecimal) i.get("ROUTING_COUNTRY_ID") : BigDecimal.ZERO);
		serDto.setResourceName(i.get("COUNTRY_NAME") != null ? i.get("COUNTRY_NAME").toString() : null);
		serDto.setResourceCode(i.get("COUNTRY_CODE") != null ? i.get("COUNTRY_CODE").toString() : null);
		return serDto;
	}

	private List<RoutingBankDto> convertRoutingBank(List<Map<String, Object>> listofService) {
		List<RoutingBankDto> dtoList = new ArrayList<>();
		listofService.forEach(serviceDto -> dtoList.add(convertToRoutingBank(serviceDto)));
		return dtoList;

	}

	private RoutingBankDto convertToRoutingBank(Map<String, Object> i) {
		RoutingBankDto serDto = new RoutingBankDto();
		serDto.setRoutingBankId(
				i.get("ROUTING_BANK_ID") != null ? (BigDecimal) i.get("ROUTING_BANK_ID") : BigDecimal.ZERO);
		serDto.setRoutingBankName(i.get("ROUTING_BANK_NAME") != null ? i.get("ROUTING_BANK_NAME").toString() : null);
		serDto.setRoutingBankCode(i.get("ROUTING_BANK_CODE") != null ? i.get("ROUTING_BANK_CODE").toString() : null);

		return serDto;
	}

	private List<RemittanceModeDto> convertRemittanceMode(List<Map<String, Object>> listofService) {
		List<RemittanceModeDto> dtoList = new ArrayList<>();
		listofService.forEach(serviceDto -> dtoList.add(convertToRemittance(serviceDto)));
		return dtoList;

	}

	private RemittanceModeDto convertToRemittance(Map<String, Object> i) {
		RemittanceModeDto serDto = new RemittanceModeDto();
		serDto.setRemittanceModeId(
				i.get("REMITTANCE_MODE_ID") != null ? (BigDecimal) i.get("REMITTANCE_MODE_ID") : BigDecimal.ZERO);
		serDto.setRemittanceDescription(
				i.get("REMITTANCE_DESCRIPTION") != null ? i.get("REMITTANCE_DESCRIPTION").toString() : null);
		serDto.setRemittancCode(i.get("REMITTANCE_CODE") != null ? i.get("REMITTANCE_CODE").toString() : null);

		return serDto;
	}

	private List<DeliveryModeDto> convertDeliveryMode(List<Map<String, Object>> listofService) {
		List<DeliveryModeDto> dtoList = new ArrayList<>();
		listofService.forEach(serviceDto -> dtoList.add(convertToDelivery(serviceDto)));
		return dtoList;

	}

	private DeliveryModeDto convertToDelivery(Map<String, Object> i) {
		DeliveryModeDto serDto = new DeliveryModeDto();
		serDto.setDeliveryModeId(
				i.get("DELIVERY_MODE_ID") != null ? (BigDecimal) i.get("DELIVERY_MODE_ID") : BigDecimal.ZERO);
		serDto.setDeliveryDescription(
				i.get("DELIVERY_DESCRIPTION") != null ? i.get("DELIVERY_DESCRIPTION").toString() : null);
		serDto.setDeliveryCode(i.get("DELIVERY_CODE") != null ? i.get("DELIVERY_CODE").toString() : null);

		return serDto;
	}

	private List<RoutingBranchDto> convertRoutingBranch(List<Map<String, Object>> listofService) {
		List<RoutingBranchDto> dtoList = new ArrayList<>();
		listofService.forEach(serviceDto -> dtoList.add(convertToBranch(serviceDto)));
		return dtoList;

	}

	private RoutingBranchDto convertToBranch(Map<String, Object> i) {
		RoutingBranchDto serDto = new RoutingBranchDto();
		serDto.setBankBranchId(
				i.get("BANK_BRANCH_ID") != null ? (BigDecimal) i.get("BANK_BRANCH_ID") : BigDecimal.ZERO);
		serDto.setBranchFullName(i.get("BRANCH_FULL_NAME") != null ? i.get("BRANCH_FULL_NAME").toString() : null);
		serDto.setBranchCode(i.get("BRANCH_CODE") != null ? (BigDecimal) i.get("BRANCH_CODE") : null);

		return serDto;
	}

	public Map<String, Object> getBeneMapSet(BigDecimal beneRelaId) {
		BenificiaryListView beneficaryDetails = beneficiaryRepository.findBybeneficiaryRelationShipSeqId(beneRelaId);
		if (beneficaryDetails == null) {
			throw new GlobalException(JaxError.BENEFICIARY_LIST_NOT_FOUND, "Beneficairy not found " + beneRelaId);
		}
		
		branceRemittanceManager.beneAddCheck(beneficaryDetails);
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("P_USER_TYPE", ConstantDocument.BRANCH);
		inputValues.put("P_APPLICATION_COUNTRY_ID", beneficaryDetails.getApplicationCountryId());
		inputValues.put("P_BENEFICIARY_COUNTRY_ID", beneficaryDetails.getBenificaryCountry()); //P_BENEFICIARY_COUNTRY_ID
		inputValues.put("P_BENEFICIARY_BANK_ID", beneficaryDetails.getBankId());
		inputValues.put("P_BENEFICIARY_BRANCH_ID", beneficaryDetails.getBranchId());
		inputValues.put("P_SERVICE_GROUP_CODE", beneficaryDetails.getServiceGroupCode());
		inputValues.put("P_CURRENCY_ID", beneficaryDetails.getCurrencyId());
		inputValues.put("P_SERVICE_GROUP_CODE", beneficaryDetails.getServiceGroupCode());
		inputValues.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneficaryDetails.getBeneficiaryAccountSeqId());

		return inputValues;
	}

/*	public RoutingResponseDto getRoutingDetailsByServiceId(BigDecimal beneRelaId, BigDecimal serviceMasterId) {
		Map<String, Object> inputValues = getBeneMapSet(beneRelaId);
		inputValues.put("P_SERVICE_MASTER_ID", serviceMasterId);
		List<RoutingServiceDto> listOfService = new ArrayList<>();
		listOfService.add(getServiceDto(serviceMasterId));
		routingResponseDto.setServiceList(listOfService);
		getRoutingCountryList(inputValues);
		return routingResponseDto;
	}*/

	
public RoutingResponseDto getRoutingDetailsByServiceId(BigDecimal beneRelaId, BigDecimal serviceMasterId) {
	Map<String, Object> inputValues = getBeneMapSet(beneRelaId);
	List<RoutingServiceDto> listOfService = new ArrayList<>();
	List<Map<String, Object>> listofService = routingPro.getServiceList(inputValues);
	if (listofService != null && !listofService.isEmpty()) {
		listOfService = convertRoutingDetails(listofService);
		routingResponseDto.setServiceList(listOfService);
	}
	inputValues.put("P_SERVICE_MASTER_ID", serviceMasterId);
	getRoutingCountryList(inputValues);
	return routingResponseDto;
}
	
	public RoutingResponseDto getRemittanceDetailsByServiceIdAndBankId(BigDecimal beneRelaId, BigDecimal serviceMasterId,
			BigDecimal routingCountryId,BigDecimal routingBankId,BigDecimal remittanceModeId) {
		Map<String, Object> inputValues = getBeneMapSet(beneRelaId);
		List<RoutingServiceDto> listOfService = new ArrayList<>();
		List<Map<String, Object>> listofService = routingPro.getServiceList(inputValues);
		if (listofService != null && !listofService.isEmpty()) {
			listOfService = convertRoutingDetails(listofService);
			routingResponseDto.setServiceList(listOfService);
		}
		inputValues.put("P_SERVICE_MASTER_ID", serviceMasterId);
		inputValues.put("P_ROUTING_COUNTRY_ID", routingCountryId);
		inputValues.put("P_ROUTING_BANK_ID", routingBankId);
		if(JaxUtil.isNullZeroBigDecimalCheck(routingCountryId)) {
			List<CountryMasterView> countryMasterView = countryRepository.findByLanguageIdAndCountryId(metaData.getLanguageId(), routingCountryId);
			List<ResourceDTO> routCount = new ArrayList<ResourceDTO>();
			ResourceDTO rout = new ResourceDTO();
			rout.setResourceId(countryMasterView.get(0).getCountryId());
			rout.setResourceName(countryMasterView.get(0).getCountryName());
			rout.setResourceCode(countryMasterView.get(0).getCountryCode());
			routCount.add(rout);
			routingResponseDto.setRoutingCountrydto(routCount);
			if(JaxUtil.isNullZeroBigDecimalCheck(routingBankId)) {
				List<RoutingBankDto> lisOfRoutingBank = new ArrayList<>();
				List<Map<String, Object>> listofRoutingMAp = routingPro.getRoutingCountryBank(inputValues);
				if (listofRoutingMAp != null && !listofRoutingMAp.isEmpty()) {
					lisOfRoutingBank = convertRoutingBank(listofRoutingMAp);
					routingResponseDto.setRoutingBankDto(lisOfRoutingBank);
				}
				//lisOfRoutingBank.add(getRoutingBankDto(routingBankId));
				//routingResponseDto.setRoutingBankDto(lisOfRoutingBank);
				if(JaxUtil.isNullZeroBigDecimalCheck(remittanceModeId)) {
					List<RemittanceModeDto> remitModeDtoLst = new ArrayList<>();
					RemittanceModeDto remitModeDto = new RemittanceModeDto();
					List<ViewRemittanceMode> list = remittanceModeRepository.findByRemittanceModeIdAndLanguageId(remittanceModeId, metaData.getLanguageId());
					remitModeDto.setRemittanceModeId(list.get(0).getRemittanceModeId());
					remitModeDto.setRemittancCode(list.get(0).getRemittancCode());
					remitModeDto.setRemittanceDescription(list.get(0).getRemittanceDescription());
					remitModeDtoLst.add(remitModeDto);
					routingResponseDto.setRemittanceModeList(remitModeDtoLst);
					inputValues.put("P_REMITTANCE_MODE_ID", list.get(0).getRemittanceModeId());
					getDeliveryModeList(inputValues);
				}else {
					getRemittanceModeList(inputValues);
				}
			}else {
					getRoutingCountryBankList(inputValues);
				}
			
			
		}else {
			getRoutingCountryList(inputValues);
		}	
	return routingResponseDto;
	}

	public RoutingResponseDto getRoutingSetup(BranchRemittanceApplRequestModel requestApplModel) {
		BigDecimal beneRelId = requestApplModel.getBeneId();
		BigDecimal serviceMasterId = requestApplModel.getServiceMasterId();
		BigDecimal routingCountryId = requestApplModel.getRoutingCountryId();
		BigDecimal routingBankId = requestApplModel.getRoutingBankId();
		BigDecimal remittanceModeId = requestApplModel.getRemittanceModeId();
		
		
		
		if(JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getBeneId())
				&& JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getServiceMasterId())
				&& JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getRoutingCountryId())) {
			getRemittanceDetailsByServiceIdAndBankId(beneRelId,serviceMasterId,routingCountryId,routingBankId,remittanceModeId);
		}else if (JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getBeneId())
				&& JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getServiceMasterId())) {
			getRoutingDetailsByServiceId(requestApplModel.getBeneId(), requestApplModel.getServiceMasterId());
		}else {
			getRoutingSetupDeatils(requestApplModel.getBeneId());
		}
		

		return routingResponseDto;

	}

	public RemittanceModeDto getRemittanceModeDto(BigDecimal remittanceId, BigDecimal languageId) {
		RemittanceModeDto remitModeDto = new RemittanceModeDto();
		List<ViewRemittanceMode> list = remittanceModeRepository.findByRemittanceModeIdAndLanguageId(remittanceId, languageId);
		remitModeDto.setRemittanceModeId(list.get(0).getRemittanceModeId());
		remitModeDto.setRemittancCode(list.get(0).getRemittancCode());
		remitModeDto.setRemittanceDescription(list.get(0).getRemittanceDescription());
		return remitModeDto;
	}

	public RoutingBranchDto getRoutingBranchDto(BigDecimal routingBankBranchId) {
		BankBranchView bankBrnView = bankBranchViewRepo.findOne(routingBankBranchId);
		RoutingBranchDto roBranch = new RoutingBranchDto();
		roBranch.setBankBranchId(bankBrnView.getBankBranchId());
		roBranch.setBranchCode(bankBrnView.getBranchCode());
		roBranch.setBranchFullName(bankBrnView.getBranchFullName());
		return roBranch;
	}

	public RoutingBankDto getRoutingBankDto(BigDecimal routingBankId) {
		List<BanksView> bankView = bankMaster.getBankListByBankId(routingBankId);
		RoutingBankDto bankDto = new RoutingBankDto();
		bankDto.setRoutingBankId(bankView.get(0).getBankId());
		bankDto.setRoutingBankCode(bankView.get(0).getBankCode());
		bankDto.setRoutingBankName(bankView.get(0).getBankFullName());
		return bankDto;
	}

	public DeliveryModeDto getDeliveryModeDto(BigDecimal deliveryId, BigDecimal languageId) {
		DeliveryModeDto delvDto = new DeliveryModeDto();
		List<ViewDeliveryMode> listDelv = deliveryModeRepository.findByDeliveryModeIdAndLanguageId(deliveryId, languageId);
		delvDto.setDeliveryCode(listDelv.get(0).getDeliveryCode());
		delvDto.setDeliveryModeId(listDelv.get(0).getDeliveryModeId());
		delvDto.setDeliveryDescription(listDelv.get(0).getDeliveryDescription());
		return delvDto;
	}

	public RoutingServiceDto getServiceDto(BigDecimal serviceMasterId) {
		List<ViewServiceDetails> view = serviceViewRepo.findByServiceMasterId(serviceMasterId);
		RoutingServiceDto service = new RoutingServiceDto();
		service.setServiceCode(view.get(0).getServiceCode());
		service.setServiceMasterId(view.get(0).getServiceMasterId());
		service.setServiceGroupCode(view.get(0).getServiceGroupCode());
		service.setServiceDescription(view.get(0).getServiceDescription());
		return service;
	}
	
	public ResourceDTO getRoutingCountryDto(BigDecimal routingCountryId) {
		List<CountryMasterView> countryMasterView = countryRepository
				.findByLanguageIdAndCountryId(metaData.getLanguageId(), routingCountryId);
		ResourceDTO service = new ResourceDTO();
		service.setResourceId(countryMasterView.get(0).getCountryId());
		service.setResourceCode(countryMasterView.get(0).getCountryCode());
		service.setResourceName(countryMasterView.get(0).getCountryName());
		return service;

	}
	
}
