
package com.amx.jax.ui.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.meta.model.SourceOfIncomeDto;
import com.amx.amxlib.model.RateAlertDTO;
import com.amx.jax.ui.beans.TenantBean;
import com.amx.jax.ui.response.ResponseMeta;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.AppEnvironment;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.session.GuestSession;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Meta APIs")
public class RateAlretsController {

	@Autowired
	private JaxService jaxService;

	@Autowired
	AppEnvironment env;

	@Autowired
	TenantBean tenantBean;

	@Autowired
	GuestSession guestSession;

	@RequestMapping(value = "/api/xtrate/alert/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<RateAlertDTO>> listOfAlerts() {
		ResponseWrapper<List<RateAlertDTO>> wrapper = new ResponseWrapper<List<RateAlertDTO>>();

		List<RateAlertDTO> results = jaxService.setDefaults().getRateAlertClient().getRateAlertForCustomer(null)
				.getResults();
		wrapper.setData(results);

		return wrapper;
	}

	@RequestMapping(value = "/api/xtrate/alert/save", method = { RequestMethod.POST, RequestMethod.POST })
	public ResponseWrapper<RateAlertDTO> saveAlert(@RequestBody RateAlertDTO rateAlertDTO) {
		ResponseWrapper<RateAlertDTO> wrapper = new ResponseWrapper<RateAlertDTO>();

		RateAlertDTO results = jaxService.setDefaults().getRateAlertClient().saveRateAlert(rateAlertDTO).getResult();
		wrapper.setData(results);

		return wrapper;
	}

	@RequestMapping(value = "/api/xtrate/alert/delete", method = { RequestMethod.POST })
	public ResponseWrapper<RateAlertDTO> deleteAlert(@RequestBody RateAlertDTO rateAlertDTO) {
		ResponseWrapper<RateAlertDTO> wrapper = new ResponseWrapper<RateAlertDTO>();
		RateAlertDTO results = jaxService.setDefaults().getRateAlertClient().deleteRateAlert(rateAlertDTO).getResult();
		wrapper.setData(results);
		return wrapper;
	}

}
