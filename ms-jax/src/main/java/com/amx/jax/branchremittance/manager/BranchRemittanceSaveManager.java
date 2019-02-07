package com.amx.jax.branchremittance.manager;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.CurrencyWiseDenomination;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ForeignCurrencyAdjust;
import com.amx.jax.dbmodel.PaymentModeModel;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.emp.Employee;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.remittance.LocalBankDetailsView;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
import com.amx.jax.model.response.remittance.RemittanceCollectionDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.PaymentModeRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.remittance.LocalBankDetailsRepository;
import com.amx.jax.service.CompanyService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.model.request.remittance.BranchApplicationDto;

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
	
	/**
	 * 
	 * @param remittanceRequestModel
	 * @return : saveing application to remittance
	 */
	
	public RemittanceResponseDto saveRemittance(BranchRemittanceRequestModel remittanceRequestModel) {
		RemittanceResponseDto responseDto  = new RemittanceResponseDto();
		
		try {
			List<BranchApplicationDto> shoppingCartList = new ArrayList<>();
			shoppingCartList = remittanceRequestModel.getRemittanceApplicationId();
			updateApplicationStatus(shoppingCartList);
			
			CollectionModel collectionModel = saveCollect(remittanceRequestModel);
			List<CollectDetailModel> collectionDetails= saveCollectionDetail(remittanceRequestModel,collectionModel);
			List<ForeignCurrencyAdjust> tempAdjustList = saveForeignCurrencyAdjust(remittanceRequestModel,collectionModel);
			
			
			
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
				
				RemittanceApplication appl =  remittanceApplicationRepository.findOne(shoppingCartList.get(0).getApplicationId());
				
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
		return collectionDetailModelList;
		
	}
	
	
	
	public List<ForeignCurrencyAdjust> saveForeignCurrencyAdjust(BranchRemittanceRequestModel remittanceRequestModel,CollectionModel  collect){
		List<ForeignCurrencyAdjust> currencyAdjustListList = new ArrayList<>();
		
		
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
					//foreignCurrencyAdjust.setExchangeRate(shoppingCartDetails.getExchangeRateApplied());
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
			/*	for() {
					
				}
		*/
		
		return  currencyAdjustListList;
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
}
