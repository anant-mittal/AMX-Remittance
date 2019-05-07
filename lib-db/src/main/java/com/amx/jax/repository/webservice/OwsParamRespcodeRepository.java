package com.amx.jax.repository.webservice;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.amx.jax.dbmodel.webservice.OwsParamRespcode;
import com.amx.jax.dbmodel.webservice.OwsParamRespcodeKey;

@Repository
public interface OwsParamRespcodeRepository extends CrudRepository<OwsParamRespcode, Serializable>{
		
	public OwsParamRespcode findByOwsParamRespcodeKey(OwsParamRespcodeKey wsParamRespcodeKey);

}
