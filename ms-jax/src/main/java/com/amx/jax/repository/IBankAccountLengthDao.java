package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.bene.BankAccountLength;

public interface IBankAccountLengthDao extends JpaRepository<BankAccountLength, Serializable>{

	@Query("select bal from BankAccountLength bal where bal.bankMasterId=?1 and bal.recordStatus='Y' and bal.acLength <>0")
	public List<BankAccountLength> getBankAccountLength(BigDecimal bankId);
}
