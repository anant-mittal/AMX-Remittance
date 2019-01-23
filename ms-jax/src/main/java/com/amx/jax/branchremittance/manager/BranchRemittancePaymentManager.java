package com.amx.jax.branchremittance.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.BranchRemittancePaymentDao;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.CurrencyWiseDenomination;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.remittance.CustomerBank;
import com.amx.jax.dbmodel.remittance.LocalBankDetailsView;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.FcSaleBranchOrderManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.response.fx.FxEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FxExchangeRateBreakup;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.CustomerShoppingCartDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
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

	/* 
	 * @param   :fetch customer shopping cart application
	 * @return CustomerShoppingCartDto
	 */
	public List<CustomerShoppingCartDto> fetchCustomerShoppingCart(BigDecimal customerId,BigDecimal localCurrencyId){
		List<CustomerShoppingCartDto> lstCustShpcrt = new ArrayList<>();
		FxExchangeRateBreakup breakup = new FxExchangeRateBreakup();
		BigDecimal decimalNumber = BigDecimal.ZERO;
		CurrencyMasterModel currencyMaster = currencyMasterService.getCurrencyMasterById(localCurrencyId);
		if(currencyMaster != null) {
			breakup.setLcDecimalNumber(currencyMaster.getDecinalNumber() == null ? decimalNumber : currencyMaster.getDecinalNumber());
			List<ShoppingCartDetails> lstCustomerShopping = branchRemittancePaymentDao.fetchCustomerShoppingCart(customerId);
			if(lstCustomerShopping != null && lstCustomerShopping.size() != 0) {
				for (ShoppingCartDetails customerApplDto : lstCustomerShopping) {
					BigDecimal fcCurrencyId = customerApplDto.getForeignCurrency();
					if(fcCurrencyId == null || fcCurrencyId.compareTo(BigDecimal.ZERO) == 0){
						throw new GlobalException(JaxError.NULL_CURRENCY_ID, "Null foreign currency id passed");
					}
					currencyMaster = currencyMasterService.getCurrencyMasterById(fcCurrencyId);
					if(currencyMaster != null) {
						breakup.setFcDecimalNumber(currencyMaster.getDecinalNumber() == null ? decimalNumber : currencyMaster.getDecinalNumber());
						lstCustShpcrt.add(createCustomerShoppingCartDto(customerApplDto,localCurrencyId,fcCurrencyId,breakup));
					}else {
						throw new GlobalException(JaxError.INVALID_CURRENCY_ID, "Invalid foreign currency id passed");
					}
				}
			}else {
				throw new GlobalException(JaxError.NO_RECORD_FOUND, "No application records found for customer");
			}
		}else {
			throw new GlobalException(JaxError.INVALID_CURRENCY_ID, "Invalid local currency id passed");
		}

		return lstCustShpcrt;
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
						lstModePayment.setPaymentModeId(new BigDecimal(paymentModes[0].toString()));
					}
					if(paymentModes[1] != null) {
						lstModePayment.setPaymentModeDescId(new BigDecimal(paymentModes[1].toString()));
					}
					if(paymentModes[2] != null) {
						lstModePayment.setPaymentModeCode(paymentModes[2].toString());
					}
					if(paymentModes[3] != null) {
						lstModePayment.setPaymentModeName(paymentModes[3].toString());
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
					customerBankDetailsDto.setLocalBankDetailsDto(localBankDetailsDto);
					List<String> custBankName = fetchCustomerNames(customerId, localBankDetailsDto.getChequeBankId());
					customerBankDetailsDto.setCustomerNames(custBankName);
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
	
	public List<String> fetchCustomerNames(BigDecimal customerId,BigDecimal bankId){
		List<String> lstCustDetails = branchRemittancePaymentDao.fetchCustomerBankNames(customerId,bankId);
		return lstCustDetails;
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

}
