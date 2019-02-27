package com.amx.jax.radar.jobs.customer;

import java.util.Date;

import com.amx.jax.grid.views.CustomerDetailViewRecord;
import com.amx.jax.grid.views.TranxViewRecord;
import com.amx.jax.radar.AESDocument;
import com.amx.jax.rates.AmxCurRate;
import com.amx.utils.ArgUtil;

public class OracleViewDocument extends AESDocument {

	CustomerDetailViewRecord customer;
	TranxViewRecord trnx;
	AmxCurRate xrate;

	public OracleViewDocument(CustomerDetailViewRecord customer) {
		super("customer");
		this.customer = customer;
		this.id = "customer-" + ArgUtil.parseAsBigDecimal(customer.getId());
		this.timestamp = ArgUtil.parseAsSimpleDate(customer.getLastUpdateDate());
	}

	public OracleViewDocument(TranxViewRecord trnx) {
		super("trnx");
		this.trnx = trnx;
		this.id = "appxn-" + ArgUtil.parseAsBigDecimal(trnx.getId());
		this.timestamp = ArgUtil.parseAsSimpleDate(trnx.getLastUpdateDate());
		this.normalizeTrnx();
	}

	public OracleViewDocument(AmxCurRate xrate) {
		super("xrate");
		this.xrate = xrate;
		this.timestamp = ArgUtil.parseAsSimpleDate(xrate.getTimestamp());
		this.id = ("xrate-" + "-" + xrate.getrSrc() + "-" + xrate.getrForCur() + "-"
				+ this.getTimestamp().getTime() / TIME_GAP_FIX).toLowerCase();

	}

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

	public AmxCurRate getXrate() {
		return xrate;
	}

	public void setXrate(AmxCurRate xrate) {
		this.xrate = xrate;
	}

	private static final long TIME_GAP_FIX = 1000 * 60 * 15;

	public OracleViewDocument xrate(AmxCurRate xrate) {
		this.xrate = xrate;
		return this;
	}

	public OracleViewDocument timestamp(Date timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public OracleViewDocument id(String id) {
		this.id = id;
		return this;
	}
}
