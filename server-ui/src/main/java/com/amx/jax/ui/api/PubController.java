package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.jax.AppConfig;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.postman.GeoLocationService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.GoogleService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.GeoLocation;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.sample.CalcLibs;
import com.amx.jax.service.HttpService;
import com.amx.jax.tunnel.TunnelClient;
import com.amx.jax.tunnel.TunnelService;
import com.amx.jax.ui.model.ServerStatus;
import com.amx.jax.ui.response.ResponseMeta;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.service.AppEnvironment;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.UserDeviceBean;
import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class PubController.
 */
@RestController
@Api(value = "Public APIs")
public class PubController {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(PubController.class);

	/** The env. */
	@Autowired
	private AppEnvironment env;

	/** The post man service. */
	@Autowired
	private PostManService postManService;

	@Autowired
	private GoogleService googleService;

	/** The geo location service. */
	@Autowired
	private GeoLocationService geoLocationService;

	/** The guest session. */
	@Autowired
	private GuestSession guestSession;

	/** The http service. */
	@Autowired
	private HttpService httpService;

	/** The user device. */
	@Autowired
	private UserDeviceBean userDevice;

	/** The calc libs. */
	@Autowired
	private CalcLibs calcLibs;

	/** The app config. */
	@Autowired
	private AppConfig appConfig;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/**
	 * Tranxhistory.
	 *
	 * @return the response wrapper
	 */
	@ApiOperation(value = "List of All Possible Codes")
	@RequestMapping(value = "/pub/meta/status/list", method = { RequestMethod.POST })
	public ResponseWrapper<ResponseMeta> tranxhistory() {
		return new ResponseWrapper<>(new ResponseMeta());
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 * @throws PostManException
	 *             the post man exception
	 */
	@ApiOperation(value = "Current Location ofr client")
	@RequestMapping(value = "/pub/location", method = { RequestMethod.GET })
	public GeoLocation getLocation() throws PostManException {
		return geoLocationService.getLocation(httpService.getIPAddress());
	}

	@Autowired
	TunnelService tunnelService;

	/**
	 * Status.
	 *
	 * @param tnt
	 *            the tnt
	 * @param httpSession
	 *            the http session
	 * @param request
	 *            the request
	 * @param device
	 *            the device
	 * @return the response wrapper
	 * @throws Exception
	 *             the exception
	 */
	@Timed
	@ApiOperation(value = "Ping")
	@RequestMapping(value = "/pub/ping", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<ServerStatus> status(@RequestParam(required = false) String tnt, HttpSession httpSession,
			HttpServletRequest request, Device device) {
		ResponseWrapper<ServerStatus> wrapper = new ResponseWrapper<>(new ServerStatus());
		Integer hits = guestSession.hitCounter();

		userDevice.getType();
		wrapper.getData().setDebug(env.isDebug());
		wrapper.getData().setId(httpSession.getId());
		wrapper.getData().setHits(hits);
		wrapper.getData().setDomain(request.getRequestURL().toString());
		wrapper.getData().setServerName(request.getServerName());
		wrapper.getData().setRequestUri(request.getRequestURI());
		wrapper.getData().setRemoteHost(request.getRemoteHost());
		wrapper.getData().setRemoteAddr(httpService.getIPAddress());
		wrapper.getData().setRemoteAddr(request.getRemoteAddr());
		wrapper.getData().setLocalAddress(request.getLocalAddr());
		wrapper.getData().setScheme(request.getScheme());
		wrapper.getData().setDevice(userDevice.toMap());
		wrapper.getData().message = calcLibs.get().getRSName();

		tunnelService.send(TunnelClient.TEST_TOPIC, new SessionEvent());
		log.info("==========appConfig======== {} == {} = {} {}", appConfig.isSwaggerEnabled(), appConfig.getAppName());

		return wrapper;
	}

	@Autowired
	private JaxService jaxService;

	@RequestMapping(value = "/pub/rates", method = { RequestMethod.GET })
	public ResponseWrapper<List<MinMaxExRateDTO>> rates() {
		return new ResponseWrapper<List<MinMaxExRateDTO>>(
				jaxService.setDefaults(new BigDecimal(0)).getxRateClient().getMinMaxExchangeRate().getResults());
	}

	/**
	 * Report us.
	 *
	 * @param email
	 *            the email
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/report", method = { RequestMethod.POST })
	public ResponseWrapper<Email> reportUs(@RequestBody SupportEmail email) {
		ResponseWrapper<Email> wrapper = new ResponseWrapper<>();
		try {

			if (sessionService.getUserSession().getCustomerModel() != null) {
				email.setVisitorEmail(sessionService.getUserSession().getCustomerModel().getEmail());
				email.setVisitorName(sessionService.getUserSession().getCustomerModel().getPersoninfo().getFirstName()
						+ " " + sessionService.getUserSession().getCustomerModel().getPersoninfo().getLastName());
				email.setVisitorPhone(sessionService.getUserSession().getCustomerModel().getPersoninfo().getMobile());
				email.setIdentity(sessionService.getUserSession().getCustomerModel().getPersoninfo().getIdentityInt());
			}
			postManService.sendEmailToSupprt(email);
			wrapper.setData(email);
		} catch (Exception e) {
			wrapper.setStatusKey(WebResponseStatus.ERROR);
			log.error("/pub/report", e);
		}
		return wrapper;
	}

	/**
	 * Contact us.
	 *
	 * @param name
	 *            the name
	 * @param cemail
	 *            the cemail
	 * @param cphone
	 *            the cphone
	 * @param message
	 *            the message
	 * @param verify
	 *            the verify
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/contact", method = { RequestMethod.POST })
	public ResponseWrapper<Email> contactUs(@RequestParam String name, @RequestParam String cemail,
			@RequestParam String cphone, @RequestParam String message, @RequestParam String verify) {
		ResponseWrapper<Email> wrapper = new ResponseWrapper<>();
		try {
			if (googleService.verifyCaptcha(verify, httpService.getIPAddress())) {
				SupportEmail email = new SupportEmail();
				email.setCaptchaCode(verify);
				email.setVisitorName(name);
				email.setVisitorPhone(cphone);
				email.setVisitorEmail(cemail);
				email.setVisitorMessage(message);
				postManService.sendEmailToSupprt(email);
				wrapper.setData(email);
			} else {
				wrapper.setStatusKey(WebResponseStatus.ERROR);
			}
		} catch (Exception e) {
			wrapper.setStatusKey(WebResponseStatus.ERROR);
			log.error("/pub/contact", e);
		}
		return wrapper;
	}

}
