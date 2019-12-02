package com.amx.jax.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AmxSharedConfig.CommunicationPrefs;
import com.amx.jax.AmxSharedConfigClient;
import com.amx.jax.def.Communication.CommunicationEvent;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.Communicatable;
import com.amx.utils.ArgUtil;

@Component
public class CommunicationPrefsUtil {

	public static class CommunicationPrefsResult {
		private boolean email;
		private boolean sms;
		private boolean whatsApp;
		private boolean pushNotify;

		private boolean emailEnabled;
		private boolean smsEnaled;
		private boolean whatsAppEnabled;
		private boolean pushNotifyEnabled;

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

		public boolean isEmailEnabled() {
			return emailEnabled;
		}

		public void setEmailEnabled(boolean emailEnabled) {
			this.emailEnabled = emailEnabled;
		}

		public boolean isSmsEnaled() {
			return smsEnaled;
		}

		public void setSmsEnaled(boolean smsEnaled) {
			this.smsEnaled = smsEnaled;
		}

		public boolean isWhatsAppEnabled() {
			return whatsAppEnabled;
		}

		public void setWhatsAppEnabled(boolean whatsAppEnabled) {
			this.whatsAppEnabled = whatsAppEnabled;
		}

		public boolean isPushNotifyEnabled() {
			return pushNotifyEnabled;
		}

		public void setPushNotifyEnabled(boolean pushNotifyEnabled) {
			this.pushNotifyEnabled = pushNotifyEnabled;
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

	public CommunicationPrefsResult forEvent(CommunicationEvent event) {
		return forCustomer(event, null);
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

		boolean canSendEmail = (communicatable == null) || communicatable.canSendEmail();
		boolean canSendSMS = (communicatable == null) || communicatable.canSendMobile();
		boolean canSendWA = (communicatable == null) || communicatable.canSendWhatsApp();

		for (long i = 1; i < 9; i++) {

			String thisLong = Long.toString(i);

			if ((thisLong.equals(prefs.getEmailPrefs()) || isAlwaysEmail)) {
				result.setEmailEnabled(true);
				if (canSendEmail) {
					result.setEmail(true);
				}
			}

			if ((thisLong.equals(prefs.getSmsPrefs()) || isAlwaysSMS)) {
				result.setSmsEnaled(true);
				if (canSendSMS) {
					result.setSms(true);
				}
			}

			if ((thisLong.equals(prefs.getWaPrefs()) || isAlwaysWA)) {
				result.setWhatsAppEnabled(true);
				if (canSendWA) {
					result.setWhatsApp(true);
				}
			}

			if ((thisLong.equals(prefs.getPushPrefs()) || isAlwaysPush)) {
				result.setPushNotifyEnabled(true);
				result.setPushNotify(true);
			}

			if ((result.isEmail() && !isAlwaysEmail)
					|| (result.isSms() && !isAlwaysSMS)
					|| (result.isWhatsApp() && !isAlwaysWA)) {
				return result;
			}
		}
		return result;
	}

}
