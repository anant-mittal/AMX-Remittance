/**
 * 
 */
package com.amx.jax.repository.promotion;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.promotion.PromotionHeader;
import com.amx.jax.dbmodel.promotion.PromotionHeaderPK;

/**
 * @author Prashant
 *
 */
public interface PromotionHeaderRepository extends CrudRepository<PromotionHeader, Serializable> {

	@Query("select p from PromotionHeader p where promotionHeaderPK in ?1  and  fromDate <= ?2 and toDate >= ?2")
	public List<PromotionHeader> findPromotioHeader(List<PromotionHeaderPK> promotionHeaderPK, Date transactionDate);
}
