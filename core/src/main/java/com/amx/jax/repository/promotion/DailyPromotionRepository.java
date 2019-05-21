package com.amx.jax.repository.promotion;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.promotion.DailyPromotion;

public interface DailyPromotionRepository extends CrudRepository<DailyPromotion, Serializable> {

}
