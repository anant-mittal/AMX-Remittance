package com.amx.jax.manager;
/**
 * @author rabil
 */

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

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
import com.amx.jax.dbmodel.FxShoppingCartDetails;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxDeliveryTimeSlotMaster;
import com.amx.jax.dbmodel.fx.FxExchangeRateView;
import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.fx.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.request.fx.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.fx.AddressTypeDto;
import com.amx.jax.model.response.fx.FcSaleApplPaymentReponseModel;
import com.amx.jax.model.response.fx.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.fx.FxApplicationDto;
import com.amx.jax.model.response.fx.FxExchangeRateBreakup;
import com.amx.jax.model.response.fx.FxOrderShoppingCartResponseModel;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.model.response.fx.ShoppingCartDetailsDto;
import com.amx.jax.model.response.fx.TimeSlotDto;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IShippingAddressRepository;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.repository.fx.FxDeliveryDetailsRepository;
import com.amx.jax.repository.fx.FxOrderDeliveryTimeSlotRepository;
import com.amx.jax.repository.fx.FxOrderTransactionRespository;
import com.amx.jax.repository.fx.FxOrderTranxLimitRespository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.jax.validation.FxOrderValidation;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FcSaleApplicationTransactionManager extends AbstractModel {

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

	@Autowired
	ParameterDetailsRespository paramRepos;

	@Autowired
	IShippingAddressRepository shippingAddressDao;

	@Autowired
	AuthenticationLimitCheckDAO authentication;

	@Autowired
	FxOrderTranxLimitRespository trnxLimitRepos;

	@Autowired
	FcSaleAddressManager addressManager;

	@Autowired
	FxDeliveryDetailsRepository deliveryDetailsRepos;

	@Autowired
	FxOrderReportManager reportManager;

	@Autowired
	FxOrderPaymentManager orderPayMang;

	@Autowired
	FcSaleOrderTransactionManager trnxManager;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4740757344377296626L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/** save application **/
	public FcSaleOrderApplicationResponseModel saveApplication(FcSaleOrderTransactionRequestModel fcSalerequestModel) {
		try {
			FcSaleOrderApplicationResponseModel responeModel = new FcSaleOrderApplicationResponseModel();
			HashMap<String, Object> mapAllDetailApplSave = new HashMap<String, Object>();
			deactivateApplications(fcSalerequestModel);
			trnxManager.checkMinDenomination(fcSalerequestModel.getForeignCurrencyId(),fcSalerequestModel.getForeignAmount());
			ReceiptPaymentApp receiptPayment = this.createFcSaleReceiptApplication(fcSalerequestModel);
			mapAllDetailApplSave.put("EX_APPL_RECEIPT", receiptPayment);
			fsSaleapplicationDao.saveAllApplicationData(mapAllDetailApplSave);
			FxOrderShoppingCartResponseModel cartDetails = fetchApplicationDetails();
			fetchCustomerAddressDetails();
			List<TimeSlotDto> fxOrderTimeSlot = fetchTimeSlot(null);
			responeModel.setTimeSlot(fxOrderTimeSlot);
			responeModel.setCartDetails(cartDetails.getShoppingCartList());
			responeModel.setDeliveryCharges(getDeliveryChargesFromParameter());
			return responeModel;
		} catch (GlobalException e) {
			logger.error("createFcSaleReceiptApplication", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("saveApplication", e.getMessage());
			throw new GlobalException("FC Sale application creation failed");
		}
	}

	/** Save application payment **/
	public FcSaleApplPaymentReponseModel saveApplicationPayment(FcSaleOrderPaynowRequestModel requestmodel) {
		FcSaleApplPaymentReponseModel responeModel = new FcSaleApplPaymentReponseModel();
		PaygDetailsModel pgDetails = createPgDetails(requestmodel);
		FxDeliveryDetailsModel deliveryDetailsModel = createDeliveryDeetails(requestmodel);
		List<ParameterDetails> parameterList = fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC,
				ConstantDocument.Yes);
		HashMap<String, Object> mapAllDetailApplSave = new HashMap<String, Object>();
		mapAllDetailApplSave.put("EX_PAYG_DETAILS", pgDetails);
		mapAllDetailApplSave.put("EX_DELIVERY_DETAILS", deliveryDetailsModel);
		mapAllDetailApplSave.put("requestmodel", requestmodel);
		fsSaleapplicationDao.saveAllAppDetails(mapAllDetailApplSave);
		BigDecimal knetamount = BigDecimal.ZERO;
		List<FxApplicationDto> listShoppingCart = requestmodel.getCartDetailList();
		if (listShoppingCart != null && !listShoppingCart.isEmpty()) {
			for (FxApplicationDto shoppingCart : listShoppingCart) {
				logger.debug("FC Sale application Id : " + shoppingCart.getApplicationId());
				ReceiptPaymentApp rcptAppl = rcptPaymentAppl.findOne(shoppingCart.getApplicationId());
				if (rcptAppl != null && rcptAppl.getIsActive().equalsIgnoreCase(ConstantDocument.Yes)
						&& rcptAppl.getApplicationStatus().equalsIgnoreCase(ConstantDocument.S)) {
					knetamount = knetamount.add(rcptAppl.getLocalTrnxAmount());
					responeModel.setRemittanceAppId(rcptAppl.getReceiptId());
					responeModel.setMerchantTrackId(metaData.getCustomerId());
					responeModel.setDocumentIdForPayment(
							rcptAppl.getPgPaymentSeqDtlId() == null ? "" : rcptAppl.getPgPaymentSeqDtlId().toString());
					UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
					if (userFinancialYear != null) {
						responeModel.setDocumentFinancialYear(userFinancialYear.getFinancialYear());
					}
				} else {
					throw new GlobalException(JaxError.FC_SALE_APPLIATION_NOT_FOUND, "No record found for payment");
				}
			} // end of for Loop.
		} else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No record found for payment");
		}

		logger.debug("Total knet amount without delivery charges: " + knetamount);
		if (parameterList != null && !parameterList.isEmpty()) {
			logger.debug("Delivery charges: " + parameterList.get(0).getNumericField1());
			knetamount = knetamount.add(parameterList.get(0).getNumericField1());
		} else {
			logger.debug("Delivery charges: " + parameterList.get(0).getNumericField1());
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_CHARGES_NOT_FOUND, "Fc Delivery not defined");
		}
		if (JaxUtil.isNullZeroBigDecimalCheck(knetamount) && knetamount.compareTo(BigDecimal.ZERO) > 0) {
			responeModel.setNetPayableAmount(knetamount);
		} else {
			throw new GlobalException(JaxError.INVALID_AMOUNT, "Toal Knet Amount ");
		}

		logger.debug("Total knet amount with delivery charges: " + knetamount);
		return responeModel;
	}

	public ReceiptPaymentApp createFcSaleReceiptApplication(FcSaleOrderTransactionRequestModel fcSalerequestModel) {
		ReceiptPaymentApp receiptPaymentAppl = new ReceiptPaymentApp();
		try {
			BigDecimal locCode = BigDecimal.ZERO;
			BigDecimal applciationCountryid = metaData.getCountryId();
			BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
			BigDecimal companyId = metaData.getCompanyId();
			BigDecimal countryBranchId = metaData.getCountryBranchId();
			ApplicationSetup appl = applCountryRepos.getApplicationSetupDetails();
			BigDecimal fcTrnxLimitPerDay = BigDecimal.ZERO;
			BigDecimal fxTrnxHistAmount = BigDecimal.ZERO;
			if (appl != null) {
				applciationCountryid = appl.getApplicationCountryId();
				companyId = appl.getCompanyId();
			}
			if (!currencyDao.getCurrencyListByCountryId(applciationCountryid).isEmpty()) {
				localCurrencyId = currencyDao.getCurrencyListByCountryId(applciationCountryid).get(0).getCurrencyId();
			}
			BigDecimal customerId = metaData.getCustomerId();
			String customerName = new String();
			receiptPaymentAppl.setCompanyId(companyId);
			receiptPaymentAppl.setCountryId(applciationCountryid);
			receiptPaymentAppl.setCustomerId(customerId);

			List<Customer> customerList = customerDao.getCustomerByCustomerId(applciationCountryid, companyId,
					customerId);
			if (customerList != null && !customerList.isEmpty()) {
				if (customerList.get(0).getFirstName() != null) {
					customerName = customerList.get(0).getFirstName();
				}
				if (!StringUtils.isEmpty(customerList.get(0).getMiddleName())) {
					customerName = customerName + " " + customerList.get(0).getMiddleName();
				}
				if (!StringUtils.isEmpty(customerList.get(0).getLastName())) {
					customerName = customerName + " " + customerList.get(0).getLastName();
				}

				receiptPaymentAppl.setCustomerName(customerName);
			} else {
				logger.error("Customer is not registered" + customerId);
				throw new GlobalException(JaxError.CUSTOMER_NOT_REGISTERED_ONLINE, "Customer is not registered");
			}

			CountryBranch countryBranch = countryBranchRepository
					.findByBranchId(ConstantDocument.ONLINE_BRANCH_LOC_CODE);
			if (countryBranch != null) {
				locCode = countryBranch.getBranchId();
				countryBranchId = countryBranch.getCountryBranchId();
			} else {
				throw new GlobalException(JaxError.INVALID_COUNTRY_BRANCH, "Country branch is not found");
			}
			validation.validateHeaderInfo();

			if (!JaxUtil.isNullZeroBigDecimalCheck(fcSalerequestModel.getForeignAmount())
					&& fcSalerequestModel.getForeignAmount().compareTo(BigDecimal.ZERO) < 0) {
				throw new GlobalException(JaxError.INVALID_EXCHANGE_AMOUNT, "Negative not allowed");
			}

			FxExchangeRateBreakup exchbreakUpRate = getExchangeRateFcSaleOrder(applciationCountryid, countryBranchId,
					fcSalerequestModel.getForeignCurrencyId(), fcSalerequestModel.getForeignAmount());

			receiptPaymentAppl.setLocalCurrencyId(localCurrencyId);
			receiptPaymentAppl.setForeignCurrencyId(fcSalerequestModel.getForeignCurrencyId());
			receiptPaymentAppl.setForignTrnxAmount(fcSalerequestModel.getForeignAmount());

			receiptPaymentAppl.setLocalTrnxAmount(exchbreakUpRate.getConvertedLCAmount() == null ? BigDecimal.ZERO
					: exchbreakUpRate.getConvertedLCAmount());
			receiptPaymentAppl.setLocalNetAmount(
					exchbreakUpRate.getNetAmount() == null ? BigDecimal.ZERO : exchbreakUpRate.getNetAmount());
			receiptPaymentAppl.setTransactionActualRate(
					exchbreakUpRate.getRate() == null ? BigDecimal.ZERO : exchbreakUpRate.getRate());
			receiptPaymentAppl.setBranchId(countryBranchId);
			UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
			if (userFinancialYear != null) {
				receiptPaymentAppl.setDocumentFinanceYear(userFinancialYear.getFinancialYear());
			}

			if (receiptPaymentAppl.getLocalTrnxAmount().compareTo(BigDecimal.ZERO) <= 0) {
				throw new GlobalException(JaxError.ZERO_NOT_ALLOWED, "Enter valid amount ");
			}

			trnxManager.checkFCSaleTrnxLimit(exchbreakUpRate, fcSalerequestModel.getForeignCurrencyId(),
					fcSalerequestModel.getForeignAmount(), localCurrencyId, customerId);
			receiptPaymentAppl.setDenominationType(fcSalerequestModel.getCurrencyDenominationType());
			receiptPaymentAppl.setTransactionType(ConstantDocument.S);
			receiptPaymentAppl.setIsActive(ConstantDocument.Yes);
			receiptPaymentAppl.setDocumentStatus(ConstantDocument.Yes);
			receiptPaymentAppl.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE_APPLICATION);

			BigDecimal documentNo = orderPayMang.generateDocumentNumber(countryBranch,
					receiptPaymentAppl.getCountryId(), receiptPaymentAppl.getCompanyId(), ConstantDocument.Update,
					receiptPaymentAppl.getDocumentFinanceYear(), ConstantDocument.DOCUMENT_CODE_FOR_FCSALE_APPLICATION);
			if (documentNo != null && documentNo.compareTo(BigDecimal.ZERO) != 0) {
				receiptPaymentAppl.setDocumentNo(documentNo);
			} else {
				throw new GlobalException(JaxError.INVALID_APPL_RECEIPT_PAYMNET_DOCUMENT_NO,
						"Receipt  document should not be blank.");
			}

			if (fcSalerequestModel.getSourceOfFundId() != null) {
				receiptPaymentAppl.setSourceofIncomeId(fcSalerequestModel.getSourceOfFundId());
				receiptPaymentAppl.setSourceOfIncomeid(fcSalerequestModel.getSourceOfFundId());
			}
			if (fcSalerequestModel.getPurposeOfTrnx() != null) {
				receiptPaymentAppl.setPurposeofTransactionId(fcSalerequestModel.getPurposeOfTrnx());
			}

			receiptPaymentAppl.setTransactionIPAddress(metaData.getDeviceIp());

			receiptPaymentAppl.setDocumentDate(new Date());
			receiptPaymentAppl.setGeneralLegerDate(new Date());
			receiptPaymentAppl.setCreatedDate(new Date());

			receiptPaymentAppl.setTravelCountryId(fcSalerequestModel.getTravelCountryId());
			receiptPaymentAppl.setTravelStartDate(fcSalerequestModel.getStartDate() == null ? new Date()
					: DateUtil.convertStringToDate(fcSalerequestModel.getStartDate()));
			receiptPaymentAppl.setTravelEndDate(fcSalerequestModel.getEndDate() == null ? new Date()
					: DateUtil.convertStringToDate(fcSalerequestModel.getEndDate()));

			if (!StringUtils.isBlank(metaData.getReferrer())) {
				receiptPaymentAppl.setCreatedBy(metaData.getReferrer());
			} else {
				if (!StringUtils.isBlank(metaData.getAppType())) {
					receiptPaymentAppl.setCreatedBy(metaData.getAppType());
				} else {
					receiptPaymentAppl.setCreatedBy("WEB");
				}
			}
			try {
				receiptPaymentAppl.setAccountMMYYYY(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
			} catch (ParseException e) {
				logger.error("Error in saving application", e);
			}

		} catch (GlobalException e) {
			logger.error("createFcSaleReceiptApplication", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		} catch (Exception e) {
			logger.error("createFcSaleReceiptApplication", e.getMessage());
		}

		return receiptPaymentAppl;
	}

	public FxExchangeRateBreakup getExchangeRateFcSaleOrder(BigDecimal countryId, BigDecimal countryBracnhId,
			BigDecimal fcCurrencyId, BigDecimal fcAmount) {
		FxExchangeRateBreakup breakup = new FxExchangeRateBreakup();

		try {

			BigDecimal maxExchangeRate = BigDecimal.ZERO;
			logger.debug("calculateTrnxRate fc currencyId :" + fcCurrencyId + "\t fcAmount :" + fcAmount
					+ "\t countryId :" + countryId + "\t countryBracnhId :" + countryBracnhId);
			FcSaleOrderApplicationResponseModel responseModel = new FcSaleOrderApplicationResponseModel();

			List<FxExchangeRateView> fxSaleRateList = fcSaleExchangeRateDao.getFcSaleExchangeRate(countryId,
					countryBracnhId, fcCurrencyId);
			List<ParameterDetails> parameterList = fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC,
					ConstantDocument.Yes);
			BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
			breakup.setFcDecimalNumber(currencyMasterService.getCurrencyMasterById(fcCurrencyId).getDecinalNumber());
			breakup.setLcDecimalNumber(currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber());

			if (fxSaleRateList != null && !fxSaleRateList.isEmpty()) {
				maxExchangeRate = fxSaleRateList.get(0).getSalMaxRate();
			}
			logger.debug(" maxExchangeRate  :" + maxExchangeRate + "\t : for Currency  :" + fcCurrencyId
					+ "\t Fc amount :" + fcAmount);

			if (JaxUtil.isNullZeroBigDecimalCheck(fcAmount) && fcAmount.compareTo(BigDecimal.ZERO) < 0) {
				throw new GlobalException(JaxError.INVALID_EXCHANGE_AMOUNT, "Negative not allowed");
			}

			if (parameterList != null && !parameterList.isEmpty()) {
				breakup.setDeliveryCharges(
						RoundUtil.roundBigDecimal(
								parameterList.get(0).getNumericField1() == null ? BigDecimal.ZERO
										: parameterList.get(0).getNumericField1(),
								breakup.getLcDecimalNumber().intValue()));
			}
			if (JaxUtil.isNullZeroBigDecimalCheck(maxExchangeRate) && JaxUtil.isNullZeroBigDecimalCheck(fcAmount)) {
				breakup.setConvertedLCAmount(maxExchangeRate.multiply(fcAmount));
			}
			if (JaxUtil.isNullZeroBigDecimalCheck(fcAmount)) {
				breakup.setConvertedFCAmount(
						RoundUtil.roundBigDecimal(fcAmount, breakup.getFcDecimalNumber().intValue()));
			}
			if (JaxUtil.isNullZeroBigDecimalCheck(breakup.getConvertedLCAmount())) {
				breakup.setConvertedLCAmount(RoundUtil.roundBigDecimal(breakup.getConvertedLCAmount(),
						breakup.getLcDecimalNumber().intValue()));
			}
			breakup.setRate(maxExchangeRate);
			if (JaxUtil.isNullZeroBigDecimalCheck(breakup.getConvertedLCAmount())) {
				breakup.setNetAmount(breakup.getConvertedLCAmount());
			}

			return breakup;
		} catch (GlobalException e) {
			logger.error("createFcSaleReceiptApplication", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getExchangeRateFcSaleOrder", e.getMessage());
			throw new GlobalException(JaxError.FS_APPLIATION_CREATION_FAILED, "FC Sale application exchange");
		}
	}

	public void deactivateApplications(FcSaleOrderTransactionRequestModel fcSalerequestModel) {
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal foreignCurrencyId = fcSalerequestModel.getForeignCurrencyId();
		fsSaleapplicationDao.deActiveUnUsedapplication(customerId, foreignCurrencyId);
	}

	public FcSaleOrderApplicationResponseModel removeitemFromCart(BigDecimal applId) {
		FcSaleOrderApplicationResponseModel responeModel = new FcSaleOrderApplicationResponseModel();
		List<ShoppingCartDetailsDto> cartListDto = new ArrayList<>();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		try {
			fsSaleapplicationDao.removeItemFromCart(applId);
			List<FxShoppingCartDetails> shoppingCartList = fcSaleExchangeRateDao
					.getFcSaleShoppingCartDetails(applciationCountryid, companyId, customerId);
			if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
				cartListDto = convertShopingCartDto(shoppingCartList);
			}
			responeModel.setCartDetails(cartListDto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("removeitemFromCart ", e.getMessage() + "applId :" + applId);
		}
		return responeModel;

	}

	// FxOrderShoppingCartResponseModel
	public FxOrderShoppingCartResponseModel fetchApplicationDetails() {
		FxOrderShoppingCartResponseModel shoppingCartResponseModel = new FxOrderShoppingCartResponseModel();
		List<ShoppingCartDetailsDto> cartListDto = new ArrayList<>();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal deliveryCharges = BigDecimal.ZERO;
		BigDecimal totalNetAmount = BigDecimal.ZERO;
		List<FxShoppingCartDetails> shoppingCartList = fcSaleExchangeRateDao
				.getFcSaleShoppingCartDetails(applciationCountryid, companyId, customerId);
		List<ParameterDetails> parameterList = fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC,
				ConstantDocument.Yes);
		if (parameterList != null && !parameterList.isEmpty()) {
			deliveryCharges = parameterList.get(0).getNumericField1() == null ? BigDecimal.ZERO
					: parameterList.get(0).getNumericField1();
		}
		if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
			cartListDto = convertShopingCartDto(shoppingCartList);
			for (ShoppingCartDetailsDto dto : cartListDto) {
				totalNetAmount = totalNetAmount.add(dto.getLocalTranxAmount());
			}
		}
		shoppingCartResponseModel.setShoppingCartList(cartListDto);
		shoppingCartResponseModel.setDeliveryCharges(deliveryCharges);
		shoppingCartResponseModel.setTotalNetAmount(totalNetAmount.add(deliveryCharges));

		return shoppingCartResponseModel;
	}

	public void fetchCustomerAddressDetails() {
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applciationCountryid = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();

	}

	public List<AddressTypeDto> getAddressTypeList() {
		List<AddressTypeDto> list = new ArrayList<>();
		List<ParameterDetails> parameterListAddType = fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_AD,
				ConstantDocument.Yes);

		if (parameterListAddType != null && !parameterListAddType.isEmpty()) {
			for (ParameterDetails addParam : parameterListAddType) {
				AddressTypeDto dto = new AddressTypeDto();
				dto.setAddressTypeCode(addParam.getCharField1());
				dto.setAddressTypeDesc(addParam.getCharField2());
				list.add(dto);
			}
		} else {
			throw new GlobalException(JaxError.ADDRESS_TYPE_SETUP_IS_MISSING, "address type setup is not avaliable");
		}
		return list;
	}

	public BigDecimal generateDocumentNumber(CountryBranch countryBranch, String processInd, BigDecimal finYear) {
		BigDecimal appCountryId = metaData.getCountryId() == null ? BigDecimal.ZERO : metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId() == null ? BigDecimal.ZERO : metaData.getCompanyId();
		BigDecimal documentId = ConstantDocument.DOCUMENT_CODE_FOR_FCSALE;
		BigDecimal branchId = countryBranch.getBranchId() == null ? BigDecimal.ZERO : countryBranch.getBranchId();
		Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId,
				finYear, processInd, branchId);
		return (BigDecimal) output.get("P_DOC_NO");
	}

	/*public List<TimeSlotDto> fetchTimeSlot(BigDecimal shippingAddressId) {
		List<TimeSlotDto> timeSlotList = new ArrayList<>();
		BigDecimal appCountryId = metaData.getCountryId() == null ? BigDecimal.ZERO : metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId() == null ? BigDecimal.ZERO : metaData.getCompanyId();
		List<FxDeliveryTimeSlotMaster> list = fcSaleOrderTimeSlotDao
				.findByCountryIdAndCompanyIdAndIsActive(appCountryId, companyId, ConstantDocument.Yes);

		if (list != null && !list.isEmpty()) {
			BigDecimal startTime = list.get(0).getStartTime() == null ? BigDecimal.ZERO : list.get(0).getStartTime();
			BigDecimal endTime = list.get(0).getEndTime() == null ? BigDecimal.ZERO : list.get(0).getEndTime();
			BigDecimal timeInterval = list.get(0).getTimeInterval() == null ? BigDecimal.ZERO
					: list.get(0).getTimeInterval();
			BigDecimal noOfDays = list.get(0).getNoOfDays() == null ? BigDecimal.ZERO : list.get(0).getNoOfDays();
			BigDecimal officeendTime = list.get(0).getOfficeEndTime() == null ? BigDecimal.ZERO
					: list.get(0).getOfficeEndTime();
			if (JaxUtil.isNullZeroBigDecimalCheck(shippingAddressId)) {
				ShippingAddressDetail shipp = shippingAddressDao.findOne(shippingAddressId);
				if (shipp != null && shipp.getAddressType() != null
						&& shipp.getAddressType().equalsIgnoreCase(ConstantDocument.FX_LOA)) {
					endTime = officeendTime;
				}
			}

			timeSlotList = DateUtil.getTimeSlotRange(startTime.intValue(), endTime.intValue(), timeInterval.intValue(),
					noOfDays.intValue());
		} else {
			throw new GlobalException(JaxError.FC_SALE_TIME_SLOT_SETUP_MISSING, "No data found in DB");
		}
		return timeSlotList;
	}*/

	
	public List<TimeSlotDto> fetchTimeSlot(BigDecimal shippingAddressId) {
		List<TimeSlotDto> timeSlotList = new ArrayList<>();
		BigDecimal appCountryId = metaData.getCountryId() == null ? BigDecimal.ZERO : metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId() == null ? BigDecimal.ZERO : metaData.getCompanyId();
		List<FxDeliveryTimeSlotMaster> list = fcSaleOrderTimeSlotDao
				.findByCountryIdAndCompanyIdAndIsActive(appCountryId, companyId, ConstantDocument.Yes);

		if (list != null && !list.isEmpty()) {
			BigDecimal startTime = list.get(0).getStartTime() == null ? BigDecimal.ZERO : list.get(0).getStartTime();
			BigDecimal endTime = list.get(0).getEndTime() == null ? BigDecimal.ZERO : list.get(0).getEndTime();
			BigDecimal timeInterval = list.get(0).getTimeInterval() == null ? BigDecimal.ZERO
					: list.get(0).getTimeInterval();
			BigDecimal noOfDays = list.get(0).getNoOfDays() == null ? BigDecimal.ZERO : list.get(0).getNoOfDays();
			BigDecimal officeendTime = list.get(0).getOfficeEndTime() == null ? BigDecimal.ZERO
					: list.get(0).getOfficeEndTime();
			BigDecimal officeStartTime = list.get(0).getOfficeStartTime() == null ? BigDecimal.ZERO
					: list.get(0).getOfficeStartTime();
			if (JaxUtil.isNullZeroBigDecimalCheck(shippingAddressId)) {
				ShippingAddressDetail shipp = shippingAddressDao.findOne(shippingAddressId);
				if (shipp != null && shipp.getAddressType() != null
						&& shipp.getAddressType().equalsIgnoreCase(ConstantDocument.FX_LOA)) {
					startTime = officeStartTime;
					endTime = officeendTime;
				}
			}

			timeSlotList = DateUtil.getTimeSlotRange(startTime, endTime, timeInterval,
					noOfDays.intValue());
		} else {
			throw new GlobalException(JaxError.FC_SALE_TIME_SLOT_SETUP_MISSING, "No data found in DB");
		}
		return timeSlotList;
	}
	public List<ShoppingCartDetailsDto> convertShopingCartDto(List<FxShoppingCartDetails> cartDetailList) {
		List<ShoppingCartDetailsDto> cartListDto = new ArrayList<>();
		cartDetailList.forEach(cartDetails -> cartListDto.add(convertCartDto(cartDetails)));
		return cartListDto;
	}

	public ShoppingCartDetailsDto convertCartDto(FxShoppingCartDetails cartDetails) {
		ShoppingCartDetailsDto dto = new ShoppingCartDetailsDto();
		try {
			BeanUtils.copyProperties(dto, cartDetails);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("convertModel  to convert currency", e);
		}
		return dto;
	}

	public PaygDetailsModel createPgDetails(FcSaleOrderPaynowRequestModel requestmodel) {
		List<FxApplicationDto> listShoppingCart = requestmodel.getCartDetailList();
		PaygDetailsModel pgmodel = new PaygDetailsModel();

		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		if (userFinancialYear != null) {
			pgmodel.setCollDocFYear(userFinancialYear.getFinancialYear());
		}
		pgmodel.setCreationDate(new Date());
		pgmodel.setCustomerId(metaData.getCustomerId());
		pgmodel.setTrnxType(ConstantDocument.S);
		if (!StringUtils.isBlank(metaData.getReferrer())) {
			pgmodel.setCreatedBy(metaData.getReferrer());
		} else {
			if (!StringUtils.isBlank(metaData.getAppType())) {
				pgmodel.setCreatedBy(metaData.getAppType());
			} else {
				pgmodel.setCreatedBy("WEB");
			}
		}
		return pgmodel;

	}

	public FxDeliveryDetailsModel createDeliveryDeetails(FcSaleOrderPaynowRequestModel requestmodel) {
		FxDeliveryDetailsModel deliveryDetails = new FxDeliveryDetailsModel();
		StringBuffer appldocNo = new StringBuffer();
		deliveryDetails.setCreatedDate(new Date());
		deliveryDetails.setDeliveryDate(requestmodel.getDeliveryDate() == null ? new Date()
				: DateUtil.convertStringToDate(requestmodel.getDeliveryDate()));
		deliveryDetails.setDeliveryTimeSlot(requestmodel.getTimeSlot());
		deliveryDetails.setDeliveryCharges(getDeliveryChargesFromParameter());
		deliveryDetails.setShippingAddressId(requestmodel.getShippingAddressId());
		if (requestmodel != null && !requestmodel.getCartDetailList().isEmpty()) {
			for (FxApplicationDto dto : requestmodel.getCartDetailList()) {
				appldocNo = appldocNo.append(dto.getApplicationId()).append(",");
			}
			deliveryDetails.setApplDocNo(appldocNo.toString());
		}
		deliveryDetails.setIsActive(ConstantDocument.Yes);
		if (!StringUtils.isBlank(metaData.getReferrer())) {
			deliveryDetails.setCreatedBy(metaData.getReferrer());
		} else {
			if (!StringUtils.isBlank(metaData.getAppType())) {
				deliveryDetails.setCreatedBy(metaData.getAppType());
			} else {
				deliveryDetails.setCreatedBy("WEB");
			}
		}
		return deliveryDetails;
	}

	public BigDecimal getDeliveryChargesFromParameter() {
		List<ParameterDetails> parameterList = fcSaleExchangeRateDao.getParameterDetails(ConstantDocument.FX_DC,
				ConstantDocument.Yes);
		BigDecimal delicharges = BigDecimal.ZERO;
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BigDecimal localDecimalCurr = currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber();
		if (parameterList != null && !parameterList.isEmpty()) {
			delicharges = RoundUtil.roundBigDecimal(
					parameterList.get(0).getNumericField1() == null ? BigDecimal.ZERO
							: parameterList.get(0).getNumericField1(),
					localDecimalCurr == null ? 0 : localDecimalCurr.intValue());
		}
		return delicharges;
	}

	public List<FxOrderTransactionHistroyDto> getFxOrderTransactionHistroy() {
		BigDecimal customerId = metaData.getCustomerId();

		List<FxOrderTransactionModel> trnxFxOrderList = new ArrayList<FxOrderTransactionModel>();
		List<FxOrderTransactionHistroyDto> trnxFxOrderListDto = new ArrayList<>();
		List<FxOrderTransactionHistroyDto> finalFxOrderListDto = new ArrayList<>();
		trnxFxOrderList = fxTransactionHistroyDao.getFxOrderTrnxList(customerId);

		if (trnxFxOrderList == null || trnxFxOrderList.isEmpty()) {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "fx order list is empty");
		} else {
			trnxFxOrderListDto = convertFxHistDto(trnxFxOrderList);
			finalFxOrderListDto = getMultipleTransactionHistroy(trnxFxOrderListDto);
		}

		return finalFxOrderListDto;
	}

	public List<FxOrderTransactionHistroyDto> convertFxHistDto(List<FxOrderTransactionModel> trnxFxOrderList) {
		List<FxOrderTransactionHistroyDto> dtoList = new ArrayList<>();
		trnxFxOrderList.forEach(trnxDetails -> dtoList.add(convertTrnxDto(trnxDetails)));
		return dtoList;
	}

	public FxOrderTransactionHistroyDto convertTrnxDto(FxOrderTransactionModel trnxDetails) {
		FxOrderTransactionHistroyDto dto = new FxOrderTransactionHistroyDto();
		try {
			BeanUtils.copyProperties(dto, trnxDetails);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("convertModel  to convert currency", e);
		}
		return dto;
	}

	public List<FxOrderTransactionHistroyDto> getMultipleTransactionHistroy(
			List<FxOrderTransactionHistroyDto> trnxFxOrderListDto) {
		List<FxOrderTransactionHistroyDto> finalFxOrderListDto = new ArrayList<>();
		List<BigDecimal> duplciate = new ArrayList<>();
		Map<BigDecimal, String> collectionInventoryIdMap = getCollectionInventoryIdMap(trnxFxOrderListDto);
		for (FxOrderTransactionHistroyDto dto : trnxFxOrderListDto) {
			if (!duplciate.contains(dto.getCollectionDocumentNo())) {
				duplciate.add(dto.getCollectionDocumentNo());
				FxOrderTransactionHistroyDto fianlDto = new FxOrderTransactionHistroyDto();

				HashMap<BigDecimal, BigDecimal> foreignCurrencyAmt = new HashMap<>();
				String mutipleAmt = null;
				String mutipleInventoryId = null;
				String mutilSourceOfIncome = null;
				String mutilPurposeOfTrnx = null;
				String multiTravelCountryName = null;
				String mulTravleDateRange = null;
				String multiTransactionNo = null;
				String multiReceiptPayId = null;
				String multiExchangeRate = null;
				String multiForeignQuotoName = null;
				int i = 0;
				for (FxOrderTransactionHistroyDto histDto : trnxFxOrderListDto) {
					i++;
					if (dto.getCollectionDocumentNo().compareTo(histDto.getCollectionDocumentNo()) == 0) {
						if (mutipleAmt != null) {
							mutipleAmt = mutipleAmt.concat(" ,").concat(histDto.getCurrencyQuoteName().concat(" ")
									.concat(histDto.getForeignTransactionAmount().toString()));
						} else {
							mutipleAmt = histDto.getCurrencyQuoteName().concat(" ")
									.concat(histDto.getForeignTransactionAmount().toString());
						}
						if (mutilSourceOfIncome != null) {
							mutilSourceOfIncome = mutilSourceOfIncome.concat(" ,")
									.concat(histDto.getSourceOfIncomeDesc());
						} else {
							mutilSourceOfIncome = histDto.getSourceOfIncomeDesc();
						}

						if (mutilPurposeOfTrnx != null) {
							mutilPurposeOfTrnx = mutilPurposeOfTrnx.concat(",").concat(histDto.getPurposeOfTrnx());
						} else {
							mutilPurposeOfTrnx = histDto.getPurposeOfTrnx();
						}

						if (multiTravelCountryName != null) {
							multiTravelCountryName = multiTravelCountryName.concat(" ,")
									.concat(histDto.getTravelCountryName());
						} else {
							multiTravelCountryName = histDto.getTravelCountryName();
						}

						if (mulTravleDateRange != null) {
							mulTravleDateRange = mulTravleDateRange.concat(" ,").concat(histDto.getTravelDateRange());
						} else {
							mulTravleDateRange = histDto.getTravelDateRange();
						}

						if (multiTransactionNo != null) {
							multiTransactionNo = multiTransactionNo.concat(" ,").concat(histDto.getDocumentFinanceYear()
									.toString().concat("/").concat(histDto.getDocumentNumber().toString()));
						} else {
							multiTransactionNo = histDto.getDocumentFinanceYear().toString().concat("/")
									.concat(histDto.getDocumentNumber().toString());
						}

						if (multiReceiptPayId != null) {
							multiReceiptPayId = mulTravleDateRange.concat(" ,").concat(histDto.getIdno().toString());
						} else {
							multiReceiptPayId = histDto.getIdno().toString();
						}

						if (multiExchangeRate != null) {
							multiExchangeRate = multiExchangeRate.concat(" ,").concat(histDto.getCurrencyQuoteName()
									.concat(" ").concat(histDto.getExchangeRate().toString()));
						} else {
							multiExchangeRate = histDto.getCurrencyQuoteName().concat(" ")
									.concat(histDto.getExchangeRate().toString());
						}

						if (multiForeignQuotoName != null) {
							multiForeignQuotoName = multiForeignQuotoName.concat(" ,")
									.concat(histDto.getCurrencyQuoteName());
						} else {
							multiForeignQuotoName = histDto.getCurrencyQuoteName();
						}
					}
				}

				if (dto.getDeliveryDetSeqId() != null) {
					fianlDto.setDeliveryAddress(getDeliveryAddress(dto.getCustomerId(), dto.getDeliveryDetSeqId()));

				}

				fianlDto.setIdno(dto.getIdno());
				fianlDto.setMultiAmount(mutipleAmt);
				fianlDto.setCollectionDocumentNo(dto.getCollectionDocumentNo());
				fianlDto.setCollectionDocumentFinYear(dto.getCollectionDocumentFinYear());
				fianlDto.setCollectionDocumentCode(dto.getCollectionDocumentCode());
				fianlDto.setBranchDesc(dto.getBranchDesc());
				fianlDto.setCreatedDate(dto.getCreatedDate());
				fianlDto.setCustomerReference(dto.getCustomerReference());
				fianlDto.setCustomerId(dto.getCustomerId());
				fianlDto.setCustomerName(dto.getCustomerName());
				fianlDto.setDeliveryCharges(dto.getDeliveryCharges());
				fianlDto.setDeliveryDetSeqId(dto.getDeliveryDetSeqId());
				fianlDto.setPagDetSeqId(dto.getPagDetSeqId());
				fianlDto.setDeliveryDate(dto.getDeliveryDate());
				fianlDto.setDeliveryTime(dto.getDeliveryTime());
				fianlDto.setSourceOfIncomeDesc(mutilSourceOfIncome);
				fianlDto.setPurposeOfTrnx(mutilPurposeOfTrnx);
				fianlDto.setTravelCountryName(
						multiTravelCountryName == null ? dto.getTravelCountryName() : multiTravelCountryName);
				fianlDto.setTravelDateRange(mulTravleDateRange == null ? dto.getTravelDateRange() : mulTravleDateRange);
				fianlDto.setLocalCurrQuoteName(dto.getLocalCurrQuoteName());
				fianlDto.setDocumentCode(dto.getDocumentCode());
				fianlDto.setOrderStatus(dto.getOrderStatus());
				fianlDto.setTransactionStatusDesc(dto.getTransactionStatusDesc());
				fianlDto.setTransactionTypeDesc(dto.getTransactionTypeDesc());
				fianlDto.setTransactionReferenceNo(multiTransactionNo);
				fianlDto.setDocumentNumber(dto.getDocumentNumber());
				fianlDto.setDocumentFinanceYear(dto.getDocumentFinanceYear());
				fianlDto.setDocumentDate(dto.getDocumentDate());
				fianlDto.setExchangeRate(dto.getExchangeRate());
				fianlDto.setMultiExchangeRate(multiExchangeRate);
				fianlDto.setLocalTrnxAmount(dto.getLocalTrnxAmount());
				fianlDto.setForeignCurrencyCode(multiForeignQuotoName);
				fianlDto.setOrderStatusCode(dto.getOrderStatusCode());
				fianlDto.setInventoryId(collectionInventoryIdMap.get(dto.getCollectionDocumentNo()));
				fianlDto.setOtpTokenPrefix(dto.getOtpTokenPrefix());
				fianlDto.setOtpTokenCustomer(dto.getOtpTokenCustomer());
				finalFxOrderListDto.add(fianlDto);
			}

		}
		return finalFxOrderListDto;
	}

	/**
	 * @param trnxFxOrderListDto
	 * @return map of collection doc no vs inventory ids separated by comma
	 */
	private Map<BigDecimal, String> getCollectionInventoryIdMap(List<FxOrderTransactionHistroyDto> trnxFxOrderListDto) {
		BinaryOperator<String> mergeFunction = (oldVal, newVal) -> {
			if (StringUtils.isNotBlank(newVal)) {
				return oldVal + "," + newVal;
			} else {
				return oldVal;
			}
		};
		return trnxFxOrderListDto.stream().collect(Collectors.toMap(i -> i.getCollectionDocumentNo(),
				(i -> i.getInventoryId() == null ? "" : i.getInventoryId()), mergeFunction));
	}

	public String getDeliveryAddress(BigDecimal customerId, BigDecimal deliveryDetSeqId) {
		logger.debug("getDeliveryAddress  :" + deliveryDetSeqId);
		ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
		String address = "";
		StringBuffer sb = new StringBuffer();
		String concat = ",";
		FxDeliveryDetailsModel fxDelDetailModel = deliveryDetailsRepos.findOne(deliveryDetSeqId);
		if (fxDelDetailModel != null) {
			shippingAddressDto = addressManager.fetchShippingAddress(customerId,
					fxDelDetailModel.getShippingAddressId());
			if (shippingAddressDto != null) {
				sb = sb.append(shippingAddressDto.getStreet() == null ? ""
						: "Street " + shippingAddressDto.getStreet() + concat)
						.append(shippingAddressDto.getBlock() == null ? ""
								: "Block " + shippingAddressDto.getBlockNo() + concat)
						.append(shippingAddressDto.getBuildingNo() == null ? ""
								: "House no. " + shippingAddressDto.getBuildingNo() + concat)
						.append(shippingAddressDto.getFlat() == null ? ""
								: "Flat " + shippingAddressDto.getHouse());
				if (shippingAddressDto.getLocalContactCity() != null) {
					sb.append(concat).append("City ").append(shippingAddressDto.getLocalContactCity() == null ? "": shippingAddressDto.getLocalContactCity());
				}
				if(shippingAddressDto.getGovernoatesDto()!=null && !shippingAddressDto.getGovernoatesDto().equals("")) {
					sb.append(concat).append(shippingAddressDto.getGovernoatesDto().getResourceName());
				}
				if(shippingAddressDto.getGovtAreaDesc()!=null && !shippingAddressDto.getGovtAreaDesc().equals("")) {
					sb.append(concat).append(shippingAddressDto.getGovtAreaDesc());
				}
				if(shippingAddressDto.getLocalContactDistrict()!=null && !shippingAddressDto.getLocalContactDistrict().equals("")) {
					sb.append(concat).append(shippingAddressDto.getLocalContactDistrict());
				}
				
				if(shippingAddressDto.getLocalContactState()!=null && !shippingAddressDto.getLocalContactState().equals("")) {
					sb.append(concat).append(shippingAddressDto.getLocalContactState());
				}
				
			}
		}
		if (sb != null) {
			address = sb.toString();
		}
		return address;
	}

}
