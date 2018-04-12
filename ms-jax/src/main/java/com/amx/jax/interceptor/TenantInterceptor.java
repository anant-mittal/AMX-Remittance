package com.amx.jax.interceptor;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.amx.jax.multitenant.TenantContext;
import com.amx.jax.scope.TenantContextHolder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String tnt = TenantContextHolder.currentSite().toString();
		if (StringUtils.isNotBlank(tnt)) {
			logger.info("current tenant: " + tnt);
			TenantContext.setCurrentTenant(tnt);
		}
		String metaInfo = request.getHeader("meta-info");
		if (!StringUtils.isEmpty(metaInfo)) {
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
			};
			HashMap<String, Object> metaInfoMap = new ObjectMapper().readValue(metaInfo, typeRef);
			if (metaInfoMap.get("tenant") != null) {
				String tenant = (String) metaInfoMap.get("tenant");
				TenantContext.setCurrentTenant(tenant);
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		TenantContext.clear();
	}
}
