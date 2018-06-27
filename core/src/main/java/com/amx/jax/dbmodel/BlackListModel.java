package com.amx.jax.dbmodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "BLIST" )
@Proxy(lazy = false)
public class BlackListModel implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8515197950007880799L;
	
	
	private  String cName;
	private String fullName;
	private String arabicName;
	private  String cAName;
	
	

	@Id
	@Column(name="CNAME") 
	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	@Column(name="FUNAME")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name="AR_FUNAME")
	public String getArabicName() {
		return arabicName;
	}

	public void setArabicName(String arabicName) {
		this.arabicName = arabicName;
	}

	@Column(name="CANAME")
	public String getcAName() {
		return cAName;
	}

	public void setcAName(String cAName) {
		this.cAName = cAName;
	}
	
	

}
