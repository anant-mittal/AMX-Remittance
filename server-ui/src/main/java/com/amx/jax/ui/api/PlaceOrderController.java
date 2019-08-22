
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
	
	@ApiOperation(value = "List of All bnfcries for PlaceOrder")
	@RequestMapping(value = "/api/po/bnfcry/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<BeneficiaryListDTO>> beneList() {
		ResponseWrapper<List<BeneficiaryListDTO>> wrapper = new ResponseWrapper<>();
		wrapper.setData(jaxService.setDefaults().getBeneClient().getBeneficiaryList(new BigDecimal(0)).getResults());
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

	/**
	 * 
	 * @param remitTranxnReqModel
	 * @return
	 */
	@RequestMapping(value = "/api/po/calc", method = { RequestMethod.POST })
	public ResponseWrapper<RemittanceTransactionResponsetModel> calculate(
			@RequestBody RemittanceTransactionRequestModel remitTranxnReqModel) {

		ResponseWrapper<RemittanceTransactionResponsetModel> wrapper = new ResponseWrapper<RemittanceTransactionResponsetModel>();
		RemittanceTransactionResponsetModel respTxMdl = jaxService.setDefaults().getRemitClient()
				.calcEquivalentAmount(remitTranxnReqModel).getResult();
		wrapper.setData(respTxMdl);
		wrapper.setMeta(
				jaxService.setDefaults().getRemitClient().getPurposeOfTransactions(remitTranxnReqModel).getResults());
		return wrapper;

	}
}
