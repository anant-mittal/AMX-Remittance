package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.AgentMasterModel;
import com.amx.jax.dbmodel.RoutingBankMasterView;
import com.amx.jax.dbmodel.ServiceProviderModel;

public interface RoutingBankMasterRepository extends CrudRepository<RoutingBankMasterView, String> {


//	@Query("Select c from RoutingBankMasterView c where  applicationCountryId=?1 and routingCountryId=?2 and serviceGroupId=?3 ORDER BY routingBankId ASC")
//	List<RoutingBankMasterView> getByCountryId(BigDecimal applicationCountryId, BigDecimal routingCountryId, BigDecimal serviceGroupId);

//	@Query("Select c from RoutingBankMasterView c where  applicationCountryId=?1 and routingCountryId=?2 and serviceGroupId=?3 and routingBankId=?4 and currencyId=?5 ORDER BY agentBankId ASC")
//	List<RoutingBankMasterView> getByCountryAndRoutingBankAndCurrencyId(BigDecimal applicationCountryId, BigDecimal routingCountryId, BigDecimal serviceGroupId,BigDecimal routingBankId, BigDecimal currencyId);

	
	@Query("Select new com.amx.jax.dbmodel.ServiceProviderModel(routingBankId,routingBankName,routingBankCode,applicationCountryId,routingCountryId,serviceGroupId) from RoutingBankMasterView c where  applicationCountryId=?1 and routingCountryId=?2 and serviceGroupId=?3 GROUP BY routingBankId,routingBankName,routingBankCode,applicationCountryId,routingCountryId,serviceGroupId ORDER BY routingBankName ASC") //Radhika
	List<ServiceProviderModel> getServiceProvider(BigDecimal applicationCountryId, BigDecimal routingCountryId, BigDecimal serviceGroupId);

	@Query("Select new com.amx.jax.dbmodel.AgentMasterModel(applicationCountryId, routingCountryId,serviceGroupId, routingBankId, currencyId, agentBankId, agentBankName, agentBankCode) from RoutingBankMasterView c where  applicationCountryId=?1 and routingCountryId=?2 and serviceGroupId=?3 and routingBankId=?4 and currencyId=?5 GROUP BY  applicationCountryId, routingCountryId,serviceGroupId, routingBankId, currencyId, agentBankId, agentBankName, agentBankCode ORDER BY agentBankName ASC")//Radhika
	List<AgentMasterModel> getAgentMaster(BigDecimal applicationCountryId, 
			                                                            BigDecimal routingCountryId, 
			                                                            BigDecimal serviceGroupId,
			                                                            BigDecimal routingBankId, 
			                                                            BigDecimal currencyId);
}
