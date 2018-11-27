package com.amx.jax.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.amx.jax.AbstractModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dao.FcSaleExchangeRateDao;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxDeliveryTimeSlotMaster;
import com.amx.jax.dbmodel.fx.FxExchangeRateView;
import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.FxShoppingCartDetails;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.fx.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.request.fx.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.fx.FcSaleApplPaymentReponseModel;
import com.amx.jax.model.response.fx.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.fx.FxApplicationDto;
import com.amx.jax.model.response.fx.FxExchangeRateBreakup;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.model.response.fx.ShoppingCartDetailsDto;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.fx.FxOrderDeliveryTimeSlotRepository;
import com.amx.jax.repository.fx.FxOrderTransactionRespository;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.jax.validation.FxOrderValidation;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FcSaleApplicationTransactionManager extends AbstractModel{

	@Autowired
	MetaData metaData;
	
	@Autowired
	FinancialService finanacialService;
	
	@Autowired
	ApplicationProcedureDao applicationProcedureDao;
	
	@Autowired
	FcSaleApplicationDao fsSaleapplicationDao;
	
	@Autowired
	CurrencyMasterService currencyMasterService;
	
	@Autowired
	FcSaleExchangeRateDao fcSaleExchangeRateDao;
	
	@Autowired
	BankMetaService bankMetaService;
	
	@Autowired
	ICustomerRepository customerDao;
	
	@Autowired
	FxOrderDeliveryTimeSlotRepository fcSaleOrderTimeSlotDao;
	
	@Autowired
	FxOrderValidation validation;
	
	@Autowired
	ICurrencyDao currencyDao;
	
	@Autowired
	ICompanyDAO compDao;
	
	@Autowired
	IApplicationCountryRepository applCountryRepos;
	
	@Autowired
	private CountryBranchRepository countryBranchRepository;

	@Autowired
	ReceiptPaymentAppRepository rcptPaymentAppl;
	
	@Autowired
	FxOrderTransactionRespository fxTransactionHistroyDao;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4740757344377296626L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/** save application **/
	public FcSaleOrderApplicationResponseModel  saveApplication(FcSaleOrderTransactionRequestModel fcSalerequestModel){
		try{
		FcSaleOrderApplicationResponseModel responeModel = new FcSaleOrderApplicationResponseModel();
		HashMap<String, Object> mapAllDetailApplSave = new HashMap<String, Object>();
		deactivateApplications(fcSalerequestModel);
		ReceiptPaymentApp receiptPayment =this. createFcSaleReceiptApplication(fcSalerequestModel);
		mapAllDetailApplSave.put("EX_APPL_RECEIPT",receiptPayment);
		fsSaleapplicationDao.saveAllApplicationData(mapAllDetailApplSave);
		List<ShoppingCartDetailsDto> cartDetails= fetchApplicationDetails();
		fetchCustomerAddressDetails();
		List<String> fxOrderTimeSlot = fetchTimeSlot(null);
		responeModel.setTimeSlot(fxOrderTimeSlot);
		responeModel.setCartDetails(cartDetails);
		responeModel.setDeliveryCharges(getDeliveryChargesFromParameter()); 
		return responeModel; 
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveApplication", e.getMessage());
			throw new GlobalException("FC Sale application creation failed");
		}
	}
	
	
	/** Save application payment **/
	public FcSaleApplPaymentReponseModel saveApplicationPayment(FcSaleOrderPaynowRequestModel requestmodel){
		FcSaleApplPaymentReponseModel responeModel = new FcSaleApplPaymentReponseModel();
		PaygDetailsModel pgDetails = createPgDetails(requestmodel);
		FxDeliveryDetailsModel deliveryDetailsModel = createDeliveryDeetails(requestmodel);
		List<ParameterDetails> parameterList 	= fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes);
		HashMap<String, Object> mapAllDetailApplSave = new HashMap<String, Object>();
		mapAllDetailApplSave.put("EX_PAYG_DETAILS", pgDetails);
		mapAllDetailApplSave.put("EX_DELIVERY_DETAILS", deliveryDetailsModel);
		mapAllDetailApplSave.put("requestmodel", requestmodel);
		fsSaleapplicationDao.saveAllAppDetails(mapAllDetailApplSave);
		BigDecimal knetamount= BigDecimal.ZERO;
		List<FxApplicationDto> listShoppingCart = requestmodel.getCartDetailList();
		if(!listShoppingCart.isEmpty()){
		for(FxApplicationDto shoppingCart:listShoppingCart){
			ReceiptPaymentApp rcptAppl = rcptPaymentAppl.findOne(shoppingCart.getApplicationId());
			if(rcptAppl!=null && rcptAppl.getIsActive().equalsIgnoreCase(ConstantDocument.Yes) 
					&& rcptAppl.getApplicationStatus().equalsIgnoreCase(ConstantDocument.S)){
				knetamount=knetamount.add(rcptAppl.getLocalTrnxAmount());
				responeModel.setRemittanceAppId(rcptAppl.getReceiptId());
				responeModel.setMerchantTrackId(metaData.getCustomerId());
				responeModel.setDocumentIdForPayment(rcptAppl.getPgPaymentSeqDtlId()==null?"":rcptAppl.getPgPaymentSeqDtlId().toString());
				UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
				if(userFinancialYear!=null){
					responeModel.setDocumentFinancialYear(userFinancialYear.getFinancialYear());
				}
			}
		} //end of for Loop.
		}else{
			throw new GlobalException("No record found for payment", JaxError.NO_RECORD_FOUND);
		}
		
		logger.info("Total knet amount without delivery charges: "+knetamount);
		if(!parameterList.isEmpty()){
			logger.info("Delivery charges: "+parameterList.get(0).getNumericField1());
			knetamount = knetamount.add(parameterList.get(0).getNumericField1());
		}else{
			logger.info("Delivery charges: "+parameterList.get(0).getNumericField1());
			throw new GlobalException("Fc Delivery not defined", JaxError.FC_CURRENCY_DELIVERY_CHARGES_NOT_FOUND);
		}
		responeModel.setNetPayableAmount(knetamount);
		logger.info("Total knet amount with delivery charges: "+knetamount);
		return responeModel; 
	}
	
	
	
	
	public ReceiptPaymentApp  createFcSaleReceiptApplication(FcSaleOrderTransactionRequestModel fcSalerequestModel){
		ReceiptPaymentApp receiptPaymentAppl = new ReceiptPaymentApp();
		try{
		BigDecimal locCode =   BigDecimal.ZERO;
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		ApplicationSetup appl = applCountryRepos.getApplicationSetupDetails();
		if(appl!= null){
			applciationCountryid = appl.getApplicationCountryId();
			companyId =appl.getCompanyId();
		}
		if(!currencyDao.getCurrencyListByCountryId(applciationCountryid).isEmpty()) {
			localCurrencyId = currencyDao.getCurrencyListByCountryId(applciationCountryid).get(0).getCurrencyId();
		}	
		BigDecimal customerId = metaData.getCustomerId();
		String customerName = new String();
		receiptPaymentAppl.setCompanyId(companyId);
		receiptPaymentAppl.setCountryId(applciationCountryid);
		receiptPaymentAppl.setCustomerId(customerId);
		
		List<Customer> customerList = customerDao.getCustomerByCustomerId(applciationCountryid, companyId, customerId);
		if(customerList !=null && !customerList.isEmpty()){
			if(customerList.get(0).getFirstName() !=null){
				customerName = customerList.get(0).getFirstName(); 
			}
			if(!StringUtils.isEmpty(customerList.get(0).getMiddleName())){
				customerName = customerName +" "+customerList.get(0).getMiddleName();
			}
			if(!StringUtils.isEmpty(customerList.get(0).getLastName())){
				customerName = customerName+ " "+ customerList.get(0).getLastName();
			}
			
			receiptPaymentAppl.setCustomerName(customerName);
		}else{
				logger.error("Customer is not registered"+customerId);
				throw new GlobalException("Customer is not registered", JaxError.CUSTOMER_NOT_REGISTERED_ONLINE.getStatusKey());
			}
		
		CountryBranch countryBranch = countryBranchRepository.findByBranchId(ConstantDocument.ONLINE_BRANCH_LOC_CODE);
		if(countryBranch != null){
			locCode = countryBranch.getBranchId();
			countryBranchId =countryBranch.getCountryBranchId(); 
		}else{
			throw new GlobalException("Country branch is not found", JaxError.INVALID_COUNTRY_BRANCH);
		}
		validation.validateHeaderInfo();
		
		FxExchangeRateBreakup exchbreakUpRate = getExchangeRateFcSaleOrder(applciationCountryid, countryBranchId, fcSalerequestModel.getForeignCurrencyId(), fcSalerequestModel.getForeignAmount());
		
		receiptPaymentAppl.setLocalCurrencyId(localCurrencyId);
		receiptPaymentAppl.setForeignCurrencyId(fcSalerequestModel.getForeignCurrencyId());
		receiptPaymentAppl.setForignTrnxAmount(fcSalerequestModel.getForeignAmount());
		receiptPaymentAppl.setLocalTrnxAmount(exchbreakUpRate.getConvertedLCAmount() ==null?BigDecimal.ZERO:exchbreakUpRate.getConvertedLCAmount());
		receiptPaymentAppl.setLocalNetAmount(exchbreakUpRate.getNetAmount()==null?BigDecimal.ZERO:exchbreakUpRate.getNetAmount());
		receiptPaymentAppl.setTransactionActualRate(exchbreakUpRate.getRate()==null?BigDecimal.ZERO:exchbreakUpRate.getRate());
		receiptPaymentAppl.setBranchId(countryBranchId);
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		if(userFinancialYear!=null){
		receiptPaymentAppl.setDocumentFinanceYear(userFinancialYear.getFinancialYear());
		}
		
		receiptPaymentAppl.setDenominationType(fcSalerequestModel.getCurrencyDenominationType());
		receiptPaymentAppl.setTransactionType(ConstantDocument.S);
		receiptPaymentAppl.setIsActive(ConstantDocument.Yes);
		receiptPaymentAppl.setDocumentStatus(ConstantDocument.Yes);
		receiptPaymentAppl.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE_APPLICATION);
		receiptPaymentAppl.setDocumentNo(generateDocumentNumber(countryBranch, ConstantDocument.Update,receiptPaymentAppl.getDocumentFinanceYear()));
		
		if(fcSalerequestModel.getSourceOfFundId()!=null){
		receiptPaymentAppl.setSourceofIncomeId(fcSalerequestModel.getSourceOfFundId());
		receiptPaymentAppl.setSourceOfIncomeid(fcSalerequestModel.getSourceOfFundId());
		}
		if(fcSalerequestModel.getPurposeOfTrnx()!=null){
			receiptPaymentAppl.setPurposeofTransactionId(fcSalerequestModel.getPurposeOfTrnx());
		}
		
		receiptPaymentAppl.setTransactionIPAddress(metaData.getDeviceIp());
		
		
		
		receiptPaymentAppl.setDocumentDate(new Date());
		receiptPaymentAppl.setGeneralLegerDate(new Date());
		receiptPaymentAppl.setCreatedDate(new Date());
		
		receiptPaymentAppl.setTravelCountryId(fcSalerequestModel.getTravelCountryId());
		receiptPaymentAppl.setTravelStartDate(fcSalerequestModel.getStartDate()==null?new Date():new Date(fcSalerequestModel.getStartDate()));
		receiptPaymentAppl.setTravelEndDate(fcSalerequestModel.getEndDate()==null?new Date():new Date(fcSalerequestModel.getEndDate()));
		
		
		if(!StringUtils.isBlank(metaData.getReferrer())){
			receiptPaymentAppl.setCreatedBy(metaData.getReferrer());
		}else{
			if(!StringUtils.isBlank(metaData.getAppType())){				
				receiptPaymentAppl.setCreatedBy(metaData.getAppType());
			}else{
				receiptPaymentAppl.setCreatedBy("WEB");
			 }
		}
		try {
			receiptPaymentAppl.setAccountMMYYYY(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
		} catch (ParseException e) {
			logger.error("Error in saving application", e);
		}
		
		
		}catch(Exception e){
			logger.error("createFcSaleReceiptApplication", e.getMessage());
			//throw new GlobalException("FC Sale application creation failed", JaxError.FS_APPLIATION_CREATION_FAILED);
		}
		
		return receiptPaymentAppl;	
	}
	
	
	
	
	public FxExchangeRateBreakup getExchangeRateFcSaleOrder(BigDecimal countryId,BigDecimal countryBracnhId ,BigDecimal fcCurrencyId,BigDecimal fcAmount){
		FxExchangeRateBreakup breakup = new FxExchangeRateBreakup();
		
		try{
		
		BigDecimal maxExchangeRate = BigDecimal.ZERO;
		logger.info("calculateTrnxRate fc currencyId :"+fcCurrencyId+"\t fcAmount :"+fcAmount+"\t countryId :"+countryId+"\t countryBracnhId :"+countryBracnhId);
		FcSaleOrderApplicationResponseModel responseModel = new FcSaleOrderApplicationResponseModel();
		
		List<FxExchangeRateView> fxSaleRateList = fcSaleExchangeRateDao.getFcSaleExchangeRate(countryId, countryBracnhId, fcCurrencyId);
		List<ParameterDetails> parameterList 	= fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes);
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		breakup.setFcDecimalNumber(currencyMasterService.getCurrencyMasterById(fcCurrencyId).getDecinalNumber());
		breakup.setLcDecimalNumber(currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber());
		
		if(fxSaleRateList!= null && !fxSaleRateList .isEmpty()){
			maxExchangeRate = fxSaleRateList.get(0).getSalMaxRate();
		}
		logger.info(" maxExchangeRate  :"+maxExchangeRate +"\t : for Currency  :"+fcCurrencyId+"\t Fc amount :"+fcAmount);
		
		if(parameterList != null && !parameterList.isEmpty()){
			breakup.setDeliveryCharges(RoundUtil.roundBigDecimal(parameterList.get(0).getNumericField1()==null?BigDecimal.ZERO:parameterList.get(0).getNumericField1(),breakup.getLcDecimalNumber().intValue()));
		}		
		if(JaxUtil.isNullZeroBigDecimalCheck(maxExchangeRate) && JaxUtil.isNullZeroBigDecimalCheck(fcAmount)){
			breakup.setConvertedLCAmount(maxExchangeRate.multiply(fcAmount));
		}
		if(JaxUtil.isNullZeroBigDecimalCheck(fcAmount)){
			breakup.setConvertedFCAmount(RoundUtil.roundBigDecimal(fcAmount,breakup.getFcDecimalNumber().intValue()));
		}
		if(JaxUtil.isNullZeroBigDecimalCheck(breakup.getConvertedLCAmount())){
			breakup.setConvertedLCAmount(RoundUtil.roundBigDecimal(breakup.getConvertedLCAmount(), breakup.getLcDecimalNumber().intValue()));
		}
		breakup.setRate(maxExchangeRate);
		if(JaxUtil.isNullZeroBigDecimalCheck(breakup.getConvertedLCAmount())){
			breakup.setNetAmount(breakup.getConvertedLCAmount());
			//breakup.setNetAmount(breakup.getConvertedLCAmount().add(breakup.getDeliveryCharges()));
		}
		
		return breakup;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getExchangeRateFcSaleOrder", e.getMessage());
			throw new GlobalException("FC Sale application exchange", JaxError.FS_APPLIATION_CREATION_FAILED);
		}
	}
	
	
	public void deactivateApplications(FcSaleOrderTransactionRequestModel fcSalerequestModel){
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal foreignCurrencyId = fcSalerequestModel.getForeignCurrencyId();
		fsSaleapplicationDao.deActiveUnUsedapplication(customerId,foreignCurrencyId);
	}
	
	
	public FcSaleOrderApplicationResponseModel removeitemFromCart(BigDecimal applId){
		FcSaleOrderApplicationResponseModel responeModel = new FcSaleOrderApplicationResponseModel();
	try{
		fsSaleapplicationDao.removeItemFromCart(applId);
		List<ShoppingCartDetailsDto> cartDetails= fetchApplicationDetails();
		responeModel.setCartDetails(cartDetails);
	}catch(Exception e){
		e.printStackTrace();
		logger.error("removeitemFromCart ", e.getMessage()+"applId :"+applId);
	}
	return responeModel;
		
	}
	
	public List<ShoppingCartDetailsDto> fetchApplicationDetails(){
		List<ShoppingCartDetailsDto> cartListDto = new ArrayList<>();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		List<FxShoppingCartDetails>  shoppingCartList  = fcSaleExchangeRateDao.getFcSaleShoppingCartDetails(applciationCountryid, companyId, customerId);
		if(!shoppingCartList.isEmpty()){
			cartListDto = convertShopingCartDto(shoppingCartList);
		}
		
		return cartListDto;
	}
	
	
	public void fetchCustomerAddressDetails(){
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		
	}
	
	
	
	public BigDecimal generateDocumentNumber(CountryBranch countryBranch, String processInd,BigDecimal finYear) {
		BigDecimal appCountryId = metaData.getCountryId()==null?BigDecimal.ZERO:metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId()==null?BigDecimal.ZERO:metaData.getCompanyId();
		BigDecimal documentId = ConstantDocument.DOCUMENT_CODE_FOR_FCSALE;
		BigDecimal branchId = countryBranch.getBranchId()==null?BigDecimal.ZERO:countryBranch.getBranchId();
		Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId,finYear, processInd, branchId);
		return (BigDecimal) output.get("P_DOC_NO");
	}

	
	
	public List<String> fetchTimeSlot(String date){
		List<String> timeSlotList = new ArrayList<>();
		BigDecimal appCountryId = metaData.getCountryId()==null?BigDecimal.ZERO:metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId()==null?BigDecimal.ZERO:metaData.getCompanyId();
		List<FxDeliveryTimeSlotMaster> list = fcSaleOrderTimeSlotDao.findByCountryIdAndCompanyIdAndIsActive(appCountryId, companyId, ConstantDocument.Yes);
		
		if(!list.isEmpty()){
			BigDecimal  startTime = list.get(0).getStartTime()==null?BigDecimal.ZERO:list.get(0).getStartTime();
			BigDecimal  endTime = list.get(0).getEndTime()==null?BigDecimal.ZERO:list.get(0).getEndTime();
			BigDecimal  timeInterval = list.get(0).getTimeInterval()==null?BigDecimal.ZERO:list.get(0).getTimeInterval();
			timeSlotList =DateUtil.getTimeSlotRange(date,startTime.intValue(),endTime.intValue(),timeInterval.intValue());
		}else{
			throw new GlobalException("No data found in DB", JaxError.FC_SALE_TIME_SLOT_SETUP_MISSING);
		}
		return timeSlotList;
	}
	
	
	
	
	public  List<ShoppingCartDetailsDto>  convertShopingCartDto(List<FxShoppingCartDetails> cartDetailList){
		List<ShoppingCartDetailsDto> cartListDto = new ArrayList<>();
		cartDetailList.forEach(cartDetails->cartListDto.add(convertCartDto(cartDetails)));
		return cartListDto;
	}
	
	public ShoppingCartDetailsDto convertCartDto(FxShoppingCartDetails cartDetails){
		ShoppingCartDetailsDto  dto = new ShoppingCartDetailsDto ();
		try {
			BeanUtils.copyProperties(dto, cartDetails);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("convertModel  to convert currency", e);
		}
		return dto;
	}
	

	
	public PaygDetailsModel createPgDetails(FcSaleOrderPaynowRequestModel requestmodel){
		List<FxApplicationDto> listShoppingCart = requestmodel.getCartDetailList();
		PaygDetailsModel pgmodel = new PaygDetailsModel();
		
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		if(userFinancialYear!=null){
			pgmodel.setCollDocFYear(userFinancialYear.getFinancialYear());
		}
		pgmodel.setCreationDate(new Date());
		pgmodel.setCustomerId(metaData.getCustomerId());
		pgmodel.setTrnxType(ConstantDocument.S);
		if(!StringUtils.isBlank(metaData.getReferrer())){
			pgmodel.setCreatedBy(metaData.getReferrer());
		}else{
			if(!StringUtils.isBlank(metaData.getAppType())){				
				pgmodel.setCreatedBy(metaData.getAppType());
			}else{
				pgmodel.setCreatedBy("WEB");
			 }
		}
		return pgmodel;
		
	}
	
	
	public FxDeliveryDetailsModel createDeliveryDeetails (FcSaleOrderPaynowRequestModel requestmodel){
		FxDeliveryDetailsModel deliveryDetails = new FxDeliveryDetailsModel();
		StringBuffer appldocNo=new StringBuffer();
		deliveryDetails.setCreatedDate(new Date());
		deliveryDetails.setDeliveryDate(requestmodel.getDeliveryDate()==null?new Date():new Date(requestmodel.getDeliveryDate()));
		deliveryDetails.setDeliveryTimeSlot(requestmodel.getTimeSlot());
		deliveryDetails.setDeliveryCharges(getDeliveryChargesFromParameter());
		deliveryDetails.setShippingAddressId(requestmodel.getShippingAddressId());
		if(!requestmodel.getCartDetailList().isEmpty()){
			for(FxApplicationDto dto :requestmodel.getCartDetailList()){
				appldocNo =appldocNo.append(dto.getApplicationId()).append(",");
			}
			deliveryDetails.setApplDocNo(appldocNo.toString());
		}
		deliveryDetails.setIsActive(ConstantDocument.Yes);
		if(!StringUtils.isBlank(metaData.getReferrer())){
			deliveryDetails.setCreatedBy(metaData.getReferrer());
		}else{
			if(!StringUtils.isBlank(metaData.getAppType())){				
				deliveryDetails.setCreatedBy(metaData.getAppType());
			}else{
				deliveryDetails.setCreatedBy("WEB");
			 }
		}
		return deliveryDetails;
	}
	
	public BigDecimal 	getDeliveryChargesFromParameter(){
		List<ParameterDetails> parameterList 	= fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes);
	   BigDecimal delicharges = BigDecimal.ZERO;
	   BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
	   BigDecimal localDecimalCurr =currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber();
		if(parameterList != null && !parameterList.isEmpty()){
			delicharges =RoundUtil.roundBigDecimal(parameterList.get(0).getNumericField1()==null?BigDecimal.ZERO:parameterList.get(0).getNumericField1(),localDecimalCurr==null?0:localDecimalCurr.intValue());
		}	
		return delicharges; 
	}
	
	
	public List<FxOrderTransactionHistroyDto> getFxOrderTransactionHistroy(){
		 BigDecimal customerId = metaData.getCustomerId();
		 List<FxOrderTransactionModel> trnxFxOrderList = new ArrayList<FxOrderTransactionModel>();
		List<FxOrderTransactionHistroyDto>   trnxFxOrderListDto = new ArrayList<>();
		trnxFxOrderList= fxTransactionHistroyDao.getFxOrderTrnxList(customerId);
		if(trnxFxOrderList.isEmpty()){
			throw new GlobalException("fx order list is empty", JaxError.NO_RECORD_FOUND);
		}else{
			trnxFxOrderListDto = convertFxHistDto(trnxFxOrderList);
		}
		
		return trnxFxOrderListDto;
	}
	
public List<FxOrderTransactionHistroyDto> convertFxHistDto(List<FxOrderTransactionModel> trnxFxOrderList){
	List<FxOrderTransactionHistroyDto> dtoList = new ArrayList<>();
	trnxFxOrderList.forEach(trnxDetails->dtoList.add(convertTrnxDto(trnxDetails)));
	return dtoList;
}
	
public FxOrderTransactionHistroyDto convertTrnxDto(FxOrderTransactionModel trnxDetails){
	FxOrderTransactionHistroyDto  dto = new FxOrderTransactionHistroyDto();
try {
	BeanUtils.copyProperties(dto, trnxDetails);
} catch (IllegalAccessException | InvocationTargetException e) {
	logger.error("convertModel  to convert currency", e);
}
return dto;
}

 	
}
