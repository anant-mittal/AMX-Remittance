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
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.FcSaleExchangeRateDao;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.PurposeOfTransaction;
import com.amx.jax.dbmodel.SourceOfIncomeView;
import com.amx.jax.dbmodel.fx.FxExchangeRateView;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.FcSaleAddressManager;
import com.amx.jax.manager.FcSaleApplicationTransactionManager;
import com.amx.jax.manager.FcSaleOrderTransactionManager;
import com.amx.jax.manager.FxOrderPaymentManager;
import com.amx.jax.manager.FxOrderReportManager;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.request.fx.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.request.fx.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.model.response.fx.AddressTypeDto;
import com.amx.jax.model.response.fx.CurrencyDenominationTypeDto;
import com.amx.jax.model.response.fx.FcSaleApplPaymentReponseModel;
import com.amx.jax.model.response.fx.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.fx.FcSaleOrderDefaultResponseModel;
import com.amx.jax.model.response.fx.FxExchangeRateDto;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.FxOrderShoppingCartResponseModel;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.model.response.fx.FxOrderTransactionStatusResponseDto;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.model.response.fx.TimeSlotDto;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.IPurposeOfTrnxDao;
import com.amx.jax.repository.ISourceOfIncomeDao;
import com.amx.jax.repository.ITermsAndConditionRepository;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.FxOrderValidation;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class FcSaleService extends AbstractService {

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

	@Autowired
	FxOrderPaymentManager paymentManager;
	
	@Autowired
	FxOrderReportManager reportManager;
	


	/**
	 * @return :to get the fc sale purpose of Trnx
	 */
	public AmxApiResponse<PurposeOfTransactionDto, Object> getPurposeofTrnxList() {
		List<PurposeOfTransaction> purposeofTrnxList = purposetrnxDao.getPurposeOfTrnx();
		if (purposeofTrnxList.isEmpty()) {
			throw new GlobalException("No data found", JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(convertPurposeOfTrnxDto(purposeofTrnxList));
	}

	/**
	 * 
	 * @return : country Id
	 */
	public AmxApiResponse<CurrencyMasterDTO, Object> getFcSalecurrencyList(BigDecimal countryId) {
		validation.fcsalecurrencyList(countryId);
		validation.validateHeaderInfo();
		List<CurrencyMasterModel> currencyList = currencyDao.getfcCurrencyList(countryId);
		if (currencyList.isEmpty()) {
			throw new GlobalException("No data found", JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(convertToModelDto(currencyList));
	}

	/**
	 * 
	 * @param applicationCountryId
	 * @param countryBranchId
	 * @param fxCurrencyId
	 * @return
	 */
	public AmxApiResponse<FxExchangeRateDto, Object> getFcSaleExchangeRate(BigDecimal applicationCountryId,
			BigDecimal countryBranchId,
			BigDecimal fxCurrencyId) {
		validation.fcSaleExchangeRate(applicationCountryId, countryBranchId, fxCurrencyId);
		validation.validateHeaderInfo();
		List<FxExchangeRateView> fxSaleRateList = fcSaleExchangeRateDao.getFcSaleExchangeRate(applicationCountryId,
				countryBranchId, fxCurrencyId);
		if (fxSaleRateList.isEmpty()) {
			throw new GlobalException("No data found", JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(convertExchangeRateModelToDto(fxSaleRateList));
	}

	/** Calculate fc and lc amount */
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getFCSaleLcAndFcAmount(
			BigDecimal applicationCountryId, BigDecimal countryBranchId, BigDecimal fxCurrencyId, BigDecimal fcAmount) {
		validation.validateHeaderInfo();
		FcSaleOrderApplicationResponseModel responseModel = trnxManager.calculateTrnxRate(applicationCountryId,
				countryBranchId, fxCurrencyId, fcAmount);
		return AmxApiResponse.build(responseModel);
	}

	/**
	 * 
	 * Puspose : FC Sale default API to display 1. Source of Income 2. Purpose of
	 * Transaction 3. Fc Currency list 4. Denomination type
	 */

	public AmxApiResponse<FcSaleOrderDefaultResponseModel, Object> getDefaultFsSale(BigDecimal applicationCountryId,
			BigDecimal countryBranchId, BigDecimal languageId) {
		validation.validateHeaderInfo();
		FcSaleOrderDefaultResponseModel responseModel = new FcSaleOrderDefaultResponseModel();
		List<PurposeOfTransaction> purposeofTrnxList = purposetrnxDao.getPurposeOfTrnx();
		List<CurrencyMasterModel> currencyList = currencyDao.getfcCurrencyList(applicationCountryId);
		List<ParameterDetails> denominationTypeList = fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_CD,
				ConstantDocument.Yes);
		List<SourceOfIncomeView> sourceOfIncomeList = sourceOfIncomeDao.getSourceofIncome(languageId);

		if (purposeofTrnxList != null && !purposeofTrnxList.isEmpty()) {
			responseModel.setPurposeOfTrnxList(convertPurposeOfTrnxDto(purposeofTrnxList));
		}

		if (currencyList != null && !currencyList.isEmpty()) {
			responseModel.setFcCurrencyList(convertToModelDto(currencyList));
		}

		if (denominationTypeList != null && !denominationTypeList.isEmpty()) {
			responseModel.setCurrDenotype(convertCurrDenoType(denominationTypeList));
		}

		if (sourceOfIncomeList != null && !sourceOfIncomeList.isEmpty()) {
			responseModel.setSourcOfIncomeList(convertSourceOfIncome(sourceOfIncomeList));
		}

		return AmxApiResponse.build(responseModel);
	}

	/**
	 * To save Application
	 */

	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> saveApplication(
			FcSaleOrderTransactionRequestModel fcSalerequestModel) {
		validation.validateHeaderInfo();
		FcSaleOrderApplicationResponseModel fcSaleAppResponseModel = applTrnxManager
				.saveApplication(fcSalerequestModel);
		return AmxApiResponse.build(fcSaleAppResponseModel);
	}

	/**
	 * Fetch address for Fc Sale
	 */

	public AmxApiResponse<ShippingAddressDto, Object> fetchFcSaleAddress() {
		validation.validateHeaderInfo();
		ShippingAddressDto dto = new ShippingAddressDto();
		List<ShippingAddressDto> shippingAddressList = fcSaleAddresManager.fetchShippingAddress();
		if (shippingAddressList.isEmpty()) {
			throw new GlobalException("No data found", JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(shippingAddressList);
	}

	/**
	 * Save shipping address
	 */

	public AmxApiResponse<CustomerShippingAddressRequestModel, Object> saveShippingAddress(
			CustomerShippingAddressRequestModel requestModel) {
		validation.validateHeaderInfo();
		fcSaleAddresManager.saveShippingAddress(requestModel);
		return AmxApiResponse.build(requestModel);
	}

	/**
	 * 
	 * @param : to get the time slot for fx order
	 * @return
	 */
	public AmxApiResponse<TimeSlotDto, Object> fetchTimeSlot(BigDecimal shippingAddressId) {
		validation.validateHeaderInfo();
		List<TimeSlotDto> timeSlotList = applTrnxManager.fetchTimeSlot(shippingAddressId);
		if (timeSlotList.isEmpty()) {
			throw new GlobalException("No data found", JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(timeSlotList);
	}

	/**
	 * 
	 * @param :Remove item from cart
	 * @returnFcSaleOrderApplicationResponseModel
	 */

	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeitemFromCart(BigDecimal applicationId) {
		validation.validateHeaderInfo();
		FcSaleOrderApplicationResponseModel fcSaleAppResponseModel = applTrnxManager.removeitemFromCart(applicationId);
		return AmxApiResponse.build(fcSaleAppResponseModel);

	}

	public AmxApiResponse<FxOrderShoppingCartResponseModel, Object> fetchShoppingCartList() {
		validation.validateHeaderInfo();
		FxOrderShoppingCartResponseModel shoppingCartDetails = applTrnxManager.fetchApplicationDetails();
		if (shoppingCartDetails==null) {
			throw new GlobalException("No data found", JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.build(shoppingCartDetails);
	}

	/** Pay now save **/

	public AmxApiResponse<FcSaleApplPaymentReponseModel, Object> saveApplicationPayment(
			FcSaleOrderPaynowRequestModel requestmodel) {
		validation.validateHeaderInfo();
		if (requestmodel.getCartDetailList().isEmpty()) {
			throw new GlobalException("Mandatory field is missing", JaxError.NULL_APPLICATION_ID);
		}
		FcSaleApplPaymentReponseModel responseModel = applTrnxManager.saveApplicationPayment(requestmodel);
		return AmxApiResponse.build(responseModel);
	}
	
	
	/** To save Knet details **/
	public AmxApiResponse<PaymentResponseDto,Object> savePaymentId(PaymentResponseDto paymentRequestDto){
		//validation.validateHeaderInfo();
		PaymentResponseDto paymentResponseDto =paymentManager.paymentCapture(paymentRequestDto); 
		return AmxApiResponse.build(paymentResponseDto);
	}
	
	
	public AmxApiResponse<FxOrderTransactionHistroyDto,Object> getFxOrderTransactionHistroy(){
		validation.validateHeaderInfo();
		List<FxOrderTransactionHistroyDto> listDto = applTrnxManager.getFxOrderTransactionHistroy();
		return AmxApiResponse.buildList(listDto);
	}
	
	
	public AmxApiResponse<FxOrderReportResponseDto, Object> getFxOrderTransactionReport(BigDecimal collNo,BigDecimal collFyr){
		FxOrderReportResponseDto reportResponseDto = reportManager.getReportDetails(collNo, collFyr);
		return AmxApiResponse.build(reportResponseDto);
	}

	
	public AmxApiResponse<FxOrderTransactionStatusResponseDto, Object> getFxOrderTransactionStatus(BigDecimal documentIdForPayment){
		FxOrderTransactionStatusResponseDto statusdto = reportManager.getTransactionStatus(documentIdForPayment);
		return AmxApiResponse.build(statusdto);
	}
	
	
	public AmxApiResponse<AddressTypeDto,Object> getAddressTypeList(){
		return AmxApiResponse.buildList(applTrnxManager.getAddressTypeList());
	}
	
	
	public AmxApiResponse<ShippingAddressDto, Object> deleteFcSaleAddress(BigDecimal addressId) {
		validation.validateHeaderInfo();
		ShippingAddressDto dto = new ShippingAddressDto();
		List<ShippingAddressDto> shippingAddressList = fcSaleAddresManager.deleteShippingAddress(addressId);
		if (shippingAddressList.isEmpty()) {
			throw new GlobalException("No data found", JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(shippingAddressList);
	}

	
	 public AmxApiResponse<ShippingAddressDto, Object> editShippingaddress(ShippingAddressDto dto){
		 validation.validateHeaderInfo();
		 List<ShippingAddressDto> shippingAddressList = fcSaleAddresManager.editShippingAddress(dto);
		 if (shippingAddressList.isEmpty()) {
				throw new GlobalException("No data found", JaxError.NO_RECORD_FOUND);
			}
			return AmxApiResponse.buildList(shippingAddressList);
	 }
	
	
	public List<PurposeOfTransactionDto> convertPurposeOfTrnxDto(List<PurposeOfTransaction> purposeofTrnxList) {
		List<PurposeOfTransactionDto> dtoList = new ArrayList<>();
		for (PurposeOfTransaction purofTrnx : purposeofTrnxList) {
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

	private List<FxExchangeRateDto> convertExchangeRateModelToDto(List<FxExchangeRateView> fxSaleRateList) {
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

	public List<CurrencyDenominationTypeDto> convertCurrDenoType(List<ParameterDetails> denominationTypeList) {
		List<CurrencyDenominationTypeDto> dto = new ArrayList<>();
		for (ParameterDetails pdetails : denominationTypeList) {
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
