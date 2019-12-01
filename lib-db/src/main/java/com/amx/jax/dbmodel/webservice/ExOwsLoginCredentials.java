package com.amx.jax.dbmodel.webservice;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "EX_OWS_LOGIN_CREDENTIALS")

public class ExOwsLoginCredentials implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "OWS_LOGIN_ID")
	BigDecimal owsLoginId;

	public BigDecimal getOwsLoginId()
	{
		return owsLoginId;
	}

	public void setOwsLoginId(BigDecimal owsLoginId)
	{
		this.owsLoginId = owsLoginId;
	}

	@Column(name = "APPLICATION_COUNTRY")
	String applicationCountry;
	
	@Column(name = "BANK_CODE")
	String bankCode;
	
	@Column(name = "WS_USER_NAME")
	String wsUserName;
	
	@Column(name = "WS_PASSWORD")
	String wsPassword;
	
	@Column(name = "WS_PIN")
	String wsPin;
	
	@Column(name = "WS_AGENT_ID")
	String wsAgentId;
	
	@Column(name = "FLEXIFIELD1")
	String flexiField1;

	public String getApplicationCountry()
	{
		return applicationCountry;
	}

	public void setApplicationCountry(String applicationCountry)
	{
		this.applicationCountry = applicationCountry;
	}

	public String getBankCode()
	{
		return bankCode;
	}

	public void setBankCode(String bankCode)
	{
		this.bankCode = bankCode;
	}

	public String getWsUserName()
	{
		return wsUserName;
	}

	public void setWsUserName(String wsUserName)
	{
		this.wsUserName = wsUserName;
	}

	public String getWsPassword()
	{
		return wsPassword;
	}

	public void setWsPassword(String wsPassword)
	{
		this.wsPassword = wsPassword;
	}

	public String getWsPin()
	{
		return wsPin;
	}

	public void setWsPin(String wsPin)
	{
		this.wsPin = wsPin;
	}

	public String getWsAgentId()
	{
		return wsAgentId;
	}

	public void setWsAgentId(String wsAgentId)
	{
		this.wsAgentId = wsAgentId;
	}

	public String getFlexiField1()
	{
		return flexiField1;
	}

	public void setFlexiField1(String flexiField1)
	{
		this.flexiField1 = flexiField1;
	}
}