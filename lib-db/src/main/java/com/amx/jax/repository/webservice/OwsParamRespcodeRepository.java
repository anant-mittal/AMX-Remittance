package com.amx.jax.repository.webservice;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.webservice.OwsParamRespcode;
import com.amx.jax.dbmodel.webservice.OwsParamRespcodeKey;

@Component
public interface OwsParamRespcodeRepository extends CrudRepository<OwsParamRespcode, Serializable>{
		
	public OwsParamRespcode findByOwsParamRespcodeKey(OwsParamRespcodeKey wsParamRespcodeKey);

}
