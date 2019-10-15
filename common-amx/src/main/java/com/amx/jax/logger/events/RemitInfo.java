package com.amx.jax.logger.events;

import java.math.BigDecimal;

public class RemitInfo {
	BigDecimal appid;
	BigDecimal amount;

	public RemitInfo(BigDecimal appid, BigDecimal amount) {
		super();
		this.appid = appid;
		this.amount = amount;
	}

	public BigDecimal getAppid() {
		return appid;
	}

	public void setAppid(BigDecimal appid) {
		this.appid = appid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
