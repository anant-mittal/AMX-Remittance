package com.amx.jax.manager;

import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.JaxDynamicField;
import com.amx.jax.model.response.jaxfield.JaxFieldDto;
import com.amx.jax.model.response.jaxfield.ValidationRegexDto;
import com.amx.jax.util.DateUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxFieldManager {

	@Autowired
	DateUtil dateUtil;

	public void validateJaxFieldRegEx(JaxFieldDto jaxField, String value) {
		if (jaxField.getAdditionalValidations() != null && Boolean.TRUE.equals(jaxField.getRequired())) {
			for (ValidationRegexDto validationRegexDto : jaxField.getValidationRegex()) {
				Pattern pattern = Pattern.compile(validationRegexDto.getValue());
				if (!pattern.matcher(value).matches()) {
					throw new GlobalException("Field " + jaxField.getLabel() + " must satify criteria:- " + validationRegexDto.getDescription());
				}
			}
		}
	}

	public Map<String, Object> getAdditionalValidations(JaxDynamicField jaxDynamicField) {
		Map<String, Object> additionalValidations = null;
		switch (jaxDynamicField) {
		case BENE_DOB:
			additionalValidations = new HashedMap<>();
			additionalValidations.put("lteq", dateUtil.format(LocalDate.now(), "MM/d/YYYY"));
		default:
		}
		return additionalValidations;
	}

}
