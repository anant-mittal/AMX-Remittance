package com.amx.jax.dbmodel.remittance;

/**]
 * @author rabil
 */
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;



@Entity
@Table(name = "V_BNKFLXVAL")
/*//@NamedNativeQuery(name="ViewBnkFlexVal" ,query="select rownum as idno,bnkcode,fileName,isActive, branchExchId ,beneBankCode ,beneBranchCode,branchExchId from V_BNKFLXVAL")
@NamedNativeQuery(name = "V_BNKFLXVAL", query ="select rownum as idno,BNKCOD,FILNAM,RECSTS, BRCH_EXCHID ,BENE_BNKCOD ,BENE_BRCHCOD,BANK_EXCHID from V_BNKFLXVAL",resultClass = ViewBnkFlexVal.class)
*/
public class ViewBnkFlexVal implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="IDNO")
	BigDecimal idNo;
	
	@Column(name="BNKCOD")
	String bnkcode;
	
	@Column(name="FILNAM")
	String fileName;
	
	@Column(name="RECSTS")
	String isActive;
	
	@Column(name="BRCH_EXCHID")
	String branchExchId;
	
	
	@Column(name="BANK_EXCHID")
	String bankExchId;
	
	@Column(name="BENE_BNKCOD")
	String beneBankCode;
	
	@Column(name="BENE_BRCHCOD")
	BigDecimal beneBranchCode;

	public BigDecimal getIdNo() {
		return idNo;
	}

	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}

	public String getBnkcode() {
		return bnkcode;
	}

	public void setBnkcode(String bnkcode) {
		this.bnkcode = bnkcode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getBranchExchId() {
		return branchExchId;
	}

	public void setBranchExchId(String branchExchId) {
		this.branchExchId = branchExchId;
	}

	public String getBeneBankCode() {
		return beneBankCode;
	}

	public void setBeneBankCode(String beneBankCode) {
		this.beneBankCode = beneBankCode;
	}

	public BigDecimal getBeneBranchCode() {
		return beneBranchCode;
	}

	public void setBeneBranchCode(BigDecimal beneBranchCode) {
		this.beneBranchCode = beneBranchCode;
	}

	public String getBankExchId() {
		return bankExchId;
	}

	public void setBankExchId(String bankExchId) {
		this.bankExchId = bankExchId;
	}
	
	
}



