package com.amx.jax.branchremittance.manager;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.branchremittance.dao.BranchRemittanceDao;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyWiseDenomination;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ForeignCurrencyAdjust;
import com.amx.jax.dbmodel.PaymentModeModel;
import com.amx.jax.dbmodel.UserFinancialYear;
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
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchApplicationDto;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.RemittanceCollectionDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.TransferDto;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.ForeignCurrencyAdjustRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.IRemitApplAmlRepository;
import com.amx.jax.repository.PaymentModeRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.remittance.LocalBankDetailsRepository;
import com.amx.jax.service.CompanyService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;

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
	
	
	
	/**
	 * 
	 * @param remittanceRequestModel
	 * @return : saveing application to remittance
	 */
	
	@Transactional
	public RemittanceResponseDto saveRemittance(BranchRemittanceRequestModel remittanceRequestModel) {
		RemittanceResponseDto responseDto  = new RemittanceResponseDto();
		
		try {
			List<BranchApplicationDto> shoppingCartList = new ArrayList<>();
			shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
			updateApplicationStatus(shoppingCartList);
			
			CollectionModel 			collectionModel 	    =saveCollect(remittanceRequestModel);
			List<CollectDetailModel> 	collectionDetails		=saveCollectionDetail(remittanceRequestModel,collectionModel);
			List<ForeignCurrencyAdjust> currencyAdjustList 		=saveForeignCurrencyAdjust(remittanceRequestModel,collectionModel);
			List<RemittanceTransaction> remitTrnxList      		=saveRemittanceTrnx(remittanceRequestModel,collectionModel);
			List<RemittanceBenificiary> remitBeneList      		=saveBeneTrnx(remitTrnxList);
			List<RemittanceAdditionalInstructionData> addInstList=saveRemitnaceinstructionData(remitTrnxList);
			List<RemittanceAml>			amlList					=saveRemittanceAml(remitTrnxList);											
			LoyaltyClaimRequest loyaltyClaim          			=saveLoyalTyClaimRequest(collectionDetails);
			List<LoyaltyPointsModel> loyaltyPoints 				= saveLoyaltyPoints(remitTrnxList);
					
					
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
			responseDto = brRemittanceDao.saveRemittanceTransaction(mapAllDetailRemitSave);
			
		}catch (GlobalException e) {
			logger.error("routing  procedure", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
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
	
	//Save temp collection 
	private CollectionModel  saveCollect(BranchRemittanceRequestModel remittanceRequestModel) {
	
		CollectionModel collection = new CollectionModel();
		List<BranchApplicationDto> shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
		
		try {
			if(shoppingCartList!=null && !shoppingCartList.isEmpty()) {
				
				//RemittanceApplication appl =  remittanceApplicationRepository.findOne(shoppingCartList.get(0).getApplicationId());
				
				Customer customerid = new Customer();
				customerid.setCustomerId(metaData.getCustomerId());
				RemittanceApplication appl =  remittanceApplicationRepository.getApplicationForRemittance(customerid,shoppingCartList.get(0).getApplicationId());
				
				
				collection.setApplicationCountryId(appl.getFsCountryMasterByApplicationCountryId().getCountryId());
				collection.setFsCustomer(appl.getFsCustomer());
				collection.setCollectDate(new Date());
				collection.setExCurrencyMaster(appl.getExCurrencyMasterByLocalTranxCurrencyId());
				collection.setPaidAmount(remittanceRequestModel.getPaidAmount());
				collection.setNetAmount(remittanceRequestModel.getTotalTrnxAmount());
				collection.setRefoundAmount(remittanceRequestModel.getPaidAmount().subtract(remittanceRequestModel.getTotalTrnxAmount()));
				collection.setAccountMMYYYY(appl.getAccountMmyyyy());
				collection.setCompanyCode(appl.getCompanyCode());
				collection.setLocCode(appl.getLoccod());
				collection.setDocumentFinanceYear(appl.getDocumentFinancialyear());
				collection.setDocumentId(documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION).get(0).getDocumentID());
				collection.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
				 BigDecimal documentNo =generateDocumentNumber(appl.getFsCountryMasterByApplicationCountryId().getCountryId(),appl.getFsCompanyMaster().getCompanyId(),collection.getDocumentId(),collection.getDocumentFinanceYear(),appl.getExCountryBranch().getBranchId());
				
				if(documentNo!=null && documentNo.compareTo(BigDecimal.ZERO)!=0){
			    	collection.setDocumentNo(documentNo);
			    }else{
			    	throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO, "Collection document should not be blank.");
			    }
				collection.setReceiptType(ConstantDocument.COLLECTION_RECEIPT_TYPE);
				collection.setCreatedDate(new Date());
				EmployeeDetailsView employee =branchRemittanceApplManager.getEmployeeDetails();
				collection.setCreatedBy(employee.getUserName());
				BigDecimal declarationTotalamount = getDeclarationReportAmount(ConstantDocument.DECL_REPORT_FOR_TOT_AMOUNT);
				if(collection.getNetAmount().compareTo(declarationTotalamount)>=1) {
					collection.setCashDeclarationIndicator(ConstantDocument.Yes);
				}
				collection.setIsActive(ConstantDocument.Yes);
				collection.setExBankBranch(appl.getExCountryBranch());
				collection.setFsCompanyMaster(appl.getFsCompanyMaster());
				collection.setTotalAmountDeclarationIndicator(null); //ned to check
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
					
					collectDetails.setDocumentCode(collect.getDocumentCode());
					collectDetails.setExBankBranch(collect.getExBankBranch());
					collectDetails.setDocumentDate(new Date());
					collectDetails.setDocumentLineNo(new BigDecimal(i++));
					collectDetails.setExCurrencyMaster(collect.getExCurrencyMaster());
					collectDetails.setCollAmt(new BigDecimal(1));
					PaymentModeModel payMode = paymentModeRepository.getPaymentModeDetails(ConstantDocument.VOCHERCODE);
					collectDetails.setCollectionMode(payMode.getPaymentModeCode());
					collectDetails.setPaymentModeId(payMode.getPaymentModeId());
					
					collectDetails.setVoucherYear(collect.getDocumentFinanceYear());
					
					BigDecimal documentNo = generateDocumentNumber(collect.getApplicationCountryId(),
							collect.getFsCompanyMaster().getCompanyId(),ConstantDocument.VOUCHER_DOCUMENT_CODE,collect.getDocumentFinanceYear(),collect.getLocCode());
					
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
		UserStockDto currencyRefundDenomination = remittanceRequestModel.getCurrencyRefundDenomination();
		
		int i = 0;
		int j=0;
		/** For Collection **/
			for (RemittanceCollectionDto collectDataTable : collectionDetails) {
				UserStockDto currencyCashDenomination = collectDataTable.getCurrencyDenomination();
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
					
					CurrencyWiseDenomination denominationMaster = new CurrencyWiseDenomination();
					denominationMaster.setDenominationId(currencyCashDenomination.getDenominationId());
					foreignCurrencyAdjust.setFsDenominationId(denominationMaster);
					foreignCurrencyAdjust.setExchangeRate(appl.getExchangeRateApplied());
					foreignCurrencyAdjust.setDenaminationAmount(currencyCashDenomination.getDenominationAmount());
					foreignCurrencyAdjust.setDocumentFinanceYear(collect.getDocumentFinanceYear());
					foreignCurrencyAdjust.setDocumentCode(ConstantDocument.COLLECTION_DOCUMENT_ID);
					foreignCurrencyAdjust.setDocumentLineNumber(new BigDecimal(++i));
					foreignCurrencyAdjust.setAccountmmyyyy(collect.getAccountMMYYYY());
					foreignCurrencyAdjust.setCountryBranch(collect.getExBankBranch());
					foreignCurrencyAdjust.setProgNumber(ConstantDocument.FC_SALE_REMIT);
					foreignCurrencyAdjust.setDocumentStatus(ConstantDocument.Yes);
					foreignCurrencyAdjust.setTransactionType(ConstantDocument.C);
					
					foreignCurrencyAdjust.setCreatedDate(new Date());
					foreignCurrencyAdjust.setCreatedBy(collect.getCreatedBy());
					foreignCurrencyAdjust.setCollect(collect);
					
					currencyAdjustListList.add(foreignCurrencyAdjust);
					
				}
				
			}
		/** For refund **/
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
				
				foreignCurrencyRefundAdjust.setCreatedDate(new Date());
				foreignCurrencyRefundAdjust.setCreatedBy(collect.getCreatedBy());
				foreignCurrencyRefundAdjust.setCollect(collect);
				currencyAdjustListList.add(foreignCurrencyRefundAdjust);
			}
			
		
		return  currencyAdjustListList;
	}
	
	
	
	
	public List<RemittanceTransaction> saveRemittanceTrnx(BranchRemittanceRequestModel remittanceRequestModel,CollectionModel  collect)
	{
		List<RemittanceTransaction> remitTrnxList      =new ArrayList<>();
		
		List<BranchApplicationDto> shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
		
		if(shoppingCartList!=null && !shoppingCartList.isEmpty()) {
			
			for(BranchApplicationDto applDto : shoppingCartList) {
				RemittanceTransaction remitTrnx = new RemittanceTransaction();
				//RemittanceApplication appl =  remittanceApplicationRepository.findOne(shoppingCartList.get(0).getApplicationId());
				
				
				Customer customerid = new Customer();
				customerid.setCustomerId(metaData.getCustomerId());
				RemittanceApplication appl =  remittanceApplicationRepository.getApplicationForRemittance(customerid,shoppingCartList.get(0).getApplicationId());
				
				
				if(appl!=null && appl.getIsactive().equalsIgnoreCase(ConstantDocument.Yes)) {
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
					remitTrnx.setBlackListIndicator(appl.getBlackListIndicator());
					remitTrnx.setBranchId(appl.getExCountryBranch());
					remitTrnx.setCollectionDocCode(collect.getDocumentCode());
					remitTrnx.setCollectionDocFinanceYear(collect.getDocumentFinanceYear());
					remitTrnx.setCollectionDocId(collect.getDocumentId());
					remitTrnx.setCollectionDocumentNo(collect.getDocumentNo());
					remitTrnx.setCompanyId(appl.getFsCompanyMaster());
					remitTrnx.setCompanyCode(appl.getCompanyCode());
					remitTrnx.setCreatedBy(collect.getCreatedBy());
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
					BigDecimal documentNo =generateDocumentNumber(appl.getFsCountryMasterByApplicationCountryId().getCountryId(),appl.getFsCompanyMaster().getCompanyId(),remitTrnx.getDocumentId().getDocumentCode(),remitTrnx.getDocumentFinanceYear(),remitTrnx.getBranchId().getBranchId());
					
					if(documentNo!=null && documentNo.compareTo(BigDecimal.ZERO)!=0){
						remitTrnx.setDocumentNo(documentNo);
				    }else{
				    	throw new GlobalException(JaxError.INVALID_REMITTANCE_DOCUMENT_NO, "Document Seriality  setup  not defined for Remittance.");
				    }
					
					 
					
					//remitTrnx.setDwFlag(dwFlag); //NC
					//
					TransferDto trnaferType = getTrasnferModeByBankServiceRule(remitTrnx);
					
					remitTrnx.setEmployeeId(metaData.getEmployeeId());
					remitTrnx.setExchangeRateApplied(appl.getExchangeRateApplied());
					remitTrnx.setFileCreation(trnaferType.getTrasnferMode());
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
					remitTrnx.setCustomerSignatureClob(appl.getCustomerSignatureClob());
					remitTrnx.setTransferMode(trnaferType.getTrasnferMode());
					remitTrnx.setTransferModeId(trnaferType.getTransferModeId());
					remitTrnx.setUsdAmount(appl.getUsdAmt());
					remitTrnx.setWesternUnionMtcno(appl.getWesternUnionMtcno());
					remitTrnx.setWuIpAddress(metaData.getDeviceIp());
					remitTrnxList.add(remitTrnx);
				}
			}
			
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record found to save in remittance");
		}
		
		return remitTrnxList;
	}
	
	
	
public   List<RemittanceBenificiary>  saveBeneTrnx(List<RemittanceTransaction> remitTrnxList){
	List<RemittanceBenificiary> remitBeneList    = new ArrayList<>();
	for(RemittanceTransaction remitTrnx : remitTrnxList) {
		RemittanceAppBenificiary applBene = applBeneRepository.findOne(remitTrnx.getApplicationDocumentNo());
		if(applBene!=null) {
			RemittanceBenificiary remitBene = new RemittanceBenificiary();
			
			remitBene.setBeneficiaryAccountNo(applBene.getBeneficiaryAccountNo());
			remitBene.setBeneficiaryAccountSeqId(applBene.getBeneficiaryAccountSeqId());
			remitBene.setBeneficiaryBank(remitBene.getBeneficiaryBank());
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
			remitBene.setBeneficiarySwiftBank2Id(remitBene.getBeneficiarySwiftBank2Id());
			remitBene.setBeneficiaryTelephoneNumber(remitBene.getBeneficiaryTelephoneNumber());
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
			
			remitBeneList.add(remitBene);

		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record found in appl bene for remittacne :"+remitTrnx.getApplicationDocumentNo());
		}

		
	}
	
	return remitBeneList;
}
	
	

public   List<RemittanceAdditionalInstructionData>   saveRemitnaceinstructionData(List<RemittanceTransaction> remitTrnxList){
	 List<RemittanceAdditionalInstructionData> addInsData = new ArrayList<>();
	 
	 for(RemittanceTransaction remitTrnx : remitTrnxList) {
			AdditionalInstructionData applInstrucData = addInstrDataRepository.findOne(remitTrnx.getApplicationDocumentNo());
		
			if(applInstrucData!=null) {
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
			remitAddData.setDocumentFinanceYear(remitTrnx.getDocumentFinanceYear());
			remitAddData.setIsactive(ConstantDocument.Yes);
			addInsData.add(remitAddData);
			
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record found in appl additional instruction  :"+remitTrnx.getApplicationDocumentNo());
		}
	 }	
	 
	 return addInsData;
	
}


public List<RemittanceAml>	saveRemittanceAml(List<RemittanceTransaction> remitTrnxList){
	List<RemittanceAml>	amlList =new ArrayList<>();	
	
	 for(RemittanceTransaction remitTrnx : remitTrnxList) {
			RemitApplAmlModel applAml = applAmlRepository.findOne(remitTrnx.getApplicationDocumentNo());
			if(applAml!=null) {
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
				amlList.add(remitAml);
				
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
				Lclaim.setClaimPoints(new BigDecimal(1000));
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


public List<LoyaltyPointsModel> saveLoyaltyPoints(List<RemittanceTransaction> remittTransactionList){
	 List<LoyaltyPointsModel> loyaltyPoints = new ArrayList<>();
		
		if(remittTransactionList!=null && !remittTransactionList.isEmpty()) {
			for(RemittanceTransaction applDto : remittTransactionList) {
			
				if(applDto!=null && applDto.getLoyaltyPointsInd().equalsIgnoreCase(ConstantDocument.Yes)) {
					LoyaltyPointsModel lpoints = new LoyaltyPointsModel();
					
					lpoints.setAccMonth(applDto.getAccountMmyyyy());
					lpoints.setCustomerReference(applDto.getCustomerRef());
					lpoints.setCompCode(applDto.getCompanyCode());
					lpoints.setTransDate(new Date());
					lpoints.setLoyaltyPoints(new BigDecimal(-1000));
					lpoints.setDocfyr(applDto.getDocumentFinanceYear());
					lpoints.setTrnRefNo(applDto.getDocumentNo());
					lpoints.setType(ConstantDocument.CLAIM);
					lpoints.setCalcFlag(ConstantDocument.R);
					lpoints.setProcessDate(new Date());
					lpoints.setDocCode(applDto.getDocumentCode());
					lpoints.setProcessDate(DateUtil.daysAddInCurrentDate(365));
					loyaltyPoints.add(lpoints);
				}
			}
		}
	 return loyaltyPoints;
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
	
public BigDecimal generateDocumentNumber(BigDecimal appCountryId,BigDecimal companyId,BigDecimal documentId,BigDecimal finYear,BigDecimal branchId) {
	Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId,finYear, ConstantDocument.Update, branchId);
	return (BigDecimal) output.get("P_DOC_NO");
	}





 public TransferDto getTrasnferModeByBankServiceRule(RemittanceTransaction remitTrnx){
	 Map<String,Object> mapBankServiceRule= routingProDao.checkBankServiceRule(remitTrnx);
	 TransferDto dto = new TransferDto();
	 String transferMode=null;
	 BigDecimal transferModeId=BigDecimal.ZERO;
	 if(mapBankServiceRule!=null) {
		 transferMode = mapBankServiceRule.get("P_TRANSFER_MODE")==null?"":mapBankServiceRule.get("P_TRANSFER_MODE").toString();
		 transferModeId = mapBankServiceRule.get("TRANSFER_MODE_ID")==null?BigDecimal.ZERO:(BigDecimal)mapBankServiceRule.get("P_TRANSFER_MODE_ID");
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
	 UserFinancialYear financeYearObj = new UserFinancialYear();
	 financeYearObj.setFinancialYear(financeYear);
	 return financeYearObj;
 }

}
