
package com.amx.jax.ui.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;

import io.swagger.annotations.Api;

/**
 * The Class PlaceOrderController.
 */
@RestController
@Api(value = "Rate Alerts Apis")
public class PlaceOrderController {

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/**
	 * List of place orders.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/po/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<PlaceOrderDTO>> listOfPlaceOrders() {
		ResponseWrapper<List<PlaceOrderDTO>> wrapper = new ResponseWrapper<List<PlaceOrderDTO>>();
		List<PlaceOrderDTO> results = jaxService.setDefaults().getPlaceOrderClient().getPlaceOrderForCustomer()
				.getResults();
		wrapper.setData(results);
		return wrapper;
	}

	/**
	 * Save place order.
	 *
	 * @param placeOrderDTO
	 *            the place order DTO
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/po/save", method = { RequestMethod.POST })
	public ResponseWrapper<PlaceOrderDTO> savePlaceOrder(@RequestBody PlaceOrderDTO placeOrderDTO) {
		ResponseWrapper<PlaceOrderDTO> wrapper = new ResponseWrapper<PlaceOrderDTO>();
		PlaceOrderDTO results = jaxService.setDefaults().getPlaceOrderClient().savePlaceOrder(placeOrderDTO)
				.getResult();
		wrapper.setData(results);
		return wrapper;
	}

	/**
	 * Update place order.
	 *
	 * @param placeOrderDTO
	 *            the place order DTO
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/po/update", method = { RequestMethod.POST })
	public ResponseWrapper<PlaceOrderDTO> updatePlaceOrder(@RequestBody PlaceOrderDTO placeOrderDTO) {
		ResponseWrapper<PlaceOrderDTO> wrapper = new ResponseWrapper<PlaceOrderDTO>();
		PlaceOrderDTO results = jaxService.setDefaults().getPlaceOrderClient().updatePlaceOrder(placeOrderDTO)
				.getResult();
		wrapper.setData(results);
		return wrapper;
	}

	/**
	 * Delete place order.
	 *
	 * @param placeOrderDTO
	 *            the place order DTO
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/po/delete", method = { RequestMethod.POST })
	public ResponseWrapper<PlaceOrderDTO> deletePlaceOrder(@RequestBody PlaceOrderDTO placeOrderDTO) {
		ResponseWrapper<PlaceOrderDTO> wrapper = new ResponseWrapper<PlaceOrderDTO>();
		PlaceOrderDTO results = jaxService.setDefaults().getPlaceOrderClient().deletePlaceOrder(placeOrderDTO)
				.getResult();
		wrapper.setData(results);
		return wrapper;
	}

}
