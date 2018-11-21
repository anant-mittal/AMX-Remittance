package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dao.FcSaleExchangeRateDao;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.PurposeOfTransaction;
import com.amx.jax.dbmodel.ReceiptPayment;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.SourceOfIncome;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.ShoppingCartDetailsDto;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.FxDeliveryDetailsRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.CustomerRepository;
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FxOrderPaymentManager {

	private static final Logger logger = LoggerFactory.getLogger(FxOrderPaymentManager.class);
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	private CustomerRepository custRepo;
	
	@Autowired
	FinancialService finanacialService;
	
	@Autowired
	ReceiptPaymentAppRepository receiptAppRepository;
	
	@Autowired
	FcSaleApplicationDao rcptApplPaydao;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	CountryBranchRepository countryBranchRepository;
	
	@Autowired
	IDocumentDao documentDao;
	
	@Autowired
	FcSaleApplicationTransactionManager applTrnxManager;

	@Autowired
	FxDeliveryDetailsRepository deliveryDetailRepos;
	
	@Autowired
	FcSaleExchangeRateDao fcSaleExchangeRateDao;
	

	
	public ApiResponse paymentCapture(PaymentResponseDto paymentResponse) {
		ApiResponse response = null;
		logger.info("paymment capture :"+paymentResponse.toString());
		logger.info("Customer Id :"+paymentResponse.getCustomerId());
		logger.info("Result code :"+paymentResponse.getResultCode()+"\t Auth Code :"+paymentResponse.getAuth_appNo());		
		logger.info("paymment capture Payment ID :"+paymentResponse.getPaymentId()+"\t Merchant Track Id :"+paymentResponse.getTrackId()+"\t UDF 3 :"+paymentResponse.getUdf3()+"\t Udf 2 :"+paymentResponse.getUdf2());
		
		
		List<ShoppingCartDetailsDto>  fxOrdershoppingCartList = new ArrayList<>();
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		List<ReceiptPaymentApp> listOfRecAppl =new ArrayList<>();
		try{
			
			if(!StringUtils.isBlank(paymentResponse.getPaymentId()) && !StringUtils.isBlank(paymentResponse.getResultCode()) 
			&& (paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.CAPTURED)|| paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.APPROVED))) 
					{
			/** update the payg details **/
			rcptApplPaydao.updatePaygDetails(listOfRecAppl, paymentResponse);
			/** To fetch the applciation **/
			 listOfRecAppl = receiptAppRepository.fetchreceiptPaymentAppl(paymentResponse.getCustomerId(), new BigDecimal(paymentResponse.getUdf3()));
			 
			 List<ReceiptPayment> receiptPayment=saveReceiptPayment(listOfRecAppl, paymentResponse);
			 CollectionModel collection = saveCollection(listOfRecAppl, paymentResponse);
			 CollectDetailModel collectDetail = saveCollectDetail(listOfRecAppl, paymentResponse);
			 
			 
				}else{
					logger.info("PaymentResponseDto "+paymentResponse.getPaymentId()+"\t Result :"+paymentResponse.getResultCode()+"\t Custoemr Id :"+paymentResponse.getCustomerId());
					listOfRecAppl = receiptAppRepository.fetchreceiptPaymentAppl(paymentResponse.getCustomerId(), new BigDecimal(paymentResponse.getUdf3()));
					if(!listOfRecAppl.isEmpty()) {
						rcptApplPaydao.updatePaygDetails(listOfRecAppl, paymentResponse);
					}
					response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
				}
		}catch(Exception e){
			logger.error("paymentCapture :"+e.getMessage());
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
		}
		return response;
	}
	
	/** Save ReceiptPayment 
	 * @param localFsCountryMaster **/
	 public List<ReceiptPayment> saveReceiptPayment(List<ReceiptPaymentApp> listOfRecAppl, PaymentResponseDto paymentResponse){
		 List<ReceiptPayment> receiptPaymentList =new ArrayList<>();
		 if(!listOfRecAppl.isEmpty()){
			 for(ReceiptPaymentApp applreceipt : listOfRecAppl){
				 
				 ReceiptPayment receiptPayment =new ReceiptPayment();
				 
				 receiptPayment.setApplicationDocumentNo(applreceipt.getDocumentNo());
				 receiptPayment.setApplicationFinanceYear(applreceipt.getDocumentFinanceYear());
				 
				 CompanyMaster comp = new CompanyMaster();
				 comp.setCompanyId(applreceipt.getCompanyId());
				 receiptPayment.setFsCompanyMaster(comp);
			     ViewCompanyDetails companyDetails = companyService.getCompanyDetailsById(applreceipt.getCompanyId());
			     receiptPayment.setCompanyCode(companyDetails.getCompanyCode());
				 
				 CountryMaster countryMas = new CountryMaster();
				 countryMas.setCountryId(applreceipt.getCountryId());
				 receiptPayment.setFsCountryMaster(countryMas);
				 
				 CurrencyMasterModel localCurrency = new CurrencyMasterModel();
				 localCurrency.setCurrencyId(applreceipt.getLocalCurrencyId());
				 receiptPayment.setLocalFsCountryMaster(localCurrency);
				 
				 
				 CurrencyMasterModel foreignCurrency = new CurrencyMasterModel();
				 foreignCurrency.setCurrencyId(applreceipt.getForeignCurrencyId());
				 receiptPayment.setForeignFsCountryMaster(foreignCurrency);
				 
				 receiptPayment.setAccountMMYYYY(applreceipt.getAccountMMYYYY());
				 receiptPayment.setCreatedDate(new Date());
				 receiptPayment.setDocumentDate(new Date());
				 
				 Customer customer = new Customer();
				 customer.setCustomerId(applreceipt.getCustomerId());
				 receiptPayment.setFsCustomer(customer);
				 receiptPayment.setCustomerName(applreceipt.getCustomerName());
				 receiptPayment.setCustomerReference(custRepo.findOne(applreceipt.getCustomerId()).getCustomerReference());
				
				 
				 CountryBranch countryBranch = countryBranchRepository.findByCountryBranchId(applreceipt.getBranchId());
				 if(countryBranch!=null){
					 receiptPayment.setCountryBranch(countryBranch);
					 receiptPayment.setLocCode(countryBranch.getBranchId());
				 }
				 
				 
				 
				 UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
				if(userFinancialYear!=null){
					receiptPayment.setDocumentFinanceYear(userFinancialYear.getFinancialYear());
					receiptPayment.setDocumentFinanceYearId(userFinancialYear.getFinancialYearID());
				}
			 
				 receiptPayment.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);
				 receiptPayment.setDocumentNo(applTrnxManager.generateDocumentNumber(countryBranch, ConstantDocument.Update, userFinancialYear.getFinancialYear()));
				 receiptPayment.setReceiptType(ConstantDocument.FC_SALE_RECEIPT_TYPE);
				 receiptPayment.setDocumentId(documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE).get(0).getDocumentID());
				 
				
				 receiptPayment.setLocalTrnxAmount(applreceipt.getLocalTrnxAmount());
				 receiptPayment.setLocalNetAmount(applreceipt.getLocalNetAmount());
				 receiptPayment.setForignTrnxAmount(applreceipt.getForignTrnxAmount());
				 receiptPayment.setTransactionIPAddress(metaData.getDeviceIp());
				 receiptPayment.setTransactionType(applreceipt.getTransactionType());
				 receiptPayment.setTransactionActualRate(applreceipt.getTransactionActualRate());
				 receiptPayment.setDocumentStatus(applreceipt.getDocumentStatus());
				 
				 PurposeOfTransaction purpose = new PurposeOfTransaction();
				 purpose.setPurposeId(applreceipt.getPurposeofTransactionId());
				 receiptPayment.setPurposeOfTransaction(purpose);
				 
				 SourceOfIncome sourceOfIncome = new SourceOfIncome();
				 sourceOfIncome.setSourceId(applreceipt.getSourceofIncomeId());
				 receiptPayment.setSourceOfIncome(sourceOfIncome);
				 
				// receiptPayment.setDeliveryCharges(applreceipt.getDeliveryCharges());
				 receiptPayment.setDenominationType(applreceipt.getDenominationType());
				 
				 
				 if(!StringUtils.isBlank(metaData.getReferrer())){
					 receiptPayment.setCreatedBy(metaData.getReferrer());
					}else{
						if(!StringUtils.isBlank(metaData.getAppType())){				
							receiptPayment.setCreatedBy(metaData.getAppType());
						}else{
							receiptPayment.setCreatedBy("WEB");
						 }
					}
				 
				 receiptPaymentList.add(receiptPayment);
			 } //End of foor loop
			 
		 }
		 return receiptPaymentList;
	 }
	
	 
	 /**  Save collection **/
	 public CollectionModel saveCollection(List<ReceiptPaymentApp> listOfRecAppl, PaymentResponseDto paymentResponse){
		 CollectionModel collection  =new CollectionModel();
		 BigDecimal totalcollectiontAmount = BigDecimal.ZERO;
		 BigDecimal deliveryCharges = BigDecimal.ZERO;
		 try{
			 if(!listOfRecAppl.isEmpty()){
				 ReceiptPaymentApp appl  = listOfRecAppl.get(0);
				 FxDeliveryDetailsModel delDetail = deliveryDetailRepos.findOne(appl.getDeliveryDetSeqId());
				 if(delDetail!=null){
					 deliveryCharges = delDetail.getDeliveryCharges();
				 }else{
					 deliveryCharges  =fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes).get(0)==null?BigDecimal.ZERO:fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes).get(0).getNumericField1();
					}
				 for(ReceiptPaymentApp appl1 : listOfRecAppl){
					 totalcollectiontAmount = totalcollectiontAmount.add(appl1.getLocalNetAmount());
				 }
				 if(deliveryCharges!=null && totalcollectiontAmount !=null){
					 totalcollectiontAmount = totalcollectiontAmount.add(deliveryCharges);
				 }
				 
				 collection.setReceiptType(ConstantDocument.COLLECTION_RECEIPT_TYPE);
			 	 collection.setApplicationCountryId(appl.getCountryId());
			 	 collection.setAccountMMYYYY(appl.getAccountMMYYYY());
			 	 collection.setCollectDate(new Date());
			 	 ViewCompanyDetails companyDetails = companyService.getCompanyDetailsById(appl.getCompanyId());
			 	 if(companyDetails!=null){
			 	  collection.setCompanyCode(companyDetails.getCompanyCode());
			 	 }else{
			 		logger.error("save saveCollection company code is blank:");
			 	 }
			 	 
			 	 CountryBranch countryBranch = countryBranchRepository.findByCountryBranchId(appl.getBranchId());
				 if(countryBranch!=null){
					 collection.setExBankBranch(countryBranch);
					 collection.setLocCode(countryBranch.getBranchId());
				 } 
			 	 
			 	collection.setCreatedDate(new Date());
			 	CurrencyMasterModel localCurrency = new CurrencyMasterModel();
			 	localCurrency.setCurrencyId(appl.getLocalCurrencyId());
			 	collection.setExCurrencyMaster(localCurrency);
			 	collection.setIsActive(ConstantDocument.Yes);
			    collection.setDocumentFinanceYear(appl.getDocumentFinanceYear());
			    collection.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);	
			    collection.setDocumentId(documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION).get(0).getDocumentID());
				
			 	 
				Customer customer = new Customer();
				customer.setCustomerId(appl.getCustomerId());
				collection.setFsCustomer(customer);
				
				collection.setNetAmount(totalcollectiontAmount);
				
				
			 	if(!StringUtils.isBlank(metaData.getReferrer())){
			 		collection.setCreatedBy(metaData.getReferrer());
					}else{
						if(!StringUtils.isBlank(metaData.getAppType())){				
							collection.setCreatedBy(metaData.getAppType());
						}else{
							collection.setCreatedBy("WEB");
						 }
					} 
				 
			 }else{
				 logger.error("save saveCollection listOfRecAppl is empty :");
			 }
			 
		 }catch(Exception e){
			 e.printStackTrace();
			 logger.error("save collection :"+e.getMessage());
		 }
		 
		 return collection; 
		 
	 }
	 /**  Save CollectDetailModel **/
	public  CollectDetailModel saveCollectDetail(List<ReceiptPaymentApp> listOfRecAppl, PaymentResponseDto paymentResponse){
		CollectDetailModel collectDetail = new CollectDetailModel();
		try{
			 
		 }catch(Exception e){
			 e.printStackTrace();
			 logger.error("save collection details :"+e.getMessage());
		 }
		return collectDetail;
	}
	 
}
