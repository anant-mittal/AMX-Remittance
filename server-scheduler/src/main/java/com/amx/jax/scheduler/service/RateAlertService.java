package com.amx.jax.scheduler.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ResponseData;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.scheduler.task.RateAlertTask;
import com.amx.jax.scheduler.task.trigger.RateAlertTrigger;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RateAlertService {

	@Autowired
	RateAlertTrigger rateAlertTrigger;

	@Autowired
	ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Autowired
	RateAlertTask rateAlertTask;

	@Autowired
	JaxMetaInfo jaxMetaInfo;

	public ApiResponse<BooleanResponse> restartRateAlertTask() {
		ApiResponse<BooleanResponse> response = getBlackApiResponse();
		BooleanResponse model = new BooleanResponse();
		rateAlertTrigger.getFuture().cancel(true);
		rateAlertTask.setTenant(jaxMetaInfo.getTenant());
		rateAlertTrigger.setRunNow(true);
		threadPoolTaskScheduler.schedule(rateAlertTask, rateAlertTrigger);
		model.setSuccess(true);
		response.getData().getValues().add(model);
		response.getData().setType(model.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	protected ApiResponse<BooleanResponse> getBlackApiResponse() {
		ApiResponse<BooleanResponse> response = new ApiResponse<BooleanResponse>();
		ResponseData data = new ResponseData();
		List<Object> values = new ArrayList<Object>();
		data.setValues(values);
		response.setData(data);
		return response;
	}
}
