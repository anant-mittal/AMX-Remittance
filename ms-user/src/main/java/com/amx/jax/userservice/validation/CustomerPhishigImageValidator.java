package com.amx.jax.userservice.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amx.jax.exception.GlobalException;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;

@Component
public class CustomerPhishigImageValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return CustomerPhishigImageValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		CustomerRegistrationTrnxModel beneficiaryTrnxModel = (CustomerRegistrationTrnxModel) target;
		String caption = beneficiaryTrnxModel.getCaption();
		String imageUrl = beneficiaryTrnxModel.getImageUrl();
		if (StringUtils.isBlank(imageUrl)) {
			throw new GlobalException("Image URL can't be blank");
		}
		if (StringUtils.isBlank(caption)) {
			throw new GlobalException("Caption can't be blank");
		}
	}

}
