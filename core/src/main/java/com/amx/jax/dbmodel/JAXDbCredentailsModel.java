package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_COUNTRY_DB_CREDENTIALS" )
public class JAXDbCredentailsModel implements java.io.Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5721171468266773520L;
	@Id
	@Column(name="APPLICATION_COUNTRY_ID")
	public BigDecimal applicationCountryId;
	@Column(name="ISO_CODE")
	public String countryIsoCode;
	@Column(name="COUNTRY_NAME")
	public String countryName;
	@Column(name="DB_HOST")
	public String dbHost;
	@Column(name="DB_PASS")
	public String dbPass;
	
	@Column(name="DB_PORT")
	public String dbPort;
	@Column(name="DB_SID")
	public String dbSid;
	@Column(name="DB_USER")
	public String dbUser;
	@Column(name="ISACTIVE")
	public String isActive;
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	public String getCountryIsoCode() {
		return countryIsoCode;
	}
	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getDbHost() {
		return dbHost;
	}
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	public String getDbPass() {
		return dbPass;
	}
	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}
	public String getDbPort() {
		return dbPort;
	}
	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}
	public String getDbSid() {
		return dbSid;
	}
	public void setDbSid(String dbSid) {
		this.dbSid = dbSid;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


}
