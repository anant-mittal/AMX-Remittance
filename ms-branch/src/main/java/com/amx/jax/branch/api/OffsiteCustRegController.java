package com.amx.jax.branch.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.service.OffsitCustRegService;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.service.CountryService;

@RestController
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

	/*@Override
	@ApiOperation("Get ID Fields List for Identity")
	@RequestMapping(value = CustRegApiEndPoints.GET_ID_FIELDS, method = RequestMethod.POST)
	public AmxApiResponse<ARespModel, Object> getIdDetailsFields(@RequestBody RegModeModel regModeModel) {
		return offsiteCustRegService.getIdDetailsFields(regModeModel);
	}*/
	
	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public AmxApiResponse<CountryMasterView,Object> getCountryListResponse() {
		return AmxApiResponse.build(countryService.getCountryListResponse().getResult());
	}	
	
	@RequestMapping(value = "/validate-employee-details", method = RequestMethod.POST)
	public ApiResponse validateEmployeeDetails(@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		return  offsiteCustRegService.validateEmployeeDetails(offsiteCustRegModel);
	}	

}
