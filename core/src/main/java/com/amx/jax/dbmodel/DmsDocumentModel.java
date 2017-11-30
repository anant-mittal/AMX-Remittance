package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DMS_DOC_BLOB")
public class DmsDocumentModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7419298983902010861L;
	
	@Id
	@Column(name="SEQ_NO")
	private BigDecimal idNo;
	@Column(name="DOC_BLOB_ID")
	private BigDecimal docBlobId;
	@Column(name="CNTRYCD")
	private BigDecimal countryId;
	@Column(name="DOC_FIN_YR")
	private BigDecimal docFyr;
	public BigDecimal getIdNo() {
		return idNo;
	}
	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
	public BigDecimal getDocBlobId() {
		return docBlobId;
	}
	public void setDocBlobId(BigDecimal docBlobId) {
		this.docBlobId = docBlobId;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getDocFyr() {
		return docFyr;
	}
	public void setDocFyr(BigDecimal docFyr) {
		this.docFyr = docFyr;
	}
	
	
	

}
