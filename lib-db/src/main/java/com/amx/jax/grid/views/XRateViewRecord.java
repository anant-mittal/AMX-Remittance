package com.amx.jax.grid.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.es.ESDocFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/*
 * Author Rahamathali Shaik
*/
@Entity
@Table(name = "EX_V_RATE_PATTERN")
@Embeddable
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class XRateViewRecord implements Serializable {

	private static final long serialVersionUID = 7477672120827921904L;

	@Id
	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "PROCESS_DATE")
	private Date processDate;

	@Column(name = "AVG_SELLRATE")
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	private BigDecimal avgSellRate;

	@Column(name = "CURRENCY_CODE")
	private String currencyCode;

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public BigDecimal getAvgSellRate() {
		return avgSellRate;
	}

	public void setAvgSellRate(BigDecimal avgSellRate) {
		this.avgSellRate = avgSellRate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}
