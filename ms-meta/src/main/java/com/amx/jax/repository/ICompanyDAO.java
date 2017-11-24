/*package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.ViewCompanyDetails;

public interface ICompanyDAO extends JpaRepository<ViewCompanyDetails, Serializable>{

	
	@Query("select cd from ViewCompanyDetails where languageId=?1 and isActive='Y'")
	public List<ViewCompanyDetails> getCompanyDetails(BigDecimal languageId);
	
}
*/