package com.amx.jax.repository.remittance;
/**
 * @author rabil
 * @date :27 May 2019
 */
import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.OWSScheduleModel;

public interface IOWSScheduleModelRepository extends CrudRepository<OWSScheduleModel, Serializable>{
	public OWSScheduleModel findByCorBank(String corBank);
}
