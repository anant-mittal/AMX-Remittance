package com.amx.jax.trnx;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.cache.TransactionModel;

@Component
public class BeneficiaryTrnxManager extends TransactionModel<BeneficiaryTrnxModel> {

	@Override
	public BeneficiaryTrnxModel init() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BeneficiaryTrnxModel commit() {
		// TODO Auto-generated method stub
		return null;
	}

	public ApiResponse saveBeneBankTrnx(BigDecimal bankId) {

		BeneficiaryTrnxModel model = new BeneficiaryTrnxModel();
		model.setBankId(new BigDecimal(1));
		save(model);
		return null;
	}

}
