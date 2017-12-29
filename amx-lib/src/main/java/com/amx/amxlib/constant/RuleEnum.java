/**
 * 
 */
package com.amx.amxlib.constant;

/**
 * @author Viki
 *
 */
public enum RuleEnum {
	
	EQUAL("equal"), 
	EQUAL_OR_GREATER("equal_or_greater"),
	EQUAL_OR_LESS("equal_or_less"),
	GREATER("grater"),
	LESS("less");

	private String ruleEnum;

	RuleEnum(String ruleEnum) {
		this.ruleEnum = ruleEnum;
	}

	/**
	 * @return the discountType
	 */
	public String getRuleEnum() {
		return this.ruleEnum;
	}

}
