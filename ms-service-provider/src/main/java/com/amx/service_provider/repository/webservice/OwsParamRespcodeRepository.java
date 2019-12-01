package com.amx.service_provider.repository.webservice;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.amx.service_provider.dbmodel.webservice.OwsParamRespcode;
import com.amx.service_provider.dbmodel.webservice.OwsParamRespcodeKey;


@Component
public interface OwsParamRespcodeRepository extends CrudRepository<OwsParamRespcode, Serializable>{
		
	public OwsParamRespcode findByOwsParamRespcodeKey(OwsParamRespcodeKey wsParamRespcodeKey);

}
