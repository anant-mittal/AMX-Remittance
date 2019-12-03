package com.amx.jax.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AmxSharedConfig.CommunicationPrefs;
import com.amx.jax.AmxSharedConfigClient;
import com.amx.jax.def.Communication.CommunicationEvent;
import com.amx.jax.dict.Communicatable;
import com.amx.utils.ArgUtil;

@Component
public class CommunicationPrefsUtil {

	public static class CommunicationPrefsResult {
		private boolean email;
		private boolean sms;
		private boolean whatsApp;
		private boolean pushNotify;

		public boolean isEmail() {
			return email;
		}

		public void setEmail(boolean email) {
			this.email = email;
		}

		public boolean isSms() {
			return sms;
		}

		public void setSms(boolean sms) {
			this.sms = sms;
		}

		public boolean isWhatsApp() {
			return whatsApp;
		}

		public void setWhatsApp(boolean whatsApp) {
			this.whatsApp = whatsApp;
		}

		public boolean isPushNotify() {
			return pushNotify;
		}

		public void setPushNotify(boolean pushNotify) {
			this.pushNotify = pushNotify;
		}

	}

	@Autowired
	AmxSharedConfigClient amxSharedConfigClient;

	private CommunicationPrefs get(CommunicationEvent event) {
		for (CommunicationPrefs iterable_element : amxSharedConfigClient.getCommunicationPrefs().getResults()) {
			if (iterable_element.getEvent().equals(event.name())) {
				return iterable_element;
			}
		}
		return null;
	}

	public CommunicationPrefsResult forCustomer(CommunicationEvent event, Communicatable communicatable) {
		// amxSharedConfigClient.clear();
		CommunicationPrefs prefs = get(event);

		CommunicationPrefsResult result = new CommunicationPrefsResult();

		if (ArgUtil.isEmpty(prefs)) {
			return result;
		}

		String alwaysLong = "9";
		String alwaysChar = "A";

		boolean isAlwaysEmail = alwaysLong.equals(prefs.getEmailPrefs()) || alwaysChar.equals(prefs.getEmailPrefs());
		boolean isAlwaysSMS = alwaysLong.equals(prefs.getSmsPrefs()) || alwaysChar.equals(prefs.getSmsPrefs());
		boolean isAlwaysWA = alwaysLong.equals(prefs.getWaPrefs()) || alwaysChar.equals(prefs.getWaPrefs());
		boolean isAlwaysPush = alwaysLong.equals(prefs.getPushPrefs()) || alwaysChar.equals(prefs.getPushPrefs());

		for (long i = 1; i < 9; i++) {

			String thisLong = Long.toString(i);

			if ((thisLong.equals(prefs.getEmailPrefs()) || isAlwaysEmail) && communicatable.canSendEmail()) {
				result.setEmail(true);
			}

			if ((thisLong.equals(prefs.getSmsPrefs()) || isAlwaysSMS) && communicatable.canSendMobile()) {
				result.setSms(true);
			}

			if ((thisLong.equals(prefs.getWaPrefs()) || isAlwaysWA) && communicatable.canSendWhatsApp()) {
				result.setWhatsApp(true);
			}

			if ((thisLong.equals(prefs.getPushPrefs()) || isAlwaysPush)) {
				result.setPushNotify(true);
			}

			if ((result.isEmail() && !isAlwaysEmail) || (result.isSms() && !isAlwaysSMS)
					|| (result.isWhatsApp() && !isAlwaysWA) || (result.isPushNotify() && !isAlwaysPush)) {
				return result;
			}
		}
		return result;
	}

}
