package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;

public interface AdditionalInstructionDataRepository extends CrudRepository<AdditionalInstructionData, BigDecimal> {

	
	public AdditionalInstructionData findByExRemittanceApplication(RemittanceApplication appl);
}
