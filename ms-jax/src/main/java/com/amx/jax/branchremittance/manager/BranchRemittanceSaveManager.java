package com.amx.jax.branchremittance.manager;
/**
 * @author rabil
 */

import java.math.BigDecimal;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.dao.BranchRemittanceDao;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.customer.manager.CustomerContactVerificationManager;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.JaxEmployeeDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.CollectDetailMdlv1;
import com.amx.jax.dbmodel.CollectionMdlv1;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.CurrencyWiseDenominationMdlv1;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ForeignCurrencyAdjustMdlv1;
import com.amx.jax.dbmodel.LanguageType;
import com.amx.jax.dbmodel.PaymentModeDesc;
import com.amx.jax.dbmodel.PaymentModeModel;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.bene.BankBlWorld;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.partner.RemitApplSrvProv;
import com.amx.jax.dbmodel.partner.RemitTrnxSrvProv;
import com.amx.jax.dbmodel.partner.TransactionDetailsView;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.LocalBankDetailsView;
import com.amx.jax.dbmodel.remittance.LoyaltyClaimRequest;
import com.amx.jax.dbmodel.remittance.LoyaltyPointsModel;
import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceAdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemittanceAml;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceApplicationSplitting;
import com.amx.jax.dbmodel.remittance.RemittanceBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dbmodel.remittance.RemittanceTransactionSplitting;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.CActivityEvent.Type;
import com.amx.jax.manager.PromotionManager;
import com.amx.jax.manager.RemittanceManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchApplicationDto;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.RemittanceCollectionDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.TransferDto;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.notification.JaxNotificationDataManager;
import com.amx.jax.partner.dao.PartnerTransactionDao;
import com.amx.jax.partner.dto.RemitTrnxSPDTO;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.IBeneBankBlackCheckDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.IPaymentModeDescRespo;
import com.amx.jax.repository.IPlaceOrderDao;
import com.amx.jax.repository.IRemitApplAmlRepository;
import com.amx.jax.repository.IRemitApplSrvProvRepository;
import com.amx.jax.repository.IRemittanceApplSplitRepository;
import com.amx.jax.repository.IRemittanceTransactionRepository;
import com.amx.jax.repository.IShoppingCartDetailsRepository;
import com.amx.jax.repository.PaymentModeRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.remittance.LocalBankDetailsRepository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.services.JaxEmailNotificationService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.services.RemittanceApplicationService;
import com.amx.jax.services.ReportManagerService;
import com.amx.jax.services.TransactionHistroyService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.utils.JsonUtil;

@Component
public class BranchRemittanceSaveManager {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MetaData metaData;
	
	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;

	@Autowired
	CompanyService companyService;
	
	@Autowired
	BranchRemittanceApplManager branchRemittanceApplManager;
	
	@Autowired
	AuthenticationLimitCheckDAO authenticationLimitCheck;
	
	@Autowired
	PaymentModeRepository paymentModeRepository;
	
	@Autowired
	BankMasterRepository bankMasterRepo;
	
	@Autowired
	LocalBankDetailsRepository localBankDetailsRepository;
	
	@Autowired
	IDocumentDao documentDao;
	
	@Autowired
	ApplicationProcedureDao applicationProcedureDao;
	
	@Autowired
	RoutingProcedureDao routingProDao;
	
	@Autowired
	RemittanceApplicationBeneRepository applBeneRepository;
	
	@Autowired
	AdditionalInstructionDataRepository  addInstrDataRepository;
	
	@Autowired
	IRemitApplAmlRepository applAmlRepository;
	
	@Autowired
	BranchRemittanceDao brRemittanceDao;
	
	@Autowired
	IBeneBankBlackCheckDao bankBlackWorldRepository;
	
	@Autowired
	IShoppingCartDetailsRepository shoppingCardRepository;
	
	@Autowired
	FinancialService finanacialService;
	
	@Autowired
	RemittanceApplicationService remittanceApplicationService;
	
	@Autowired
	ICurrencyDao currencyDao;
	
	@Autowired
	IPaymentModeDescRespo payModeDescRespo;
	
	@Autowired
	IRemittanceTransactionRepository remitTrnxRepository;
	
	@Autowired
	TransactionHistroyService transactionHistroyService;
	
	@Autowired
	private RemittanceApplicationDao remitAppDao;
	
	@Autowired
	private ReportManagerService reportManagerService;
	
	@Autowired
	PromotionManager promotionManager;
	
	@Autowired
	JaxEmployeeDao employeeDao;
	
	@Autowired
	UserService userService;
	
    @Autowired
    IPlaceOrderDao placeOrderdao;
	@Autowired
	RemittanceManager remittanceManager;
	
	@Autowired
	JaxEmailNotificationService jaxEmailNotificationService;
	
	@Autowired
	JaxNotificationService notificationService;

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	IRemitApplSrvProvRepository remitApplSrvProvRepository;
	
	@Autowired
	PartnerTransactionManager partnerTransactionManager;
	
	@Autowired
	PartnerTransactionDao partnerTransactionDao;
	
    @Autowired
	RemittanceSignatureManager remittanceSignatureManager;

    @Autowired
    IRemittanceApplSplitRepository applSplitRepo;
    @Autowired
    JaxNotificationDataManager jaxNotificationDataManager;
    
	
	
	List<LoyaltyPointsModel> loyaltyPoints 	 = new ArrayList<>();
	Map<BigDecimal,RemittanceBenificiary> remitBeneList = new HashMap<>();
	Map<BigDecimal,List<RemittanceAdditionalInstructionData>> addInstList = new HashMap<>();
	Map<BigDecimal,List<RemittanceAml>>			amlList	 = new HashMap<>();
	Map<BigDecimal,RemitTrnxSrvProv> mapRemitTrnxSrvProv = new HashMap<>();
	Map<BigDecimal,List<RemittanceTransactionSplitting>> remitSplitMap = new HashMap<>();
	
	
	
	@Autowired
    	AuditService auditService;
	
	
	@Autowired
	JaxTenantProperties jaxTenantProperties;
	
	@Autowired
	BankMetaService bankMetaService;
	
	/**
	 * 
	 * @param remittanceRequestModel
	 * @return : saveing application to remittance
	 */
	

