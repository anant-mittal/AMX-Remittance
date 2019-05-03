package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.IDNumberLengthCheckView;

public interface IIdNumberLengthCheckRepository extends CrudRepository<IDNumberLengthCheckView, Serializable>{
	
	public IDNumberLengthCheckView findByIDTypeId(BigDecimal iDTypeId);

}
