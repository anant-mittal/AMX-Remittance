package com.amx.jax.dbmodel.webservice;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "OWS_PARAM_RESPCODE")
public class OwsParamRespcode implements Serializable {

	private static final long serialVersionUID = 7805653137109249539L;

	@EmbeddedId
	OwsParamRespcodeKey owsParamRespcodeKey;

	@Column(name = "RSP_DESC")
	String response_des;

	@Column(name = "ACTION_IND")
	String action_ind;


	public OwsParamRespcodeKey getOwsParamRespcodeKey()
	{
		return owsParamRespcodeKey;
	}

	public void setOwsParamRespcodeKey(OwsParamRespcodeKey owsParamRespcodeKey)
	{
		this.owsParamRespcodeKey = owsParamRespcodeKey;
	}

	public String getResponse_des() {
		return response_des;
	}

	public void setResponse_des(String response_des) {
		this.response_des = response_des;
	}

	public String getAction_ind() {
		return action_ind;
	}

	public void setAction_ind(String action_ind) {
		this.action_ind = action_ind;
	}
}
