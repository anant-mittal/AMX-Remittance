package com.amx.jax.config;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AmxConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.meta.MetaData;
import com.amx.jax.rest.IMetaRequestInFilter;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.CountryBranchService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JaxMetaServiceFilter implements IMetaRequestInFilter<JaxMetaInfo> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MetaData metaData;

	@Autowired
	private AmxConfig amxConfig;

	@Autowired
	CountryBranchService countryBranchService;

	@Autowired
	CompanyService companyService;

	@Autowired
	CurrencyMasterService currencyMasterService;

	public void importMeta(JaxMetaInfo meta, HttpServletRequest req) {
		String metaInfo = req.getHeader(AppConstants.META_XKEY);

		if (!StringUtils.isEmpty(metaInfo)) {
			JaxMetaInfo metaInfoMap;
			try {
				metaInfoMap = new ObjectMapper().readValue(metaInfo, JaxMetaInfo.class);
				metaData.setDefaultCurrencyId(amxConfig.getDefaultCurrencyId());
				BeanUtils.copyProperties(metaData, metaInfoMap);
				MDC.put("customer-id", metaData.getCustomerId());
				logger.debug("Referrer = {}", metaData.getReferrer());
			} catch (IOException | IllegalAccessException | InvocationTargetException e) {
				logger.error("Meta Exception", e);
			}

		}
		resolveMetaDataFields();
	}

	@Override
	public void inFilter(JaxMetaInfo metaInfoMap) {
		if (!ArgUtil.isEmpty(metaInfoMap)) {
			try {
				metaData.setDefaultCurrencyId(amxConfig.getDefaultCurrencyId());
				BeanUtils.copyProperties(metaData, metaInfoMap);
				MDC.put("customer-id", metaData.getCustomerId());
				logger.debug("Referrer = {}", metaData.getReferrer());
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error("Meta Exception", e);
			}
		}
		resolveMetaDataFields();
	}

	private void resolveMetaDataFields() {
		if (JaxChannel.ONLINE.equals(metaData.getChannel())) {
			CountryBranchMdlv1 cb = countryBranchService.getOnlineCountryBranch();
			if (cb != null) {
				metaData.setCountryBranchId(cb.getCountryBranchId());
			}
		}

		if (ArgUtil.isEmpty(metaData.getCountryId())) {
			metaData.setCountryId(amxConfig.getDefaultCountryId());
		}
		if (ArgUtil.isEmpty(metaData.getLanguageId())) {
			metaData.setLanguageId(amxConfig.getDefaultLanguageId());
		}
		if (ArgUtil.isEmpty(metaData.getCompanyId())) {
			metaData.setCompanyId(amxConfig.getDefaultCompanyId());
		}
		if (ArgUtil.isEmpty(metaData.getDefaultCurrencyId())) {
			metaData.setDefaultCurrencyId(amxConfig.getDefaultCurrencyId());
		}
		if (ArgUtil.isEmpty(metaData.getCountryBranchId())) {
			metaData.setCountryBranchId(amxConfig.getDefaultBranchId());
		}
		if (metaData.getLanguageId() != null) {
			ViewCompanyDetails company = companyService.getCompanyDetail(metaData.getLanguageId());
			metaData.setCompanyId(company.getCompanyId());
			BigDecimal defaultCurrencyId = currencyMasterService
					.getCurrencyMasterByCountryId(company.getApplicationCountryId()).get(0).getCurrencyId();
			metaData.setDefaultCurrencyId(defaultCurrencyId);
			metaData.setCountryId(company.getApplicationCountryId());
		}

	}

	@Override
	public Class<JaxMetaInfo> getMetaClass() {
		return JaxMetaInfo.class;
	}

}
