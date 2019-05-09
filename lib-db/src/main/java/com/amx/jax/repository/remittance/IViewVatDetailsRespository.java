package com.amx.jax.repository.remittance;

/**
 * @author rabil
 */
import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewVatDetails;

import org.springframework.data.jpa.repository.Query;


public interface IViewVatDetailsRespository extends CrudRepository<ViewVatDetails, Serializable>{
	
	@Query(value="select * from  V_EX_VAT_DETAILS where ACCOUNT_TYPE=?1 and trunc(sysdate) between from_dt and to_dt", nativeQuery = true)
	public List<ViewVatDetails> getVatDetails(String accountType);
	
	
	
/*	
	select VAT_TYPE   , VAT_PERCENTAGE,  CALCULATION_TYPE,    ROUND_OFF, 'Y'
    INTO   W_VAT_TYPE,  W_VAT_PERCENGATE, W_CALCULATION_TYPE,   W_ROUND_OFF , W_VAT_APPLICABLE
    from V_EX_VAT_DETAILS A
    where A.ACCOUNT_TYPE = 'COMMISSION'
    AND  TRUNC(SYSDATE) BETWEEN A.FROM_DT AND A.TO_DT;*/

}
