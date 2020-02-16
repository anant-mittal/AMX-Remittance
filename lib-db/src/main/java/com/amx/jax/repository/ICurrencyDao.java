package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
/**
 * 
 * @author :Rabil
 *
 */

public interface ICurrencyDao extends JpaRepository<CurrencyMasterMdlv1, Serializable>{

	
	@Query("select cm from CurrencyMasterMdlv1 cm where cm.currencyId =?1 and cm.isactive='Y'")
	public List<CurrencyMasterMdlv1> getCurrencyList(BigDecimal currencyId);
	
	@Query("select cm from CurrencyMasterMdlv1 cm where cm.countryId =?1 and cm.isactive='Y'")
	public List<CurrencyMasterMdlv1> getCurrencyListByCountryId(BigDecimal countryId);
	
	public List<CurrencyMasterMdlv1> findByisactive(String isActive);
	
	@Query("select cm from CurrencyMasterMdlv1 cm where cm.countryId <>:countryid and cm.isactive ='Y' and cm.isAllowFcSale='Y'")
	public List<CurrencyMasterMdlv1> getfcCurrencyList(@Param("countryid") BigDecimal countryid);
	
}

