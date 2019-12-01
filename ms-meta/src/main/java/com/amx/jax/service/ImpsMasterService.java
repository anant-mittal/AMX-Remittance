package com.amx.jax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.ImpsMaster;
import com.amx.jax.repository.ImpsMasterRepository;

@Service
public class ImpsMasterService {

	@Autowired
	ImpsMasterRepository impsMasterRepository;

	public List<ImpsMaster> getImpsMaster(BankMasterMdlv1 rb, BankMasterMdlv1 bb, String isActive, CountryMaster cm) {
		return impsMasterRepository
				.findByFsCountryMasterAndRoutingBankIdAndBeneBankIdAndApprovedDateIsNotNullAndIsActive(cm, rb, bb,
						isActive);
	}
}
