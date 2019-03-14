package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.remittance.BeneficiaryAccountException;

public interface BeneficiaryAccountExceptRepository extends JpaRepository<BeneficiaryAccountException, Serializable>{

	@Query("select ba from BeneficiaryAccountException ba where ba.bankCountryId=:bankcountryid and ba.bankId =:bankid and ba.beneAccountNumber=:beneaccountnumber and ba.isActive =:isactive")
	public List<BeneficiaryAccountException> getBeneAccountExceptionList(@Param("bankcountryid") BigDecimal bankcountryid,@Param("bankid") BigDecimal bankid,@Param("beneaccountnumber") String beneaccountnumber,@Param("isactive") String isactive);
}


