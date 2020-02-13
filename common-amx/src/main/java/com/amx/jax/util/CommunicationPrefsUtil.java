package com.amx.jax.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AmxSharedConfig.CommunicationPrefs;
import com.amx.jax.AmxSharedConfigClient;
import com.amx.jax.def.Communication.CommunicationEvent;
import com.amx.jax.dict.Communicatable;
import com.amx.jax.dict.ContactType;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

@Component
public class CommunicationPrefsUtil {

	private static String ALWAYSLONG = "9";
	private static String ALWAYSCHAR = "A";

	private static boolean isAlways(String prefString) {
		return prefString.contains(ALWAYSLONG) || prefString.contains(ALWAYSCHAR);
	}

	private static boolean isSkipVerify(String prefString) {
		return prefString.contains("-");
	}

	private static String readOrder(String prefString) {
		return prefString.replace("-", Constants.BLANK);
	}

	public static class CommunicationPrefsResult {
		private boolean email;
		private boolean sms;
		private boolean whatsApp;
		private boolean pushNotify;

		private boolean emailEnabled;
		private boolean smsEnaled;
		private boolean whatsAppEnabled;
		private boolean pushNotifyEnabled;

		private boolean emailAlways;
		private boolean smsAlways;
		private boolean whatsAppAlways;
		private boolean pushNotifyAlways;

		private boolean emailSkipVerify;
		private boolean smsSkipVerify;
		private boolean whatsAppSkipVerify;
		private boolean pushNotifySkipVerify;

		private String emailOrder;
		private String smsOrder;
		private String whatsAppOrder;
		private String pushNotifyOrder;

		public CommunicationPrefsResult(CommunicationPrefs prefs) {
			this.emailAlways = isAlways(prefs.getEmailPrefs());
			this.smsAlways = isAlways(prefs.getSmsPrefs());
			this.whatsAppAlways = isAlways(prefs.getWaPrefs());
			this.pushNotifyAlways = isAlways(prefs.getPushPrefs());

			this.emailSkipVerify = isSkipVerify(prefs.getEmailPrefs());
			this.smsSkipVerify = isSkipVerify(prefs.getSmsPrefs());
			this.whatsAppSkipVerify = isSkipVerify(prefs.getWaPrefs());
			this.pushNotifySkipVerify = isSkipVerify(prefs.getPushPrefs());

			this.emailOrder = readOrder(prefs.getEmailPrefs());
			this.smsOrder = readOrder(prefs.getSmsPrefs());
			this.whatsAppOrder = readOrder(prefs.getWaPrefs());
			this.pushNotifyOrder = readOrder(prefs.getPushPrefs());

		}

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

		public boolean isEmailSkipVerify() {
			return emailSkipVerify;
		}

		public void setEmailSkipVerify(boolean emailSkipVerify) {
			this.emailSkipVerify = emailSkipVerify;
		}

		public boolean isSmsSkipVerify() {
			return smsSkipVerify;
		}

		public void setSmsSkipVerify(boolean smsSkipVerify) {
			this.smsSkipVerify = smsSkipVerify;
		}

		public boolean isWhatsAppSkipVerify() {
			return whatsAppSkipVerify;
		}

		public void setWhatsAppSkipVerify(boolean whatsAppSkipVerify) {
			this.whatsAppSkipVerify = whatsAppSkipVerify;
		}

		public boolean isPushNotifySkipVerify() {
			return pushNotifySkipVerify;
		}

		public void setPushNotifySkipVerify(boolean pushNotifySkipVerify) {
			this.pushNotifySkipVerify = pushNotifySkipVerify;
		}

		public boolean isPushNotifyAlways() {
			return pushNotifyAlways;
		}

		public void setPushNotifyAlways(boolean pushNotifyAlways) {
			this.pushNotifyAlways = pushNotifyAlways;
		}

		public boolean isEmailAlways() {
			return emailAlways;
		}

		public void setEmailAlways(boolean emailAlways) {
			this.emailAlways = emailAlways;
		}

		public boolean isSmsAlways() {
			return smsAlways;
		}

		public void setSmsAlways(boolean smsAlways) {
			this.smsAlways = smsAlways;
		}

		public boolean isWhatsAppAlways() {
			return whatsAppAlways;
		}

		public void setWhatsAppAlways(boolean whatsAppAlways) {
			this.whatsAppAlways = whatsAppAlways;
		}

		public String getEmailOrder() {
			return emailOrder;
		}

		public void setEmailOrder(String emailOrder) {
			this.emailOrder = emailOrder;
		}

		public String getSmsOrder() {
			return smsOrder;
		}

		public void setSmsOrder(String smsOrder) {
			this.smsOrder = smsOrder;
		}

		public String getWhatsAppOrder() {
			return whatsAppOrder;
		}

		public void setWhatsAppOrder(String whatsAppOrder) {
			this.whatsAppOrder = whatsAppOrder;
		}

		public String getPushNotifyOrder() {
			return pushNotifyOrder;
		}

		public void setPushNotifyOrder(String pushNotifyOrder) {
			this.pushNotifyOrder = pushNotifyOrder;
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

		CommunicationPrefsResult result = new CommunicationPrefsResult(prefs);

		if (ArgUtil.isEmpty(prefs)) {
			return result;
		}

		boolean canSendEmail = (communicatable == null) || communicatable.canSendEmail()
				|| (result.isEmailSkipVerify() && communicatable.hasPresent(ContactType.EMAIL));
		boolean canSendSMS = (communicatable == null) || communicatable.canSendMobile()
				|| (result.isSmsSkipVerify() && communicatable.hasPresent(ContactType.SMS));
		boolean canSendWA = (communicatable == null) || communicatable.canSendWhatsApp()
				|| (result.isWhatsAppSkipVerify() && communicatable.hasPresent(ContactType.WHATSAPP));

		for (int i = 1; i < 9; i++) {

			String thisLong = Long.toString(i);

			if ((thisLong.equals(result.getEmailOrder()) || result.isEmailAlways())) {
				result.setEmailEnabled(true);
				if (canSendEmail) {
					result.setEmail(true);
				}
			}

			if ((thisLong.equals(result.getSmsOrder()) || result.isSmsAlways())) {
				result.setSmsEnaled(true);
				if (canSendSMS) {
					result.setSms(true);
				}
			}

			if ((thisLong.equals(result.getWhatsAppOrder()) || result.isWhatsAppAlways())) {
				result.setWhatsAppEnabled(true);
				if (canSendWA) {
					result.setWhatsApp(true);
				}
			}

			if ((thisLong.equals(result.getPushNotifyOrder()) || result.isPushNotifyAlways())) {
				result.setPushNotifyEnabled(true);
				result.setPushNotify(true);
			}

			if ((result.isEmail() && !result.isEmailAlways()) || (result.isSms() && !result.isSmsAlways())
					|| (result.isWhatsApp() && !result.isWhatsAppAlways())
					|| (result.isPushNotify() && !result.isPushNotifyAlways())) {
				return result;
			}
		}
		return result;
	}

}
