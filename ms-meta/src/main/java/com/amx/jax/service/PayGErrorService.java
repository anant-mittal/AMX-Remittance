package com.amx.jax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
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
	
	public ApiResponse getPaygErrorResponse() {
		
		List<PaygErrorMaster> paygErrorList = paygErrorDao.getPaygErrorList();
		
		ApiResponse response = getBlackApiResponse();

		if (paygErrorList.isEmpty()) {
			throw new GlobalException("PayG Error are not available");
		} else {
			response.getData().getValues().addAll(paygErrorList);
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("payg-error");
		return response;
	}
	
	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return "payg-error";
	}

}