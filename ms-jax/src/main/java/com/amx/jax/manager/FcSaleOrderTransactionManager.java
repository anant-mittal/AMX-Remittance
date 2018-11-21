package com.amx.jax.manager;

/**
 * @Author : Rabil
 * @Date		: 05/11/2018
 */
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.FcSaleExchangeRateDao;
import com.amx.jax.dbmodel.FxExchangeRateView;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.FxExchangeRateBreakup;
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

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5831221861217249594L;

	public FcSaleOrderApplicationResponseModel calculateTrnxRate(BigDecimal countryId,BigDecimal countryBracnhId ,BigDecimal fcCurrencyId,BigDecimal fcAmount){
		BigDecimal maxExchangeRate = BigDecimal.ZERO;
		logger.info("calculateTrnxRate fc currencyId :"+fcCurrencyId+"\t fcAmount :"+fcAmount+"\t countryId :"+countryId+"\t countryBracnhId :"+countryBracnhId);
		FcSaleOrderApplicationResponseModel responseModel = new FcSaleOrderApplicationResponseModel();
		FxExchangeRateBreakup breakup = new FxExchangeRateBreakup();
		List<FxExchangeRateView> fxSaleRateList = fcSaleExchangeRateDao.getFcSaleExchangeRate(countryId, countryBracnhId, fcCurrencyId);
		List<ParameterDetails> parameterList 	= fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes);
		BigDecimal localCurrencyId = meta.getDefaultCurrencyId();
		breakup.setFcDecimalNumber(currencyMasterService.getCurrencyMasterById(fcCurrencyId).getDecinalNumber());
		breakup.setLcDecimalNumber(currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber());
		
		if(fxSaleRateList!= null && !fxSaleRateList .isEmpty()){
			maxExchangeRate = fxSaleRateList.get(0).getSalMaxRate();
		}
		logger.info(" maxExchangeRate  :"+maxExchangeRate +"\t : for Currency  :"+fcCurrencyId+"\t Fc amount :"+fcAmount);
		
		if(parameterList != null && !parameterList.isEmpty()){
			responseModel.setTxnFee(RoundUtil.roundBigDecimal(parameterList.get(0).getNumericField1()==null?BigDecimal.ZERO:parameterList.get(0).getNumericField1(),breakup.getLcDecimalNumber().intValue()));
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
		responseModel.setExRateBreakup(breakup);
		
		logger.info("breakup rate maxExchangeRate  :"+breakup.getRate() +"\t : for Currency  :"+fcCurrencyId+"\t Fc amount :"+breakup.getConvertedFCAmount()+"\t LC amount :"+breakup.getConvertedLCAmount());
	
		return responseModel; 
	}
	
	
	
	
	

}
