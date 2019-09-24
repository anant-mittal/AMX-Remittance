package com.amx.jax.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AmxSharedConfig.CommunicationPrefs;
import com.amx.jax.AmxSharedConfigClient;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.Communicatable;

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

	private CommunicationPrefs get(CommunicationEvents event) {
		for (CommunicationPrefs iterable_element : amxSharedConfigClient.getCommunicationPrefs().getResults()) {
			if (iterable_element.getEvent().equals(event)) {
				return iterable_element;
			}
		}
		return null;
	}

	public CommunicationPrefsResult forCustomer(CommunicationEvents event, Communicatable communicatable) {
		//amxSharedConfigClient.clear();
		CommunicationPrefs prefs = get(event);

		CommunicationPrefsResult result = new CommunicationPrefsResult();

		for (long i = 1; i < 5; i++) {
			if (prefs.getEmailPrefs().longValue() == i && communicatable.canSendEmail()) {
				result.setEmail(true);
			}

			if (prefs.getSmsPrefs().longValue() == i && communicatable.canSendMobile()) {
				result.setSms(true);
			}

			if (prefs.getWaPrefs().longValue() == i && communicatable.canSendWhatsApp()) {
				result.setWhatsApp(true);
			}

			if (prefs.getPushPrefs().longValue() == i) {
				result.setPushNotify(true);
			}

			if (result.isEmail() || result.isSms() || result.isWhatsApp()) {
				return result;
			}
		}
		return result;
	}

}
