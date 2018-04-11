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

import com.amx.amxlib.constant.JaxChannel;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.meta.MetaData;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.CountryBranchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HeaderInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private MetaData metaData;
	
	@Autowired 
	CountryBranchService countryBranchService;
	
	@Autowired
	CompanyService companyService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String metaInfo = request.getHeader("meta-info");
		
		if (!StringUtils.isEmpty(metaInfo)) {
			JaxMetaInfo metaInfoMap = new ObjectMapper().readValue(metaInfo, JaxMetaInfo.class);
			metaData.setDefaultCurrencyId(new BigDecimal(1));// TODO: get currencyId from above countryId from db
			BeanUtils.copyProperties(metaData, metaInfoMap);
			MDC.put("customer-id", metaData.getCustomerId());
			logger.info("Referrer = {}", metaData.getReferrer());
		}
		resolveMetaDataFields();

		return super.preHandle(request, response, handler);
	}

	private void resolveMetaDataFields() {
		if (JaxChannel.ONLINE.equals(metaData.getChannel())) {
			CountryBranch cb = countryBranchService.getOnlineCountryBranch();
			if (cb != null) {
				metaData.setCountryBranchId(cb.getCountryBranchId());
			}
		}
		if(metaData.getLanguageId() != null) {
			ViewCompanyDetails company = companyService.getCompanyDetail(metaData.getLanguageId());
			metaData.setCompanyId(company.getCompanyId());
		}

	}

}
