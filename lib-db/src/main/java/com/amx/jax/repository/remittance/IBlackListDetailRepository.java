package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.BlackListDetailModel;

public interface IBlackListDetailRepository extends CrudRepository<BlackListDetailModel, Serializable>{
	
	List<BlackListDetailModel> findByIdNumberAndIdType(String idNumber,String idType);

}
