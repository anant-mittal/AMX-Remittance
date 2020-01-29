package com.amx.jax.dbmodel.tpc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_TPC_CLIENT_MASTER")
public class TpcClientTerminalMaster {

	@Id
	@Column(name = "TPC_TERMINAL_MASTER_ID")
	int id;

	@Column(name = "TERMINAL_ID")
	String terminalId;

	@Column(name = "TERMINAL_IP")
	String terminalIp;

	@Column(name = "TERMINAL_DESC")
	String terminalDescription;

	@JoinColumn(name = "TPC_CLIENT_MASTER_ID")
	@ManyToOne
	TpcClientMaster tpcClientMaster;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getTerminalIp() {
		return terminalIp;
	}

	public void setTerminalIp(String terminalIp) {
		this.terminalIp = terminalIp;
	}

	public String getTerminalDescription() {
		return terminalDescription;
	}

	public void setTerminalDescription(String terminalDescription) {
		this.terminalDescription = terminalDescription;
	}

	public TpcClientMaster getTpcClientMaster() {
		return tpcClientMaster;
	}

	public void setTpcClientMaster(TpcClientMaster tpcClientMaster) {
		this.tpcClientMaster = tpcClientMaster;
	}

}
