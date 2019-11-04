package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.swagger.ApiMockModelProperty;
import com.amx.utils.JsonUtil;

public class BenePackageRequest {
	@NotNull
	@ApiMockModelProperty(example="4314078")
	BigDecimal beneId;
	Map<String, Object> flexFields;
	Map<String, FlexFieldDto> flexFieldDtoMap;

	public BigDecimal getBeneId() {
		return beneId;
	}

	public void setBeneId(BigDecimal beneId) {
		this.beneId = beneId;
	}

	public Map<String, Object> getFlexFields() {
		return flexFields;
	}

	public void setFlexFields(Map<String, Object> flexFields) {
		this.flexFields = flexFields;
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
			this.flexFieldDtoMap = flexFieldMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, valueMapper));
		}
	}

	private Map<String, String> createFlexFieldMap(Map<String, Object> flexFields2) {

		Set<Entry<String, Object>> es = flexFields2.entrySet();
		Map<String, String> output = es.stream().collect(Collectors.toMap(x -> x.getKey(), x -> JsonUtil.toJson(x.getValue())));
		return output;
	}
}
