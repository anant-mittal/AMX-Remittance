package com.amx.jax.userservice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.AddressProofModel;
import com.amx.jax.userservice.repository.AddressProofRepository;

@Component
public class AddressProofDao {
	@Autowired
	AddressProofRepository addressProofRepository;
	public List<AddressProofModel> getAddressProof(){
		List<AddressProofModel> addressProofModel = addressProofRepository.getAddressProof();
		return addressProofModel;
	}
}
