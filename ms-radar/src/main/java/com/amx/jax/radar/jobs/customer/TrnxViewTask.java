package com.amx.jax.radar.jobs.customer;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.grid.GridConstants;
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridService.GridViewBuilder;
import com.amx.jax.grid.GridView;
import com.amx.jax.grid.views.TranxViewRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.mcq.Candidate;
import com.amx.jax.mcq.shedlock.SchedulerLock;
import com.amx.jax.mcq.shedlock.SchedulerLock.LockContext;
import com.amx.jax.radar.AESRepository.BulkRequestBuilder;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.TimeUtils;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
@ConditionalOnProperty("jax.jobs.trnx")
public class TrnxViewTask extends AbstractDBSyncTask {

	private static final Logger LOGGER = LoggerService.getLogger(TrnxViewTask.class);
	private static final String TIME_TRACK_KEY = "lastUpdateDate";
	private static final int PAGE_SIZE = 1000;

	long intervalDays = 10;

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_MIN * 30, context = LockContext.BY_CLASS)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 15)
	public void doTaskModeNight() {
		if (TimeUtils.inHourSlot(4, 0)) {
			this.doTask();
		}
	}

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_MIN * 30, context = LockContext.BY_CLASS)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN * 10)
	public void doTaskModeDay() {
		if (!TimeUtils.inHourSlot(4, 0)) {
			this.doTask();
		}
	}

	@Override
	// @Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 10)
	public void doTask() {
		this.doBothTask();
	}

	public void doTask(int lastPage, String lastId) {

		Long lastUpdateDateNow = oracleVarsCache.getTranxScannedStamp(false);
		Long lastUpdateDateNowLimit = lastUpdateDateNow + (intervalDays * AmxCurConstants.INTERVAL_DAYS);

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Pg:{},Tm:{} {}-{}", lastPage, lastUpdateDateNow, dateString, dateStringLimit);

		GridQuery gridQuery = getForwardQuery(lastPage, PAGE_SIZE, TIME_TRACK_KEY, dateString, dateStringLimit);

		GridViewBuilder<TranxViewRecord> y = gridService
				.view(GridView.VW_KIBANA_TRNX, gridQuery);

		AmxApiResponse<TranxViewRecord, GridMeta> x = y.get();

		BulkRequestBuilder builder = new BulkRequestBuilder();

		Long lastUpdateDateNowStart = lastUpdateDateNow;
		String lastIdNow = Constants.BLANK;
		for (TranxViewRecord record : x.getResults()) {
			try {
				// Long lastUpdateDate = DateUtil.toUTC(record.getLastUpdateDate());
				Long lastUpdateDate = record.getLastUpdateDate().getTime();
				LOGGER.debug("DIFF {}", lastUpdateDateNow - lastUpdateDate);
				if (lastUpdateDate > lastUpdateDateNow) {
					lastUpdateDateNow = lastUpdateDate;
				}
				OracleViewDocument document = new OracleViewDocument(record);
				lastIdNow = ArgUtil.parseAsString(document.getId(), Constants.BLANK);
				builder.update(oracleVarsCache.getTranxIndex(), document);
			} catch (Exception e) {
				LOGGER.error("TranxViewRecord Excep", e);
			}
		}

		LOGGER.info("Pg:{}, Rcds:{},{}, Nxt:{}", lastPage, x.getResults().size(), lastIdNow, lastUpdateDateNow);

		if (lastIdNow.equalsIgnoreCase(lastId) && x.getResults().size() > 0) {
			// Same data records case, nothing to do
			return;
		}

		lastId = lastIdNow;

		long todayOffset = System.currentTimeMillis() - AmxCurConstants.INTERVAL_DAYS;
		if (x.getResults().size() > 0) {
			intervalDays = 10;
			esRepository.bulk(builder.build());
			oracleVarsCache.setTranxScannedStamp(lastUpdateDateNow, false);
			if ((lastUpdateDateNowStart == lastUpdateDateNow) || (x.getResults().size() == 1000 && lastPage < 10)) {
				doTask(lastPage + 1, lastId);
			}
		} else if (lastUpdateDateNowLimit < todayOffset) {
			intervalDays++;
			oracleVarsCache.setTranxScannedStamp(lastUpdateDateNowLimit, false);
		} else {
			oracleVarsCache
					.setTranxScannedStamp(Math.min(todayOffset, lastUpdateDateNow + AmxCurConstants.INTERVAL_DAYS),
							false);
		}

	}

	public void doTaskRev(int lastPage, String lastId) {

		Long lastUpdateDateNowFrwrds = oracleVarsCache.getTranxScannedStamp(false);
		Long lastUpdateDateNow = oracleVarsCache.getTranxScannedStamp(true);

		if (lastUpdateDateNow < lastUpdateDateNowFrwrds
				|| lastUpdateDateNow < OracleVarsCache.START_TIME) {
			return;
		}

		Long lastUpdateDateNowLimit = lastUpdateDateNow - (10 * AmxCurConstants.INTERVAL_DAYS);

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Pg:{},Tm:{} {}-{}", lastPage, lastUpdateDateNow, dateString, dateStringLimit);

		GridQuery gridQuery = getReverseQuery(lastPage, PAGE_SIZE, TIME_TRACK_KEY, dateString, dateStringLimit);

		GridViewBuilder<TranxViewRecord> y = gridService
				.view(GridView.VW_KIBANA_TRNX, gridQuery);

		AmxApiResponse<TranxViewRecord, GridMeta> x = y.get();

		BulkRequestBuilder builder = new BulkRequestBuilder();

		Long lastUpdateDateNowStart = lastUpdateDateNow;
		String lastIdNow = Constants.BLANK;
		for (TranxViewRecord record : x.getResults()) {
			try {
				// Long lastUpdateDate = DateUtil.toUTC(record.getLastUpdateDate());
				Long lastUpdateDate = record.getLastUpdateDate().getTime();
				LOGGER.debug("DIFF {}", lastUpdateDateNow - lastUpdateDate);
				if (lastUpdateDate < lastUpdateDateNow) {
					lastUpdateDateNow = lastUpdateDate;
				}

				OracleViewDocument document = new OracleViewDocument(record);
				lastIdNow = ArgUtil.parseAsString(document.getId(), Constants.BLANK);
				builder.update(oracleVarsCache.getTranxIndex(), document);
			} catch (Exception e) {
				LOGGER.error("TranxViewRecordRev Excep", e);
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
			oracleVarsCache.setTranxScannedStamp(lastUpdateDateNow, true);
			if ((lastUpdateDateNowStart == lastUpdateDateNow) || (x.getResults().size() == 1000 && lastPage < 2)) {
				doTaskRev(lastPage + 1, lastId);
			}
		} else {
			oracleVarsCache.setTranxScannedStamp(lastUpdateDateNowLimit, true);
		}

	}

}
