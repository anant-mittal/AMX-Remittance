package com.amx.jax.sample.tranx;

import java.util.List;

import com.amx.common.ScopedBeanFactory;

public class TransactionBridge<T> extends ScopedBeanFactory<String, T> {

	public TransactionBridge(List<T> beans) {
		super(beans);
	}

	public T getTransactionModel() {
		return null;
	}

	@Override
	public String[] getKeys(T bean) {
		return new String[] { bean.getClass().getName() };
	}

	@Override
	public String getKey() {
		return null;
	}

}
