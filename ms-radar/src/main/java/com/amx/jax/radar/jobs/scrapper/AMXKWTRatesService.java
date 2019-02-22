package com.amx.jax.radar.jobs.scrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.jax.client.snap.ISnapService.RateSource;
import com.amx.jax.client.snap.ISnapService.RateType;
import com.amx.jax.dict.Currency;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.mcq.shedlock.SchedulerLock;
import com.amx.jax.mcq.shedlock.SchedulerLock.LockContext;
import com.amx.jax.radar.jobs.customer.AbstractDBSyncTask;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurRate;
import com.amx.jax.rates.AmxCurRateRepository;
import com.amx.utils.ArgUtil;
import com.amx.utils.TimeUtils;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
@ConditionalOnProperty("jax.jobs.rate")
public class AMXKWTRatesService extends AbstractDBSyncTask {

	@Autowired
	private AmxCurRateRepository curRateRepository;

	public static final Logger LOGGER = LoggerService.getLogger(AMXKWTRatesService.class);
	private static final String TIME_TRACK_KEY = "lastUpdateDate";
	private static final int PAGE_SIZE = 1000;

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_HRS, context = LockContext.BY_CLASS)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN * 30)
	public void doTask() {
		if (TimeUtils.inHourSlot(4, 1)) {
			this.doBothTask();
		}
	}

	@Override
	public void doTask(int lastPage, String lastId) {
		LOGGER.info("Scrapper Task");
		List<MinMaxExRateDTO> rates = null; // xRateClient.getMinMaxExchangeRate().getResults();
		RateType type = RateType.SELL_TRNSFR;
		for (MinMaxExRateDTO minMaxExRateDTO : rates) {
			Currency cur = (Currency) ArgUtil.parseAsEnum(minMaxExRateDTO.getToCurrency().getQuoteName(),
					Currency.UNKNOWN);
			if (!Currency.UNKNOWN.equals(cur)) {
				BigDecimal rate = ArgUtil.parseAsBigDecimal(minMaxExRateDTO.getMaxExrate());
				if (!ArgUtil.isEmpty(rate)) {
					AmxCurRate trnsfrRate = new AmxCurRate();
					trnsfrRate.setrSrc(RateSource.AMX);
					trnsfrRate.setrDomCur(Currency.KWD);
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

	@Override
	public void doTaskRev(int lastPage, String lastId) {
		// TODO Auto-generated method stub
	}

}
