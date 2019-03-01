package com.amx.jax.pricer.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.repository.BankMasterRepository;

@Component
public class BankMasterDao {

	@Autowired
	BankMasterRepository bankMasterRepository;
	
	public BankMasterModel getBankById(BigDecimal bankId) {
		return bankMasterRepository.findOne(bankId);
	}
}
