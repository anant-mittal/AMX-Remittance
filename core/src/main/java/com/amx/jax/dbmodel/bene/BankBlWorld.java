package com.amx.jax.dbmodel.bene;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="BNKBLWORD")
@NamedQuery(name="BankBlWorld" ,query="select rownum as idNo,bnkCode,blWord,recStatus from BankBlWorld")
public class BankBlWorld  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="idNo")
	private BigDecimal idNo;
	@Column(name="BNKCOD")
	private String bnkCode;
	@Column(name="BLWORD")
	private String blWord;
	@Column(name="RECSTS")
	private String recStatus;
	public String getBnkCode() {
		return bnkCode;
	}
	public void setBnkCode(String bnkCode) {
		this.bnkCode = bnkCode;
	}
	public String getBlWord() {
		return blWord;
	}
	public void setBlWord(String blWord) {
		this.blWord = blWord;
	}
	public String getRecStatus() {
		return recStatus;
	}
	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}


