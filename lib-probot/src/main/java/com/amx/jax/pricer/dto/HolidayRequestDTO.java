package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class HolidayRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Country Id Can not be Null or Empty")
	private BigDecimal countryId;
	
	@NotNull(message = "Event Date Can not be Null or Empty")
	private Date eventDate;
	

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	@Override
	public String toString() {
		return "HolidayRequestDTO [countryId=" + countryId + ", eventDate=" + eventDate + "]";
	}

	
	
	



	
	

}
