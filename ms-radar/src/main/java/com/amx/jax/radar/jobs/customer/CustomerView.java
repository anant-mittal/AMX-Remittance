package com.amx.jax.radar.jobs.customer;

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
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridView;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.ARadarTask;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.rates.AmxCurConstants;

@Configuration
@EnableScheduling
@Component
@Service
public class CustomerView extends ARadarTask {

	private static final Logger LOGGER = LoggerService.getLogger(CustomerView.class);

	@Autowired
	private ESRepository esRepository;

	@Autowired
	GridServiceClient gridService;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void doTask() {

		GridQuery gridQuery = new GridQuery();
		gridQuery.setPageNo(0);
		gridQuery.setPageSize(100);
		gridQuery.setPaginated(false);

		AmxApiResponse<Map<String, Object>, GridMeta> x = gridService.gridView(GridView.VW_EX_CUSTOMER_INFO, gridQuery,
				new ParameterizedTypeReference<AmxApiResponse<Map<String, Object>, GridMeta>>() {

				});

		for (Map<String, Object> record : x.getResults()) {

		}

	}

}
