package com.amx.service_provider.dbmodel.webservice;

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
	
	@Column(name = "FLEXIFIELD2")
	String flexiField2;
	
	@Column(name = "FLEXIFIELD3")
	String flexiField3;
	
	@Column(name = "FLEXIFIELD4")
	String flexiField4;

	@Column(name = "KEYSTORE_PATH")
	String keystore_path;

	@Column(name = "KEYSTORE_USER")
	String keystore_user;

	@Column(name = "KEYSTORE_PWD")
	String keystore_pwd;

	@Column(name = "KEYSTORE_ALIAS")
	String keystore_alias;

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

	public String getFlexiField2()
	{
		return flexiField2;
	}

	public void setFlexiField2(String flexiField2)
	{
		this.flexiField2 = flexiField2;
	}

	public String getFlexiField3()
	{
		return flexiField3;
	}

	public void setFlexiField3(String flexiField3)
	{
		this.flexiField3 = flexiField3;
	}

	public String getFlexiField4()
	{
		return flexiField4;
	}

	public void setFlexiField4(String flexiField4)
	{
		this.flexiField4 = flexiField4;
	}

	public String getKeystore_path()
	{
		return keystore_path;
	}

	public void setKeystore_path(String keystore_path)
	{
		this.keystore_path = keystore_path;
	}

	public String getKeystore_user()
	{
		return keystore_user;
	}

	public void setKeystore_user(String keystore_user)
	{
		this.keystore_user = keystore_user;
	}

	public String getKeystore_pwd()
	{
		return keystore_pwd;
	}

	public void setKeystore_pwd(String keystore_pwd)
	{
		this.keystore_pwd = keystore_pwd;
	}

	public String getKeystore_alias()
	{
		return keystore_alias;
	}

	public void setKeystore_alias(String keystore_alias)
	{
		this.keystore_alias = keystore_alias;
	}
}