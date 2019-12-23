package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.BanksView;

public interface IBankMasterFromViewDao extends JpaRepository<BanksView, Serializable>{

	
	@Query("select bm from BanksView bm where bm.bankId=?1")
	public List<BanksView> getBankListByBankId(BigDecimal bankId);
	
	@Query("select bm from BanksView bm where bm.bankId=:beneBankId and bm.bankCountryId=:beneBankCountryId")
	public List<BanksView> getBankListByBeneBankIdAndCountry(@Param("beneBankId") BigDecimal beneBankId,@Param("beneBankCountryId")BigDecimal beneBankCountryId);
	
	public BanksView findByBankId(BigDecimal bankId);
}
