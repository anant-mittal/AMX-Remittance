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

	public static enum CommunicationEvents implements CommunicationEvent {
		CASH_PICKUP_BANK, CASH_PICKUP_WU, CASH_PICKUP_TF, BIRTHDAY_WISHES, NEW_DEVICE_LOGIN, BPI_JOLLIBEE,
		ADD_BENEFICIARY, TRNX_BENE_CREDIT, REMITTANCE;
	
	public static CommunicationEvent fromString(String eventStr) {
		CommunicationEvent x = (CommunicationEvent) ArgUtil.parseAsEnum(eventStr, CommunicationEvents.class);
		if (ArgUtil.isEmpty(x)) {
			return new CommunicationEventModel(eventStr);
		}
		return x;
	}
	}

}
