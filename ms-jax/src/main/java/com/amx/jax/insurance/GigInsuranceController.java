package com.amx.jax.insurance;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.request.insurance.CreateOrUpdateNomineeRequest;
import com.amx.jax.model.request.insurance.SaveInsuranceDetailRequest;
import com.amx.jax.model.response.insurance.GigInsuranceDetail;

@RestController
public class GigInsuranceController implements IGigInsuranceService {

	@Autowired
	GigInsuranceService gigInsuranceService;

	@RequestMapping(value = Path.FETCH_INSURANCE_DETAIL, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<GigInsuranceDetail, Object> fetchInsuranceDetail() {
		GigInsuranceDetail result = gigInsuranceService.fetchInsuranceDetail();
		return AmxApiResponse.build(result);
	}

	@RequestMapping(value = Path.SAVE_INSURANCE_DETAIL, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> saveInsuranceDetail(@RequestBody @Valid SaveInsuranceDetailRequest request) {
		gigInsuranceService.saveInsuranceDetail(request);
		return AmxApiResponse.build(new BoolRespModel(true));
	}
}
