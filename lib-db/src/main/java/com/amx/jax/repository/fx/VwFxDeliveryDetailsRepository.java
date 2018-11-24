package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.VwFxDeliveryDetailsModel;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;

public interface VwFxDeliveryDetailsRepository extends CrudRepository<VwFxDeliveryDetailsModel, Serializable> {

	List<VwFxDeliveryDetailsModel> findByDriverEmployeeIdAndDeliveryDate(BigDecimal driverEmployeeId, Date deliveryDate);

}
