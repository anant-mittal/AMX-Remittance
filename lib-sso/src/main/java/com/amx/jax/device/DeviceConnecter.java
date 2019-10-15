package com.amx.jax.device;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.adapter.IDeviceConnecter;
import com.amx.jax.sso.SSOStatus.SSOServerCodes;
import com.amx.jax.sso.SSOStatus.SSOServerError;
import com.amx.utils.ArgUtil;

@Component
public class DeviceConnecter implements IDeviceConnecter {

	@Autowired
	private CardBox cardBox;

	@Override
	public void saveCardDetailsByTerminal(String terminalId, CardData data) {
		try {
			if (ArgUtil.isEmpty(data) || data.isEmpty()) {
				cardBox.fastRemove(terminalId);
			} else {
				cardBox.put(terminalId, data);
			}
		} catch (Exception e) {
			throw new SSOServerError(SSOServerCodes.ERROR_TERMINAL_CARD_DATA, "Cannot Save Card Data for Terminal");
		}
	}

	@Override
	public CardData getCardDetailsByTerminal(String terminalId, Boolean wait, Boolean flush)
			throws InterruptedException {
		CardData data = null;
		try {
			if (wait) {
				data = cardBox.take(terminalId, 15, TimeUnit.SECONDS);
			} else {
				data = cardBox.get(terminalId);
			}
			if (flush) {
				cardBox.fastRemove(terminalId);
			}
		} catch (Exception e) {
			throw new SSOServerError(SSOServerCodes.ERROR_TERMINAL_CARD_DATA, "Cannot Read Card Data for Terminal");
		}
		return data;
	}

	@Autowired
	private NotipyBox notipyBox;

	@Override
	public void sendSACtoEmployee(String empId, String sac) {
		NotipyData x = notipyBox.getOrDefault(empId);
		x.setSac(sac);
		x.setUpdatestamp(System.currentTimeMillis());
		notipyBox.put(empId, x);
	}

}
