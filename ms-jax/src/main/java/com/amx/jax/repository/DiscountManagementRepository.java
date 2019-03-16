package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CountryBranch;

public interface DiscountManagementRepository extends CrudRepository<CountryBranch, BigDecimal> {

	@Query(value = "SELECT * FROM EX_COUNTRY_BRANCH WHERE COUNTRY_ID=?1 AND "
			+ " ISACTIVE='Y' AND CORPORATE_STATUS='N' AND branch_name != 'ONLINE' AND HEAD_OFFICE_INDICATOR=0", nativeQuery = true)
	List<CountryBranch> getCountryBranch(BigDecimal countryId);
}
