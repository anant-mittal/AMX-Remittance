/**
 * 
 */
package com.amx.jax.repository.promotion;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.promotion.PromotionHeader;
import com.amx.jax.dbmodel.promotion.PromotionHeaderPK;

/**
 * @author Prashant
 *
 */
public interface PromotionHeaderRepository extends CrudRepository<PromotionHeader, Serializable> {

	public PromotionHeader findByPromotionHeaderPK(PromotionHeaderPK promotionHeaderPK);
}
