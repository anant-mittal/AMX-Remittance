package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;

public interface FxDeliveryDetailsRepository extends CrudRepository<FxDeliveryDetailsModel, Serializable>{
	
	public List<FxDeliveryDetailsModel> findByDeleviryDelSeqIdAndIsActive(BigDecimal deliveryDetailsId,String isActive);

}
