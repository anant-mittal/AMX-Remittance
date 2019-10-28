package com.amx.jax.logger;

import java.util.List;

import org.springframework.stereotype.Component;

import com.amx.common.HandlerBeanFactory;

@Component
public class AuditHandlerBeanFactory extends HandlerBeanFactory<AuditHandler> {

	private static final long serialVersionUID = 2959267620648739583L;

	public AuditHandlerBeanFactory(List<AuditHandler> libs) {
		super(libs);
	}

}
