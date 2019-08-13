package com.amx.jax.manager;
/**
 * @author rabil
 *
 */
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.ResponseCodeDetailDTO;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.dal.LoyaltyInsuranceProDao;
import com.amx.jax.dbmodel.CollectionDetailViewModel;
import com.amx.jax.dbmodel.CollectionPaymentDetailsViewModel;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;
import com.amx.jax.dict.PayGRespCodeJSONConverter;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.fx.FxDeliveryReportDetailDto;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.model.response.fx.FxOrderTransactionStatusResponseDto;
import com.amx.jax.model.response.fx.PaygDetailsDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICollectionDetailViewDao;
import com.amx.jax.repository.ICollectionPaymentDetailsViewDao;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IShippingAddressRepository;
import com.amx.jax.repository.IViewArea;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.repository.PaygDetailsRepository;
import com.amx.jax.repository.PaymentModeRepository;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.repository.fx.FxDeliveryDetailsRepository;
import com.amx.jax.repository.fx.FxOrderTransactionRespository;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;



@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FxOrderReportManager {

	private Logger logger = Logger.getLogger(FxOrderReportManager.class);

	@Autowired
	MetaData metaData;

	@Autowired
	FxOrderTransactionRespository fxTransactionHistroyDao;

	@Autowired
	FcSaleApplicationTransactionManager applTrnxManager;

	@Autowired
	FxDeliveryDetailsRepository deliveryDetailsRepos;

	@Autowired
	PaygDetailsRepository payGDeatilsRepos;

	@Autowired
	IShippingAddressRepository shippingAddressDao;


	@Autowired
	ICustomerRepository customerDao;


	@Autowired
	IContactDetailDao contactDao;

	@Autowired
	CountryRepository countryDao;

	@Autowired
	private MetaData meta;

	@Autowired
	IViewCityDao cityDao;

	@Autowired
	IViewStateDao stateDao;

	@Autowired
	IViewDistrictDAO districtDao;

	@Autowired
	IViewArea areaDao;

	@Autowired
	ReceiptPaymentAppRepository rcptPaymentAppl;

	@Autowired
	LoyaltyInsuranceProDao loyaltyInsuranceProDao;

	@Autowired
	PaymentModeRepository payModeRepositoy;

	@Autowired
	ICollectionDetailRepository collDetailRepos;

	@Autowired
	ICompanyDAO iCompanyDao;


	@Autowired
	ICollectionDetailViewDao collectionDetailViewDao;

	@Autowired
	ICollectionPaymentDetailsViewDao collectionPaymentDetailsViewDao;

	@Autowired
	ICurrencyDao currencyDao;
	
	@Autowired
	FcSaleAddressManager addressManager;


	/**
	 * 
	 * @param collNo
	 * @param collFyr
	 * @return
	 */
	public FxOrderReportResponseDto getReportDetails(BigDecimal customerId ,BigDecimal collNo,BigDecimal collFyr){
		FxOrderReportResponseDto reportModel = new FxOrderReportResponseDto();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal languageId = metaData.getLanguageId()==null?new BigDecimal(1):metaData.getLanguageId();
		BigDecimal customerReferenceId = BigDecimal.ZERO;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		BigDecimal collectionDocNo =BigDecimal.ZERO;
		BigDecimal collectionDocfyear =BigDecimal.ZERO;
		BigDecimal collectionDocCode =BigDecimal.ZERO;
		BigDecimal localCurrency =BigDecimal.ZERO;
		String localCurrQuoteName ="";
		String createdDate=""; 
		String receiptNo = "";
		String customerName="";
		String phoneNo ="";
		BigDecimal loyaltyPoints = BigDecimal.ZERO;
		String email = "";
		FxDeliveryDetailsModel fxDelDetailModel = null;
		PaygDetailsModel pgDetailsModel = null;

		List<FxOrderTransactionModel> fxOrderTrnxList =  fxTransactionHistroyDao.getFxOrderTrnxListByCollectionDocNumber(customerId,collNo,collFyr);

		if(fxOrderTrnxList != null && !fxOrderTrnxList.isEmpty()){

			if(JaxUtil.isNullZeroBigDecimalCheck(customerId) && customerId.compareTo(fxOrderTrnxList.get(0).getCustomerId())!=0){
				logger.error("custoemr not found meta data:"+customerId+"\t details :"+fxOrderTrnxList.get(0).getCustomerId());
				throw new GlobalException(JaxError.INVALID_CUSTOMER, "customer not found");
			}

			List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
			if(customerList != null && !customerList.isEmpty()){
				reportModel.setIdExpiryDate(DateUtil.todaysDateWithDDMMYY(customerList.get(0).getIdentityExpiredDate(),"0"));
				reportModel.setCivilId(customerList.get(0).getIdentityInt());
				customerReferenceId = customerList.get(0).getCustomerReference();
				phoneNo =  customerList.get(0).getMobile();
				loyaltyPoints = customerList.get(0).getLoyaltyPoints()==null?BigDecimal.ZERO:customerList.get(0).getLoyaltyPoints();
				email = customerList.get(0).getEmail();
				reportModel.setLoyaltyPoints(loyaltyPoints);
				reportModel.setEmail(email);
				reportModel.setCustomerReferenceId(customerReferenceId);
			}else{
				logger.error("customer not found :"+customerId);
				throw new GlobalException(JaxError.INVALID_CUSTOMER, "customer not found");
			}

			List<FxOrderTransactionHistroyDto> fxOrderTrnxListDto = applTrnxManager.convertFxHistDto(fxOrderTrnxList);
			List<FxOrderTransactionHistroyDto> finalList = new ArrayList<>();
			if(fxOrderTrnxListDto !=null && !fxOrderTrnxListDto.isEmpty()){
				finalList = applTrnxManager.getMultipleTransactionHistroy(fxOrderTrnxListDto);
			}else{
				logger.error("fxOrderTrnxListDto trnx list not found :"+customerId+"\t Colle : "+collNo+"\t Coll fyr :"+collFyr);
				throw new GlobalException(JaxError.NO_RECORD_FOUND, "customer not found");
			}

			reportModel.setFxOrderTrnxList(finalList);
			reportModel.setNoOfTransaction(new BigDecimal(fxOrderTrnxList.size()));
			BigDecimal deliveryDetSeqId =fxOrderTrnxList.get(0).getDeliveryDetSeqId() ;
			BigDecimal paygSeqId        = fxOrderTrnxList.get(0).getPagDetSeqId();
			if(fxOrderTrnxList.get(0).getCollectionDocumentNo()!=null && fxOrderTrnxList.get(0).getCollectionDocumentFinYear()!=null){
				collectionDocfyear = fxOrderTrnxList.get(0).getCollectionDocumentFinYear();
				collectionDocNo = fxOrderTrnxList.get(0).getCollectionDocumentNo();
				collectionDocCode = fxOrderTrnxList.get(0).getCollectionDocumentCode();
				localCurrency = fxOrderTrnxList.get(0).getLocalCurrencyId();
				localCurrQuoteName = fxOrderTrnxList.get(0).getLocalCurrQuoteName(); 
				receiptNo =  fxOrderTrnxList.get(0).getCollectionDocumentFinYear().toString()+"/"+fxOrderTrnxList.get(0).getCollectionDocumentNo().toString();
				reportModel.setDeliveryDate(fxOrderTrnxList.get(0).getDeliveryDate()==null?"":fxOrderTrnxList.get(0).getDeliveryDate());
				reportModel.setDeliveryTime(fxOrderTrnxList.get(0).getDeliveryTime()==null?"":fxOrderTrnxList.get(0).getDeliveryTime());
			}

			int decimalPerCurrency =0;
			if(localCurrency!=null){
				decimalPerCurrency = currencyDao.getCurrencyList(localCurrency).get(0).getDecinalNumber().intValue();
			}
			reportModel.setDeliveryCharges(RoundUtil.roundBigDecimal(fxOrderTrnxList.get(0).getDeliveryCharges(),decimalPerCurrency));
			customerName=fxOrderTrnxList.get(0).getCustomerName();
			if(deliveryDetSeqId != null) {
				fxDelDetailModel = deliveryDetailsRepos.findOne(deliveryDetSeqId);
			}
			if(paygSeqId != null) {
				pgDetailsModel = payGDeatilsRepos.findOne(paygSeqId);
			}
			createdDate = fxOrderTrnxList.get(0).getCreatedDate();


	    	if(fxDelDetailModel!=null){
	    		FxDeliveryReportDetailDto delDto = convertFxDeliveryDto(fxDelDetailModel);
	    		reportModel.setDeliveryDetailReport(delDto);
	    		ShippingAddressDto shippingAddressDto = addressManager.fetchShippingAddress(customerId, fxDelDetailModel.getShippingAddressId());
	    		reportModel.setShippingAddressdto(shippingAddressDto);
	    	}
	    	

			List<ViewCompanyDetails> companyMaster = iCompanyDao.getCompanyDetailsByCompanyId(languageId, companyId);

			StringBuffer engCompanyInfo = null;
			StringBuffer arabicCompanyInfo = null;
			if (companyMaster !=null && !companyMaster .isEmpty()) {
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
				reportModel.setEngCompanyInfo(engCompanyInfo.toString());

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
				reportModel.setArabicCompanyInfo(arabicCompanyInfo.toString());
			}else{
				logger.error("companyMaster not found :");
				throw new GlobalException(JaxError.INVALID_COMPANY_ID, "customer not found");
			}

			List<CollectionDetailViewModel> collectionDetailList1= collectionDetailViewDao.getCollectionDetailView(companyId,collectionDocNo,collectionDocfyear,collectionDocCode);

			if(collectionDetailList1!= null & !collectionDetailList1.isEmpty()){
				CollectionDetailViewModel collectionDetailView = collectionDetailList1.get(0);

				if(collectionDetailView.getNetAmount()!=null && localCurrency!=null){
					BigDecimal collectNetAmount=RoundUtil.roundBigDecimal((collectionDetailView.getNetAmount()),decimalPerCurrency);
					reportModel.setNetAmount(localCurrQuoteName+"     ******"+collectNetAmount);
				}

				if(collectionDetailView.getPaidAmount()!=null && localCurrency!=null){
					BigDecimal collectPaidAmount=RoundUtil.roundBigDecimal((collectionDetailView.getPaidAmount()),decimalPerCurrency);
					reportModel.setPaidAmount(localCurrQuoteName+"     ******"+collectPaidAmount);
				}

				if(collectionDetailView.getRefundedAmount()!=null && localCurrency!=null){
					BigDecimal collectRefundAmount=RoundUtil.roundBigDecimal((collectionDetailView.getRefundedAmount()),decimalPerCurrency);
					reportModel.setRefundedAmount(localCurrQuoteName+"     ******"+collectRefundAmount);
				}
			}else{
				logger.error("collectionDetailList1 not found :");
				throw new GlobalException(JaxError.PAYMENT_DETAILS_NOT_FOUND, "Payment details not found");
			}

			//addedd new column
			BigDecimal lessLoyaltyEncash = BigDecimal.ZERO;
			BigDecimal amountPayable = BigDecimal.ZERO;
			List<CollectionPaymentDetailsViewModel> collectionPmtDetailList= collectionPaymentDetailsViewDao.getCollectedPaymentDetails(companyId,collectionDocNo,collectionDocfyear,collectionDocCode);


			for(CollectionPaymentDetailsViewModel collPaymentDetailsView: collectionPmtDetailList){
				if(collPaymentDetailsView.getCollectionMode() !=null && collPaymentDetailsView.getCollectionMode().equalsIgnoreCase(ConstantDocument.VOCHERCODE)){
					lessLoyaltyEncash = collPaymentDetailsView.getCollectAmount();
					amountPayable=amountPayable.add(collPaymentDetailsView.getCollectAmount());
				}else{
					amountPayable=amountPayable.add(collPaymentDetailsView.getCollectAmount());
				}
				reportModel.setPaymentMode(collPaymentDetailsView.getCollectionModeDesc());
				reportModel.setKnetReceiptDateTime(collPaymentDetailsView.getKnetReceiptDatenTime());
				reportModel.setCollectionMode(collPaymentDetailsView.getCollectionModeDesc());
				reportModel.setApprovalNo(collPaymentDetailsView.getApprovalNo());
			}


			if(pgDetailsModel!=null){
				PaygDetailsDto pgdto = convertFxPgDetailsDto(pgDetailsModel);
				pgdto.setPaymentMode(reportModel.getPaymentMode());
				pgdto.setKnetReceiptDateTime(reportModel.getKnetReceiptDateTime());
				reportModel.setPayg(pgdto);
			}

			if(amountPayable!=null && localCurrQuoteName!=null && localCurrency!=null){
				BigDecimal payable=RoundUtil.roundBigDecimal((amountPayable),currencyDao.getCurrencyList(localCurrency).get(0).getDecinalNumber().intValue());
				reportModel.setAmountPayable(localCurrQuoteName+"     ******"+payable);
			}

			reportModel.setReceiptNo(receiptNo);
			reportModel.setCustomerName(customerName);
			reportModel.setPhoneNumber(phoneNo);
			reportModel.setLocation(fxOrderTrnxList.get(0).getBranchDesc());
			reportModel.setLocalCurrency(localCurrQuoteName);
			
		}else{
			logger.error("trnx list not found :"+customerId+"\t Colle : "+collNo+"\t Coll fyr :"+collFyr);
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "customer not found");
		}

		Map<String, Object> loyaltiPoints = loyaltyInsuranceProDao.loyaltyInsuranceProcedure(customerReferenceId, createdDate);

		String prLtyStr1 =loyaltiPoints.get("P_LTY_STR1")==null?"":loyaltiPoints.get("P_LTY_STR1").toString();
		String prLtyStr2 =loyaltiPoints.get("P_LTY_STR2")==null?"":loyaltiPoints.get("P_LTY_STR2").toString();
		String prInsStr1 =loyaltiPoints.get("P_INS_STR1")==null?"":loyaltiPoints.get("P_INS_STR1").toString();
		String prInsStr2 =loyaltiPoints.get("P_INS_STR2")==null?"":loyaltiPoints.get("P_INS_STR2").toString();
		String prInsStrAr1 =loyaltiPoints.get("P_INS_STR_AR1")==null?"":loyaltiPoints.get("P_INS_STR_AR1").toString();
		String prInsStrAr2 =loyaltiPoints.get("P_INS_STR_AR2")==null?"":loyaltiPoints.get("P_INS_STR_AR2").toString();

		if(!prLtyStr1.trim().equals("") && !prLtyStr2.trim().equals("")){
			reportModel.setLoyalityPointExpiring(prLtyStr1+"  \n"+prLtyStr2);
		}else if(!prLtyStr1.trim().equals("")){
			reportModel.setLoyalityPointExpiring(prLtyStr1);
		}else if(!prLtyStr2.trim().equals("")){
			reportModel.setLoyalityPointExpiring(prLtyStr2);
		}

		if(!prInsStr1.trim().equals("")){
			reportModel.setInsurence1(prInsStr1);
		}else if(!prInsStrAr1.trim().equals("")){
			reportModel.setInsurence1(prInsStrAr1);
		}
		if(!prInsStr2.trim().equals("") && !prInsStrAr2.trim().equals("")){
			reportModel.setInsurence2(prInsStr2+"  \n"+prInsStrAr2);
		}else if(!prInsStr2.trim().equals("")){
			reportModel.setInsurence2(prInsStr2);
		}else if(!prInsStrAr2.trim().equals("")){
			reportModel.setInsurence2(prInsStrAr2);
		}

		return reportModel; 
	}

	/*
	 * To get Transaction status: 
	 */

	public FxOrderTransactionStatusResponseDto  getTransactionStatus(BigDecimal paymentSeqId){
		FxOrderTransactionStatusResponseDto responseModel = new FxOrderTransactionStatusResponseDto();
		FxOrderTransactionHistroyDto histroyDto = new FxOrderTransactionHistroyDto();
		FxDeliveryDetailsModel fxDelDetailModel =null;
		PaygDetailsModel pgDetailsModel =null;
		BigDecimal custoemrId = metaData.getCustomerId();
		BigDecimal netAmount =BigDecimal.ZERO;
		BigDecimal deliveryCharges =BigDecimal.ZERO;
		String receiptNo="";
		String trnxRefNo ="";
		List<ReceiptPaymentApp> applReceipt = rcptPaymentAppl.getApplicationByPagdetailSeqIAndcustomerId(custoemrId, paymentSeqId);
		if(JaxUtil.isNullZeroBigDecimalCheck(paymentSeqId)){
			pgDetailsModel = payGDeatilsRepos.findOne(paymentSeqId);
		}
		if(applReceipt != null && !applReceipt .isEmpty() && applReceipt.get(0).getDeliveryDetSeqId()!=null){
			fxDelDetailModel = deliveryDetailsRepos.findOne(applReceipt.get(0).getDeliveryDetSeqId());
		}else{
			logger.error(" getTransactionStatus custoemrId - paymentSeqId :"+custoemrId +"-"+paymentSeqId);
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"No record found :");
		}
		JaxTransactionStatus jaxTrnxStatus = getJaxTransactionStatus(pgDetailsModel,applReceipt);
		
		if(applReceipt != null && !applReceipt.isEmpty()){
			List<FxOrderTransactionModel> fxOrderTrnxList =  fxTransactionHistroyDao.getFxOrderTrnxListByCollectionDocNumber(custoemrId,applReceipt.get(0).getColDocNo(),applReceipt.get(0).getColDocFyr());
			if(fxOrderTrnxList!=null && !fxOrderTrnxList.isEmpty()){
				if(fxOrderTrnxList.get(0).getCollectionDocumentNo()!=null && fxOrderTrnxList.get(0).getCollectionDocumentFinYear()!=null){
					receiptNo =  fxOrderTrnxList.get(0).getCollectionDocumentFinYear().toString()+"/"+fxOrderTrnxList.get(0).getCollectionDocumentNo().toString();
					trnxRefNo =fxOrderTrnxList.get(0).getTransactionReferenceNo();
				}
				List<FxOrderTransactionHistroyDto> fxOrderTrnxListDto = applTrnxManager.convertFxHistDto(fxOrderTrnxList);
				responseModel.setFxOrderTrnxHistroyDTO(fxOrderTrnxListDto);
				responseModel.setReceiptNo(receiptNo);
			}
			if(!StringUtils.isBlank(trnxRefNo)){
			responseModel.setTransactionReference(trnxRefNo);
			}else{
				responseModel.setTransactionReference(applReceipt.get(0).getDocumentFinanceYear().toString()+"/"+applReceipt.get(0).getDocumentNo());
			}
			for(ReceiptPaymentApp appl : applReceipt){
				netAmount = netAmount.add(appl.getLocalTrnxAmount());
			}
		}

		if(fxDelDetailModel !=null){
			deliveryCharges = fxDelDetailModel.getDeliveryCharges();
		}	

		if(pgDetailsModel.getResultCode() != null) {
			String resultCategory = pgDetailsModel.getResultCode();
			logger.info("Result Category from DB : "+resultCategory);
			if(resultCategory.contains(" ")) {
				resultCategory = resultCategory.replace(" ", "_");
				logger.info("Result Category from SPACE : "+resultCategory);
			}
			if(resultCategory.contains("+")) {
				resultCategory = resultCategory.replace("\\+", "_");
				logger.info("Result Category from PLUS : "+resultCategory);
			}
			ResponseCodeDetailDTO responseCodeDetail = PayGRespCodeJSONConverter.getResponseCodeDetail(resultCategory);
			responseModel.setResponseCodeDetail(responseCodeDetail);
		}

		responseModel.setNetAmount(netAmount.add(deliveryCharges));
		responseModel.setStatus(jaxTrnxStatus);
		return responseModel;
	}


	FxDeliveryDetailDto  deliveryDetailList = new FxDeliveryDetailDto();
	ShippingAddressDto shippingAddressdto =new ShippingAddressDto();
	PaygDetailsDto payg = new PaygDetailsDto();

	public FxDeliveryReportDetailDto convertFxDeliveryDto(FxDeliveryDetailsModel delDetails){
		FxDeliveryReportDetailDto  dto = new FxDeliveryReportDetailDto();
		try {
			BeanUtils.copyProperties(dto, delDetails);
		} catch (IllegalAccessException | InvocationTargetException e) {

		}
		return dto;
	}

	public PaygDetailsDto convertFxPgDetailsDto(PaygDetailsModel pgdelDetails){
		PaygDetailsDto  dto = new PaygDetailsDto();
		try {
			BeanUtils.copyProperties(dto, pgdelDetails);
		} catch (IllegalAccessException | InvocationTargetException e) {

		}
		return dto;
	}

	public ShippingAddressDto getShippingaddressDetails(ShippingAddressDetail shippingAddressDetail){
		ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
		BigDecimal countryId = meta.getCountryId();
		BigDecimal customerId  = meta.getCustomerId();
		if(shippingAddressDetail!=null){
		 customerId = shippingAddressDetail.getFsCustomer().getCustomerId();
		}
		BigDecimal companyId = meta.getCompanyId();
		List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		if (shippingAddressDetail!=null) {
			shippingAddressDto.setAddressId(shippingAddressDetail.getShippingAddressDetailId());

			if(customerList !=null && !customerList.isEmpty()){
				shippingAddressDto.setFirstName(customerList.get(0).getFirstName());
				shippingAddressDto.setMiddleName(customerList.get(0).getMiddleName());
				shippingAddressDto.setLastName(customerList.get(0).getLastName());
				shippingAddressDto.setMobile(customerList.get(0).getMobile());
				shippingAddressDto.setEmail(customerList.get(0).getEmail());

			}else{
				throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND ,"customer not found :"+customerId);
			}
			shippingAddressDto.setCustomerId(shippingAddressDetail.getFsCustomer().getCustomerId());
			shippingAddressDto.setCompanyId(companyId);
			shippingAddressDto.setMobile(shippingAddressDetail.getMobile());
			shippingAddressDto.setLocalContactBuilding(shippingAddressDetail.getBuildingNo());
			shippingAddressDto.setStreet(shippingAddressDetail.getStreet());
			shippingAddressDto.setBlockNo(shippingAddressDetail.getBlock());
			shippingAddressDto.setHouse(shippingAddressDetail.getFlat());
			shippingAddressDto.setBuildingNo(shippingAddressDetail.getBuildingNo());
			shippingAddressDto.setAdressType(shippingAddressDetail.getAddressType());
			shippingAddressDto.setAreaDesc(areaDao.getAreaList(shippingAddressDetail.getAreaCode())==null?"":areaDao.getAreaList(shippingAddressDetail.getAreaCode()).getShortDesc());
			shippingAddressDto.setFlat(shippingAddressDetail.getFlat());
			List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),shippingAddressDetail.getFsCountryMaster().getCountryId());
			if (countryMasterView !=null && !countryMasterView.isEmpty()) {
				shippingAddressDto.setLocalContactCountry(countryMasterView.get(0).getCountryName());
				if (shippingAddressDetail.getFsStateMaster() != null) {
					List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),shippingAddressDetail.getFsStateMaster().getStateId(), new BigDecimal(1));
					if (stateMasterView !=null && !stateMasterView.isEmpty()) {
						shippingAddressDto.setLocalContactState(stateMasterView.get(0).getStateName());
						DistrictMaster distictMaster = shippingAddressDetail.getFsDistrictMaster();
						if (distictMaster != null) {
							List<ViewDistrict> districtMas = districtDao.getDistrict(stateMasterView.get(0).getStateId(), distictMaster.getDistrictId(),new BigDecimal(1));
							if (districtMas !=null && !districtMas.isEmpty()) {
								shippingAddressDto.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
								List<ViewCity> cityDetails = cityDao.getCityDescription(districtMas.get(0).getDistrictId(),shippingAddressDetail.getFsCityMaster().getCityId(), new BigDecimal(1));
								if (!cityDetails.isEmpty()) {
									shippingAddressDto.setLocalContactCity(cityDetails.get(0).getCityName());
								}
							}else{
								//throw new GlobalException("City not found  :" ,JaxError.INVALID_CITY);
							}
						}
					}else{
						//throw new GlobalException("District not found  :" ,JaxError.INVALID_DISTRICT);
					}
				}else{
					//throw new GlobalException("State not found  :" ,JaxError.INVALID_STATE);
				}

			}else{
				//throw new GlobalException("Country not found  :" ,JaxError.COUNTRY_NOT_FOUND);
			}
		} //end 

		return shippingAddressDto;
}




	private JaxTransactionStatus getJaxTransactionStatus(PaygDetailsModel pgDetailsModel,List<ReceiptPaymentApp> applReceipt) {
		JaxTransactionStatus status = JaxTransactionStatus.APPLICATION_CREATED;
		String applicationStatus ="";
		if(!applReceipt.isEmpty()){
			applicationStatus = applReceipt.get(0).getApplicationStatus();
		}

		if (StringUtils.isBlank(applicationStatus) && pgDetailsModel != null && pgDetailsModel.getPgPaymentId() != null) {
			status = JaxTransactionStatus.PAYMENT_IN_PROCESS;
		}
		String resultCode = pgDetailsModel.getResultCode();
		if ("CAPTURED".equalsIgnoreCase(resultCode)) {
			if ("S".equals(applicationStatus) || "T".equals(applicationStatus)) {
				status = JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_SUCCESS;
			} else {
				status = JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_FAIL;
			}
		}
		if ("NOT CAPTURED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_FAIL;
		}
		if ("CANCELED".equalsIgnoreCase(resultCode) || "CANCELLED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_CANCELED_BY_USER;
		}

		return status;
	}


}
