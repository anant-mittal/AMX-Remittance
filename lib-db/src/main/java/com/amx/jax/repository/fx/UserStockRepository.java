package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.UserStockView;

public interface UserStockRepository extends CrudRepository<UserStockView, Serializable>{
	
	@Query("select us from UserStockView us where us.countryId =:countryId and us.oracleUser=:userName and us.countryBranchId =:countryBranchId and us.currencyId =:foreignCurrencyId and trunc(logDate) = trunc(sysdate)")
	public List<UserStockView> fetchUserStockByCurrencyDate(BigDecimal countryId,String userName,BigDecimal countryBranchId,BigDecimal foreignCurrencyId);
	
	@Query("select us from UserStockView us where us.countryId =:countryId and us.oracleUser=:userName and us.countryBranchId =:countryBranchId and trunc(logDate) = trunc(sysdate)")
	public List<UserStockView> fetchUserStockByDate(BigDecimal countryId,String userName,BigDecimal countryBranchId);
	
	@Query("select us.currencyId,sum(us.currentStock) as currentStock from UserStockView us where us.countryId =:countryId and us.oracleUser=:userName and us.countryBranchId =:countryBranchId and trunc(logDate) = trunc(sysdate) Group By us.currencyId")
	public List<Object[]> fetchUserStockByDateSum(BigDecimal countryId,String userName,BigDecimal countryBranchId);

}
