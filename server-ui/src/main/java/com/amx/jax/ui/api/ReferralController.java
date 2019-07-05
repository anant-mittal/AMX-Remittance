package com.amx.jax.ui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.LinkDTO;
import com.amx.amxlib.model.LinkResponseModel;
import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.ReferralDTO;
import com.amx.amxlib.model.ReferralResponseModel;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;

import io.swagger.annotations.Api;

/**
 * The Class PlaceOrderController.
 */
@RestController
@Api(value = "Referral Apis")
public class ReferralController {
	@Autowired
	JaxService jaxService;
	
	@RequestMapping(value = "/api/referral/get", method = { RequestMethod.GET })
	public ResponseWrapper<ReferralResponseModel> getRefferal() {
		ResponseWrapper<ReferralResponseModel> wrapper = new ResponseWrapper<ReferralResponseModel>();
		ReferralResponseModel results = jaxService.setDefaults().getReferralClient().getRefferal()
				.getResult();
		wrapper.setData(results);
		return wrapper;
	}
	
	@RequestMapping(value = "/api/link/get", method = { RequestMethod.POST })
	public ResponseWrapper<LinkResponseModel> getRefferalLink(@RequestBody LinkDTO linkDTO) {
		ResponseWrapper<LinkResponseModel> wrapper = new ResponseWrapper<LinkResponseModel>();
		LinkResponseModel results = jaxService.setDefaults().getReferralClient().getReferralLink(linkDTO)
				.getResult();
		wrapper.setData(results);
		return wrapper;
	}
	
	@RequestMapping(value = "/api/update/link", method = { RequestMethod.POST })
	public ResponseWrapper<LinkDTO> getUpdateRefferalLink(@RequestBody LinkDTO linkDTO) {
		ResponseWrapper<LinkDTO> wrapper = new ResponseWrapper<LinkDTO>();
		LinkDTO results = jaxService.setDefaults().getReferralClient().updateReferralLink(linkDTO)
				.getResult();
		wrapper.setData(results);
		return wrapper;
	}
}
