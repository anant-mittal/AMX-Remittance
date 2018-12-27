package com.amx.jax.rates;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.AESRepository;
import com.amx.utils.ArgUtil;

@Repository
public class AmxCurRateRepository extends AESRepository {

	Logger LOGGER = LoggerService.getLogger(AmxCurRateRepository.class);

	private final String index = "marketrate";
	private final String type = "currate";

	public AmxCurRate insertRate(AmxCurRate rate) {
		if (!ArgUtil.isEmpty(rate) && !ArgUtil.isEmpty(rate.getrRate())
				&& rate.getrRate().compareTo(BigDecimal.ZERO) > 0) {
			super.insert(index, type, rate);
		}
		return rate;
	}

	public Map<String, Object> getRateById(String id) {
		return super.getById(index, type, id);
	}

	public Map<String, Object> updateRateById(String id, AmxCurRate vote) {
		return this.updateById(index, type, id, vote);
	}

	public void deleteRateById(String id) {
		this.deleteById(index, type, id);
	}

}
