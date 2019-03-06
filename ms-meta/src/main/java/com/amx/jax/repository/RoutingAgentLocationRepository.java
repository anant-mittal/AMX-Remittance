package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.AgentBranchModel;
import com.amx.jax.dbmodel.RoutingAgentLocationView;

public interface RoutingAgentLocationRepository extends CrudRepository<RoutingAgentLocationView, String> {

	@Query("Select new com.amx.jax.dbmodel.AgentBranchModel(applicationCountryId, routingCountryId,serviceGroupId, routingBankId, currencyId, agentBankId, bankBranchId, routingBranchId, branchFullName) from RoutingAgentLocationView c where  applicationCountryId=?1 and routingCountryId=?2 and serviceGroupId=?3 and routingBankId=?4 and currencyId=?5 and agentBankId=?6 GROUP BY  applicationCountryId, routingCountryId,serviceGroupId, routingBankId, currencyId, agentBankId, bankBranchId, routingBranchId, branchFullName ORDER BY branchFullName ASC")
	List<AgentBranchModel> getAgentBranch(BigDecimal applicationCountryId, 
	                                      BigDecimal routingCountryId, 
	                                      BigDecimal serviceGroupId,
	                                      BigDecimal routingBankId, 
	                                      BigDecimal currencyId,
	                                      BigDecimal agentBankId);
}
