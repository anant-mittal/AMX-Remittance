package com.amx.jax.branchbene;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.AgentBranchModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.repository.RoutingAgentLocationRepository;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BeneAccountManager {

	@Autowired
	RoutingAgentLocationRepository routingAgentLocationRepository;
	@Autowired
	MetaData metaData;

	private static final Logger log = LoggerFactory.getLogger(BeneAccountManager.class);

	public BigDecimal getRoutingBankBranchId(BeneAccountModel beneAccountModel) {
		BigDecimal cashRoutingBankBranchId = null;
		List<AgentBranchModel> agentBranchList = routingAgentLocationRepository.getRoutingBankBranch(metaData.getCountryId(),
				beneAccountModel.getBeneficaryCountryId(), beneAccountModel.getServiceGroupId(), beneAccountModel.getServiceProviderId(),
				beneAccountModel.getCurrencyId(), beneAccountModel.getBankId(), beneAccountModel.getServiceProviderBranchId());
		if (agentBranchList.size() > 0) {
			cashRoutingBankBranchId = agentBranchList.get(0).getRoutingBranchId();
		}
		if (agentBranchList.size() > 1) {
			log.info("Found more than 1 routing bank branches for cash");
		}
		return cashRoutingBankBranchId;
	}

}
