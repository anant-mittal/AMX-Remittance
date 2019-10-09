package com.amx.jax.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.GetJaxFieldRequest;
import com.amx.amxlib.model.request.AddJaxFieldRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.JaxConditionalFieldRule;
import com.amx.jax.dbmodel.JaxField;
import com.amx.jax.dbmodel.ValidationRegex;
import com.amx.jax.manager.JaxFieldManager;
import com.amx.jax.repository.JaxConditionalFieldRuleRepository;
import com.amx.jax.repository.JaxFieldRepository;
import com.amx.jax.util.JaxUtil;
import com.amx.libjax.model.jaxfield.JaxConditionalFieldDto;
import com.amx.libjax.model.jaxfield.JaxFieldDto;
import com.amx.libjax.model.jaxfield.ValidationRegexDto;

/**
 * @author Prashant
 *
 */
@Service
public class JaxFieldService extends AbstractService {

	@Autowired
	JaxUtil jaxUtil;
	@Autowired
	JaxConditionalFieldRuleRepository jaxConditionalFieldRuleRepository;
	@Autowired
	JaxFieldRepository jaxFieldRepository;
	@Autowired
	JaxFieldManager jaxFieldManager;

	@Override
	public String getModelType() {
		return "jax-field";
	}

	public ApiResponse<JaxConditionalFieldDto> getJaxFieldsForEntity(GetJaxFieldRequest request) {
		ApiResponse<JaxConditionalFieldDto> apiResponse = getBlackApiResponse();
		List<JaxConditionalFieldRule> fieldList = null;
		if (request.getCondition() != null && request.getCondition().getConditionKey() != null
				&& request.getCondition().getConditionValue() != null) {
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

	public ApiResponse addJaxField(AddJaxFieldRequest request) {
		ApiResponse apiResponse = getBlackApiResponse();
		BooleanResponse resp = new BooleanResponse(true);
		JaxField jaxfield = new JaxField();
		List<ValidationRegex> validationRegex = convertValidationDto(request.getValidationRegex());
		jaxUtil.convert(request, jaxfield);
		jaxfield.setValidationRegex(validationRegex);
		jaxFieldRepository.save(jaxfield);
		apiResponse.getData().getValues().add(resp);

		return apiResponse;
	}

	private List<ValidationRegex> convertValidationDto(List<ValidationRegexDto> validationRegexDto) {
		List<ValidationRegex> validationRegex = new ArrayList<>();
		for (ValidationRegexDto dto : validationRegexDto) {
			ValidationRegex entity = new ValidationRegex();
			jaxUtil.convert(dto, entity);
			validationRegex.add(entity);
		}

		return validationRegex;
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
		JaxFieldDto fieldDto = convert(i.getField());
		dto.setField(fieldDto);
		dto.setId(i.getId());
		return dto;
	}

	private JaxFieldDto convert(JaxField field) {
		JaxFieldDto dto = new JaxFieldDto();
		jaxUtil.convert(field, dto);
		dto.setRequired(ConstantDocument.Yes.equals(field.getRequired()) ? true : false);
		List<ValidationRegexDto> validationdtos = new ArrayList<>();
		if (field.getValidationRegex() != null) {
			field.getValidationRegex().forEach(validation -> {
				ValidationRegexDto regexdto = new ValidationRegexDto();
				jaxUtil.convert(validation, regexdto);
				validationdtos.add(regexdto);
			});
		}
		dto.setValidationRegex(validationdtos);
		return dto;
	}

	/**
	 * updates dto from database using name
	 * 
	 * @param jaxFieldDto
	 *            - input dto object
	 * 
	 */
	public void updateDtoFromDb(List<JaxFieldDto> jaxFieldDtos) {
		List<String> names = jaxFieldDtos.stream().map(i -> i.getLabel()).collect(Collectors.toList());
		List<JaxField> jaxFields = jaxFieldRepository.findByNameIn(names);
		final Map<String, JaxField> jaxFieldDbMap = jaxFields.stream()
				.collect(Collectors.toMap(JaxField::getName, x -> x));
		jaxFieldDtos.forEach(i -> {
			JaxField valueFromDB = jaxFieldDbMap.get(i.getLabel());
			if (valueFromDB != null) {
				jaxUtil.convertNotNull(valueFromDB, i);
			}
		});
	}

	public void validateJaxFieldRegEx(JaxFieldDto jaxField, String value) {
		jaxFieldManager.validateJaxFieldRegEx(jaxField, value);
	}
}
