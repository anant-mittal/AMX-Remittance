package com.amx.jax.jobs.scrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurConstants.RCur;
import com.amx.jax.rates.AmxCurConstants.RSource;
import com.amx.jax.rates.AmxCurConstants.RType;
import com.amx.jax.rates.AmxCurRate;
import com.amx.jax.rates.AmxCurRateRepository;
import com.amx.utils.ArgUtil;

@Configuration
@EnableScheduling
@Component
@Service
public class AMXJob extends AScrapperTasks {

	@Autowired
	private AmxCurRateRepository curRateRepository;

	@Autowired
	private ExchangeRateClient xRateClient;

	Logger logger = LoggerService.getLogger(AMXJob.class);

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void doTask() {
		List<MinMaxExRateDTO> rates = xRateClient.getMinMaxExchangeRate().getResults();

		RType type = RType.SELL_TRNSFR;

		for (MinMaxExRateDTO minMaxExRateDTO : rates) {

			AmxCurConstants.RCur cur = (RCur) ArgUtil.parseAsEnum(minMaxExRateDTO.getToCurrency().getQuoteName(),
					AmxCurConstants.RCur.UNKNOWN);
			if (!AmxCurConstants.RCur.UNKNOWN.equals(cur)) {
				BigDecimal rate = ArgUtil.parseAsBigDecimal(minMaxExRateDTO.getMaxExrate());
				if (!ArgUtil.isEmpty(rate)) {
					AmxCurRate trnsfrRate = new AmxCurRate();
					trnsfrRate.setrSrc(RSource.AMX);
					trnsfrRate.setrDomCur(RCur.KWD);
					trnsfrRate.setrForCur(cur);
					trnsfrRate.setrType(type);
					trnsfrRate.setrRate(rate);
					trnsfrRate.setrRate(BigDecimal.ONE.divide(rate, 12, RoundingMode.CEILING));
					// System.out.println(JsonUtil.toJson(trnsfrRate));
					curRateRepository.insertRate(trnsfrRate);
				}
			}
		}

	}

}
