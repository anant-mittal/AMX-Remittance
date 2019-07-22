package com.amx.jax.repository.promotion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.promotion.DailyPromotion;

public interface DailyPromotionRepository extends CrudRepository<DailyPromotion, Serializable> {

	@Query(value = "SELECT * FROM EX_DAILY_PROMOTIONS_DTLS WHERE UTILIZED IS NULL ORDER BY PROMO_CODE", nativeQuery = true)
	List<DailyPromotion> getPromoCodeList();

	DailyPromotion findFirstByUtilizeIsNull();

	@Query(value = "select d from DailyPromotion d where remitTrnxId=?1 and utilize='Y'")
	DailyPromotion getWantitByTrnxId(BigDecimal remitTrnxId);
}
