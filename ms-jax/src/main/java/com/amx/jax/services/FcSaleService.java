package com.amx.jax.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;







import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.FxExchangeRateDto;
import com.amx.amxlib.model.PurposeOfTransactionDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dao.FcSaleExchangeRateDao;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.FxExchangeRateView;
import com.amx.jax.dbmodel.PurposeOfTransaction;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.IPurposeOfTrnxDao;

import org.apache.commons.beanutils.BeanUtils;
/**
 * 
 * @author rabil
 * Date : 03/11/2018
 *
 */



@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class FcSaleService extends AbstractService{
	
	Logger logger = LoggerFactory.getLogger(FcSaleService.class);

	@Autowired
	IPurposeOfTrnxDao purposetrnxDao;
	
	@Autowired
	ICurrencyDao currencyDao;

	@Autowired
	FcSaleExchangeRateDao fcSaleExchangeRateDao;
	
	public ApiResponse getPurposeofTrnxList(){
		ApiResponse response = getBlackApiResponse();
		List<PurposeOfTransaction> purposeofTrnxList = purposetrnxDao.getPurposeOfTrnx();
		if(purposeofTrnxList.isEmpty()){
			throw new GlobalException("No data found",JaxError.NO_RECORD_FOUND);
		}else {
			response.getData().getValues().addAll(convertPurposeOfTrnxDto(purposeofTrnxList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("fx-purpose-of-trnx");
		return response;
	}

	
	/**
	 * 
	 * @return : country Id
	 */
	public ApiResponse getFcSalecurrencyList(BigDecimal countryId){
		ApiResponse response = getBlackApiResponse();
		List<CurrencyMasterModel> currencyList = currencyDao.getfcCurrencyList(countryId);
		if(currencyList.isEmpty()){
			throw new GlobalException("No data found",JaxError.NO_RECORD_FOUND);
		}else {
			response.getData().getValues().addAll(convertToModelDto(currencyList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("currencyMaster");
		return response;
	}
	
	
	/**
	 * 
	 * @param country Id,country branch id ,currency id
	 * @return
	 */
	
	public ApiResponse getFcSaleExchangeRate(BigDecimal applicationCountryId,BigDecimal countryBranchId,BigDecimal fxCurrencyId){
		ApiResponse response = getBlackApiResponse();
		List<FxExchangeRateView> fxSaleRateList = fcSaleExchangeRateDao.getFcSaleExchangeRate(applicationCountryId, countryBranchId, fxCurrencyId);
		if(fxSaleRateList.isEmpty()){
			throw new GlobalException("No data found",JaxError.NO_RECORD_FOUND);
		}else{
			response.getData().getValues().addAll(convertExchangeRateModelToDto(fxSaleRateList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("fc_sale_xrate");
		return response;
	}
	
			
	public List<PurposeOfTransactionDto> convertPurposeOfTrnxDto(List<PurposeOfTransaction> purposeofTrnxList){
		List<PurposeOfTransactionDto> dtoList = new ArrayList<>();
		for(PurposeOfTransaction purofTrnx : purposeofTrnxList){
			PurposeOfTransactionDto dto = new PurposeOfTransactionDto();
			dto.setPurposeId(purofTrnx.getPurposeId());
			dto.setPurposeShortDesc(purofTrnx.getPurposeShortDesc());
			dto.setPurposeFullDesc(purofTrnx.getPurposeFullDesc());
			dto.setCompanyId(purofTrnx.getFsCompanyMaster().getCompanyId());
			dto.setCountryId(purofTrnx.getFsCountryMaster().getCountryId());
			dto.setIsActive(purofTrnx.getIsActive());
			dtoList.add(dto);
		}
		return dtoList;
	}
		
	
	
	private List<CurrencyMasterDTO> convertToModelDto(List<CurrencyMasterModel> currencyList) {
		List<CurrencyMasterDTO> output = new ArrayList<>();
		currencyList.forEach(currency -> output.add(convertModel(currency)));
		return output;
	}
	
	public CurrencyMasterDTO convertModel(CurrencyMasterModel currency) {
		CurrencyMasterDTO dto = new CurrencyMasterDTO();
		try {
			BeanUtils.copyProperties(dto, currency);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("convertModel  to convert currency", e);
		}
		return dto;
	}

	
	private List<FxExchangeRateDto> convertExchangeRateModelToDto(List<FxExchangeRateView> fxSaleRateList ){
		List<FxExchangeRateDto> output = new ArrayList<>();
		fxSaleRateList.forEach(exchnage -> output.add(convertExchangeModel(exchnage)));
		return output;
	}
	
	
	public FxExchangeRateDto convertExchangeModel(FxExchangeRateView exchnage) {
		FxExchangeRateDto dto = new FxExchangeRateDto();
		try {
			BeanUtils.copyProperties(dto, exchnage);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("convertExchangeModel  to convert currency", e);
		}
		return dto;
	}

}
		
		

