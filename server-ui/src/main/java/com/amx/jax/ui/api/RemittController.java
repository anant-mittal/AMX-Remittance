package com.amx.jax.ui.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.ui.beans.TenantBean;
import com.amx.jax.ui.beans.UserBean;
import com.amx.jax.ui.model.XRateData;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;
import com.bootloaderjs.JsonUtil;
import com.lowagie.text.DocumentException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Remote APIs")
public class RemittController {

	@Autowired
	private JaxService jaxService;

	@Autowired
	private TenantBean tenantBean;

	@Autowired
	private UserBean userBean;

	@Autowired
	private PostManClient postManClient;

	@ApiOperation(value = "Returns transaction history")
	@RequestMapping(value = "/api/user/tranx/history", method = { RequestMethod.POST })
	public ResponseWrapper<List<TransactionHistroyDTO>> tranxhistory() {
		ResponseWrapper<List<TransactionHistroyDTO>> wrapper = new ResponseWrapper<List<TransactionHistroyDTO>>(
				jaxService.setDefaults().getRemitClient().getTransactionHistroy("2017", null, null, null).getResults());
		return wrapper;
	}

	@ApiOperation(value = "Returns transaction history")
	@RequestMapping(value = "/api/user/tranx/print_history", method = { RequestMethod.POST })
	public ResponseWrapper<List<TransactionHistroyDTO>> printHistory(
			@RequestBody ResponseWrapper<List<TransactionHistroyDTO>> wrapper) throws IOException, DocumentException {
		postManClient.downloadPDF("RemittanceStatment", wrapper, "RemittanceStatment.pdf");
		return wrapper;
	}

	@ApiOperation(value = "Returns transaction reciept")
	@RequestMapping(value = "/api/user/tranx/report", method = { RequestMethod.POST })
	public String tranxreport(@RequestBody TransactionHistroyDTO tranxDTO,
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
		return JsonUtil.toJson(wrapper);
	}

	@ApiOperation(value = "Returns transaction reciept")
	@RequestMapping(value = "/api/user/tranx/report.{ext}", method = { RequestMethod.GET })
	public @ResponseBody String tranxreportExt(@RequestParam(required = false) BigDecimal collectionDocumentNo,
			@RequestParam(required = false) BigDecimal collectionDocumentFinYear,
			@RequestParam(required = false) BigDecimal collectionDocumentCode,
			@RequestParam(required = false) BigDecimal customerReference, @PathVariable("ext") String ext)
			throws IOException, DocumentException {

		TransactionHistroyDTO tranxDTO = new TransactionHistroyDTO();
		tranxDTO.setCollectionDocumentNo(collectionDocumentNo);
		tranxDTO.setCollectionDocumentFinYear(collectionDocumentFinYear);
		tranxDTO.setCollectionDocumentCode(collectionDocumentCode);
		tranxDTO.setCustomerReference(customerReference);

		RemittanceReceiptSubreport rspt = jaxService.setDefaults().getRemitClient().report(tranxDTO).getResult();
		ResponseWrapper<RemittanceReceiptSubreport> wrapper = new ResponseWrapper<RemittanceReceiptSubreport>(rspt);
		if ("pdf".equals(ext)) {
			postManClient.createPDF("RemittanceReceiptReport", wrapper);
			return null;
		} else if ("html".equals(ext)) {
			return postManClient.processTemplate("RemittanceReceiptReport", wrapper, "RemittanceReceiptReport")
					.getContent();
		} else {
			return JsonUtil.toJson(wrapper);
		}
	}

