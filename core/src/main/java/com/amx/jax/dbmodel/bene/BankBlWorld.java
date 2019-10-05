package com.amx.jax.dbmodel.bene;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="BNKBLWORD")
//@Table(name="BNKBLWORD",uniqueConstraints = { @UniqueConstraint( columnNames = { "BNKCOD", "BLWORD" } ) })
//@NamedQuery(name="BankBlWorld" ,query="select rownum as idNo,bnkCode,blWord,recStatus from BankBlWorld")
public class BankBlWorld  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//@Table(name= "VW_REPUSH_BANK_TRNX_LIST_JAVA",uniqueConstraints = { @UniqueConstraint( columnNames = { "REMITTANCE_TRANSACTION_ID", "REMITTANCE_SPLIT_ID" } ) })
	
	@EmbeddedId
	BankBlWorldEmbeded bankWorldEmded;
	
	/*
	 * @Id
	 * 
	 * @Column(name="idNo") private BigDecimal idNo;
	 */

	/*
	 * @Column(name="BNKCOD") private String bnkCode;
	 * 
	 * @Column(name="BLWORD") private String blWord;
	 */
	
	@Column(name="RECSTS")
	private String recStatus;
	
	/*
	 * public String getBnkCode() { return bnkCode; } public void setBnkCode(String
	 * bnkCode) { this.bnkCode = bnkCode; } public String getBlWord() { return
	 * blWord; } public void setBlWord(String blWord) { this.blWord = blWord; }
	 */
	public String getRecStatus() {
		return recStatus;
	}
	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public BankBlWorldEmbeded getBankWorldEmded() {
		return bankWorldEmded;
	}
	public void setBankWorldEmded(BankBlWorldEmbeded bankWorldEmded) {
		this.bankWorldEmded = bankWorldEmded;
	}
	

}


