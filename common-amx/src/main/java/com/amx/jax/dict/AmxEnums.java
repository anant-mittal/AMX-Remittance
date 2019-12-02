package com.amx.jax.dict;

import com.amx.jax.def.Communication.CommunicationEvent;
import com.amx.jax.def.Communication.CommunicationEventModel;
import com.amx.utils.ArgUtil;
import com.amx.utils.EnumType;

public class AmxEnums {

	public static enum DenominationType implements EnumType {
		HIGH_VALUE_NOTES("High Value Notes"), MIXED_NOTES("Mixed Notes");

		private String note;

		DenominationType(String note) {
			this.note = note;
		}

		@Override
		public String note() {
			return note;
		}
	}

	public static enum Products implements EnumType {
		REMIT, FXORDER, HOME, OFFICE,REMITLINK;
	}

	public static enum FxOrderStatus implements EnumType {
		ORD, ACP, PCK, DVD, OFD_ACK, OFD_CNF, OFD, CND_ACK, CND, RTD_ACK, RTD
	}

	public static enum CommunicationEvents implements EnumType {
		CASH_PICKUP_BANK, CASH_PICKUP_WU, CASH_PICKUP_TF, NEW_DEVICE_LOGIN, BIRTHDAY_WISHES, ADD_BENEFICIARY,
		BPI_JOLLIBEE, TRNX_BENE_CREDIT, REMITTANCE, FORGOT_PASSWORD, RESET_PASSWORD, CONTACT_DETAILS, UPDATE_SECQUE,
		IPSOS_DISCOUNT, GIG_PENDING_TRNX, GIG_POLICY_CONFIRM, GIG_OPTOUT_POLICY, GIG_EXPIRY_POLICY, SIGNUP_ONLINE,
		FORGOT_SECQUE, BENE_CREAT_SUCC, PAYMENT_LINK,FC_ORDER_SUCCESS,FX_ORDER_OTP;
		public static CommunicationEvent fromString(String eventStr) {
			CommunicationEvent x = (CommunicationEvent) ArgUtil.parseAsEnum(eventStr, CommunicationEvents.class);
			if (ArgUtil.isEmpty(x)) {
				return new CommunicationEventModel(eventStr);
			}
			return x;
		}
	}

}
