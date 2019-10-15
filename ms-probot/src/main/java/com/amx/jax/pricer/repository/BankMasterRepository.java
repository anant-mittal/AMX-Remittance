package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.BankMasterModel;

public interface BankMasterRepository extends CrudRepository<BankMasterModel, BigDecimal> {

	public List<BankMasterModel> findBybankCountryIdAndRecordStatusOrderByBankShortNameAsc(BigDecimal bankCountryId, String isActive);
	
	public BankMasterModel findBybankIdAndRecordStatus(BigDecimal bankId, String isActive);
	
	public List<BankMasterModel> findByBankIdIn(List<BigDecimal> bankIds);
	
}
