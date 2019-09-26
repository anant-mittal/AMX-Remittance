package com.amx.jax.model.response.remittance;

/**
 * @author rabil
 * @date 25/07/2019
 */
import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.model.ResourceDTO;

public class ParameterDetailsDto extends ResourceDTO {

	private static final long serialVersionUID = -3369097385587671323L;
	private BigDecimal parameterDetailsId;
	private BigDecimal parameterMasterId;
	private String recordId;// gift
	private String paramCodeDef;
	private String fullDesc;
	private String shortDesc;

	private BigDecimal numericUdf1; // branch code
	private BigDecimal numericUdf2; // amount
	private BigDecimal numericUdf3;// min amount
	private BigDecimal numericUdf4; // max amount
	private BigDecimal numericUdf5;
	private String charUdf1;
	private String charUdf2;// bank code
	private String charUdf3;
	private String charUdf4;
	private String charUdf5;

	private Date dateUdf1;
	private Date dateUdf2;
	private Date dateUdf3;
	private Date dateUdf4;
	private Date dateUdf5;

	private ResourceDTO resourceDto;

	public BigDecimal getParameterDetailsId() {
		return parameterDetailsId;
	}

	public void setParameterDetailsId(BigDecimal parameterDetailsId) {
		this.parameterDetailsId = parameterDetailsId;
	}

	public BigDecimal getParameterMasterId() {
		return parameterMasterId;
	}

	public void setParameterMasterId(BigDecimal parameterMasterId) {
		this.parameterMasterId = parameterMasterId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getParamCodeDef() {
		return paramCodeDef;
	}

	public void setParamCodeDef(String paramCodeDef) {
		this.paramCodeDef = paramCodeDef;
	}

	public String getFullDesc() {
		return fullDesc;
	}

	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public BigDecimal getNumericUdf1() {
		return numericUdf1;
	}

	public void setNumericUdf1(BigDecimal numericUdf1) {
		this.numericUdf1 = numericUdf1;
	}

	public BigDecimal getNumericUdf2() {
		return numericUdf2;
	}

	public void setNumericUdf2(BigDecimal numericUdf2) {
		this.numericUdf2 = numericUdf2;
	}

	public BigDecimal getNumericUdf3() {
		return numericUdf3;
	}

	public void setNumericUdf3(BigDecimal numericUdf3) {
		this.numericUdf3 = numericUdf3;
	}

	public BigDecimal getNumericUdf4() {
		return numericUdf4;
	}

	public void setNumericUdf4(BigDecimal numericUdf4) {
		this.numericUdf4 = numericUdf4;
	}

	public BigDecimal getNumericUdf5() {
		return numericUdf5;
	}

	public void setNumericUdf5(BigDecimal numericUdf5) {
		this.numericUdf5 = numericUdf5;
	}

	public String getCharUdf1() {
		return charUdf1;
	}

	public void setCharUdf1(String charUdf1) {
		this.charUdf1 = charUdf1;
	}

	public String getCharUdf2() {
		return charUdf2;
	}

	public void setCharUdf2(String charUdf2) {
		this.charUdf2 = charUdf2;
	}

	public String getCharUdf3() {
		return charUdf3;
	}

	public void setCharUdf3(String charUdf3) {
		this.charUdf3 = charUdf3;
	}

	public String getCharUdf4() {
		return charUdf4;
	}

	public void setCharUdf4(String charUdf4) {
		this.charUdf4 = charUdf4;
	}

	public String getCharUdf5() {
		return charUdf5;
	}

	public void setCharUdf5(String charUdf5) {
		this.charUdf5 = charUdf5;
	}

	public Date getDateUdf1() {
		return dateUdf1;
	}

	public void setDateUdf1(Date dateUdf1) {
		this.dateUdf1 = dateUdf1;
	}

	public Date getDateUdf2() {
		return dateUdf2;
	}

	public void setDateUdf2(Date dateUdf2) {
		this.dateUdf2 = dateUdf2;
	}

	public Date getDateUdf3() {
		return dateUdf3;
	}

	public void setDateUdf3(Date dateUdf3) {
		this.dateUdf3 = dateUdf3;
	}

	public Date getDateUdf4() {
		return dateUdf4;
	}

	public void setDateUdf4(Date dateUdf4) {
		this.dateUdf4 = dateUdf4;
	}

	public Date getDateUdf5() {
		return dateUdf5;
	}

	public void setDateUdf5(Date dateUdf5) {
		this.dateUdf5 = dateUdf5;
	}

	public ResourceDTO getResourceDto() {
		return resourceDto;
	}

	public void setResourceDto(ResourceDTO resourceDto) {
		this.resourceDto = resourceDto;
	}

	public BigDecimal getAmount() {
		return numericUdf2;
	}

	public void setAmount(BigDecimal amount) {
		this.numericUdf2 = amount;
	}

	public BigDecimal getMinAmount() {
		return numericUdf3;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.numericUdf3 = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return numericUdf4;
	}

	public void setmaxAmount(BigDecimal amount) {
		this.numericUdf4 = amount;
	}
}
