package com.amx.jax.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.amx.amxlib.model.BeneAccountModel;

public class CashChannelValidator implements ConstraintValidator<CashChannelConstraint, BeneAccountModel> {

	@Override
	public void initialize(CashChannelConstraint constraintAnnotation) {

	}

	@Override
	public boolean isValid(BeneAccountModel field, ConstraintValidatorContext cxt) {
		return true;
	}

}
