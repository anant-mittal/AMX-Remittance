package com.amx.jax.complaince;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="report_indicators")
public class ReportIndicators {
	
	
    @XmlElement(name="indicator")
	public List<String> getIndicator() {
		return indicator;
	}



	public void setIndicator(List<String> indicator) {
		this.indicator = indicator;
	}



	private  List<String> indicator;
	
	


	public ReportIndicators(List<String> indicator) {
		super();
		this.indicator = indicator;
	}



	public ReportIndicators() {
		super();
		
	}
	


}
