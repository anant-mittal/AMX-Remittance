package com.amx.jax.radar.jobs.customer;

import java.util.Map;

import com.amx.jax.grid.views.TranxViewRecord;
import com.amx.jax.radar.AESDocument;

public class OracleViewDocument extends AESDocument {

	Map<String, Object> customer;
	TranxViewRecord trnx;

	public Map<String, Object> getCustomer() {
		return customer;
	}

	public void setCustomer(Map<String, Object> customer) {
		this.customer = customer;
	}

	public TranxViewRecord getTrnx() {
		return trnx;
	}

	public void setTrnx(TranxViewRecord trnx) {
		this.trnx = trnx;
	}
}
