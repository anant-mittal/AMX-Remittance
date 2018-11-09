package com.amx.jax.exception;

public interface IMessageKey {
	public int getArgCount();

	public String getMessageKey(Object... args);
}
