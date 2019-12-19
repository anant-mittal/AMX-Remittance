package com.amx.jax.complaince;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="report_indicators")
public class ReportIndicators {
	
	private  String indicator;

	 @XmlElement(name="indicator")
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
