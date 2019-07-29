package com.amx.jax.dbmodel.remittance;
/**
 * Author	: Rabil
 * Date 	: 25/07/2019
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;



@Entity
@Table(name = "JAX_VW_PARAMETER_DETAILS")
public class ViewParameterDetails implements IResourceEntity{

	
	private static final long serialVersionUID = 2315791709068216697L;
	
	private BigDecimal parameterDetailsId;
	private BigDecimal parameterMasterId;
	private String recordId;
	private String paramCodeDef;
	private String fullDesc;
	private String shortDesc;
	
	private BigDecimal numericField1;
	private BigDecimal numericField2;
	private BigDecimal numericField3;
	private BigDecimal numericField4;
	private BigDecimal numericField5;
	private String charField1;
	private String charField2;
	private String charField3;
	private String charField4;
	private String charField5;
	
	private Date dateField1;
	private Date dateField2;
	private Date dateField3;
	private Date dateField4;
	private Date dateField5;
	
	
	@Id
	@Column(name = "PARAMETER_DETAILS_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getParameterDetailsId() {
		return parameterDetailsId;
	}

	public void setParameterDetailsId(BigDecimal parameterDetailsId) {
		this.parameterDetailsId = parameterDetailsId;
	}

	
	@Column(name = "PARAM_MASTER_ID")
	public BigDecimal getParameterMasterId() {
		return parameterMasterId;
	}

	public void setParameterMasterId(BigDecimal parameterMasterId) {
		this.parameterMasterId = parameterMasterId;
	}

	@Column(name = "RECORD_ID")
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Column(name = "PARAM_CODE_DEF")
	public String getParamCodeDef() {
		return paramCodeDef;
	}

	public void setParamCodeDef(String paramCodeDef) {
		this.paramCodeDef = paramCodeDef;
	}

	@Column(name = "FULL_DESC")
	public String getFullDesc() {
		return fullDesc;
	}

	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
	}

	@Column(name = "SHORT_DESC")
	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	

	@Column(name = "NUMERIC_FIELD1")
	public BigDecimal getNumericField1() {
		return numericField1;
	}

	public void setNumericField1(BigDecimal numericField1) {
		this.numericField1 = numericField1;
	}

	@Column(name = "NUMERIC_FIELD2")
	public BigDecimal getNumericField2() {
		return numericField2;
	}

	public void setNumericField2(BigDecimal numericField2) {
		this.numericField2 = numericField2;
	}

	@Column(name = "NUMERIC_FIELD3")
	public BigDecimal getNumericField3() {
		return numericField3;
	}

	public void setNumericField3(BigDecimal numericField3) {
		this.numericField3 = numericField3;
	}

	@Column(name = "NUMERIC_FIELD4")
	public BigDecimal getNumericField4() {
		return numericField4;
	}

	public void setNumericField4(BigDecimal numericField4) {
		this.numericField4 = numericField4;
	}

	@Column(name = "NUMERIC_FIELD5")
	public BigDecimal getNumericField5() {
		return numericField5;
	}

	public void setNumericField5(BigDecimal numericField5) {
		this.numericField5 = numericField5;
	}

	

	@Column(name = "CHAR_FIELD1")
	public String getCharField1() {
		return charField1;
	}

	public void setCharField1(String charField1) {
		this.charField1 = charField1;
	}

	@Column(name = "CHAR_FIELD2")
	public String getCharField2() {
		return charField2;
	}

	public void setCharField2(String charField2) {
		this.charField2 = charField2;
	}

	@Column(name = "CHAR_FIELD3")
	public String getCharField3() {
		return charField3;
	}

	public void setCharField3(String charField3) {
		this.charField3 = charField3;
	}

	@Column(name = "CHAR_FIELD4")
	public String getCharField4() {
		return charField4;
	}

	public void setCharField4(String charField4) {
		this.charField4 = charField4;
	}

	@Column(name = "CHAR_FIELD5")
	public String getCharField5() {
		return charField5;
	}

	public void setCharField5(String charField5) {
		this.charField5 = charField5;
	}

	

	@Column(name = "DATE_FIELD1")
	public Date getDateField1() {
		return dateField1;
	}

	public void setDateField1(Date dateField1) {
		this.dateField1 = dateField1;
	}

	@Column(name = "DATE_FIELD2")
	public Date getDateField2() {
		return dateField2;
	}

	public void setDateField2(Date dateField2) {
		this.dateField2 = dateField2;
	}

	@Column(name = "DATE_FIELD3")
	public Date getDateField3() {
		return dateField3;
	}

	public void setDateField3(Date dateField3) {
		this.dateField3 = dateField3;
	}

	@Column(name = "DATE_FIELD4")
	public Date getDateField4() {
		return dateField4;
	}

	public void setDateField4(Date dateField4) {
		this.dateField4 = dateField4;
	}

	@Column(name = "DATE_FIELD5")
	public Date getDateField5() {
		return dateField5;
	}

	public void setDateField5(Date dateField5) {
		this.dateField5 = dateField5;
	}

	@Override
	public BigDecimal resourceId() {
		// TODO Auto-generated method stub
		return this.parameterDetailsId;
	}

	@Override
	public String resourceName() {
		// TODO Auto-generated method stub
		return this.charField1;
	}

	@Override
	public String resourceCode() {
		// TODO Auto-generated method stub
		return this.paramCodeDef;
	}

	
}