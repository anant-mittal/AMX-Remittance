package com.amx.jax.complaince;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName= "report_indicators")
public class ReportIndicators {
	
	private  String indicator;

	 @JacksonXmlProperty
	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public ReportIndicators(String indicator) {
		super();
		this.indicator = indicator;
	}

	public ReportIndicators() {
		super();
		
	}
	
	
	
	


	


}