	public RemittanceResponseDto saveRemittanceTrnx(BranchRemittanceRequestModel remittanceRequestModel) {
		logger.debug("saveRemittanceTrnx request model : {}", JsonUtil.toJson(remittanceRequestModel));
		List<BranchApplicationDto> shoppingCartList = new ArrayList<>();
		shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
		//updateApplicationStatus(shoppingCartList);
		RemittanceResponseDto responseDto = saveRemittance(remittanceRequestModel);
		TransactionDetailsView serviceProviderView = null;
		String partnerTransactionId = null;
		
		try {
		// service Provider api
		if(responseDto!=null && JaxUtil.isNullZeroBigDecimalCheck(responseDto.getCollectionDocumentNo())) {
			Boolean spCheckStatus = Boolean.FALSE;
			List<TransactionDetailsView> lstTrnxDetails = partnerTransactionDao.fetchTrnxSPDetails(metaData.getCustomerId(),responseDto.getCollectionDocumentFYear(),responseDto.getCollectionDocumentNo());
			for (TransactionDetailsView transactionDetailsView : lstTrnxDetails) {
				if(transactionDetailsView.getBankCode().equalsIgnoreCase(SERVICE_PROVIDER_BANK_CODE.HOME.name())) {
					spCheckStatus = Boolean.TRUE;
					serviceProviderView = transactionDetailsView;
					break;
				}
			}

			if(spCheckStatus) {
				AmxApiResponse<Remittance_Call_Response, Object> apiResponse = partnerTransactionManager.callingPartnerApi(responseDto);
				if(apiResponse != null) {
					if(serviceProviderView != null && serviceProviderView.getPartnerSessionId() != null) {
						partnerTransactionId = serviceProviderView.getPartnerSessionId();
					}
					RemitTrnxSPDTO remitTrnxSPDTO = partnerTransactionManager.saveRemitTransactionDetails(apiResponse,responseDto,partnerTransactionId);
					if(remitTrnxSPDTO != null && remitTrnxSPDTO.getActionInd() != null && remitTrnxSPDTO.getResponseDescription() != null) {
						// got success to fetch response from API
						logger.info(" Service provider result Action Ind " +remitTrnxSPDTO.getActionInd() + " Description : " + remitTrnxSPDTO.getResponseDescription());
					}else {
						logger.error("Service provider api fail to execute : ColDocNo : ", responseDto.getCollectionDocumentNo() + " : ColDocCod : " +responseDto.getCollectionDocumentCode()+"  : ColDocYear : "+responseDto.getCollectionDocumentFYear());
						auditService.log(new CActivityEvent(Type.TRANSACTION_CREATED,String.format("%s/%s", responseDto.getCollectionDocumentFYear(),responseDto.getCollectionDocumentNo())).field("STATUS").to(JaxTransactionStatus.PAYMENT_SUCCESS_SERVICE_PROVIDER_FAIL).result(Result.DONE));
						throw new GlobalException("Transaction failed to send to Service Provider");
					}
				}else {
					logger.error("Service provider api fail to execute : ColDocNo : ", responseDto.getCollectionDocumentNo() + " : ColDocCod : " +responseDto.getCollectionDocumentCode()+"  : ColDocYear : "+responseDto.getCollectionDocumentFYear());
					auditService.log(new CActivityEvent(Type.TRANSACTION_CREATED,String.format("%s/%s", responseDto.getCollectionDocumentFYear(),responseDto.getCollectionDocumentNo())).field("STATUS").to(JaxTransactionStatus.PAYMENT_SUCCESS_SERVICE_PROVIDER_FAIL).result(Result.DONE));
					throw new GlobalException("Transaction failed to send to Service Provider");
				}
			}
		}else {
			logger.error("Service provider api fail to execute : ColDocNo : ", responseDto.getCollectionDocumentNo() + " : ColDocCod : " +responseDto.getCollectionDocumentCode()+"  : ColDocYear : "+responseDto.getCollectionDocumentFYear());
		}
		logger.info("MRU --BEFORE appliation move to EMOS -->"+responseDto.getCollectionDocumentNo());
		
		if(responseDto!=null && JaxUtil.isNullZeroBigDecimalCheck(responseDto.getCollectionDocumentNo())) {
			brRemittanceDao.updateApplicationToMoveEmos(responseDto);
			PaymentResponseDto paymentResponse = new PaymentResponseDto();
			paymentResponse.setCollectionDocumentCode(responseDto.getCollectionDocumentCode());
			paymentResponse.setCollectionDocumentNumber(responseDto.getCollectionDocumentNo());
			paymentResponse.setCollectionFinanceYear(responseDto.getCollectionDocumentFYear());
			paymentResponse.setCompanyId(metaData.getCompanyId());
			paymentResponse.setApplicationCountryId(metaData.getCountryId());
			paymentResponse.setCustomerId(metaData.getCustomerId());
			if(jaxTenantProperties.getHashSigEnable()==true) {
				remittanceSignatureManager.updateSignatureHash(paymentResponse);
			}
			logger.info("MRU --BEFORE saveRemittancetoOldEmos  EMOS -->"+JsonUtil.toJson(paymentResponse));
			Map<String, Object> outpuMap = remittanceApplicationService.saveRemittancetoOldEmos(paymentResponse);
			logger.info("MRU procedure OUTPUT --->"+outpuMap==null?"TRNX MOVED SUCCESS":outpuMap.toString());
			if(outpuMap!=null && outpuMap.get("P_ERROR_MESSAGE")!=null) {
				String errrMsg = outpuMap.get("P_ERROR_MESSAGE").toString();
				logger.info("MRU Procedure Error Msg :"+errrMsg);
				if(!StringUtils.isBlank(errrMsg)) {
					notificationService.sendTransactionErrorAlertEmail(errrMsg,"TRNX NOT MOVED TO EMOS",paymentResponse);
				}
			}
			String promotionMsg = promotionManager.getPromotionPrizeForBranch(responseDto);
			responseDto.setPromotionMessage(promotionMsg);
		}else {
			logger.info("NOT moved to old emos ", responseDto.getCollectionDocumentNo() + "" +responseDto.getCollectionDocumentCode()+" "+responseDto.getCollectionDocumentFYear());
		}
		
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("MRU saveRemittanceTrnx catch block -->"+e.getMessage());
		}
		return responseDto;
	}
	
	
	public RemittanceResponseDto saveRemittance(BranchRemittanceRequestModel remittanceRequestModel) {
		RemittanceResponseDto responseDto  = new RemittanceResponseDto();
			
		try {
			CollectionMdlv1 			collectionModel 	    =saveCollect(remittanceRequestModel);
			List<CollectDetailMdlv1> 	collectionDetails		=saveCollectionDetail(remittanceRequestModel,collectionModel);
			List<ForeignCurrencyAdjustMdlv1> currencyAdjustList 		=saveForeignCurrencyAdjust(remittanceRequestModel,collectionModel);
			Map<BigDecimal,RemittanceTransaction>   remitTrnxList=saveRemittanceTrnx(remittanceRequestModel,collectionModel);
			LoyaltyClaimRequest loyaltyClaim          			=saveLoyalTyClaimRequest(collectionDetails);
					
			collectedAmountValidation(collectionModel,collectionDetails,currencyAdjustList);
			HashMap<String, Object> mapAllDetailRemitSave = new HashMap<String, Object>();
			mapAllDetailRemitSave.put("EX_COLLECT",collectionModel);
			mapAllDetailRemitSave.put("EX_COLLECT_DET",collectionDetails);
			mapAllDetailRemitSave.put("LYL_CLAIM",loyaltyClaim);
			mapAllDetailRemitSave.put("EX_CURR_ADJUST",currencyAdjustList);
			mapAllDetailRemitSave.put("EX_REMIT_TRNX", remitTrnxList);
			mapAllDetailRemitSave.put("EX_REMIT_BENE", remitBeneList);
			mapAllDetailRemitSave.put("EX_REMIT_ADDL", addInstList);
			mapAllDetailRemitSave.put("EX_REMIT_AML", amlList);
			mapAllDetailRemitSave.put("LOYALTY_POINTS", loyaltyPoints);
			mapAllDetailRemitSave.put("EX_REMIT_SRV_PROV", mapRemitTrnxSrvProv);
			mapAllDetailRemitSave.put("EX_REMIT_SPLIT", remitSplitMap);
			validateSaveTrnxDetails(mapAllDetailRemitSave);
			responseDto = brRemittanceDao.saveRemittanceTransaction(mapAllDetailRemitSave);
			auditService.log(new CActivityEvent(Type.TRANSACTION_CREATED,String.format("%s/%s", responseDto.getCollectionDocumentFYear(),responseDto.getCollectionDocumentNo())).field("STATUS").to(JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_SUCCESS).result(Result.DONE));
	}catch (GlobalException e) {
			logger.error("routing  procedure", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		}finally {
			amlList	 = new HashMap<>();
			remitBeneList   = new HashMap<>();
			addInstList = new HashMap<>();
			loyaltyPoints 	 = new ArrayList<>();
			mapRemitTrnxSrvProv = new HashMap<>();
			remitSplitMap = new HashMap<>();
		}
		
		return responseDto;
	}

	
	
	/** updating application status *to 'S'*/
	
	public void updateApplicationStatus(List<BranchApplicationDto> shoppingCartList) {
		try {
			for (BranchApplicationDto applId : shoppingCartList) {
				RemittanceApplication remittanceApplication =  remittanceApplicationRepository.findOne(applId.getApplicationId());
				if(remittanceApplication != null ){
					remittanceApplication.setApplicaitonStatus("S");
				}
				remittanceApplicationRepository.save(remittanceApplication);
			}
		
		}catch(GlobalException e){
			logger.debug("create collection", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}
		
	}
	

	private CollectionMdlv1  saveCollect(BranchRemittanceRequestModel remittanceRequestModel) {
	
		CollectionMdlv1 collection = new CollectionMdlv1();
		List<BranchApplicationDto> shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
		
		 checknetPercentage(remittanceRequestModel);
		
		try {
			if(shoppingCartList!=null && !shoppingCartList.isEmpty()) {
				
				Customer customerid = new Customer();
				customerid.setCustomerId(metaData.getCustomerId());
				RemittanceApplication appl =  remittanceApplicationRepository.getApplicationForRemittance(customerid,shoppingCartList.get(0).getApplicationId());
				
				if(appl!=null) {
				collection.setApplicationCountryId(appl.getFsCountryMasterByApplicationCountryId().getCountryId());
				collection.setFsCustomer(appl.getFsCustomer());
				collection.setCollectDate(new Date());
				collection.setExCurrencyMaster(appl.getExCurrencyMasterByLocalTranxCurrencyId());
				collection.setPaidAmount(remittanceRequestModel.getPaidAmount().add(remittanceRequestModel.getTotalLoyaltyAmount()==null?BigDecimal.ZERO:remittanceRequestModel.getTotalLoyaltyAmount()));
				collection.setNetAmount(remittanceRequestModel.getTotalTrnxAmount());
				collection.setRefoundAmount(collection.getPaidAmount().subtract(remittanceRequestModel.getTotalTrnxAmount()));
				collection.setAccountMMYYYY(appl.getAccountMmyyyy());
				collection.setCompanyCode(appl.getCompanyCode());
				collection.setDocumentFinanceYear(appl.getDocumentFinancialyear());
				collection.setDocumentId(documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION).get(0).getDocumentID());
				collection.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
				collection.setReceiptType(ConstantDocument.COLLECTION_RECEIPT_TYPE);
				collection.setCreatedDate(new Date());
				/*EmployeeDetailsView employee =branchRemittanceApplManager.getEmployeeDetails();
				collection.setCreatedBy(employee.getUserName());
				collection.setLocCode(employee.getBranchId());*/
				BigDecimal declarationTotalamount = getDeclarationReportAmount(ConstantDocument.DECL_REPORT_FOR_TOT_AMOUNT);
				if(JaxUtil.isNullZeroBigDecimalCheck(declarationTotalamount) && collection.getNetAmount().compareTo(declarationTotalamount)>=1) {
					collection.setCashDeclarationIndicator(ConstantDocument.Yes);
				}
				collection.setIsActive(ConstantDocument.Yes);
			
				
				CountryBranchMdlv1 countryBranch = new CountryBranchMdlv1();
				countryBranch = bankMetaService.getCountryBranchById(metaData.getCountryBranchId()); //user branch not customer branch
				logger.info("Meta Country Branch id : " +metaData.getCountryBranchId());
				if(countryBranch!=null && countryBranch.getBranchId().compareTo(ConstantDocument.ONLINE_BRANCH_LOC_CODE)==0) {
					collection.setLocCode(countryBranch.getBranchId());
					if(!StringUtils.isBlank(metaData.getReferrer())){
						collection.setCreatedBy(metaData.getReferrer());
					}else{
						if(!StringUtils.isBlank(metaData.getAppType())){				
							collection.setCreatedBy(metaData.getAppType());
						}else{
							collection.setCreatedBy("WEB");
						 }
					}
				}else {
					logger.info("EmployeeDetails View : ");
					EmployeeDetailsView employee =branchRemittanceApplManager.getEmployeeDetails();
					collection.setCreatedBy(employee.getUserName());
					collection.setLocCode(employee.getBranchId());
					countryBranch.setCountryBranchId(employee.getCountryBranchId());
				}
				
				
				
				/*CountryBranch countryBranch = new CountryBranch();
				if(employee!=null && JaxUtil.isNullZeroBigDecimalCheck(employee.getCountryBranchId())) {
					countryBranch.setCountryBranchId(employee.getCountryBranchId());
				}else {
					countryBranch.setCountryBranchId(metaData.getCountryBranchId());
				}*/
				collection.setExBankBranch(countryBranch);
				collection.setFsCompanyMaster(appl.getFsCompanyMaster());
				collection.setTotalAmountDeclarationIndicator(null); //ned to check
				
				 BigDecimal documentNo =generateDocumentNumber(appl.getFsCountryMasterByApplicationCountryId().getCountryId(),appl.getFsCompanyMaster().getCompanyId(),collection.getDocumentId(),collection.getDocumentFinanceYear(),collection.getLocCode(),ConstantDocument.A);
					
					if(documentNo!=null && documentNo.compareTo(BigDecimal.ZERO)!=0){
				    	collection.setDocumentNo(documentNo);
				    }else{
				    	throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO, "Collection document should not be blank.");
				    }
				
				
				}else {
					throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record not found to save in collection"+customerid+"\t appl No :"+shoppingCartList.get(0).getApplicationId());
				}
			}else {
				throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record not found to save in collection");
			}
			
		}catch(GlobalException e){
			logger.info("Exception : CREATE COLLECTION ");
			e.printStackTrace();
			logger.error("create collection", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}
		return collection;
	}
	
	
	
	
	public List<CollectDetailMdlv1> saveCollectionDetail(BranchRemittanceRequestModel remittanceRequestModel,CollectionMdlv1 collect){
		
		List<CollectDetailMdlv1> collectionDetailModelList = new ArrayList<CollectDetailMdlv1>();
		List<BranchApplicationDto> shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
		
		int i = 1;
		BigDecimal totalAmt = BigDecimal.ZERO;
		List<RemittanceCollectionDto> collectionDetails = remittanceRequestModel.getCollctionModeDto();
		
		
		for (RemittanceCollectionDto collectDataTable : collectionDetails) {
			
			CollectDetailMdlv1 collectDetails = new CollectDetailMdlv1();
			
			collectDetails.setCashCollectionId(collect);
			Customer customer = new Customer();
			customer.setCustomerId(collect.getFsCustomer().getCustomerId());
			collectDetails.setFsCustomer(customer);
			
			CountryMaster appcountrymaster = new CountryMaster();
			appcountrymaster.setCountryId(collect.getApplicationCountryId());
			collectDetails.setFsCountryMaster(appcountrymaster);
			
			collectDetails.setDocumentCode(collect.getDocumentCode());
			collectDetails.setExBankBranch(collect.getExBankBranch());
			collectDetails.setDocumentDate(new Date());
			collectDetails.setDocumentLineNo(new BigDecimal(i++));
			
			collectDetails.setDocumentId(collect.getDocumentId());
			collectDetails.setDocumentNo(collect.getDocumentNo());
			collectDetails.setFsCompanyMaster(collect.getFsCompanyMaster());
			collectDetails.setIsActive(ConstantDocument.Yes);
			collectDetails.setAcyymm(collect.getAccountMMYYYY());
			collectDetails.setCreatedBy(collect.getCreatedBy());
			collectDetails.setCreatedDate(new Date());
			
			collectDetails.setExCurrencyMaster(collect.getExCurrencyMaster());
			collectDetails.setCollAmt(collectDataTable.getPaymentAmount());
			collectDetails.setLocCode(collect.getLocCode());
			collectDetails.setCompanyCode(collect.getCompanyCode());
			collectDetails.setDocumentFinanceYear(collect.getDocumentFinanceYear());
			
			collectDetails.setPaymentModeId(collectDataTable.getPaymentModeId());
			PaymentModeModel payMode = getPaymentModeDetailsById(collectDataTable.getPaymentModeId());
			collectDetails.setCollectionMode(payMode.getPaymentModeCode());
			
		
			
			if(payMode.getPaymentModeCode().equalsIgnoreCase(ConstantDocument.KNET_CODE)) {
				
				collectDetails.setChequeBankRef(collectDataTable.getChequeBankCode());
				collectDetails.setDbCardName(collectDataTable.getColCardHolderName());
				collectDetails.setApprovalNo(collectDataTable.getApprovalNo());
				collectDetails.setKnetReceiptDateTime(new SimpleDateFormat("dd/MM/YYYY hh:mm").format(new Date()));
				collectDetails.setChequeRef(collectDataTable.getChequeBankCode());
				if(null != collectDataTable.getPosBankCode()) {
					BankMasterMdlv1 Model = getPosBankDetails(collectDataTable.getPosBankCode());
					collectDetails.setPosBankId(Model.getBankId());
				}
			}
			
			if(payMode.getPaymentModeCode().equalsIgnoreCase(ConstantDocument.CHEQUE)) {
				collectDetails.setChequeRef(collectDataTable.getColchequeRefNo());
				collectDetails.setChequeDate(collectDataTable.getColchequeDate() == null ? new Date(): DateUtil.convertStringToDate(collectDataTable.getColchequeDate()));
				collectDetails.setApprovalNo(collectDataTable.getApprovalNo());
				collectDetails.setChequeBankRef(collectDataTable.getChequeBankCode());
			}
			
			if(payMode.getPaymentModeCode().equalsIgnoreCase(ConstantDocument.BANK_TRANSFER)) {
				collectDetails.setChequeRef(collectDataTable.getColchequeRefNo());
				collectDetails.setChequeBankRef(collectDataTable.getChequeBankCode());
			}
			
		collectionDetailModelList.add(collectDetails);
		}
		//For Loyalty  Claim
		BigDecimal loyaltyAmount = BigDecimal.ZERO;
		Boolean lyaltyInd = false;
		
		if(shoppingCartList!=null && !shoppingCartList.isEmpty()) {
			
			for(BranchApplicationDto applDto :  shoppingCartList) {
				RemittanceApplication appl = remittanceApplicationRepository.findOne(applDto.getApplicationId());
				if(appl!=null && appl.getLoyaltyPointInd().equalsIgnoreCase(ConstantDocument.Yes)) {
					lyaltyInd = true;
					loyaltyAmount = loyaltyAmount.add(appl.getLoyaltyPointsEncashed());
				}
			}
				
				
			
				if(lyaltyInd && loyaltyAmount.compareTo(BigDecimal.ZERO)>0) {
					CollectDetailMdlv1 collectDetails = new CollectDetailMdlv1();
					
					collectDetails.setCashCollectionId(collect);
					Customer customer = new Customer();
					customer.setCustomerId(collect.getFsCustomer().getCustomerId());
					collectDetails.setFsCustomer(customer);
					
					CountryMaster appcountrymaster = new CountryMaster();
					appcountrymaster.setCountryId(collect.getApplicationCountryId());
					collectDetails.setFsCountryMaster(appcountrymaster);
					collectDetails.setLocCode(collect.getLocCode());
					collectDetails.setCompanyCode(collect.getCompanyCode());
					collectDetails.setDocumentFinanceYear(collect.getDocumentFinanceYear());
					
					collectDetails.setDocumentId(collect.getDocumentId());
					collectDetails.setDocumentNo(collect.getDocumentNo());
					collectDetails.setFsCompanyMaster(collect.getFsCompanyMaster());
					collectDetails.setIsActive(ConstantDocument.Yes);
					collectDetails.setAcyymm(collect.getAccountMMYYYY());
					collectDetails.setCreatedBy(collect.getCreatedBy());
					collectDetails.setCreatedDate(new Date());
					
					
					collectDetails.setDocumentCode(collect.getDocumentCode());
					collectDetails.setExBankBranch(collect.getExBankBranch());
					collectDetails.setDocumentDate(new Date());
					collectDetails.setDocumentLineNo(new BigDecimal(i++));
					collectDetails.setExCurrencyMaster(collect.getExCurrencyMaster());
					collectDetails.setCollAmt(loyaltyAmount);
					PaymentModeModel payMode = paymentModeRepository.getPaymentModeDetails(ConstantDocument.VOCHERCODE);
					collectDetails.setCollectionMode(payMode.getPaymentModeCode());
					collectDetails.setPaymentModeId(payMode.getPaymentModeId());
					
					collectDetails.setVoucherYear(collect.getDocumentFinanceYear());
					
					BigDecimal documentNo = generateDocumentNumber(collect.getApplicationCountryId(),
							collect.getFsCompanyMaster().getCompanyId(),ConstantDocument.VOUCHER_DOCUMENT_CODE,collect.getDocumentFinanceYear(),collect.getLocCode(),ConstantDocument.Update);
					
					if(documentNo!=null && documentNo.compareTo(BigDecimal.ZERO)!=0){
						collectDetails.setVoucherNo(documentNo);
				    }else{
				    	throw new GlobalException(JaxError.INVALID_VOUCHER_DOCUMENT_NO, "Document Seriality  setup  not  defined for Voucher ");
				    }
					
					collectionDetailModelList.add(collectDetails);
				
				}	
			
		} 
		
		
		return collectionDetailModelList;
		
	}
	
	
	
	
	
	
	
	
	public List<ForeignCurrencyAdjustMdlv1> saveForeignCurrencyAdjust(BranchRemittanceRequestModel remittanceRequestModel,CollectionMdlv1  collect){
		
		List<ForeignCurrencyAdjustMdlv1> currencyAdjustListList = new ArrayList<>();
		
		List<BranchApplicationDto> shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
		RemittanceApplication appl  =new RemittanceApplication();
		
		if(shoppingCartList!=null && !shoppingCartList.isEmpty()) {
			Customer customerid = new Customer();
			customerid.setCustomerId(metaData.getCustomerId());
			appl =  remittanceApplicationRepository.getApplicationForRemittance(customerid,shoppingCartList.get(0).getApplicationId());
		}
		
		
		List<RemittanceCollectionDto> collectionDetails = remittanceRequestModel.getCollctionModeDto();
		List<UserStockDto> currencyRefundDenominationList = remittanceRequestModel.getCurrencyRefundDenomination();
		
		int i = 0;
		int j=0;
		/** For Collection **/
			for (RemittanceCollectionDto collectDataTable : collectionDetails) {
				
				List<UserStockDto> currencyDenomination = collectDataTable.getCurrencyDenominationList();
				
				for(UserStockDto currencyCashDenomination :currencyDenomination) {
				if(JaxUtil.isNullZeroBigDecimalCheck(currencyCashDenomination.getDenominationQuatity())) {
					ForeignCurrencyAdjustMdlv1 foreignCurrencyAdjust = new ForeignCurrencyAdjustMdlv1();
					CountryMaster countryMaster = new CountryMaster();
					countryMaster.setCountryId(collect.getApplicationCountryId());
					foreignCurrencyAdjust.setFsCountryMaster(countryMaster);
					foreignCurrencyAdjust.setFsCustomer(collect.getFsCustomer());
					foreignCurrencyAdjust.setDocumentDate(new Date());
					foreignCurrencyAdjust.setFsCurrencyMaster(collect.getExCurrencyMaster());
					foreignCurrencyAdjust.setNotesQuantity(currencyCashDenomination.getDenominationQuatity());
					foreignCurrencyAdjust.setAdjustmentAmount(currencyCashDenomination.getDenominationPrice());
					foreignCurrencyAdjust.setFsCompanyMaster(appl.getFsCompanyMaster());
					foreignCurrencyAdjust.setCompanyCode(appl.getCompanyCode());
					foreignCurrencyAdjust.setDocumentId(collect.getDocumentId());
					foreignCurrencyAdjust.setOracleUser(collect.getCreatedBy());
					
					
					CurrencyWiseDenominationMdlv1 denominationMaster = new CurrencyWiseDenominationMdlv1();
					denominationMaster.setDenominationId(currencyCashDenomination.getDenominationId());
					foreignCurrencyAdjust.setFsDenominationId(denominationMaster);
					foreignCurrencyAdjust.setExchangeRate(appl.getExchangeRateApplied());
					foreignCurrencyAdjust.setDenaminationAmount(currencyCashDenomination.getDenominationAmount());
					foreignCurrencyAdjust.setDocumentFinanceYear(collect.getDocumentFinanceYear());
					foreignCurrencyAdjust.setDocumentCode(ConstantDocument.COLLECTION_DOCUMENT_ID);
					foreignCurrencyAdjust.setDocumentNo(collect.getDocumentNo());
					foreignCurrencyAdjust.setDocumentLineNumber(new BigDecimal(++i));
					foreignCurrencyAdjust.setAccountmmyyyy(collect.getAccountMMYYYY());
					foreignCurrencyAdjust.setCountryBranch(collect.getExBankBranch());
					foreignCurrencyAdjust.setProgNumber(ConstantDocument.FC_SALE_REMIT);
					foreignCurrencyAdjust.setDocumentStatus(ConstantDocument.Yes);
					foreignCurrencyAdjust.setTransactionType(ConstantDocument.C);
					
					foreignCurrencyAdjust.setCreatedDate(new Date());
					foreignCurrencyAdjust.setCreatedBy(collect.getCreatedBy());
					foreignCurrencyAdjust.setApprovalBy(collect.getCreatedBy());
					foreignCurrencyAdjust.setApprovalDate(new Date());
					
					foreignCurrencyAdjust.setCollect(collect);
					currencyAdjustListList.add(foreignCurrencyAdjust);
				}
				
			}
				
		}	
		/** For refund **/
			if(currencyRefundDenominationList!=null && !currencyRefundDenominationList.isEmpty()) {
			for(UserStockDto currencyRefundDenomination :currencyRefundDenominationList) {
			if(JaxUtil.isNullZeroBigDecimalCheck(currencyRefundDenomination.getDenominationQuatity())) {
				ForeignCurrencyAdjustMdlv1 foreignCurrencyRefundAdjust = new ForeignCurrencyAdjustMdlv1();
				CountryMaster countryMaster = new CountryMaster();
				countryMaster.setCountryId(collect.getApplicationCountryId());
				foreignCurrencyRefundAdjust.setFsCountryMaster(countryMaster);
				foreignCurrencyRefundAdjust.setFsCustomer(collect.getFsCustomer());
				foreignCurrencyRefundAdjust.setDocumentDate(new Date());
				foreignCurrencyRefundAdjust.setFsCurrencyMaster(collect.getExCurrencyMaster());
				foreignCurrencyRefundAdjust.setNotesQuantity(currencyRefundDenomination.getDenominationQuatity());
				foreignCurrencyRefundAdjust.setAdjustmentAmount(currencyRefundDenomination.getDenominationPrice());
				foreignCurrencyRefundAdjust.setOracleUser(collect.getCreatedBy());
				foreignCurrencyRefundAdjust.setFsCompanyMaster(appl.getFsCompanyMaster());
				foreignCurrencyRefundAdjust.setCompanyCode(appl.getCompanyCode());
				
				CurrencyWiseDenominationMdlv1 denominationMaster = new CurrencyWiseDenominationMdlv1();
				denominationMaster.setDenominationId(currencyRefundDenomination.getDenominationId());
				foreignCurrencyRefundAdjust.setFsDenominationId(denominationMaster);
				foreignCurrencyRefundAdjust.setDenaminationAmount(currencyRefundDenomination.getDenominationAmount());
				foreignCurrencyRefundAdjust.setDocumentFinanceYear(collect.getDocumentFinanceYear());
				foreignCurrencyRefundAdjust.setDocumentCode(ConstantDocument.COLLECTION_DOCUMENT_ID);
				foreignCurrencyRefundAdjust.setDocumentLineNumber(new BigDecimal(++i));
				foreignCurrencyRefundAdjust.setAccountmmyyyy(collect.getAccountMMYYYY());
				foreignCurrencyRefundAdjust.setCountryBranch(collect.getExBankBranch());
				foreignCurrencyRefundAdjust.setProgNumber(ConstantDocument.FC_SALE_REMIT);
				foreignCurrencyRefundAdjust.setDocumentStatus(ConstantDocument.Yes);
				foreignCurrencyRefundAdjust.setTransactionType(ConstantDocument.F);
				foreignCurrencyRefundAdjust.setDocumentNo(collect.getDocumentNo());
				
				foreignCurrencyRefundAdjust.setDocumentId(collect.getDocumentId());
				foreignCurrencyRefundAdjust.setCreatedDate(new Date());
				foreignCurrencyRefundAdjust.setApprovalBy(collect.getCreatedBy());
				foreignCurrencyRefundAdjust.setApprovalDate(new Date());
				
				foreignCurrencyRefundAdjust.setCreatedBy(collect.getCreatedBy());
				foreignCurrencyRefundAdjust.setCollect(collect);
				currencyAdjustListList.add(foreignCurrencyRefundAdjust);
			}
		 }
		}
		return  currencyAdjustListList;
	}
	
	
	
	
	public Map<BigDecimal,RemittanceTransaction> saveRemittanceTrnx(BranchRemittanceRequestModel remittanceRequestModel,CollectionMdlv1  collect)
	{
		Map<BigDecimal,RemittanceTransaction> remitTrnxList      =new HashMap<>();
		
		List<BranchApplicationDto> shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
		
		if(shoppingCartList!=null && !shoppingCartList.isEmpty()) {
			
			for(BranchApplicationDto applDto : shoppingCartList) {
				RemittanceTransaction remitTrnx = new RemittanceTransaction();
				Customer customerid = new Customer();
				customerid.setCustomerId(metaData.getCustomerId());
				RemittanceApplication appl =  remittanceApplicationRepository.getApplicationForRemittance(customerid,applDto.getApplicationId());
				
				if(appl!=null && appl.getIsactive().equalsIgnoreCase(ConstantDocument.Yes)) {
					logger.debug("appl :"+appl.getDocumentNo());
					remitTrnx.setAccountMmyyyy(appl.getAccountMmyyyy());
					remitTrnx.setApplicationCountryId(appl.getFsCountryMasterByApplicationCountryId());
					remitTrnx.setApplicationDocumentNo(appl.getDocumentNo());
					remitTrnx.setApplicationFinanceYear(appl.getDocumentFinancialyear());
					remitTrnx.setApplCreatedby(appl.getCreatedBy());
					remitTrnx.setApplCreateDate(appl.getCreatedDate());
					remitTrnx.setApplInd(appl.getApplInd());
					
					remitTrnx.setExchangeRateApplied(appl.getExchangeRateApplied());
					remitTrnx.setBankBranchId(appl.getExBankBranch());
					remitTrnx.setBankCountryId(appl.getFsCountryMasterByBankCountryId());
					remitTrnx.setBankId(appl.getExBankMaster());
					//remitTrnx.setBankReference(appl.getBa); nC
					remitTrnx.setBlackListIndicator(checkBlackListIndicator(appl.getFsCustomer().getCustomerId(),appl.getRemittanceApplicationId()));
					remitTrnx.setBranchId(collect.getExBankBranch());
					remitTrnx.setCollectionDocCode(collect.getDocumentCode());
					remitTrnx.setCollectionDocFinanceYear(collect.getDocumentFinanceYear());
					remitTrnx.setCollectionDocId(collect.getDocumentId());
					remitTrnx.setCollectionDocumentNo(collect.getDocumentNo());
					remitTrnx.setCompanyId(appl.getFsCompanyMaster());
					remitTrnx.setCompanyCode(appl.getCompanyCode());
					remitTrnx.setCreatedBy(collect.getCreatedBy());
					remitTrnx.setLoccod(collect.getLocCode());
					remitTrnx.setCreatedDate(new Date());
					remitTrnx.setCustomerId(appl.getFsCustomer());
					remitTrnx.setCustomerName(appl.getCustomerName());
					remitTrnx.setCustomerRef(appl.getCustomerRef());
					
					
					remitTrnx.setCustomerSignatureClob(appl.getCustomerSignatureClob());
					remitTrnx.setDebitAccountNo(appl.getDebitAccountNo()); //need to check
					remitTrnx.setDeliveryModeId(appl.getExDeliveryMode());
					remitTrnx.setDocumentDate(new Date());
					remitTrnx.setDocumentFinanceYear(appl.getDocumentFinancialyear());
					remitTrnx.setDocumentFinanceYr(appl.getExUserFinancialYearByDocumentFinanceYear().getFinancialYearID());
					
					List<Document> documentList = documentDao.getDocumnetByCode(ConstantDocument.REMITTANCE_DOCUMENT_CODE);
					
					if(documentList!=null && !documentList.isEmpty()) {
						remitTrnx.setDocumentId(documentList.get(0));
						remitTrnx.setDocumentCode(documentList.get(0).getDocumentCode());
					}else {
						throw new GlobalException(JaxError.INVALID_REMITTANCE_DOCUMENT_CODE,"Document ID could not be updated in our records.");
					}
					
					//remitTrnx.setDwFlag(dwFlag); //NC
				
					remitTrnx.setEmployeeId(metaData.getEmployeeId());
					remitTrnx.setExchangeRateApplied(appl.getExchangeRateApplied());
					
					remitTrnx.setForeignCurrencyId(appl.getExCurrencyMasterByForeignCurrencyId());
					remitTrnx.setForeignTranxAmount(appl.getForeignTranxAmount());
					remitTrnx.setGeneralLedgerEntry(null);
					remitTrnx.setGeneralLedgerErr(null);
					remitTrnx.setHighValueAuthDate(null);
					remitTrnx.setHighValueAuthUser(null);
					remitTrnx.setHighValueTranx(null);
					remitTrnx.setInstruction(appl.getInstruction());
					remitTrnx.setIsactive(ConstantDocument.Yes);
					remitTrnx.setLocalChargeAmount(appl.getLocalChargeAmount());
					remitTrnx.setLocalChargeCurrencyId(appl.getExCurrencyMasterByLocalChargeCurrencyId());
					remitTrnx.setLocalCommisionAmount(appl.getLocalCommisionAmount());
					remitTrnx.setLocalDeliveryAmount(appl.getLocalDeliveryAmount());
					remitTrnx.setLocalDeliveryCurrencyId(appl.getExCurrencyMasterByLocalDeliveryCurrencyId());
					remitTrnx.setLocalNetCurrencyId(appl.getExCurrencyMasterByLocalNetCurrencyId());
					remitTrnx.setLocalNetTranxAmount(appl.getLocalNetTranxAmount());
					remitTrnx.setLocalTranxCurrencyId(appl.getExCurrencyMasterByLocalTranxCurrencyId());
					remitTrnx.setLocalTranxAmount(appl.getLocalTranxAmount());
					remitTrnx.setLoyaltyPointsEncashed(appl.getLoyaltyPointsEncashed());
					remitTrnx.setLoyaltyPointsInd(appl.getLoyaltyPointInd());
					remitTrnx.setOriginalExchangeRate(appl.getOriginalExchangeRate());
					remitTrnx.setRemittanceModeId(appl.getExRemittanceMode());
					remitTrnx.setSourceofincome(appl.getSourceofincome());
					remitTrnx.setLocalCommisionCurrencyId(appl.getExCurrencyMasterByLocalTranxCurrencyId());
					TransferDto trnaferType = getTrasnferModeByBankServiceRule(remitTrnx);
					remitTrnx.setFileCreation(trnaferType.getFileCreation());
					remitTrnx.setTransferMode(trnaferType.getTrasnferMode());
					remitTrnx.setTransferModeId(trnaferType.getTransferModeId());
					remitTrnx.setUsdAmount(appl.getUsdAmt());
					remitTrnx.setWesternUnionMtcno(appl.getWesternUnionMtcno());
					remitTrnx.setWuIpAddress(metaData.getDeviceIp());
					remitTrnx.setDiscountOnCommission(appl.getDiscountOnCommission());
					remitTrnx.setChannelDiscount(appl.getChannelDiscount());
					remitTrnx.setChannelDiscountId(appl.getChannelDiscountId());
					remitTrnx.setCusCatDiscountId(appl.getCusCatDiscountId());
					remitTrnx.setCusCatDiscount(appl.getCusCatDiscount());
					remitTrnx.setChannelDiscountId(appl.getChannelDiscountId());
					remitTrnx.setPipsDiscount(appl.getPipsDiscount());
					remitTrnx.setPipsFromAmt(appl.getPipsFromAmt());
					remitTrnx.setPipsToAmt(appl.getPipsToAmt());
					remitTrnx.setIsDiscountAvailed(appl.getIsDiscountAvailed());
					remitTrnx.setReachedCostRateLimit(appl.getReachedCostRateLimit());
					remitTrnx.setBeneDeductFlag(appl.getBeneDeductFlag());
					remitTrnx.setInstruction(appl.getInstruction());
					remitTrnx.setUsdAmt(appl.getUsdAmt());
					remitTrnx.setWuPurposeOfTransaction(appl.getWuPurposeOfTransaction());
					remitTrnx.setApplSplit(appl.getApplSplit());
					Date date = DateUtil.addCurrentDateTimeToGetNewTime(appl.getTimeToDeliverInSec());
						if(date!=null) {
							remitTrnx.setTimeToDeliver(date);
						}
					
					BigDecimal documentNo =generateDocumentNumber(appl.getFsCountryMasterByApplicationCountryId().getCountryId(),appl.getFsCompanyMaster().getCompanyId(),remitTrnx.getDocumentId().getDocumentCode(),remitTrnx.getDocumentFinanceYear(),remitTrnx.getLoccod(),ConstantDocument.A);
					
					if(documentNo!=null && documentNo.compareTo(BigDecimal.ZERO)!=0){
						remitTrnx.setDocumentNo(documentNo);
				    }else{
				    	throw new GlobalException(JaxError.INVALID_REMITTANCE_DOCUMENT_NO, "Document Seriality  setup  not defined for Remittance.");
				    }
				
					remitTrnxList.put(appl.getRemittanceApplicationId(),remitTrnx);
					saveBeneTrnx(appl, remitTrnx);
					saveRemitnaceinstructionData(appl,remitTrnx);
					saveRemittanceAml(appl, remitTrnx);
					saveLoyaltyPoints(remitTrnx);
					mapRemitTrnxSrvProv = saveRemitTrnxSrvProv(appl.getRemittanceApplicationId(), remitTrnx.getCreatedBy());
					saveRemitTrnxSplit(appl,remitTrnx);
				}
			}
			
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record not found to save in remittance");
		}
		
		return remitTrnxList;
	}
	

	public  Map<BigDecimal,RemittanceBenificiary>  saveBeneTrnx(RemittanceApplication applicationNo,RemittanceTransaction remitTrnx){
	if(applicationNo!=null) {
		logger.debug("saveBeneTrnx :"+applicationNo.getRemittanceApplicationId() +"\t Customer Id :"+metaData.getCustomerId());
		RemittanceAppBenificiary applBene = applBeneRepository.findByExRemittanceAppfromBenfi(applicationNo);
		if(applBene!=null) {
			RemittanceBenificiary remitBene = new RemittanceBenificiary();
			
			remitBene.setBeneficiaryAccountNo(applBene.getBeneficiaryAccountNo());
			remitBene.setBeneficiaryAccountSeqId(applBene.getBeneficiaryAccountSeqId());
			remitBene.setBeneficiaryBank(applBene.getBeneficiaryBank());
			remitBene.setBeneficiaryBankBranchId(applBene.getBeneficiaryBankBranchId());
			remitBene.setBeneficiaryBankCountryId(applBene.getBeneficiaryBankCountryId());
			remitBene.setBeneficiaryBankId(applBene.getBeneficiaryBankId());
			remitBene.setBeneficiaryBankSwift(applBene.getBeneficiaryBankSwift());
			remitBene.setBeneficiaryBranch(applBene.getBeneficiaryBranch());
			remitBene.setBeneficiaryBranchCityId(applBene.getBeneficiaryBranchCityId());
			remitBene.setBeneficiaryBranchDistrictId(applBene.getBeneficiaryBranchDistrictId());
			remitBene.setBeneficiaryBranchStateId(applBene.getBeneficiaryBranchStateId());
			
			remitBene.setBeneficiaryName(applBene.getBeneficiaryName());
			remitBene.setBeneficiaryFirstName(applBene.getBeneficiaryFirstName());
			remitBene.setBeneficiarySecondName(applBene.getBeneficiarySecondName());
			remitBene.setBeneficiaryThirdName(applBene.getBeneficiaryThirdName());
			remitBene.setBeneficiaryFourthName(applBene.getBeneficiaryFourthName());
			remitBene.setBeneficiaryFifthName(applBene.getBeneficiaryFifthName());
			remitBene.setBeneficiaryId(applBene.getBeneficiaryId());
			remitBene.setBeneficiaryRelationShipSeqId(applBene.getBeneficiaryRelationShipSeqId());
			remitBene.setBeneficiarySwiftAddr1(applBene.getBeneficiarySwiftAddr1());
			remitBene.setBeneficiarySwiftAddr2(applBene.getBeneficiarySwiftAddr2());
			remitBene.setBeneficiarySwiftBank1(applBene.getBeneficiarySwiftBank1());
			remitBene.setBeneficiarySwiftBank1Id(applBene.getBeneficiarySwiftBank1Id());
			remitBene.setBeneficiarySwiftBank2(applBene.getBeneficiarySwiftBank2());
			remitBene.setBeneficiarySwiftBank2Id(applBene.getBeneficiarySwiftBank2Id());
			remitBene.setBeneficiaryTelephoneNumber(applBene.getBeneficiaryTelephoneNumber());
			remitBene.setCreatedBy(remitTrnx.getCreatedBy());
			remitBene.setCreatedDate(new Date());
			remitBene.setDocumentCode(remitTrnx.getDocumentCode());
			remitBene.setExDocument(remitTrnx.getDocumentId());
			remitBene.setCompanyCode(remitTrnx.getCompanyCode());
			remitBene.setFsCompanyMaster(remitTrnx.getCompanyId());
			remitBene.setDocumentNo(remitTrnx.getDocumentNo());  //at the time of actual save commented  by MRU
			remitBene.setExRemittancefromBenfi(remitTrnx);
			remitBene.setExUserFinancialYear(getFinancialYearObj(remitTrnx.getDocumentFinanceYear()));
			remitBene.setIsactive(ConstantDocument.Yes);
			logger.debug("saveBeneTrnx remitBeneList.put :"+applBene.getExRemittanceAppfromBenfi().getRemittanceApplicationId() +"\t Customer Id :"+metaData.getCustomerId());
			remitBeneList.put(applBene.getExRemittanceAppfromBenfi().getRemittanceApplicationId(), remitBene);
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record not found in appl bene for remittance : "+remitTrnx.getApplicationDocumentNo());
		}
	}else {
		throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record not found in appl bene for remittance : "+remitTrnx.getApplicationDocumentNo());
	}
	
	return remitBeneList;
}
	
	public   Map<BigDecimal,List<RemittanceAdditionalInstructionData>>   saveRemitnaceinstructionData(RemittanceApplication applicationNo,RemittanceTransaction remitTrnx){
		
		List<RemittanceAdditionalInstructionData> remitAddList = new ArrayList<>();
	 if(applicationNo!=null) {
			List<AdditionalInstructionData> applInstrucDataList = addInstrDataRepository.findByExRemittanceApplication(applicationNo);
		
			if(applInstrucDataList!=null && !applInstrucDataList.isEmpty()) {
			for (AdditionalInstructionData applInstrucData :applInstrucDataList) {
			RemittanceAdditionalInstructionData remitAddData = new RemittanceAdditionalInstructionData();
			remitAddData.setAdditionalBankFieldsId(applInstrucData.getAdditionalBankFieldsId());
			remitAddData.setAmiecCode(applInstrucData.getAmiecCode());
			remitAddData.setCreatedBy(remitTrnx.getCreatedBy());
			remitAddData.setCreatedDate(new Date());
			remitAddData.setDocumentFinanceYear(remitTrnx.getDocumentFinanceYear());
			remitAddData.setDocumentNo(remitTrnx.getDocumentNo());
			remitAddData.setExDocument(remitTrnx.getDocumentId());
			remitAddData.setFlexField(applInstrucData.getFlexField());
			remitAddData.setFlexFieldValue(applInstrucData.getFlexFieldValue());
			remitAddData.setExUserFinancialYear(getFinancialYearObj(remitTrnx.getDocumentFinanceYear()));
			remitAddData.setExRemittanceTransaction(remitTrnx);
			remitAddData.setFsCompanyMaster(remitTrnx.getCompanyId());
			remitAddData.setFsCountryMaster(remitTrnx.getApplicationCountryId());
			remitAddData.setCompanyCode(applInstrucData.getFsCompanyMaster().getCompanyCode());
			remitAddData.setDocumentFinanceYear(remitTrnx.getDocumentFinanceYear());
			remitAddData.setIsactive(ConstantDocument.Yes);
			remitAddList.add(remitAddData);
		}
			addInstList.put(applicationNo.getRemittanceApplicationId(), remitAddList);
			
			}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record not found in appl additional instruction  :"+remitTrnx.getApplicationDocumentNo());
		}
	  
	 }
	 return addInstList;
}


public Map<BigDecimal,List<RemittanceAml>>	saveRemittanceAml(RemittanceApplication applicationNo,RemittanceTransaction remitTrnx){
		
	List<RemittanceAml> amlLst = new ArrayList<>();
	if(applicationNo!=null) {
		List<RemitApplAmlModel> applAmlList = applAmlRepository.findByExRemittanceAppfromAml(applicationNo);
		if(applAmlList!=null && !applAmlList.isEmpty()) {
			for(RemitApplAmlModel applAml :applAmlList) {
			RemittanceAml remitAml = new RemittanceAml();
			remitAml.setAuthorizedBy(applAml.getAuthorizedBy());
			remitAml.setAuthType(applAml.getAuthType());
			remitAml.setCreatedBy(remitTrnx.getCreatedBy());
			remitAml.setCreatedDate(new Date());
			remitAml.setBlackListDate(new Date());
			remitAml.setBlackListReason(applAml.getBlackListReason());
			remitAml.setBlackListRemarks(applAml.getBlackListRemarks());
			remitAml.setFsCompanyMaster(remitTrnx.getCompanyId());
			remitAml.setFsCountryMaster(remitTrnx.getApplicationCountryId());
			remitAml.setBlackListUser(applAml.getBlackListUser());
			remitAml.setIsactive(ConstantDocument.Yes);
			remitAml.setExRemittancefromAml(remitTrnx);
			amlLst.add(remitAml);
			}
			amlList.put(applicationNo.getRemittanceApplicationId(),amlLst);
		}
	}

return amlList;
	
}

private LoyaltyClaimRequest saveLoyalTyClaimRequest(List<CollectDetailMdlv1> collectDetailModelList) {
	LoyaltyClaimRequest Lclaim = new LoyaltyClaimRequest();
	if(collectDetailModelList!=null && !collectDetailModelList.isEmpty()) {
		for(CollectDetailMdlv1 collectDetail : collectDetailModelList) {
			if(collectDetail.getCollectionMode().equalsIgnoreCase(ConstantDocument.VOCHERCODE)) {
				Lclaim.setClaimDate(new Date());
				if(collectDetail.getCollAmt().compareTo(BigDecimal.ONE)>0) {
					Lclaim.setClaimPoints(collectDetail.getCollAmt()==null?BigDecimal.ZERO:collectDetail.getCollAmt().multiply(new BigDecimal(1000)));
				}else {
					Lclaim.setClaimPoints(collectDetail.getCollAmt()==null?BigDecimal.ZERO:collectDetail.getCollAmt().multiply(new BigDecimal(1000)));
				}
				Lclaim.setEcLocCode(collectDetail.getLocCode());
				Lclaim.setDocfyr(collectDetail.getDocumentFinanceYear());
				Lclaim.setEcmCode(routingProDao.getEcmCode());
				Lclaim.setEcDocDate(new Date());
				Lclaim.setEcValue(collectDetail.getCollAmt());
				Lclaim.setVoucherNo(collectDetail.getVoucherNo());
				
			}
		}
		
	}
	
	return Lclaim;
}


public List<LoyaltyPointsModel> saveLoyaltyPoints(RemittanceTransaction applDto){
	
		
		if(applDto!=null) {
			//for(RemittanceTransaction applDto : remittTransactionList) {
				if(applDto!=null && JaxUtil.isNullZeroBigDecimalCheck(applDto.getLoyaltyPointsEncashed()) && applDto.getLoyaltyPointsInd().equalsIgnoreCase(ConstantDocument.Yes)) {
					LoyaltyPointsModel lpoints = new LoyaltyPointsModel();
					lpoints.setAccMonth(applDto.getAccountMmyyyy());
					lpoints.setCustomerReference(applDto.getCustomerRef());
					lpoints.setCompCode(applDto.getCompanyCode());
					lpoints.setTransDate(new Date());
					lpoints.setLoyaltyPoints(applDto.getLoyaltyPointsEncashed()==null?BigDecimal.ZERO:applDto.getLoyaltyPointsEncashed().multiply(new BigDecimal(-1000)));
					lpoints.setDocfyr(applDto.getDocumentFinanceYear());
					lpoints.setTrnRefNo(applDto.getDocumentNo());
					lpoints.setType(ConstantDocument.CLAIM);
					lpoints.setCalcFlag(ConstantDocument.R);
					lpoints.setProcessDate(new Date());
					lpoints.setDocCode(applDto.getDocumentCode());
					lpoints.setProcessDate(DateUtil.daysAddInCurrentDate(365));
					lpoints.setExpiryDate(DateUtil.daysAddInCurrentDate(365));
					lpoints.setConsumedLp(applDto.getLoyaltyPointsEncashed()==null?BigDecimal.ZERO:applDto.getLoyaltyPointsEncashed().multiply(new BigDecimal(-1000)));
					lpoints.setAvaliableLp(BigDecimal.ZERO);
					loyaltyPoints.add(lpoints);
				}
			//}
		}
	 return loyaltyPoints;
}




public void collectedAmountValidation(CollectionMdlv1 collectionModel,List<CollectDetailMdlv1> collectionDetails,List<ForeignCurrencyAdjustMdlv1> currencyAdjustList){

	BigDecimal totalPaidAmount =BigDecimal.ZERO;
	BigDecimal refundAmount =BigDecimal.ZERO;
	BigDecimal netAmount =BigDecimal.ZERO;
	BigDecimal totalCollectedAmount =BigDecimal.ZERO;
	BigDecimal totalCashAmount =BigDecimal.ZERO;
	BigDecimal totalCurrencyAdjust=BigDecimal.ZERO;
	
	
	if(collectionModel!=null) {
		totalPaidAmount = collectionModel.getPaidAmount();
		refundAmount    = collectionModel.getRefoundAmount();
		netAmount       = collectionModel.getNetAmount();
		
		logger.info("collectionModel not empty ------> " +totalPaidAmount);
		if(totalPaidAmount.subtract(refundAmount).compareTo(netAmount)!=0) {
			throw new GlobalException(JaxError.AMOUNT_MISMATCH,"There is a mismatch found in the Net amount and Refunded amount");
			}
		
		}else {
			throw new GlobalException(JaxError.AMOUNT_MISMATCH,"There is a mismatch found in the Net amount and Refunded amount");
		}
	
	
	if(collectionDetails!=null && !collectionDetails.isEmpty()) {
		totalCollectedAmount = collectionDetails.stream().map(CollectDetailMdlv1::getCollAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalCashAmount      =collectionDetails.stream().filter(c->c.getCollectionMode().equalsIgnoreCase(ConstantDocument.CASH)).map(CollectDetailMdlv1::getCollAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
	}else {
		throw new GlobalException(JaxError.AMOUNT_MISMATCH,"The amount you have entered is 0. Please enter the correct amount ");
	}
	
	if(totalPaidAmount.compareTo(totalCollectedAmount)!=0) {
		logger.info("Total Paid Amt : " +totalPaidAmount+ " Total Collect Amt : " +totalCollectedAmount);
		throw new GlobalException(JaxError.AMOUNT_MISMATCH,"The collection amount does not match with the collection details as per payment mode selected.");
	}
	
	
	if(totalCashAmount.compareTo(BigDecimal.ZERO)>0) {
		if(currencyAdjustList!=null && !currencyAdjustList.isEmpty()) {
			BigDecimal totalCurrencyAdjustCollect =BigDecimal.ZERO;
			BigDecimal totalCurrencyAdjustRefund=BigDecimal.ZERO;
			totalCurrencyAdjustCollect = currencyAdjustList.stream().filter(a->a.getTransactionType().equalsIgnoreCase(ConstantDocument.CASH)).map(ForeignCurrencyAdjustMdlv1::getAdjustmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalCurrencyAdjustRefund  =currencyAdjustList.stream().filter(a->a.getTransactionType().equalsIgnoreCase(ConstantDocument.F)).map(ForeignCurrencyAdjustMdlv1::getAdjustmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalCurrencyAdjust =totalCurrencyAdjustCollect.subtract(totalCurrencyAdjustRefund);
			
			
			if(totalCashAmount.subtract(refundAmount).compareTo(totalCurrencyAdjust)!=0) {
				throw new GlobalException(JaxError.AMOUNT_MISMATCH,"Mismatch found in cash collected and Denomination entered.");
			}
			
		}else {
			throw new GlobalException(JaxError.AMOUNT_MISMATCH,"The currency count could not be updated in the currency adjustment table.");
		}
	}
	
	
	
/*	
	if(totalCollectedAmount.subtract(refundAmount).compareTo(totalCurrencyAdjust)!=0) {
		throw new GlobalException(JaxError.AMOUNT_MISMATCH,"Mismatch found in cash collected and Denomination entered.");
	}*/
	
	
	
/*	if(totalCashAmount.subtract(refundAmount).compareTo(totalCurrencyAdjust)!=0) {
		throw new GlobalException(JaxError.AMOUNT_MISMATCH,"Mismatch found in cash collected and Denomination entered.");
	}*/
	
	
}


public String checkBlackListIndicator(BigDecimal customerId,BigDecimal  applId) {
	String blakListIndicator = "N";
	ShoppingCartDetails shopCartDetails = shoppingCardRepository.findByCustomerIdAndRemittanceApplicationId(customerId, applId);
	if(shopCartDetails!=null) {
	List<BankBlWorld> list = bankBlackWorldRepository.getCheckBnakBeneBanned(shopCartDetails.getBeneficiaryName());
	
	if(list!=null && !list.isEmpty()) {
		blakListIndicator=ConstantDocument.Yes;
	}
	
	}
	return blakListIndicator;
}

	
	public BigDecimal getDeclarationReportAmount(String authType) {
		BigDecimal declarationTotalamount =BigDecimal.ZERO;
		AuthenticationLimitCheckView authParam = authenticationLimitCheck.findByAuthorizationType(authType);
		if(authParam!=null) {
		 declarationTotalamount = authParam.getAuthLimit()==null?BigDecimal.ZERO:authParam.getAuthLimit();
		}
		return declarationTotalamount;
	}
	
	public PaymentModeModel getPaymentModeDetailsById(BigDecimal paymentModeid) {
		PaymentModeModel patMode = new PaymentModeModel();
		patMode = paymentModeRepository.getPaymentModeDetailsById(paymentModeid);
		if(patMode==null) {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Payment mode not found");
		}
		return patMode;
	}
	
	
	public BankMasterMdlv1 getPosBankDetails(String bankCode) {
		BankMasterMdlv1 bankMaster = bankMasterRepo.findByBankCodeAndRecordStatus(bankCode,ConstantDocument.Yes);
		if(bankMaster==null) {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record not found for bank code :"+bankCode);
		}
		return bankMaster;
	}
	
	public LocalBankDetailsView getLocalBankDetails(String bankCode) {
		LocalBankDetailsView localBank = localBankDetailsRepository.findByChequeBankCode(bankCode);
		if(localBank==null) {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Local bank not found  :"+bankCode);
		}
		return localBank;
	}
	
public BigDecimal generateDocumentNumber(BigDecimal appCountryId,BigDecimal companyId,BigDecimal documentId,BigDecimal finYear,BigDecimal branchId,String process) {
	Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId,finYear, process, branchId);
	return (BigDecimal) output.get("P_DOC_NO");
	}





 public TransferDto getTrasnferModeByBankServiceRule(RemittanceTransaction remitTrnx){
	 Map<String,Object> mapBankServiceRule= routingProDao.checkBankServiceRule(remitTrnx);
	 logger.debug("getTrasnferModeByBankServiceRule request json : {}", JsonUtil.toJson(remitTrnx));
	 TransferDto dto = new TransferDto();
	 String transferMode=null;
	 String fileCreation=ConstantDocument.No;
	 BigDecimal transferModeId=BigDecimal.ZERO;
	 if(mapBankServiceRule!=null && !mapBankServiceRule.isEmpty()) {
		 transferMode = mapBankServiceRule.get("P_TRANSFER_MODE")==null?"":mapBankServiceRule.get("P_TRANSFER_MODE").toString();
		 transferModeId = mapBankServiceRule.get("P_TRANSFER_MODE_ID")==null?BigDecimal.ZERO:(BigDecimal)mapBankServiceRule.get("P_TRANSFER_MODE_ID");
		 
		 if(!JaxUtil.isNullZeroBigDecimalCheck(transferModeId) && StringUtils.isBlank(transferMode)){
			 throw new GlobalException(JaxError.NO_RECORD_FOUND,"Transfer mode is not defined in bank service rule");
		 }
		 
		 if(!StringUtils.isBlank(transferMode) && (transferMode.equalsIgnoreCase(ConstantDocument.FILE_CREATION) || transferMode.equalsIgnoreCase(ConstantDocument.TELEX_TRANFER))) {
			 fileCreation=ConstantDocument.Yes;
		 }else if(!StringUtils.isBlank(transferMode) && transferMode.equalsIgnoreCase(ConstantDocument.WEB_SERVICE)) {
			 fileCreation=ConstantDocument.No;
		 }
	 }else {		
		 throw new GlobalException(JaxError.NO_RECORD_FOUND,"Transfer mode is not defined in bank service rule");
	 }
	
	 dto.setTransferModeId(transferModeId);
	 dto.setTrasnferMode(transferMode);
	 dto.setFileCreation(fileCreation);
	 return dto;
 }
 
 
 public UserFinancialYear getFinancialYearObj(BigDecimal financeYear) {
		UserFinancialYear financeYearObj = finanacialService.getUserFinancialYear();
		if (financeYearObj == null) {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Financial year setup is missing");
		}
	 return financeYearObj;
 }

 public String convertClobToStringVs(Clob clob) {
	 String signatureStr = null;
	 try {
		  signatureStr  = clob.getSubString(1, (int) clob.length());
	 }catch(Exception e){
		 e.printStackTrace();
		logger.debug("convertClobToStringVs "+e.getMessage());
		 throw new GlobalException(JaxError.CUSTOMER_SIGNATURE_UNAVAILABLE, e.getMessage());
	 }
	 return signatureStr;
 }
 
 public java.sql.Clob stringToClob(String source) throws Exception {
		try {
			return new javax.sql.rowset.serial.SerialClob(source.toCharArray());
		} catch (Exception e) {
			return null;
		}
	}
 
 

 public void checknetPercentage(BranchRemittanceRequestModel remittanceRequestModel) {
	
	 List<RemittanceCollectionDto> collectionDetails = remittanceRequestModel.getCollctionModeDto();
	 
	 BigDecimal colKnetAmount = BigDecimal.ZERO;
	 BigDecimal totalTrnxAmount = remittanceRequestModel.getTotalTrnxAmount();
	 BigDecimal percentage = new BigDecimal(5).divide(new BigDecimal(100));
	 BigDecimal percentageAmount = percentage.multiply(totalTrnxAmount);
	 CurrencyMasterMdlv1 currencyMaster =currencyDao.findOne(metaData.getDefaultCurrencyId());
	 BigDecimal decimalValue = currencyMaster.getDecinalNumber()==null?BigDecimal.ZERO:currencyMaster.getDecinalNumber();
	 BigDecimal totalAmount = RoundUtil.roundBigDecimal(percentageAmount.add(totalTrnxAmount),decimalValue.intValue());
	 
	 if(collectionDetails !=null && !collectionDetails.isEmpty()) {
		for(RemittanceCollectionDto collModel :collectionDetails) {
			PaymentModeModel payMode = getPaymentModeDetailsById(collModel.getPaymentModeId());
			if(payMode!= null && payMode.getPaymentModeCode().equalsIgnoreCase(ConstantDocument.KNET_CODE)) {
				LanguageType lan =new LanguageType();
				lan.setLanguageId(metaData.getLanguageId());
				PaymentModeDesc payModeDesc = payModeDescRespo.findByPaymentModeAndLanguageType(payMode, lan);
				colKnetAmount = collModel.getPaymentAmount();
				if (colKnetAmount.compareTo(totalAmount) >0){
					throw new  GlobalException(JaxError.AMOUNT_VALIDATION,"Amount is Greater Than Net Amount "+ payModeDesc.getLocalPaymentName()==null?"":payModeDesc.getLocalPaymentName() +" allow extra 5 % for the Net Amount To Pay :" +totalAmount);
				}
			}
			
		}
		  
	 }
	
 } 
 
 
	public Boolean sendReceiptOnEmail(BigDecimal collectionDocNo, BigDecimal collectionDocYear,
			BigDecimal collectionDocCode) {
		Boolean validStatus = Boolean.FALSE;
		PaymentResponseDto paymentResponse = new PaymentResponseDto();
		try {
			TransactionHistroyDTO trxnDto = new TransactionHistroyDTO();
			Customer customer = customerDao.getCustById(metaData.getCustomerId());
			paymentResponse.setCollectionDocumentCode(collectionDocCode);
			paymentResponse.setCollectionDocumentNumber(collectionDocNo);
			paymentResponse.setCollectionFinanceYear(collectionDocYear);

			trxnDto.setCollectionDocumentCode(collectionDocCode);
			trxnDto.setCollectionDocumentFinYear(collectionDocYear);
			trxnDto.setCollectionDocumentNo(collectionDocNo);
			trxnDto.setCustomerId(customer.getCustomerId());
			trxnDto.setCompanyId(metaData.getCompanyId());
			trxnDto.setLanguageId(metaData.getLanguageId());
			trxnDto.setApplicationCountryId(metaData.getCountryId());
			trxnDto.setCustomerReference(customer.getCustomerReference());
			reportManagerService.generatePersonalRemittanceReceiptReportDetails(trxnDto, Boolean.TRUE);
			List<RemittanceReceiptSubreport> rrsrl = reportManagerService.getRemittanceReceiptSubreportList();
			PersonInfo personinfo = new PersonInfo();
			try {
				BeanUtils.copyProperties(personinfo, customer);
			} catch (Exception e) {
			}

			if (personinfo != null && rrsrl != null && !StringUtils.isBlank(personinfo.getEmail())) {
				notificationService.sendTransactionNotification(rrsrl.get(0), personinfo,
						jaxNotificationDataManager.getTransactionSuccessEmailData());
				validStatus = Boolean.TRUE;
			}
		} catch (Exception e) {
			validStatus = Boolean.FALSE;
			throw new GlobalException(JaxError.UNKNOWN_JAX_ERROR, e.getMessage());
		}
		return validStatus;
	}
@SuppressWarnings("unchecked")
public void validateSaveTrnxDetails(HashMap<String, Object> mapAllDetailRemitSave ) {
	CollectionMdlv1 collectModel = (CollectionMdlv1) mapAllDetailRemitSave.get("EX_COLLECT");
	List<CollectDetailMdlv1> collectDetailsModel = (List<CollectDetailMdlv1>) mapAllDetailRemitSave.get("EX_COLLECT_DET");

	Map<BigDecimal,RemittanceTransaction> remitTrnxList = (Map<BigDecimal,RemittanceTransaction>) mapAllDetailRemitSave.get("EX_REMIT_TRNX");
	Map<BigDecimal,RemittanceBenificiary> remitBeneList = (Map<BigDecimal,RemittanceBenificiary>) mapAllDetailRemitSave.get("EX_REMIT_BENE");
	Map<BigDecimal,List<RemittanceAdditionalInstructionData>> addlTrnxList = (Map<BigDecimal,List<RemittanceAdditionalInstructionData>>) mapAllDetailRemitSave.get("EX_REMIT_ADDL");
	if(collectModel==null) {
		throw new GlobalException(JaxError.NO_RECORD_FOUND, "Collection data not found");
	}	
	if(collectDetailsModel==null || collectDetailsModel.isEmpty() ) {
		throw new GlobalException(JaxError.NO_RECORD_FOUND, "Collection details data not found");
	}
	if(remitTrnxList.isEmpty()) {
		throw new GlobalException(JaxError.NO_RECORD_FOUND, "Remittance trnx details not found");
	}
	if(remitBeneList.isEmpty()) {
		throw new GlobalException(JaxError.NO_RECORD_FOUND, "Remittance bene  details not found");
	}
	if(addlTrnxList.isEmpty()) {
		throw new GlobalException(JaxError.NO_RECORD_FOUND, "Remittance additional instruction details not found");
	}
}
	
	public Map<BigDecimal,RemitTrnxSrvProv> saveRemitTrnxSrvProv(BigDecimal remittanceApplicationId,String createdBy) {
		Map<BigDecimal,RemitTrnxSrvProv> mapRemitTrnxSrvProv = null;
		RemitTrnxSrvProv remitTrnxSrvProv = null;
		
		RemitApplSrvProv applSrvProv = remitApplSrvProvRepository.findByRemittanceApplicationId(remittanceApplicationId);

		if (applSrvProv != null) {
			mapRemitTrnxSrvProv = new HashMap<>();
			remitTrnxSrvProv = new RemitTrnxSrvProv();
			remitTrnxSrvProv.setAmgSessionId(applSrvProv.getAmgSessionId());
			remitTrnxSrvProv.setBankId(applSrvProv.getBankId());
			remitTrnxSrvProv.setFixedCommInSettlCurr(applSrvProv.getFixedCommInSettlCurr());
			remitTrnxSrvProv.setIntialAmountInSettlCurr(applSrvProv.getIntialAmountInSettlCurr());
			remitTrnxSrvProv.setPartnerReferenceNo(applSrvProv.getPartnerReferenceNo());
			remitTrnxSrvProv.setPartnerSessionId(applSrvProv.getPartnerSessionId());
			remitTrnxSrvProv.setSettlementCurrency(applSrvProv.getSettlementCurrency());
			remitTrnxSrvProv.setTransactionMargin(applSrvProv.getTransactionMargin());
			remitTrnxSrvProv.setVariableCommInSettlCurr(applSrvProv.getVariableCommInSettlCurr());
			remitTrnxSrvProv.setCreatedBy(createdBy);
			remitTrnxSrvProv.setCreatedDate(new Date());
			remitTrnxSrvProv.setOfferExpirationDate(applSrvProv.getOfferExpirationDate());
			remitTrnxSrvProv.setOfferStartingDate(applSrvProv.getOfferStartingDate());
			
			mapRemitTrnxSrvProv.put(remittanceApplicationId, remitTrnxSrvProv);
		}
		
		return mapRemitTrnxSrvProv;
	}

	
	Map<BigDecimal,List<RemittanceTransactionSplitting>>  saveRemitTrnxSplit(RemittanceApplication appl, RemittanceTransaction remitTrnx){
		remitSplitMap = new HashMap<>(); 
		
		List<RemittanceTransactionSplitting> trnxList = new ArrayList<>();
		List<RemittanceApplicationSplitting> applSplitList = applSplitRepo.findByRemittanceApplicationId(appl);
		
		
		if(applSplitList !=null && !applSplitList.isEmpty()) {
			
			for (RemittanceApplicationSplitting remitApplSplit : applSplitList) {
				RemittanceTransactionSplitting remitTrnxSplit = new RemittanceTransactionSplitting();
				remitTrnxSplit.setAccountMmyyyy(remitTrnx.getAccountMmyyyy());
				remitTrnxSplit.setCreatedDate(new Date());
				remitTrnxSplit.setCreatedBy(remitTrnx.getCreatedBy());
				remitTrnxSplit.setDocumentDate(remitTrnx.getDocumentDate());
				remitTrnxSplit.setDocumentFinanceYear(remitTrnx.getDocumentFinanceYear());
				remitTrnxSplit.setDocumentNo(remitTrnx.getDocumentNo());
				remitTrnxSplit.setForeignTranxAmount(remitApplSplit.getForeignTranxAmount());
				remitTrnxSplit.setIsactive(ConstantDocument.Yes);
				remitTrnxSplit.setSplitDocumentNo(remitApplSplit.getSplitDocumentNo());
				remitTrnxSplit.setDocumentId(remitTrnx.getDocumentId().getDocumentID());
				trnxList.add(remitTrnxSplit);
				
			}
			}
		remitSplitMap.put(appl.getRemittanceApplicationId(), trnxList);
		
		return remitSplitMap;
	}
	
}
