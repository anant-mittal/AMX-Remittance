package com.amx.amxlib.meta.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddAdditionalBankDataDto {
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
    private Boolean msgRender = false;
    private String exceptionMessage;
    private String LocalFullName;

    public List<AdditionalBankRuleAddDataDto> additionalBankRuleData = new ArrayList<>();
    public List<AdditionalBankDetailsViewDto> listadditionAmiecData = new ArrayList<>();

    public AddAdditionalBankDataDto() {

    }

    public AddAdditionalBankDataDto(BigDecimal additionalBankRuleFiledId, String additionalDesc, String flexiField,
            String variableName, BigDecimal additionalBankId, String addItionalData, String fieldType, String navicable,
            String mandatory, int minLenght, BigDecimal maxLenght, String validation, String isActive,
            BigDecimal fieldLength, Boolean required, Boolean renderInputText, Boolean renderSelect,
            Boolean renderOneSelect, String oneAdditional, BigDecimal oneAdditionalId, String amicCode, String amicDesc,
            Boolean msgRender, String LocalFullName) {
        super();
        this.additionalBankRuleFiledId = additionalBankRuleFiledId;
        this.additionalDesc = additionalDesc;
        this.flexiField = flexiField;
        this.variableName = variableName;
        this.additionalBankId = additionalBankId;
        this.addItionalData = addItionalData;
        this.fieldType = fieldType;
        this.navicable = navicable;
        this.mandatory = mandatory;
        this.minLenght = minLenght;
        this.maxLenght = maxLenght;
        this.validation = validation;
        this.isActive = isActive;
        this.fieldLength = fieldLength;
        this.required = required;
        this.renderInputText = renderInputText;
        this.renderSelect = renderSelect;
        this.renderOneSelect = renderOneSelect;
        this.oneAdditional = oneAdditional;
        this.oneAdditionalId = oneAdditionalId;
        this.amicCode = amicCode;
        this.amicDesc = amicDesc;
        this.LocalFullName = LocalFullName;

        this.msgRender = msgRender;
    }

    public BigDecimal getAdditionalBankRuleFiledId() {
        return additionalBankRuleFiledId;
    }

    public void setAdditionalBankRuleFiledId(BigDecimal additionalBankRuleFiledId) {
        this.additionalBankRuleFiledId = additionalBankRuleFiledId;
    }

    public String getAdditionalDesc() {
        return additionalDesc;
    }

    public void setAdditionalDesc(String additionalDesc) {
        this.additionalDesc = additionalDesc;
    }

    public String getFlexiField() {
        return flexiField;
    }

    public void setFlexiField(String flexiField) {
        this.flexiField = flexiField;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public BigDecimal getAdditionalBankId() {
        return additionalBankId;
    }

    public void setAdditionalBankId(BigDecimal additionalBankId) {
        this.additionalBankId = additionalBankId;
    }

    public String getAddItionalData() {
        return addItionalData;
    }

    public void setAddItionalData(String addItionalData) {
        this.addItionalData = addItionalData;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getNavicable() {
        return navicable;
    }

    public void setNavicable(String navicable) {
        this.navicable = navicable;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public int getMinLenght() {
        return minLenght;
    }

    public void setMinLenght(int minLenght) {
        this.minLenght = minLenght;
    }

    public BigDecimal getMaxLenght() {
        return maxLenght;
    }

    public void setMaxLenght(BigDecimal maxLenght) {
        this.maxLenght = maxLenght;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public BigDecimal getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(BigDecimal fieldLength) {
        this.fieldLength = fieldLength;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getRenderInputText() {
        return renderInputText;
    }

    public void setRenderInputText(Boolean renderInputText) {
        this.renderInputText = renderInputText;
    }

    public Boolean getRenderSelect() {
        return renderSelect;
    }

    public void setRenderSelect(Boolean renderSelect) {
        this.renderSelect = renderSelect;
    }

    public Boolean getRenderOneSelect() {
        return renderOneSelect;
    }

    public void setRenderOneSelect(Boolean renderOneSelect) {
        this.renderOneSelect = renderOneSelect;
    }

    public String getOneAdditional() {
        return oneAdditional;
    }

    public void setOneAdditional(String oneAdditional) {
        this.oneAdditional = oneAdditional;
    }

    public BigDecimal getOneAdditionalId() {
        return oneAdditionalId;
    }

    public void setOneAdditionalId(BigDecimal oneAdditionalId) {
        this.oneAdditionalId = oneAdditionalId;
    }

    public String getAmicCode() {
        return amicCode;
    }

    public void setAmicCode(String amicCode) {
        this.amicCode = amicCode;
    }

    public String getAmicDesc() {
        return amicDesc;
    }

    public void setAmicDesc(String amicDesc) {
        this.amicDesc = amicDesc;
    }

    public Boolean getMsgRender() {
        return msgRender;
    }

    public void setMsgRender(Boolean msgRender) {
        this.msgRender = msgRender;
    }

    public List<AdditionalBankRuleAddDataDto> getAdditionalBankRuleData() {
        return additionalBankRuleData;
    }

    public void setAdditionalBankRuleData(List<AdditionalBankRuleAddDataDto> additionalBankRuleData) {
        this.additionalBankRuleData = additionalBankRuleData;
    }

    public List<AdditionalBankDetailsViewDto> getListadditionAmiecData() {
        return listadditionAmiecData;
    }

    public void setListadditionAmiecData(List<AdditionalBankDetailsViewDto> listadditionAmiecData) {
        this.listadditionAmiecData = listadditionAmiecData;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

	public String getLocalFullName() {
		return LocalFullName;
	}

	public void setLocalFullName(String localFullName) {
		LocalFullName = localFullName;
	}

	

	
    
    
}
