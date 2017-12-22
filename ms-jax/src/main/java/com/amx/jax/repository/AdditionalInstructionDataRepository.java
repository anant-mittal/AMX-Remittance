package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;

public interface AdditionalInstructionDataRepository extends CrudRepository<AdditionalInstructionData, BigDecimal> {

}
