package com.amx.jax.radar.jobs.customer;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Language;
import com.amx.jax.grid.GridColumn;
import com.amx.jax.grid.GridEnums.FilterDataType;
import com.amx.jax.grid.GridEnums.FilterOperater;
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
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.DateUtil;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class TrnxViewTask extends ARadarTask {

	private static final Logger LOGGER = LoggerService.getLogger(TrnxViewTask.class);

	@Autowired
	private ESRepository esRepository;

	@Autowired
	GridService gridService;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	OracleVarsCache oracleVarsCache;

	private Long lastUpdateDateNow = 0L;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_TASK)
	public void doTask() {
		AppContextUtil.setTenant(TenantContextHolder.currentSite(appConfig.getDefaultTenant()));
		AppContextUtil.init();
		LOGGER.info("Running Task lastUpdateDateNow:{} {}", lastUpdateDateNow,
				new Date(lastUpdateDateNow).toGMTString());

		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxMetaInfo.DEFAULT_COUNTRY_BRANCH_ID));
		lastUpdateDateNow = oracleVarsCache.getTranxScannedStamp();
		GridQuery gridQuery = new GridQuery();
		// gridQuery.setPageNo(lastPage++);
		gridQuery.setPageSize(1000);
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());
		GridColumn column = new GridColumn();
		column.setKey("lastUpdateDate");
		column.setOperator(FilterOperater.GTE);
		column.setDataType(FilterDataType.TIMESTAMP);
		column.setValue(ArgUtil.parseAsString(lastUpdateDateNow, "0"));
		column.setSortDir(SortOrder.ASC);
		gridQuery.getColumns().add(column);
		gridQuery.setSortBy(0);
		gridQuery.setSortOrder(SortOrder.ASC);

		GridViewBuilder<TranxViewRecord> y = gridService
				.view(GridView.VW_KIBANA_TRNX, gridQuery);

		AmxApiResponse<TranxViewRecord, GridMeta> x = y.get();

		BulkRequestBuilder builder = new BulkRequestBuilder();

		for (TranxViewRecord record : x.getResults()) {
			try {
				Long lastUpdateDate = DateUtil.toUTC(record.getLastUpdateDate());
				LOGGER.debug("DIFF {}", lastUpdateDateNow - lastUpdateDate);
				if (lastUpdateDate > lastUpdateDateNow) {
					lastUpdateDateNow = lastUpdateDate;
				}

				BigDecimal appId = ArgUtil.parseAsBigDecimal(record.getId());
				Date creationDate = ArgUtil.parseAsSimpleDate(record.getLastUpdateDate());
				OracleViewDocument document = new OracleViewDocument();
				document.setId("appxn-" + appId);
				document.setTimestamp(creationDate);
				document.setTrnx(record);
				builder.update(oracleVarsCache.getTranxIndex(), "appxn", document);
			} catch (ParseException e) {
				LOGGER.error("TranxViewRecord Excep", e);
			}
		}

		if (x.getResults().size() > 0) {
			esRepository.bulk(builder.build());
			oracleVarsCache.setTranxScannedStamp(lastUpdateDateNow);
		}

	}

}
