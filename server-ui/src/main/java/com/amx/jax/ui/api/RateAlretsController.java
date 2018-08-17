
package com.amx.jax.ui.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.RateAlertDTO;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;

import io.swagger.annotations.Api;

/**
 * The Class RateAlretsController.
 */
@RestController
@Api(value = "Rate Alerts Apis")
public class RateAlretsController {

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/**
	 * List of alerts.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/xtrate/alert/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<RateAlertDTO>> listOfAlerts() {
		ResponseWrapper<List<RateAlertDTO>> wrapper = new ResponseWrapper<List<RateAlertDTO>>();

		List<RateAlertDTO> results = jaxService.setDefaults().getRateAlertClient().getRateAlertForCustomer()
				.getResults();
		wrapper.setData(results);

		return wrapper;
	}

	/**
	 * Save alert.
	 *
	 * @param rateAlertDTO
	 *            the rate alert DTO
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/xtrate/alert/save", method = { RequestMethod.POST, RequestMethod.POST })
	public ResponseWrapper<RateAlertDTO> saveAlert(@RequestBody RateAlertDTO rateAlertDTO) {
		ResponseWrapper<RateAlertDTO> wrapper = new ResponseWrapper<RateAlertDTO>();

		RateAlertDTO results = jaxService.setDefaults().getRateAlertClient().saveRateAlert(rateAlertDTO).getResult();
		wrapper.setData(results);

		return wrapper;
	}

	/**
	 * Delete alert.
	 *
	 * @param rateAlertDTO
	 *            the rate alert DTO
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/xtrate/alert/delete", method = { RequestMethod.POST })
	public ResponseWrapper<RateAlertDTO> deleteAlert(@RequestBody RateAlertDTO rateAlertDTO) {
		ResponseWrapper<RateAlertDTO> wrapper = new ResponseWrapper<RateAlertDTO>();
		RateAlertDTO results = jaxService.setDefaults().getRateAlertClient().deleteRateAlert(rateAlertDTO).getResult();
		wrapper.setData(results);
		return wrapper;
	}

}
