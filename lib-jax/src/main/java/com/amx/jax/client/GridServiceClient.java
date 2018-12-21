package com.amx.jax.client;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridView;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rest.RestService;

@Component
public class GridServiceClient implements IGridService {
	private static final Logger LOGGER = LoggerService.getLogger(DeviceStateClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<?, GridMeta> gridView(
			GridView gridView,
			GridQuery gridQuery) {
		try {
			String url = appConfig.getJaxURL() + Path.GRID_VIEW;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.pathParam(Params.GRID_VIEW, gridView)
					.post(gridQuery).as(new ParameterizedTypeReference<AmxApiResponse<?, GridMeta>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getStatus : ", e);
			return JaxSystemError.evaluate(e);
		}
	}
}