	@RequestMapping(value = "/api/remitt/xrate", method = { RequestMethod.POST })
	public ResponseWrapper<XRateData> xrate(@RequestParam(required = false) BigDecimal forCur,
			@RequestParam(required = false) String banBank, @RequestParam(required = false) BigDecimal domAmount) {
		ResponseWrapper<XRateData> wrapper = new ResponseWrapper<XRateData>(new XRateData());

		CurrencyMasterDTO domCur = tenantBean.getDomCurrency();
		CurrencyMasterDTO forCurcy = null;

		wrapper.getData().setDomCur(domCur);

		if (forCur == null) {
			forCurcy = userBean.getDefaultForCurrency();
		} else {
			for (CurrencyMasterDTO currency : tenantBean.getOnlineCurrencies()) {
				if (currency.getCurrencyId().equals(forCur)) {
					forCurcy = currency;
					break;
				}
			}
		}

		if (forCurcy != null) {
			wrapper.getData().setForCur(forCurcy);
			ExchangeRateResponseModel resp;
			try {
				resp = jaxService.setDefaults().getxRateClient()
						.getExchangeRate(new BigDecimal(JaxService.DEFAULT_CURRENCY_ID), forCurcy.getCurrencyId(),
								domAmount, null)
						.getResult();
				wrapper.getData().setForXRate(resp.getExRateBreakup().getInverseRate());
				wrapper.getData().setDomXRate(resp.getExRateBreakup().getRate());
				wrapper.getData().setForAmount(resp.getExRateBreakup().getConvertedFCAmount());
				wrapper.getData().setBeneBanks(resp.getBankWiseRates());
			} catch (ResourceNotFoundException | InvalidInputException e) {
				wrapper.setMessage(ResponseStatus.ERROR, e);
			}
		}
		return wrapper;
	}

	@RequestMapping(value = "/api/remitt/default", method = { RequestMethod.POST })
	public ResponseWrapper<RemittancePageDto> bnfcryCheck(@RequestParam(required = false) BigDecimal beneId,
			@RequestParam(required = false) BigDecimal transactionId) {
		ResponseWrapper<RemittancePageDto> wrapper = new ResponseWrapper<RemittancePageDto>();
		RemittancePageDto remittancePageDto = jaxService.setDefaults().getBeneClient()
				.defaultBeneficiary(beneId, transactionId).getResult();

		BigDecimal forCurId = remittancePageDto.getBeneficiaryDto().getCurrencyId();

		for (CurrencyMasterDTO currency : tenantBean.getOnlineCurrencies()) {
			if (currency.getCurrencyId().equals(forCurId)) {
				remittancePageDto.setForCur(currency);
				break;
			}
		}
		remittancePageDto.setDomCur(tenantBean.getDomCurrency());

		wrapper.setData(remittancePageDto);
		return wrapper;
	}

	@RequestMapping(value = "/api/remitt/purpose/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<PurposeOfTransactionModel>> bnfcryCheck(@RequestParam BigDecimal beneId) {
		ResponseWrapper<List<PurposeOfTransactionModel>> wrapper = new ResponseWrapper<List<PurposeOfTransactionModel>>();
		wrapper.setData(jaxService.setDefaults().getRemitClient().getPurposeOfTransactions(beneId).getResults());
		return wrapper;
	}

	@RequestMapping(value = "/api/remitt/tranxrate", method = { RequestMethod.POST })
	public ResponseWrapper<RemittanceTransactionResponsetModel> bnfcryCheck(
			@RequestBody RemittanceTransactionRequestModel request) {
		ResponseWrapper<RemittanceTransactionResponsetModel> wrapper = new ResponseWrapper<RemittanceTransactionResponsetModel>();
		try {
			RemittanceTransactionResponsetModel respTxMdl = jaxService.setDefaults().getRemitClient()
					.validateTransaction(request).getResult();
			wrapper.setData(respTxMdl);
		} catch (RemittanceTransactionValidationException | LimitExeededException e) {
			wrapper.setMessage(ResponseStatus.ERROR, e);
		}
		return wrapper;
	}

	@RequestMapping(value = "/api/remitt/tranx/pay", method = { RequestMethod.POST })
	public ResponseWrapper<RemittanceApplicationResponseModel> createApplication(
			@RequestBody RemittanceTransactionRequestModel transactionRequestModel) {
		ResponseWrapper<RemittanceApplicationResponseModel> wrapper = new ResponseWrapper<RemittanceApplicationResponseModel>();
		try {
			RemittanceApplicationResponseModel respTxMdl = jaxService.setDefaults().getRemitClient()
					.saveTransaction(transactionRequestModel).getResult();
			wrapper.setData(respTxMdl);
		} catch (RemittanceTransactionValidationException | LimitExeededException e) {
			wrapper.setMessage(ResponseStatus.ERROR, e);
		}
		return wrapper;
	}
}
