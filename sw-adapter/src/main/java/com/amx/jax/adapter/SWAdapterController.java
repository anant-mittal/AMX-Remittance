package com.amx.jax.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.device.CardReader;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.FileUtil;

@Controller
public class SWAdapterController {

	public static final String PUB_AMX_PREFIX = "/pub/amx";
	public static final String PARAM_URL = PUB_AMX_PREFIX + "/params";

	@Autowired
	ACardReaderService aCardReaderService;

	@Autowired
	DeviceConnectorClient adapterServiceClient;

	@Autowired
	private ApplicationContext applicationContext;

	@ResponseBody
	@RequestMapping(value = "/pub/card/read", method = RequestMethod.GET)
	public CardReader readCard() throws InterruptedException {
		return aCardReaderService.read();
	}

	@ResponseBody
	@RequestMapping(value = "/pub/card/sync", method = RequestMethod.GET)
	public CardReader syncCard() throws InterruptedException {
		return aCardReaderService.sync();
	}

	@ResponseBody
	@RequestMapping(value = "/pub/script/validation.js", method = RequestMethod.GET)
	public String makesession(@RequestParam String tranx) throws Exception {
		String tid = "";
		String rid = "";
		String excep = "";
		if (!ArgUtil.isEmpty(aCardReaderService.getDevicePairingCreds())
				&& !ArgUtil.isEmpty(aCardReaderService.getSessionPairingCreds())) {
			try {
				AmxApiResponse<Object, Object> x = adapterServiceClient.pairTerminal(aCardReaderService.getAddress(),
						aCardReaderService.getDevicePairingCreds(), aCardReaderService.getSessionPairingCreds(),
						tranx);
				tid = ArgUtil.parseAsString(x.getResult());
				rid = ArgUtil.parseAsString(x.getMeta());
				aCardReaderService.sync();
			} catch (Exception e) {

			}
		}
		return "var _tid_ = '" + tid + "', _rid_ = '" + rid + "', _excep_='" + excep + "';";
	}

	@ResponseBody
	@RequestMapping(value = "/pub/script/index.js", method = RequestMethod.GET)
	public String indexJs() throws Exception {
		return FileUtil.read(applicationContext.getResource("classpath:templates/index.js").getURL());
	}

	@ResponseBody
	@RequestMapping(value = { "/**", "/*", "/" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String home(@RequestParam(required = false) String terminalId, Model model,
			@RequestParam(required = false) String reset)
			throws Exception {
		if (!ArgUtil.isEmpty(terminalId)) {
			aCardReaderService.setTerminalId(terminalId);
		}
		if (!ArgUtil.isEmpty(reset)) {
			aCardReaderService.resetTerminalPairing();
		}

		String body = "";
		if (!ArgUtil.isEmpty(aCardReaderService.getDevicePairingCreds())) {
			body = FileUtil.read(applicationContext.getResource("classpath:templates/regid.html").getURL());
		} else if (ArgUtil.isEmpty(aCardReaderService.getTerminalId())) {
			body = FileUtil.read(applicationContext.getResource("classpath:templates/terminal.html").getURL());
		} else {
			body = FileUtil.read(applicationContext.getResource("classpath:templates/index.html").getURL());
		}
		return body.replace("${REG_ID}", ArgUtil.isEmpty(aCardReaderService.getDevicePairingCreds())
				? Constants.BLANK
				: aCardReaderService.getDevicePairingCreds().getDeviceRegId())
				.replace("${WIN_TITLE}", SWAdapterGUI.WIN_TITLE)
				.replace("${HOST_NAME}", aCardReaderService.getAddress().getHostName())
				.replace("${USER_NAME}", aCardReaderService.getAddress().getUserName())
				.replace("${LOCAL_IP}", aCardReaderService.getAddress().getLocalIp())
				.replace("${TERMINAL_IP}", ArgUtil.parseAsString(aCardReaderService.getTerminalId(), Constants.BLANK))
				.replace("${DEVICE_STATUS}", String.format("%s", aCardReaderService.getDeviceStatus()))
				.replace("${CARD_STATUS}", String.format("%s", aCardReaderService.getCardStatusValue()))
				.replace("${DATA_STATUS}", String.format("%s", aCardReaderService.getDataStatusValue()))
				.replace("${SERVER_URL}", String.format("%s", aCardReaderService.getServerUrl()))
				.replace("${ADAPTER_VERSION}", String.format("%s", aCardReaderService.getVersion()))
				.replace("${LOG}", String.format("%s", SWAdapterGUI.LOG));
	}

}
