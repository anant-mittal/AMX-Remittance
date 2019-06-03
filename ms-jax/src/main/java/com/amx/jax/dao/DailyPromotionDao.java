package com.amx.jax.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.promotion.DailyPromotion;
import com.amx.jax.repository.promotion.DailyPromotionRepository;

@Component
public class DailyPromotionDao {

	@Autowired
	DailyPromotionRepository dailyPromotionRepository;

	public DailyPromotion getWantitByTrnxId(BigDecimal remittanceTransactionId) {

		return dailyPromotionRepository.getWantitByTrnxId(remittanceTransactionId);
	}
}
