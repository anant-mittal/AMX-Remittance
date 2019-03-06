package com.amx.jax.logger.events;

import com.amx.jax.exception.AmxApiException;
import com.amx.jax.logger.AuditEvent;
import com.amx.utils.ArgUtil;

public class ApiAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 6277691611931240782L;

	public static enum Type implements EventType {
		API;
		@Override
		public EventMarker marker() {
			return EventMarker.NOTICE;
		}
	}

	public ApiAuditEvent(Type type, AmxApiException excep) {
		super(type);
		this.message = excep.getErrorMessage();
		this.result = Result.ERROR;
		this.description = String.format("%s_%s:%s", this.type, this.result,
				ArgUtil.isEmpty(excep.getErrorKey()) ? ArgUtil.parseAsString(excep.getError())
						: excep.getErrorKey());

	}

	public ApiAuditEvent(AmxApiException excep) {
		this(Type.API, excep);
	}

}
