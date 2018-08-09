package com.amx.jax.ui.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.amx.amxlib.model.request.IRemitTransReqPurpose;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.dict.Language;
import com.amx.jax.payment.PayGService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.UserBean;
import com.amx.jax.ui.model.XRateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.ResponseWrapperM;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.service.TenantService;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class RemittController.
 */
@RestController
@Api(value = "Remote APIs")
public class RemittController {

	/** The response. */
	@Autowired
	private HttpServletResponse response;

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/** The tenant context. */
	@Autowired
	private TenantService tenantContext;

	/** The user bean. */
	@Autowired
	private UserBean userBean;

	/** The post man service. */
	@Autowired
	private PostManService postManService;

	/** The pay G service. */
	@Autowired
	private PayGService payGService;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/**
	 * Tranxhistory.
	 *
	 * @return the response wrapper
	 */
	@ApiOperation(value = "Returns transaction history")
	@RequestMapping(value = "/api/user/tranx/history", method = { RequestMethod.POST })
	public ResponseWrapper<List<TransactionHistroyDTO>> tranxhistory() {
		return new ResponseWrapper<List<TransactionHistroyDTO>>(
				jaxService.setDefaults().getRemitClient().getTransactionHistroy("2017", null, null, null).getResults());
	}

	/**
	 * Send history.
	 *
	 * @param fromDate
	 *            the from date
	 * @param toDate
	 *            the to date
	 * @param docfyr
	 *            the docfyr
	 * @return the response wrapper
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PostManException
	 *             the post man exception
	 */
	@RequestMapping(value = "/api/user/tranx/print_history", method = { RequestMethod.GET })
	public ResponseWrapper<List<TransactionHistroyDTO>> sendHistory(@RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam(required = false) String docfyr)
			throws IOException, PostManException {

		ResponseWrapper<List<TransactionHistroyDTO>> wrapper = new ResponseWrapper<List<TransactionHistroyDTO>>();
		List<TransactionHistroyDTO> data = jaxService.setDefaults().getRemitClient()
				.getTransactionHistroy(docfyr, null, fromDate, toDate).getResults();
		File file = new File();
		file.setLang(Language.EN);
		file.setTemplate(Templates.REMIT_STATMENT_EMAIL_FILE);
		file.setType(File.Type.PDF);
		file.getModel().put(UIConstants.RESP_DATA_KEY, data);
		Email email = new Email();
		email.setSubject(String.format("Transaction Statement %s - %s", fromDate, toDate));
		email.addTo(sessionService.getUserSession().getCustomerModel().getEmail());
		email.setTemplate(Templates.REMIT_STATMENT_EMAIL);
		email.getModel().put(UIConstants.RESP_DATA_KEY,
				sessionService.getUserSession().getCustomerModel().getPersoninfo());
		email.addFile(file);
		email.setHtml(true);
		postManService.sendEmailAsync(email);
		return wrapper;
	}

	/**
	 * Prints the history.
	 *
	 * @param wrapper
	 *            the wrapper
	 * @return the response wrapper
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PostManException
	 *             the post man exception
	 */
	@ApiOperation(value = "Returns transaction history")
	@RequestMapping(value = "/api/user/tranx/print_history", method = { RequestMethod.POST })
	public ResponseWrapper<List<Map<String, Object>>> printHistory(
			@RequestBody ResponseWrapper<List<Map<String, Object>>> wrapper) throws IOException, PostManException {
		File file = postManService.processTemplate(new File(Templates.REMIT_STATMENT, wrapper, File.Type.PDF));
		file.create(response, true);
		return wrapper;
	}

	/**
	 * Tranxreport.
	 *
	 * @param tranxDTO
	 *            the tranx DTO
	 * @param duplicate
	 *            the duplicate
	 * @param skipd
	 *            the skipd
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PostManException
	 *             the post man exception
	 */
	@ApiOperation(value = "Returns transaction reciept")
	@RequestMapping(value = "/api/user/tranx/report", method = { RequestMethod.POST })
	public String tranxreport(@RequestBody TransactionHistroyDTO tranxDTO,
			@RequestParam(required = false) Boolean duplicate, @RequestParam(required = false) Boolean skipd)
			throws IOException, PostManException {
		RemittanceReceiptSubreport rspt = jaxService.setDefaults().getRemitClient().report(tranxDTO).getResult();
		ResponseWrapper<RemittanceReceiptSubreport> wrapper = new ResponseWrapper<RemittanceReceiptSubreport>(rspt);
		duplicate = (duplicate == null || duplicate.booleanValue() == false) ? false : true;

		File file = null;
		if (skipd == null || skipd.booleanValue() == false) {
			file = postManService.processTemplate(
					new File(duplicate ? Templates.REMIT_RECEIPT_COPY_JASPER : Templates.REMIT_RECEIPT_JASPER, wrapper,
							File.Type.PDF));
			file.create(response, true);
		}
		return JsonUtil.toJson(file);
	}

