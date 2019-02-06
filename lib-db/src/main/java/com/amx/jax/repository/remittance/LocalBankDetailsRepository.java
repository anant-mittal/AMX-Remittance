package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.LocalBankDetailsView;

public interface LocalBankDetailsRepository extends CrudRepository<LocalBankDetailsView, Serializable>{
	
	public List<LocalBankDetailsView> findByApplicationCountryId(BigDecimal countryId);
	
	@Query(value = "Select Distinct A.* from V_EX_CBNK A, Ex_Customer_Bank B where A.Cheque_Bank_Id = B.Bank_Id And B.Customer_Id = ?1 And B.Isactive = 'Y'", nativeQuery = true)
	public List<LocalBankDetailsView> fetchCustomerBankDetails(BigDecimal customerId);
	
	@Query(value = "Select Distinct(Debit_Card_Name) from Ex_Customer_Bank where Customer_Id = ?1 and Bank_Id = ?2 and Isactive = 'Y'", nativeQuery = true)
	public List<String> fetchCustomerBankNames(BigDecimal customerId,BigDecimal bankId);

	public LocalBankDetailsView findByChequeBankCode(String chequeBankCode);

}
