package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.ImpsMaster;

public interface ImpsMasterRepository extends CrudRepository<ImpsMaster, BigDecimal> {

	List<ImpsMaster> findByFsCountryMasterAndRoutingBankIdAndBeneBankIdAndApprovedDateIsNotNullAndIsActive(
			CountryMaster cm, BankMasterModel rb, BankMasterModel bb, String isActive);

}
