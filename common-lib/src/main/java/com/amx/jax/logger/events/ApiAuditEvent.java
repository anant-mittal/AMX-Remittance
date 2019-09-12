package com.amx.jax.logger.events;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.exception.AmxApiException;
import com.amx.jax.logger.AuditEvent;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

public class ApiAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 6277691611931240782L;

	public static class ApiAuditEventContext {
		public static Map<String, String> put(String key, Object value) {
			Map<String, String> x = map(new HashMap<String, String>());
			x.put(key, ArgUtil.parseAsString(value));
			return x;
		}

		@SuppressWarnings("unchecked")
		public static Map<String, String> map(Map<String, String> details) {
			Object mapObj = ContextUtil.map().get("ApiAuditEvent");
			if (ArgUtil.isEmpty(details)) {
				return (Map<String, String>) mapObj;
			} else if (ArgUtil.isEmpty(mapObj)) {
				ContextUtil.map().put("ApiAuditEvent", details);
				return details;
			} else {
				Map<String, String> map = ((Map<String, String>) mapObj);
				map.putAll(details);
				ContextUtil.map().put("ApiAuditEvent", map);
				return map;
			}
		}
	}

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
		this.errorCode = ArgUtil.isEmpty(excep.getErrorKey()) ? ArgUtil.parseAsString(excep.getError())
				: excep.getErrorKey();
		this.description = String.format("%s_%s:%s", this.type, this.result, this.errorCode);
		this.details = ApiAuditEventContext.map(excep.getDetailMap());

	}

	public ApiAuditEvent(AmxApiException excep) {
		this(Type.API, excep);
	}

}
