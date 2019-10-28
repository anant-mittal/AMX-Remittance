package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.ImpsMaster;

public interface ImpsMasterRepository extends CrudRepository<ImpsMaster, BigDecimal> {

	List<ImpsMaster> findByFsCountryMasterAndRoutingBankIdAndBeneBankIdAndApprovedDateIsNotNullAndIsActive(
			CountryMaster cm, BankMasterMdlv1 rb, BankMasterMdlv1 bb, String isActive);

}
