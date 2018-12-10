package com.amx.jax.manager;

/**
 * @author : Rabil
 * @date   : 05/11/2018
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.fx.FxExchangeRateView;
import com.amx.jax.dbmodel.fx.FxOrderTranxLimitView;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.fx.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.fx.FxExchangeRateBreakup;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.fx.FxOrderTranxLimitRespository;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
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

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5831221861217249594L;

	public FcSaleOrderApplicationResponseModel calculateTrnxRate(BigDecimal countryId,BigDecimal countryBracnhId ,BigDecimal fcCurrencyId,BigDecimal fcAmount){
		BigDecimal maxExchangeRate = BigDecimal.ZERO;
		BigDecimal customerId = meta.getCustomerId();
		logger.debug("calculateTrnxRate fc currencyId :"+fcCurrencyId+"\t fcAmount :"+fcAmount+"\t countryId :"+countryId+"\t countryBracnhId :"+countryBracnhId);
		FcSaleOrderApplicationResponseModel responseModel = new FcSaleOrderApplicationResponseModel();
		
		List<CurrencyMasterModel> curr =currencyDao.getCurrencyList(fcCurrencyId);
		
		if(JaxUtil.isNullZeroBigDecimalCheck(fcAmount) && fcAmount.compareTo(BigDecimal.ZERO)<0){
			throw new GlobalException("Negative not allowed", JaxError.INVALID_EXCHANGE_AMOUNT);
		}
		
		if(curr !=null && curr.isEmpty()){
			throw new GlobalException("Currency is not  available/invalid currency id", JaxError.INVALID_CURRENCY_ID);
		}
		FxExchangeRateBreakup breakup = new FxExchangeRateBreakup();
		List<FxExchangeRateView> fxSaleRateList = fcSaleExchangeRateDao.getFcSaleExchangeRate(countryId, countryBracnhId, fcCurrencyId);
		
		if(fxSaleRateList!= null && !fxSaleRateList .isEmpty()){
			maxExchangeRate = fxSaleRateList.get(0).getSalMaxRate();
		}else{
			throw new GlobalException("No record found", JaxError.NO_RECORD_FOUND);
		}
		
		List<ParameterDetails> parameterList 	= fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes);
		BigDecimal localCurrencyId = meta.getDefaultCurrencyId();
		breakup.setFcDecimalNumber(currencyMasterService.getCurrencyMasterById(fcCurrencyId).getDecinalNumber());
		breakup.setLcDecimalNumber(currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber());
		
		if(fxSaleRateList!= null && !fxSaleRateList .isEmpty()){
			maxExchangeRate = fxSaleRateList.get(0).getSalMaxRate();
		}else{
			throw new GlobalException("Fc currency rate is not defiend", JaxError.FC_CURRENCY_RATE_IS_NOT_AVAILABLE);
		}
		logger.debug(" maxExchangeRate  :"+maxExchangeRate +"\t : for Currency  :"+fcCurrencyId+"\t Fc amount :"+fcAmount);
		
		if(parameterList != null && !parameterList.isEmpty()){
			responseModel.setTxnFee(RoundUtil.roundBigDecimal(parameterList.get(0).getNumericField1()==null?BigDecimal.ZERO:parameterList.get(0).getNumericField1(),breakup.getLcDecimalNumber().intValue()));
		}else{
			throw new GlobalException("Fc delivery charge is not defined", JaxError.FC_CURRENCY_DELIVERY_CHARGES_NOT_FOUND);
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
			throw new GlobalException("Customer not found ",JaxError.CUSTOMER_NOT_FOUND);
		}
		String quoteName ="";
		int localDecimal =0;
		if(JaxUtil.isNullZeroBigDecimalCheck(fcAmount) && fcAmount.compareTo(BigDecimal.ZERO)<0){
			throw new GlobalException("Negative or zeror not allowed", JaxError.INVALID_EXCHANGE_AMOUNT);
		}
		if(fcAmount.compareTo(BigDecimal.ZERO)<=0){
			throw new GlobalException("Enter valid amount ",JaxError.ZERO_NOT_ALLOWED);
		}
		CurrencyMasterModel curr = currencyMasterService.getCurrencyMasterById(localCurrencyId);
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
			 throw new GlobalException("You have reached daily limit of FC Sale  "+quoteName +" "+RoundUtil.roundBigDecimal(fcTrnxLimitPerDay,localDecimal),JaxError.FC_SALE_TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED);
		 }
		}else{
			throw new GlobalException("FX Order limit setup is not defined",JaxError.FC_SALE_DAY_LIMIT_SETUP_NOT_DIFINED);
		}
		
	}
	
	

}
