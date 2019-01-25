package com.amx.jax.manager;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.JaxFieldDto;
import com.amx.amxlib.model.ValidationRegexDto;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxFieldManager {

	public void validateJaxFieldRegEx(JaxFieldDto jaxField, String value) {
		if (jaxField.getAdditionalValidations() != null) {
			for (ValidationRegexDto validationRegexDto : jaxField.getValidationRegex()) {
				Pattern pattern = Pattern.compile(validationRegexDto.getValue());
				if (!pattern.matcher(value).matches()) {
					throw new GlobalException("Field " + jaxField.getLabel() + " must satify criteria:- "
							+ validationRegexDto.getDescription());
				}
			}
		}
	}

}
