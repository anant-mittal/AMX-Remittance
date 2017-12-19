package com.amx.jax.scope;

import org.springframework.stereotype.Component;

@Component
@TenantScoped
public class TenantBean {

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public TenantBean() {
	}

	public void sayHello() {
		System.out.println(String.format("Hello from %s of type %s", this.name, this.getClass().getName()));
	}
}