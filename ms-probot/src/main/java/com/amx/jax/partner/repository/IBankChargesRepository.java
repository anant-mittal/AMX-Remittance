package com.amx.jax.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.partner.dbmodel.BankCharges;

public interface IBankChargesRepository extends CrudRepository<BankCharges, Serializable> {
	
	@Query(value = "SELECT * FROM EX_BANK_CHARGES WHERE BANK_SERVICE_RULE_ID=?1 AND FROM_AMOUNT<=?2 AND TO_AMOUNT>=?2 AND CHARGES_FOR=?3 AND CHARGES_TYPE=?4 AND ISACTIVE = 'Y'", nativeQuery = true)
	public List<BankCharges> fetchBankCharges(BigDecimal bankServiceRuleId,BigDecimal fcAmount,BigDecimal chargesFor,String chargesType);

}
