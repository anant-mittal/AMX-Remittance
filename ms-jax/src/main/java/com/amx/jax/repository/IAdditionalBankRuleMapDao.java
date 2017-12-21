package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;

public interface IAdditionalBankRuleMapDao extends JpaRepository<AdditionalBankRuleMap, Serializable>{
	
	@Query("select am from AdditionalBankRuleMap am where am.countryId=?1 and am.flexField =?2 and am.isActive='Y'")
	public List<AdditionalBankRuleMap> getDynamicLevelMatch(BigDecimal routingCountry,String flexiField);

}
