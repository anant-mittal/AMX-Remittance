package com.amx.jax.radar.jobs.customer;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
import com.amx.jax.mcq.shedlock.SchedulerLock;
import com.amx.jax.mcq.shedlock.SchedulerLock.LockContext;
import com.amx.jax.radar.RadarConfig;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncIndex;
import com.amx.jax.radar.snap.SnapQueryService.BulkRequestSnapBuilder;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.TimeUtils;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
//@ConditionalOnProperty("jax.jobs.trnx")
@ConditionalOnExpression(RadarConfig.CE_TRNX_SYNC_AND_ES)
public class TrnxViewUpdateTask extends AbstractDBSyncTask {

	private static final int REV_INTERVAL_DAYS = 2;
	private static final int FWD_INTERVAL_DAYS = 2;
	private static final Logger LOGGER = LoggerService.getLogger(TrnxViewUpdateTask.class);
	private static final String TIME_TRACK_KEY = "lastUpdateDate";
	private static final int PAGE_SIZE = 5000;

	long intervalDays = FWD_INTERVAL_DAYS;

	@Autowired
	RadarConfig radarConfig;

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_MIN * 30, context = LockContext.BY_METHOD)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 15)
	public void doTaskModeNight() {
		if (TimeUtils.inHourSlot(4, 0) && radarConfig.isJobTranxNightEnabled()) {
			this.doTask();
		}
	}

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_MIN * 30, context = LockContext.BY_METHOD)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN * 10)
	public void doTaskModeDay() {
		if (!TimeUtils.inHourSlot(4, 0) && radarConfig.isJobTranxDayEnabled()) {
			this.doTask();
		}
	}

	@Override
	// @Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 10)
	public void doTask() {
		this.doBothTask();
	}

	public void doTask(int lastPage, String lastId) {

		Long lastUpdateDateNow = oracleVarsCache.getStampStartTime(DBSyncIndex.TRANSACTION_JOB);
		Long lastUpdateDateNowLimit = lastUpdateDateNow + (intervalDays * AmxCurConstants.INTERVAL_DAYS);

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Pg:{},Tm:{} {}-{}", lastPage, lastUpdateDateNow, dateString, dateStringLimit);

		GridQuery gridQuery = getForwardQuery(lastPage, PAGE_SIZE, TIME_TRACK_KEY, dateString, dateStringLimit);

		GridViewBuilder<TranxViewRecord> y = gridService
				.view(GridView.VW_KIBANA_TRNX_MV, gridQuery);

		AmxApiResponse<TranxViewRecord, GridMeta> x = y.get();

		BulkRequestSnapBuilder builder = new BulkRequestSnapBuilder();

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
				builder.update(DBSyncIndex.TRANSACTION_JOB.getIndexName(), document);
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
			intervalDays = 1;
			esRepository.bulk(builder.build());
			oracleVarsCache.setStampStart(DBSyncIndex.TRANSACTION_JOB, lastUpdateDateNow);
			if ((lastUpdateDateNowStart == lastUpdateDateNow) || (x.getResults().size() == 1000 && lastPage < 10)) {
				doTask(lastPage + 1, lastId);
			}
		} else if (lastUpdateDateNowLimit < todayOffset) {
			intervalDays++;
			oracleVarsCache.setStampStart(DBSyncIndex.TRANSACTION_JOB, lastUpdateDateNowLimit);
		} else {
			oracleVarsCache
					.setStampStart(DBSyncIndex.TRANSACTION_JOB,
							Math.min(todayOffset, lastUpdateDateNow + AmxCurConstants.INTERVAL_DAYS));
		}

	}

	public void doTaskRev(int lastPage, String lastId) {

		Long lastUpdateDateNowFrwrds = oracleVarsCache.getStampStartTime(DBSyncIndex.TRANSACTION_JOB);
		Long lastUpdateDateNow = oracleVarsCache.getStampEndTime(DBSyncIndex.TRANSACTION_JOB);

		if (lastUpdateDateNow < lastUpdateDateNowFrwrds
				|| lastUpdateDateNow < OracleVarsCache.START_TIME) {
			return;
		}

		Long lastUpdateDateNowLimit = lastUpdateDateNow - (REV_INTERVAL_DAYS * AmxCurConstants.INTERVAL_DAYS);

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Pg:{},Tm:{} {}-{}", lastPage, lastUpdateDateNow, dateString, dateStringLimit);

		GridQuery gridQuery = getReverseQuery(lastPage, PAGE_SIZE, TIME_TRACK_KEY, dateString, dateStringLimit);

		GridViewBuilder<TranxViewRecord> y = gridService
				.view(GridView.VW_KIBANA_TRNX_MV, gridQuery);

		AmxApiResponse<TranxViewRecord, GridMeta> x = y.get();

		BulkRequestSnapBuilder builder = new BulkRequestSnapBuilder();

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
				builder.update(DBSyncIndex.TRANSACTION_JOB.getIndexName(), document);
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
			oracleVarsCache.setStampEnd(DBSyncIndex.TRANSACTION_JOB, lastUpdateDateNow);
			if ((lastUpdateDateNowStart == lastUpdateDateNow) || (x.getResults().size() == 1000 && lastPage < 2)) {
				doTaskRev(lastPage + 1, lastId);
			}
		} else {
			oracleVarsCache.setStampEnd(DBSyncIndex.TRANSACTION_JOB, lastUpdateDateNowLimit);
		}

	}

}
