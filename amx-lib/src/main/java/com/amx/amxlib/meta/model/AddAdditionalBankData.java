package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class AddAdditionalBankData {
	private BigDecimal additionalBankRuleFiledId;
	private String additionalDesc;
	private String flexiField;
	private String variableName;
	private BigDecimal additionalBankId;
	private String addItionalData;

	private String fieldType;
	private String navicable;
	private String mandatory;
	private int minLenght;
	private BigDecimal maxLenght;
	private String validation;
	private String isActive;
	private BigDecimal fieldLength;
	private Boolean required = false;

	private Boolean renderInputText = false;
	private Boolean renderSelect = false;
	private Boolean renderOneSelect = false;
	private String oneAdditional;
	private BigDecimal oneAdditionalId;
	private String amicCode;
	private String amicDesc;
	private Boolean msgRender=false;
	
	/*public List<AdditionalBankRuleAddData> additionalBankRuleData = new ArrayList<AdditionalBankRuleAddData>();
	public List<AdditionalBankDetailsView> listadditionAmiecData = new CopyOnWriteArrayList<AdditionalBankDetailsView>();*/
}
