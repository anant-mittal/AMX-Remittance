package com.amx.jax.client.remittance;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.fx.FcSaleOrderClient;
import com.amx.jax.client.fx.IFxOrderDelivery.Params;
import com.amx.jax.client.fx.IFxOrderDelivery.Path;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;
import com.amx.jax.rest.RestService;

public class RemittanceClient  implements IRemittanceService{
	private static final Logger LOGGER = Logger.getLogger(FcSaleOrderClient.class);

	@Autowired
	RestService restService;
	
	@Autowired
	AppConfig appConfig;
	
}

