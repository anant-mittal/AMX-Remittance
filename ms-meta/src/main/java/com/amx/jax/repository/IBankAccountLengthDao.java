package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.BankAccountLength;

public interface IBankAccountLengthDao extends JpaRepository<BankAccountLength, Serializable>{

	
	@Query("select bal from BankAccountLength bal where bal.bankId=?1")
	public List<BankAccountLength> getBankAccountLengthByBankId(BigDecimal bankId);
}
