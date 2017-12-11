
package com.amx.jax.ui.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.ui.model.XRateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;
import com.lowagie.text.DocumentException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Remote APIs")
public class RemittController {

	@Autowired
	private JaxService jaxService;

	@Autowired
	private PostManClient postManClient;

	@ApiOperation(value = "Returns transaction history")
	@RequestMapping(value = "/api/user/tranx/history", method = { RequestMethod.POST })
	public ResponseWrapper<List<TransactionHistroyDTO>> tranxhistory() {
		ResponseWrapper<List<TransactionHistroyDTO>> wrapper = new ResponseWrapper<List<TransactionHistroyDTO>>(
				jaxService.setDefaults().getRemitClient().getTransactionHistroy("2017", null, null, null).getResults());
		return wrapper;
	}

	@ApiOperation(value = "Returns transaction reciept")
	@RequestMapping(value = "/api/user/tranx/report", method = { RequestMethod.POST })
	public ResponseWrapper<RemittanceReceiptSubreport> tranxreport(@RequestBody TransactionHistroyDTO tranxDTO,
			@RequestParam(required = false) BigDecimal collectionDocumentNo,
			@RequestParam(required = false) BigDecimal collectionDocumentFinYear,
			@RequestParam(required = false) BigDecimal collectionDocumentCode,
			@RequestParam(required = false) BigDecimal customerReference, @RequestParam(required = false) Boolean skipd)
			throws IOException, DocumentException {
		RemittanceReceiptSubreport rspt = jaxService.setDefaults().getRemitClient().report(tranxDTO).getResult();
		ResponseWrapper<RemittanceReceiptSubreport> wrapper = new ResponseWrapper<RemittanceReceiptSubreport>(rspt);
		if (skipd == null || skipd.booleanValue() == false) {
			postManClient.downloadPDF("RemittanceReceiptReport", wrapper, "RemittanceReceiptReport"
					+ tranxDTO.getCollectionDocumentFinYear() + "-" + tranxDTO.getCollectionDocumentNo() + ".pdf");
		}
		return wrapper;
	}

	@RequestMapping(value = "/api/meta/ccy/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<CurrencyMasterDTO>> ccyList() {
		return new ResponseWrapper<List<CurrencyMasterDTO>>(
				jaxService.setDefaults().getMetaClient().getAllOnlineCurrency().getResults());
	}

	@RequestMapping(value = "/api/remitt/xrate", method = { RequestMethod.POST })
	public ResponseWrapper<XRateData> xrate(@RequestParam(required = false) BigDecimal forCur,
			@RequestParam(required = false) String banBank, @RequestParam(required = false) BigDecimal domAmount)
			throws ResourceNotFoundException, InvalidInputException {
		ResponseWrapper<XRateData> wrapper = new ResponseWrapper<XRateData>(new XRateData());

		wrapper.getData().setDomCur(new BigDecimal(JaxService.DEFAULT_CURRENCY_ID));

		if (forCur != null) {
			ExchangeRateResponseModel resp = jaxService.setDefaults().getxRateClient()
					.getExchangeRate(new BigDecimal(JaxService.DEFAULT_CURRENCY_ID), forCur, domAmount, null)
					.getResult();
			wrapper.getData().setForXRate(resp.getRate());
			wrapper.getData().setForAmount(resp.getRate());
			wrapper.getData().setBeneBanks(resp.getBankWiseRates());

		}
		
		return wrapper;
	}

}
