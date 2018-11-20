package com.amx.jax.services;

/**
 * @author rabil
 * @Date : 03/11/2018
 *
 */

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.TermsAndConditionDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.FcSaleExchangeRateDao;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.FxExchangeRateView;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.PurposeOfTransaction;
import com.amx.jax.dbmodel.SourceOfIncomeView;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.FcSaleAddressManager;
import com.amx.jax.manager.FcSaleApplicationTransactionManager;
import com.amx.jax.manager.FcSaleOrderTransactionManager;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.request.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.request.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.CurrencyDenominationTypeDto;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.FcSaleApplPaymentReponseModel;
import com.amx.jax.model.response.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.FcSaleOrderDefaultResponseModel;
import com.amx.jax.model.response.FxExchangeRateDto;
import com.amx.jax.model.response.PurposeOfTransactionDto;
import com.amx.jax.model.response.ShippingAddressDto;
import com.amx.jax.model.response.ShoppingCartDetailsDto;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.IPurposeOfTrnxDao;
import com.amx.jax.repository.ISourceOfIncomeDao;
import com.amx.jax.repository.ITermsAndConditionRepository;
import com.amx.jax.validation.FxOrderValidation;




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
	
	@Autowired
	FcSaleOrderTransactionManager trnxManager;
	
	@Autowired
	ISourceOfIncomeDao sourceOfIncomeDao;
	
	@Autowired
	FcSaleApplicationTransactionManager applTrnxManager;
	
	@Autowired
	FcSaleAddressManager fcSaleAddresManager;
	
	@Autowired
	FxOrderValidation validation;
	
	@Autowired
	ITermsAndConditionRepository termsAndCondition;
	

	
	
	
	/**
	 * @return :to get the fc sale purpose of Trnx
	 */
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
		validation.fcsalecurrencyList(countryId);
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
		validation.fcSaleExchangeRate(applicationCountryId,countryBranchId,fxCurrencyId);
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
	
	
	/** Calculate fc and lc amount */
	public ApiResponse getFCSaleLcAndFcAmount(BigDecimal applicationCountryId,BigDecimal countryBranchId,BigDecimal fxCurrencyId,BigDecimal fcAmount){
		ApiResponse response = getBlackApiResponse();
		validation.validateHeaderInfo();
		FcSaleOrderApplicationResponseModel responseModel =trnxManager.calculateTrnxRate(applicationCountryId, countryBranchId, fxCurrencyId, fcAmount); 
		response.getData().getValues().add(responseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(responseModel.getModelType());
		return response;
	}
	
	
	
	

	
	/**

	 * Puspose : FC Sale default API to display 
	 * 		1. Source of Income
	 * 		2. Purpose of Transaction
	 * 		3. Fc Currency list
	 * 		4. Denomination type  
	 */
	
	public ApiResponse getDefaultFsSale(BigDecimal applicationCountryId,BigDecimal countryBranchId,BigDecimal languageId){
		ApiResponse response = getBlackApiResponse();
		validation.validateHeaderInfo();
		FcSaleOrderDefaultResponseModel responseModel = new FcSaleOrderDefaultResponseModel(); 
		List<PurposeOfTransaction> purposeofTrnxList = purposetrnxDao.getPurposeOfTrnx();
		List<CurrencyMasterModel> currencyList = currencyDao.getfcCurrencyList(applicationCountryId);
		List<ParameterDetails> denominationTypeList = fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_CD, ConstantDocument.Yes);
		List<SourceOfIncomeView> sourceOfIncomeList = sourceOfIncomeDao.getSourceofIncome(languageId);
		
		
		if(purposeofTrnxList!= null && !purposeofTrnxList.isEmpty()){
			responseModel.setPurposeOfTrnxList(convertPurposeOfTrnxDto(purposeofTrnxList));
		}
		
		if(currencyList!= null && !currencyList.isEmpty()){
			responseModel.setFcCurrencyList(convertToModelDto(currencyList));
		}
		
		if(denominationTypeList!=null && !denominationTypeList.isEmpty()){
			responseModel.setCurrDenotype(convertCurrDenoType(denominationTypeList));
		}
		
		if(sourceOfIncomeList!= null && !sourceOfIncomeList.isEmpty()){
			responseModel.setSourcOfIncomeList(convertSourceOfIncome(sourceOfIncomeList));
		}
		
		response.getData().getValues().add(responseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(responseModel.getModelType());
		return response;
	}
	
	
	
	/**
	 * To save Application
	 */
	
	public ApiResponse saveApplication(FcSaleOrderTransactionRequestModel fcSalerequestModel){
		validation.validateHeaderInfo();
		ApiResponse response = getBlackApiResponse();
		FcSaleOrderApplicationResponseModel fcSaleAppResponseModel =  applTrnxManager.saveApplication(fcSalerequestModel);
		response.getData().getValues().add(fcSaleAppResponseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(fcSaleAppResponseModel.getModelType());
		return response;
	}
	
	
	
	/**
	 * Fetch address for Fc Sale
	 */
	
	public ApiResponse fetchFcSaleAddress(){
		validation.validateHeaderInfo();
		ApiResponse response = getBlackApiResponse();
		ShippingAddressDto dto = new ShippingAddressDto();
		List<ShippingAddressDto> shippingAddressList = fcSaleAddresManager.fetchShippingAddress();
		response.getData().getValues().add(shippingAddressList);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(dto.getModelType());
		return response;
	}
	
	
	/**
	 * Save shipping address
	 */
	
	public ApiResponse saveShippingAddress(CustomerShippingAddressRequestModel requestModel){
		validation.validateHeaderInfo();
		ApiResponse response = getBlackApiResponse();
		fcSaleAddresManager.saveShippingAddress(requestModel);
		response.getData().getValues().add(requestModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(requestModel.getModelType());
		return response;
	}
	
	

	
	/**
	 * 
	 * @param : to get the time slot for fx order
	 * @return
	 */
	
	
	public AmxApiResponse fetchTimeSlot(String date){
		List<String> timeSlotList = applTrnxManager.fetchTimeSlot(date); 
		if(timeSlotList.isEmpty()){
			throw new GlobalException("No data found",JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(timeSlotList);
	}
	
	/**
	 * 
	 * @param   :Remove item from cart
	 * @returnFcSaleOrderApplicationResponseModel
	 */
	
	public AmxApiResponse removeitemFromCart(BigDecimal applicationId){	
		if(applicationId==null || applicationId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application id should not be blank",JaxError.NULL_APPLICATION_ID);
		}
		FcSaleOrderApplicationResponseModel fcSaleAppResponseModel =  applTrnxManager.removeitemFromCart(applicationId);
		return AmxApiResponse.build(fcSaleAppResponseModel);
		
	}
	
	

	public AmxApiResponse fetchShoppingCartList(){
		List<ShoppingCartDetailsDto> shoppingCartDetails =  applTrnxManager.fetchApplicationDetails();
		return AmxApiResponse.buildList(shoppingCartDetails);
		}

	
	
	/** Pay now save **/
	
	public AmxApiResponse saveApplicationPayment(FcSaleOrderPaynowRequestModel requestmodel){
		if(requestmodel.getCartDetailList().isEmpty()){
			throw new GlobalException("Mandatory field is missing",JaxError.NULL_APPLICATION_ID);
		}
		FcSaleApplPaymentReponseModel responseModel = applTrnxManager.saveApplicationPayment(requestmodel);
		return AmxApiResponse.build(responseModel);
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
	
	public List<CurrencyDenominationTypeDto> convertCurrDenoType(List<ParameterDetails> denominationTypeList){
		List<CurrencyDenominationTypeDto> dto = new ArrayList<>();
		for(ParameterDetails pdetails: denominationTypeList){
			CurrencyDenominationTypeDto currDto = new CurrencyDenominationTypeDto();
			currDto.setCurrencyDenominationDesc(pdetails.getCharField1());
			dto.add(currDto);
		}
		
		return dto;
	}
	public List<SourceOfIncomeDto> convertSourceOfIncome(List<SourceOfIncomeView> sourceOfIncomeList) {
		List<SourceOfIncomeDto> list = new ArrayList<>();
		for (SourceOfIncomeView model : sourceOfIncomeList) {
			SourceOfIncomeDto dto = new SourceOfIncomeDto();
			dto.setSourceofIncomeId(model.getSourceofIncomeId());
			dto.setShortDesc(model.getShortDesc());
			dto.setLanguageId(model.getLanguageId());
			dto.setDescription(model.getDescription());
			list.add(dto);
		}
		return list;

	}
	

}
		
		

