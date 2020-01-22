package com.amx.jax.branchremittance.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dao.BranchRemittancePaymentDao;
import com.amx.jax.dao.PayAtBranchDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.BanksView;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.CurrencyWiseDenominationMdlv1;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.PaymentModesModel;
import com.amx.jax.dbmodel.partner.RemitApplSrvProv;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.CustomerBank;
import com.amx.jax.dbmodel.remittance.LocalBankDetailsView;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.dbmodel.remittance.StaffAuthorizationView;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.FcSaleBranchOrderManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.CustomerBankDto;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.response.fx.FxEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FxExchangeRateBreakup;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.ConfigDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.CustomerBankRelationNameDto;
import com.amx.jax.model.response.remittance.CustomerShoppingCartDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentLinkAppDto;
import com.amx.jax.model.response.remittance.PaymentModeDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
import com.amx.jax.model.response.remittance.PlaceOrderApplDto;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.pricer.var.PricerServiceConstants;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.IBankMasterFromViewDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IRemitApplSrvProvRepository;
import com.amx.jax.repository.IShoppingCartDetailsRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.response.payatbranch.PaymentModesDTO;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.LoyalityPointService;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.utils.ArgUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittancePaymentManager extends AbstractModel {

	Logger logger = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Autowired
	MetaData metaData;

	@Autowired
	CurrencyMasterService currencyMasterService;

	@Autowired
	BranchRemittancePaymentDao branchRemittancePaymentDao;

	@Autowired
	FcSaleBranchOrderManager fcSaleBranchOrderManager;
	
	@Autowired
	IBankMasterFromViewDao bankMaster;
	
	@Autowired
	ICustomerRepository customerRepos;
	
	@Autowired
	RemittanceApplicationRepository appRepository;
	
	@Autowired
	RoutingProcedureDao routingProDao;
	
	@Autowired
	ICurrencyDao currDao;
	
	@Autowired
	CryptoUtil cryptoUtil;
		
	@Autowired
	IShoppingCartDetailsRepository iShoppingCartDetailsRepository;

	@Autowired
	IRemitApplSrvProvRepository remitApplSrvProvRepository;
	
	@Autowired
	PartnerTransactionManager partnerTransactionManager;
	
	@Autowired
	AdditionalInstructionDataRepository addlInstDataRepo;
	
	@Autowired
	BankMetaService bankMetaService;
	
	@Autowired
	CountryBranchRepository countryBranchRepository;

	@Autowired
	BankService bankService;
	
	@Autowired
	PayAtBranchDao payAtBranchDao;
	
	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;
	
	@Autowired
	BranchRemittanceManager branchRemitManager;
	
	@Autowired
	LoyalityPointService loyalityPointService;

	@Autowired
	PlaceOrderManager placeOrderManager;

	/* 
	 * @param   :fetch customer shopping cart application
	 * @return CustomerShoppingCartDto
	 */
	public BranchRemittanceApplResponseDto fetchCustomerShoppingCart(BigDecimal customerId,BigDecimal localCurrencyId){
		BranchRemittanceApplResponseDto cartList = new BranchRemittanceApplResponseDto();
		/** Total gross amount without commission **/
		BigDecimal totalLocalAmount =BigDecimal.ZERO;
		/* total net amount with commission */
		BigDecimal totalNetAmount =BigDecimal.ZERO;
		BigDecimal totalTrnxFees =BigDecimal.ZERO;
		BigDecimal totalLyltyPointAmt =BigDecimal.ZERO;
		BigDecimal totalCustomerLoyaltyPoits = BigDecimal.ZERO;
		Boolean addtoCart =true;
		
		
		List<CustomerShoppingCartDto> lstCustShpcrt = new ArrayList<>();
		List<PaymentLinkAppDto> listPaymentLinkAppDto = new ArrayList<PaymentLinkAppDto>();
		FxExchangeRateBreakup breakup = new FxExchangeRateBreakup();
		BigDecimal decimalNumber = BigDecimal.ZERO;
		CurrencyMasterMdlv1 currencyMaster = currencyMasterService.getCurrencyMasterById(localCurrencyId);
		if(currencyMaster != null) {
			breakup.setLcDecimalNumber(currencyMaster.getDecinalNumber() == null ? decimalNumber : currencyMaster.getDecinalNumber());
			Customer customer = customerRepos.getCustomerByCountryAndCompAndCustoemrId(metaData.getCountryId(),metaData.getCompanyId(),customerId);
			
			if(customer!=null) {
				totalCustomerLoyaltyPoits = customer.getLoyaltyPoints()==null?BigDecimal.ZERO:customer.getLoyaltyPoints();
			}
			
			
			CountryBranchMdlv1 countryBranch = new CountryBranchMdlv1();
			countryBranch = bankMetaService.getCountryBranchById(metaData.getCountryBranchId()); //user branch not customer branch
			
			/*
			 * if(countryBranch!=null &&
			 * !countryBranch.getBranchId().equals(ConstantDocument.ONLINE_BRANCH_LOC_CODE))
			 * { deActivateOnlineApplication(); }else if (countryBranch!=null &&
			 * countryBranch.getBranchId().equals(ConstantDocument.ONLINE_BRANCH_LOC_CODE)){
			 * //De-activate Branch application in online
			 * deActivateBranchApplicationInOnline(); }
			 */
			
			List<ShoppingCartDetails> lstCustomerShopping = branchRemittancePaymentDao.fetchCustomerShoppingCart(customerId);
			
			
			ConfigDto config = getLimitCheck(metaData.getCustomerId());
			config = partnerTransactionManager.paymentModeServiceProviderLimit(config,lstCustomerShopping,customer.getIdentityTypeId());
			cartList.setConfigDto(config);
			
			if(lstCustomerShopping != null && !lstCustomerShopping.isEmpty() && lstCustomerShopping.size() != 0) {
				clearCartForPB(lstCustomerShopping);
				for (ShoppingCartDetails customerApplDto : lstCustomerShopping) {
					
					if(customerApplDto.getApplicationType()!=null && !customerApplDto.getApplicationType().equalsIgnoreCase(ConstantDocument.FS)) {
					BigDecimal fcCurrencyId = customerApplDto.getForeignCurrency();
					totalLocalAmount = totalLocalAmount.add(customerApplDto.getLocalTranxAmount()==null?BigDecimal.ZERO:customerApplDto.getLocalTranxAmount());
					totalNetAmount   =totalNetAmount.add(customerApplDto.getLocalNextTranxAmount()==null?BigDecimal.ZERO:customerApplDto.getLocalNextTranxAmount());
					totalTrnxFees    =totalTrnxFees.add(customerApplDto.getLocalCommisionAmount()==null?BigDecimal.ZERO:customerApplDto.getLocalCommisionAmount());
					totalLyltyPointAmt =totalLyltyPointAmt.add(customerApplDto.getLoyaltsPointencahsed()==null?BigDecimal.ZERO:customerApplDto.getLoyaltsPointencahsed());
					BigDecimal loyalityPointAmountEncashed = ((customerApplDto.getLoyaltsPointencahsed()==null)?BigDecimal.ZERO:customerApplDto.getLoyaltsPointencahsed());
					BigDecimal loyalityPointsEncashed  = loyalityPointService.getEquivalentLoyalityPoints(loyalityPointAmountEncashed);
					if(customerApplDto.getLoyaltsPointIndicator()!=null && customerApplDto.getLoyaltsPointIndicator().equalsIgnoreCase(ConstantDocument.Yes) && totalCustomerLoyaltyPoits.compareTo(new BigDecimal(1000))>=0) {
						totalCustomerLoyaltyPoits = totalCustomerLoyaltyPoits.subtract(loyalityPointsEncashed);
					}
					
					cartList.setTotalLocalAmount(totalLocalAmount);
					cartList.setTotalNetAmount(totalNetAmount);//.subtract(totalLyltyPointAmt));
					cartList.setTotalTrnxFees(totalTrnxFees);
					cartList.setTotalLyltyPointAmt(totalLyltyPointAmt);
					cartList.setTotalLoyaltyPointAvaliable(totalCustomerLoyaltyPoits);
					cartList.setTotalNetCollectionAmount(totalNetAmount.subtract(totalLyltyPointAmt==null?BigDecimal.ZERO:totalLyltyPointAmt));
					List<PaymentModesDTO> paymentModeDtoList = new ArrayList<>();
					fetchPaymentModes(paymentModeDtoList);
					
					
					BankMasterMdlv1 bankMasterView = bankService.getBankById(customerApplDto.getRoutingBankId());
					
					if(bankMasterView!=null && bankMasterView.getBankCode().contains(SERVICE_PROVIDER_BANK_CODE.HOME.toString()) &&  addtoCart==true) {
						cartList.setAddToCart(false);
						addtoCart =false;
						Iterator<PaymentModesDTO> iter = paymentModeDtoList.iterator();
						while(iter.hasNext()) {
							PaymentModesDTO paymentModesDTO = iter.next();
							if(ConstantDocument.PB_PAYMENT.equals(paymentModesDTO.getPaymentModeCode())) {
								iter.remove();
								break;
							}
						}
						
						
					}
					
					cartList.setPaymentModeList(paymentModeDtoList);
					
					if(fcCurrencyId == null || fcCurrencyId.compareTo(BigDecimal.ZERO) == 0){
						throw new GlobalException(JaxError.NULL_CURRENCY_ID, "Null foreign currency id passed");
					}
					currencyMaster = currencyMasterService.getCurrencyMasterById(fcCurrencyId);
					if(currencyMaster != null) {
						breakup.setFcDecimalNumber(currencyMaster.getDecinalNumber() == null ? decimalNumber : currencyMaster.getDecinalNumber());

							/*
							 * if(!ConstantDocument.PB_PAYMENT.equalsIgnoreCase(customerApplDto.
							 * getApplicationPaymentType())) {
							 * lstCustShpcrt.add(createCustomerShoppingCartDto(customerApplDto,
							 * localCurrencyId,fcCurrencyId,breakup)); }
							 */

						lstCustShpcrt.add(createCustomerShoppingCartDto(customerApplDto,localCurrencyId,fcCurrencyId,breakup));
						// Direct Payment link application now getting converted to remittance so commenting the following code
						/*if(customerApplDto.getPaymentLinkId()!=null) {
							PaygDetailsModel paymentLinkModel = payGDetailsRepository.findOne(customerApplDto.getPaymentLinkId());
							if(paymentLinkModel != null && ConstantDocument.DIRECT_PAYMENT_LINK_PAID.equalsIgnoreCase(paymentLinkModel.getLinkActive())) {
								String applicationIds = paymentLinkModel.getApplIds();
								String appIdsArray[] = applicationIds.split(",");
								for(int i=0;i<appIdsArray.length;i++) {
									listPaymentLinkAppDto.add(createPaymentLinkAppDto(new BigDecimal(appIdsArray[i]), paymentLinkModel));
									cartList.setPaymentLinkAppDto(listPaymentLinkAppDto);
								}
							}
							
							
						}*/

						
						cartList.setShoppingCartDetails(lstCustShpcrt);
						
						
					}else {
						throw new GlobalException(JaxError.INVALID_CURRENCY_ID, "Invalid foreign currency id passed");
					}
				}
				}
			}else {
				cartList.setTotalLoyaltyPointAvaliable(totalCustomerLoyaltyPoits);
			}
		}else {
			throw new GlobalException(JaxError.INVALID_CURRENCY_ID, "Invalid local currency id passed");
		}

		List<PlaceOrderApplDto> placeOrderList = placeOrderManager.getPlaceOrderList();
		
		if(placeOrderList!=null && !placeOrderList.isEmpty()) {
			cartList.setPlaceOrderApplList(placeOrderList);
		}
		
		return cartList;
	}

	private void fetchPaymentModes(List<PaymentModesDTO> paymentModeDtoList) {
		
		List<PaymentModesModel> paymentModesList = payAtBranchDao.getPaymentModes();
		
		for(PaymentModesModel paymentModel:paymentModesList) {
			PaymentModesDTO paymentModeDto = new PaymentModesDTO();
			paymentModeDto.setPaymentModeCode(paymentModel.getPaymentType());
			paymentModeDto.setPaymentModeDesc(paymentModel.getPaymentDescription());
			paymentModeDtoList.add(paymentModeDto);
		}
		
	}

	private CustomerShoppingCartDto createCustomerShoppingCartDto(ShoppingCartDetails shoppingCartDetails,BigDecimal localCurrencyId,BigDecimal fcCurrencyId,FxExchangeRateBreakup breakup) {
		String bankCode = null;
		
		CustomerShoppingCartDto shoppingCartDataTableBean = new CustomerShoppingCartDto();
		shoppingCartDataTableBean.setRemittanceApplicationId(shoppingCartDetails.getRemittanceApplicationId());
		shoppingCartDataTableBean.setApplicationType(shoppingCartDetails.getApplicationType());
		shoppingCartDataTableBean.setApplicationTypeDesc(shoppingCartDetails.getApplicationTypeDesc());
		shoppingCartDataTableBean.setBeneficiaryAccountNo(shoppingCartDetails.getBeneficiaryAccountNo());
		shoppingCartDataTableBean.setBeneficiaryBank(shoppingCartDetails.getBeneficiaryBank());
		shoppingCartDataTableBean.setBeneficiaryBranch(shoppingCartDetails.getBeneficiaryBranch());
		shoppingCartDataTableBean.setBeneficiaryFirstName(shoppingCartDetails.getBeneficiaryFirstName());
		shoppingCartDataTableBean.setBeneficiarySecondName(shoppingCartDetails.getBeneficiarySecondName());
		shoppingCartDataTableBean.setBeneficiaryThirdName(shoppingCartDetails.getBeneficiaryThirdName());
		shoppingCartDataTableBean.setBeneficiaryFourthName(shoppingCartDetails.getBeneficiaryFourthName());
		shoppingCartDataTableBean.setBeneficiaryId(shoppingCartDetails.getBeneficiaryId());
		shoppingCartDataTableBean.setBeneficiaryInterBankOne(shoppingCartDetails.getBeneficiarySwiftAddrOne());
		shoppingCartDataTableBean.setBeneficiaryInterBankTwo(shoppingCartDetails.getBeneficiarySwiftAddrTwo());
		shoppingCartDataTableBean.setBeneficiarySwiftBankOne(shoppingCartDetails.getBeneficiarySwiftBankOne());
		shoppingCartDataTableBean.setBeneficiarySwiftBankTwo(shoppingCartDetails.getBeneficiarySwiftBankTwo());
		shoppingCartDataTableBean.setBeneficiaryName(shoppingCartDetails.getBeneficiaryName());
		shoppingCartDataTableBean.setCompanyId(shoppingCartDetails.getCompanyId());
		shoppingCartDataTableBean.setDocumentFinanceYear(shoppingCartDetails.getDocumentFinanceYear());
		shoppingCartDataTableBean.setDocumentId(shoppingCartDetails.getDocumentId());
		shoppingCartDataTableBean.setLocalDeliveryAmount(shoppingCartDetails.getLocalDeliveryAmount());
		shoppingCartDataTableBean.setIsActive(shoppingCartDetails.getIsActive());
		shoppingCartDataTableBean.setCustomerId(shoppingCartDetails.getCustomerId());
		shoppingCartDataTableBean.setExchangeRateApplied(shoppingCartDetails.getExchangeRateApplied());
		shoppingCartDataTableBean.setApplicationDetailsId(shoppingCartDetails.getApplicationId());
		shoppingCartDataTableBean.setDocumentNo(shoppingCartDetails.getDocumentNo());
		shoppingCartDataTableBean.setForeigncurrency(shoppingCartDetails.getForeignCurrency());
		shoppingCartDataTableBean.setForeignCurrencyDesc(shoppingCartDetails.getForeignCurrencyDesc());
		shoppingCartDataTableBean.setLocalcurrency(shoppingCartDetails.getLocalCurrency());
		shoppingCartDataTableBean.setLoyaltsPointIndicator(shoppingCartDetails.getLoyaltsPointIndicator());
		shoppingCartDataTableBean.setLoyaltsPointencahsed(shoppingCartDetails.getLoyaltsPointencahsed());
		shoppingCartDataTableBean.setAmtbCouponencahsed(shoppingCartDetails.getAmtbCouponEncashed());
		shoppingCartDataTableBean.setSelectedrecord(Boolean.FALSE);
		shoppingCartDataTableBean.setSourceOfIncomeDesc(shoppingCartDetails.getSourceOfIncomeDesc());
		shoppingCartDataTableBean.setRemittanceDesc(shoppingCartDetails.getRemittanceDescription());
		shoppingCartDataTableBean.setDeliveryDesc(shoppingCartDetails.getDeliveryDescription());

		if (shoppingCartDetails.getForeignTranxAmount() != null && breakup.getFcDecimalNumber() != null) {
			shoppingCartDataTableBean.setForeignTranxAmount(RoundUtil.roundBigDecimal(shoppingCartDetails.getForeignTranxAmount(),breakup.getFcDecimalNumber().intValue()));
		}
		if (shoppingCartDetails.getLocalTranxAmount() != null && breakup.getLcDecimalNumber() != null) {
			shoppingCartDataTableBean.setLocalTranxAmount(RoundUtil.roundBigDecimal(shoppingCartDetails.getLocalTranxAmount(),breakup.getLcDecimalNumber().intValue()));
		}
		if (shoppingCartDetails.getLocalChargeAmount() != null && breakup.getLcDecimalNumber() != null) {
			shoppingCartDataTableBean.setLocalChargeAmount(RoundUtil.roundBigDecimal(shoppingCartDetails.getLocalChargeAmount(),breakup.getLcDecimalNumber().intValue()));
		}
		if (shoppingCartDetails.getLocalCommisionAmount() != null && breakup.getLcDecimalNumber() != null) {
			shoppingCartDataTableBean.setLocalCommisionAmount(RoundUtil.roundBigDecimal(shoppingCartDetails.getLocalCommisionAmount(),breakup.getLcDecimalNumber().intValue()));
		}
		if (shoppingCartDetails.getLocalNextTranxAmount() != null && breakup.getLcDecimalNumber() != null) {
			shoppingCartDataTableBean.setLocalNextTranxAmount(RoundUtil.roundBigDecimal(shoppingCartDetails.getLocalNextTranxAmount(),breakup.getLcDecimalNumber().intValue()));
		}
		if (shoppingCartDetails.getSpldeal() != null) {
			shoppingCartDataTableBean.setSpldeal(shoppingCartDetails.getSpldeal());
			shoppingCartDataTableBean.setSpldealStatus(ConstantDocument.Yes);
		} else {
			shoppingCartDataTableBean.setSpldealStatus(ConstantDocument.No);
		}
		
		List<BanksView> bankView =bankMaster.getBankListByBankId(shoppingCartDetails.getRoutingBankId());
		if(bankView != null && !bankView.isEmpty()) {
			BanksView routingbankDt = bankView.get(0);
			if(routingbankDt != null) {
				if(routingbankDt.getBankFullName() != null) {
					shoppingCartDataTableBean.setRoutingBank(routingbankDt.getBankFullName());
				}
				if(routingbankDt.getBankInd() != null) {
					shoppingCartDataTableBean.setBankIndicator(routingbankDt.getBankInd());
				}
				if(routingbankDt.getBankCode() != null) {
					bankCode = routingbankDt.getBankCode();
				}
			}
		}
		
		shoppingCartDataTableBean.setRoutingBankId(shoppingCartDetails.getRoutingBankId());
		shoppingCartDataTableBean.setBeneRelationseqId(shoppingCartDetails.getBeneRelationseqId());
		shoppingCartDataTableBean.setSourceOfIncomeId(shoppingCartDetails.getSourceofincome()==null?BigDecimal.ZERO:new BigDecimal(shoppingCartDetails.getSourceofincome()));
		shoppingCartDataTableBean.setDomXRate(RoundUtil.roundBigDecimal(BigDecimal.ONE.divide(shoppingCartDetails.getExchangeRateApplied(),10,RoundingMode.HALF_UP),breakup.getFcDecimalNumber().intValue()));
		shoppingCartDataTableBean.setCustomerSignatureString(shoppingCartDetails.getCustomerSignatureClob());
		if(JaxUtil.isNullZeroBigDecimalCheck(shoppingCartDetails.getLocalCurrency())){
			CurrencyMasterMdlv1 localCurr = currDao.getOne(shoppingCartDetails.getLocalCurrency());
			shoppingCartDataTableBean.setLocalCurrencyCode(localCurr.getQuoteName()==null?"":localCurr.getQuoteName());
		}

		if(JaxUtil.isNullZeroBigDecimalCheck(shoppingCartDetails.getForeignCurrency())){
			CurrencyMasterMdlv1 fcCurr = currDao.getOne(shoppingCartDetails.getForeignCurrency());
			shoppingCartDataTableBean.setForeignCurrencyCode(fcCurr.getQuoteName()==null?"":fcCurr.getQuoteName());
		}
		
		RemittanceApplication remittanceApplication = remittanceApplicationDao.getApplication(shoppingCartDetails.getRemittanceApplicationId());
		if(!ArgUtil.isEmpty(remittanceApplication) && ConstantDocument.PB_PAYMENT.equals(remittanceApplication.getPaymentType())) {
			shoppingCartDataTableBean.setCustomerSignatureString(remittanceApplication.getFsCustomer().getSignatureSpecimenClob());
		}
		else {
			shoppingCartDataTableBean.setCustomerSignatureString(shoppingCartDetails.getCustomerSignatureClob());
		}

		
		// fetch trnx expiry date
		if(bankCode != null && bankCode.equalsIgnoreCase(PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE.HOME.name())) {
			RemitApplSrvProv remitApplSrvProv = remitApplSrvProvRepository.findByRemittanceApplicationId(shoppingCartDetails.getRemittanceApplicationId());
			if(remitApplSrvProv != null) {
				if(remitApplSrvProv.getOfferExpirationDate() != null) {
					shoppingCartDataTableBean.setTrnxExpirationDate(remitApplSrvProv.getOfferExpirationDate().getTime());
				}
				if(remitApplSrvProv.getOfferStartingDate() != null) {
					shoppingCartDataTableBean.setTrnxStartDate(remitApplSrvProv.getOfferStartingDate().getTime());
				}
			}else {
				Calendar endcalendar = Calendar.getInstance();
				endcalendar.setTimeInMillis(System.currentTimeMillis());
				endcalendar.add(Calendar.MINUTE, -1);
				shoppingCartDataTableBean.setTrnxExpirationDate(endcalendar.getTimeInMillis());
				shoppingCartDataTableBean.setTrnxStartDate(endcalendar.getTimeInMillis());
			}
		}
		
		shoppingCartDataTableBean.setApplPaymentType(shoppingCartDetails.getApplicationPaymentType());
		shoppingCartDataTableBean.setPurposeOfTrnx(getPurposeOfTrnx(shoppingCartDetails.getRemittanceApplicationId()));
		
		return shoppingCartDataTableBean;
	}

	/* 
	 * @param   :fetch mode of payment
	 * @return PaymentModeOfPaymentDto
	 */
	public PaymentModeDto fetchModeOfPayment(BigDecimal languageId){
		branchRemitManager.checkingStaffIdNumberWithCustomer();
		PaymentModeDto dto = new PaymentModeDto();
		List<PaymentModeOfPaymentDto> lstModeofPayment = new ArrayList<>();
		List<Object[]> lstPayment = branchRemittancePaymentDao.fetchModeOfPayment(languageId);
		ConfigDto config= getLimitCheck(metaData.getCustomerId());
		//config = partnerTransactionManager.paymentModeServiceProviderLimit(config);
		if (lstPayment != null && lstPayment.size() != 0) {
			for (Object object : lstPayment) {
				Object[] paymentModes = (Object[]) object;
				if (paymentModes.length >= 6) {
					PaymentModeOfPaymentDto lstModePayment = new PaymentModeOfPaymentDto();
					
					if(paymentModes[0] != null) {
						lstModePayment.setResourceId(new BigDecimal(paymentModes[0].toString()));
					}
					if(paymentModes[1] != null) {
						lstModePayment.setPaymentModeDescId(new BigDecimal(paymentModes[1].toString()));
					}
					if(paymentModes[2] != null) {
						lstModePayment.setResourceCode(paymentModes[2].toString());
					}
					if(paymentModes[3] != null) {
						lstModePayment.setResourceName(paymentModes[3].toString());
					}
					if(paymentModes[4] != null) {
						lstModePayment.setLanguageId(new BigDecimal(paymentModes[4].toString()));
					}
					if(paymentModes[5] != null) {
						lstModePayment.setIsActive(paymentModes[5].toString());
					}
					
					lstModeofPayment.add(lstModePayment);
				}
				
			}
			dto.setPaymentModeDtoList(lstModeofPayment);
			dto.setConfigDto(config);
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found for mode of payments");
		}
		return dto;
	}

	/* 
	 * @param   :fetch local bank list
	 * @return LocalBankDetailsDto
	 */
	public List<LocalBankDetailsDto> fetchLocalBanks(BigDecimal countryId){
		List<LocalBankDetailsDto> lstLocalBanksDto = new ArrayList<>();
		List<LocalBankDetailsView> lstlocalBanksView = branchRemittancePaymentDao.fetchLocalBanks(countryId);
		if(lstlocalBanksView != null && lstlocalBanksView.size() != 0) {
			lstlocalBanksView.forEach(localBanks -> lstLocalBanksDto.add(convertCartDto(localBanks)));
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found for local banks");
		}

		return lstLocalBanksDto;
	}

	/* 
	 * @param   :fetch customer local bank list
	 * @return LocalBankDetailsDto
	 */
	public List<CustomerBankDetailsDto> fetchCustomerLocalBanks(BigDecimal customerId,BigDecimal countryId){
		List<CustomerBankDetailsDto> lstCustBanksDto = new ArrayList<>();
		List<LocalBankDetailsDto> lstCustLocalBanksDto = new ArrayList<>();
		List<LocalBankDetailsDto> lstLocalBanksDto = new ArrayList<>();
		List<LocalBankDetailsView> lstCustlocalBanks = branchRemittancePaymentDao.fetchCustomerLocalBanks(customerId);
		if(lstCustlocalBanks != null && lstCustlocalBanks.size() != 0) {
			lstCustlocalBanks.forEach(localBanks -> lstCustLocalBanksDto.add(convertCartDto(localBanks)));
			if(lstCustLocalBanksDto != null && lstCustLocalBanksDto.size() != 0) {
				for (LocalBankDetailsDto localBankDetailsDto : lstCustLocalBanksDto) {
					CustomerBankDetailsDto customerBankDetailsDto = new CustomerBankDetailsDto();
					customerBankDetailsDto = fetchCustomerNames(customerId, localBankDetailsDto.getChequeBankId());
					customerBankDetailsDto.setLocalBankDetailsDto(localBankDetailsDto);
					lstCustBanksDto.add(customerBankDetailsDto);
				}
			}
		}else {
			lstLocalBanksDto = fetchLocalBanks(countryId);
			if(lstCustLocalBanksDto != null && lstCustLocalBanksDto.size() != 0) {
				for (LocalBankDetailsDto localBankDetailsDto : lstLocalBanksDto) {
					CustomerBankDetailsDto customerBankDetailsDto = new CustomerBankDetailsDto();
					customerBankDetailsDto.setLocalBankDetailsDto(localBankDetailsDto);
					lstCustBanksDto.add(customerBankDetailsDto);
				}
			}
		}

		return lstCustBanksDto;
	}

	public LocalBankDetailsDto convertCartDto(LocalBankDetailsView lstlocalBanksView) {
		LocalBankDetailsDto dto = new LocalBankDetailsDto();
		try {
			BeanUtils.copyProperties(dto, lstlocalBanksView);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("local banks convertion fail", e);
		}
		return dto;
	}

	public CustomerBankDetailsDto fetchCustomerNames(BigDecimal customerId,BigDecimal bankId){
		CustomerBankDetailsDto customerBankDetailsDto = new CustomerBankDetailsDto();
		List<String> nameList = new ArrayList<>();
		List<CustomerBankRelationNameDto> listRelationName = new ArrayList<>();	
	List<Object[]> custBankName = branchRemittancePaymentDao.fetchCustomerBankNames(customerId,bankId);
	if (custBankName != null && custBankName.size() != 0) {
		for (Object object : custBankName) {
			Object[] custBankNameObject = (Object[]) object;
			CustomerBankRelationNameDto customerBankrelationName = new CustomerBankRelationNameDto();
			if(custBankNameObject[0]!=null) {
				customerBankrelationName.setCutomerBankNBame(custBankNameObject[0].toString());
				nameList.add(custBankNameObject[0].toString());
				//customerBankDetailsDto.setCustomerNames(nameList);
			}
			if(custBankNameObject[1]!=null) {
				List<BigDecimal> relationList = new ArrayList<>();
				relationList.add(new BigDecimal(custBankNameObject[1].toString()));
				customerBankrelationName.setRelationId(new BigDecimal(custBankNameObject[1].toString()));
				//customerBankDetailsDto.setRelationId(relationList);
			}
			if(custBankNameObject[2]!=null) {
				customerBankrelationName.setCardTypeId(new BigDecimal(custBankNameObject[2].toString()));
			}
			
			listRelationName.add(customerBankrelationName);
			customerBankDetailsDto.setCustomerBankrelationName(listRelationName);
		}
	}
	return customerBankDetailsDto;
}

	
	
	/* 
	 * @param   :fetch pos banks list
	 * @return ResourceDTO
	 */
	public List<ResourceDTO> fetchPosBanks(){
		String recordId = ConstantDocument.PARAM_POS_BANK;
		List<ParameterDetails> paramDetails = branchRemittancePaymentDao.fetchParameterDetails(recordId, ConstantDocument.Yes);
		if(paramDetails != null && paramDetails.size() != 0) {
			return paramDetails.stream().map(remark -> {
				return new ResourceDTO(remark.getParameterDetailsId(), remark.getCharField2(), remark.getCharField1());
			}).collect(Collectors.toList());
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found for pos banks");
		}
	}

	/* 
	 * @param   :fetch pay in stock local currency
	 * @return UserStockDto
	 */
	public List<UserStockDto> fetchLocalCurrencyDenomination(BigDecimal currencyId){
		List<UserStockDto> lstUserStockDto = new ArrayList<>();
		List<CurrencyWiseDenominationMdlv1> lstCurrencyDenomination = branchRemittancePaymentDao.fetchCurrencyDenomination(currencyId, ConstantDocument.Yes);
		if(lstCurrencyDenomination != null && lstCurrencyDenomination.size() != 0) {
			for (CurrencyWiseDenominationMdlv1 currencyWiseDenomination : lstCurrencyDenomination) {
				UserStockDto userStockDto = new UserStockDto();
				userStockDto.setCurrencyId(currencyWiseDenomination.getExCurrencyMaster().getCurrencyId());
				userStockDto.setCurrentStock(null);
				userStockDto.setDenominationAmount(currencyWiseDenomination.getDenominationAmount());
				userStockDto.setDenominationDesc(currencyWiseDenomination.getDenominationDesc());
				userStockDto.setDenominationId(currencyWiseDenomination.getDenominationId());
				userStockDto.setStockId(null);
				lstUserStockDto.add(userStockDto);
			}
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found for local currency denomination");
		}

		return lstUserStockDto;
	}

	/* 
	 * @param   :save customer bank [KNET] details
	 * @return True or False
	 */
	public Boolean saveCustomerBankDetails(CustomerBankRequest customerBank,BigDecimal appcountryId,BigDecimal employeeId,BigDecimal customerId,BigDecimal companyId) {
		Boolean status = Boolean.FALSE;
		Customer customer = null;
		List<CustomerBank> lstCustomerBank = new ArrayList<>();
		FxEmployeeDetailsDto empDet = fcSaleBranchOrderManager.fetchEmployee(employeeId);
		List<Customer> lstcustomer = branchRemittancePaymentDao.fetchCustomerByCustomerId(appcountryId, companyId, customerId);
		if(lstcustomer != null && lstcustomer.size() != 0) {
			customer = lstcustomer.get(0);
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No customer records found");
		}

		if(customerBank != null) {
			List<CustomerBankDto> custmerBankList = customerBank.getCustomerBankDetails();
			for (CustomerBankDto customerBankRequest : custmerBankList) {
				CustomerBank customerBankDt = new CustomerBank();
				customerBankDt.setBankCode(customerBankRequest.getBankCode());
				customerBankDt.setBankId(customerBankRequest.getBankId());
				customerBankDt.setCollectionMode(customerBankRequest.getCollectionMode());
				customerBankDt.setCreatedBy(empDet.getUserName());
				customerBankDt.setCreatedDate(new Date());
				customerBankDt.setCustomerId(customerId);
				customerBankDt.setCustomerReference(customer.getCustomerReference());
				customerBankDt.setDebitCardName(customerBankRequest.getDebitCardName());
				customerBankDt.setIsActive(ConstantDocument.Yes);
				customerBankDt.setRelationsId(customerBankRequest.getRelationsId());
				
				if(customerBankRequest.getCardTypeId() != null){
					customerBankDt.setCardTypeId(customerBankRequest.getCardTypeId());
				}
				
				lstCustomerBank.add(customerBankDt);
			}
			if(lstCustomerBank != null && lstCustomerBank.size() != 0) {
				branchRemittancePaymentDao.saveCustomerBanks(lstCustomerBank);
				status = Boolean.TRUE;
			}
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found to save customer details");
		}

		return status;
	}

	/* 
	 * @param   :fetch staff credentials details for validation
	 * @return StaffAuthorizationView
	 */
	public List<StaffAuthorizationView> fetchStaffDetailsForValidation(BigDecimal countryBranchCode) {
		List<StaffAuthorizationView> lstStaffAuth = branchRemittancePaymentDao.fetchStaffDetailsForValidation(countryBranchCode);
		if(lstStaffAuth != null && lstStaffAuth.size() != 0) {
			// continue
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found to staff validation details");
		}
		return lstStaffAuth;
	}

	/* 
	 * @param   : validate staff user name and password
	 * @return True or False
	 */
	public Boolean validationStaffCredentials(String userName,String password,BigDecimal countryBranchCode) {
		Boolean validStatus = Boolean.FALSE;
		BigDecimal validate = branchRemittancePaymentDao.validationStaffCredentials(userName,password,countryBranchCode);
		if(validate != null && validate.compareTo(BigDecimal.ZERO) != 0) {
			validStatus = Boolean.TRUE;
		}else {
			validStatus = Boolean.FALSE;
		}
		return validStatus;
	}

	

	public Boolean deActivateOnlineApplication() {
		try {
			appRepository.deActivateNotUsedOnlineApplication(new Customer(metaData.getCustomerId()),ConstantDocument.ONLINE_BRANCH_LOC_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("De-Activate Application failed for customer:" + metaData.getCustomerId());
		}
		return true;
	}
	
	
	
	
	
	public Boolean deActivateBranchApplicationInOnline() {
		try {
			appRepository.deActivateBranchApplicationInOnline(new Customer(metaData.getCustomerId()),ConstantDocument.ONLINE_BRANCH_LOC_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("De-Activate Application failed for customer:" + metaData.getCustomerId());
		}
		return true;
	}
	
	
	public ConfigDto getLimitCheck(BigDecimal customerId) {
		ConfigDto config = new ConfigDto();
		logger.debug("Customer id is{}",customerId);
		String accMonthYear =DateUtil.getCurrentAccMMYear();
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("P_CUSTOMER_ID", metaData.getCustomerId());
		inputValues.put("P_ACMMYY", accMonthYear);
		logger.debug("Country id is {}{}{}",metaData.getCountryId(),metaData.getCompanyId(),metaData.getCustomerId());
		Customer customer = customerRepos.getCustomerByCountryAndCompAndCustoemrId(metaData.getCountryId(),metaData.getCompanyId(),metaData.getCustomerId());
		logger.debug("Customer data is {}",customer.toString());
		BigDecimal idType =null==customer.getIdentityTypeId()?BigDecimal.ZERO:customer.getIdentityTypeId();
		inputValues.put("ID_TYPE", idType);
		
		Map<String,Object> map =routingProDao.limitCheck(inputValues);
		Map<String,Object> mapRemit=routingProDao.todayRemitAmount(inputValues);
		Map<String,Object> mapReceipt=routingProDao.todayReceiptAmount(inputValues);
		//Map<String,Object> mapMisAmount=routingProDao.todayMisReceAmount(inputValues);
		BigDecimal todayTrnxLimit = BigDecimal.ZERO;
		if(map!=null) {
			config.setCashLimit(map.get("W_CB_LIMIT")==null?BigDecimal.ZERO:(BigDecimal)map.get("W_CB_LIMIT"));
			config.setGccLimit(map.get("W_GCC_CARD_LIMIT")==null?BigDecimal.ZERO:(BigDecimal)map.get("W_GCC_CARD_LIMIT"));
			config.setPassportLimit(map.get("W_PASSPORT_LIMIT")==null?BigDecimal.ZERO:(BigDecimal)map.get("W_PASSPORT_LIMIT"));
		}
		if(mapRemit!=null){
			todayTrnxLimit = mapRemit.get("REMIT_AMT")==null?BigDecimal.ZERO:(BigDecimal)mapRemit.get("REMIT_AMT");
		}
		if(mapReceipt!=null){
			todayTrnxLimit = todayTrnxLimit.add(mapReceipt.get("REMIT_AMT")==null?BigDecimal.ZERO:(BigDecimal)mapReceipt.get("REMIT_AMT"));
		}
		
		/*if(mapMisAmount!=null){
			todayTrnxLimit = todayTrnxLimit.add(mapMisAmount.get("REMIT_AMT")==null?BigDecimal.ZERO:(BigDecimal)mapMisAmount.get("REMIT_AMT"));
		}
		*/
		config.setTodayTrnxAmount(todayTrnxLimit);
		return config;
	}
	
	/** added by Rabil on 5th Oct 2019**/
	private String getPurposeOfTrnx(BigDecimal remitApplId) {
		String purTrnx = null;
		RemittanceApplication remitAppl = new RemittanceApplication();
		remitAppl.setRemittanceApplicationId(remitApplId);
		AdditionalInstructionData addlInsData = addlInstDataRepo.findByExRemittanceApplicationAndFlexField(remitAppl, ConstantDocument.INDIC1);
		if(addlInsData!=null) {
			purTrnx = addlInsData.getFlexFieldValue();
		}
		return purTrnx;
	}

	// Direct Payment link application now getting converted to remittance so commenting the following code
	@Deprecated
	private PaymentLinkAppDto createPaymentLinkAppDto(BigDecimal remittanceApplicationId,PaygDetailsModel paymentLinkModel) {
		ShoppingCartDetails shoppingCartDetails=iShoppingCartDetailsRepository.findByCustomerIdAndRemittanceApplicationId(metaData.getCustomerId(), remittanceApplicationId); 
		PaymentLinkAppDto paymentLinkAppDto = new PaymentLinkAppDto();
		paymentLinkAppDto.setRemittanceApplicationId(remittanceApplicationId);
		paymentLinkAppDto.setLinkDate(paymentLinkModel.getLinkDate());
		paymentLinkAppDto.setCustomerId(metaData.getCustomerId());
		paymentLinkAppDto.setAmount(shoppingCartDetails.getLocalNextTranxAmount());
		paymentLinkAppDto.setForeignAmount(shoppingCartDetails.getForeignTranxAmount());
		paymentLinkAppDto.setExchangeRate(shoppingCartDetails.getExchangeRateApplied());
		paymentLinkAppDto.setForeignCurrencyDesc(shoppingCartDetails.getForeignCurrencyDesc());
		return paymentLinkAppDto;
		
		
	}
	
	// Clear cart for PB
	private void clearCartForPB(List<ShoppingCartDetails> shoppingCartDetailsList) {
		
		
		Iterator<ShoppingCartDetails> iter = shoppingCartDetailsList.iterator();
		
		while (iter.hasNext()) {
			ShoppingCartDetails shoppingCartDetails = iter.next();
			RemittanceApplication remittanceApplication = appRepository.findOne(shoppingCartDetails.getApplicationId());
			CountryBranchMdlv1 countryBranchMdlv1 = countryBranchRepository
					.findByCountryBranchId(metaData.getCountryBranchId());
			if (countryBranchMdlv1.getBranchId().equals(ConstantDocument.ONLINE_BRANCH_LOC_CODE)
					&& remittanceApplication!=null 
					&& remittanceApplication.getPaymentType()!=null
					&& ConstantDocument.PB_PAYMENT.equals(remittanceApplication.getPaymentType())
					&& ConstantDocument.PB_STATUS_NEW.equals(remittanceApplication.getWtStatus())) {
				iter.remove();
			}

		}
		
	}

}
