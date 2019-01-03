package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.CountryMasterView;


/**
 * 
 * @author Rabil
 *
 */
public interface CountryRepository extends JpaRepository<CountryMasterView, BigDecimal>{
					
	@Query("Select c from CountryMasterView c where  languageId=?1 ORDER BY countryName asc")
	List<CountryMasterView> findByLanguageId(BigDecimal languageId);
	
	@Query("Select c from CountryMasterView c where  languageId=?1 and countryId=?2 ORDER BY countryName asc")
	List<CountryMasterView> findByLanguageIdAndCountryId(BigDecimal languageId,BigDecimal countryId);
	
	@Query("Select c from CountryMasterView c where  languageId=?1 and businessCountry='Y' ORDER BY countryName asc")
	List<CountryMasterView> getBusinessCountry(BigDecimal languageId);
	
	@Query("Select c from CountryMasterView c where  languageId=?1 and countryAlpha2Code  in ('IN', 'PH', 'EG') ORDER BY countryName asc")
	List<CountryMasterView> getBeneCountryList(BigDecimal languageId);
	
	@Query("Select c from CountryMasterView c where  languageId=?1 and (beneCountryRisk != 1 or countryId = ?2 ) ORDER BY countryName asc")
	List<CountryMasterView> findByLanguageIdAndNonBeneRisk(BigDecimal languageId, BigDecimal homeCountryId);

}
