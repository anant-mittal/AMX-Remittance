package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;

public interface AdditionalInstructionDataRepository extends CrudRepository<AdditionalInstructionData, BigDecimal> {

	
	public List<AdditionalInstructionData> findByExRemittanceApplication(RemittanceApplication appl);
	
	/** added by Rabil on 10 Oct 2019 **/
	public AdditionalInstructionData findByExRemittanceApplicationAndFlexField(RemittanceApplication appl,String flexField);
}
