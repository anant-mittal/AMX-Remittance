package com.amx.jax.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.meta.PaygErrorMaster;
import com.amx.jax.repository.IPayGErrorRepository;

/**
 * @author Shivaraj
 *
 */
@Component
public class PayGErrorDao {

	@Autowired
	private IPayGErrorRepository repo;
	
	
	public List<PaygErrorMaster> getPaygErrorList() {
		List<PaygErrorMaster> list = repo.findAll();
		return list ;
	}

}
