package com.amx.jax.dbmodel.bene;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BankBlWorldEmbeded implements Serializable{

	@Column(name="BNKCOD")
	private String bnkCode;

	@Column(name="BLWORD")
	private String blWord;

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
	
}
