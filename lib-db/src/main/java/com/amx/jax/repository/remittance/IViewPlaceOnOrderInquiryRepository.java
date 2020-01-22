package com.amx.jax.repository.remittance;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author rabil
 * @date 11 Nov 2019
 */
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewPlaceOnOrderInquiry;

public interface IViewPlaceOnOrderInquiryRepository extends CrudRepository<ViewPlaceOnOrderInquiry, Serializable>{

	List<ViewPlaceOnOrderInquiry> findByCountryBranchId(BigDecimal countryBranchId);
	
	
}
