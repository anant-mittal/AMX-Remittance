package com.amx.jax.adapter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.device.CardReader;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.FileUtil;

import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.util.LockException;

@Controller
public class SWAdapterController {

	public static final String PUB_AMX_PREFIX = "/pub/amx";
	public static final String PARAM_URL = PUB_AMX_PREFIX + "/params";

	@Autowired
	ACardReaderService kwtCardReaderService;

	@Autowired
	AdapterServiceClient adapterServiceClient;

	@Autowired
	private ApplicationContext applicationContext;

	@ResponseBody
	@RequestMapping(value = "/pub/card/kwt/read", method = RequestMethod.GET)
	public CardReader readCard() throws InterruptedException {
		return kwtCardReaderService.read();
	}

	@ResponseBody
	@RequestMapping(value = "/pub/script/validation.js", method = RequestMethod.GET)
	public String makesession(@RequestParam String tranx) throws Exception {
		adapterServiceClient.pairTerminal(kwtCardReaderService.getAddress(),
				kwtCardReaderService.getDevicePairingCreds(), kwtCardReaderService.getSessionPairingCreds(), tranx);
		return "var _ba_ = true";
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
			kwtCardReaderService.setTerminalId(terminalId);
		}
		if (!ArgUtil.isEmpty(reset)) {
			kwtCardReaderService.resetTerminalPairing();
			adapterServiceClient.deActivateDevice(kwtCardReaderService.getDevicePairingCreds());
		}

		String body = "";
		if (ArgUtil.isEmpty(kwtCardReaderService.getTerminalId())) {
			body = FileUtil.read(applicationContext.getResource("classpath:templates/terminal.html").getURL());
		} else if (!ArgUtil.isEmpty(kwtCardReaderService.getDevicePairingCreds())) {
			body = FileUtil.read(applicationContext.getResource("classpath:templates/regid.html").getURL());
		} else {
			body = FileUtil.read(applicationContext.getResource("classpath:templates/index.html").getURL());
		}
		return body.replace("${REG_ID}", ArgUtil.isEmpty(kwtCardReaderService.getDevicePairingCreds())
				? Constants.BLANK
				: kwtCardReaderService.getDevicePairingCreds().getDeviceRegId())
				.replace("${HOST_NAME}", kwtCardReaderService.getAddress().getHostName())
				.replace("${USER_NAME}", kwtCardReaderService.getAddress().getUserName())
				.replace("${LOCAL_IP}", kwtCardReaderService.getAddress().getLocalIp())
				.replace("${DEVICE_STATUS}", String.format("%s", kwtCardReaderService.getDeviceStatus()))
				.replace("${CARD_STATUS}", String.format("%s", kwtCardReaderService.getCardStatusValue()))
				.replace("${DATA_STATUS}", String.format("%s", kwtCardReaderService.getDataStatusValue()))
				.replace("${LOG}", String.format("%s", SWAdapterGUI.CONTEXT.LOG));
	}

}
