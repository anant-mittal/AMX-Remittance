package com.amx.jax.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.ImpsMaster;
import com.amx.jax.repository.ImpsMasterRepository;

@Service
public class ImpsMasterService {

	ImpsMasterRepository impsMasterRepository;

	public List<ImpsMaster> getImpsMaster(BankMasterModel rb, BankMasterModel bb, String isActive, CountryMaster cm) {
		return impsMasterRepository
				.findByFsCountryMasterAndRoutingBankIdAndBeneBankIdAndApprovedDateIsNotNullAndIsActive(cm, rb, bb,
						isActive);
	}
}
