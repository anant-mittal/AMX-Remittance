package com.amx.jax.logger.events;

import java.math.BigDecimal;

import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CActivityEvent extends AmxAuditEvent<CActivityEvent> {

	private static final long serialVersionUID = 340515142629212154L;

	public static enum Type implements EventType {

		LOGIN,

		VALIDATION,

		PROFILE_UPDATE, ACCOUNT_LOCKED, ACCOUNT_UNLOCKED,

		BENE_ADD, BENE_UPDATE,

		APPLICATION_CREATED, APPLICATION_UPDATE,

		FC_UPDATE, TRANSACTION_CREATED,

		CONTACT_VERF,

		LANG_CHNG(EventMarker.NOTICE),

		TP_REDIRECT;

		EventMarker marker;

		@Override
		public EventMarker marker() {
			return this.marker;
		}

		Type() {
			this.marker = EventMarker.AUDIT;
		}

		Type(EventMarker marker) {
			this.marker = marker;
		}
	}

	public static enum Step implements EventStep {
		CREATE, SEND, RESEND, INIT, COMPLETE, VERIFY
	}

	public CActivityEvent(Type type, Object target) {
		super(type, target);
	}

	public CActivityEvent(Type type, BigDecimal targetId, Object target) {
		super(type, targetId, target);
	}

	public CActivityEvent(Type type, BigDecimal targetId) {
		super(type, targetId);
	}

	public CActivityEvent(Type type) {
		super(type);
	}

	@Override
	public String getDescription() {
		return this.type + (ArgUtil.isEmpty(this.step) ? Constants.BLANK : ("_" + step)) + ":" + this.result;
	}

	public static String getMergedString(String oldStr, String newStr) {
		if (!ArgUtil.isEmpty(newStr)) {
			oldStr = ArgUtil.isEmpty(oldStr) ? newStr
					: String.format("%s;%s", oldStr, newStr);
		}
		return oldStr;
	}

}
