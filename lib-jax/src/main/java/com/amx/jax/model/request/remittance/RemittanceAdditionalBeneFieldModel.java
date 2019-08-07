package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.utils.JsonUtil;

public abstract class RemittanceAdditionalBeneFieldModel extends AbstractRemittanceApplicationRequestModel {

	protected BigDecimal beneId;
	private BigDecimal additionalBankRuleFiledId;
	private BigDecimal srlId;
	private Map<String, Object> flexFields;
	private Map<String, FlexFieldDto> flexFieldDtoMap;
	private Map<String, Object> additionalFields;
	private BigDecimal purposeOfTrnxId;
	private FlexFieldDto servicePackage;

	private String staffUserName;
	private String amlRemarks;

	public Map<String, Object> getAdditionalFields() {
		return additionalFields;
	}

	public void setAdditionalFields(Map<String, Object> additionalFields) {
		this.additionalFields = additionalFields;
	}

	public BigDecimal getBeneId() {
		return beneId;
	}

	public void setBeneId(BigDecimal beneId) {
		this.beneId = beneId;
	}

	public BigDecimal getSrlId() {
		return srlId;
	}

	public void setSrlId(BigDecimal srlId) {
		this.srlId = srlId;
	}

	public BigDecimal getAdditionalBankRuleFiledId() {
		return additionalBankRuleFiledId;
	}

	public void setAdditionalBankRuleFiledId(BigDecimal additionalBankRuleFiledId) {
		this.additionalBankRuleFiledId = additionalBankRuleFiledId;
	}

	public Map<String, FlexFieldDto> getFlexFieldDtoMap() {
		return flexFieldDtoMap;
	}

	public void setFlexFieldDtoMap(Map<String, FlexFieldDto> flexFieldDtoMap) {
		this.flexFieldDtoMap = flexFieldDtoMap;
	}

	public void populateFlexFieldDtoMap() {
		if (this.flexFields != null) {
			Map<String, String> flexFieldMap = createFlexFieldMap(flexFields);
			Function<Map.Entry<String, String>, FlexFieldDto> valueMapper = (entryObject) -> {
				String value = entryObject.getValue().toString();
				FlexFieldDto flexFieldDto = null;
				try {
					flexFieldDto = JsonUtil.fromJson(value, FlexFieldDto.class);
				} catch (Exception e) {
				}
				if (flexFieldDto == null) {
					flexFieldDto = new FlexFieldDto(value);
				}
				return flexFieldDto;
			};
			this.flexFieldDtoMap = flexFieldMap.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, valueMapper));
		}
	}

	public Map<String, Object> getFlexFields() {
		return flexFields;
	}

	public void setFlexFields(Map<String, Object> flexFields) {
		this.flexFields = flexFields;
	}

	private Map<String, String> createFlexFieldMap(Map<String, Object> flexFields2) {

		Set<Entry<String, Object>> es = flexFields2.entrySet();
		Map<String, String> output = es.stream()
				.collect(Collectors.toMap(x -> x.getKey(), x -> JsonUtil.toJson(x.getValue())));
		return output;
	}

	public BigDecimal getPurposeOfTrnxId() {
		return purposeOfTrnxId;
	}

	public void setPurposeOfTrnxId(BigDecimal purposeOfTrnxId) {
		this.purposeOfTrnxId = purposeOfTrnxId;
	}

	public String getStaffUserName() {
		return staffUserName;
	}

	public void setStaffUserName(String staffUserName) {
		this.staffUserName = staffUserName;
	}

	public String getAmlRemarks() {
		return amlRemarks;
	}

	public void setAmlRemarks(String amlRemarks) {
		this.amlRemarks = amlRemarks;
	}

	public FlexFieldDto getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(FlexFieldDto servicePackage) {
		this.servicePackage = servicePackage;
	}

}
