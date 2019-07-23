package com.amx.jax.branchremittance.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.AccountTypeFromViewModel;
import com.amx.jax.dbmodel.BankBranchView;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleAmiec;
import com.amx.jax.dbmodel.remittance.BeneficiaryAccountException;
import com.amx.jax.dbmodel.remittance.ViewBnkFlexVal;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.RemittanceApplicationAdditionalDataManager;
import com.amx.jax.manager.RemittanceApplicationManager;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.AmlCheckResponseDto;
import com.amx.jax.model.response.remittance.BeneAdditionalDto;
import com.amx.jax.model.response.remittance.BranchExchangeRateBreakup;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.IAccountTypeFromViewDao;
import com.amx.jax.repository.IAdditionalBankRuleAmiecRepository;
import com.amx.jax.repository.IBankBranchView;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.repository.remittance.BeneficiaryAccountExceptRepository;
import com.amx.jax.repository.remittance.BranchDayTransactionRepository;
import com.amx.jax.repository.remittance.IvBankFlxValRepository;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryCheckService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.RoutingService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.jax.validation.FxOrderValidation;


@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceManager extends AbstractModel {

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
	RoutingService  routingService;
	
	@Autowired
	RemittanceTransactionManager remitTrnxManager;
	
	@Autowired
	BankService bankService;
	
	@Autowired
	RoutingProcedureDao routingPro;
	
	@Autowired
	CustomerIdProofRepository customerIdProof ;
	
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
	
	@Autowired
	IAdditionalBankRuleAmiecRepository amiecBankRuleRepo;


	@Autowired
	RemittanceApplicationAdditionalDataManager applDataManager;
	
	@Autowired
	BranchRoutingManager branchRoutingManager;
	

	@Autowired
	BankMasterRepository bankMasterDao;
	
	

	
	
	
	@Autowired
	IServiceApplicabilityRuleDao serviceApplRule;
	
	@Autowired
	IBankBranchView bankBranchRepo;
	
	@Autowired
	IViewCityDao cityRespository;
	
	@Autowired
	IvBankFlxValRepository viewBankFlxValRepository;
	
	
	
	public void checkingStaffIdNumberWithCustomer() {
		boolean checkStatus = Boolean.FALSE;
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal countryId = metaData.getCountryId();
		String customerIdentityId =null;
		String employeeIdentityId =null;
		
		EmployeeDetailsView empDetails = null;
		List<Customer> customerList = null;
		
		if(JaxUtil.isNullZeroBigDecimalCheck(employeeId)) {
			empDetails = employeeDetailsRepository.findByEmployeeId(employeeId);
			if(empDetails!=null) {
			employeeIdentityId = empDetails.getCivilId();
			}else {
				throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"Employee deails not found ");
			}
		}else {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"Employee Id should not be blank");
		}
		 customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		 
		 if(customerList!=null && !customerList.isEmpty()) {
			 customerIdentityId = customerList.get(0).getIdentityInt();
		 }
		 if(employeeIdentityId!= null && customerIdentityId!=null && employeeIdentityId.equalsIgnoreCase(customerIdentityId)) {
			 throw new GlobalException(JaxError.TRNX_NOT_ALLOWED_ON_YOUR_OWN_LOGIN,"Transaction cannot be done on your own login account");
		 }
		
	}
	
	public boolean beneAccountException(BenificiaryListView beneficaryDetails) {
		boolean checkStatus = Boolean.TRUE;
		BigDecimal customerId = metaData.getCustomerId();
		List<BeneficiaryAccountException> accountExcepList = null;  
		if(beneficaryDetails !=null && beneficaryDetails.getBankAccountNumber()!=null) {
			accountExcepList = beneAccountExceptionRepo.getBeneAccountExceptionList(beneficaryDetails.getBenificaryCountry(), beneficaryDetails.getBankId(), beneficaryDetails.getBankAccountNumber(), beneficaryDetails.getIsActive());
		}
		
		if(accountExcepList != null && !accountExcepList.isEmpty()) {
			checkStatus = Boolean.FALSE;
			 throw new GlobalException(JaxError.BENE_ACCOUNT_EXCEPTION,"We can't proceed for transactions by using this account details!");
		}
		
		return checkStatus;
	}
	
	
	public void checkBeneAccountType(BenificiaryListView beneficaryDetails) {
		boolean chkAccType = Boolean.FALSE;
		List<AccountTypeFromViewModel> lstAccType = accountTypeRepository.getAccountTypeByCountryId(beneficaryDetails.getBenificaryCountry());
		if((beneficaryDetails.getBankAccountNumber()!=null || beneficaryDetails.getIbanNumber()!=null) && beneficaryDetails.getBankAccountTypeId()!=null) {
		if (lstAccType != null && lstAccType.size() != 0) {
			for (AccountTypeFromViewModel accountTypeFromView : lstAccType) {
				if (beneficaryDetails.getBankAccountTypeId()!= null && accountTypeFromView.getAdditionalAmiecId() != null && accountTypeFromView.getAdditionalAmiecId().compareTo(beneficaryDetails.getBankAccountTypeId()) == 0) {
					chkAccType = Boolean.TRUE;
					break;
				}
			}
		}else {
			chkAccType = Boolean.FALSE;
		}
		
	}else {
		chkAccType = Boolean.TRUE;
	}
		if(!chkAccType) {
			 throw new GlobalException(JaxError.BENE_ACCOUNT_TYPE_MISMATCH,"Account Type Mismatch, Please edit and save the beneficairy ");
		}
		

	}
	
	
	public void beneAddCheck(BenificiaryListView beneficaryDetails) {
		BeneficiaryListDTO checkdto = beneCheckService.beneCheck(convertBeneModelToDto(beneficaryDetails));
		
		String iBanFlag = null;
		if(checkdto!=null) {
			if(checkdto.getBeneficiaryErrorStatus()!=null && checkdto.getBeneficiaryErrorStatus().size()>0) {
				throw new GlobalException(checkdto.getBeneficiaryErrorStatus().get(0).getErrorCode(),checkdto.getBeneficiaryErrorStatus().get(0).getErrorDesc());
			}
		}
		
		BankMasterModel bankMaster = bankService.getBankById(beneficaryDetails.getBankId());
		if(bankMaster!=null) {
			iBanFlag = bankMaster.getIbanFlag();
		}
		
		if(!JaxUtil.isNullZeroBigDecimalCheck(beneficaryDetails.getMapSequenceId())) {
			throw new GlobalException(JaxError.BENE_MAP_SEQ_MISSING,"Beneficairy map seq is missing , please update beneficiray");
		}
		
		
	}
	
	public String bannedBankCheck(BigDecimal beneRelationId) {
		
		BenificiaryListView beneficaryDetails =beneficiaryRepository.findBybeneficiaryRelationShipSeqId(beneRelationId);
		Map<String, Object> inputValues = new HashMap<>();
		String alertMessage =null;
		inputValues.put("P_APPLICATION_COUNTRY_ID", beneficaryDetails.getApplicationCountryId());
		inputValues.put("P_BENEFICIARY_BANK_ID", beneficaryDetails.getBankId());
		inputValues.put("P_BENEFICIARY_MASTER_ID", beneficaryDetails.getBeneficaryMasterSeqId());
		Map<String, Object> output = applProcedureDao.getBannedBankCheckProcedure(inputValues);
		if(output!=null) {
			String errorMessage = (String)output.get("P_ERROR_MESSAGE");
			 alertMessage = (String)output.get("P_ALERT_MESSAGE");
			
			if (errorMessage != null) {
				throw new GlobalException(JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL, errorMessage);
			}
		}
		return alertMessage;
	}
	

	
	
	
	
	
	//public Map<String ,Object> getExchangeRateForBranch(BranchRemittanceApplRequestModel requestModel,Map<String ,Object> map){
	public Map<String ,Object> getExchangeRateForBranch(BranchRemittanceApplRequestModel requestModel,RoutingResponseDto branchRoutingDto){
	
		Map<String, Object> outPut = new HashMap<>();
		try {
			
			BenificiaryListView beneficaryDetails =beneficiaryRepository.findBybeneficiaryRelationShipSeqId(requestModel.getBeneId());
			//BranchExchangeRateBreakup brExchRateBreakup =  requestModel.getBranchExRateBreakup();
			Customer customer = custDao.getCustById(metaData.getCustomerId());
			String customerType =getCustomerType(customer.getCustomerTypeId());  
			BigDecimal routingBankId = BigDecimal.ZERO;
			BigDecimal serviceMasterId = BigDecimal.ZERO;
			BigDecimal remittancModeId = BigDecimal.ZERO;
			
			
			if(JaxUtil.isNullZeroBigDecimalCheck(requestModel.getRoutingBankId()) && requestModel.getRoutingBankId().compareTo(BigDecimal.ZERO)>0) {
				routingBankId = requestModel.getRoutingBankId();
			}else {
				routingBankId =branchRoutingDto.getRoutingBankDto().get(0).getRoutingBankId();
			}
			
			
			if(JaxUtil.isNullZeroBigDecimalCheck(requestModel.getServiceMasterId()) && requestModel.getServiceMasterId().compareTo(BigDecimal.ZERO)>0) {
				serviceMasterId = requestModel.getServiceMasterId();
			}else {
				serviceMasterId =branchRoutingDto.getServiceList().get(0).getServiceMasterId();
			}
			
			
			if(JaxUtil.isNullZeroBigDecimalCheck(requestModel.getRemittanceModeId()) && requestModel.getRemittanceModeId().compareTo(BigDecimal.ZERO)>0) {
				remittancModeId = requestModel.getRemittanceModeId();
			}else {
				remittancModeId =branchRoutingDto.getRemittanceModeList().get(0).getRemittanceModeId();
			}
			
			
			
			
			
			Map<String, Object> inputValues = new HashMap<>();
			inputValues.put("P_USER_TYPE", ConstantDocument.BRANCH);
			inputValues.put("P_APPLICATION_COUNTRY_ID", beneficaryDetails.getApplicationCountryId());
			inputValues.put("P_ROUTING_COUNTRY_ID",branchRoutingDto.getRoutingCountrydto().get(0).getResourceId()); // map.get("P_ROUTING_COUNTRY_ID"));
			inputValues.put("P_BRANCH_ID",metaData.getCountryBranchId());
			inputValues.put("P_COMPANY_ID",metaData.getCompanyId());
			inputValues.put("P_ROUTING_BANK_ID", routingBankId);//map.get("P_ROUTING_BANK_ID"));
			inputValues.put("P_SERVICE_MASTER_ID", serviceMasterId);// map.get("P_SERVICE_MASTER_ID"));
			inputValues.put("P_DELIVERY_MODE_ID", branchRoutingDto.getDeliveryModeList().get(0).getDeliveryModeId());//map.get("P_DELIVERY_MODE_ID"));
			inputValues.put("P_REMITTANCE_MODE_ID", remittancModeId);//map.get("P_REMITTANCE_MODE_ID")); 
			inputValues.put("P_FOREIGN_CURRENCY_ID", beneficaryDetails.getCurrencyId()); //NC
			inputValues.put("P_SELECTED_CURRENCY_ID", getSelectedCurrency(beneficaryDetails.getCurrencyId(), requestModel)); // NC
			inputValues.put("P_CUSTOMER_ID", metaData.getCustomerId());
			inputValues.put("P_CUSTOMER_TYPE", customerType);
			inputValues.put("P_LOYALTY_POINTS_IND", requestModel.isAvailLoyalityPoints()==true?ConstantDocument.Yes:ConstantDocument.No);
			inputValues.put("P_SPECIAL_DEAL_RATE", null);
			inputValues.put("P_OVERSEAS_CHRG_IND", null);
			inputValues.put("P_SELECTED_CURRENCY_AMOUNT", getSelectedCurrencyAmount(beneficaryDetails.getCurrencyId(), requestModel));//requestModel.getBranchExRateBreakup().getConvertedFCAmount());
			inputValues.put("P_SPOT_RATE", null);
			inputValues.put("P_CASH_ROUND_IND", null);
			inputValues.put("P_ROUTING_BANK_BRANCH_ID", branchRoutingDto.getRoutingBankBranchDto().get(0).getBankBranchId());//map.get("P_ROUTING_BANK_BRANCH_ID"));
			inputValues.put("P_BENE_ID", beneficaryDetails.getBeneficaryMasterSeqId());
			inputValues.put("P_BENEFICIARY_COUNTRY_ID", beneficaryDetails.getBenificaryCountry());
			inputValues.put("P_BENE_BANK_ID", beneficaryDetails.getBankId());
			inputValues.put("P_BENE_BANK_BRANCH_ID", beneficaryDetails.getBranchId());
			inputValues.put("P_BENE_ACCOUNT_NO", beneficaryDetails.getBankAccountNumber());
			inputValues.put("P_APPROVAL_YEAR", BigDecimal.ZERO);
			inputValues.put("P_APPROVAL_NO", BigDecimal.ZERO);
			outPut = applProcedureDao.getExchangeRateForBranch(inputValues);
			if(outPut!=null && outPut.get("P_ERROR_MESSAGE")!=null){
				throw new GlobalException(JaxError.EXCHANGE_RATE_ERROR, outPut.get("P_ERROR_MESSAGE").toString());
			}
			
		}catch(GlobalException e){
			logger.error("exchange rate procedure", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}
		return outPut; 
	}

	
	//public List<AmlCheckResponseDto> amlTranxAmountCheckForRemittance(BranchRemittanceApplRequestModel requestModel,BranchRemittanceGetExchangeRateResponse exchangeRateResposne){
	
	public List<AmlCheckResponseDto> amlTranxAmountCheckForRemittance(BigDecimal beneRelId,BigDecimal foreignamount){
		Map<String, Object> outPut = new HashMap<>();
		List<AmlCheckResponseDto> listAmlMessage = new ArrayList<>();
		try {
			BenificiaryListView beneficaryDetails =beneficiaryRepository.findBybeneficiaryRelationShipSeqId(beneRelId);
			Map<String, Object> inputValues = new HashMap<>();
			inputValues.put("P_APPLICATION_COUNTRY_ID", beneficaryDetails.getApplicationCountryId());
			inputValues.put("P_BENEFICIARY_COUNTRY_ID",beneficaryDetails.getBenificaryCountry());
			inputValues.put("P_CUSTOMER_ID",metaData.getCustomerId());
			inputValues.put("P_BENE_ID",beneficaryDetails.getBeneficaryMasterSeqId());
			inputValues.put("P_FC_AMOUNT",foreignamount); 
			outPut = applProcedureDao.amlTranxAmountCheckForRemittance(inputValues);
			
			if(outPut!=null) {
				if( outPut.get("MESSAGE1")!=null && outPut.get("MESSAGE1")!="") {
				AmlCheckResponseDto dto = new AmlCheckResponseDto();
				dto.setMessageCode("MESSAGE1");
				dto.setMessageDescription(outPut.get("MESSAGE1").toString());
				dto.setAmlFlag(ConstantDocument.Yes);
				//listAmlMessage.add(dto);
				}
				if( outPut.get("MESSAGE2")!=null && outPut.get("MESSAGE2")!="") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("MESSAGE2");
					dto.setMessageDescription(outPut.get("MESSAGE2").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					}
				
				if( outPut.get("MESSAGE3")!=null && outPut.get("MESSAGE3")!="") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("MESSAGE3");
					dto.setMessageDescription(outPut.get("MESSAGE3").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					}
				if( outPut.get("MESSAGE4")!=null && outPut.get("MESSAGE4")!="") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("MESSAGE4");
					dto.setMessageDescription(outPut.get("MESSAGE4").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					}
				
				if(outPut.get("MESSAGE3")!=null) {
					
				
				if(outPut.get("RANGE1FROM")!=null && !outPut.get("RANGE1FROM").equals("")) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("RANGE1FROM - RANGE1TO");
					dto.setMessageDescription(outPut.get("RANGE1FROM").toString()+" - " + outPut.get("RANGE1TO")==null?"0":outPut.get("RANGE1TO").toString());
					dto.setRangeSlab(outPut.get("RANGE1COUNT")==null?"0":outPut.get("RANGE1COUNT").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					
				}
					
				if(outPut.get("RANGE2FROM")!=null && !outPut.get("RANGE2FROM").equals("")) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("RANGE2FROM - RANGE2TO" );
					dto.setMessageDescription(outPut.get("RANGE2FROM").toString()+" - " + outPut.get("RANGE2TO")==null?"0":outPut.get("RANGE2TO").toString());
					dto.setRangeSlab(outPut.get("RANGE2COUNT")==null?"0":outPut.get("RANGE2COUNT").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
				
				}
				
				if(outPut.get("RANGE3FROM")!=null && !outPut.get("RANGE3FROM").equals("")) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("RANGE3FROM - RANGE3TO");
					dto.setMessageDescription(outPut.get("RANGE3FROM").toString()+" - "+ outPut.get("RANGE3TO")==null?"0":outPut.get("RANGE3TO").toString());
					dto.setRangeSlab(outPut.get("RANGE3COUNT")==null?"0":outPut.get("RANGE3COUNT").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					
				}
				
				if(outPut.get("RANGE4FROM")!=null && !outPut.get("RANGE4FROM").equals("")) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("RANGE4FROM - RANGE4TO");
					dto.setMessageDescription(outPut.get("RANGE4FROM").toString()+" - "+ outPut.get("RANGE4TO")==null?"0":outPut.get("RANGE4TO").toString());
					dto.setRangeSlab(outPut.get("RANGE4COUNT")==null?"0":outPut.get("RANGE4COUNT").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					
				}
				if(outPut.get("RANGE5FROM")!=null && !outPut.get("RANGE5FROM").equals("")) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("RANGE5FROM - RANGE5TO");
					dto.setMessageDescription(outPut.get("RANGE5FROM").toString()+" - "+ outPut.get("RANGE5TO")==null?"0":outPut.get("RANGE5TO").toString());
					dto.setRangeSlab(outPut.get("RANGE5COUNT")==null?"0":outPut.get("RANGE5COUNT").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					
				}
				if(outPut.get("RANGE6FROM")!=null && !outPut.get("RANGE6FROM").equals("")) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("RANGE6FROM - RANGE6TO");
					dto.setMessageDescription(outPut.get("RANGE6FROM").toString()+" - "+ outPut.get("RANGE6TO")==null?"0":outPut.get("RANGE6TO").toString());
					dto.setRangeSlab(outPut.get("RANGE6COUNT")==null?"0":outPut.get("RANGE6COUNT").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					
				}
				
			}
				
				if (outPut.get("AUTHTYPE1") != null && outPut.get("AUTHTYPE1") != "") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("AUTHTYPE1");
					dto.setMessageDescription(outPut.get("MESSAGE1").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					listAmlMessage.add(dto);
				}
				if (outPut.get("AUTHTYPE2") != null && outPut.get("AUTHTYPE2") != "") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("AUTHTYPE2");
					dto.setMessageDescription(outPut.get("MESSAGE2").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					listAmlMessage.add(dto);
				}
				
				if (outPut.get("AUTHTYPE3") != null && outPut.get("AUTHTYPE3") != "") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("AUTHTYPE3");
					dto.setMessageDescription(outPut.get("MESSAGE3").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					listAmlMessage.add(dto);
				}
				
				if (outPut.get("AUTHTYPE4") != null && outPut.get("AUTHTYPE4") != "") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("AUTHTYPE4");
					dto.setMessageDescription(outPut.get("MESSAGE4").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					listAmlMessage.add(dto);
				}
			
		}
			
		}catch(GlobalException e){
			logger.error("aml procedure", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}
		return listAmlMessage; 
	}
	public void validateBlackListedBene(BenificiaryListView beneficaryDetails){
		remitTrnxManager.validateBlackListedBene(beneficaryDetails);
	}
	
	
	
	 public void validateAdditionalCheck(Customer customer,BenificiaryListView beneficaryDetails,BigDecimal localNetAmount,BranchRemittanceApplRequestModel requestApplModel){
		 // EX_APPL_ADDL_CHECKS
		 BigDecimal customerId = BigDecimal.ZERO;
		 String allowNoBank = null;
		 
		 BigDecimal  routingBankId   = requestApplModel.getRoutingBankId();
		 BigDecimal  serviceMasterId =requestApplModel.getServiceMasterId();
		 BigDecimal  beneBankId   = beneficaryDetails.getBankId()==null?BigDecimal.ZERO:beneficaryDetails.getBankId();
		 
		 if(customer!=null) {
		  customerId     =customer.getCustomerId();
		 }else {
			 throw new GlobalException(JaxError.NULL_CUSTOMER_ID, "customer Id should not be blank");
		 }
		 
		 BankMasterModel bankDetails = bankService.getBankById(routingBankId);
		 if(bankDetails!=null && bankDetails.getRecordStatus()!=null && bankDetails.getRecordStatus().equalsIgnoreCase(ConstantDocument.Yes)) {
			 allowNoBank = bankDetails.getAllowNoBank();
		 }else {
			 throw new GlobalException(JaxError.INVALID_BENE_BANK, " Invalid Bank  "+routingBankId);
		 }
		 
		 if(JaxUtil.isNullZeroBigDecimalCheck(serviceMasterId) 
				 && serviceMasterId.compareTo(ConstantDocument.SERVICE_MASTER_ID_DD)==0 
				 && !allowNoBank.equalsIgnoreCase(ConstantDocument.Yes))
		 {
			 throw new GlobalException(JaxError.INVALID_BENE_BANK, " For Cash / DD  Bene bank and branch should be No bank and No branch "+routingBankId +"\t serviceMasterId :"+serviceMasterId+"\t allowNoBank :"+allowNoBank);
		 }
		 
		 if(JaxUtil.isNullZeroBigDecimalCheck(routingBankId) 
				 && JaxUtil.isNullZeroBigDecimalCheck(beneBankId) 
				 && routingBankId.compareTo(beneBankId)==0
				 && serviceMasterId.compareTo(ConstantDocument.SERVICE_MASTER_ID_TT)==0 ) {
			 throw new GlobalException(JaxError.INVALID_BENE_BANK, " Routing and beneficary bank cannot be the same for TT "+routingBankId +"\t serviceMasterId :"+serviceMasterId+"\t beneBankId :"+beneBankId);
		 }
		 
		 if(customer.getCustomerTypeId().compareTo(new BigDecimal(91))==0){ //Checking for retails 
			 List<CustomerIdProof> list = idProofDao.getCustomerImageValidation(customerId, ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID);
			 
			 BigDecimal wbLimit = routingPro.getWbLimit();
			
			 
			 if(list!=null && !list.isEmpty() && list.size()>1) {
				 throw new GlobalException(JaxError.INVALID_CUSTOMER, " Customer has many ID Proofs-'-"+customerId);
			 }
			 
			 if(list!=null && list.size()==1 && JaxUtil.isNullZeroBigDecimalCheck(wbLimit) && (list.get(0).getIdentityTypeId().compareTo(ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID)==0?true:false)) {
				if(localNetAmount.compareTo(wbLimit)==1) {
					 throw new GlobalException(JaxError.INVALID_BENE_BANK, " Non ID card holders cannot exceed amount-"+wbLimit);
				}
			 }
		}
		 
	 }
	
	
	 public Map<String, Object> validateAdditionalBeneDetails(BenificiaryListView beneficaryDetails,BranchRemittanceApplRequestModel requestApplModel) {
		 
		    BigDecimal beneficaryMasterId = beneficaryDetails.getBeneficaryMasterSeqId();
			BigDecimal beneficaryBankId = beneficaryDetails.getBankId();
			BigDecimal beneficaryBankBranchId = beneficaryDetails.getBranchId();
			BigDecimal beneAccNumSeqId = beneficaryDetails.getBeneficiaryAccountSeqId();
		
			
			BigDecimal routingCountry =requestApplModel.getRoutingCountryId();
			BigDecimal routingBank = requestApplModel.getRoutingBankId();
			BigDecimal routingBranch = requestApplModel.getRoutingBankBranchId();
			BigDecimal serviceMasterId = requestApplModel.getServiceMasterId();
			
			
			BigDecimal applicationCountryId = beneficaryDetails.getApplicationCountryId();
			BigDecimal currencyId =beneficaryDetails.getCurrencyId();
			BigDecimal remitMode = requestApplModel.getRemittanceModeId();
			BigDecimal deliveryMode = requestApplModel.getDeliveryModeId();
			BigDecimal beneficaryRelationSeqId = beneficaryDetails.getBeneficiaryRelationShipSeqId();
			
			Map<String, Object> inputValues = new HashMap<>();
			inputValues.put("P_BENEFICIARY_MASTER_ID", beneficaryMasterId);
			inputValues.put("P_BENEFICIARY_BANK_ID", beneficaryBankId);
			inputValues.put("P_BENEFICIARY_BRANCH_ID", beneficaryBankBranchId);
			inputValues.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneAccNumSeqId);
			inputValues.put("P_ROUTING_COUNTRY_ID", routingCountry);
			inputValues.put("P_ROUTING_BANK_ID", routingBank);
			inputValues.put("P_ROUTING_BANK_BRANCH_ID", routingBranch);
			inputValues.put("P_SERVICE_MASTER_ID", serviceMasterId);
			inputValues.put("P_APPLICATION_COUNTRY_ID", applicationCountryId);
			inputValues.put("P_CURRENCY_ID", currencyId);
			inputValues.put("P_REMITTANCE_MODE_ID", remitMode);
			inputValues.put("P_DELIVERY_MODE_ID", deliveryMode);
			inputValues.put("P_BENE_RELATION_SEQ_ID", beneficaryRelationSeqId);
		 
		 Map<String, Object> outPut = applProcedureDao.toFetchDetilaFromAddtionalBenficiaryDetails(inputValues);
			if(outPut!=null && outPut.get("P_ERROR_MESSAGE")!=null){
				throw new GlobalException(JaxError.BENE_ADD_CHECK_ERROR, outPut.get("P_ERROR_MESSAGE").toString());
			}
			
			return outPut;
	 }
	 
	 
	
	 @SuppressWarnings("unchecked")
	 public void validateAdditionalErrorMessages(Map<String ,Object> hashMap) {
		 	BranchRemittanceApplRequestModel applRequestModel = (BranchRemittanceApplRequestModel)hashMap.get("APPL_REQ_MODEL");
			
			
			BenificiaryListView beneDetails  =(BenificiaryListView) hashMap.get("BENEFICIARY_DETAILS");
			//RoutingResponseDto branchRoutingDto = (RoutingResponseDto)hashMap.get("ROUTING_DETAILS_DTO");
			RemittanceTransactionRequestModel requestModel = new RemittanceTransactionRequestModel();
			requestModel.setAdditionalBankRuleFiledId(applRequestModel.getAdditionalBankRuleFiledId());
			
			remitApplParametersMap.put("P_APPLICATION_COUNTRY_ID", beneDetails.getApplicationCountryId());
			remitApplParametersMap.put("P_ROUTING_COUNTRY_ID",applRequestModel.getRoutingCountryId());
			remitApplParametersMap.put("P_ROUTING_BANK_ID",applRequestModel.getRoutingBankId());
			remitApplParametersMap.put("P_FOREIGN_CURRENCY_ID",beneDetails.getCurrencyId());
			remitApplParametersMap.put("P_REMITTANCE_MODE_ID",applRequestModel.getRemittanceModeId());
			remitApplParametersMap.put("P_DELIVERY_MODE_ID",applRequestModel.getDeliveryModeId());
			validateAdditionalErrorMessages(applRequestModel);
	 }
	 
	
	public BigDecimal generateDocumentNumber(BigDecimal applCountryId, BigDecimal companyId,BigDecimal documentId, BigDecimal financialYear, String processIn, BigDecimal branchId) {
		Map<String, Object> output = applProcedureDao.getDocumentSeriality(applCountryId, companyId, documentId,financialYear, processIn, branchId);
		return (BigDecimal) output.get("P_DOC_NO");
	}	
	
	
	
	
	
	public void validateAdditionalErrorMessages(BranchRemittanceApplRequestModel requestModel) {
		remitApplParametersMap.put("P_FURTHER_INSTR", "URGENT");
		Map<String, Object> errorResponse = applProcedureDao.toFetchPurtherInstractionErrorMessaage(remitApplParametersMap);
		String errorMessage = (String) errorResponse.get("P_ERRMSG");
		Map<String, Object> furtherSwiftAdditionalDetails = applProcedureDao.fetchAdditionalBankRuleIndicators(remitApplParametersMap);
		remitApplParametersMap.putAll(furtherSwiftAdditionalDetails);
		remitApplParametersMap.put("P_ADDITIONAL_BANK_RULE_ID_1", requestModel.getAdditionalBankRuleFiledId());
		
		if (requestModel.getPurposeOfTrnxId() != null) {
			BigDecimal srlId = requestModel.getPurposeOfTrnxId();
			logger.info("Srl Id received: " + srlId);
			BigDecimal bankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
			BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
			BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
			logger.info("bankId: " + bankId + "remittanceModeId: " + remittanceModeId + "deliveryModeId "
					+ deliveryModeId + " foreignCurrencyId: " + foreignCurrencyId);
			//AdditionalBankDetailsViewx additionaBnankDetail = bankService.getAdditionalBankDetail(srlId,foreignCurrencyId, bankId, remittanceModeId, deliveryModeId);
			
			AdditionalBankRuleAmiec additionaBnankDetail = applDataManager.getBankRuleAmiecDescription(requestModel.getPurposeOfTrnxId());
			
			
			if (additionaBnankDetail != null) {
				//logger.info("additionaBnankDetail getServiceApplicabilityRuleId: "+ additionaBnankDetail.getAdditionalBankFieldId().get);
				remitApplParametersMap.put("P_AMIEC_CODE_1", additionaBnankDetail.getAmiecCode());
				remitApplParametersMap.put("P_FLEX_FIELD_VALUE_1", additionaBnankDetail.getAmiecDescription());
				remitApplParametersMap.put("P_FLEX_FIELD_CODE_1", additionaBnankDetail.getFlexField());
			}
		}
		if (remitApplParametersMap.get("P_ADDITIONAL_BANK_RULE_ID_1") == null) {
			errorMessage = "Additional Field required by bank not set";
		}
		if (StringUtils.isNotBlank(errorMessage)) {
			throw new GlobalException(JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL, errorMessage);
		}
	}

	

	
	
	public BigDecimal getSelectedCurrency(BigDecimal foreignCurrencyId,BranchRemittanceApplRequestModel requestApplModel) {
		if (JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getForeignAmount())) {
			return foreignCurrencyId;
		}
		return metaData.getDefaultCurrencyId();
	}
	
	public BigDecimal getSelectedCurrencyAmount(BigDecimal selectedCurrency,BranchRemittanceApplRequestModel requestApplModel) {
		if (JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getForeignAmount())){
			return requestApplModel.getForeignAmount();
		}else {
			return requestApplModel.getLocalAmount();
		}
		
	}
	
	
	public String getCustomerType(BigDecimal componentDataId) {
	 BizComponentData bizComData  =bizCompDao.getBizComponentDataByComponmentDataId(componentDataId);
	 String customerType = ConstantDocument.Individual;
	 if(bizComData!=null) {
		 customerType = bizComData.getComponentCode();
	 }else {
		 throw new GlobalException(JaxError.INVALID_BENE_BANK, " Customer type is not defined "+componentDataId);
	 }
		return customerType;
	}
	
	private BeneficiaryListDTO convertBeneModelToDto(BenificiaryListView beneModel) {
		BeneficiaryListDTO dto = new BeneficiaryListDTO();
		try {
			BeanUtils.copyProperties(dto, beneModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("bene list display", e);
		}
		return dto;
	}
	
  
  public List<AdditionalExchAmiecDto> getPurposeOfTrnx(BigDecimal beneRelId,BigDecimal routingCountryId){
	  BenificiaryListView beneficaryDetails =beneficiaryRepository.findBybeneficiaryRelationShipSeqId(beneRelId);
	  List<AdditionalBankRuleAmiec> amiecRuleMap  = null;
	  CountryMaster cntMaster = new CountryMaster();
		if(beneficaryDetails==null) {
			throw new GlobalException(JaxError.BENEFICIARY_LIST_NOT_FOUND,"Beneficairy not found "+beneRelId);
			
		}	
	List<AdditionalExchAmiecDto> purposeofTrnx = new ArrayList<>();
	
	if(JaxUtil.isNullZeroBigDecimalCheck(routingCountryId)) {
		cntMaster.setCountryId(routingCountryId);
	}
	
	if(cntMaster!=null && JaxUtil.isNullZeroBigDecimalCheck(cntMaster.getCountryId())) { 
		amiecRuleMap = amiecBankRuleRepo.getPurposeOfTrnxByCountryId(cntMaster);
	}
	  
	  if(amiecRuleMap != null && amiecRuleMap.size() != 0) {
		 return convertPurposeOfTrnxDto(amiecRuleMap);
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found");
		}
  }
  
  public List<AdditionalExchAmiecDto> convertPurposeOfTrnxDto(List<AdditionalBankRuleAmiec> amiecRuleMap ) {
	  List<AdditionalExchAmiecDto>  dto = new ArrayList<>();
		
	  	for(AdditionalBankRuleAmiec amiec : amiecRuleMap) {
	  		AdditionalExchAmiecDto amiecDto = new AdditionalExchAmiecDto();
	  		amiecDto.setResourceId(amiec.getAdditionalBankRuleDetailId());
	  		amiecDto.setResourceCode(amiec.getAmiecCode());
	  		amiecDto.setResourceName(amiec.getAmiecDescription().toUpperCase());
	  		amiecDto.setFlexField(amiec.getFlexField());
	  		amiecDto.setCountryId(amiec.getCountryId().getCountryId());
	  		amiecDto.setAdditionalBankFieldId(amiec.getAdditionalBankFieldId().getAdditionalBankRuleId());
	  		dto.add(amiecDto);
	  	}
	  
		return dto;
	}
  
	 
public void validateTrnxLimitCheck(ExchangeRateBreakup breakup,BigDecimal commission,BenificiaryListView beneficaryDetails) {
 CurrencyMasterModel beneCurrencyMaster = currencyDao.findOne(beneficaryDetails.getCurrencyId()); 
	BigDecimal decimalCurrencyValue = beneCurrencyMaster.getDecinalNumber();
	String currencyQuoteName = beneficaryDetails.getCurrencyQuoteName();
if (commission == null) {
		Map<String, Object> commissionRangeMap = remitTrnxManager.getCommissionRange(breakup);
		String msg = "";
		BigDecimal fromAmount = BigDecimal.ZERO;
		BigDecimal toAmount = BigDecimal.ZERO;
		BigDecimal fcAmount = RoundUtil.roundBigDecimal(breakup.getConvertedFCAmount(),decimalCurrencyValue.intValue());
		if (commissionRangeMap.get("FROM_AMOUNT") != null || commissionRangeMap.get("TO_AMOUNT") != null) {
			fromAmount = (BigDecimal) commissionRangeMap.get("FROM_AMOUNT");
			toAmount = (BigDecimal) commissionRangeMap.get("TO_AMOUNT");
			if (fcAmount.compareTo(fromAmount) < 0) {
				msg = "Amount to be remitted, cannot be lesser than " + currencyQuoteName + " " + fromAmount
						+ ".Please increase the amount to be remitted.";
			} else if (fcAmount.compareTo(toAmount) > 0) {
				msg = "Amount to be remitted, exceeds the permissible limit .Please decrease the amount to be remitted to less than "
						+ currencyQuoteName + " " + toAmount + ".";
			}
		}
		if (!StringUtils.isBlank(msg)) {
			throw new GlobalException(JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL, msg);
		}

	}
}	

/** EX_GET_ADDL_BENE_DETAILS **/
public BeneAdditionalDto getAdditionalBeneDetailJax(BenificiaryListView beneficaryDetails,BranchRemittanceApplRequestModel requestApplModel){
	 BeneAdditionalDto beneAddDto = new BeneAdditionalDto();
	 
	 String langInd=null;
	 String beneName=null;
	 String engBeneName =null;
	 String arabicBeneName =null;
	 String beneFirstName =null;
	 String beneSecondName=null;
	 String beneFirstNameLocal =null;
	 String beneSecondNameLocal=null;
	 
	 String beneThirdName=null;
	 String beneFourthName=null;
	 String beneFifthName=null;
	
	 String beneBranchName =null;
	 String beneBankName=null; 
	 String beneBankNameArabic=null; 
	 String beneBankCode=null; 
	 String beneCityName = null;
	 String beneCityNameArabic = null;
	 String routingCityName = null;
	 String routingCityNameArabic = null;
	 String beneBranchFullName=null;
	 String beneBranchFullNameArabic=null;
	 String branchShortName=null;
	 String branchCode=null;
	 BigDecimal routingStateId = null;
	 BigDecimal beneStateId = null;
	 BigDecimal routingDistrictId = null;
	 BigDecimal beneDistrictId = null;
	 BigDecimal routingCityId = null;
	 BigDecimal benecityId = null;
	 String swiftBic = null;
	 BigDecimal bnfBankLangInd= BigDecimal.ONE;
	 BigDecimal bnfBankBranchLangInd= BigDecimal.ONE;
	 String bankName = null;
	 String bankBranchName = null;
	
	 

	
	 BigDecimal routingCountryId = requestApplModel.getRoutingCountryId();
	 BigDecimal routingBankId = requestApplModel.getRoutingBankId();
	 BigDecimal routingBranchId = requestApplModel.getRoutingBankBranchId();
	 
	 BigDecimal beneBankId = beneficaryDetails.getBankId();
	 BigDecimal beneBranchId = beneficaryDetails.getBranchId();
	 BigDecimal serviceMasterId = requestApplModel.getServiceMasterId();
	 
	 
	 BankMasterModel routingBankMasterModel = bankService.getBankById(routingBankId);
	 BankMasterModel beneBankMasterModel = bankService.getBankById(beneBankId);
	 BigDecimal applicationCountry = beneficaryDetails.getApplicationCountryId();
	 BigDecimal beneCountry = beneficaryDetails.getBenificaryCountry();
	 BigDecimal currencyId = beneficaryDetails.getCurrencyId();
	
	 BigDecimal remittanceModeId = requestApplModel.getRemittanceModeId();
	 BigDecimal deliveryModeId = requestApplModel.getDeliveryModeId();
	 
	 /** GET THE SERVICE APPL RULE  FOR BANK **/
	 ServiceApplicabilityRule serviceApplRuleforBank =serviceApplRule.getServiceApplicabilityRulesForBank(applicationCountry, beneCountry, currencyId, ConstantDocument.BNFBANK, remittanceModeId, deliveryModeId);
	 
	 /** GET THE SERVICE APPL RULE  FOR BRANCH **/
	 ServiceApplicabilityRule serviceApplRuleforBranch =serviceApplRule.getServiceApplicabilityRulesForBranchAndSwift(applicationCountry, beneCountry, currencyId, ConstantDocument.BNFBRCH, remittanceModeId, deliveryModeId);
	 
	 /** GET THE SERVICE APPL RULE  FOR SWIFT BANK **/
	 ServiceApplicabilityRule serviceApplRuleforBeneSwiftBank =serviceApplRule.getServiceApplicabilityRulesForBranchAndSwift(applicationCountry, routingCountryId, currencyId, ConstantDocument.BNFBANK_SWIFT, remittanceModeId, deliveryModeId);
		
	
	 
	/** GET THE ROUTING BANK AND BRANCH NAME **/
	 List<BankBranchView> routingBankBranchView = bankBranchRepo.getBankBranch(routingBankId,routingBranchId);
	
	 /**  GET THE BENEFICARY BANK AND BRANCH NAME **/
	 List<BankBranchView> beneBankBranchView = bankBranchRepo.getBankBranch(beneBankId,beneBranchId);
	 
	 
	 
	if(routingBankMasterModel==null) {
		throw new GlobalException(JaxError.INVALID_ROUTING_BANK, "Invalid  bank");
	}else {
		langInd = routingBankMasterModel.getLanguageInd();
	}
	
	
	if(beneBankMasterModel==null) {
		throw new GlobalException(JaxError.INVALID_ROUTING_BANK, "Invalid  bank");
	}else {
		beneBankName = beneBankMasterModel.getBankFullName();
		beneBankNameArabic = beneBankMasterModel.getBankFullNameAr();
		beneBankCode = beneBankMasterModel.getBankCode();
	}
	
	if(!StringUtils.isBlank(langInd) && langInd.equalsIgnoreCase(ConstantDocument.L_ARAB)) {
		langInd=ConstantDocument.L_ARAB;
		arabicBeneName = beneficaryDetails.getArbenificaryName();
	}else {
		langInd=ConstantDocument.L_ENG;
		engBeneName = beneficaryDetails.getBenificaryName();
	}
	 
	
	
	
	
	if(langInd.equalsIgnoreCase(ConstantDocument.L_ENG)){
		if(StringUtils.isBlank(engBeneName)) {
				throw new GlobalException(JaxError.BENE_ENGLISH_NAME_REQUIRED, "English name not available for beneficiary");
			}else {
				beneAddDto.setBeneName(engBeneName);
				beneFirstName =beneficaryDetails.getFirstName();
				beneSecondName =beneficaryDetails.getSecondName();
				beneThirdName  =beneficaryDetails.getThirdName();
				beneFourthName =beneficaryDetails.getFourthName();
				beneFifthName  =beneficaryDetails.getFiftheName();
				
				if(beneficaryDetails!=null && StringUtils.isBlank(beneFirstName)) {
					throw new GlobalException(JaxError.BENE_ENGLISH_NAME_REQUIRED, "English first name not available for beneficiary");
				}else {
					beneFirstName = beneficaryDetails.getFirstName();
				}
				
				if(beneficaryDetails!=null && StringUtils.isBlank(beneSecondName)) {
					throw new GlobalException(JaxError.BENE_ENGLISH_NAME_REQUIRED, "English second name or last  not available for beneficiary");
				}else {
					beneSecondName = beneficaryDetails.getSecondName();
				}
				beneAddDto.setBeneFirstName(beneFirstName);
				beneAddDto.setBeneSecondName(beneSecondName);
				beneAddDto.setBeneThirdName(beneThirdName);
				beneAddDto.setBeneFourthName(beneFourthName);
				beneAddDto.setBeneFifthName(beneFifthName);
			}
	
	}	
	if(langInd.equalsIgnoreCase(ConstantDocument.L_ARAB)) {
		if(StringUtils.isBlank(arabicBeneName)) {
			throw new GlobalException(JaxError.BENE_ARABIC_NAME_REQUIRED, "Arabic name not available for beneficiary");
			}else {
			beneAddDto.setBeneName(arabicBeneName);
			beneFirstNameLocal =beneficaryDetails.getFirstNameLocal();
			beneSecondNameLocal =beneficaryDetails.getSecondNameLocal();
			beneThirdName  =beneficaryDetails.getThirdNameLocal();
			beneFourthName =beneficaryDetails.getFourthNameLocal();
			beneFifthName  =beneficaryDetails.getFifthNameLocal();
			
			if(beneficaryDetails!=null && StringUtils.isBlank(beneFirstNameLocal)) {
				throw new GlobalException(JaxError.BENE_ARABIC_NAME_REQUIRED, "Arabic first name not available for beneficiary");
			}
			
			if(beneficaryDetails!=null && StringUtils.isBlank(beneSecondNameLocal)) {
				throw new GlobalException(JaxError.BENE_ARABIC_NAME_REQUIRED, "Arabic second name or last  not available for beneficiary");
			}
			beneAddDto.setBeneFirstName(beneFirstNameLocal);
			beneAddDto.setBeneSecondName(beneSecondNameLocal);
			beneAddDto.setBeneThirdName(beneThirdName);
			beneAddDto.setBeneFourthName(beneFourthName);
			beneAddDto.setBeneFifthName(beneFifthName);
		}
	}	
	
	
	if(routingBankBranchView==null || routingBankBranchView.isEmpty()) {
		throw new GlobalException(JaxError.INVALID_ROUTING_BANK, "Invalid bank branch for routing bank "+routingBankId +"\t routing branch "+routingBranchId);
	}
	
	if(beneBankBranchView==null || beneBankBranchView.isEmpty()) {
		throw new GlobalException(JaxError.INVALID_BENE_BANK, "Invalid bank branch for bene bank "+routingBankId +"\t bene branch "+routingBranchId);
	}
	
	
	
	/** to get bene City eng and arabic name **/
	if(beneBankBranchView!=null && !beneBankBranchView.isEmpty() && beneBankBranchView.size()==1) {
		if(beneBankBranchView.get(0)!=null) {
		BigDecimal beneCityId = beneBankBranchView.get(0).getCityId();
		if(JaxUtil.isNullZeroBigDecimalCheck(beneCityId)) {
			List<ViewCity> viewCityList= cityRespository.findByCityMasterId(beneCityId);
			if(viewCityList!=null && !viewCityList.isEmpty()) {
				for(ViewCity vc: viewCityList) {
					if(JaxUtil.isNullZeroBigDecimalCheck(vc.getLanguageId()) && vc.getLanguageId().compareTo(BigDecimal.ONE)==0) {
						beneCityName = vc.getCityName()==null?"":vc.getCityName();
					}
					if(JaxUtil.isNullZeroBigDecimalCheck(vc.getLanguageId()) && vc.getLanguageId().compareTo(new BigDecimal(2))==0) {
						beneCityNameArabic = vc.getCityName()==null?"":vc.getCityName();
					}
				}
				
			}
		}
	}
	}
	
	
	/** to get routing  City eng and arabic name **/
	if(routingBankBranchView!=null && !routingBankBranchView.isEmpty() && routingBankBranchView.size()==1) {
		if(routingBankBranchView.get(0)!=null) {
		BigDecimal routingCityid = routingBankBranchView.get(0).getCityId();
		if(JaxUtil.isNullZeroBigDecimalCheck(routingCityid)) {
			List<ViewCity> viewCityList= cityRespository.findByCityMasterId(routingCityid);
			if(viewCityList!=null && !viewCityList.isEmpty()) {
				for(ViewCity vc: viewCityList) {
					if(JaxUtil.isNullZeroBigDecimalCheck(vc.getLanguageId()) && vc.getLanguageId().compareTo(BigDecimal.ONE)==0) {
						routingCityName = vc.getCityName();
					}
					if(JaxUtil.isNullZeroBigDecimalCheck(vc.getLanguageId()) && vc.getLanguageId().compareTo(new BigDecimal(2))==0) {
						routingCityNameArabic = vc.getCityName();
					}
				}
				
			}
		}
		}
	}
	
	if(serviceApplRuleforBeneSwiftBank!=null && serviceApplRuleforBeneSwiftBank.getMandatory() !=null && serviceApplRuleforBeneSwiftBank.getMandatory().equalsIgnoreCase(ConstantDocument.Yes)) {
		swiftBic= beneficaryDetails.getSwiftBic();
		if(StringUtils.isBlank(swiftBic)) {
			if(beneBankBranchView!=null && !beneBankBranchView.isEmpty() && beneBankBranchView.get(0)!=null) {
				swiftBic = beneBankBranchView.get(0).getSwift();
			}
		}
		if(StringUtils.isBlank(swiftBic)) {
			throw new GlobalException(JaxError.BANK_SWIFT_EMPTY, " Beneficiary swift is mandatory  "+beneBankName +"\t bene branch "+beneBranchFullName);
		}
		
	}
	
	
	if(serviceApplRuleforBank!=null) {
		bnfBankLangInd = serviceApplRuleforBank.getLanguageId();
	}
	if(serviceApplRuleforBranch!=null) {
		bnfBankBranchLangInd = serviceApplRuleforBranch.getLanguageId();
	}
	/** IF SERVICE ID IS CASH OR DD THEN THEN BANK AND BRANCH NAME SHOULD BE SAME AS THE ROUTING BANK AND BRANCH NAME **/
	
	if(serviceMasterId.compareTo(ConstantDocument.SERVICE_MASTER_ID_CASH)==0 || serviceMasterId.compareTo(ConstantDocument.SERVICE_MASTER_ID_DD)==0) {
		
		if(!StringUtils.isBlank(langInd) && langInd.equalsIgnoreCase(ConstantDocument.L_ARAB) && bnfBankLangInd != null && bnfBankLangInd.compareTo(new BigDecimal(2))==0) {
			bankName = routingBankMasterModel.getBankFullNameAr();
			if(!StringUtils.isBlank(routingCityNameArabic)) {
				bankBranchName =routingBankBranchView.get(0)==null?"":routingBankBranchView.get(0).getBranchFullNameArabic()+","+ routingCityNameArabic==null?"":routingCityNameArabic;
			}else {
				bankBranchName =routingBankBranchView.get(0)==null?"":routingBankBranchView.get(0).getBranchFullNameArabic();
			}
		}else { //If eng
			bankName = routingBankMasterModel.getBankFullName();
			if(!StringUtils.isBlank(routingCityName)) {
				bankBranchName =routingBankBranchView.get(0)==null?"":routingBankBranchView.get(0).getBranchFullName()+","+ routingCityName==null?"":routingCityName;
			}else {
				bankBranchName =routingBankBranchView.get(0)==null?"":routingBankBranchView.get(0).getBranchFullName();
			}
		}
	}else { /** other than cash or DD routingBankMasterModel **/
		/** for Bank  **/
		if(serviceApplRuleforBank!=null && serviceApplRuleforBank.getValidate()!=null && serviceApplRuleforBank.getValidate().equalsIgnoreCase(ConstantDocument.Yes)) {
			List<ViewBnkFlexVal>  bankFlexField = viewBankFlxValRepository.findByBnkcodeAndFileNameAndBeneBankCode(routingBankMasterModel.getBankCode(), ConstantDocument.BNFBANK, beneBankMasterModel.getBankCode());
			
			if(bankFlexField!=null && !bankFlexField.isEmpty()) {
				if(bankFlexField.size()>1) {
					throw new GlobalException(JaxError.INVALID_ROUTING_BANK, "Too many rows for this combination in bnfbank for flex rules "+beneBankMasterModel.getBankCode());
				}else {
					bankName = bankFlexField.get(0).getBankExchId()==null?"":bankFlexField.get(0).getBankExchId() +","+beneBankName;
				}
			}else {
				throw new GlobalException(JaxError.INVALID_ROUTING_BANK, "No data found in Flex fields For this Bene bank "+beneBankMasterModel.getBankCode());
			}
			}else {
				 if(!StringUtils.isBlank(langInd) && langInd.equalsIgnoreCase(ConstantDocument.L_ARAB) && bnfBankLangInd!=null && bnfBankLangInd.compareTo(new BigDecimal(2))==0) {
					 bankName = beneBankMasterModel.getBankFullNameAr(); 
				 }else {
					 bankName = beneBankMasterModel.getBankFullName();
				 }
				
		  }
		
		/** for Branch **/
		if(serviceApplRuleforBranch!=null && serviceApplRuleforBranch.getValidate()!=null && serviceApplRuleforBranch.getValidate().equalsIgnoreCase(ConstantDocument.Yes)) {
			
			//List<ViewBnkFlexVal>  bankFlexField = viewBankFlxValRepository.findByBnkcodeAndFileNameAndBeneBankCode(routingBankMasterModel.getBankCode(), ConstantDocument.BNFBRCH, beneBankMasterModel.getBankCode());
			
			List<ViewBnkFlexVal>  bankFlexField = viewBankFlxValRepository.findByBnkcodeAndFileNameAndBeneBankCodeAndBeneBranchCode(routingBankMasterModel.getBankCode(), ConstantDocument.BNFBRCH, beneBankMasterModel.getBankCode(),beneBankBranchView.get(0).getBranchCode());
			
			if(bankFlexField!=null && !bankFlexField.isEmpty()) {
				if(bankFlexField.size()>1) {
					throw new GlobalException(JaxError.INVALID_ROUTING_BANK, "Too many rows for this combination in  Bene bank and branch for flex rules "+beneBankMasterModel.getBankCode());
				}else {
					bankBranchName =bankFlexField.get(0).getBankExchId()==null?"": bankFlexField.get(0).getBankExchId()+" ,"+beneBranchName==null?"":beneBranchName;
				}
			}else {
				throw new GlobalException(JaxError.INVALID_ROUTING_BANK, "No data found in Flex fields For this  Bene bank and branch "+beneBankMasterModel.getBankCode());
			}
			}else {
				 if(!StringUtils.isBlank(langInd) && langInd.equalsIgnoreCase(ConstantDocument.L_ARAB) && bnfBankBranchLangInd !=null && bnfBankBranchLangInd.compareTo(new BigDecimal(2))==0) {
					 bankBranchName = beneBankBranchView.get(0).getBranchFullNameArabic()+","+beneCityNameArabic==null?"":beneCityNameArabic; 
				 }else {
					 if(beneBankBranchView.get(0)!=null) {
						 bankBranchName = beneBankBranchView.get(0).getBranchFullName()==null?"":beneBankBranchView.get(0).getBranchFullName()+" ,"+beneCityName==null?"":beneCityName;
					 }
				 }
			}
	}
	
	
	beneAddDto.setBeneBankName(bankName);
	beneAddDto.setBeneBranchName(bankBranchName);
	/*beneAddDto.setBeneFirstName(beneFirstName);
	beneAddDto.setBeneSecondName(beneSecondName);
	beneAddDto.setBeneThirdName(beneThirdName);
	beneAddDto.setBeneFourthName(beneFourthName);
	beneAddDto.setBeneFifthName(beneFifthName);*/
	
	if(beneBankBranchView!=null && !beneBankBranchView.isEmpty()) {
		if(beneBankBranchView.get(0)!=null && beneBankBranchView.get(0).getStateId()!=null) {
		beneAddDto.setStateId(beneBankBranchView.get(0).getStateId());
		}if(beneBankBranchView.get(0)!=null && beneBankBranchView.get(0).getDistrictId()!=null) {
			beneAddDto.setDistrictId(beneBankBranchView.get(0).getDistrictId());
		}
		if(beneBankBranchView.get(0)!=null && beneBankBranchView.get(0).getCityId()!=null) {
		beneAddDto.setCityId(beneBankBranchView.get(0).getCityId());
		}
	}
	
	 return beneAddDto;
}




}	

