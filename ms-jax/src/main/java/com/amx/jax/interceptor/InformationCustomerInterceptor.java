package com.amx.jax.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.amx.amxlib.constant.ApiEndpoint;
import com.amx.jax.client.fx.IFxOrderService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.manager.CustomerFlagManager;

@Component
public class InformationCustomerInterceptor extends HandlerInterceptorAdapter {

	public static final String[] INFO_ONLY_ACCESS_URI = { ApiEndpoint.REMIT_API_ENDPOINT + "/save-application/",
			ApiEndpoint.BENE_API_ENDPOINT + "/trnx/addbene/commit/", IFxOrderService.Path.FCSALE_SAVE_PAYNOW };

	@Autowired
	CustomerFlagManager customerFlagManager;
	@Autowired
	MetaData metaData;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String uri = request.getRequestURI();
		boolean isInfoOnlyApi = false;
		for (String path : INFO_ONLY_ACCESS_URI) {
			if (uri.contains(path)) {
				isInfoOnlyApi = true;
			}
		}
		if (isInfoOnlyApi) {
			customerFlagManager.validateInformationOnlyCustomer(metaData.getCustomerId());
		}
		return true;
	}

}
