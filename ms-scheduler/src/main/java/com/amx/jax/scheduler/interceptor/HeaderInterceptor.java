package com.amx.jax.scheduler.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HeaderInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	JaxMetaInfo jaxMetaInfo;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String metaInfo = request.getHeader("meta-info");
		if (!StringUtils.isEmpty(metaInfo)) {
			JaxMetaInfo metaInfoMap = new ObjectMapper().readValue(metaInfo, JaxMetaInfo.class);
			BeanUtils.copyProperties(jaxMetaInfo, metaInfoMap);
			MDC.put("customer-id", jaxMetaInfo.getCustomerId());
		}

		return super.preHandle(request, response, handler);
	}

}
