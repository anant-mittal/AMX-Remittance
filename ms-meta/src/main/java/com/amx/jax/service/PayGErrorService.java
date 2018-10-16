package com.amx.jax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dao.PayGErrorDao;
import com.amx.jax.dbmodel.meta.PaygErrorMaster;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.AbstractService;

/**
 * @author Shivaraj
 *
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PayGErrorService extends AbstractService {

	@Autowired
	MetaData meta;

	@Autowired
	PayGErrorDao paygErrorDao;

	@Deprecated
	public AmxApiResponse<PaygErrorMaster, Object> getPaygErrorResponse() {
		List<PaygErrorMaster> paygErrorList = paygErrorDao.getPaygErrorList();
		return AmxApiResponse.buildList(paygErrorList);
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return "payg-error";
	}

}