package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.BankCharges;
import com.amx.jax.pricer.dbmodel.BankServiceRule;
import com.amx.jax.pricer.dbmodel.BenificiaryListView;
import com.amx.jax.pricer.dbmodel.CountryMasterModel;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.CustomerDetailsView;
import com.amx.jax.pricer.dbmodel.ParameterDetailsModel;
import com.amx.jax.pricer.dbmodel.ServiceProviderRateView;
import com.amx.jax.pricer.repository.CountryMasterRepository;
import com.amx.jax.pricer.repository.CurrencyMasterRepository;
import com.amx.jax.pricer.repository.IBankChargesRepository;
import com.amx.jax.pricer.repository.IBankServiceRuleRepository;
import com.amx.jax.pricer.repository.IBeneficiaryViewRepository;
import com.amx.jax.pricer.repository.ICustomerViewRepository;
import com.amx.jax.pricer.repository.IParameterDetailsRespository;
import com.amx.jax.pricer.repository.IServiceProviderMarginRepository;
import com.amx.jax.pricer.repository.IUsdExchangeRateRepository;

@Component
public class PartnerServiceDao {

	@Autowired
	IBeneficiaryViewRepository beneficiaryViewRepository;

	@Autowired
	ICustomerViewRepository customerViewRepository;

	@Autowired
	IUsdExchangeRateRepository usdExchangeRateRepository;

	@Autowired
	CurrencyMasterRepository currencyMasterRepository;

	@Autowired
	IServiceProviderMarginRepository serviceProviderMarginRepository;

	@Autowired
	IBankServiceRuleRepository bankServiceRuleRepository;

	@Autowired
	IBankChargesRepository bankChargesRepository;

	@Autowired
	IParameterDetailsRespository parameterDetailsRespository;

	@Autowired
	CountryMasterRepository countryMasterRepository;

	public BenificiaryListView getBeneficiaryDetails(BigDecimal customerId, BigDecimal beneficiaryRelationShipSeqId) {
		return beneficiaryViewRepository.findByCustomerIdAndBeneficiaryRelationShipSeqId(customerId,
				beneficiaryRelationShipSeqId);
	}

	public CustomerDetailsView getCustomerDetails(BigDecimal customerId) {
		return customerViewRepository.findByCustomerId(customerId);
	}

	public BigDecimal fetchUsdExchangeRate() {
		return usdExchangeRateRepository.fetchUsdExchangeRate();
	}

	public List<CurrencyMasterModel> fetchCurrencyMasterDetails(String currencyCode, String isActive) {
		return currencyMasterRepository.findByIsoCurrencyCodeAndIsactive(currencyCode, isActive);
	}

	public ServiceProviderRateView fetchMarginByProduct(BigDecimal countryId, BigDecimal bankId, BigDecimal currencyId,
			BigDecimal remittanceId, BigDecimal deliveryId) {
		return serviceProviderMarginRepository.fetchMerginByProduct(countryId, bankId, currencyId, remittanceId,
				deliveryId);
	}

	public List<BankServiceRule> fetchBankServiceRuleDetails(BigDecimal countryId, BigDecimal bankId,
			BigDecimal currencyId, BigDecimal remittanceId, BigDecimal deliveryId) {
		return bankServiceRuleRepository.fetchBankServiceRule(countryId, bankId, currencyId, remittanceId, deliveryId);
	}

	public List<BankCharges> fetchBankChargesDetails(BigDecimal bankServiceRuleId, BigDecimal fcAmount,
			BigDecimal chargesFor, String chargesType) {
		return bankChargesRepository.fetchBankCharges(bankServiceRuleId, fcAmount, chargesFor, chargesType);
	}

	public ParameterDetailsModel fetchServPrvBankCode(String recordId, String beneCountryCode) {
		return parameterDetailsRespository.fetchServPrvBankCode(recordId, beneCountryCode);
	}

	public CountryMasterModel fetchCountryMasterDetails(BigDecimal countryId) {
		return countryMasterRepository.findByCountryId(countryId);
	}

	public BigDecimal fetchServiceProviderRefernceNum() {
		return usdExchangeRateRepository.fetchServiceProviderRefernceNum();
	}

}
