package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewDeliveryMode;

public interface IViewDeliveryMode extends CrudRepository<ViewDeliveryMode, Serializable>{
	
	 List<ViewDeliveryMode> findByDeliveryModeIdAndLanguageId(BigDecimal deliveryModeId,BigDecimal languageId);

}
