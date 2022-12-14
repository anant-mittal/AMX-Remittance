package com.amx.jax.manager;

/**
 * @author : Rabil
 * @date   : 05/11/2018
 */

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.FcSaleExchangeRateDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.CurrencyWiseDenominationMdlv1;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ExEmailNotification;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.fx.ForeignCurrencyStockView;
import com.amx.jax.dbmodel.fx.FxExchangeRateView;
import com.amx.jax.dbmodel.fx.FxOrderTranxLimitView;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.request.fx.FcSaleOrderFailReportDTO;
import com.amx.jax.model.response.fx.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.fx.FxExchangeRateBreakup;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.repository.CurrencyWiseDenominationRepository;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IExEmailNotificationDao;
import com.amx.jax.repository.fx.ForeignCurrencyMaxStockRepository;
import com.amx.jax.repository.fx.FxOrderTranxLimitRespository;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.utils.JsonUtil;
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FcSaleOrderTransactionManager extends AbstractModel{

	private Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired
	FcSaleExchangeRateDao fcSaleExchangeRateDao;

	@Autowired
	CurrencyMasterService currencyMasterService;

	@Autowired
	private MetaData meta;

	@Autowired
	ICurrencyDao currencyDao;

	@Autowired
	AuthenticationLimitCheckDAO authentication;

	@Autowired
	FxOrderTranxLimitRespository trnxLimitRepos;

	@Autowired
	CurrencyWiseDenominationRepository currenDenominationRepository;

	@Autowired
	ForeignCurrencyMaxStockRepository foreignCurrencyMaxStockRepository;

	@Autowired
	IExEmailNotificationDao emailNotificationDao;
	
	@Autowired
	ICustomerRepository customerDao;
	
	@Autowired
	JaxNotificationService jaxNotificationService;
	
	@Autowired
	PushNotifyClient pushNotifyClient;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5831221861217249594L;

	public FcSaleOrderApplicationResponseModel calculateTrnxRate(BigDecimal countryId,BigDecimal countryBracnhId ,BigDecimal fcCurrencyId,BigDecimal fcAmount){
		BigDecimal maxExchangeRate = BigDecimal.ZERO;
		BigDecimal customerId = meta.getCustomerId();
		logger.debug("calculateTrnxRate fc currencyId :"+fcCurrencyId+"\t fcAmount :"+fcAmount+"\t countryId :"+countryId+"\t countryBracnhId :"+countryBracnhId);
		FcSaleOrderApplicationResponseModel responseModel = new FcSaleOrderApplicationResponseModel();

		List<CurrencyMasterMdlv1> curr =currencyDao.getCurrencyList(fcCurrencyId);

		if(JaxUtil.isNullZeroBigDecimalCheck(fcAmount) && fcAmount.compareTo(BigDecimal.ZERO)<0){
			throw new GlobalException(JaxError.INVALID_EXCHANGE_AMOUNT, "Negative not allowed");
		}

		if(curr !=null && curr.isEmpty()){
			throw new GlobalException(JaxError.INVALID_CURRENCY_ID, "Currency is not  available/invalid currency id");
		}

		// checking minimum denomination
		checkMinDenomination(fcCurrencyId,fcAmount);

		FxExchangeRateBreakup breakup = new FxExchangeRateBreakup();
		List<FxExchangeRateView> fxSaleRateList = fcSaleExchangeRateDao.getFcSaleExchangeRate(countryId, countryBracnhId, fcCurrencyId);

		if(fxSaleRateList!= null && !fxSaleRateList .isEmpty()){
			maxExchangeRate = fxSaleRateList.get(0).getSalMaxRate();
		}else{
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No record found");
		}

		List<ParameterDetails> parameterList 	= fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes);
		BigDecimal localCurrencyId = meta.getDefaultCurrencyId();
		breakup.setFcDecimalNumber(currencyMasterService.getCurrencyMasterById(fcCurrencyId).getDecinalNumber());
		breakup.setLcDecimalNumber(currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber());

		if(fxSaleRateList!= null && !fxSaleRateList .isEmpty()){
			maxExchangeRate = fxSaleRateList.get(0).getSalMaxRate();
		}else{
			throw new GlobalException(JaxError.FC_CURRENCY_RATE_IS_NOT_AVAILABLE, "Fc currency rate is not defiend");
		}
		logger.debug(" maxExchangeRate  :"+maxExchangeRate +"\t : for Currency  :"+fcCurrencyId+"\t Fc amount :"+fcAmount);

		if(parameterList != null && !parameterList.isEmpty()){
			responseModel.setTxnFee(RoundUtil.roundBigDecimal(parameterList.get(0).getNumericField1()==null?BigDecimal.ZERO:parameterList.get(0).getNumericField1(),breakup.getLcDecimalNumber().intValue()));
		}else{
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_CHARGES_NOT_FOUND, "Fc delivery charge is not defined");
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
			breakup.setNetAmount(breakup.getConvertedLCAmount().add(responseModel.getTxnFee()));
		}

		if(JaxUtil.isNullZeroBigDecimalCheck(maxExchangeRate)){
			breakup.setInverseRate(RoundUtil.roundBigDecimal(new BigDecimal(1).divide(maxExchangeRate, 10, RoundingMode.HALF_UP),breakup.getFcDecimalNumber().intValue()));
		}
		responseModel.setExRateBreakup(breakup);
		logger.debug("breakup rate maxExchangeRate  :"+breakup.getRate() +"\t : for Currency  :"+fcCurrencyId+"\t Fc amount :"+breakup.getConvertedFCAmount()+"\t LC amount :"+breakup.getConvertedLCAmount());

		checkFCSaleTrnxLimit(breakup,fcCurrencyId,fcAmount,localCurrencyId,customerId);

		return responseModel; 
	}


	/**
	 * 
	 * @param exchbreakUpRate
	 * @param fcCurrencyId
	 * @param fcAmount
	 * @param localCurrencyId
	 * @param customerId
	 */
	public void checkFCSaleTrnxLimit(FxExchangeRateBreakup exchbreakUpRate,BigDecimal fcCurrencyId,BigDecimal fcAmount,BigDecimal localCurrencyId,BigDecimal customerId){
		BigDecimal fcTrnxLimitPerDay = BigDecimal.ZERO;
		BigDecimal fxTrnxHistAmount = BigDecimal.ZERO;
		if(!JaxUtil.isNullZeroBigDecimalCheck(customerId)){
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND,"Customer not found ");
		}
		String quoteName ="";
		int localDecimal =0;
		if(JaxUtil.isNullZeroBigDecimalCheck(fcAmount) && fcAmount.compareTo(BigDecimal.ZERO)<0){
			throw new GlobalException(JaxError.INVALID_EXCHANGE_AMOUNT, "Negative or zeror not allowed");
		}
		if(fcAmount.compareTo(BigDecimal.ZERO)<=0){
			throw new GlobalException(JaxError.ZERO_NOT_ALLOWED,"Enter valid amount ");
		}
		CurrencyMasterMdlv1 curr = currencyMasterService.getCurrencyMasterById(localCurrencyId);
		if(curr!=null){
			quoteName = curr.getQuoteName();
			localDecimal = curr.getDecinalNumber()==null?0:curr.getDecinalNumber().intValue();
		}

		AuthenticationLimitCheckView authLimit = authentication.getFxOrderTxnLimit();
		if(authLimit!=null){
			fcTrnxLimitPerDay = authLimit.getAuthLimit();
			FxOrderTranxLimitView trnxViewModel = trnxLimitRepos.getFxTransactionLimit(customerId);
			if(trnxViewModel!=null){
				fxTrnxHistAmount  = trnxViewModel.getTotalAmount()==null?BigDecimal.ZERO:trnxViewModel.getTotalAmount().add(exchbreakUpRate.getConvertedLCAmount());
			}else{
				fxTrnxHistAmount = exchbreakUpRate.getConvertedLCAmount();
			}
			if(JaxUtil.isNullZeroBigDecimalCheck(fxTrnxHistAmount) && JaxUtil.isNullZeroBigDecimalCheck(fcTrnxLimitPerDay) && fxTrnxHistAmount.compareTo(fcTrnxLimitPerDay)>=0){
				throw new GlobalException(JaxError.FC_SALE_TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED,"You have reached daily limit of FC Sale  "+quoteName +" "+RoundUtil.roundBigDecimal(fcTrnxLimitPerDay,localDecimal));
			}
		}else{
			throw new GlobalException(JaxError.FC_SALE_DAY_LIMIT_SETUP_NOT_DIFINED,"FX Order limit setup is not defined");
		}

	}



	public void checkMinDenomination(BigDecimal fcCurrencyId,BigDecimal fcAmount) {
		BigDecimal denominationId = null;
		List<CurrencyMasterMdlv1> curr =currencyDao.getCurrencyList(fcCurrencyId);
		if(curr!=null && !curr.isEmpty()) {
			denominationId = curr.get(0).getMinDenominationId();
		}
		if(JaxUtil.isNullZeroBigDecimalCheck(denominationId)) {
			CurrencyWiseDenominationMdlv1 currDenomination = currenDenominationRepository.getMinimumCurrencyDenominationValue(meta.getCountryId(),fcCurrencyId,denominationId, ConstantDocument.Yes);
			if(currDenomination!=null && JaxUtil.isNullZeroBigDecimalCheck(currDenomination.getDenominationAmount())) {
				double minDenoAmount = currDenomination.getDenominationAmount().doubleValue();
				double fcAmountDouble  =fcAmount.doubleValue(); 
				double result = fcAmountDouble%minDenoAmount;
				if(result!=0) {
					throw new GlobalException(JaxError.MIN_DENOMINATION_ERROR,"The foreign amount ("+fcAmountDouble+") should be mutiple of "+minDenoAmount);
				}
			}
		} /*
		 * else { throw new GlobalException(JaxError.
		 * MIN_DENOMINATION_ERROR,"Minimum denomination is not defined"); }
		 */
	}

	// stock (staff and safe ??? across all branches AND Murgab 3 FC branch) * 75%
	public void checkMaximumAmountPerCurrency(ReceiptPaymentApp receiptPayment) {
		ForeignCurrencyStockView foreignCurrencyStockView = foreignCurrencyMaxStockRepository.findByCurrencyId(receiptPayment.getForeignCurrencyId());
		if(foreignCurrencyStockView != null && foreignCurrencyStockView.getMaxStockBalance() != null && foreignCurrencyStockView.getMaxStockBalance().compareTo(BigDecimal.ZERO) != 0) {
			if(receiptPayment.getForignTrnxAmount() != null && receiptPayment.getForignTrnxAmount().compareTo(BigDecimal.ZERO) != 0 && receiptPayment.getForignTrnxAmount().compareTo(foreignCurrencyStockView.getMaxStockBalance()) > 0) {
				BigDecimal percentage = new BigDecimal(10).divide(new BigDecimal(100));
				BigDecimal percentageAmount = percentage.multiply(foreignCurrencyStockView.getMaxStockBalance());
				BigDecimal differenceAmount = foreignCurrencyStockView.getMaxStockBalance().add(percentageAmount);
				if(differenceAmount.compareTo(receiptPayment.getForignTrnxAmount()) >= 0) {
					sendFcSaleTranxFailReport(receiptPayment);
					throw new GlobalException(JaxError.FC_MAX_STOCK_ERROR,"The currency is out of stock for the amount you have chosen due to higher demand. You can place an order of ("+foreignCurrencyStockView.getCurrencyQuote()+" "+foreignCurrencyStockView.getMaxStockBalance()+") as of now. Else, we shall revert back to you via email once the currency is available for the amount you have chosen.");
				}else {
					sendFcSaleTranxFailReport(receiptPayment);
					throw new GlobalException(JaxError.FC_MAX_STOCK_ERROR,"The currency is out of stock for the amount you have chosen due to higher demand. We shall revert back to you via email once the currency is available for the amount you have chosen.");
				}
			}
		}else {
			sendFcSaleTranxFailReport(receiptPayment);
			throw new GlobalException(JaxError.INVALID_FC_STOCK_ERROR,"The currency is out of stock due to higher demand. We shall revert back to you via email once the currency is available.");
		}
	}

	// template to send fail transaction
	public void sendFcSaleTranxFailReport(ReceiptPaymentApp receiptPayment) {
		Customer customerDetails = null;
		// email ids
		List<ExEmailNotification> emailNotification = emailNotificationDao.getFCSupportTeamEmailNotification();

		FcSaleOrderFailReportDTO model = new FcSaleOrderFailReportDTO();
		
		if(receiptPayment.getCountryId() != null && receiptPayment.getCompanyId() != null && receiptPayment.getCustomerId() != null) {
			customerDetails = customerDao.getCustomerByCountryAndCompAndCustoemrId(receiptPayment.getCountryId(), receiptPayment.getCompanyId(), receiptPayment.getCustomerId());
			if(customerDetails != null) {
				model.setCustomerReference(customerDetails.getCustomerReference());
				model.setCustomerContact(customerDetails.getMobile());
			}
		}
		
		model.setCustomerName(receiptPayment.getCustomerName());

		if(receiptPayment.getForeignCurrencyId() != null) {
			CurrencyMasterMdlv1 settlementCurrencyModel = currencyMasterService.getCurrencyMasterById(receiptPayment.getForeignCurrencyId());
			if(settlementCurrencyModel != null) {
				model.setForeignCurrencyQuote(settlementCurrencyModel.getQuoteName());
			}
		}

		if(meta.getDefaultCurrencyId() != null) {
			CurrencyMasterMdlv1 localCurrencyModel = currencyMasterService.getCurrencyMasterById(meta.getDefaultCurrencyId());
			if(localCurrencyModel != null) {
				model.setLocalCurrencyQuote(localCurrencyModel.getQuoteName());
			}
		}
		model.setTransactionForeignAmount(receiptPayment.getForignTrnxAmount());
		model.setTransactionLocalAmount(receiptPayment.getLocalTrnxAmount());
		
		if(receiptPayment.getTravelStartDate() != null) {
			model.setFromDate(new SimpleDateFormat("dd-MMM-YYYY").format(receiptPayment.getTravelStartDate()));
		}
		if(receiptPayment.getTravelEndDate() != null) {
			model.setToDate(new SimpleDateFormat("dd-MMM-YYYY").format(receiptPayment.getTravelEndDate()));
		}

		logger.info("Email Foreign currency sale Fail Transaction : " + JsonUtil.toJson(model));
		
		if(emailNotification != null && emailNotification.size() != 0) {
			jaxNotificationService.sendFCSaleSupportErrorEmail(model, emailNotification);
		}
		if(customerDetails != null && customerDetails.getEmail() != null && receiptPayment.getCustomerId() != null) {
			// email notification
			jaxNotificationService.sendFCSaleCustomerErrorEmail(model, customerDetails.getEmail());
			
			// message notification for mobile
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(TemplatesMX.FC_OUTOF_STOCK_CUSTOMER);
			pushMessage.addToUser(receiptPayment.getCustomerId());
			logger.info("customer_id:"+receiptPayment.getCustomerId());
			pushMessage.getModel().put(RESP_DATA_KEY, model);
			pushNotifyClient.send(pushMessage);
		}
	}
}
