/**
 * 
 */
package com.amx.jax.repository.promotion;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.promotion.PromotionLocation;
import com.amx.jax.dbmodel.promotion.PromotionLocationPK;

/**
 * @author Prashant
 *
 */
public interface PromotionLocationRepository extends CrudRepository<PromotionLocation, Serializable> {

	public List<PromotionLocation> findByPromotionLocationPK(PromotionLocationPK promotionLocationPK);
}
