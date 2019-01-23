package com.amx.jax.radar.jobs.customer;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
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
import com.amx.jax.grid.views.CustomerDetailViewRecord;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.AESRepository.BulkRequestBuilder;
import com.amx.jax.radar.TestSizeApp;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

@Configuration
@EnableScheduling
@Component
@Service
@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class CustomerViewTask extends AbstractDBSyncTask {

	private static final Logger LOGGER = LoggerService.getLogger(CustomerViewTask.class);
	private static final String TIME_TRACK_KEY = "lastUpdateDate";
	public static final int PAGE_SIZE = 1000;
	public static final Long TIME_PAGE_DELTA = 30 * AmxCurConstants.INTERVAL_DAYS;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 30)
	public void doTask() {
		this.doBothTask();
	}

	public void doTask(int lastPage, String lastId) {

		Long lastUpdateDateNow = oracleVarsCache.getCustomerScannedStamp(false);
		Long lastUpdateDateNowLimit = lastUpdateDateNow + TIME_PAGE_DELTA;

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Pg:{},Time:{} {} - {}", lastPage, lastUpdateDateNow, dateString, dateStringLimit);

		GridQuery gridQuery = getForwardQuery(lastPage, PAGE_SIZE, TIME_TRACK_KEY, dateString, dateStringLimit);

		GridViewBuilder<CustomerDetailViewRecord> y = gridService
				.view(GridView.VW_CUSTOMER_KIBANA, gridQuery);

		AmxApiResponse<CustomerDetailViewRecord, GridMeta> x = y.get();

		BulkRequestBuilder builder = new BulkRequestBuilder();

		Long lastUpdateDateNowStart = lastUpdateDateNow;
		String lastIdNow = Constants.BLANK;
		for (CustomerDetailViewRecord record : x.getResults()) {

			try {
				// Long lastUpdateDate = DateUtil.toUTC(record.getLastUpdateDate());
				Long lastUpdateDate = record.getLastUpdateDate().getTime();
				LOGGER.debug("DIFF {}", lastUpdateDateNow - lastUpdateDate);
				if (lastUpdateDate > lastUpdateDateNow) {
					lastUpdateDateNow = lastUpdateDate;
				}

				BigDecimal customerId = ArgUtil.parseAsBigDecimal(record.getId());
				Date creationDate = ArgUtil.parseAsSimpleDate(record.getLastUpdateDate());
				OracleViewDocument document = new OracleViewDocument();
				document.setId("customer-" + customerId);
				document.setTimestamp(creationDate);
				document.setCustomer(record);
				builder.update(oracleVarsCache.getCustomerIndex(), "customer", document);
				lastIdNow = ArgUtil.parseAsString(document.getId(), Constants.BLANK);
			} catch (Exception e) {
				LOGGER.error("CustomerViewTask Excep", e);
			}
		}

		if (lastIdNow.equalsIgnoreCase(lastId)) {
			// Same data records case, nothing to do
			return;
		}
		lastId = lastIdNow;

		LOGGER.info("Pg:{}, Rcds:{}, Nxt:{}", lastPage, x.getResults().size(), lastUpdateDateNow);
		if (x.getResults().size() > 0) {
			esRepository.bulk(builder.build());
			oracleVarsCache.setCustomerScannedStamp(lastUpdateDateNow, false);
			if ((lastUpdateDateNowStart == lastUpdateDateNow) || (x.getResults().size() == 1000 && lastPage < 10)) {
				doTask(lastPage + 1, lastIdNow);
			}
		} else if (lastUpdateDateNowLimit < (System.currentTimeMillis() - AmxCurConstants.INTERVAL_DAYS)) {
			oracleVarsCache.setCustomerScannedStamp(lastUpdateDateNowLimit, false);
		}
	}

	public void doTaskRev(int lastPage, String lastId) {

		Long lastUpdateDateNow = oracleVarsCache.getCustomerScannedStamp(true);
		Long lastUpdateDateNowFrwrds = oracleVarsCache.getCustomerScannedStamp(false);

		if (lastUpdateDateNow < lastUpdateDateNowFrwrds
				|| lastUpdateDateNow < OracleVarsCache.START_TIME) {
			return;
		}

		Long lastUpdateDateNowLimit = lastUpdateDateNow - TIME_PAGE_DELTA;

		String dateString = GridConstants.GRID_TIME_FORMATTER_JAVA.format(new Date(lastUpdateDateNow));
		String dateStringLimit = GridConstants.GRID_TIME_FORMATTER_JAVA
				.format(new Date(lastUpdateDateNowLimit));

		LOGGER.info("Pg:-{},Time:{} {} - {}", lastPage, lastUpdateDateNow, dateString, dateStringLimit);

		GridQuery gridQuery = getReverseQuery(lastPage, PAGE_SIZE, TIME_TRACK_KEY, dateString, dateStringLimit);

		GridViewBuilder<CustomerDetailViewRecord> y = gridService
				.view(GridView.VW_CUSTOMER_KIBANA, gridQuery);

		AmxApiResponse<CustomerDetailViewRecord, GridMeta> x = y.get();

		BulkRequestBuilder builder = new BulkRequestBuilder();

		Long lastUpdateDateNowStart = lastUpdateDateNow;
		String lastIdNow = Constants.BLANK;
		for (CustomerDetailViewRecord record : x.getResults()) {

			try {
				// Long lastUpdateDate = DateUtil.toUTC(record.getLastUpdateDate());
				Long lastUpdateDate = record.getLastUpdateDate().getTime();
				LOGGER.debug("DIFF {}", lastUpdateDateNow - lastUpdateDate);
				if (lastUpdateDate < lastUpdateDateNow) {
					lastUpdateDateNow = lastUpdateDate;
				}

				BigDecimal customerId = ArgUtil.parseAsBigDecimal(record.getId());
				Date creationDate = ArgUtil.parseAsSimpleDate(record.getLastUpdateDate());
				OracleViewDocument document = new OracleViewDocument();
				document.setId("customer-" + customerId);
				document.setTimestamp(creationDate);
				document.setCustomer(record);
				builder.update(oracleVarsCache.getCustomerIndex(), "customer", document);
				lastIdNow = ArgUtil.parseAsString(document.getId(), Constants.BLANK);
			} catch (Exception e) {
				LOGGER.error("CustomerViewTask Excep", e);
			}
		}

		LOGGER.info("Pg:{}, Rcds:{}, Nxt:{}", lastPage, x.getResults().size(), lastUpdateDateNow);

		if (lastIdNow.equalsIgnoreCase(lastId)) {
			// Same data records case, nothing to do
			return;
		}
		lastId = lastIdNow;

		if (x.getResults().size() > 0) {
			esRepository.bulk(builder.build());
			oracleVarsCache.setCustomerScannedStamp(lastUpdateDateNow, true);
			if ((lastUpdateDateNowStart == lastUpdateDateNow) || (x.getResults().size() == 1000 && lastPage < 2)) {
				doTaskRev(lastPage + 1, lastIdNow);
			}
		} else {
			oracleVarsCache.setCustomerScannedStamp(lastUpdateDateNowLimit, true);
		}
	}

}
