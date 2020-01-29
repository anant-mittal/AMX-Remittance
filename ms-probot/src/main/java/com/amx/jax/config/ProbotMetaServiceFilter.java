package com.amx.jax.config;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConstants;
import com.amx.jax.pricer.meta.ProbotMetaInfo;
import com.amx.jax.rest.IMetaRequestInFilter;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProbotMetaServiceFilter implements IMetaRequestInFilter<ProbotMetaInfo> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProbotMetaInfo metaInfo;

	public void importMeta(ProbotMetaInfo meta, HttpServletRequest req) {

		String metaInfoStr = req.getHeader(AppConstants.META_XKEY);

		if (!StringUtils.isEmpty(metaInfoStr)) {
			try {
				metaInfo = new ObjectMapper().readValue(metaInfoStr, ProbotMetaInfo.class);
			} catch (IOException e) {
				logger.error("Meta Exception", e);
			}

		}
		// resolveMetaDataFields();
	}

	@Override
	public void inFilter(ProbotMetaInfo metaInfoMap) {
		if (!ArgUtil.isEmpty(metaInfoMap)) {
			try {
				BeanUtils.copyProperties(metaInfo, metaInfoMap);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error("Meta Exception", e);
			}
		}
	}

	/*
	 * private void resolveMetaDataFields() { if
	 * (JaxChannel.ONLINE.equals(metaData.getChannel())) { CountryBranch cb =
	 * countryBranchService.getOnlineCountryBranch(); if (cb != null) {
	 * metaData.setCountryBranchId(cb.getCountryBranchId()); } }
	 * 
	 * if (ArgUtil.isEmpty(metaData.getCountryId())) {
	 * metaData.setCountryId(amxConfig.getDefaultCountryId()); } if
	 * (ArgUtil.isEmpty(metaData.getLanguageId())) {
	 * metaData.setLanguageId(amxConfig.getDefaultLanguageId()); } if
	 * (ArgUtil.isEmpty(metaData.getCompanyId())) {
	 * metaData.setCompanyId(amxConfig.getDefaultCompanyId()); } if
	 * (ArgUtil.isEmpty(metaData.getDefaultCurrencyId())) {
	 * metaData.setDefaultCurrencyId(amxConfig.getDefaultCurrencyId()); } if
	 * (ArgUtil.isEmpty(metaData.getCountryBranchId())) {
	 * metaData.setCountryBranchId(amxConfig.getDefaultBranchId()); } if
	 * (metaData.getLanguageId() != null) { ViewCompanyDetails company =
	 * companyService.getCompanyDetail(metaData.getLanguageId());
	 * metaData.setCompanyId(company.getCompanyId()); BigDecimal defaultCurrencyId =
	 * currencyMasterService
	 * .getCurrencyMasterByCountryId(company.getApplicationCountryId()).get(0).
	 * getCurrencyId(); metaData.setDefaultCurrencyId(defaultCurrencyId);
	 * metaData.setCountryId(company.getApplicationCountryId()); }
	 * 
	 * }
	 */

	@Override
	public Class<ProbotMetaInfo> getMetaClass() {
		return ProbotMetaInfo.class;
	}

}
