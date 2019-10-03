package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ParameterDetails;

public interface ParameterDetailsRespository extends CrudRepository<ParameterDetails, Serializable>{
	
	public List<ParameterDetails> findByRecordIdAndIsActive(String recordId,String isactive);
	
	@Query(value = "SELECT * FROM EX_PARAMETER_DETAILS WHERE RECORD_ID =?1 AND CHAR_FIELD2=?2 AND ISACTIVE= 'Y'", nativeQuery = true)
	public ParameterDetails fetchServPrvBankCode(String recordId,String beneCountryCode);
	
	@Query(value = "SELECT * FROM EX_PARAMETER_DETAILS WHERE RECORD_ID =?1 AND CHAR_FIELD3=?2 AND ISACTIVE= 'Y'", nativeQuery = true)
	public ParameterDetails fetchBeneCountryBeneAddressNotReq(String recordId,String beneCountryCode);
	
	public ParameterDetails findByRecordIdAndParamCodeDefAndIsActive(String recordId,String paramCodeDef,String isActive);

}
