package com.amx.jax.repository.remittance;
/**
 * @author rabil
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewVatDetails;


public interface IViewVatDetailsRespository extends CrudRepository<ViewVatDetails, Serializable>{
	
	@Query(value="select * from  V_EX_VAT_DETAILS where APPLICATION_COUNTRY_ID =?1 and VAT_CATEGORY =?2 and ACCOUNT_TYPE=?3 and trunc(sysdate) between from_dt and to_dt", nativeQuery = true)
	public List<ViewVatDetails> getVatDetails(BigDecimal applicationCountryId,String vatCategory,String accountType);
	
	


}
