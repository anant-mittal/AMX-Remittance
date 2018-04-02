package com.amx.jax.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amx.amxlib.model.JaxConditionalFieldRuleDto;
import com.amx.amxlib.model.JaxFieldDto;
import com.amx.amxlib.model.request.GetJaxFieldRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.dbmodel.JaxConditionalFieldRule;
import com.amx.jax.repository.JaxConditionalFieldRuleRepository;

@Service
public class JaxFieldService extends AbstractService {

	JaxConditionalFieldRuleRepository jaxConditionalFieldRuleRepository;

	@Override
	public String getModelType() {
		return "jax-field";
	}

	public ApiResponse getJaxFieldsForEntity(GetJaxFieldRequest request) {
		ApiResponse apiResponse = getBlackApiResponse();
		List<JaxConditionalFieldRule> fieldList = jaxConditionalFieldRuleRepository
				.findByEntityNameAndConditionKeyAndConditionValue(request.getEntity(),
						request.getCondition().getConditionKey(), request.getCondition().getConditionValue());
		apiResponse.getData().getValues().addAll(convert(fieldList));

		return apiResponse;
	}

	private List<JaxConditionalFieldRuleDto> convert(List<JaxConditionalFieldRule> fieldList) {
		List<JaxConditionalFieldRuleDto> list = new ArrayList<>();
		fieldList.forEach(i -> {
			list.add(convert(i));
		});
		return list;
	}

	private JaxConditionalFieldRuleDto convert(JaxConditionalFieldRule i) {
		JaxConditionalFieldRuleDto dto = new JaxConditionalFieldRuleDto();
		dto.setConditionKey(i.getConditionKey());
		dto.setConditionValue(i.getConditionValue());
		dto.setEntityName(i.getEntityName());
		JaxFieldDto field = null;
		dto.setField(field);
		dto.setId(i.getId());
		return dto;
	}

}
