package com.amx.jax.interceptor;


import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.meta.MetaData;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HeaderInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private MetaData metaData;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String metaInfo = request.getHeader("meta-info");
		if (!StringUtils.isEmpty(metaInfo)) {
			JaxMetaInfo metaInfoMap = new ObjectMapper().readValue(metaInfo, JaxMetaInfo.class);
//			metaData.setChannel(metaInfoMap.getChannel());
//			metaData.setCompanyId(metaInfoMap.getCompanyId());
//			metaData.setCountryId(metaInfoMap.getCountryId());
			metaData.setDefaultCurrencyId(new BigDecimal(1));// TODO: get currencyId from above countryId from db
			BeanUtils.copyProperties(metaData, metaInfoMap);
			MDC.put("customer-id", metaData.getCustomerId());
		}

		return super.preHandle(request, response, handler);
	}

}
