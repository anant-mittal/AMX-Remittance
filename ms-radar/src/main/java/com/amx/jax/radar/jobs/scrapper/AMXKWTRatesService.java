package com.amx.jax.radar.jobs.scrapper;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.snap.ISnapService.RateSource;
import com.amx.jax.client.snap.ISnapService.RateType;
import com.amx.jax.dict.Currency;
import com.amx.jax.grid.GridConstants;
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridService.GridViewBuilder;
import com.amx.jax.grid.GridView;
import com.amx.jax.grid.views.XRateViewRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.mcq.shedlock.SchedulerLock;
import com.amx.jax.mcq.shedlock.SchedulerLock.LockContext;
import com.amx.jax.radar.AESRepository.BulkRequestBuilder;
import com.amx.jax.radar.jobs.customer.AbstractDBSyncTask;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncJobs;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurRate;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.TimeUtils;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
@ConditionalOnProperty("jax.jobs.rate")
public class AMXKWTRatesService extends AbstractDBSyncTask {

	public static final Logger LOGGER = LoggerService.getLogger(AMXKWTRatesService.class);
	private static final String TIME_TRACK_KEY = "processDate";
	private static final int PAGE_SIZE = 3000;

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_HRS, context = LockContext.BY_CLASS)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN * 50)
	public void doTask() {
		if (TimeUtils.inHourSlot(4, 0)) {
			this.doBothTask();
		}
	}

	@Override
	public void doTask(int lastPage, String lastId) {
		LOGGER.info("Scrapper Task");

		Long lastUpdateDateNow = oracleVarsCache.getStampStartTime(DBSyncJobs.XRATE_JOB);
		Long lastUpdateDateNowLimit = System.currentTimeMillis(); // lastUpdateDateNow + (20 * 365 *
																	// AmxCurConstants.INTERVAL_DAYS);

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Pg:{},Tm:{} {}-{}", lastPage, lastUpdateDateNow, dateString, dateStringLimit);

		GridQuery gridQuery = getForwardQuery(lastPage, PAGE_SIZE, TIME_TRACK_KEY, dateString, dateStringLimit);

		GridViewBuilder<XRateViewRecord> y = gridService
				.view(GridView.EX_V_RATE_PATTERN, gridQuery);

		AmxApiResponse<XRateViewRecord, GridMeta> x = y.get();

		////
		BulkRequestBuilder builder = new BulkRequestBuilder();
		Long lastUpdateDateNowStart = lastUpdateDateNow;
		String lastIdNow = Constants.BLANK;

		for (XRateViewRecord xrate : x.getResults()) {
			try {
				// Long lastUpdateDate = DateUtil.toUTC(record.getLastUpdateDate());
				Long lastUpdateDate = xrate.getProcessDate().getTime();
				LOGGER.debug("DIFF {}", lastUpdateDateNow - lastUpdateDate);
				if (lastUpdateDate > lastUpdateDateNow) {
					lastUpdateDateNow = lastUpdateDate;
				}

				Currency cur = (Currency) ArgUtil.parseAsEnum(xrate.getCurrencyCode(), Currency.UNKNOWN);
				if (!Currency.UNKNOWN.equals(cur)) {
					BigDecimal rate = ArgUtil.parseAsBigDecimal(xrate.getAvgSellRate());
					if (!ArgUtil.isEmpty(rate)) {
						AmxCurRate trnsfrRate = new AmxCurRate();
						trnsfrRate.setrSrc(RateSource.AMX);
						trnsfrRate.setrDomCur(Currency.KWD);
						trnsfrRate.setrForCur(cur);
						trnsfrRate.setrType(RateType.SELL_TRNSFR);
						trnsfrRate.setrRate(rate);
						trnsfrRate.setTimestamp(ArgUtil.parseAsSimpleDate(xrate.getProcessDate()));
						OracleViewDocument document = new OracleViewDocument(trnsfrRate);
						lastIdNow = document.getId();
						builder.update(oracleVarsCache.getIndex(DBSyncJobs.XRATE_JOB), document);
					}
				}
			} catch (Exception e) {
				LOGGER.error("XRateViewRecord Excep", e);
			}
		}

		LOGGER.info("Pg:{}, Rcds:{},{}, Nxt:{}", lastPage, x.getResults().size(), lastIdNow, lastUpdateDateNow);

		if (lastIdNow.equalsIgnoreCase(lastId) && x.getResults().size() > 0) {
			// Same data records case, nothing to do
			return;
		}
		lastId = lastIdNow;

		if (x.getResults().size() > 0) {
			esRepository.bulk(builder.build());
			oracleVarsCache.setStampStart(DBSyncJobs.XRATE_JOB, lastUpdateDateNow);
			if ((lastUpdateDateNowStart == lastUpdateDateNow)
					|| (x.getResults().size() == PAGE_SIZE && lastPage < 10)) {
				doTask(lastPage + 1, lastIdNow);
			}
		} else if (lastUpdateDateNowLimit < (System.currentTimeMillis() - AmxCurConstants.INTERVAL_DAYS)) {
			oracleVarsCache.setStampStart(DBSyncJobs.XRATE_JOB, lastUpdateDateNowLimit);
		}
	}

	@Override
	public void doTaskRev(int lastPage, String lastId) {
		// TODO Auto-generated method stub
	}

}
