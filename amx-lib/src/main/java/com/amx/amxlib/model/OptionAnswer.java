package com.amx.amxlib.model;

public class OptionAnswer extends AbstractAnswer {

	String optionKey;
	String optionValue;
	

	
	public OptionAnswer() {
		super();
	}
	
	public OptionAnswer(String optionKey, String optionValue) {
		super();
		this.optionKey = optionKey;
		this.optionValue = optionValue;
	}
	public String getOptionKey() {
		return optionKey;
	}
	public void setOptionKey(String optionKey) {
		this.optionKey = optionKey;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
}
