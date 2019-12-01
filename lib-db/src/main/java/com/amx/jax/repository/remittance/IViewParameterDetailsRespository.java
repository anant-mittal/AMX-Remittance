package com.amx.jax.repository.remittance;
/**
 * Author	: Rabil
 * Date 	: 25/07/2019
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewParameterDetails;

public interface IViewParameterDetailsRespository extends CrudRepository<ViewParameterDetails, Serializable>{

	List<ViewParameterDetails> findByRecordIdAndCharField2AndNumericField1(String recordId,String bankCode,BigDecimal branchCode);
}
