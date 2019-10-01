package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.UserFcStockView;

public interface UserFcStockRepository extends CrudRepository<UserFcStockView, Serializable>{
	
	@Query(value = "SELECT * FROM VW_FC_STOCK WHERE ORAUSER=?1 AND COUNTRY_BRANCH_ID=?2 AND CURRENCY_ID in ?3", nativeQuery = true)
	public List<UserFcStockView> fetchUserStockByAllCurrencyDate(String userName,BigDecimal countryBranchId,List<BigDecimal> foreignCurrencyId);

}
