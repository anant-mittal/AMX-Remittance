package com.amx.jax.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "tenant", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TenantBean {

	private final String name;

	public TenantBean(String name) {
		this.name = name;
	}

	public void sayHello() {
		System.out.println(String.format("Hello from %s of type %s", this.name, this.getClass().getName()));
	}
}