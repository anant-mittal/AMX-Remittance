package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_PURPOSE_TRNX_AMIEC_DESC")
public class PurposeTrnxAmicDesc {

	 @Id
	 @GeneratedValue(generator = "amic_id_seq", strategy = GenerationType.SEQUENCE)
	 @SequenceGenerator(name = "amic_id_seq", sequenceName = "EX_AMIC_ID_SEQ", allocationSize = 1)
	 @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	 private BigDecimal Id;
	 
	 @Column(name="AMIEC_CODE")
	  private String amicCode;
	 
	 @Column(name="SHORT_DESC")
	  private String shortDesc;
	 
	 @Column(name="FULL_DESC")
	  private String fullDesc;
	 
	 @Column(name="LOCAL_FULL_DESC")
	  private String localFulldesc;
	
	 @Column(name="LANGUAGE_ID")
	 private BigDecimal languageId;

	public BigDecimal getId() {
		return Id;
	}

	public void setId(BigDecimal id) {
		Id = id;
	}

	public String getAmicCode() {
		return amicCode;
	}

	public void setAmicCode(String amicCode) {
		this.amicCode = amicCode;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getFullDesc() {
		return fullDesc;
	}

	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
	}

	public String getLocalFulldesc() {
		return localFulldesc;
	}

	public void setLocalFulldesc(String localFulldesc) {
		this.localFulldesc = localFulldesc;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	 
	 }


