package com.amx.jax.repository;
/**
 * @author rabil
 */

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.CurrencyOtherInformation;

public interface ICurrencyOtherInfoRepository extends CrudRepository<CurrencyOtherInformation, Serializable>{

	public CurrencyOtherInformation findByExCurrencyMasterAndIsActive(CurrencyMasterMdlv1 currId,String isActive);
}
