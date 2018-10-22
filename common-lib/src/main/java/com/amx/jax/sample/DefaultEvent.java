package com.amx.jax.sample;

import com.amx.jax.logger.AuditEvent;
import com.amx.utils.EnumType;

public class DefaultEvent extends AuditEvent {

	public static enum Type implements EnumType {
		DEFAULT_EVENT;
		public static final EnumType DEFAULT = DEFAULT_EVENT;
	}

}
