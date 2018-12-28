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
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridView;
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

	private Integer lastPage = 0;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_TEST)
	public void doTask() {

		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxMetaInfo.DEFAULT_COUNTRY_BRANCH_ID));

		GridQuery gridQuery = new GridQuery();
		gridQuery.setPageNo(lastPage++);
		gridQuery.setPageSize(100);
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());

		AmxApiResponse<Map<String, Object>, GridMeta> x = gridService.gridView(GridView.VW_EX_CUSTOMER_INFO, gridQuery,
				new ParameterizedTypeReference<AmxApiResponse<Map<String, Object>, GridMeta>>() {
				});
		for (Map<String, Object> record : x.getResults()) {
			BigDecimal customerId = ArgUtil.parseAsBigDecimal(record.get("customerId"));
			Date creationDate = ArgUtil.parseAsSimpleDate(record.get("lastUpdateDate"));
			CustomerViewDocument document = new CustomerViewDocument();
			document.setId("customer-" + customerId);
			document.setTimestamp(creationDate);
			document.setCustomer(record);
			esRepository.insert("oracle-customer", "oracle", document);
		}
		if (x.getResults().size() < 100) {
			lastPage = 0;
		}

	}

}
