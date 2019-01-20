package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.remittance.BranchDayTransactionView;

public interface BranchDayTransactionRepository extends CrudRepository<BranchDayTransactionView, Serializable>{
	
	@Query(value="select * from VW_EX_BRANCH_DAY_TRNX_ENQUIRY where ACCOUNT_MMYYYY =to_date(:accountMMYYYY,'dd/MM/yyyy') and COUNTRY_BRANCH_ID=:countryBranchId and EMPLOYEE_ID =:employeeId and trunc(DOCUMENT_DATE)=to_date(:documentDate,'dd/MM/yyyy') " ,nativeQuery = true)
	 public List<BranchDayTransactionView> getTotalTrnxCount(@Param("accountMMYYYY") String accountMMYYYY,
			 @Param("countryBranchId") BigDecimal countryBranchId,
			 @Param("employeeId") BigDecimal employeeId,
			 @Param("documentDate") String documentDate);
	
	
}
