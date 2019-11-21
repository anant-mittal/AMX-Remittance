package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.ViewCompanyDetails;

public interface ICompanyDAO extends JpaRepository<ViewCompanyDetails, Serializable>{

	
	@Query("select cd from ViewCompanyDetails cd where languageId=?1 and isActive='Y'")
	public List<ViewCompanyDetails> getCompanyDetails(BigDecimal languageId);
	
	@Query("select cd from ViewCompanyDetails cd where (languageId=?1 or languageId=?2) and isActive='Y'")
	public List<ViewCompanyDetails> getCompanyDetailInLang(BigDecimal languageId,BigDecimal deflanguageId);
	
	@Query("select cd from ViewCompanyDetails cd where languageId=?1 and companyId=?2 and  isActive='Y' ")
	public List<ViewCompanyDetails> getCompanyDetailsByCompanyId(BigDecimal languageId,BigDecimal companyId);
	
	@Query("select cd from ViewCompanyDetails cd where companyId=?1 and  isActive='Y' ")
	public List<ViewCompanyDetails> getCompanyDetailsByCompanyId(BigDecimal companyId);
	
}
