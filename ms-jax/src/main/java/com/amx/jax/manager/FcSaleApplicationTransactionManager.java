package com.amx.jax.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
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
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.jax.AbstractModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dao.FcSaleExchangeRateDao;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.FxExchangeRateView;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.ShoppingCartDetails;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.ShoppingCartDetailsDto;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;

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
		responeModel.setCartDetails(cartDetails);
		return responeModel; 
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveApplication", e.getMessage());
			throw new GlobalException("FC Sale application creation failed", JaxError.FS_APPLIATION_CREATION_FAILED);
		}
	}
	
	
	public ReceiptPaymentApp  createFcSaleReceiptApplication(FcSaleOrderTransactionRequestModel fcSalerequestModel){
		try{
		ReceiptPaymentApp receiptPaymentAppl = new ReceiptPaymentApp();
		BigDecimal locCode =   BigDecimal.ZERO;
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		BigDecimal companyId = metaData.getCompanyId();
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
			if(StringUtils.isEmpty(customerList.get(0).getMiddleName())){
				customerName = customerName +" "+customerList.get(0).getMiddleName();
			}
			if(StringUtils.isEmpty(customerList.get(0).getLastName())){
				customerName = customerName+ " "+ customerList.get(0).getLastName();
			}
			
			receiptPaymentAppl.setCustomerName(customerName);
		}
		
		CountryBranch countryBranch = bankMetaService.getCountryBranchById((metaData.getCountryBranchId()));
		if(countryBranch != null){
			locCode = countryBranch.getBranchId();
		}
		
		
		
		ExchangeRateBreakup exchbreakUpRate = getExchangeRateFcSaleOrder(applciationCountryid, countryBranchId, fcSalerequestModel.getForeignCurrencyId(), fcSalerequestModel.getForeignAmount());
		
		receiptPaymentAppl.setLocalCurrencyId(localCurrencyId);
		receiptPaymentAppl.setForeignCurrencyId(fcSalerequestModel.getForeignCurrencyId());
		receiptPaymentAppl.setForignTrnxAmount(fcSalerequestModel.getForeignAmount());
		receiptPaymentAppl.setLocalTrnxAmount(exchbreakUpRate.getConvertedLCAmount() ==null?BigDecimal.ZERO:exchbreakUpRate.getConvertedLCAmount());
		receiptPaymentAppl.setLocalNetAmount(exchbreakUpRate.getNetAmount()==null?BigDecimal.ZERO:exchbreakUpRate.getNetAmount());
		receiptPaymentAppl.setTransactionActualRate(exchbreakUpRate.getRate()==null?BigDecimal.ZERO:exchbreakUpRate.getRate());
		receiptPaymentAppl.setDeliveryCharges(exchbreakUpRate.getDeliveryCharges()==null?BigDecimal.ZERO:exchbreakUpRate.getDeliveryCharges());
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
		
		
		if(!StringUtils.isBlank(metaData.getReferrer())){
			receiptPaymentAppl.setCreatedBy(metaData.getReferrer());
		}else{
			if(!StringUtils.isBlank(metaData.getAppType())){				
				receiptPaymentAppl.setCreatedBy(metaData.getAppType());
			}else{
				receiptPaymentAppl.setCreatedBy("WEB");
			 }
		}
		receiptPaymentAppl.setRemarks(fcSalerequestModel.getRemarks());
		try {
			receiptPaymentAppl.setAccountMMYYYY(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
		} catch (ParseException e) {
			logger.error("Error in saving application", e);
		}
		
		return receiptPaymentAppl;
		}catch(Exception e){
			logger.error("createFcSaleReceiptApplication", e.getMessage());
			throw new GlobalException("FC Sale application creation failed", JaxError.FS_APPLIATION_CREATION_FAILED);
		}
	}
	
	
	
	
	public ExchangeRateBreakup getExchangeRateFcSaleOrder(BigDecimal countryId,BigDecimal countryBracnhId ,BigDecimal fcCurrencyId,BigDecimal fcAmount){
		ExchangeRateBreakup breakup = new ExchangeRateBreakup();
		
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
			breakup.setNetAmount(breakup.getConvertedLCAmount().add(breakup.getDeliveryCharges()));
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
	
	
	public List<ShoppingCartDetailsDto> fetchApplicationDetails(){
		List<ShoppingCartDetailsDto> cartListDto = new ArrayList<>();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		List<ShoppingCartDetails>  shoppingCartList  = fcSaleExchangeRateDao.getShoppingCartDetails(applciationCountryid,companyId,customerId);
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

	
	public  List<ShoppingCartDetailsDto>  convertShopingCartDto(List<ShoppingCartDetails> cartDetailList){
		List<ShoppingCartDetailsDto> cartListDto = new ArrayList<>();
		cartDetailList.forEach(cartDetails->cartListDto.add(convertCartDto(cartDetails)));
		return cartListDto;
	}
	
	public ShoppingCartDetailsDto convertCartDto(ShoppingCartDetails cartDetails){
		ShoppingCartDetailsDto  dto = new ShoppingCartDetailsDto ();
		try {
			BeanUtils.copyProperties(dto, cartDetails);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("convertModel  to convert currency", e);
		}
		return dto;
	}
	
	
	
	
	
}
