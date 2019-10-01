package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.CurrencyMasterModel;
/**
 * 
 * @author :Rabil
 *
 */

public interface ICurrencyDao extends JpaRepository<CurrencyMasterModel, Serializable>{

	
	@Query("select cm from CurrencyMasterModel cm where cm.currencyId =?1 and cm.isactive='Y'")
	public List<CurrencyMasterModel> getCurrencyList(BigDecimal currencyId);
	
	@Query("select cm from CurrencyMasterModel cm where cm.countryId =?1 and cm.isactive='Y'")
	public List<CurrencyMasterModel> getCurrencyListByCountryId(BigDecimal countryId);
	
	public List<CurrencyMasterModel> findByisactive(String isActive);
	
	@Query("select cm from CurrencyMasterModel cm where cm.countryId <>:countryid and cm.isactive ='Y' and cm.isAllowFcSale='Y'")
	public List<CurrencyMasterModel> getfcCurrencyList(@Param("countryid") BigDecimal countryid);
	
}

