
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.CustomerProfileClient;
import com.amx.jax.client.JaxClientUtil;
import com.amx.jax.client.remittance.RemittanceClient;
import com.amx.jax.dict.AmxEnums.Products;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.Language;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.http.RequestType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.customer.CustomerRatingDTO;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.payg.PayGParams;
import com.amx.jax.payg.PayGService;
import com.amx.jax.rest.RestService;
import com.amx.jax.swagger.ApiStatusBuilder.ApiStatus;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.UIConstants.Features;
import com.amx.jax.ui.WebAppConfig;
import com.amx.jax.ui.config.UIServerError;
import com.amx.jax.ui.config.OWAStatus;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.model.ServerStatus;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.session.UserDeviceBean;
import com.amx.utils.ArgUtil;
import com.amx.utils.HttpUtils;
import com.amx.utils.JsonUtil;
import com.amx.utils.Urly;

import io.swagger.annotations.Api;

/**
 * The Class HomeController.
 */
@Controller
@Api(value = "Auth APIs")
public class HomeController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(HomeController.class);

	/** The web app config. */
	@Autowired
	private WebAppConfig webAppConfig;

	/** The user device. */
	@Autowired
	private UserDeviceBean userDevice;

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	@Autowired
	private CustomerProfileClient customerProfileClient;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/** The http service. */
	@Autowired
	CommonHttpRequest httpService;

	/** The check time. */
	private long checkTime = 0L;

	/** The version new. */
	private String versionNew = "_";

	/** The post man service. */
	@Autowired
	private RestService restService;

	@Autowired
	RemittanceClient remittanceClient;

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		long checkTimeNew = System.currentTimeMillis() / (1000 * 60 * 5);
		if (checkTimeNew != checkTime) {
			try {
				Map<String, Object> map = restService
						.ajax(webAppConfig.getCleanCDNUrl() + "/dist/build.json?_=" + checkTimeNew).get().asMap();
				if (map.containsKey("version")) {
					versionNew = ArgUtil.parseAsString(map.get("version"));
				}
				checkTime = checkTimeNew;
			} catch (Exception e) {
				LOGGER.error("getVersion Exception", e);
			}
		}
		return versionNew;
	}

	/**
	 * Login ping.
	 *
	 * @param request the request
	 * @return the string
	 */
	@ApiJaxStatus({ JaxError.ACCOUNT_LENGTH, JaxError.ACCOUNT_TYPE_UPDATE })
	@RequestMapping(value = "/pub/meta/**", method = { RequestMethod.GET })
	@ResponseBody
	public String loginPing(HttpServletRequest request) {
		ResponseWrapper<ServerStatus> wrapper = new ResponseWrapper<ServerStatus>(new ServerStatus());
		Integer hits = sessionService.getGuestSession().hitCounter();
		userDevice.resolve();
		wrapper.getData().setHits(hits);
		wrapper.getData().setDomain(request.getRequestURL().toString());
		wrapper.getData().setRequestUri(request.getRequestURI());
		wrapper.getData().setRemoteAddr(httpService.getIPAddress());
		wrapper.getData().setDevice(userDevice.getUserDevice().toSanitized());
		return JsonUtil.toJson(wrapper);
	}

	/**
	 * Login J page.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/login/**", method = { RequestMethod.GET })
	public String loginJPage(Model model) {
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webAppConfig.getAppTitle());
		model.addAttribute("cdnUrl", webAppConfig.getCleanCDNUrl());
		model.addAttribute(UIConstants.CDN_VERSION, getVersion());
		model.addAttribute(AppConstants.DEVICE_ID_KEY, userDevice.getUserDevice().getFingerprint());
		model.addAttribute("fcmSenderId", webAppConfig.getFcmSenderId());
		return "app";
	}

	/**
	 * Login P json.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/login/**", method = { RequestMethod.GET, RequestMethod.POST }, headers = {
			"Accept=application/json", "Accept=application/v0+json" })
	@ResponseBody
	public String loginPJson() {
		LOGGER.debug("This is debug Statment");
		LOGGER.info("This is debug Statment");
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>(null);
		wrapper.setMessage(OWAStatusStatusCodes.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
		return JsonUtil.toJson(wrapper);
	}

	/**
	 * Default page.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = { "/register/**", "/app/**", "/home/**", "/refer/**", "/" },
			method = { RequestMethod.GET, RequestMethod.POST })
	public String defaultPage(Model model) {
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webAppConfig.getAppTitle());
		model.addAttribute("cdnUrl", webAppConfig.getCleanCDNUrl());
		model.addAttribute(UIConstants.CDN_VERSION, getVersion());
		model.addAttribute(AppConstants.DEVICE_ID_KEY, userDevice.getUserDevice().getFingerprint());
		model.addAttribute("fcmSenderId", webAppConfig.getFcmSenderId());
		return "app";
	}

	@RequestMapping(value = { "/pub/app/**" }, method = { RequestMethod.GET })
	public String defaultPubPage(Model model) {
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webAppConfig.getAppTitle());
		model.addAttribute("cdnUrl", webAppConfig.getCleanCDNUrl());
		model.addAttribute(UIConstants.CDN_VERSION, getVersion());
		model.addAttribute(AppConstants.DEVICE_ID_KEY, userDevice.getUserDevice().getFingerprint());
		model.addAttribute("fcmSenderId", webAppConfig.getFcmSenderId());
		return "pay";
	}

	/**
	 * Terms page.
	 *
	 * @param model the model
	 * @param lang  the lang
	 * @return the string
	 */
	@RequestMapping(value = { "/app/terms", "/pub/terms" }, method = { RequestMethod.GET })
	public String termsPage(Model model, @RequestParam Language lang,
			@RequestParam(required = false) Products product) {
		model.addAttribute("lang", httpService.getLanguage());
		sessionService.getGuestSession().setLanguage(lang);
		if (ArgUtil.isEmpty(product) || Products.REMIT.equals(product)) {
			model.addAttribute("terms", jaxService.setDefaults().getMetaClient().getTermsAndCondition().getResults());
		} else if (Products.FXORDER.equals(product)) {
			model.addAttribute("terms",
					jaxService.setDefaults().getMetaClient().getTermsAndConditionAsPerCountryForFxOrder().getResults());
		}
		return "terms";
	}

	@Autowired
	private SpringTemplateEngine templateEngine;

	@ApiRequest(type = RequestType.NO_TRACK_PING)
	@RequestMapping(value = { "/apple-app-site-association", "/.well-known/apple-app-site-association" }, method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String applejson(Model model, HttpServletResponse response, Locale locale) {
		model.addAttribute("appid", webAppConfig.getIosAppId());
		Context context = new Context(locale);
		context.setVariables(model.asMap());
		return templateEngine.process("json/apple-app-site-association", context);
	}

	@ApiJaxStatus({ JaxError.CUSTOMER_NOT_FOUND, JaxError.INVALID_OTP, JaxError.ENTITY_INVALID,
			JaxError.ENTITY_EXPIRED })
	@ApiStatus({ ApiStatusCodes.PARAM_MISSING })
	@RequestMapping(value = { "/pub/verify/{contactType}/{verId}/{verCode}" },
			method = { RequestMethod.GET, RequestMethod.POST })
	public String verification(Model model,
			@PathVariable ContactType contactType, @PathVariable BigDecimal verId, @PathVariable String verCode,
			@RequestParam(required = false) String identity, @RequestParam(required = false) String resend,
			@RequestParam(required = false) String submit,
			@RequestParam(required = false) String customerId) {
		String errorCode = null;
		String errorMessage = null;
		contactType = contactType.contactType();
		try {
			if (!ArgUtil.isEmpty(resend)) {
				customerProfileClient.resendLink(identity, verId, verCode);
			} else if (identity == null) {
				customerProfileClient.validateVerificationLink(verId).getResult();
			} else {
				customerProfileClient.verifyLinkByCode(identity, verId, verCode);
			}
		} catch (AmxApiException e) {
			errorCode = e.getErrorKey();
			errorMessage = e.getMessage();
		}
		model.addAttribute("resend", resend);
		model.addAttribute("submit", submit);
		model.addAttribute("errorCode", errorCode);
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("contactType", contactType);
		model.addAttribute("verId", verId);
		model.addAttribute("verCode", verCode);
		model.addAttribute("idType", webAppConfig.getCompanyIdtype());
		model.addAttribute("companyTnt", webAppConfig.getCompanyTnt());
		return "verify";
	}

	@ApiJaxStatus({ JaxError.CUSTOMER_NOT_FOUND, JaxError.INVALID_OTP, JaxError.ENTITY_INVALID,
			JaxError.ENTITY_EXPIRED })
	@ApiStatus({ ApiStatusCodes.PARAM_MISSING })
	@RequestMapping(value = { "/pub/verify/{contactType}/resend" },
			method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> verification(
			@PathVariable ContactType contactType,
			@RequestParam(required = true) String identity) {
		String errorCode = null;
		String errorMessage = null;
		contactType = contactType.contactType();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			customerProfileClient.createVerificationLink(null, contactType, identity);
		} catch (AmxApiException e) {
			map.put("errorCode", e.getErrorKey());
			map.put("errorMessage", e.getMessage());
		}
		return map;
	}

	@ApiJaxStatus({ JaxError.CUSTOMER_NOT_FOUND, JaxError.INVALID_OTP, JaxError.ENTITY_INVALID,
			JaxError.ENTITY_EXPIRED })
	@ApiStatus({ ApiStatusCodes.PARAM_MISSING })
	@RequestMapping(value = { "/pub/rating/{prodType}/{trnxId}/{veryCode}/**" },
			method = { RequestMethod.GET }, produces = {
					CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	@ResponseBody
	public Map<String, Object> rating(
			@PathVariable Products prodType, @PathVariable BigDecimal trnxId, @PathVariable String veryCode) {

		boolean valid = false;
		AmxApiResponse<CustomerRatingDTO, ?> rating = null;

		if (prodType.equals(Products.REMIT)) {
			valid = JaxClientUtil.getTransactionVeryCode(trnxId).equals(veryCode);
			rating = jaxService.getRemitClient().inquireCustomerRating(trnxId, prodType.toString());
		}
		if (prodType.equals(Products.FXORDER)) {
			valid = JaxClientUtil.getTransactionVeryCode(trnxId).equals(veryCode);
			rating = jaxService.getFxOrderBranchClient().inquirefxOrderCustomerRating(trnxId, prodType.toString());
		}

		String errorCode = null;
		String errorMessage = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rating", rating);
		map.put("trnxId", trnxId);
		map.put("errorCode", errorCode);
		map.put("errorMessage", errorMessage);
		map.put("prodType", prodType);
		map.put("verCode", veryCode);
		map.put("valid", valid);
		return map;
	}

	@ApiJaxStatus({ JaxError.CUSTOMER_NOT_FOUND, JaxError.INVALID_OTP, JaxError.ENTITY_INVALID,
			JaxError.ENTITY_EXPIRED })
	@ApiStatus({ ApiStatusCodes.PARAM_MISSING })
	@RequestMapping(value = { "/pub/rating/{prodType}/{trnxId}/{veryCode}" },
			method = { RequestMethod.GET })
	public String rating(Model model,
			@PathVariable Products prodType, @PathVariable BigDecimal trnxId, @PathVariable String veryCode) {
		Map<String, Object> map = rating(prodType, trnxId, veryCode);
		model.addAttribute("ratingData", (map));
		model.addAttribute("companyTnt", AppContextUtil.getTenant());
		return "rating";
	}


	@ApiJaxStatus({ JaxError.CUSTOMER_NOT_FOUND, JaxError.INVALID_OTP, JaxError.ENTITY_INVALID,
			JaxError.ENTITY_EXPIRED })
	@ApiStatus({ ApiStatusCodes.PARAM_MISSING })
	@RequestMapping(value = { "/pub/rating/{prodType}/submit" }, method = { RequestMethod.POST })
	@ResponseBody
	public ResponseWrapper<CustomerRatingDTO> appStatus(@RequestBody CustomerRatingDTO customerRatingDTO,
			@RequestParam String veryCode, @PathVariable Products prodType) {

		if (prodType.equals(Products.REMIT)) {
			if (!JaxClientUtil.getTransactionVeryCode(customerRatingDTO.getRemittanceTransactionId())
					.equals(veryCode)) {
				throw new UIServerError(OWAStatusStatusCodes.INVALID_LINK);
			}
		}
		if (prodType.equals(Products.FXORDER)) {
			if (!JaxClientUtil.getTransactionVeryCode(customerRatingDTO.getRemittanceTransactionId())
					.equals(veryCode)) {
				throw new UIServerError(OWAStatusStatusCodes.INVALID_LINK);
			}
		}

		return ResponseWrapper
				.build(jaxService.setDefaults().getRemitClient().saveCustomerRating(customerRatingDTO, prodType));

	}

	@Autowired
	private PayGService payGService;

	public static final String PAYMENT_PATH = "/pub/pay/{prodType}/{linkId}";

	@ApiJaxStatus({ JaxError.CUSTOMER_NOT_FOUND, JaxError.INVALID_OTP, JaxError.ENTITY_INVALID,
			JaxError.ENTITY_EXPIRED })
	@ApiStatus({ ApiStatusCodes.PARAM_MISSING })
	@RequestMapping(value = { PAYMENT_PATH },
			method = { RequestMethod.GET }, produces = {
					CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	@ResponseBody
	public Map<String, Object> directPaymentJson(Model model,
			@PathVariable Products prodType, @PathVariable BigDecimal linkId,
			@RequestParam(value = "v") String veryCode,
			HttpServletRequest request) throws MalformedURLException, URISyntaxException {

		PaymentLinkRespDTO link = remittanceClient.validatePayLink(linkId, veryCode).getResult();

		PayGParams payment = new PayGParams();
		payment.setPayId(ArgUtil.parseAsString(link.getId()));

		payment.setDocFyObject(link.getDocumentFinancialYear());
		payment.setDocNo(link.getDocumentIdForPayment());
		payment.setTrackIdObject(link.getMerchantTrackId());
		payment.setAmountObject(link.getNetAmount());
		payment.setServiceCode(link.getPgCode());
		payment.setProduct(prodType);

		LOGGER.info("Payment Link Response DTO Values : " + link.toString());

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("payment_link", payGService.getPaymentUrl(payment,
				Urly.parse(HttpUtils.getServerName(request)).path("/pub/app/pay/{prodType}/{linkId}")
						.pathParam("prodType", prodType)
						.pathParam("linkId", linkId)
						.queryParam("v", veryCode).queryParam("page", "response").getURL()));

		map.put("statusKey", OWAStatus.OWAStatusStatusCodes.SUCCESS);
		map.put("messageKey", OWAStatus.OWAStatusStatusCodes.SUCCESS);
		map.put("cart", link);
		map.put("linkId", linkId);
		map.put("veryCode", veryCode);
		map.put("tnt", AppContextUtil.getTenant());
		return map;
	}

	@RequestMapping(value = { "/pub/recaptcha/{feature}" },
			method = { RequestMethod.GET })
	public String recaptach(Model model, @PathVariable Features feature) {
		model.addAttribute("googelReCaptachSiteKey", webAppConfig.getGoogelReCaptachSiteKey());
		model.addAttribute("companyTnt", AppContextUtil.getTenant());
		return "recaptcha";
	}
}
