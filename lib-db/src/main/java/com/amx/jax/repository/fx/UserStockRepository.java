package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.UserStockView;

@Transactional
public interface UserStockRepository extends CrudRepository<UserStockView, Serializable>{
	
	@Query(value = "SELECT * FROM V_EX_STOCK WHERE COUNTRY_ID=?1 AND ORACLE_USER=?2 AND COUNTRY_BRANCH_ID=?3 AND CURRENCY_ID=?4 AND TRUNC(LOG_DATE)=TRUNC(SYSDATE)", nativeQuery = true)
	public List<UserStockView> fetchUserStockByCurrencyDate(BigDecimal countryId,String userName,BigDecimal countryBranchId,BigDecimal foreignCurrencyId);
	
	@Query(value = "SELECT * FROM V_EX_STOCK WHERE COUNTRY_ID=?1 AND ORACLE_USER=?2 AND COUNTRY_BRANCH_ID=?3 AND TRUNC(LOG_DATE)=TRUNC(SYSDATE)", nativeQuery = true)
	public List<UserStockView> fetchUserStockByDate(BigDecimal countryId,String userName,BigDecimal countryBranchId);
	
	//@Query(value = "SELECT CURRENCY_ID,SUM(CURRENT_STOCK) FROM V_EX_STOCK WHERE COUNTRY_ID=?1 AND ORACLE_USER=?2 AND COUNTRY_BRANCH_ID=?3 AND TRUNC(LOG_DATE)=TRUNC(SYSDATE) Group By CURRENCY_ID", nativeQuery = true)
	@Query(value = "SELECT CURRENCY_ID,SUM(T_VALUE) FROM (SELECT CURRENCY_ID, CURRENT_STOCK * DENOMINATION_AMOUNT T_VALUE FROM V_EX_STOCK WHERE COUNTRY_ID=?1 AND ORACLE_USER=?2 AND COUNTRY_BRANCH_ID=?3 AND TRUNC(LOG_DATE)=TRUNC(SYSDATE)) Group By CURRENCY_ID", nativeQuery = true)
	public List<Object[]> fetchUserStockByDateSum(BigDecimal countryId,String userName,BigDecimal countryBranchId);

}
