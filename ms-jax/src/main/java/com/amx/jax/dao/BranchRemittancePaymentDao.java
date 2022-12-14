package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.CurrencyWiseDenominationMdlv1;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.remittance.CustomerBank;
import com.amx.jax.dbmodel.remittance.LocalBankDetailsView;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.dbmodel.remittance.StaffAuthorizationView;
import com.amx.jax.repository.CurrencyWiseDenominationRepository;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IShoppingCartDetailsRepository;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.jax.repository.PaymentModeRepository;
import com.amx.jax.repository.remittance.CustomerBankRepository;
import com.amx.jax.repository.remittance.LocalBankDetailsRepository;
import com.amx.jax.repository.remittance.StaffAuthorizationRepository;

@Component
public class BranchRemittancePaymentDao {
	
	@Autowired
	IShoppingCartDetailsRepository shoppingCartDetailsRepository;
	
	@Autowired
	PaymentModeRepository paymentModeRepository;
	
	@Autowired
	LocalBankDetailsRepository localBankDetailsRepository;
	
	@Autowired
	ParameterDetailsRespository parameterDetailsRespository;
	
	@Autowired
	CurrencyWiseDenominationRepository currencyWiseDenominationRepository;
	
	@Autowired
	ICustomerRepository customerRepository;
	
	@Autowired
	CustomerBankRepository customerBankRepository;
	
	@Autowired
	StaffAuthorizationRepository staffAuthorizationRepository;
	
	public List<ShoppingCartDetails> fetchCustomerShoppingCart(BigDecimal customerId){
		return shoppingCartDetailsRepository.findByCustomerId(customerId);
	}
	
	public List<Object[]> fetchModeOfPayment(BigDecimal languageId){
		return paymentModeRepository.fetchModeOfPayment(languageId);
	}
	
	public List<LocalBankDetailsView> fetchLocalBanks(BigDecimal countryId){
		return localBankDetailsRepository.findByApplicationCountryId(countryId);
	}
	
	public List<LocalBankDetailsView> fetchCustomerLocalBanks(BigDecimal customerId){
		return localBankDetailsRepository.fetchCustomerBankDetails(customerId);
	}
	
	public List<ParameterDetails> fetchParameterDetails(String recordId,String isactive){
		return parameterDetailsRespository.findByRecordIdAndIsActive(recordId, isactive);
	}
	
	public List<CurrencyWiseDenominationMdlv1> fetchCurrencyDenomination(BigDecimal currencyId,String isActive){
		return currencyWiseDenominationRepository.fetchCurrencyDenomination(currencyId, isActive);
	}
	
	public List<Customer> fetchCustomerByCustomerId(BigDecimal appcountryId,BigDecimal companyId,BigDecimal customerId){
		return customerRepository.getCustomerByCustomerId(appcountryId, companyId, customerId);
	}
	
	public void saveCustomerBanks(List<CustomerBank> customerBank){
		customerBankRepository.save(customerBank);
	}
	
	/*public List<String> fetchCustomerBankNames(BigDecimal customerId,BigDecimal bankId){
		return localBankDetailsRepository.fetchCustomerBankNames(customerId,bankId);
	}
	*/
	
	public List<Object[]> fetchCustomerBankNames(BigDecimal customerId,BigDecimal bankId){
	return localBankDetailsRepository.fetchCustomerBankNames(customerId,bankId);
}

	
	public List<StaffAuthorizationView> fetchStaffDetailsForValidation(BigDecimal countryBranchCode){
		return staffAuthorizationRepository.fetchStaffAuthorization();
	}
	
	public BigDecimal validationStaffCredentials(String userName,String password,BigDecimal countryBranchCode){
		return staffAuthorizationRepository.validationStaffCredentials(userName,password);
	}

}
