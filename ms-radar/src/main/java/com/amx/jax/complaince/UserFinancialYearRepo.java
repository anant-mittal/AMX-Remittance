package com.amx.jax.complaince;


	
	import java.io.Serializable;
	import java.util.List;

	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.data.jpa.repository.Query;

	

	public interface UserFinancialYearRepo extends JpaRepository<UserFinancialYear, Serializable>{
		
		
		@Query(value ="select * from EX_USER_FINANCIAL_YEAR  fc where FINANCIAL_YEAR_BEGIN<=trunc(sysdate) and  FINANCIAL_YEAR_END >=trunc(sysdate)",nativeQuery=true)
		public List<UserFinancialYear> getFinancialYear();
	}


