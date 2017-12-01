package com.amx.jax.interceptor;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.amx.jax.meta.MetaData;
import com.fasterxml.jackson.core.type.TypeReference;
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
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
			};
			HashMap<String, Object> metaInfoMap = new ObjectMapper().readValue(metaInfo, typeRef);
			if (metaInfoMap.get("country-id") != null) {
				Integer countryId = (Integer) metaInfoMap.get("country-id");
				metaData.setCountryId(new BigDecimal(countryId));
			}
			if (metaInfoMap.get("company-id") != null) {
				Integer companyId = (Integer) metaInfoMap.get("company-id");
				metaData.setCompanyId(new BigDecimal(companyId));
			}
		}

		return super.preHandle(request, response, handler);
	}

}
