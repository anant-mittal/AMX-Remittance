package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ParameterDetailsModel;


public interface IParameterDetailsRespository extends CrudRepository<ParameterDetailsModel, Serializable>{
	
	public List<ParameterDetailsModel> findByRecordIdAndIsActive(String recordId,String isactive);
	
	@Query(value = "SELECT * FROM EX_PARAMETER_DETAILS WHERE RECORD_ID =?1 AND CHAR_FIELD2=?2 AND ISACTIVE= 'Y'", nativeQuery = true)
	public ParameterDetailsModel fetchServPrvBankCode(String recordId,String beneCountryCode);

}
