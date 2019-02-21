package com.amx.jax.pricer.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.repository.BankMasterRepository;

@Component
public class BankService {

	@Autowired
	BankMasterRepository bankMasterRepository;
	
	public BankMasterModel getBankById(BigDecimal bankId) {
		return bankMasterRepository.findOne(bankId);
	}
}
