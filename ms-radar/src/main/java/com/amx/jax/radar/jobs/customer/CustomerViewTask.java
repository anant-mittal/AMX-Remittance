package com.amx.jax.radar.jobs.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.GridServiceClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Language;
import com.amx.jax.grid.GridColumn;
import com.amx.jax.grid.GridEnums.FilterDataType;
import com.amx.jax.grid.GridEnums.FilterOperater;
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridView;
import com.amx.jax.grid.SortOrder;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.ARadarTask;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;

@Configuration
@EnableScheduling
@Component
@Service
public class CustomerViewTask extends ARadarTask {

	private static final Logger LOGGER = LoggerService.getLogger(CustomerViewTask.class);

	@Autowired
	private ESRepository esRepository;

	@Autowired
	GridServiceClient gridService;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	OracleVarsCache oracleVarsCache;

	private Long lastUpdateDateNow = 0L;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_TEST)
	public void doTask() {

		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxMetaInfo.DEFAULT_COUNTRY_BRANCH_ID));

		lastUpdateDateNow = oracleVarsCache.getCustomerScannedStamp();
		GridQuery gridQuery = new GridQuery();
		// gridQuery.setPageNo(lastPage++);
		gridQuery.setPageSize(100);
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

		AmxApiResponse<Map<String, Object>, GridMeta> x = gridService.gridView(GridView.VW_EX_CUSTOMER_INFO, gridQuery,
				new ParameterizedTypeReference<AmxApiResponse<Map<String, Object>, GridMeta>>() {
				});

		for (Map<String, Object> record : x.getResults()) {
			Object lastUpdateDateObj = record.get("lastUpdateDate");

			Long lastUpdateDate = ArgUtil.parseAsLong(lastUpdateDateObj, 0L);
			if (lastUpdateDate > lastUpdateDateNow) {
				lastUpdateDateNow = lastUpdateDate;
			}

			BigDecimal customerId = ArgUtil.parseAsBigDecimal(record.get("customerId"));
			Date creationDate = ArgUtil.parseAsSimpleDate(lastUpdateDateObj);
			CustomerViewDocument document = new CustomerViewDocument();
			document.setId("customer-" + customerId);
			document.setTimestamp(creationDate);
			document.setCustomer(record);
			esRepository.update("oracle-customer", "oracle", document);
		}

		oracleVarsCache.setCustomerScannedStamp(lastUpdateDateNow);

	}

}
