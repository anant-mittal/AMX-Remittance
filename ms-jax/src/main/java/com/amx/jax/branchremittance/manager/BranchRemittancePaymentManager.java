package com.amx.jax.branchremittance.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.BranchRemittancePaymentDao;
import com.amx.jax.dbmodel.BanksView;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.CurrencyWiseDenomination;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.remittance.CustomerBank;
import com.amx.jax.dbmodel.remittance.LocalBankDetailsView;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.dbmodel.remittance.StaffAuthorizationView;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.FcSaleBranchOrderManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.response.fx.FxEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FxExchangeRateBreakup;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.CustomerShoppingCartDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
import com.amx.jax.repository.IBankMasterFromViewDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.util.RoundUtil;

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
		
		
		List<CustomerShoppingCartDto> lstCustShpcrt = new ArrayList<>();
		FxExchangeRateBreakup breakup = new FxExchangeRateBreakup();
		BigDecimal decimalNumber = BigDecimal.ZERO;
		CurrencyMasterModel currencyMaster = currencyMasterService.getCurrencyMasterById(localCurrencyId);
		if(currencyMaster != null) {
			breakup.setLcDecimalNumber(currencyMaster.getDecinalNumber() == null ? decimalNumber : currencyMaster.getDecinalNumber());
			Customer customer = customerRepos.getCustomerByCountryAndCompAndCustoemrId(metaData.getCountryId(),metaData.getCompanyId(),customerId);
			
			if(customer!=null) {
				totalCustomerLoyaltyPoits = customer.getLoyaltyPoints()==null?BigDecimal.ZERO:customer.getLoyaltyPoints();
			}
			
			List<ShoppingCartDetails> lstCustomerShopping = branchRemittancePaymentDao.fetchCustomerShoppingCart(customerId);
			if(lstCustomerShopping != null && lstCustomerShopping.size() != 0) {
				for (ShoppingCartDetails customerApplDto : lstCustomerShopping) {
					BigDecimal fcCurrencyId = customerApplDto.getForeignCurrency();
					totalLocalAmount = totalLocalAmount.add(customerApplDto.getLocalTranxAmount());
					totalNetAmount   =totalNetAmount.add(customerApplDto.getLocalNextTranxAmount());
					totalTrnxFees    =totalTrnxFees.add(customerApplDto.getLocalCommisionAmount());
					totalLyltyPointAmt =totalLyltyPointAmt.add(customerApplDto.getLoyaltsPointencahsed()==null?BigDecimal.ZERO:customerApplDto.getLoyaltsPointencahsed());
					
					if(customerApplDto.getLoyaltsPointIndicator()!=null && customerApplDto.getLoyaltsPointIndicator().equalsIgnoreCase(ConstantDocument.Yes) && totalCustomerLoyaltyPoits.compareTo(new BigDecimal(1000))>=0) {
						totalCustomerLoyaltyPoits = totalCustomerLoyaltyPoits.subtract(new BigDecimal(1000));
					}
					
					cartList.setTotalLocalAmount(totalLocalAmount);
					cartList.setTotalNetAmount(totalNetAmount);//.subtract(totalLyltyPointAmt));
					cartList.setTotalTrnxFees(totalTrnxFees);
					cartList.setTotalLyltyPointAmt(totalLyltyPointAmt);
					cartList.setTotalLoyaltyPointAvaliable(totalCustomerLoyaltyPoits);
					cartList.setTotalNetCollectionAmount(totalNetAmount.subtract(totalLyltyPointAmt));
					
					if(fcCurrencyId == null || fcCurrencyId.compareTo(BigDecimal.ZERO) == 0){
						throw new GlobalException(JaxError.NULL_CURRENCY_ID, "Null foreign currency id passed");
					}
					currencyMaster = currencyMasterService.getCurrencyMasterById(fcCurrencyId);
					if(currencyMaster != null) {
						breakup.setFcDecimalNumber(currencyMaster.getDecinalNumber() == null ? decimalNumber : currencyMaster.getDecinalNumber());
						lstCustShpcrt.add(createCustomerShoppingCartDto(customerApplDto,localCurrencyId,fcCurrencyId,breakup));
						cartList.setShoppingCartDetails(lstCustShpcrt);
						
					}else {
						throw new GlobalException(JaxError.INVALID_CURRENCY_ID, "Invalid foreign currency id passed");
					}
				}
			}else {
				cartList.setTotalLoyaltyPointAvaliable(totalCustomerLoyaltyPoits);
			}
		}else {
			throw new GlobalException(JaxError.INVALID_CURRENCY_ID, "Invalid local currency id passed");
		}

		return cartList;
	}

	private CustomerShoppingCartDto createCustomerShoppingCartDto(ShoppingCartDetails shoppingCartDetails,BigDecimal localCurrencyId,BigDecimal fcCurrencyId,FxExchangeRateBreakup breakup) {

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
		shoppingCartDataTableBean.setRoutingBank(bankMaster.getBankListByBankId(shoppingCartDetails.getRoutingBankId()).get(0).getBankFullName());
		shoppingCartDataTableBean.setBeneRelationseqId(shoppingCartDetails.getBeneRelationseqId());
		shoppingCartDataTableBean.setSourceOfIncomeId(shoppingCartDetails.getSourceofincome()==null?BigDecimal.ZERO:new BigDecimal(shoppingCartDetails.getSourceofincome()));
		shoppingCartDataTableBean.setDomXRate(RoundUtil.roundBigDecimal(BigDecimal.ONE.divide(shoppingCartDetails.getExchangeRateApplied(),10,RoundingMode.HALF_UP),breakup.getFcDecimalNumber().intValue()));
		shoppingCartDataTableBean.setCustomerSignatureString(shoppingCartDetails.getCustomerSignatureClob());
		
	
		
		
		return shoppingCartDataTableBean;
	}

	/* 
	 * @param   :fetch mode of payment
	 * @return PaymentModeOfPaymentDto
	 */
	public List<PaymentModeOfPaymentDto> fetchModeOfPayment(BigDecimal languageId){
		List<PaymentModeOfPaymentDto> lstModeofPayment = new ArrayList<>();
		List<Object[]> lstPayment = branchRemittancePaymentDao.fetchModeOfPayment(languageId);
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
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found for mode of payments");
		}

		return lstModeofPayment;
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
	List<Object[]> custBankName = branchRemittancePaymentDao.fetchCustomerBankNames(customerId,bankId);
	if (custBankName != null && custBankName.size() != 0) {
		for (Object object : custBankName) {
			Object[] custBankNameObject = (Object[]) object;
			if(custBankNameObject[0]!=null) {
				List<String> nameList = new ArrayList<>();
				nameList.add(custBankNameObject[0].toString());
				customerBankDetailsDto.setCustomerNames(nameList);
			}
			if(custBankNameObject[1]!=null) {
				List<BigDecimal> relationList = new ArrayList<>();
				relationList.add(new BigDecimal(custBankNameObject[1].toString()));
				customerBankDetailsDto.setRelationId(relationList);
			}
			
		}
	}
	
	
	//customerBankDetailsDto.setCustomerNames(new ArrayList(custBankName.get(0).));
	//lstCustBanksDto.add(customerBankDetailsDto);
	
	
	return customerBankDetailsDto;
	//return lstCustDetails;
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
		List<CurrencyWiseDenomination> lstCurrencyDenomination = branchRemittancePaymentDao.fetchCurrencyDenomination(currencyId, ConstantDocument.Yes);
		if(lstCurrencyDenomination != null && lstCurrencyDenomination.size() != 0) {
			for (CurrencyWiseDenomination currencyWiseDenomination : lstCurrencyDenomination) {
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
	public Boolean saveCustomerBankDetails(List<CustomerBankRequest> customerBank,BigDecimal appcountryId,BigDecimal employeeId,BigDecimal customerId,BigDecimal companyId) {
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

		if(customerBank != null && customerBank.size() != 0) {
			for (CustomerBankRequest customerBankRequest : customerBank) {
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

}
