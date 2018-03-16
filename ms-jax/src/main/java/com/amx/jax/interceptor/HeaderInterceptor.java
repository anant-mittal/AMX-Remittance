package com.amx.jax.interceptor;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.meta.MetaData;
import com.amx.jax.scope.TenantContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HeaderInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private MetaData metaData;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String metaInfo = request.getHeader("meta-info");
		if (!StringUtils.isEmpty(metaInfo)) {
			JaxMetaInfo metaInfoMap = new ObjectMapper().readValue(metaInfo, JaxMetaInfo.class);
			metaData.setDefaultCurrencyId(new BigDecimal(1));// TODO: get currencyId from above countryId from db
			BeanUtils.copyProperties(metaData, metaInfoMap);
			TenantContextHolder.setCurrent(metaData.getTenant());
			MDC.put("customer-id", metaData.getCustomerId());
			logger.info("Referrer = {}", metaData.getReferrer());
		}

		return super.preHandle(request, response, handler);
	}

}
