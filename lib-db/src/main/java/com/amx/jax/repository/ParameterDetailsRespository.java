package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.jboss.logging.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;




import com.amx.jax.dbmodel.ParameterDetails;


public interface ParameterDetailsRespository extends CrudRepository<ParameterDetails, Serializable>{
	
	public List<ParameterDetails> findByRecordIdAndIsActive(String recordId,String isactive);
	
	

}
