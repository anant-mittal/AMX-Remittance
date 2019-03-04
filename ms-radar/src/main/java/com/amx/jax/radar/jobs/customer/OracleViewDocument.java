package com.amx.jax.radar.jobs.customer;

import java.util.Date;

import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.grid.views.BeneViewRecord;
import com.amx.jax.grid.views.BranchUserViewRecord;
import com.amx.jax.grid.views.BranchViewRecord;
import com.amx.jax.grid.views.CustomerDetailViewRecord;
import com.amx.jax.grid.views.TranxViewRecord;
import com.amx.jax.radar.AESDocument;
import com.amx.jax.rates.AmxCurRate;
import com.amx.utils.ArgUtil;

public class OracleViewDocument extends AESDocument {

	CustomerDetailViewRecord customer;
	TranxViewRecord trnx;
	AmxCurRate xrate;
	BranchViewRecord branch;
	BranchUserViewRecord user;
	UserDeviceClient client;
	BeneViewRecord bene;

	public OracleViewDocument(CustomerDetailViewRecord customer) {
		super("customer");
		this.customer = customer;
		this.id = "customer-" + ArgUtil.parseAsBigDecimal(customer.getId());
		this.timestamp = ArgUtil.parseAsSimpleDate(customer.getLastUpdateDate());
	}

	public OracleViewDocument(TranxViewRecord trnx) {
		super("trnx");
		this.trnx = trnx;
		this.id = "trnx-" + ArgUtil.parseAsBigDecimal(trnx.getId());
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

		this.branch = new BranchViewRecord();

		this.branch.setId(this.trnx.getCountryBranchId());
		this.trnx.setCountryBranchId(null);

		this.branch.setName(this.trnx.getCountryBranchName());
		this.trnx.setCountryBranchName(null);

		this.branch.setAreaId(this.trnx.getBranchAreaId());
		this.trnx.setBranchAreaId(null);

		this.branch.setAreaName(this.trnx.getBranchAreaName());
		this.trnx.setBranchAreaName(null);

		// Bene Details
		this.bene = new BeneViewRecord();
		this.bene.setBranchId(this.trnx.getBeneBankBranchId());
		this.trnx.setBeneBankBranchId(null);
		this.bene.setBranchName(this.trnx.getBeneBankBranchName());
		this.trnx.setBeneBankBranchName(null);
		this.bene.setBankId(this.trnx.getBeneBankId());
		this.trnx.setBeneBankId(null);
		this.bene.setBankName(this.trnx.getBeneBankName());
		this.trnx.setBeneBankName(null);
		this.bene.setCountryCode(this.trnx.getBeneCountryCode());
		this.trnx.setBeneCountryCode(null);

		this.user = new BranchUserViewRecord();

		this.user.setName(this.trnx.getBranchUser());
		this.trnx.setBranchUser(null);

		// Client Info
		this.client = new UserDeviceClient();
		this.client.setIp(this.trnx.getClientIp());
		this.trnx.setClientIp(null);

		this.client.setFingerprint(this.trnx.getClientId());
		this.trnx.setClientId(null);

		Object clientType = ArgUtil.parseAsEnum(this.trnx.getClientType(), ClientType.UNKNOWN);
		if (!ArgUtil.isEmpty(clientType)) {
			this.client.setClientType((ClientType) clientType);
			this.trnx.setClientType(null);
		}

	}

	public AmxCurRate getXrate() {
		return xrate;
	}

	public void setXrate(AmxCurRate xrate) {
		this.xrate = xrate;
	}

	private static final long TIME_GAP_FIX = 1000 * 60 * 30;

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

	public BranchViewRecord getBranch() {
		return branch;
	}

	public void setBranch(BranchViewRecord branch) {
		this.branch = branch;
	}

	public BranchUserViewRecord getUser() {
		return user;
	}

	public void setUser(BranchUserViewRecord user) {
		this.user = user;
	}

	public UserDeviceClient getClient() {
		return client;
	}

	public void setClient(UserDeviceClient client) {
		this.client = client;
	}

	public BeneViewRecord getBene() {
		return bene;
	}

	public void setBene(BeneViewRecord bene) {
		this.bene = bene;
	}
}
