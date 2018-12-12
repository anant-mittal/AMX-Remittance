package com.amx.jax.dict;

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
		REMIT, FXORDER;
	}

}
