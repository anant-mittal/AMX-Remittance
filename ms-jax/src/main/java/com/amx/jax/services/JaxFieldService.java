package com.amx.jax.services;

import java.math.BigDecimal;
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
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldDto;
import com.amx.jax.model.response.jaxfield.ValidationRegexDto;
import com.amx.jax.repository.JaxConditionalFieldRuleRepository;
import com.amx.jax.repository.JaxFieldRepository;
import com.amx.jax.service.CountryService;
import com.amx.jax.util.JaxUtil;

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
	@Autowired
	CountryService countryService;
	@Autowired
	BeneficiaryService beneficiaryService;

	@Override
	public String getModelType() {
		return "jax-field";
	}

	public ApiResponse<JaxConditionalFieldDto> getJaxFieldsForEntity(GetJaxFieldRequest request) {
		ApiResponse<JaxConditionalFieldDto> apiResponse = getBlackApiResponse();
		List<JaxConditionalFieldRule> fieldList = new ArrayList<>();
		if (request.getCondition() != null && request.getCondition().getConditionKey() != null
				&& request.getCondition().getConditionValue() != null) {
			// special handling for key
			if ("country-institution".equals(request.getCondition().getConditionKey())) {
				fieldList = getCountrytInstitutionFields(request);
			} else {
				fieldList = jaxConditionalFieldRuleRepository.findByEntityNameAndConditionKeyAndConditionValue(request.getEntity(),
						request.getCondition().getConditionKey(), request.getCondition().getConditionValue());
			}
		} else {
			fieldList = jaxConditionalFieldRuleRepository.findByEntityName(request.getEntity());
		}
		apiResponse.getData().getValues().addAll(convert(fieldList));
		apiResponse.getData().setType("jax-field-rules");

		return apiResponse;
	}

	private List<JaxConditionalFieldRule> getCountrytInstitutionFields(GetJaxFieldRequest request) {
		List<JaxConditionalFieldRule> output = new ArrayList<>();
		String value = request.getCondition().getConditionValue();
		BigDecimal countryId = new BigDecimal(value.split(",")[0]);
		BigDecimal beneficaryTypeId = new BigDecimal(value.split(",")[1]);
		if (countryService.isEgyptCountry(countryId) && beneficiaryService.isNonIndividualBene(beneficaryTypeId)) {
			output = jaxConditionalFieldRuleRepository.findByEntityNameAndConditionKeyAndConditionValue(request.getEntity(), "country-institution",
					"ALL");
		}
		if (!beneficiaryService.isNonIndividualBene(beneficaryTypeId)) {
			output = jaxConditionalFieldRuleRepository.findByEntityNameAndConditionKeyAndConditionValue(request.getEntity(), "bene-country-id",
					countryId.toString());
		}
		return output;
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
		fieldList.sort((o1, o2) -> {
			Double s1 = o1.getField().getSortOrder();
			Double s2 = o2.getField().getSortOrder();
			if (s1 == null) {
				return 0;
			}
			return s1.compareTo(s2);
		});
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
		dto.setDtoPath(field.getDefaultDtoPath());
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
		final Map<String, JaxField> jaxFieldDbMap = jaxFields.stream().collect(Collectors.toMap(JaxField::getName, x -> x));
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
