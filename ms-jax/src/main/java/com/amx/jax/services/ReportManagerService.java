package com.amx.jax.services;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.PurposeOfRemittanceReportBean;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.RemittanceReportBean;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.PromotionDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.LoyaltyInsuranceProDao;
import com.amx.jax.dbmodel.CollectionDetailViewModel;
import com.amx.jax.dbmodel.CollectionPaymentDetailsViewModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.PurposeOfRemittanceViewModel;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.PromotionManager;
import com.amx.jax.repository.ICollectionDetailViewDao;
import com.amx.jax.repository.ICollectionPaymentDetailsViewDao;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.IPurposeOfRemittance;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.RoundUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReportManagerService extends AbstractService{
	
	private Logger logger = Logger.getLogger(ReportManagerService.class);
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	LoyaltyInsuranceProDao loyaltyInsuranceProDao;
	
	@Autowired
	ICollectionDetailViewDao collectionDetailViewDao;
	
	@Autowired
	ICollectionPaymentDetailsViewDao collectionPaymentDetailsViewDao;
	
	@Autowired
	ICompanyDAO iCompanyDao;
	
	@Autowired
	IPurposeOfRemittance purposeOfRemittance;
	
	@Autowired
	IRemittanceTransactionDao remittanceTransactionDao;
	
	@Autowired
	ICurrencyDao currencyDao;
	
	
	private List<RemittanceReceiptSubreport> remittanceReceiptSubreportList;
	@Autowired
	PromotionManager promotionManager;
	@Autowired
	UserService userService;

	
	
	BigDecimal  companyId = null;
	BigDecimal currencyId  = null;
	String userName = null;
	BigDecimal applicationCountryId = null;
	BigDecimal  customerId = null; 
	BigDecimal employeeId = null;
	BigDecimal branchCode = null;
	BigDecimal branchId = null;
	BigDecimal customerRefernce =null;
	BigDecimal languageId=null;
	
	
	/**
	 *  For HTML Template 
	 */
	/**
	 * @param transactionHistroyDTO
	 * @return
	 */
	public ApiResponse generatePersonalRemittanceReceiptReportDetails(TransactionHistroyDTO transactionHistroyDTO, 
			Boolean promotion){
		
		ApiResponse response = null;
		try {
		remittanceReceiptSubreportList = new ArrayList<RemittanceReceiptSubreport>();
		 response = getBlackApiResponse();
		customerId = transactionHistroyDTO.getCustomerId();
		companyId = transactionHistroyDTO.getCompanyId();
		languageId = transactionHistroyDTO.getLanguageId();
		applicationCountryId = transactionHistroyDTO.getApplicationCountryId();
		BigDecimal collectionDocNo = transactionHistroyDTO.getCollectionDocumentNo();
		BigDecimal financeYear = transactionHistroyDTO.getCollectionDocumentFinYear();
		BigDecimal collectionDocumentCode = transactionHistroyDTO.getCollectionDocumentCode();
		customerRefernce =transactionHistroyDTO.getCustomerReference(); 
		
		 if (customerId == null) {
				throw new GlobalException(JaxError.NULL_CUSTOMER_ID.getStatusKey(), "Null customer id passed ");
			}
		
		 if (applicationCountryId == null) {
				throw new GlobalException(JaxError.NULL_APPLICATION_COUNTRY_ID.getStatusKey(), "Null applicationCountryId  passed ");
		  } 
		
		if(!currencyDao.getCurrencyListByCountryId(applicationCountryId).isEmpty()) {
			currencyId = currencyDao.getCurrencyListByCountryId(applicationCountryId).get(0).getCurrencyId();
		}else {
			throw new GlobalException(JaxError.NULL_CURRENCY_ID.getStatusKey(), "Null local currency id passed ");
			
		  }
		
		
		
		logger.info("Document Number=="+collectionDocNo+"\t docCode :"+collectionDocumentCode+"\t docYear :"+financeYear);
	
		Customer customer = userService.getCustById(customerId);
		
		 
		remittanceReceiptSubreportList = new ArrayList<RemittanceReceiptSubreport>();

		List<RemittanceTransactionView> remittanceApplicationList = new ArrayList<RemittanceTransactionView>();

		List<RemittanceReportBean> remittanceApplList = new ArrayList<RemittanceReportBean>();

		List<RemittanceReportBean> fcsaleAppList = new ArrayList<RemittanceReportBean>();

		
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = dateFormat.format(new Date());
		int noOfTransactions = 0;
	
		String currencyQuoteName = currencyDao.getCurrencyList(currencyId).get(0).getQuoteName();
		
			List<RemittanceTransactionView> remittanceViewlist = remittanceTransactionDao
					.getRemittanceTransactionForReport(collectionDocNo, financeYear, collectionDocumentCode,
							customer.getIdentityTypeId());
				
				
		logger.info("Remittance View List Size is======"+remittanceViewlist.size());
		if (!remittanceViewlist.isEmpty()) {

			for (RemittanceTransactionView remittanceAppview : remittanceViewlist) {
				if (remittanceAppview.getApplicationTypeDesc().equalsIgnoreCase("REMITTANCE")) {
					remittanceApplicationList.add(remittanceAppview);
					noOfTransactions= noOfTransactions+1;
				} 
			}

			//remittance List
			for (RemittanceTransactionView view : remittanceApplicationList) {

				RemittanceReportBean obj = new RemittanceReportBean();

				if (view.getCustomerReference() != null && view.getFirstName() != null && view.getMiddleName() != null && view.getLastName()!=null) {
					obj.setFirstName(view.getCustomerReference().toString() + " / " + view.getFirstName() + " " + view.getMiddleName()+" "+view.getLastName());
				} else if (view.getCustomerReference() == null && view.getFirstName() != null && view.getMiddleName() != null && view.getLastName()!=null) {
					obj.setFirstName(view.getFirstName() + " "+ view.getMiddleName()+" "+view.getLastName());
				} else if (view.getCustomerReference() == null && view.getFirstName() == null && view.getMiddleName() != null && view.getLastName()!=null) {
					obj.setFirstName(view.getMiddleName()+" "+view.getLastName());
				} else if (view.getCustomerReference() != null && view.getFirstName() == null && view.getMiddleName() != null && view.getLastName()!=null) {
					obj.setFirstName(view.getCustomerReference().toString() + " / " + view.getMiddleName()+" "+view.getLastName());
				} else if (view.getCustomerReference() == null && view.getFirstName() != null && view.getMiddleName() == null && view.getLastName()!=null) {
					obj.setFirstName(view.getFirstName()+" "+view.getLastName());
				} else if (view.getCustomerReference() != null && view.getFirstName() == null && view.getMiddleName() == null && view.getLastName()==null) {
					obj.setFirstName(view.getCustomerReference().toString());
				} else if (view.getCustomerReference() != null && view.getFirstName() != null && view.getMiddleName() == null && view.getLastName()!=null) {
					obj.setFirstName(view.getCustomerReference().toString() + " " + view.getFirstName()+" "+view.getLastName());
				}
				if (StringUtils.isNotBlank(view.getContactNumber())) {
					obj.setMobileNo(new BigDecimal(view.getContactNumber()));
				}
				obj.setCivilId(view.getIdentityInt());
				Date sysdate = view.getIdentityExpiryDate();
				if(sysdate != null){
					obj.setIdExpiryDate(new SimpleDateFormat("dd/MM/yyy").format(sysdate));
				}
				
				obj.setLocation(view.getCountryBranchName());

				if(view.getDocumentFinancialYear()!=null && view.getCollectionDocumentNo()!=null){
					obj.setReceiptNo(view.getDocumentFinancialYear()+" / "+view.getCollectionDocumentNo());
				}else if(view.getCollectionDocumentNo()!=null){
					obj.setReceiptNo(view.getCollectionDocumentNo().toString());
				}


				if(view.getDocumentFinancialYear()!=null && view.getDocumentNo()!=null){
					obj.setTransactionNo(view.getDocumentFinancialYear()+" / "+view.getDocumentNo());
				}else if(view.getDocumentNo()!=null){
					obj.setTransactionNo(view.getDocumentNo().toString());
				}

				Date docDate = view.getDocumentDate();
				if(docDate != null){
					obj.setDate(new SimpleDateFormat("dd/MM/yyy HH:mm").format(docDate));
				}
				
				obj.setBeneficiaryName(view.getBeneficiaryName());
				obj.setBenefeciaryBankName(view.getBeneficiaryBank());
				obj.setBenefeciaryBranchName(view.getBenefeciaryBranch());
				obj.setBenefeciaryAccountNumber(view.getBenefeciaryAccountNo());
				obj.setNoOfTransaction(new BigDecimal(noOfTransactions));
				obj.setPhoneNumber(view.getPhoneNumber()); 
				obj.setUserName(view.getCreatedBy());
				obj.setPinNo(view.getPinNo() );
				
				

	
				Map<String, Object> loyaltiPoints = loyaltyInsuranceProDao.loyaltyInsuranceProcedure(view.getCustomerReference(), obj.getDate());
				
				String prLtyStr1 =loyaltiPoints.get("P_LTY_STR1")==null?"":loyaltiPoints.get("P_LTY_STR1").toString();
				String prLtyStr2 =loyaltiPoints.get("P_LTY_STR2")==null?"":loyaltiPoints.get("P_LTY_STR2").toString();
				String prInsStr1 =loyaltiPoints.get("P_INS_STR1")==null?"":loyaltiPoints.get("P_INS_STR1").toString();
				String prInsStr2 =loyaltiPoints.get("P_INS_STR2")==null?"":loyaltiPoints.get("P_INS_STR2").toString();
				String prInsStrAr1 =loyaltiPoints.get("P_INS_STR_AR1")==null?"":loyaltiPoints.get("P_INS_STR_AR1").toString();
				String prInsStrAr2 =loyaltiPoints.get("P_INS_STR_AR2")==null?"":loyaltiPoints.get("P_INS_STR_AR2").toString();

				if(!prLtyStr1.trim().equals("") && !prLtyStr2.trim().equals("")){
					obj.setLoyalityPointExpiring(prLtyStr1+"  \n"+prLtyStr2);
				}else if(!prLtyStr1.trim().equals("")){
					obj.setLoyalityPointExpiring(prLtyStr1);
				}else if(!prLtyStr2.trim().equals("")){
					obj.setLoyalityPointExpiring(prLtyStr2);
				}

				/**
				 * @author Chetan Pawar
				 * comment if condition because of code duplication. 
				 * 11-05-2018
				 */
				/*if(!prInsStr1.trim().equals("") && !prInsStrAr1.trim().equals("")){
					obj.setInsurence1(prInsStr1+"  \n"+prInsStrAr1);
				}else*/ if(!prInsStr1.trim().equals("")){
					obj.setInsurence1(prInsStr1);
				}else if(!prInsStrAr1.trim().equals("")){
					obj.setInsurence1(prInsStrAr1);
				}


				if(!prInsStr2.trim().equals("") && !prInsStrAr2.trim().equals("")){
					obj.setInsurence2(prInsStr2+"  \n"+prInsStrAr2);
				}else if(!prInsStr2.trim().equals("")){
					obj.setInsurence2(prInsStr2);
				}else if(!prInsStrAr2.trim().equals("")){
					obj.setInsurence2(prInsStrAr2);
				}





				if (view.getBeneCityName() != null && view.getBeneDistrictName() != null && view.getBeneStateName() != null) {
					obj.setAddress(view.getBeneCityName() + ", " + view.getBeneDistrictName() + ", " + view.getBeneStateName());
				} else if (view.getBeneCityName() == null && view.getBeneDistrictName() != null && view.getBeneStateName() != null) {
					obj.setAddress(view.getBeneDistrictName() + ", " + view.getBeneStateName());
				} else if (view.getBeneCityName() == null && view.getBeneDistrictName() == null && view.getBeneStateName() != null) {
					obj.setAddress(view.getBeneStateName());
				} else if (view.getBeneCityName() != null && view.getBeneDistrictName() == null && view.getBeneStateName() != null) {
					obj.setAddress(view.getBeneCityName() + ", " + view.getBeneStateName());
				} else if (view.getBeneCityName() != null && view.getBeneDistrictName() == null && view.getBeneStateName() == null) {
					obj.setAddress(view.getBeneCityName());
				} else if (view.getBeneCityName() == null && view.getBeneDistrictName() != null && view.getBeneStateName() == null) {
					obj.setAddress(view.getBeneDistrictName());
				} else if (view.getBeneCityName() != null && view.getBeneDistrictName() != null && view.getBeneStateName() == null) {
					obj.setAddress(view.getBeneCityName() + ", " + view.getBeneDistrictName());
				}



				if (view.getRemittanceDescription() != null && view.getDeliveryDescription() != null) {
					obj.setPaymentChannel(view.getRemittanceDescription()+ " - " + view.getDeliveryDescription());
				} else if (view.getRemittanceDescription() == null && view.getDeliveryDescription() != null) {
					obj.setPaymentChannel(view.getDeliveryDescription());
				} else if (view.getRemittanceDescription() != null && view.getDeliveryDescription() == null) {
					obj.setPaymentChannel(view.getRemittanceDescription());
				}

				String currencyAndAmount=null;
				BigDecimal foreignTransationAmount=RoundUtil.roundBigDecimal((view.getForeignTransactionAmount()),currencyDao.getCurrencyList(view.getForeignCurrencyId()).get(0).getDecinalNumber().intValue());
				if(view.getCurrencyQuoteName()!=null && foreignTransationAmount!=null){
														
					currencyAndAmount = view.getCurrencyQuoteName()+"     "+foreignTransationAmount; 
				}
				obj.setCurrencyAndAmount(currencyAndAmount);
				List<PurposeOfRemittanceViewModel>  purposeOfRemittanceList =   purposeOfRemittance.getPurposeOfRemittance(view.getDocumentNo(),view.getDocumentFinancialYear());
					
				List<PurposeOfRemittanceReportBean> purposeOfRemitTrList1=new ArrayList<PurposeOfRemittanceReportBean>( );
				for(PurposeOfRemittanceViewModel purposeObj :purposeOfRemittanceList){
					PurposeOfRemittanceReportBean beanObj=new PurposeOfRemittanceReportBean();
					beanObj.setPurposeOfTrField(purposeObj.getFlexfieldName());
					beanObj.setPurposeOfTrfieldArabic(null);
					beanObj.setPurposeOfTrValue(purposeObj.getFlexiFieldValue() );
					purposeOfRemitTrList1.add(beanObj);
				}

				if(!purposeOfRemitTrList1.isEmpty()){
					obj.setPurposeOfRemitTrList(purposeOfRemitTrList1);
				}

				if(view.getCurrencyQuoteName()!=null && currencyQuoteName!=null && view.getExchangeRateApplied()!=null){
					obj.setExchangeRate(view.getCurrencyQuoteName()+" / "+currencyQuoteName+"     "+view.getExchangeRateApplied().toString());
				}
				int decimalPerCurrency =0;
				if(view.getLocalTransactionCurrencyId()!=null){
					
				 decimalPerCurrency = currencyDao.getCurrencyList(view.getLocalTransactionCurrencyId()).get(0).getDecinalNumber().intValue();
				}
				if(view.getLocalTransactionAmount()!=null && view.getLocalTransactionCurrencyId()!=null){
					BigDecimal transationAmount=RoundUtil.roundBigDecimal((view.getLocalTransactionAmount()),decimalPerCurrency);
					obj.setLocalTransactionAmount(currencyQuoteName+"     "+transationAmount.toString()); 
				}

				if(view.getLocalCommissionAmount()!=null && view.getLocalTransactionCurrencyId()!=null){
					BigDecimal localCommitionAmount=RoundUtil.roundBigDecimal((view.getLocalCommissionAmount()),decimalPerCurrency);
					obj.setCommision(currencyQuoteName+"     "+localCommitionAmount.toString()); 
				}

				if(view.getLocalChargeAmount()!=null && view.getLocalTransactionCurrencyId()!=null){
					BigDecimal localChargeAmount=RoundUtil.roundBigDecimal((view.getLocalChargeAmount()),decimalPerCurrency);
					obj.setOtherCharges(currencyQuoteName+"     "+localChargeAmount.toString()); 
				}

				if(view.getLocalNetTransactionAmount()!=null && view.getLocalTransactionCurrencyId()!=null){
					BigDecimal netAmount=RoundUtil.roundBigDecimal((view.getLocalNetTransactionAmount()),decimalPerCurrency);
					obj.setTotalAmount(currencyQuoteName+"     "+netAmount.toString()); 
				}

				getSpecialRateData(view, obj, currencyQuoteName, decimalPerCurrency);
				
				obj.setFutherInstructions(view.getInstructions());
				obj.setSourceOfIncome(view.getSourceOfIncomeDesc());
				obj.setIntermediataryBank(view.getBenefeciaryInterBank1());


				List<CollectionDetailViewModel> collectionDetailList1= collectionDetailViewDao.getCollectionDetailView(view.getCompanyId(),view.getCollectionDocumentNo(),view.getCollectionDocFinanceYear(),view.getCollectionDocCode());
						
					
				CollectionDetailViewModel collectionDetailView = collectionDetailList1.get(0);

				if(collectionDetailView.getNetAmount()!=null && view.getLocalTransactionCurrencyId()!=null){
					BigDecimal collectNetAmount=RoundUtil.roundBigDecimal((collectionDetailView.getNetAmount()),decimalPerCurrency);
					obj.setNetAmount(currencyQuoteName+"     "+collectNetAmount);
				}

				if(collectionDetailView.getPaidAmount()!=null && view.getLocalTransactionCurrencyId()!=null){
					BigDecimal collectPaidAmount=RoundUtil.roundBigDecimal((collectionDetailView.getPaidAmount()),decimalPerCurrency);
					obj.setPaidAmount(currencyQuoteName+"     "+collectPaidAmount); 
				}

				if(collectionDetailView.getRefundedAmount()!=null && view.getLocalTransactionCurrencyId()!=null){
					BigDecimal collectRefundAmount=RoundUtil.roundBigDecimal((collectionDetailView.getRefundedAmount()),decimalPerCurrency);
					obj.setRefundedAmount(currencyQuoteName+"     "+collectRefundAmount); 
				}

				
				obj.setCollectionDetailList(calculateCollectionMode(view));
				
				
				//addedd new column
				BigDecimal lessLoyaltyEncash = BigDecimal.ZERO;
				BigDecimal amountPayable = BigDecimal.ZERO;
				List<CollectionPaymentDetailsViewModel> collectionPmtDetailList= collectionPaymentDetailsViewDao.getCollectedPaymentDetails(view.getCompanyId(),view.getCollectionDocumentNo(),view.getCollectionDocFinanceYear(),view.getCollectionDocCode());
						
					
				for(CollectionPaymentDetailsViewModel collPaymentDetailsView: collectionPmtDetailList){
					if(collPaymentDetailsView.getCollectionMode().equalsIgnoreCase(ConstantDocument.VOCHERCODE)){
						lessLoyaltyEncash = collPaymentDetailsView.getCollectAmount();
						amountPayable=amountPayable.add(collPaymentDetailsView.getCollectAmount());
					}else{
						amountPayable=amountPayable.add(collPaymentDetailsView.getCollectAmount());
					}
				}
				if(lessLoyaltyEncash.compareTo(BigDecimal.ZERO)==0){
					obj.setLessLoyaltyEncasement(null);					
				}else{
					BigDecimal loyaltyAmount=RoundUtil.roundBigDecimal((lessLoyaltyEncash),currencyDao.getCurrencyList(view.getLocalTransactionCurrencyId()).get(0).getDecinalNumber().intValue());
					obj.setLessLoyaltyEncasement(currencyQuoteName+"     "+loyaltyAmount);
				}

				if(amountPayable!=null && currencyQuoteName!=null && view.getLocalTransactionCurrencyId()!=null){
					BigDecimal payable=RoundUtil.roundBigDecimal((amountPayable),currencyDao.getCurrencyList(view.getLocalTransactionCurrencyId()).get(0).getDecinalNumber().intValue());
					obj.setAmountPayable(currencyQuoteName+"     "+payable); 
				}
  
				// Added by Rabil
				try {
					
				if(view.getCustomerSignatureClob()!=null) {
					obj.setSignature(view.getCustomerSignatureClob());
				}
				
					List<ViewCompanyDetails> companyMaster = iCompanyDao.getCompanyDetailsByCompanyId(languageId, companyId);
			
					StringBuffer engCompanyInfo = null;
					StringBuffer arabicCompanyInfo = null;
					if (!companyMaster .isEmpty()) {
						engCompanyInfo = new StringBuffer();
						if (companyMaster.get(0).getEngAddress1()!= null && companyMaster.get(0).getEngAddress1().length() > 0) {
							engCompanyInfo = engCompanyInfo.append(companyMaster.get(0).getEngAddress1() + ",");
						}
						if (companyMaster.get(0).getEngAddress2() != null && companyMaster.get(0).getEngAddress2().length() > 0) {
							engCompanyInfo = engCompanyInfo.append(companyMaster.get(0).getEngAddress2() + ",");
						}
						if (companyMaster.get(0).getEngAddress3() != null && companyMaster.get(0).getEngAddress3().length() > 0) {
							engCompanyInfo = engCompanyInfo.append(companyMaster.get(0).getEngAddress3() + ",");
						}
						if (companyMaster.get(0).getRegistrationNumber() != null && companyMaster.get(0).getRegistrationNumber().length() > 0) {
							engCompanyInfo = engCompanyInfo.append("C.R. " + companyMaster.get(0).getRegistrationNumber() + ",");
						}
						if (companyMaster.get(0).getCapitalAmount() != null && companyMaster.get(0).getCapitalAmount().length() > 0) {
							engCompanyInfo = engCompanyInfo.append("Share Capital-" + companyMaster.get(0).getCapitalAmount());
						}
						obj.setEngCompanyInfo(engCompanyInfo.toString());

						arabicCompanyInfo = new StringBuffer();

						if (companyMaster.get(0).getArabicAddress1() != null && companyMaster.get(0).getArabicAddress1().length() > 0) {
							arabicCompanyInfo = arabicCompanyInfo.append(companyMaster.get(0).getArabicAddress1());
						}
						if (companyMaster.get(0).getArabicAddress2() != null && companyMaster.get(0).getArabicAddress2().length() > 0) {
							arabicCompanyInfo = arabicCompanyInfo.append(companyMaster.get(0).getArabicAddress2() + ",");
						}
						if (companyMaster.get(0).getArabicAddress3() != null && companyMaster.get(0).getArabicAddress3().length() > 0) {
							arabicCompanyInfo = arabicCompanyInfo.append(companyMaster.get(0).getArabicAddress3() + ",");
						}
						if (companyMaster.get(0).getRegistrationNumber() != null && companyMaster.get(0).getRegistrationNumber().length() > 0) {
							arabicCompanyInfo = arabicCompanyInfo.append(ConstantDocument.CR + " " + companyMaster.get(0).getRegistrationNumber() + ",");
						}
						if (companyMaster.get(0).getCapitalAmount() != null && companyMaster.get(0).getCapitalAmount().length() > 0) {
							arabicCompanyInfo = arabicCompanyInfo.append(ConstantDocument.Share_Capital + " " + companyMaster.get(0).getCapitalAmount());
						}
						obj.setArabicCompanyInfo(arabicCompanyInfo.toString());
						
						obj.setVatNumber(companyMaster.get(0).getVatNumber()==null?"":companyMaster.get(0).getVatNumber());
						obj.setVatDate(companyMaster.get(0).getVatRegistrationDate()==null?"":companyMaster.get(0).getVatRegistrationDate());
					}
					// 
					
					
					
				} catch (Exception e) {
					logger.info( "Exception Occured While Report2 "+e.getMessage());
					throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
				
				}

				remittanceApplList.add(obj);
			}

			 

			//for Main Remittance Receipt report (Remittance Receipt and Fc Sale Application)
			RemittanceReceiptSubreport remittanceObj = new RemittanceReceiptSubreport();
		
			remittanceObj.setWaterMarkCheck(true);
			remittanceObj.setFcsaleAppList(fcsaleAppList);
			remittanceObj.setRemittanceApplList(remittanceApplList);
			

			if (!fcsaleAppList.isEmpty()) {
				remittanceObj.setFcsaleApplicationCheck(true);
			} 
			if(!remittanceApplList.isEmpty()){
				remittanceObj.setRemittanceReceiptCheck(true);
			}

			remittanceReceiptSubreportList.add(remittanceObj);
			
			
		
		}
		if (Boolean.TRUE.equals(promotion)) {
			PromotionDto promotionDto = promotionManager.getPromotionDto(transactionHistroyDTO.getDocumentNumber(),
					transactionHistroyDTO.getDocumentFinanceYear());
				if (promotionDto != null && !promotionDto.isChichenVoucher()) {
					remittanceReceiptSubreportList.get(0).getRemittanceApplList().get(0).setPromotionDto(promotionDto);
				}
		}
		response.getData().getValues().addAll(remittanceReceiptSubreportList);
		response.setResponseStatus(ResponseStatus.OK);
	    response.getData().setType("remitReport");
	 
		}catch(Exception e) {
			e.printStackTrace();
			response.getData().getValues().addAll(remittanceReceiptSubreportList);
			response.setResponseStatus(ResponseStatus.NOT_FOUND);
		    response.getData().setType("remitReport");
		  
		}
		
		   return response;
	
	}
		
	// ----------------- SPECIAL RATE RECEIPT DATA -------------------
	private void getSpecialRateData(RemittanceTransactionView view, RemittanceReportBean obj, String currencyQuoteName,
			int decimalPerCurrency) {
		// Special Exchange Rate
		if (view.getCurrencyQuoteName() != null && currencyQuoteName != null && view.getExchangeRateApplied() != null) {
			obj.setSpecialExchangeRate(view.getCurrencyQuoteName() + " / " + currencyQuoteName + "     "
					+ view.getExchangeRateApplied().toString());
		}

		// Equivalent kwd Amount
		if (view.getLocalTransactionAmount() != null && view.getLocalTransactionCurrencyId() != null) {
			BigDecimal transationAmount = RoundUtil.roundBigDecimal((view.getLocalTransactionAmount()),
					decimalPerCurrency);
			obj.setSpecialKwdAmount(currencyQuoteName + "     " + transationAmount.toString());
		}

		// Branch Exchange Rate and kwd Amount
		if (null != view.getIsDiscAvail() && view.getIsDiscAvail().equals("Y")) {
			if (view.getCurrencyQuoteName() != null && currencyQuoteName != null
					&& view.getOriginalExchangeRate() != null) {
				if (view.getOriginalExchangeRate().compareTo(view.getExchangeRateApplied()) != 1) {
					obj.setBranchExchangeRate(view.getCurrencyQuoteName() + " / " + currencyQuoteName + "     "
							+ view.getExchangeRateApplied().toString());
					if (view.getLocalTransactionAmount() != null && view.getLocalTransactionCurrencyId() != null) {
						BigDecimal transationAmount = RoundUtil.roundBigDecimal((view.getLocalTransactionAmount()),
								decimalPerCurrency);
						obj.setKwdAmount(currencyQuoteName + "     " + transationAmount.toString());
					}
				} else {
					obj.setBranchExchangeRate(view.getCurrencyQuoteName() + " / " + currencyQuoteName + "     "
							+ view.getOriginalExchangeRate().toString());
					if (view.getOriginalExchangeRate() != null && view.getForeignTransactionAmount() != null
							&& view.getLocalTransactionCurrencyId() != null) {
						BigDecimal calKwtAmt = view.getOriginalExchangeRate().multiply(view.getForeignTransactionAmount());
						BigDecimal transationAmount = RoundUtil.roundBigDecimal((calKwtAmt), decimalPerCurrency);
						obj.setKwdAmount(currencyQuoteName + "     " + transationAmount.toString());
					}
				}

			}
			
		} else {
			if (view.getCurrencyQuoteName() != null && currencyQuoteName != null
					&& view.getExchangeRateApplied() != null) {
				obj.setBranchExchangeRate(view.getCurrencyQuoteName() + " / " + currencyQuoteName + "     "
						+ view.getExchangeRateApplied().toString());
			}
			if (view.getLocalTransactionAmount() != null && view.getLocalTransactionCurrencyId() != null) {
				BigDecimal transationAmount = RoundUtil.roundBigDecimal((view.getLocalTransactionAmount()),
						decimalPerCurrency);
				obj.setKwdAmount(currencyQuoteName + "     " + transationAmount.toString());
			}
		}
	}

public List<RemittanceReportBean> calculateCollectionMode(RemittanceTransactionView viewCollectionObj){	
			List<RemittanceReportBean> collectionDetailList = new ArrayList<RemittanceReportBean>();
			List<CollectionPaymentDetailsViewModel> collectionPaymentDetailList= collectionPaymentDetailsViewDao.getCollectedPaymentDetails(viewCollectionObj.getCompanyId(),viewCollectionObj.getCollectionDocumentNo(),viewCollectionObj.getCollectionDocFinanceYear(),viewCollectionObj.getCollectionDocCode());
		
			int size = collectionPaymentDetailList.size();
			for(CollectionPaymentDetailsViewModel viewObj: collectionPaymentDetailList){
				RemittanceReportBean obj = new RemittanceReportBean();
				if(viewObj.getCollectionMode()!=null && viewObj.getCollectionMode().equalsIgnoreCase("K")){
					obj.setCollectionMode(viewObj.getCollectionModeDesc());
					obj.setApprovalNo(viewObj.getApprovalNo());
					obj.setTransactionId(viewObj.getTransactionId());
					obj.setKnetreceiptDateTime(viewObj.getKnetReceiptDatenTime());
					obj.setKnetBooleanCheck(true);
					if(viewObj.getCollectAmount()!=null && viewCollectionObj.getLocalTransactionCurrencyId()!=null){
						BigDecimal collectAmount=RoundUtil.roundBigDecimal((viewObj.getCollectAmount()),currencyDao.getCurrencyList(viewCollectionObj.getLocalTransactionCurrencyId()).get(0).getDecinalNumber().intValue());
						obj.setCollectAmount(collectAmount);
					}
				}else{
					obj.setCollectionMode(viewObj.getCollectionModeDesc());
					obj.setKnetBooleanCheck(false);
					if(viewObj.getCollectAmount()!=null && viewCollectionObj.getLocalTransactionCurrencyId()!=null){
						BigDecimal collectAmount=RoundUtil.roundBigDecimal((viewObj.getCollectAmount()),currencyDao.getCurrencyList(viewCollectionObj.getLocalTransactionCurrencyId()).get(0).getDecinalNumber().intValue());
						obj.setCollectAmount(collectAmount);
					}
				}
				if(size>1){
					obj.setDrawLine(true);
				}else{
					obj.setDrawLine(false);
				}
				collectionDetailList.add(obj);
				size = size-1;
			}
			return collectionDetailList;
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

		public List<RemittanceReceiptSubreport> getRemittanceReceiptSubreportList() {
			return remittanceReceiptSubreportList;
		}

		public void setRemittanceReceiptSubreportList(List<RemittanceReceiptSubreport> remittanceReceiptSubreportList) {
			this.remittanceReceiptSubreportList = remittanceReceiptSubreportList;
		}

		


	
}
