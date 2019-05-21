package com.amx.jax.pricer.partner.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.pricer.dbmodel.CountryMasterDT;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.partner.dbmodel.BankCharges;
import com.amx.jax.pricer.partner.dbmodel.BankServiceRule;
import com.amx.jax.pricer.partner.dbmodel.BenificiaryListView;
import com.amx.jax.pricer.partner.dbmodel.CustomerDetailsView;
import com.amx.jax.pricer.partner.dbmodel.ServiceProviderRateView;
import com.amx.jax.pricer.partner.repository.IBankChargesRepository;
import com.amx.jax.pricer.partner.repository.IBankServiceRuleRepository;
import com.amx.jax.pricer.partner.repository.IBeneficiaryViewRepository;
import com.amx.jax.pricer.partner.repository.ICountryMasterRepository;
import com.amx.jax.pricer.partner.repository.ICustomerViewRepository;
import com.amx.jax.pricer.partner.repository.IServiceProviderMarginRepository;
import com.amx.jax.pricer.partner.repository.IUsdExchangeRateRepository;
import com.amx.jax.pricer.repository.CurrencyMasterRepository;

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
	
	/*@Autowired
	ParameterDetailsRespository parameterDetailsRespository;*/
	
	@Autowired
	ICountryMasterRepository countryMasterRepository;
	
	public BenificiaryListView getBeneficiaryDetails(BigDecimal customerId,BigDecimal beneficiaryRelationShipSeqId) {
		return beneficiaryViewRepository.findByCustomerIdAndBeneficiaryRelationShipSeqId(customerId, beneficiaryRelationShipSeqId);
	}
	
	public CustomerDetailsView getCustomerDetails(BigDecimal customerId) {
		return customerViewRepository.findByCustomerId(customerId);
	}
	
	public BigDecimal fetchUsdExchangeRate() {
		return usdExchangeRateRepository.fetchUsdExchangeRate();
	}
	
	public List<CurrencyMasterModel> fetchCurrencyMasterDetails(String currencyCode,String isActive) {
		return currencyMasterRepository.findByIsoCurrencyCodeAndIsactive(currencyCode, isActive);
	}
	
	public ServiceProviderRateView fetchMarginByProduct(BigDecimal countryId,BigDecimal bankId,BigDecimal currencyId,BigDecimal remittanceId,BigDecimal deliveryId) {
		return serviceProviderMarginRepository.fetchMerginByProduct(countryId, bankId, currencyId, remittanceId, deliveryId);
	}
	
	public List<BankServiceRule> fetchBankServiceRuleDetails(BigDecimal countryId,BigDecimal bankId,BigDecimal currencyId,BigDecimal remittanceId,BigDecimal deliveryId){
		return bankServiceRuleRepository.fetchBankServiceRule(countryId, bankId, currencyId, remittanceId, deliveryId);
	}
	
	public List<BankCharges> fetchBankChargesDetails(BigDecimal bankServiceRuleId,BigDecimal fcAmount,BigDecimal chargesFor,String chargesType){
		return bankChargesRepository.fetchBankCharges(bankServiceRuleId, fcAmount, chargesFor, chargesType);
	}
	
	public ParameterDetails fetchServPrvBankCode(String recordId,String beneCountryCode){
		return null;//parameterDetailsRespository.fetchServPrvBankCode(recordId, beneCountryCode);
	}
	
	public CountryMasterDT fetchCountryMasterDetails(BigDecimal countryId) {
		return countryMasterRepository.findByCountryId(countryId);
	}
	
	public BigDecimal fetchServiceProviderRefernceNum() {
		return usdExchangeRateRepository.fetchServiceProviderRefernceNum();
	}
}
