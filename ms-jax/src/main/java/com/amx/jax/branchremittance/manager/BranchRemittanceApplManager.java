package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;
import java.sql.ClientInfoStatus;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author rabil 
 * @date 21/01/2019
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.branchremittance.dao.BranchRemittanceDao;
import com.amx.jax.branchremittance.service.BranchRemittanceExchangeRateService;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.BankBranch;
import com.amx.jax.dbmodel.remittance.DeliveryMode;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceModeMaster;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.RemittanceApplicationAdditionalDataManager;
import com.amx.jax.manager.RemittanceApplicationManager;
import com.amx.jax.manager.remittance.RemittanceAdditionalFieldManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.remittance.AmlCheckResponseDto;
import com.amx.jax.model.response.remittance.BranchExchangeRateBreakup;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerShoppingCartDto;
import com.amx.jax.model.response.remittance.RemittanceDeclarationReportDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.repository.BranchSystemDetailRepository;
import com.amx.jax.repository.DeviceStateRepository;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.IDeviceRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.service.LoyalityPointService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.RemittanceTransactionRequestValidator;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceApplManager {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	BranchRemittanceManager branchRemitManager;
	
	@Autowired
	IDocumentDao documentDao;
	
	@Autowired
	IApplicationCountryRepository applCountryRepos;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	BankMetaService bankMetaService;

	@Autowired
	FinancialService finanacialService;
	
	@Autowired
	ICurrencyDao currDao;
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;
	
	@Autowired
	RemittanceApplicationManager remitApplManager;
	
	@Autowired
	EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	BeneficiaryService beneficiaryService;
	
	//@Autowired
	//RemittanceTransactionManager remittanceTxnManger;
	
	@Autowired
	private CustomerDao custDao;
	
	@Autowired
	BankService bankService;
	
	@Resource
	private Map<String, Object> remitApplParametersMap;
	
	@Autowired
	BranchRemittanceDao brRemittanceDao;
	
	@Autowired
	BranchRemittancePaymentManager  branchRemittancePaymentManager; 
	
	@Autowired
	LoyalityPointService loyalityPointService;
	
	@Autowired
	RemittanceApplicationDao remittApplDao;
	

	@Autowired
	RemittanceApplicationAdditionalDataManager remittanceAppAddlDataManager;
	
	@Autowired
	BranchRoutingManager branchRoutingManager;
	
	@Autowired
	BranchRemittanceExchangeRateService branchExchRateService;
	
	@Autowired
	RemittanceTransactionRequestValidator remittanceTransactionRequestValidator;
	
	@Autowired
	RemittanceAdditionalFieldManager remittanceAdditionalFieldManager;
	
	@Autowired
	DeviceStateRepository deviceStateRepository;
	
	@Autowired
	private BranchSystemDetailRepository branchSystemDetailRepository;
	
	@Autowired
	IDeviceRepository deviceRepository;


	
	public BranchRemittanceApplResponseDto saveBranchRemittanceApplication(BranchRemittanceApplRequestModel requestApplModel) {
		Map<String,Object> hashMap = new HashMap<>();
		
		/*To fetch customer details **/
		 Customer customer = custDao.getCustById(metaData.getCustomerId());
		/** To fetch bene details **/
		 BenificiaryListView beneficaryDetails =getBeneDetails(requestApplModel);
		/*checkingStaffIdNumberWithCustomer **/
		 branchRemitManager.checkingStaffIdNumberWithCustomer();
		 /*checking bene account exception  **/
		 branchRemitManager.beneAccountException(beneficaryDetails);
		 /*checking bene account type **/
		 branchRemitManager.checkBeneAccountType(beneficaryDetails);
		 /*checking bene additional info missing detail check**/
		 branchRemitManager.beneAddCheck(beneficaryDetails);
		 /*checking banned bank details **/
		 branchRemitManager.bannedBankCheck(beneficaryDetails);
		 /* validate blck list bene **/
		 branchRemitManager.validateBlackListedBene(beneficaryDetails);
		 /* get Routing setup details **/
		 //Map<String, Object> branchRoutingDetails =branchRemitManager.getRoutingSetupDeatils(beneficaryDetails);
		 RoutingResponseDto branchRoutingDto= branchRoutingManager.getRoutingSetup(requestApplModel);
		 
		 //logger.info("branchRoutingDetails :"+branchRoutingDetails.toString());
		 /* get exchange setup details **/
		 //Map<String, Object> branchExchangeRate =branchRemitManager.getExchangeRateForBranch(requestApplModel, branchRoutingDetails);
		 //Map<String, Object> branchExchangeRate =branchRemitManager.getExchangeRateForBranch(requestApplModel,branchRoutingDto);
		 
		 
		 //Priccing API
		 BranchRemittanceGetExchangeRateResponse exchangeRateResposne = branchExchRateService.getExchaneRate(requestApplModel).getResult();
		 
		 remittanceTransactionRequestValidator.validateExchangeRate(requestApplModel, exchangeRateResposne);
		 remittanceTransactionRequestValidator.validateFlexFields(requestApplModel, remitApplParametersMap);
		 remittanceAdditionalFieldManager.validateAdditionalFields(requestApplModel, remitApplParametersMap);
		 remittanceAdditionalFieldManager.processAdditionalFields(requestApplModel); 

		 
		 logger.debug("branchExchangeRate :"+exchangeRateResposne);
		 /* get aml cehck   details **/
		//List<AmlCheckResponseDto> amlList= branchRemitManager.amlTranxAmountCheckForRemittance(requestApplModel,branchExchangeRate);
		 
		 List<AmlCheckResponseDto> amlList= branchRemitManager.amlTranxAmountCheckForRemittance(requestApplModel,exchangeRateResposne);
		 
		 logger.info("amlList :"+amlList.toString());
		 /* additional check **/
		//branchRemitManager.validateAdditionalCheck(branchRoutingDetails,customer,beneficaryDetails,(BigDecimal)branchExchangeRate.get("P_LOCAL_NET_PAYABLE"));
		 
		 branchRemitManager.validateAdditionalCheck(branchRoutingDto,customer,beneficaryDetails,exchangeRateResposne.getExRateBreakup().getNetAmount());
		 
		 if(!JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getRoutingBankId())) {
			 requestApplModel.setRoutingBankId(branchRoutingDto.getRoutingBankDto().get(0).getRoutingBankId());
		 }
		 
		
		 
		/** bene additional check **/
		//Map<String, Object> addBeneDetails =branchRemitManager.validateAdditionalBeneDetails(branchRoutingDetails,branchExchangeRate,beneficaryDetails);
		 
		 Map<String, Object> addBeneDetails =branchRemitManager.validateAdditionalBeneDetails(branchRoutingDto,exchangeRateResposne,beneficaryDetails);
		 
		 
		
		
		//hashMap.put("ROUTING_DETAILS_MAP", branchRoutingDetails);
		
		
		hashMap.put("ROUTING_DETAILS_DTO", branchRoutingDto);
		hashMap.put("EXCH_RATE_MAP", exchangeRateResposne);
		hashMap.put("APPL_REQ_MODEL", requestApplModel);
		hashMap.put("BENEFICIARY_DETAILS", beneficaryDetails);
		hashMap.put("ADD_BENE_DETAILS", addBeneDetails);
		hashMap.put("CUSTOMER", customer);
		hashMap.put("AML_CHECK", amlList);

		branchRemitManager.validateAdditionalErrorMessages(hashMap);
		
		
		
		/* create applciation */
		RemittanceApplication remittanceApplication = this.createRemittanceApplication(hashMap);
		RemittanceAppBenificiary remittanceAppBeneficairy = this.createRemittanceAppBeneficiary(remittanceApplication,hashMap);
		List<AdditionalInstructionData>  additioalInstructionData = remittanceAppAddlDataManager.createAdditionalInstnDataForBranch(remittanceApplication,hashMap);
		
		//RemittanceTransactionRequestModel
		List<RemitApplAmlModel> amlData = this.saveRemittanceAppAML(remittanceApplication,hashMap);
		
		/* Saving application deatils */
		HashMap<String, Object> mapAllDetailApplSave = new HashMap<String, Object>();
		mapAllDetailApplSave.put("EX_APPL_TRNX", remittanceApplication);
		mapAllDetailApplSave.put("EX_APPL_BENE", remittanceAppBeneficairy);
		mapAllDetailApplSave.put("EX_APPL_ADDL", additioalInstructionData);
		mapAllDetailApplSave.put("EX_APPL_AML", amlData);
		brRemittanceDao.saveAllApplications(mapAllDetailApplSave);
		BranchRemittanceApplResponseDto applResponseDto = branchRemittancePaymentManager.fetchCustomerShoppingCart(customer.getCustomerId(),metaData.getDefaultCurrencyId());
		
		return applResponseDto;
	}

	@SuppressWarnings("unchecked")
	private RemittanceApplication createRemittanceApplication(Map<String,Object> hashMap){
		
		RemittanceApplication remittanceApplication = new RemittanceApplication();
		try {
			
			String signature =getCustomerSignature();

			if(!StringUtils.isBlank(signature)) {
				try {
				remittanceApplication.setCustomerSignatureClob(stringToClob(signature));
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else {
				throw new GlobalException(JaxError.CUSTOMER__SIGNATURE_UNAVAILABLE,"Customer signature required");
			}
			
			BranchRemittanceApplRequestModel applRequestModel = (BranchRemittanceApplRequestModel)hashMap.get("APPL_REQ_MODEL");
			//Map<String, Object> branchRoutingDetails =(HashMap)hashMap.get("ROUTING_DETAILS_MAP");
			
			RoutingResponseDto branchRoutingDto = (RoutingResponseDto)hashMap.get("ROUTING_DETAILS_DTO");
			//Map<String, Object> branchExchangeRate =(HashMap)hashMap.get("EXCH_RATE_MAP");
			
			BranchRemittanceGetExchangeRateResponse branchExchangeRate =(BranchRemittanceGetExchangeRateResponse)hashMap.get("EXCH_RATE_MAP");
			
			BenificiaryListView beneDetails  =(BenificiaryListView) hashMap.get("BENEFICIARY_DETAILS");
			BranchExchangeRateBreakup rateBreakUp = applRequestModel.getBranchExRateBreakup();
			
			
			
			BigDecimal routingCountryId = branchRoutingDto.getRoutingCountrydto().get(0).getResourceId();
			Customer customer = (Customer) hashMap.get("CUSTOMER");
			BigDecimal routingBankId = branchRoutingDto.getRoutingBankDto().get(0).getRoutingBankId();
			BigDecimal routingBankBranchId = (BigDecimal) branchRoutingDto.getRoutingBankBranchDto().get(0).getBankBranchId();
			BigDecimal foreignCurrencyId = beneDetails.getCurrencyId();
			BigDecimal deliveryId =branchRoutingDto.getDeliveryModeList().get(0).getDeliveryModeId(); //(BigDecimal) branchExchangeRate.get("P_DELIVERY_MODE_ID");
			BigDecimal remittanceId = applRequestModel.getRemittanceModeId();//(BigDecimal) branchExchangeRate.get("P_REMITTANCE_MODE_ID");
			
			BigDecimal selectedCurrencyId = branchRemitManager.getSelectedCurrency(foreignCurrencyId, applRequestModel);
			
			Document document = documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION).get(0);
			//BigDecimal selectedCurrency = getSelectedCurrency(foreignCurrencyId, requestModel);
			remittanceApplication.setExDocument(document);
			remittanceApplication.setDocumentCode(document.getDocumentCode());
			CountryMaster appCountryId = new CountryMaster();
			
			ApplicationSetup applSetup = getApplicationSetup();
			CurrencyMasterModel LocalCurrencyModel = getCurrencyByCountryId(applSetup.getApplicationCountryId());
			
			
			appCountryId.setCountryId(applSetup.getApplicationCountryId());
			remittanceApplication.setFsCountryMasterByApplicationCountryId(appCountryId);
			CurrencyMasterModel localCurrency = new CurrencyMasterModel();
			CurrencyMasterModel foreignCurrency = new CurrencyMasterModel();
			foreignCurrency.setCurrencyId(foreignCurrencyId);
			localCurrency.setCurrencyId(LocalCurrencyModel.getCurrencyId());
			remittanceApplication.setSourceofincome(applRequestModel.getSourceOfFund());
			remittanceApplication.setExCurrencyMasterByForeignCurrencyId(foreignCurrency);
			// commission currency
			remittanceApplication.setExCurrencyMasterByLocalCommisionCurrencyId(localCurrency);
			// local currency
			remittanceApplication.setExCurrencyMasterByLocalTranxCurrencyId(localCurrency);
			// local charge currency
			remittanceApplication.setExCurrencyMasterByLocalChargeCurrencyId(localCurrency);
			// net amt currency
			remittanceApplication.setExCurrencyMasterByLocalNetCurrencyId(localCurrency);
			remittanceApplication.setSpotRateInd(ConstantDocument.No);
			
			
		
			
			
			
			// company Id and code
			CompanyMaster companymaster = new CompanyMaster();
			companymaster.setCompanyId(applSetup.getCompanyId());
			remittanceApplication.setFsCompanyMaster(companymaster);
			ViewCompanyDetails companyDetails = companyService.getCompanyDetailsById(applSetup.getCompanyId());
			remittanceApplication.setCompanyCode(companyDetails.getCompanyCode());
			// branch id
			CountryBranch countryBranch = bankMetaService.getCountryBranchById(getEmployeeDetails().getCountryBranchId()); //user branch not customer branch
			remittanceApplication.setLoccod(countryBranch.getBranchId());
			remittanceApplication.setExCountryBranch(countryBranch);
			// fin year
			UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
			if(userFinancialYear!=null) {
			remittanceApplication.setExUserFinancialYearByDocumentFinanceYear(userFinancialYear);
			remittanceApplication.setTransactionFinancialyear(userFinancialYear.getFinancialYear());
			remittanceApplication.setDocumentFinancialyear(userFinancialYear.getFinancialYear());
			}else {
				throw new GlobalException(JaxError.NULL_FINANCIAL_YEAR,"Financial error is not defined");
			}
			
			// routing Country
			CountryMaster bencountrymaster = new CountryMaster();
			bencountrymaster.setCountryId(routingCountryId);
			remittanceApplication.setFsCountryMasterByBankCountryId(bencountrymaster);

			// Delivery Mode from service
			DeliveryMode deliverymode = new DeliveryMode();

			deliverymode.setDeliveryModeId(deliveryId);
			remittanceApplication.setExDeliveryMode(deliverymode);

			// RemittanceModeMaster to get Remittance
			RemittanceModeMaster remittancemode = new RemittanceModeMaster();
			remittancemode.setRemittanceModeId(remittanceId);
			remittanceApplication.setExRemittanceMode(remittancemode);

			// Customer id
			remittanceApplication.setFsCustomer(customer);
			remittanceApplication.setCustomerRef(customer.getCustomerReference());
			remittanceApplication.setCustomerName(getCustomerFullName(customer));

			// Routing Bank
			BankMasterModel bankmaster = new BankMasterModel();
			bankmaster.setBankId(routingBankId);
			remittanceApplication.setExBankMaster(bankmaster);

			// Routing Bank Branch
			BankBranch bankbranch = new BankBranch();
			bankbranch.setBankBranchId(routingBankBranchId);
			remittanceApplication.setExBankBranch(bankbranch);
			// document date
			remittanceApplication.setDocumentDate(new Date());
			remittanceApplication.setDebitAccountNo(beneDetails.getBankAccountNumber());
			
			// rates
			if(JaxUtil.isNullZeroBigDecimalCheck(rateBreakUp.getConvertedFCAmount())) {
				remittanceApplication.setForeignTranxAmount(rateBreakUp.getConvertedFCAmount());
			}else if(JaxUtil.isNullZeroBigDecimalCheck(branchExchangeRate.getExRateBreakup().getConvertedFCAmount())){
				remittanceApplication.setForeignTranxAmount(branchExchangeRate.getExRateBreakup().getConvertedFCAmount());
			}else {
				throw new GlobalException(JaxError.INVALID_FC_AMOUNT,"Invalid foreign Amount");
			}
			if(JaxUtil.isNullZeroBigDecimalCheck(rateBreakUp.getConvertedLCAmount())) {
				remittanceApplication.setLocalTranxAmount(rateBreakUp.getConvertedLCAmount());
			}else if(JaxUtil.isNullZeroBigDecimalCheck(branchExchangeRate.getExRateBreakup().getConvertedLCAmount())){
				remittanceApplication.setLocalTranxAmount(branchExchangeRate.getExRateBreakup().getConvertedLCAmount());
			}else {
				throw new GlobalException(JaxError.INVALID_FC_AMOUNT,"Invalid local Amount");
			}
			if(JaxUtil.isNullZeroBigDecimalCheck(rateBreakUp.getNetAmountWithoutLoyality())) {
				remittanceApplication.setLocalNetTranxAmount(rateBreakUp.getNetAmountWithoutLoyality());
			}else if(JaxUtil.isNullZeroBigDecimalCheck(branchExchangeRate.getExRateBreakup().getNetAmountWithoutLoyality())){
				remittanceApplication.setLocalNetTranxAmount(branchExchangeRate.getExRateBreakup().getNetAmountWithoutLoyality());
			}else {
				
				throw new GlobalException(JaxError.INVALID_FC_AMOUNT,"Invalid local Amount");
			}
			
			if(JaxUtil.isNullZeroBigDecimalCheck(rateBreakUp.getInverseRate())) {
				remittanceApplication.setExchangeRateApplied(rateBreakUp.getInverseRate());
			}else if(JaxUtil.isNullZeroBigDecimalCheck(branchExchangeRate.getExRateBreakup().getInverseRate())){
				remittanceApplication.setExchangeRateApplied(branchExchangeRate.getExRateBreakup().getInverseRate());
			}else {
			
				throw new GlobalException(JaxError.EXCHANGE_RATE_ERROR,"Invalid exchange rate");
			}
			
			
			remittanceApplication.setLocalCommisionAmount(branchExchangeRate.getTxnFee());
			remittanceApplication.setLocalChargeAmount(BigDecimal.ZERO);
			remittanceApplication.setLocalDeliveryAmount(BigDecimal.ZERO);
			remittanceApplication.setLoyaltyPointsEncashed(BigDecimal.ZERO); 
			
			BigDecimal loyalityPointsEncashed = BigDecimal.ZERO;
			if(applRequestModel.isAvailLoyalityPoints() && JaxUtil.isNullZeroBigDecimalCheck(customer.getLoyaltyPoints()) && customer.getLoyaltyPoints().compareTo(new BigDecimal(1000))>=0) {
				remittanceApplication.setLoyaltyPointInd(ConstantDocument.Yes);
				loyalityPointsEncashed = loyalityPointService.getVwLoyalityEncash().getEquivalentAmount();
				
			}else {
				remittanceApplication.setLoyaltyPointInd(ConstantDocument.No);
			}
			remittanceApplication.setLoyaltyPointsEncashed(loyalityPointsEncashed); 
			
		
			remittanceApplication.setDocumentFinancialyear(userFinancialYear.getFinancialYear());
			remittanceApplication.setSelectedCurrencyId(selectedCurrencyId);

			try {
				remittanceApplication.setAccountMmyyyy(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
			} catch (ParseException e) {
				logger.error("Error in saving application", e);
			}
			
			remittanceApplication.setCreatedBy(getEmployeeDetails().getUserName());
			remittanceApplication.setEmployeeId(getEmployeeDetails().getEmployeeId());
			remittanceApplication.setCreatedDate(new Date());
			remittanceApplication.setIsactive(ConstantDocument.Yes);
			remittanceApplication.setSourceofincome(applRequestModel.getSourceOfFund());
			remittanceApplication.setApplInd(ConstantDocument.COUNTER);
			BigDecimal documentNo = branchRemitManager.generateDocumentNumber(applSetup.getApplicationCountryId(), applSetup.getCompanyId(), ConstantDocument.DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION, userFinancialYear.getFinancialYear(), ConstantDocument.Update, countryBranch.getBranchId());
			if(JaxUtil.isNullZeroBigDecimalCheck(documentNo)) {
				remittanceApplication.setDocumentNo(documentNo);
			}else {
				throw new GlobalException(JaxError.INVALID_APPLICATION_DOCUMENT_NO,"Application document number shouldnot be null or blank");
			}			
			
			
			remittanceApplication.setWuIpAddress(metaData.getDeviceIp());
			remittanceApplication.setInstruction("URGENT");
			
			
			return remittanceApplication;
			
		}catch(GlobalException e){
			logger.error("create application", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}
		
		
	}
	
	private RemittanceAppBenificiary createRemittanceAppBeneficiary(RemittanceApplication remittanceApplication,Map<String, Object> hashMap) {

		logger.info(" Enter into saveRemittanceAppBenificary :");
		RemittanceAppBenificiary remittanceAppBenificary = new RemittanceAppBenificiary();
		BenificiaryListView beneficiaryDT  =(BenificiaryListView) hashMap.get("BENEFICIARY_DETAILS");
		String telNumber = beneficiaryService.getBeneficiaryContactNumber(beneficiaryDT.getBeneficaryMasterSeqId());
		Map<String,Object> beneAddDeatisl = (HashMap)hashMap.get("ADD_BENE_DETAILS");
		
		remittanceAppBenificary = new RemittanceAppBenificiary();

		// Document Id
		Document documentid = new Document();
		documentid.setDocumentID(remittanceApplication.getExDocument().getDocumentID());
		remittanceAppBenificary.setExDocument(documentid);

		// company Id
		CompanyMaster companymaster = new CompanyMaster();
		companymaster.setCompanyId(remittanceApplication.getFsCompanyMaster().getCompanyId());
		remittanceAppBenificary.setFsCompanyMaster(companymaster);

		// company code
		remittanceAppBenificary.setCompanyCode(remittanceApplication.getCompanyCode());

		// User Financial Year for Transaction
		UserFinancialYear userfinancialyear = new UserFinancialYear();
		userfinancialyear.setFinancialYearID(remittanceApplication.getExUserFinancialYearByDocumentFinanceYear().getFinancialYearID());
		remittanceAppBenificary.setExUserFinancialYear(userfinancialyear);

		// RemittanceApplication Id
		remittanceAppBenificary.setExRemittanceAppfromBenfi(remittanceApplication);
		remittanceAppBenificary.setDocumentCode(remittanceApplication.getDocumentCode());
		remittanceAppBenificary.setDocumentNo(remittanceApplication.getDocumentNo());
		remittanceAppBenificary.setBeneficiaryId(beneficiaryDT.getBeneficaryMasterSeqId());
		
		
		if (beneficiaryDT.getBankAccountNumber() != null) {
			//remittanceAppBenificary.setBeneficiaryAccountNo(beneficiaryDT.getBankAccountNumber());
			remittanceAppBenificary.setBeneficiaryAccountNo(getAccountNumber(beneficiaryDT));
		}
		
		remittanceAppBenificary.setBeneficiaryBank(beneficiaryDT.getBankName());
		remittanceAppBenificary.setBeneficiaryBranch(beneficiaryDT.getBankBranchName());
		
	  /*	remittanceAppBenificary.setBeneficiaryName(beneficiaryDT.getBenificaryName());
		remittanceAppBenificary.setBeneficiaryFirstName(beneficiaryDT.getFirstName());
		remittanceAppBenificary.setBeneficiarySecondName(beneficiaryDT.getSecondName());
		remittanceAppBenificary.setBeneficiaryThirdName(beneficiaryDT.getThirdName());
		remittanceAppBenificary.setBeneficiaryFourthName(beneficiaryDT.getFourthName());
		remittanceAppBenificary.setBeneficiaryFifthName(beneficiaryDT.getFiftheName());
		*/
		remittanceAppBenificary.setBeneficiaryName(beneAddDeatisl.get("P_BENEFICIARY_NAME")==null? beneficiaryDT.getBenificaryName():(String) beneAddDeatisl.get("P_BENEFICIARY_NAME"));
		remittanceAppBenificary.setBeneficiaryFirstName(beneAddDeatisl.get("P_BENEFICIARY_FIRST_NAME")==null?beneficiaryDT.getFirstName():(String) beneAddDeatisl.get("P_BENEFICIARY_FIRST_NAME"));
		remittanceAppBenificary.setBeneficiarySecondName(beneAddDeatisl.get("P_BENEFICIARY_SECOND_NAME")==null?beneficiaryDT.getSecondName():(String) beneAddDeatisl.get("P_BENEFICIARY_SECOND_NAME"));
		remittanceAppBenificary.setBeneficiaryThirdName(beneAddDeatisl.get("P_BENEFICIARY_THIRD_NAME")==null?beneficiaryDT.getThirdName():(String) beneAddDeatisl.get("P_BENEFICIARY_THIRD_NAME"));
		remittanceAppBenificary.setBeneficiaryFourthName(beneAddDeatisl.get("P_BENEFICIARY_FOURTH_NAME")==null?beneficiaryDT.getFourthName():(String) beneAddDeatisl.get("P_BENEFICIARY_FOURTH_NAME"));
		remittanceAppBenificary.setBeneficiaryFifthName(beneAddDeatisl.get("P_BENEFICIARY_FIFTH_NAME")==null?beneficiaryDT.getFiftheName():(String) beneAddDeatisl.get("P_BENEFICIARY_FIFTH_NAME"));
		
		
		if(beneficiaryDT.getSwiftBic()!=null) {
			remittanceAppBenificary.setBeneficiaryBankSwift(beneficiaryDT.getSwiftBic());
		}else {
			remittanceAppBenificary.setBeneficiaryBankSwift(bankService.getBranchSwiftCode(beneficiaryDT.getBankId(),beneficiaryDT.getBranchId()));
		}
		
	
		remittanceAppBenificary.setCreatedBy(remittanceApplication.getCreatedBy());
		remittanceAppBenificary.setCreatedDate(new Date());
		remittanceAppBenificary.setIsactive(ConstantDocument.Yes);
		remittanceAppBenificary.setBeneficiaryBranchStateId(beneficiaryDT.getStateId());
		remittanceAppBenificary.setBeneficiaryBranchDistrictId(beneficiaryDT.getDistrictId());
		remittanceAppBenificary.setBeneficiaryBranchCityId(beneficiaryDT.getCityId());
		
		remittanceAppBenificary.setBeneficiaryBankCountryId(beneficiaryDT.getBenificaryCountry());
		remittanceAppBenificary.setBeneficiaryBankId(beneficiaryDT.getBankId());
		remittanceAppBenificary.setBeneficiaryBankBranchId(beneficiaryDT.getBranchId());
		remittanceAppBenificary.setBeneficiaryAccountSeqId(beneficiaryDT.getBeneficiaryAccountSeqId());
		remittanceAppBenificary.setBeneficiaryRelationShipSeqId(beneficiaryDT.getBeneficiaryRelationShipSeqId());
		remittanceAppBenificary.setBeneficiaryTelephoneNumber(telNumber);

		logger.info(" Exit from saveRemittanceAppBenificary ");

		return remittanceAppBenificary;
	}
	
/*	public List<AdditionalInstructionData> createAdditionalInstnData(RemittanceApplication remittanceApplication,Map hashMap) {

		logger.info(" Enter into saveAdditionalInstnData ");

		BigDecimal applicationCountryId = metaData.getCountryId();
		List<AdditionalInstructionData> lstAddInstrData = new ArrayList<AdditionalInstructionData>();
		Map<String, Object> branchRoutingDetails =(Map)hashMap.get("ROUTING_DETAILS_MAP");
		BranchRemittanceApplRequestModel requestApplModel = (BranchRemittanceApplRequestModel)hashMap.get("APPL_REQ_MODEL");
		
		//Map<String, FlexFieldDto> flexFields = requestApplModel.getFlexFieldDtoMap();
		List<FlexFiledView> allFlexFields = remittApplDao.getFlexFields();
		Map<String, FlexFieldDto> requestFlexFields = requestApplModel.getFlexFieldDtoMap();
		
		
		
		if (requestFlexFields == null) {
			requestFlexFields = new HashMap<>();
			requestApplModel.setFlexFieldDtoMap(requestFlexFields);
		} else {
			validateFlexFieldValues(requestFlexFields);
		}
		
		
		
		
		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());
		Map<String, FlexFieldDto> flexFields  = remittApplDao.getFlexFields();
		flexFields.forEach((k, v) -> {
			BigDecimal bankId = (BigDecimal) branchRoutingDetails.get("P_ROUTING_BANK_ID");
			BigDecimal remittanceModeId = (BigDecimal) branchRoutingDetails.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryModeId = (BigDecimal) branchRoutingDetails.get("P_DELIVERY_MODE_ID");
			BigDecimal foreignCurrencyId = (BigDecimal) branchRoutingDetails.get("P_FOREIGN_CURRENCY_ID");
			
			if (v.getSrlId() != null) {
				AdditionalBankDetailsViewx additionaBnankDetail = bankService.getAdditionalBankDetail(v.getSrlId(),foreignCurrencyId, bankId, remittanceModeId, deliveryModeId);
				AdditionalInstructionData additionalInsDataTmp = createAdditionalIndicatorsData(remittanceApplication,applicationCountryId, k, additionaBnankDetail.getAmiecCode(),additionaBnankDetail.getAmieceDescription(), v.getAdditionalBankRuleFiledId());
				lstAddInstrData.add(additionalInsDataTmp);
			} else {
				AdditionalInstructionData additionalInsDataTmp = createAdditionalIndicatorsData(remittanceApplication,applicationCountryId, k, ConstantDocument.AMIEC_CODE, v.getAmieceDescription(),v.getAdditionalBankRuleFiledId());lstAddInstrData.add(additionalInsDataTmp);
			}
		});

		logger.info(" Exit from saveAdditionalInstnData ");

		return lstAddInstrData;
	}
*/
	// checking Indic1,Indic2,Indic3,Indic4,Indic5
	private AdditionalInstructionData createAdditionalIndicatorsData(RemittanceApplication remittanceApplication,BigDecimal applicationCountryId, String indicatorCode, String amiecCode, String flexFieldValue,BigDecimal additionalBankRuleId) {

		logger.info(" Enter into fetchIndicatorsData :" + flexFieldValue);

		AdditionalInstructionData additionalInsData = new AdditionalInstructionData();

		// document Id
		Document document = new Document();
		document.setDocumentID(remittanceApplication.getExDocument().getDocumentID());
		additionalInsData.setExDocument(document);

		// company Id
		CompanyMaster companymaster = new CompanyMaster();
		companymaster.setCompanyId(remittanceApplication.getFsCompanyMaster().getCompanyId());
		additionalInsData.setFsCompanyMaster(companymaster);

		// Application Country
		CountryMaster countrymaster = new CountryMaster();
		countrymaster.setCountryId(applicationCountryId);
		additionalInsData.setFsCountryMaster(countrymaster);

		if (additionalBankRuleId != null) {
			AdditionalBankRuleMap additionalBank = new AdditionalBankRuleMap();
			additionalBank.setAdditionalBankRuleId(additionalBankRuleId);
			additionalInsData.setAdditionalBankFieldsId(additionalBank);
		}
		additionalInsData.setFlexField(indicatorCode);
		additionalInsData.setFlexFieldValue(flexFieldValue);
		if (amiecCode != null) {
			additionalInsData.setAmiecCode(amiecCode);
		} else {
			additionalInsData.setAmiecCode(ConstantDocument.AMIEC_CODE);
		}

		additionalInsData.setExRemittanceApplication(remittanceApplication);
		additionalInsData.setExUserFinancialYear(remittanceApplication.getExUserFinancialYearByDocumentFinanceYear());
		additionalInsData.setDocumentFinanceYear(remittanceApplication.getDocumentFinancialyear());

		additionalInsData.setCreatedBy(remittanceApplication.getCreatedBy());
		additionalInsData.setCreatedDate(new Date());
		additionalInsData.setIsactive(ConstantDocument.Yes);
		additionalInsData.setDocumentNo(remittanceApplication.getDocumentNo());

		logger.info(" Exit from fetchIndicatorsData ");

		return additionalInsData;
	}

	
		/* saving data to Remittance Application AML */
		@SuppressWarnings("unchecked")
		public List<RemitApplAmlModel> saveRemittanceAppAML(RemittanceApplication remittanceApplication,Map hashMap){
			List<AmlCheckResponseDto> amlList =(List<AmlCheckResponseDto>)hashMap.get("AML_CHECK");
			BranchRemittanceApplRequestModel requestApplModel = (BranchRemittanceApplRequestModel)hashMap.get("APPL_REQ_MODEL");

			List<RemitApplAmlModel> remitApplAmlList = new ArrayList<RemitApplAmlModel>();

			try {
				for(AmlCheckResponseDto amlDto :amlList) {
					RemitApplAmlModel amlModel = new RemitApplAmlModel();

					RemitApplAmlModel remitApplAml = new RemitApplAmlModel();
					remitApplAml.setCompanyId(remittanceApplication.getFsCompanyMaster().getCompanyId());
					remitApplAml.setRemittanceApplicationId(remittanceApplication.getRemittanceApplicationId());
					remitApplAml.setExRemittanceAppfromAml(remittanceApplication);
					remitApplAml.setCountryId(remittanceApplication.getFsCountryMasterByApplicationCountryId().getCountryId());
					remitApplAml.setCreatedBy(remittanceApplication.getCreatedBy());
					remitApplAml.setCreatedDate(new Date());
					remitApplAml.setIsactive(ConstantDocument.Yes);
					remitApplAml.setAuthorizedBy(null); //Required 
					remitApplAml.setAuthType(null); //Required 
					remitApplAml.setBlackListReason(amlDto.getMessageDescription());
					remitApplAml.setBlackListRemarks(requestApplModel.getAmlRemarks());
					
					
					remitApplAmlList.add(remitApplAml);
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			return remitApplAmlList;
		}


	
	
	public ApplicationSetup getApplicationSetup() {
		
		ApplicationSetup appl = applCountryRepos.findByCountryIdAndCompanyIdDeatils(metaData.getCompanyId(),metaData.getCountryId());
		if(appl==null) {
			appl = applCountryRepos.getApplicationSetupDetails();
		}		
		if(appl==null) {
			 throw new GlobalException(JaxError.INVALID_APPLICATION_COUNTRY_ID,"Application country setup not defined");
		}
		return appl;
	}
	
	public CurrencyMasterModel getCurrencyByCountryId(BigDecimal applCountryId) {
		
		List<CurrencyMasterModel> currencyMaster = currDao.getCurrencyListByCountryId(applCountryId);
		if(currencyMaster!=null && !currencyMaster.isEmpty()) {
			return currencyMaster.get(0);
		}else {
			 throw new GlobalException(JaxError.INVALID_CURRENCY_ID,"Invalid currency");
		}
		
	}
	
	public BenificiaryListView getBeneDetails(BranchRemittanceApplRequestModel requestApplModel) { 
		BenificiaryListView beneficaryDetails =beneficiaryRepository.findBybeneficiaryRelationShipSeqId(requestApplModel.getBeneId());
		if(beneficaryDetails==null) {
			throw new GlobalException(JaxError.BENEFICIARY_LIST_NOT_FOUND,"Beneficairy not found "+requestApplModel.getBeneId());
			
		}
		return beneficaryDetails;
	}
	
	
	public EmployeeDetailsView getEmployeeDetails() {
		EmployeeDetailsView empDetails = employeeDetailsRepository.findByEmployeeId(metaData.getEmployeeId());
		if(empDetails==null) {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"Employee detais not found"+metaData.getEmployeeId());
		}else if(empDetails!=null && !empDetails.getIsActive().equalsIgnoreCase(ConstantDocument.Yes)) {
			throw new GlobalException(JaxError.INACTIVE_EMPLOYEE,"Employee is not active "+metaData.getEmployeeId());
		}else if(empDetails!=null && StringUtils.isBlank(empDetails.getUserName())) {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"User name should not be  blank "+metaData.getEmployeeId());
		}
		return empDetails;
	}
	

	

	
	public Boolean loyalityPointsAvailed(RemittanceTransactionRequestModel requestModel,RemittanceTransactionResponsetModel responseModel) {
		if (requestModel.isAvailLoyalityPoints() && responseModel.getCanRedeemLoyalityPoints()) {
			return true;
		}
		return false;
	}
	
	
	

	public String getCustomerFullName(Customer customer){
		String customerName =null;

		if(customer !=null){
			if(customer.getFirstName() !=null){
				customerName = customer.getFirstName(); 
			}
			if(!StringUtils.isEmpty(customer.getMiddleName())){
				customerName = customerName +" "+customer.getMiddleName();
			}
			if(!StringUtils.isEmpty(customer.getLastName())){
				customerName = customerName+ " "+ customer.getLastName();
			}
		}
		return customerName;
		}
	
	
	
	public java.sql.Clob stringToClob(String source) throws Exception {
		try {
			return new javax.sql.rowset.serial.SerialClob(source.toCharArray());
		} catch (Exception e) {
			return null;
		}
	}
	
 public BranchRemittanceApplResponseDto deleteFromShoppingCart(BigDecimal remittanceAppliId) {
	 brRemittanceDao.deleteFromCart(remittanceAppliId, ConstantDocument.Deleted);
	 BranchRemittanceApplResponseDto applResponseDto = branchRemittancePaymentManager.fetchCustomerShoppingCart(metaData.getCustomerId(),metaData.getDefaultCurrencyId());
	 return applResponseDto;
 }
 

 public String getCustomerSignature() {
	 String signature = null;
	 String ipaddress = metaData.getDeviceIp();
	 BigDecimal terminalId = metaData.getTerminalId();
	 
	 logger.debug("ipaddress :"+ipaddress+"\t CustomerId :"+metaData.getCustomerId()+"\t terminalId :"+terminalId);
	// BranchSystemDetail brSystemDetails = branchSystemDetailRepository.findByIpAddress(ipaddress);
	// if(brSystemDetails!=null) {
		// BigDecimal inventoryId = brSystemDetails.getCountryBranchSystemInventoryId();
		 if(JaxUtil.isNullZeroBigDecimalCheck(terminalId)) {
			 Device deviceClient = deviceRepository.findByDeviceTypeAndBranchSystemInventoryIdAndStatus(ClientType.SIGNATURE_PAD, terminalId,ConstantDocument.Yes);
			 DeviceStateInfo deviceStateInfo =  deviceStateRepository.findOne(deviceClient.getRegistrationId());
			 if(deviceStateInfo!=null && deviceStateInfo.getSignature()!=null) {
				 signature = deviceStateInfo.getSignature();
			 }
		 /*}else {
			 throw new GlobalException(JaxError.INVENTORY_ID_NOT_EXISTS,"Branch system inventory doesnot exist "+inventoryId);
		 }*/
	 }
	 
	 return signature;
 }
 
 
	private String getAccountNumber(BenificiaryListView beneficiaryDT) {
		String iBanFlag = bankService.getBankById(beneficiaryDT.getBankId()).getIbanFlag();
		String accountNumber = beneficiaryDT.getBankAccountNumber();
		String ibanNumber = beneficiaryService.getBeneAccountByAccountSeqId(beneficiaryDT.getBeneficiaryAccountSeqId()).getIbanNumber();
		logger.debug("iBanFlag: {} , iBANNum: {}", iBanFlag, ibanNumber);
		if (ConstantDocument.Yes.equalsIgnoreCase(iBanFlag) && StringUtils.isNotBlank(ibanNumber)) {
			accountNumber = ibanNumber;
		}
		return accountNumber;
	}
 
}
