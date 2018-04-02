package com.amx.jax.trnx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ResponseData;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.cache.TransactionModel;

@Component
@SuppressWarnings("rawtypes")
public class BeneficiaryTrnxManager extends TransactionModel<BeneficiaryTrnxModel> {

	@Override
	public BeneficiaryTrnxModel init() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BeneficiaryTrnxModel commit() {
		BeneficiaryTrnxModel beneficiaryTrnxModel = get();
		return beneficiaryTrnxModel;
	}

	public ApiResponse saveBeneAccountTrnx(BeneAccountModel beneAccountModel) {
		BeneficiaryTrnxModel trnxModel = get();
		trnxModel.setBeneAccountModel(beneAccountModel);
		save(trnxModel);
		ApiResponse apiResponse = getBooleanApiResponse();

		return apiResponse;
	}

	public ApiResponse saveBeneAccountTrnx(BenePersonalDetailModel benePersonalDetailModel) {
		BeneficiaryTrnxModel trnxModel = get();
		trnxModel.setBenePersonalDetailModel(benePersonalDetailModel);
		save(trnxModel);
		ApiResponse apiResponse = getBooleanApiResponse();

		return apiResponse;
	}

	private ApiResponse getBooleanApiResponse() {
		ApiResponse response = new ApiResponse();
		ResponseData data = new ResponseData();
		List<Object> values = new ArrayList<Object>();
		data.setValues(values);
		response.setData(data);
		BooleanResponse booleanResponse = new BooleanResponse();
		response.getData().getValues().add(booleanResponse);
		response.getData().setType(booleanResponse.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

}
