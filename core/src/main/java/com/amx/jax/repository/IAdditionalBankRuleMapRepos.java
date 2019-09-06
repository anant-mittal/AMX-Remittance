package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;



public interface IAdditionalBankRuleMapRepos extends CrudRepository<AdditionalBankRuleMap, Serializable> {

	AdditionalBankRuleMap findByFlexFieldAndIsActive(String flexField,String isActive);
}
