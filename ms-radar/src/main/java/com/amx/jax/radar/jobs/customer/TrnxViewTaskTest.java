package com.amx.jax.radar.jobs.customer;

import java.util.Date;

import org.slf4j.Logger;
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
import com.amx.jax.rates.AmxCurConstants;
import com.amx.utils.JsonUtil;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
//@ConditionalOnProperty("jax.jobs.trnx")
//@ConditionalOnExpression(RadarConfig.CE_TRNX_SYNC_AND_ES)
public class TrnxViewTaskTest extends AbstractDBSyncTask {

	private static final Logger LOGGER = LoggerService.getLogger(TrnxViewTaskTest.class);
	private static final String TIME_TRACK_KEY = "lastUpdateDate";
	private static final int PAGE_SIZE = 5000;

	long intervalDays = 20;

	// @SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_MIN * 30, context =
	// LockContext.BY_METHOD)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 5)
	public void doTaskModeNight() {
		this.doTask(0,null);
	}

	@Override
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 10)
	public void doTask() {
		// this.doBothTask();
	}

	public void doTask(int lastPage, String lastId) {

		Long lastUpdateDateNow = System.currentTimeMillis()- (5 * AmxCurConstants.INTERVAL_DAYS);
		Long lastUpdateDateNowLimit = System.currentTimeMillis();

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Pg:{},Tm:{} {}-{}", lastPage, lastUpdateDateNow, dateString, dateStringLimit);

		GridQuery gridQuery = getForwardQuery(lastPage, 200, TIME_TRACK_KEY, dateString, dateStringLimit);

		GridViewBuilder<TranxViewRecord> y = gridService
				.view(GridView.VW_KIBANA_TRNX, gridQuery);

		AmxApiResponse<TranxViewRecord, GridMeta> x = y.get();

		for (TranxViewRecord record : x.getResults()) {
			try {
				// Long lastUpdateDate = DateUtil.toUTC(record.getLastUpdateDate());
				Long lastUpdateDate = record.getLastUpdateDate().getTime();
				LOGGER.debug("DIFF {}", lastUpdateDateNow - lastUpdateDate);
				if (lastUpdateDate > lastUpdateDateNow) {
					lastUpdateDateNow = lastUpdateDate;
				}
				LOGGER.info(JsonUtil.toJson(record));
			} catch (Exception e) {
				LOGGER.error("TranxViewRecord Excep", e);
			}
		}

	}

	public void doTaskRev(int lastPage, String lastId) {

	}

}
