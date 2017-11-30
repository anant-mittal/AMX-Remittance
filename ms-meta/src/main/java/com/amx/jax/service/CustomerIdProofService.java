package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ICustomerIdProofDAO;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class CustomerIdProofService extends AbstractService {

	@Autowired
	ICustomerIdProofDAO customerIdProofRepository;

	public void validateCustomerIdProofs(BigDecimal customerId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCustomerIdProofByCustomerId(customerId);
		if (idProofList.isEmpty()) {
			throw new GlobalException("NO_ID_PROOFS_AVAILABLE", "ID proofs not available, contact branch");
		}
	}

	public ApiResponse getCustomerImageValidation(BigDecimal customerId, BigDecimal identityTypeId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCustomerImageVAlidation(customerId,
				identityTypeId);
		ApiResponse response = getBlackApiResponse();
		if (idProofList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		} else {
			response.getData().getValues().addAll(idProofList);
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("idproof");
		return response;

	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
