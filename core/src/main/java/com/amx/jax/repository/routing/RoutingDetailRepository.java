package com.amx.jax.repository.routing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.routing.RoutingDetails;

public interface RoutingDetailRepository extends CrudRepository<RoutingDetails, Serializable> {

	@Query("select distinct rd.agentBankId from RoutingDetails rd where rd.agentBankId is not null and  rd.isActive='Y' and rd.exCurrenyId= ?1"
			+ " and exCountryId= ?2")
	List<BigDecimal> getCashRoutingBanks(CurrencyMasterMdlv1 currencyId, CountryMaster countryMaster);
}
