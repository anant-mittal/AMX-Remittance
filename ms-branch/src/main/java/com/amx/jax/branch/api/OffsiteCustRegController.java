package com.amx.jax.branch.api;

import static com.amx.amxlib.constant.ApiEndpoint.OFFSITE_CUSTOMER_REG;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.request.GetJaxFieldRequest;
import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.ICustRegService;
import com.amx.jax.api.ARespModel;
import com.amx.jax.api.AResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.service.OffsitCustRegService;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.JaxConditionalFieldRule;
import com.amx.jax.service.CountryService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(OFFSITE_CUSTOMER_REG)
@SuppressWarnings("rawtypes")
public class OffsiteCustRegController /*implements ICustRegService*/ {

	@Autowired
	OffsitCustRegService offsiteCustRegService;
	
	@Autowired
	CountryService countryService;

	/*@Override
	@RequestMapping(value = CustRegApiEndPoints.GET_MODES, method = RequestMethod.GET)
	public AmxApiResponse<BigDecimal, Object> getModes() {
		return offsiteCustRegService.getModes();
	}*/
	
	@RequestMapping(value ="/get-field-list" , method = RequestMethod.POST)
	public AmxApiResponse<List<JaxConditionalFieldRule>, Object> getIdDetailsFields(@RequestBody GetJaxFieldRequest model) {
		return offsiteCustRegService.getIdDetailsFields(model);
	}
	
	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public AmxApiResponse<CountryMasterView,Object> getCountryListResponse() {
		return AmxApiResponse.build(countryService.getCountryListResponse().getResult());
	}	
	
	@RequestMapping(value = "/send-otp", method = RequestMethod.POST)
	public AmxApiResponse<CivilIdOtpModel, Object> validateEmployeeDetails(@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		return  offsiteCustRegService.validateEmployeeDetails(offsiteCustRegModel);
	}	
	
	@RequestMapping(value = "/validate-otp", method = RequestMethod.POST)
	public AmxApiResponse<String, Object> validateOTP(@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		return  offsiteCustRegService.validateOTP(offsiteCustRegModel);
	}	

}
