package com.amx.jax.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.jax.dbmodel.bene.BankAccountLength;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.util.JaxUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BeneficiaryValidationService {

	@Autowired
	BankService bankService;

	@Autowired
	JaxUtil jaxUtil;

	public void validateBeneAccount(BeneAccountModel beneAccountModel) {
		List<BankAccountLength> accontNumLength = bankService.getBankAccountLength(beneAccountModel.getBankId());
		List<Integer> accNumLength = new ArrayList<>();
		accontNumLength.forEach(i -> {
			if (i.getAcLength() != null && i.getAcLength().intValue() > 0) {
				accNumLength.add(i.getAcLength().intValue());
			}
		});
		if (!accNumLength.isEmpty()) {
			boolean isValid = false;
			for (int i : accNumLength) {
				if (beneAccountModel.getBankAccountNumber().length() == i) {
					isValid = true;
				}
			}
			if (!isValid) {
				String validLengths = accNumLength.stream().map(i -> i.toString()).collect(Collectors.joining(","));
				String errorExpression = jaxUtil
						.buildErrorExpression(JaxError.INVALID_BANK_ACCOUNT_NUM_LENGTH.toString(), validLengths);
				throw new GlobalException("Invalid Bank Account number length", errorExpression);
			}
		}
	}

}
