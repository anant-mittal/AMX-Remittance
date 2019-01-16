package com.amx.jax.radar.jobs.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.grid.GridColumn;
import com.amx.jax.grid.GridConstants;
import com.amx.jax.grid.GridConstants.FilterDataType;
import com.amx.jax.grid.GridConstants.FilterOperater;
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridService;
import com.amx.jax.grid.GridService.GridViewBuilder;
import com.amx.jax.grid.GridView;
import com.amx.jax.grid.SortOrder;
import com.amx.jax.grid.views.TranxViewRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.AESRepository.BulkRequestBuilder;
import com.amx.jax.radar.ARadarTask;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.radar.TestSizeApp;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;

@Configuration
@EnableScheduling
@Component
@Service
@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class TrnxViewTaskRev extends ARadarTask {

	private static final Logger LOGGER = LoggerService.getLogger(TrnxViewTaskRev.class);

	@Autowired
	private ESRepository esRepository;

	@Autowired
	GridService gridService;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	OracleVarsCache oracleVarsCache;

	private Long lastUpdateDateNow = 0L;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 10)
	public void doTask() {
		doTask(0);
	}

	public void doTask(int lastPage) {
		AppContextUtil.setTenant(TenantContextHolder.currentSite(appConfig.getDefaultTenant()));
		AppContextUtil.getTraceId(true, true);
		AppContextUtil.init();

		Long lastUpdateDateNowFrwrds = oracleVarsCache.getTranxScannedStamp();
		lastUpdateDateNow = oracleVarsCache.getTranxScannedStamp(true);

		if (lastUpdateDateNow < lastUpdateDateNowFrwrds
				|| lastUpdateDateNow < OracleVarsCache.START_TIME) {
			return;
		}

		Long lastUpdateDateNowLimit = lastUpdateDateNow - (10 * AmxCurConstants.INTERVAL_DAYS);

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Pg:{},Time:{} {} - {}", lastPage, lastUpdateDateNow, dateString, dateStringLimit);

		GridQuery gridQuery = new GridQuery();
		// gridQuery.setPageNo(lastPage++);
		gridQuery.setPageSize(1000);
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());
		GridColumn column = new GridColumn();
		column.setKey("lastUpdateDate");
		column.setOperator(FilterOperater.STE);
		column.setDataType(FilterDataType.TIME);
		column.setValue(dateString);
		column.setSortDir(SortOrder.DESC);
		gridQuery.getColumns().add(column);

		GridColumn column2 = new GridColumn();
		column2.setKey("lastUpdateDate");
		column2.setOperator(FilterOperater.GT);
		column2.setDataType(FilterDataType.TIME);
		column2.setValue(dateStringLimit);
		column2.setSortDir(SortOrder.DESC);
		gridQuery.getColumns().add(column2);

		gridQuery.setSortBy(0);
		gridQuery.setSortOrder(SortOrder.DESC);

		GridViewBuilder<TranxViewRecord> y = gridService
				.view(GridView.VW_KIBANA_TRNX, gridQuery);

		AmxApiResponse<TranxViewRecord, GridMeta> x = y.get();

		BulkRequestBuilder builder = new BulkRequestBuilder();

		Long lastUpdateDateNowStart = lastUpdateDateNow;
		for (TranxViewRecord record : x.getResults()) {
			try {
				// Long lastUpdateDate = DateUtil.toUTC(record.getLastUpdateDate());
				Long lastUpdateDate = record.getLastUpdateDate().getTime();
				LOGGER.debug("DIFF {}", lastUpdateDateNow - lastUpdateDate);
				if (lastUpdateDate < lastUpdateDateNow) {
					lastUpdateDateNow = lastUpdateDate;
				}

				BigDecimal appId = ArgUtil.parseAsBigDecimal(record.getId());
				Date creationDate = ArgUtil.parseAsSimpleDate(record.getLastUpdateDate());
				OracleViewDocument document = new OracleViewDocument();
				document.setId("appxn-" + appId);
				document.setTimestamp(creationDate);
				document.setTrnx(record);
				document.normalizeTrnx();
				builder.update(oracleVarsCache.getTranxIndex(), "appxn", document);
			} catch (Exception e) {
				LOGGER.error("TranxViewRecordRev Excep", e);
			}
		}

		LOGGER.info("Pg:{}, Rcds:{}, Nxt:{}", lastPage, x.getResults().size(), lastUpdateDateNow);
		if (x.getResults().size() > 0) {
			esRepository.bulk(builder.build());
			oracleVarsCache.setTranxScannedStamp(lastUpdateDateNow, true);
			if (lastUpdateDateNowStart == lastUpdateDateNow) {
				doTask(lastPage + 1);
			}
		} else {
			oracleVarsCache.setTranxScannedStamp(lastUpdateDateNowLimit, true);
		}

	}

}
