package com.amx.jax.branchremittance.manager;


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
import com.amx.jax.branchremittance.dao.BranchRemittanceDao;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.JaxEmployeeDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.CurrencyWiseDenomination;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ForeignCurrencyAdjust;
import com.amx.jax.dbmodel.LanguageType;
import com.amx.jax.dbmodel.PaymentModeDesc;
import com.amx.jax.dbmodel.PaymentModeModel;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.bene.BankBlWorld;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
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
import com.amx.jax.dbmodel.remittance.RemittanceBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
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
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.IBeneBankBlackCheckDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.IPaymentModeDescRespo;
import com.amx.jax.repository.IPlaceOrderDao;
import com.amx.jax.repository.IRemitApplAmlRepository;
import com.amx.jax.repository.IRemittanceTransactionRepository;
import com.amx.jax.repository.IShoppingCartDetailsRepository;
import com.amx.jax.repository.PaymentModeRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.remittance.LocalBankDetailsRepository;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.service.JaxEmailNotificationService;
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
	
	
	//List<RemittanceAml>			amlList	 = new ArrayList<>();
	//List<RemittanceBenificiary> remitBeneList   = new ArrayList<>();
	//List<RemittanceAdditionalInstructionData> addInstList = new ArrayList<>();
	
	List<LoyaltyPointsModel> loyaltyPoints 	 = new ArrayList<>();
	Map<BigDecimal,RemittanceBenificiary> remitBeneList = new HashMap<>();
	Map<BigDecimal,List<RemittanceAdditionalInstructionData>> addInstList = new HashMap<>();
	Map<BigDecimal,List<RemittanceAml>>			amlList	 = new HashMap<>();
	
	
	@Autowired
    AuditService auditService;
	
	/**
	 * 
	 * @param remittanceRequestModel
	 * @return : saveing application to remittance
	 */
	

	public RemittanceResponseDto saveRemittanceTrnx(BranchRemittanceRequestModel remittanceRequestModel) {
		logger.debug("saveRemittanceTrnx request model : {}", JsonUtil.toJson(remittanceRequestModel));
		List<BranchApplicationDto> shoppingCartList = new ArrayList<>();
		shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
		RemittanceResponseDto responseDto = saveRemittance(remittanceRequestModel);
		
		if(responseDto!=null && JaxUtil.isNullZeroBigDecimalCheck(responseDto.getCollectionDocumentNo())) {
			brRemittanceDao.updateApplicationToMoveEmos(responseDto);
			PaymentResponseDto paymentResponse = new PaymentResponseDto();
			paymentResponse.setCollectionDocumentCode(responseDto.getCollectionDocumentCode());
			paymentResponse.setCollectionDocumentNumber(responseDto.getCollectionDocumentNo());
			paymentResponse.setCollectionFinanceYear(responseDto.getCollectionDocumentFYear());
			paymentResponse.setCompanyId(metaData.getCompanyId());
			paymentResponse.setApplicationCountryId(metaData.getCountryId());
			paymentResponse.setCustomerId(metaData.getCustomerId());
			remittanceApplicationService.saveRemittancetoOldEmos(paymentResponse);
		}else {
			logger.error("NOT moved to old emos ", responseDto.getCollectionDocumentNo() + "" +responseDto.getCollectionDocumentCode()+" "+responseDto.getCollectionDocumentFYear());
		}
		return responseDto;
	}
	
	
	public RemittanceResponseDto saveRemittance(BranchRemittanceRequestModel remittanceRequestModel) {
		RemittanceResponseDto responseDto  = new RemittanceResponseDto();
			
		try {
			CollectionModel 			collectionModel 	    =saveCollect(remittanceRequestModel);
			List<CollectDetailModel> 	collectionDetails		=saveCollectionDetail(remittanceRequestModel,collectionModel);
			List<ForeignCurrencyAdjust> currencyAdjustList 		=saveForeignCurrencyAdjust(remittanceRequestModel,collectionModel);
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
			validateSaveTrnxDetails(mapAllDetailRemitSave);
			responseDto = brRemittanceDao.saveRemittanceTransaction(mapAllDetailRemitSave);
			auditService.log(new CActivityEvent(Type.TRANSACTION_CREATED,String.format("{}/{}", responseDto.getCollectionDocumentFYear(),responseDto.getCollectionDocumentNo())).field("STATUS").to(JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_SUCCESS).result(Result.DONE));
	}catch (GlobalException e) {
			logger.error("routing  procedure", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		}finally {
			amlList	 = new HashMap<>();
			remitBeneList   = new HashMap<>();
			addInstList = new HashMap<>();
			loyaltyPoints 	 = new ArrayList<>();
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
			logger.error("create collection", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}
		
	}
	

	private CollectionModel  saveCollect(BranchRemittanceRequestModel remittanceRequestModel) {
	
		CollectionModel collection = new CollectionModel();
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
				 BigDecimal documentNo =generateDocumentNumber(appl.getFsCountryMasterByApplicationCountryId().getCountryId(),appl.getFsCompanyMaster().getCompanyId(),collection.getDocumentId(),collection.getDocumentFinanceYear(),appl.getExCountryBranch().getBranchId(),ConstantDocument.A);
				
				if(documentNo!=null && documentNo.compareTo(BigDecimal.ZERO)!=0){
			    	collection.setDocumentNo(documentNo);
			    }else{
			    	throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO, "Collection document should not be blank.");
			    }
				collection.setReceiptType(ConstantDocument.COLLECTION_RECEIPT_TYPE);
				collection.setCreatedDate(new Date());
				EmployeeDetailsView employee =branchRemittanceApplManager.getEmployeeDetails();
				collection.setCreatedBy(employee.getUserName());
				collection.setLocCode(employee.getBranchId());
				BigDecimal declarationTotalamount = getDeclarationReportAmount(ConstantDocument.DECL_REPORT_FOR_TOT_AMOUNT);
				if(collection.getNetAmount().compareTo(declarationTotalamount)>=1) {
					collection.setCashDeclarationIndicator(ConstantDocument.Yes);
				}
				collection.setIsActive(ConstantDocument.Yes);
				
				CountryBranch countryBranch = new CountryBranch();
				if(employee!=null && JaxUtil.isNullZeroBigDecimalCheck(employee.getCountryBranchId())) {
					countryBranch.setCountryBranchId(employee.getCountryBranchId());
				}else {
					countryBranch.setCountryBranchId(metaData.getCountryBranchId());
				}
				collection.setExBankBranch(countryBranch);
				collection.setFsCompanyMaster(appl.getFsCompanyMaster());
				collection.setTotalAmountDeclarationIndicator(null); //ned to check
				}else {
					throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record found to save in collection"+customerid+"\t appl No :"+shoppingCartList.get(0).getApplicationId());
				}
			}else {
				throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record found to save in collection");
			}
			
		}catch(GlobalException e){
			logger.error("create collection", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}
		return collection;
	}
	
	
	
	
	public List<CollectDetailModel> saveCollectionDetail(BranchRemittanceRequestModel remittanceRequestModel,CollectionModel collect){
		
		List<CollectDetailModel> collectionDetailModelList = new ArrayList<CollectDetailModel>();
		List<BranchApplicationDto> shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
		
		int i = 1;
		BigDecimal totalAmt = BigDecimal.ZERO;
		List<RemittanceCollectionDto> collectionDetails = remittanceRequestModel.getCollctionModeDto();
		
		
		for (RemittanceCollectionDto collectDataTable : collectionDetails) {
			
			CollectDetailModel collectDetails = new CollectDetailModel();
			
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
				BankMasterModel Model = getPosBankDetails(collectDataTable.getPosBankCode());
				collectDetails.setPosBankId(Model.getBankId());
				
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
					CollectDetailModel collectDetails = new CollectDetailModel();
					
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
	
	
	
	
	
	
	
	
	public List<ForeignCurrencyAdjust> saveForeignCurrencyAdjust(BranchRemittanceRequestModel remittanceRequestModel,CollectionModel  collect){
		
		List<ForeignCurrencyAdjust> currencyAdjustListList = new ArrayList<>();
		
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
					ForeignCurrencyAdjust foreignCurrencyAdjust = new ForeignCurrencyAdjust();
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
					
					
					CurrencyWiseDenomination denominationMaster = new CurrencyWiseDenomination();
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
				ForeignCurrencyAdjust foreignCurrencyRefundAdjust = new ForeignCurrencyAdjust();
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
				
				CurrencyWiseDenomination denominationMaster = new CurrencyWiseDenomination();
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
	
	
	
	
	public Map<BigDecimal,RemittanceTransaction> saveRemittanceTrnx(BranchRemittanceRequestModel remittanceRequestModel,CollectionModel  collect)
	{
		//List<RemittanceTransaction> remitTrnxList      =new ArrayList<>();
		
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
					remitTrnx.setInstruction(null);
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
					remitTrnx.setFileCreation(trnaferType.getTrasnferMode());
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
					
					
					BigDecimal documentNo =generateDocumentNumber(appl.getFsCountryMasterByApplicationCountryId().getCountryId(),appl.getFsCompanyMaster().getCompanyId(),remitTrnx.getDocumentId().getDocumentCode(),remitTrnx.getDocumentFinanceYear(),remitTrnx.getBranchId().getBranchId(),ConstantDocument.A);
					
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
				}
			}
			
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record not found to save in remittance");
		}
		
		return remitTrnxList;
	}
	

	public  Map<BigDecimal,RemittanceBenificiary>  saveBeneTrnx(RemittanceApplication applicationNo,RemittanceTransaction remitTrnx){
	if(applicationNo!=null) {
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
			//addInstList.add(remitAddData);
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

private LoyaltyClaimRequest saveLoyalTyClaimRequest(List<CollectDetailModel> collectDetailModelList) {
	LoyaltyClaimRequest Lclaim = new LoyaltyClaimRequest();
	if(collectDetailModelList!=null && !collectDetailModelList.isEmpty()) {
		for(CollectDetailModel collectDetail : collectDetailModelList) {
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




public void collectedAmountValidation(CollectionModel collectionModel,List<CollectDetailModel> collectionDetails,List<ForeignCurrencyAdjust> currencyAdjustList){

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
		if(totalPaidAmount.subtract(refundAmount).compareTo(netAmount)!=0) {
			throw new GlobalException(JaxError.AMOUNT_MISMATCH,"There is a mismatch found in the Net amount and Refunded amount");
			}
		
		}else {
			throw new GlobalException(JaxError.AMOUNT_MISMATCH,"There is a mismatch found in the Net amount and Refunded amount");
		}
	
	
	if(collectionDetails!=null && !collectionDetails.isEmpty()) {
		totalCollectedAmount = collectionDetails.stream().map(CollectDetailModel::getCollAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalCashAmount      =collectionDetails.stream().filter(c->c.getCollectionMode().equalsIgnoreCase(ConstantDocument.CASH)).map(CollectDetailModel::getCollAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
	}else {
		throw new GlobalException(JaxError.AMOUNT_MISMATCH,"The amount you have entered is 0. Please enter the correct amount ");
	}
	
	if(totalPaidAmount.compareTo(totalCollectedAmount)!=0) {
		throw new GlobalException(JaxError.AMOUNT_MISMATCH,"The collection amount does not match with the collection details as per payment mode selected.");
	}
	
	
	if(totalCashAmount.compareTo(BigDecimal.ZERO)>0) {
		if(currencyAdjustList!=null && !currencyAdjustList.isEmpty()) {
			BigDecimal totalCurrencyAdjustCollect =BigDecimal.ZERO;
			BigDecimal totalCurrencyAdjustRefund=BigDecimal.ZERO;
			totalCurrencyAdjustCollect = currencyAdjustList.stream().filter(a->a.getTransactionType().equalsIgnoreCase(ConstantDocument.CASH)).map(ForeignCurrencyAdjust::getAdjustmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalCurrencyAdjustRefund  =currencyAdjustList.stream().filter(a->a.getTransactionType().equalsIgnoreCase(ConstantDocument.F)).map(ForeignCurrencyAdjust::getAdjustmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
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
		AuthenticationLimitCheckView authParam = authenticationLimitCheck.findByAuthorizationType(authType);
		BigDecimal declarationTotalamount = authParam.getAuthLimit()==null?BigDecimal.ZERO:authParam.getAuthLimit();
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
	
	
	public BankMasterModel getPosBankDetails(String bankCode) {
		BankMasterModel bankMaster = bankMasterRepo.findByBankCodeAndRecordStatus(bankCode,ConstantDocument.Yes);
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
	 TransferDto dto = new TransferDto();
	 String transferMode=null;
	 BigDecimal transferModeId=BigDecimal.ZERO;
	 if(mapBankServiceRule!=null) {
		 transferMode = mapBankServiceRule.get("P_TRANSFER_MODE")==null?"":mapBankServiceRule.get("P_TRANSFER_MODE").toString();
		 transferModeId = mapBankServiceRule.get("P_TRANSFER_MODE_ID")==null?BigDecimal.ZERO:(BigDecimal)mapBankServiceRule.get("P_TRANSFER_MODE_ID");
		 if(transferMode!=null && transferMode.equalsIgnoreCase(ConstantDocument.FILE_CREATION)) {
			 transferMode=ConstantDocument.Yes;
		 }else if(transferMode!=null && transferMode.equalsIgnoreCase(ConstantDocument.WEB_SERVICE)) {
			 transferMode=ConstantDocument.No;
		 }
		 if(!JaxUtil.isNullZeroBigDecimalCheck(transferModeId)){
			 throw new GlobalException(JaxError.NO_RECORD_FOUND,"Please check bank service rule.:"+remitTrnx);
		 }
		 
	 }else {
		 throw new GlobalException(JaxError.NO_RECORD_FOUND,"Please check bank service rule.:"+remitTrnx);
	 }
	
	 dto.setTransferModeId(transferModeId);
	 dto.setTrasnferMode(transferMode);
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
		 throw new GlobalException(JaxError.CUSTOMER__SIGNATURE_UNAVAILABLE, e.getMessage());
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
	 CurrencyMasterModel currencyMaster =currencyDao.findOne(metaData.getDefaultCurrencyId());
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
				notificationService.sendTransactionNotification(rrsrl.get(0), personinfo);
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
	CollectionModel collectModel = (CollectionModel) mapAllDetailRemitSave.get("EX_COLLECT");
	List<CollectDetailModel> collectDetailsModel = (List<CollectDetailModel>) mapAllDetailRemitSave.get("EX_COLLECT_DET");

	Map<BigDecimal,RemittanceTransaction> remitTrnxList = (Map<BigDecimal,RemittanceTransaction>) mapAllDetailRemitSave.get("EX_REMIT_TRNX");
	Map<BigDecimal,RemittanceBenificiary> remitBeneList = (Map<BigDecimal,RemittanceBenificiary>) mapAllDetailRemitSave.get("EX_REMIT_BENE");
	Map<BigDecimal,List<RemittanceAdditionalInstructionData>> addlTrnxList = (Map<BigDecimal,List<RemittanceAdditionalInstructionData>>) mapAllDetailRemitSave.get("EX_REMIT_ADDL");
	//List<RemittanceTransaction> remitTrnxList = (List<RemittanceTransaction>) mapAllDetailRemitSave.get("EX_REMIT_TRNX");
	//List<RemittanceBenificiary> remitBeneList = (List<RemittanceBenificiary>) mapAllDetailRemitSave.get("EX_REMIT_BENE");
	//List<RemittanceAdditionalInstructionData> addlTrnxList = (List<RemittanceAdditionalInstructionData>) mapAllDetailRemitSave.get("EX_REMIT_ADDL");
	
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
}