	/**
	 * Tranxreport ext.
	 *
	 * @param collectionDocumentNo
	 *            the collection document no
	 * @param collectionDocumentFinYear
	 *            the collection document fin year
	 * @param collectionDocumentCode
	 *            the collection document code
	 * @param customerReference
	 *            the customer reference
	 * @param ext
	 *            the ext
	 * @param duplicate
	 *            the duplicate
	 * @return the string
	 * @throws PostManException
	 *             the post man exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@ApiOperation(value = "Returns transaction reciept:")
	@RequestMapping(value = "/api/user/tranx/report.{ext}", method = { RequestMethod.GET })
	public @ResponseBody String tranxreportExt(@RequestParam(required = false) BigDecimal collectionDocumentNo,
			@RequestParam(required = false) BigDecimal collectionDocumentFinYear,
			@RequestParam(required = false) BigDecimal collectionDocumentCode,
			@RequestParam(required = false) BigDecimal customerReference, @PathVariable("ext") String ext,
			@RequestParam(required = false) Boolean duplicate) throws PostManException, IOException {

		duplicate = (duplicate == null || duplicate.booleanValue() == false) ? false : true;

		TransactionHistroyDTO tranxDTO = new TransactionHistroyDTO();
		tranxDTO.setCollectionDocumentNo(collectionDocumentNo);
		tranxDTO.setCollectionDocumentFinYear(collectionDocumentFinYear);
		tranxDTO.setCollectionDocumentCode(collectionDocumentCode);
		tranxDTO.setCustomerReference(customerReference);

		RemittanceReceiptSubreport rspt = jaxService.setDefaults().getRemitClient().report(tranxDTO).getResult();
		ResponseWrapper<RemittanceReceiptSubreport> wrapper = new ResponseWrapper<RemittanceReceiptSubreport>(rspt);
		if ("pdf".equals(ext)) {
			File file = postManService.processTemplate(
					new File(duplicate ? Templates.REMIT_RECEIPT_COPY_JASPER : Templates.REMIT_RECEIPT_JASPER, wrapper,
							File.Type.PDF));
			file.create(response, false);
			return null;
		} else if ("html".equals(ext)) {
			File file = postManService.processTemplate(new File(
					duplicate ? Templates.REMIT_RECEIPT_COPY_JASPER : Templates.REMIT_RECEIPT_JASPER, wrapper, null));
			return file.getContent();
		} else {
			return JsonUtil.toJson(wrapper);
		}
	}

	/**
	 * Xrate.
	 *
	 * @param forCur
	 *            the for cur
	 * @param domAmount
	 *            the dom amount
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/remitt/xrate", method = { RequestMethod.POST })
	public ResponseWrapper<XRateData> xrate(@RequestParam(required = false) BigDecimal forCur,
			@RequestParam(required = false) BigDecimal domAmount) {
		ResponseWrapper<XRateData> wrapper = new ResponseWrapper<XRateData>(new XRateData());

		CurrencyMasterDTO domCur = tenantContext.getDomCurrency();
		CurrencyMasterDTO forCurcy = userBean.getDefaultForCurrency(forCur);

		wrapper.getData().setDomCur(domCur);

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
				wrapper.setMessage(WebResponseStatus.ERROR, e);
			}
		}
		return wrapper;
	}

	/**
	 * Bnfcry check.
	 *
	 * @param beneId
	 *            the bene id
	 * @param transactionId
	 *            the transaction id
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/remitt/default", method = { RequestMethod.POST })
	public ResponseWrapper<RemittancePageDto> bnfcryCheck(@RequestParam(required = false) BigDecimal beneId,
			@RequestParam(required = false) BigDecimal transactionId) {
		ResponseWrapper<RemittancePageDto> wrapper = new ResponseWrapper<RemittancePageDto>();
		RemittancePageDto remittancePageDto = jaxService.setDefaults().getBeneClient()
				.defaultBeneficiary(beneId, transactionId).getResult();

		BigDecimal forCurId = remittancePageDto.getBeneficiaryDto().getCurrencyId();

		for (CurrencyMasterDTO currency : tenantContext.getOnlineCurrencies()) {
			if (currency.getCurrencyId().equals(forCurId)) {
				remittancePageDto.setForCur(currency);
				break;
			}
		}
		remittancePageDto.setDomCur(tenantContext.getDomCurrency());

		wrapper.setData(remittancePageDto);
		return wrapper;
	}

	/**
	 * Bnfcry check.
	 *
	 * @param beneId
	 *            the bene id
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/remitt/purpose/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<PurposeOfTransactionModel>> bnfcryCheck(
			@RequestBody IRemitTransReqPurpose remitTransReqPurpose) {
		ResponseWrapper<List<PurposeOfTransactionModel>> wrapper = new ResponseWrapper<List<PurposeOfTransactionModel>>();
		wrapper.setData(
				jaxService.setDefaults().getRemitClient().getPurposeOfTransactions(remitTransReqPurpose).getResults());
		return wrapper;
	}

	/**
	 * Bnfcry check.
	 *
	 * @param request
	 *            the request
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/remitt/tranxrate", method = { RequestMethod.POST })
	public ResponseWrapper<RemittanceTransactionResponsetModel> bnfcryCheck(
			@RequestBody RemittanceTransactionRequestModel request) {
		ResponseWrapper<RemittanceTransactionResponsetModel> wrapper = new ResponseWrapper<RemittanceTransactionResponsetModel>();
		try {
			RemittanceTransactionResponsetModel respTxMdl = jaxService.setDefaults().getRemitClient()
					.validateTransaction(request).getResult();
			wrapper.setData(respTxMdl);
		} catch (RemittanceTransactionValidationException | LimitExeededException e) {
			wrapper.setMessage(WebResponseStatus.ERROR, e);
		}
		return wrapper;
	}

	/**
	 * Creates the application.
	 *
	 * @param transactionRequestModel
	 *            the transaction request model
	 * @param request
	 *            the request
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/remitt/tranx/pay", method = { RequestMethod.POST })
	public ResponseWrapperM<RemittanceApplicationResponseModel, AuthResponseOTPprefix> createApplication(
			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestParam(required = false) String mOtp,
			@RequestBody RemittanceTransactionRequestModel transactionRequestModel, HttpServletRequest request) {
		ResponseWrapperM<RemittanceApplicationResponseModel, AuthResponseOTPprefix> wrapper = new ResponseWrapperM<RemittanceApplicationResponseModel, AuthResponseOTPprefix>();

		// Noncompliant - exception is lost
		try {
			mOtp = ArgUtil.ifNotEmpty(mOtp, mOtpHeader);
			transactionRequestModel.setmOtp(mOtp);

			RemittanceApplicationResponseModel respTxMdl = jaxService.setDefaults().getRemitClient()
					.saveTransaction(transactionRequestModel).getResult();
			wrapper.setData(respTxMdl);
			if (respTxMdl.getCivilIdOtpModel() != null && respTxMdl.getCivilIdOtpModel().getmOtpPrefix() != null) {
				wrapper.setMeta(new AuthData());
				wrapper.getMeta().setmOtpPrefix(respTxMdl.getCivilIdOtpModel().getmOtpPrefix());
				wrapper.setStatus(WebResponseStatus.MOTP_REQUIRED);
			} else {
				wrapper.setRedirectUrl(payGService.getPaymentUrl(respTxMdl,
						"https://" + request.getServerName() + "/app/landing/remittance"));
			}

		} catch (RemittanceTransactionValidationException | LimitExeededException | MalformedURLException
				| URISyntaxException e) {
			wrapper.setMessage(WebResponseStatus.ERROR, e);
		}
		return wrapper;
	}

	/**
	 * App status.
	 *
	 * @param request
	 *            the request
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/remitt/tranx/status", method = { RequestMethod.POST })
	public ResponseWrapper<RemittanceTransactionStatusResponseModel> appStatus(
			@RequestBody RemittanceTransactionStatusRequestModel request) {
		ResponseWrapper<RemittanceTransactionStatusResponseModel> wrapper = new ResponseWrapper<RemittanceTransactionStatusResponseModel>();
		wrapper.setData(jaxService.setDefaults().getRemitClient().fetchTransactionDetails(request).getResult());
		return wrapper;
	}
}
