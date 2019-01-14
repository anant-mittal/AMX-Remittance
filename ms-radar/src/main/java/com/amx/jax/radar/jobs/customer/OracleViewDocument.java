package com.amx.jax.radar.jobs.customer;

import com.amx.jax.grid.views.CustomerDetailViewRecord;
import com.amx.jax.grid.views.TranxViewRecord;
import com.amx.jax.radar.AESDocument;

public class OracleViewDocument extends AESDocument {

	CustomerDetailViewRecord customer;
	TranxViewRecord trnx;

	public CustomerDetailViewRecord getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDetailViewRecord customer) {
		this.customer = customer;
	}

	public TranxViewRecord getTrnx() {
		return trnx;
	}

	public void setTrnx(TranxViewRecord trnx) {
		this.trnx = trnx;
	}

	public void normalizeTrnx() {
		this.customer = new CustomerDetailViewRecord();

		this.customer.setId(this.trnx.getCustmerId());
		this.trnx.setCustmerId(null);

		this.customer.setIdentity(this.trnx.getIdentity());
		this.trnx.setIdentity(null);

		this.customer.setName(this.trnx.getCustmerName());
		this.trnx.setCustmerName(null);

		this.customer.setNationalityCode(this.trnx.getCustmerNationCode());
		this.trnx.setCustmerNationCode(null);

		this.customer.setNationality(this.trnx.getCustmerNation());
		this.trnx.setCustmerNation(null);

	}
}
