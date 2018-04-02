package com.amx.jax.ui.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppConfig;
import com.amx.jax.postman.GeoLocationService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.GeoLocation;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.sample.CalcLibs;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.model.ServerStatus;
import com.amx.jax.ui.response.ResponseMeta;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.service.AppEnvironment;
import com.amx.jax.ui.service.HttpService;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.UserDevice;
import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Public APIs")
public class PubController {

	private static final Logger log = LoggerFactory.getLogger(PubController.class);

	@Autowired
	private AppEnvironment env;

	@Autowired
	private PostManService postManService;

	@Autowired
	private GeoLocationService geoLocationService;

	@Autowired
	private GuestSession guestSession;

	@Autowired
	private HttpService httpService;

	@Autowired
	private UserDevice userDevice;

	@Autowired
	private CalcLibs calcLibs;

	@Autowired
	private AppConfig appConfig;

	@ApiOperation(value = "List of All Possible Codes")
	@RequestMapping(value = "/pub/meta/status/list", method = { RequestMethod.POST })
	public ResponseWrapper<ResponseMeta> tranxhistory() {
		ResponseWrapper<ResponseMeta> wrapper = new ResponseWrapper<ResponseMeta>(new ResponseMeta());
		return wrapper;
	}

	@ApiOperation(value = "Current Location ofr client")
	@RequestMapping(value = "/pub/location", method = { RequestMethod.POST })
	public GeoLocation getLocation(@RequestParam String ip) throws PostManException {
		return geoLocationService.getLocation(ip);
	}

	@Timed
	@ApiOperation(value = "Ping")
	@RequestMapping(value = "/pub/ping", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<ServerStatus> status(@RequestParam(required = false) String tnt, HttpSession httpSession,
			HttpServletRequest request, Device device) throws Exception {
		ResponseWrapper<ServerStatus> wrapper = new ResponseWrapper<ServerStatus>(new ServerStatus());
		Integer hits = guestSession.hitCounter();

		userDevice.getType();

		wrapper.getData().debug = env.isDebug();
		wrapper.getData().id = httpSession.getId();
		wrapper.getData().hits = hits;
		wrapper.getData().domain = request.getRequestURL().toString();
		wrapper.getData().serverName = request.getServerName();
		wrapper.getData().requestUri = request.getRequestURI();
		wrapper.getData().remoteHost = request.getRemoteHost();
		wrapper.getData().remoteAddr = httpService.getIPAddress();
		wrapper.getData().remoteAddr = request.getRemoteAddr();

		wrapper.getData().localAddress = request.getLocalAddr();

		wrapper.getData().scheme = request.getScheme();

		wrapper.getData().device = userDevice.toMap();
		wrapper.getData().message = calcLibs.get().getRSName();

		log.info("==========appConfig======== {} == {} = {}", appConfig.isSwaggerEnabled(), appConfig.getAppName(),
				appConfig.isDebug());
		// jaxService.setDefaults().getMetaClient().getApplicationCountry().getResult();

		/*
		 * Email email = new Email(); email.addTo("lalit.tanwar07@gmail.com");
		 * email.setObject(wrapper);
		 * 
		 * email.setSubject("Test Email"); email.setTemplate(Templates.RESET_OTP);
		 * email.setHtml(true);
		 * 
		 * File file = new File(); file.setTemplate(Templates.RESET_OTP);
		 * file.setObject(wrapper); file.setType(File.Type.PDF); email.addFile(file);
		 * 
		 * postManService.sendEmail(email);
		 *
		 */

		/*
		 * Map<String, Integer> mapCustomers = hazelcastInstance.getMap("test");
		 * 
		 * hits = mapCustomers.get("hits"); if (hits == null) { hits = 0; }
		 * 
		 * wrapper.getData().put("h-name", hazelcastInstance.getName());
		 * wrapper.getData().put("hits-h", hits); mapCustomers.put("hits", ++hits);
		 */
		// if (!"".equalsIgnoreCase(httpSession.getId()))
		// throw new Exception();

		return wrapper;
	}

	@RequestMapping(value = "/pub/contact", method = { RequestMethod.POST })
	public ResponseWrapper<Email> contactUs(@RequestParam String name, @RequestParam String cemail,
			@RequestParam String cphone, @RequestParam String message, @RequestParam String verify) {
		ResponseWrapper<Email> wrapper = new ResponseWrapper<Email>();

		try {
			if (postManService.verifyCaptcha(verify, httpService.getIPAddress())) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", name);
				map.put("cphone", cphone);
				map.put("cemail", cemail);
				map.put("message", message);
				Email email = new Email();
				email.setFrom("exch-online@almullagroup.com");
				email.setReplyTo(cemail);
				email.addTo("alexander.jacob@almullagroup.com", "exch-online1@almullagroup.com",
						"exch-amx@almullagroup.com");
				email.getModel().put(UIConstants.RESP_DATA_KEY, map);
				email.setSubject("Inquiry");
				email.setTemplate(Templates.CONTACT_US);
				email.setHtml(true);
				postManService.sendEmail(email);
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