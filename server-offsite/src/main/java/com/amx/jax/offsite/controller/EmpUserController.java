package com.amx.jax.offsite.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.JaxStompClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.dict.UserClient.AppType;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.open.model.EmployeeMetaData;

import io.swagger.annotations.Api;

/**
 * The Class MetaController.
 */
@RestController
@Api(value = "Employee User APIs")
public class EmpUserController {

	private static final Logger LOGGER = LoggerService.getLogger(EmpUserController.class);

	@Autowired
	private CommonHttpRequest httpService;

	@Autowired
	MetaClient metaClient;

	@Autowired
	AppConfig appConfig;

	@RequestMapping(value = "/pub/user/meta", method = { RequestMethod.POST, RequestMethod.GET })
	public AmxApiResponse<EmployeeMetaData, ?> getMeta(@RequestParam(required = false) AppType appType,
			@RequestParam(required = false) String appVersion) {
		EmployeeMetaData empMeta = new EmployeeMetaData();
		empMeta.setTenant(AppContextUtil.getTenant());
		empMeta.setTenantCode(AppContextUtil.getTenant().getCode());
		empMeta.setLang(httpService.getLanguage());
		empMeta.setCdnUrl(appConfig.getCdnURL());
		empMeta.setValidSession(true);
		return AmxApiResponse.build(empMeta);
	}

	@Autowired
	JaxStompClient jaxStompClient;

	@RequestMapping(value = "/pub/user/stomp/test", method = { RequestMethod.GET })
	public AmxApiResponse<?, ?> getMeta(@RequestParam(required = true) BigDecimal empId,
			@RequestParam(required = true) BigDecimal customerId) {
		jaxStompClient.publishOnCallCustomerStatus(empId, customerId);
		return AmxApiResponse.build();
	}

}
