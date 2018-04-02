package com.amx.jax.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.JaxFieldDto;
import com.amx.amxlib.model.request.GetJaxFieldRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.dbmodel.JaxConditionalFieldRule;
import com.amx.jax.dbmodel.JaxField;
import com.amx.jax.repository.JaxConditionalFieldRuleRepository;
import com.amx.jax.util.JaxUtil;

@Service
public class JaxFieldService extends AbstractService {

	@Autowired
	JaxUtil jaxUtil;

	@Autowired
	JaxConditionalFieldRuleRepository jaxConditionalFieldRuleRepository;

	@Override
	public String getModelType() {
		return "jax-field";
	}

	public ApiResponse getJaxFieldsForEntity(GetJaxFieldRequest request) {
		ApiResponse apiResponse = getBlackApiResponse();
		List<JaxConditionalFieldRule> fieldList = null;
		if (request.getCondition().getConditionKey() != null && request.getCondition().getConditionValue() != null) {
			fieldList = jaxConditionalFieldRuleRepository.findByEntityNameAndConditionKeyAndConditionValue(
					request.getEntity(), request.getCondition().getConditionKey(),
					request.getCondition().getConditionValue());
		} else {
			fieldList = jaxConditionalFieldRuleRepository.findByEntityName(request.getEntity());
		}
		apiResponse.getData().getValues().addAll(convert(fieldList));
		apiResponse.getData().setType("jax-field-rules");

		return apiResponse;
	}

	private List<JaxConditionalFieldDto> convert(List<JaxConditionalFieldRule> fieldList) {
		List<JaxConditionalFieldDto> list = new ArrayList<>();
		fieldList.forEach(i -> {
			list.add(convert(i));
		});
		return list;
	}

	private JaxConditionalFieldDto convert(JaxConditionalFieldRule i) {
		JaxConditionalFieldDto dto = new JaxConditionalFieldDto();
		dto.setEntityName(i.getEntityName());
		JaxFieldDto field = convert(i.getField());
		dto.setField(field);
		dto.setId(i.getId());
		return dto;
	}

	private JaxFieldDto convert(JaxField field) {
		JaxFieldDto dto = new JaxFieldDto();
		jaxUtil.convert(field, dto);
		return dto;
	}

}
