package com.amx.jax.partner.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.AccountTypeFromViewModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CustomerDetailsView;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.partner.BankExternalReferenceDetail;
import com.amx.jax.dbmodel.partner.BankExternalReferenceHead;
import com.amx.jax.dbmodel.partner.PaymentModeLimitsView;
import com.amx.jax.dbmodel.partner.TransactionDetailsView;
import com.amx.jax.partner.repository.IBankExternalReferenceDetailsRepository;
import com.amx.jax.partner.repository.IBankExternalReferenceHeadRepository;
import com.amx.jax.partner.repository.IPaymentModeLimitsRepository;
import com.amx.jax.partner.repository.ITransactionSPDetailsRepository;
import com.amx.jax.repository.CountryMasterRepository;
import com.amx.jax.repository.IAccountTypeFromViewDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICustomerViewRepository;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.jax.repository.remittance.IUsdExchangeRateRepository;

@Component
public class PartnerTransactionDao {
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryViewRepository;
	
	@Autowired
	ICustomerViewRepository customerViewRepository;
	
	@Autowired
	CountryMasterRepository countryMasterRepository;
	
	@Autowired
	IAccountTypeFromViewDao accountTypeRepository;
	
	@Autowired
	ParameterDetailsRespository parameterDetailsRespository;
	
	@Autowired
	ITransactionSPDetailsRepository transactionSPDetailsRepository;
	
	@Autowired
	IBankExternalReferenceHeadRepository bankExternalReferenceHeadRepository;
	
	@Autowired
	IBankExternalReferenceDetailsRepository bankExternalReferenceDetailsRepository;
	
	@Autowired
	IPaymentModeLimitsRepository paymentModeLimitsRepository;
	
	@Autowired
	IUsdExchangeRateRepository usdExchangeRateRepository;
	
	public BenificiaryListView getBeneficiaryDetails(BigDecimal customerId,BigDecimal beneficiaryRelationShipSeqId) {
		return beneficiaryViewRepository.findByCustomerIdAndBeneficiaryRelationShipSeqId(customerId, beneficiaryRelationShipSeqId);
	}
	
	public CustomerDetailsView getCustomerDetails(BigDecimal customerId) {
		return customerViewRepository.findByCustomerId(customerId);
	}
	
	public CountryMaster getCountryMasterDetails(BigDecimal countryId) {
		return countryMasterRepository.findOne(countryId);
	}
	
	public AccountTypeFromViewModel getAccountTypeDetails(BigDecimal additionalAmiecId) {
		return accountTypeRepository.findByAdditionalAmiecId(additionalAmiecId);
	}
	
	public ParameterDetails fetchServPrvBankCode(String recordId,String beneCountryCode){
		return parameterDetailsRespository.fetchServPrvBankCode(recordId, beneCountryCode);
	}
	
	public ParameterDetails fetchBeneCountryBeneAddressNotReq(String recordId,String beneCountryCode){
		return parameterDetailsRespository.fetchBeneCountryBeneAddressNotReq(recordId, beneCountryCode);
	}
	
	public List<TransactionDetailsView> fetchTrnxSPDetails(BigDecimal customerId,BigDecimal collectionDocYear,BigDecimal collectionDocNumber){
		return transactionSPDetailsRepository.fetchTrnxSPDetails(customerId,collectionDocYear,collectionDocNumber);
	}
	
	public List<TransactionDetailsView> fetchTrnxWiseDetails(BigDecimal customerId,BigDecimal docYear,BigDecimal docNumber){
		return transactionSPDetailsRepository.fetchTrnxWiseDetails(customerId,docYear,docNumber);
	}
	
	public List<BankExternalReferenceHead> fetchBankExternalReferenceHeadDetails(BigDecimal countryId,BigDecimal corBankId,BigDecimal beneBankId){
		return bankExternalReferenceHeadRepository.fetchBankExternalHeaderDetails(countryId,corBankId,beneBankId);
	}
	
	public List<BankExternalReferenceDetail> fetchBankExternalReferenceBranchDetails(BigDecimal countryId,BigDecimal corBankId,BigDecimal beneBankId,BigDecimal beneBankBranchId){
		return bankExternalReferenceDetailsRepository.fetchBankExternalBranchDetails(countryId, corBankId, beneBankId, beneBankBranchId);
	}
	
	public List<PaymentModeLimitsView> fetchPaymentLimitDetails(BigDecimal bankId,BigDecimal currencyId,String customerTypeFrom,String customerTypeTo){ 
		return paymentModeLimitsRepository.fetchPaymentLimitDetails(bankId, currencyId, customerTypeFrom, customerTypeTo);
	}
	
	public BigDecimal fetchUsdExchangeRate() {
		return usdExchangeRateRepository.fetchUsdExchangeRate();
	}
	
}
